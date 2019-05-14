package com.mobileleader.rpa.view.controller.rest.authority;

import com.mobileleader.rpa.view.model.form.AuthoritySearchForm;
import com.mobileleader.rpa.view.model.response.AuthorityListResponse;
import com.mobileleader.rpa.view.service.authority.AuthorityWebService;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authority")
public class AutuorityRestController {

    @Autowired
    private AuthorityWebService authorityService;

    /**
     * 권한 리스트 가져오기.
     *
     * @param form 검색 폼
     * @return 권한 리스트
     */
    @PostMapping("/list")
    public ResponseEntity<AuthorityListResponse> getAuthorityList(@RequestBody @Valid AuthoritySearchForm form,
            BindingResult bindingResult) {
        return new ResponseEntity<AuthorityListResponse>(authorityService.getAuthorityList(form), HttpStatus.OK);
    }
}
