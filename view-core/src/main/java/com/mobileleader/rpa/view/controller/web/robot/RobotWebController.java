package com.mobileleader.rpa.view.controller.web.robot;

import com.mobileleader.rpa.auth.type.RpaAuthority;
import com.mobileleader.rpa.data.type.GroupCode;
import com.mobileleader.rpa.repository.data.mapper.common.CodeNameRepositoryMapper;
import com.mobileleader.rpa.view.model.form.RobotSearchForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/robot")
public class RobotWebController {

    @Autowired
    private CodeNameRepositoryMapper codeNameRepositoryMapper;

    /**
     * 로봇 관리 화면을 로드한다.
     *
     * @return
     */
    @RequestMapping(value = "")
    @Secured(RpaAuthority.SecuredRole.MANAGER_ROBOT_READ)
    public ModelAndView robot(@ModelAttribute("robotSearchForm") RobotSearchForm form) {
        ModelAndView mav = new ModelAndView();

        mav.addObject("robotStatusCodeList",
                codeNameRepositoryMapper.selectByGroupCode(GroupCode.ROBOT_STATUS_CODE.getCode()));
        mav.setViewName("/core/robot/robotList");

        return mav;
    }

}
