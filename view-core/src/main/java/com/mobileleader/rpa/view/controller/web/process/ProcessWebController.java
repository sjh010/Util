package com.mobileleader.rpa.view.controller.web.process;

import com.mobileleader.rpa.auth.type.RpaAuthority;
import com.mobileleader.rpa.view.service.process.ProcessWebService;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/process")
public class ProcessWebController {

    @Autowired
    private ProcessWebService processService;

    /**
     * 프로세스 관리 페이지 로드.
     *
     * @return
     */
    @GetMapping
    @Secured(RpaAuthority.SecuredRole.MANAGER_PROCESS_READ)
    public ModelAndView getProcessManagementPage() {

        ModelAndView mv = new ModelAndView();

        mv.addObject("configManagementStatusCodeList", processService.getConfigManagementStatusCodeList());
        mv.setViewName("/core/process/processList");

        return mv;
    }

    /**
     * 프로세스 파일 다운로드.
     *
     * @param sequenceList 프로세스 버전 시퀀스 리스트
     * @param request request
     * @param response response
     */
    @GetMapping(value = "/download")
    public void downloadProcess(@RequestParam("sequenceList") String sequenceList, HttpServletRequest request,
            HttpServletResponse response) {

        List<Integer> processVersionSequenceList = new ArrayList<Integer>();

        for (String processVersionSequence : sequenceList.split(",")) {
            processVersionSequenceList.add(Integer.parseInt(processVersionSequence));
        }

        processService.downloadProcess(processVersionSequenceList, request, response);
    }
}
