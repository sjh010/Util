package com.mobileleader.rpa.api.service.authetication;

import com.mobileleader.rpa.api.data.mapper.base.AuthenticationMapper;
import com.mobileleader.rpa.api.data.mapper.biz.RobotMapper;
import com.mobileleader.rpa.api.model.request.RobotAuthenticationRequest;
import com.mobileleader.rpa.api.model.response.RobotAuthenticationResponse;
import com.mobileleader.rpa.api.support.BizLogSupport;
import com.mobileleader.rpa.auth.service.authentication.token.AuthenticationTokenDetails;
import com.mobileleader.rpa.auth.type.AuthenticationType;
import com.mobileleader.rpa.auth.type.RpaAuthority;
import com.mobileleader.rpa.data.dto.base.Authentication;
import com.mobileleader.rpa.data.dto.biz.Robot;
import com.mobileleader.rpa.data.type.ConfigVariableName;
import com.mobileleader.rpa.data.type.RobotLogStatusCode;
import com.mobileleader.rpa.data.type.RobotLogTypeCode;
import com.mobileleader.rpa.repository.code.CodeAndConfigSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class RobotAuthenticationServiceImpl implements RobotAuthenticationService {

    @Autowired
    private RobotMapper robotMapper;

    @Autowired
    private AuthenticationMapper authenticationMapper;

    @Autowired
    private BizLogSupport bizLogSupport;

    @Override
    @Transactional
    public RobotAuthenticationResponse getAuthentication(RobotAuthenticationRequest request) {
        Robot robot = robotMapper.selectByIpAddressAndPcName(request.getIpAddress(), request.getPcName());
        Assert.notNull(robot, "Unknown robot");
        Authentication authentication =
                authenticationMapper.selectByPrimaryKey(AuthenticationType.ROBOT.getCode(), robot.getRobotSequence());
        Assert.notNull(authentication, "authentication not found");
        AuthenticationTokenDetails tokenDetails = new AuthenticationTokenDetails.Builder()
                .authenticationSequence(robot.getRobotSequence()).authenticationType(AuthenticationType.ROBOT)
                .commaSeparatedStringRoles(RpaAuthority.ROBOT_API.getAuthority()).uuid(authentication.getUuid())
                .userId(robot.getPcIpAddress()).userName(robot.getRobotName()).build();
        bizLogSupport.addRobotLog(RobotLogTypeCode.CONNECT, RobotLogStatusCode.SUCCESS, robot.getRobotSequence(),
                robot.getRobotName(), null);
        return new RobotAuthenticationResponse.Builder()
                .authenticationToken(AuthenticationTokenDetails.serialize(tokenDetails))
                .healthCheckInterval(CodeAndConfigSupport
                        .getConfigValue(ConfigVariableName.CLIENT_ROBOT_HEALTH_CHECK_INTERVAL.getCode()))
                .robotName(robot.getRobotName()).build();
    }
}
