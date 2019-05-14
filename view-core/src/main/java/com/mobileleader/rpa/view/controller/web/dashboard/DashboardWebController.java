package com.mobileleader.rpa.view.controller.web.dashboard;

import com.mobileleader.rpa.utils.datetime.DateTimeUtils;
import com.mobileleader.rpa.view.service.dashboard.DashboardWebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/dashboard")
public class DashboardWebController {

    @Autowired
    DashboardWebService dashboardService;

    /**
     * 대시보드 화면 로드.
     *
     * @return
     */
    @GetMapping()
    public ModelAndView getDashboardPage() {
        ModelAndView mv = new ModelAndView();

        mv.addObject("today", DateTimeUtils.getNowString("yyyy.MM.dd HH:mm"));
        mv.setViewName("/core/dashboard/dashboard");

        return mv;
    }

}
