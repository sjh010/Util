package com.mobileleader.rpa.api.repository;

import com.mobileleader.rpa.api.data.mapper.base.AuthenticationMapper;
import com.mobileleader.rpa.api.exception.RpaApiError;
import com.mobileleader.rpa.api.exception.RpaApiException;
import com.mobileleader.rpa.auth.type.AuthenticationType;
import com.mobileleader.rpa.data.dto.base.Authentication;
import com.mobileleader.rpa.repository.file.FileReadWriteSupport;
import com.mobileleader.rpa.utils.cipher.RpaRsaCipher;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Repository
public class RsaKeyRepository {

    @Autowired
    private RpaRsaCipher rpaRsaCipher;

    @Autowired
    private AuthenticationMapper authenticationMapper;

    @Autowired
    private FileReadWriteSupport fileReadWriteSupport;

    /**
     * Insert Authentication.
     *
     * @param userSequence userSequence
     * @param privateKey privateKey
     */
    @Transactional
    public void storePrivateKey(Integer userSequence, PrivateKey privateKey) {
        deletePrivateKey(userSequence);
        String uuid = writePrivateKeyFile(privateKey);
        Authentication authentication = new Authentication();
        authentication.setAuthenticationTargetSequence(userSequence);
        authentication.setUuid(uuid);
        authentication.setAuthenticationTypeCode(AuthenticationType.STUDIO_SIGNING_IN.getCode());
        authenticationMapper.insert(authentication);
    }

    private String writePrivateKeyFile(PrivateKey privateKey) {
        String uuid = UUID.randomUUID().toString();
        try {
            fileReadWriteSupport.writeTempFile(privateKey.getEncoded(), uuid);
        } catch (IOException e) {
            throw new RpaApiException(RpaApiError.FILE_IO_ERROR, e);
        }
        return uuid;
    }

    /**
     * Get PrivateKey from Authentication.
     *
     * @param userSequence userSequence
     * @return {@link PrivateKey}
     */
    @Transactional
    public PrivateKey getPrivateKey(Integer userSequence) {
        Authentication authentication =
                authenticationMapper.selectByPrimaryKey(AuthenticationType.STUDIO_SIGNING_IN.getCode(), userSequence);
        Assert.notNull(authentication, "authentication not found");
        byte[] privateKeyFileBytes = null;
        try {
            privateKeyFileBytes = fileReadWriteSupport.readTempFile(authentication.getUuid());
        } catch (IOException e) {
            throw new RpaApiException(RpaApiError.FILE_IO_ERROR, e);
        }
        try {
            return rpaRsaCipher.getPrivateKeyFromEncodedKey(privateKeyFileBytes);
        } catch (InvalidKeySpecException e) {
            throw new RpaApiException(RpaApiError.INTERNAL_SERVER_ERROR, e);
        }
    }

    /**
     * Delete Authentication.
     *
     * @param userSequence userSequence
     */
    @Transactional
    public void deletePrivateKey(Integer userSequence) {
        Authentication authentication =
                authenticationMapper.selectByPrimaryKey(AuthenticationType.STUDIO_SIGNING_IN.getCode(), userSequence);
        if (authentication != null) {
            try {
                fileReadWriteSupport.deleteTempFile(authentication.getUuid());
            } catch (IOException e) {
                throw new RpaApiException(RpaApiError.FILE_IO_ERROR, e);
            }
            authenticationMapper.deleteByPrimaryKey(AuthenticationType.STUDIO_SIGNING_IN.getCode(), userSequence);
        }
    }

    /**
     * 로그인 완료 처리. 키 파일 삭제. Authentication 상태 SIGNING_IN -> SIGNED_IN 으로 변경.
     *
     * @param userSequence userSequence
     * @return updated count
     */
    @Transactional
    public String completeSignedIn(Integer userSequence) {
        Authentication authentication =
                authenticationMapper.selectByPrimaryKey(AuthenticationType.STUDIO_SIGNING_IN.getCode(), userSequence);
        if (authentication == null) {
            throw new RpaApiException(RpaApiError.LOGIN_FAILURE);
        }
        Authentication oldAuthentication =
                authenticationMapper.selectByPrimaryKey(AuthenticationType.STUDIO_SIGNED_IN.getCode(), userSequence);
        if (oldAuthentication != null) {
            authenticationMapper.deleteByPrimaryKey(oldAuthentication.getAuthenticationTypeCode(),
                    oldAuthentication.getAuthenticationTargetSequence());
        }
        try {
            fileReadWriteSupport.deleteTempFile(authentication.getUuid());
        } catch (IOException e) {
            throw new RpaApiException(RpaApiError.LOGIN_FAILURE, e);
        }
        authenticationMapper.updateAuthenticationTypeCodeByPrimaryKey(AuthenticationType.STUDIO_SIGNING_IN.getCode(),
                userSequence, AuthenticationType.STUDIO_SIGNED_IN.getCode());
        return authentication.getUuid();
    }
}
