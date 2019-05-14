package com.mobileleader.rpa.view.service.taksqueue;

import com.mobileleader.rpa.auth.type.RpaAuthority;
import com.mobileleader.rpa.view.data.dto.RobotName;
import com.mobileleader.rpa.view.data.dto.TaskQueueByWork;
import com.mobileleader.rpa.view.data.mapper.biz.RobotMapper;
import com.mobileleader.rpa.view.data.mapper.biz.TaskQueueMapper;
import com.mobileleader.rpa.view.model.form.TaskQueueSearchForm;
import com.mobileleader.rpa.view.model.response.TaskQueueFileterResponse;
import com.mobileleader.rpa.view.model.response.TaskQueueListResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskQueueWebServiceImpl implements TaskQueueWebService {

    // private static final Logger logger = LoggerFactory.getLogger(TaskQueueWebServiceImpl.class);

    @Autowired
    private TaskQueueMapper taskQueueMapper;

    @Autowired
    private RobotMapper robotMapper;

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_WORK_ASSIGNMENT_READ)
    @Transactional
    public TaskQueueListResponse getTaskQueueList(TaskQueueSearchForm form) {
        TaskQueueListResponse response = new TaskQueueListResponse();

        Integer count = taskQueueMapper.selectCountByWork(form);
        form.resetPageNoIfNotValid(count);
        response.setCount(count);
        if (count > 0) {
            List<TaskQueueByWork> taskQueueList = taskQueueMapper.selectByWork(form);
            response.setTaskQueueList(taskQueueList);
        }
        response.setForm(form);
        return response;

    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_WORK_ASSIGNMENT_READ)
    @Transactional
    public TaskQueueFileterResponse getTaskQueueFilterList(Integer workSequence) {
        TaskQueueFileterResponse response = new TaskQueueFileterResponse();

        List<RobotName> robotNameList = robotMapper.selectTaskQueueRobotNameByWorkSequence(workSequence);
        response.setRobotNameList(robotNameList);

        return response;
    }
}
