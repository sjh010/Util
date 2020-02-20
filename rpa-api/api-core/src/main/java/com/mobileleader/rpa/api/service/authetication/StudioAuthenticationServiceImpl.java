package com.mobileleader.rpa.api.service.authetication;

import com.mobileleader.rpa.api.data.mapper.base.AuthenticationMapper;
import com.mobileleader.rpa.api.data.mapper.base.AuthorityFunctionMapper;
import com.mobileleader.rpa.api.data.mapper.base.UserMapper;
import com.mobileleader.rpa.api.exception.RpaApiError;
import com.mobileleader.rpa.api.exception.RpaApiException;
import com.mobileleader.rpa.api.model.request.StudioAuthenticationRequest;
import com.mobileleader.rpa.api.model.response.RsaPublicKeyResponse;
import com.mobileleader.rpa.api.model.response.StudioAuthenticationResponse;
import com.mobileleader.rpa.api.repository.RsaKeyRepository;
import com.mobileleader.rpa.api.support.AuthenticationTokenSupport;
import com.mobileleader.rpa.api.support.BizLogSupport;
import com.mobileleader.rpa.auth.service.authentication.token.AuthenticationTokenDetails;
import com.mobileleader.rpa.auth.type.AuthenticationType;
import com.mobileleader.rpa.auth.type.EncryptTypeCode;
import com.mobileleader.rpa.auth.type.RpaAuthority;
import com.mobileleader.rpa.data.dto.base.AuthorityFunction;
import com.mobileleader.rpa.data.dto.base.User;
import com.mobileleader.rpa.data.type.StudioLogStatusCode;
import com.mobileleader.rpa.data.type.StudioLogTypeCode;
import com.mobileleader.rpa.utils.cipher.RpaRsaCipher;
import com.mobileleader.rpa.utils.cipher.RpaRsaCipher.EncryptConvertType;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudioAuthenticationServiceImpl implements StudioAuthenticationService {

    @Autowired
    private RpaRsaCipher rpaRsaCipher;

    @Autowired
    private RsaKeyRepository rsaKeyRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthorityFunctionMapper authorityFunctionMapper;

    @Autowired
    private AuthenticationMapper authenticationMapper;

    @Autowired
    private BizLogSupport bizLogSupport;

    @Override
    @Transactional
    public StudioAuthenticationResponse create(StudioAuthenticationRequest request) {
        Integer userSequence = userMapper.selectUserSequenceByUserId(request.getUserId());
        User user = userMapper.selectByPrimaryKey(userSequence);
        if (user == null) {
            throw new RpaApiException(RpaApiError.INVALID_USER_ID_OR_PASSWORD);
        }
        if (!isMatchedPassword(userSequence, user.getUserPassword(), request)) {
            throw new RpaApiException(RpaApiError.INVALID_USER_ID_OR_PASSWORD);
        }
        String uuid = rsaKeyRepository.completeSignedIn(userSequence);
        String authenticationToken = AuthenticationTokenDetails.serialize(
                new AuthenticationTokenDetails.Builder().authenticationType(AuthenticationType.STUDIO_SIGNED_IN)
                        .commaSeparatedStringRoles(getAuthority(user.getAuthoritySequence()))
                        .authenticationSequence(user.getUserSequence()).uuid(uuid).userId(user.getUserId())
                        .userName(user.getUserName()).build());
        bizLogSupport.addStudioLog(StudioLogTypeCode.LOGIN, StudioLogStatusCode.SUCCESS, user.getUserId(),
                user.getUserName(), null);
        return new StudioAuthenticationResponse.Builder().authenticationToken(authenticationToken)
                .userName(user.getUserName()).build();
    }

    @Override
    @Transactional
    public RsaPublicKeyResponse createPublicKey(StudioAuthenticationRequest request) {
        Integer userSequence = userMapper.selectUserSequenceByUserId(request.getUserId());
        if (userSequence == null) {
            throw new RpaApiException(RpaApiError.INVALID_USER_ID_OR_PASSWORD);
        }
        KeyPair keyPair = rpaRsaCipher.getKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        rsaKeyRepository.storePrivateKey(userSequence, privateKey);
        return new RsaPublicKeyResponse(rpaRsaCipher.getRsaPublicKeySpec(publicKey));
    }

    @Override
    @Transactional
    @Secured(RpaAuthority.SecuredRole.STUDIO_API)
    public void delete() {
        AuthenticationType authenticationType = AuthenticationType.STUDIO_SIGNED_IN;
        Integer authenticationSequence = AuthenticationTokenSupport.getAuthenticationSequence(authenticationType);
        if (authenticationSequence != null) {
            authenticationMapper.deleteByPrimaryKey(authenticationType.getCode(), authenticationSequence);
            bizLogSupport.addStudioLog(StudioLogTypeCode.LOGOUT, StudioLogStatusCode.SUCCESS, null);
        }
    }

    private String getAuthority(Short authoritySequence) {
        AuthorityFunction authorityFunction =
                authorityFunctionMapper.selectByPrimaryKey(authoritySequence, RpaAuthority.STUDIO_API.getAuthority());
        if (authorityFunction != null) {
            return authorityFunction.getFunctionCode();
        } else {
            throw new RpaApiException(RpaApiError.AUTHORITY_ERROR);
        }
    }

    private boolean isMatchedPassword(Integer userSequence, String encodedPassword,
            StudioAuthenticationRequest request) {
        String decryptedPassword = null;
        if (EncryptTypeCode.PLAIN.getCode().equals(request.getEncryptTypeCode())) {
            decryptedPassword = request.getPassword();
        } else {
            decryptedPassword = decryptPassword(userSequence, request);
        }
        return passwordEncoder.matches(decryptedPassword, encodedPassword);
    }

    private String decryptPassword(Integer userSequence, StudioAuthenticationRequest request) {
        PrivateKey privateKey = rsaKeyRepository.getPrivateKey(userSequence);
        String decryptedPassword = null;
        try {
            decryptedPassword = rpaRsaCipher.decrypt(privateKey, request.getPassword(), EncryptConvertType.BASE64);
        } catch (GeneralSecurityException e) {
            throw new RpaApiException(RpaApiError.LOGIN_FAILURE, e);
        }
        return decryptedPassword;
    }
}
