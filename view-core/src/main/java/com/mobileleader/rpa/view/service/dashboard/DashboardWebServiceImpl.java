package com.mobileleader.rpa.view.service.dashboard;

import com.mobileleader.rpa.auth.type.RpaAuthority;
import com.mobileleader.rpa.view.data.mapper.biz.ProcessMapper;
import com.mobileleader.rpa.view.data.mapper.biz.RobotMapper;
import com.mobileleader.rpa.view.data.mapper.biz.WorkMapper;
import com.mobileleader.rpa.view.model.response.DashboardResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardWebServiceImpl implements DashboardWebService {

    @Autowired
    private RobotMapper robotMapper;

    @Autowired
    private ProcessMapper processMapper;

    @Autowired
    private WorkMapper workMapper;

    @Override
    @Secured({ RpaAuthority.SecuredRole.MANAGER_DASHBOARD_READ })
    @Transactional
    public DashboardResponse getDashboardInfo() {

        DashboardResponse response = new DashboardResponse();

        response.setProcessCount(processMapper.selectProcessCount());
        response.setActivationWorkCount(workMapper.selectActivationWorkCount());
        response.setRobotInfo(robotMapper.selectRobotStatusInfo());
        response.setProcessList(processMapper.selectProcessExecuteInfo());
        response.setProcessResultList(processMapper.selectProcessResultInfo());
        response.setRobotErrorList(robotMapper.selectRobotErrorInfo());

        return response;
    }

}
