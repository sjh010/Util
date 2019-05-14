package com.mobileleader.rpa.view.controller.rest.dashboard;

import com.mobileleader.rpa.view.model.response.DashboardResponse;
import com.mobileleader.rpa.view.service.dashboard.DashboardWebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardRestController {

    @Autowired
    DashboardWebService dashboardService;

    /**
     * 대시보드 정보 조회.
     *
     * @return {@link DashboardResponse}
     */
    @PostMapping()
    public ResponseEntity<DashboardResponse> getDashboardInfo() {
        return new ResponseEntity<DashboardResponse>(dashboardService.getDashboardInfo(), HttpStatus.OK);
    }
}
