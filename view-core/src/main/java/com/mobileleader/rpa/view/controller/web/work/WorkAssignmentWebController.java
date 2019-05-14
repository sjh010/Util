package com.mobileleader.rpa.view.controller.web.work;

import com.mobileleader.rpa.auth.type.RpaAuthority;
import com.mobileleader.rpa.view.model.form.WorkAssignmentSearchForm;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/work")
public class WorkAssignmentWebController {

    /**
     * 업무할당 관리 화면을 로드한다.
     *
     * @return
     */
    @GetMapping(value = "")
    @Secured(RpaAuthority.SecuredRole.MANAGER_WORK_ASSIGNMENT_READ)
    public ModelAndView workAssignment(@ModelAttribute("workAssignmentSearchForm") WorkAssignmentSearchForm form) {
        ModelAndView mav = new ModelAndView();

        mav.setViewName("/core/work/workAssignmentList");

        return mav;
    }

    /**
     * 업무 할당 추가 화면을 로드한다.
     *
     * @return
     */
    @GetMapping(value = "/add")
    @Secured(RpaAuthority.SecuredRole.MANAGER_WORK_ASSIGNMENT_MODIFY)
    public ModelAndView addWorkAssignment() {
        ModelAndView mav = new ModelAndView();

        mav.setViewName("/core/work/workAssignmentAddPopup");

        return mav;
    }

    /**
     * 업무 할당 수정 화면을 로드한다.
     *
     * @return
     */
    @GetMapping(value = "/modify/{workSequence}")
    @Secured(RpaAuthority.SecuredRole.MANAGER_WORK_ASSIGNMENT_MODIFY)
    public ModelAndView modifyWorkAssignment(@PathVariable int workSequence) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/core/work/workAssignmentModifyPopup");
        mav.addObject("workSequence", workSequence);
        return mav;
    }

    /**
     * 업무 작업 내역 화면을 로드한다.
     *
     * @return
     */
    @GetMapping(value = "/history/{workSequence}")
    @Secured(RpaAuthority.SecuredRole.MANAGER_WORK_ASSIGNMENT_READ)
    public ModelAndView historyWorkAssignment(@PathVariable int workSequence) {
        ModelAndView mav = new ModelAndView();

        mav.setViewName("/core/work/workAssignmentHistoryPopup");
        mav.addObject("workSequence", workSequence);

        return mav;
    }
}
