package com.mobileleader.rpa.api.controller.task;

import com.mobileleader.rpa.api.schedule.ScheduleManager;
import com.mobileleader.rpa.api.schedule.trigger.JobTriggerInfo;
import com.mobileleader.rpa.repository.code.CodeAndConfigSupport;
import com.mobileleader.rpa.repository.user.UserInfoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/temporary")
public class TemporaryController {
    // TODO : 테스트 컨트롤러. 추후 삭제

    @Autowired
    private ScheduleManager scheduleManager;

    @GetMapping("/trigger")
    public JobTriggerInfo getTriggerInfo(@RequestParam("triggerName") String triggerName,
            @RequestParam("triggerGroup") String triggerGroup) {
        return scheduleManager.getJobTriggerInfo(triggerName, triggerGroup);
    }

    @PostMapping("/trigger/remove")
    public boolean removeTrigger(@RequestParam("triggerName") String triggerName,
            @RequestParam("triggerGroup") String triggerGroup) {
        return scheduleManager.removeTrigger(triggerName, triggerGroup);
    }

    @PostMapping("/repository")
    public void reloadRepository() {
        CodeAndConfigSupport.reload();
        UserInfoSupport.reload();
    }
}
