package com.mobileleader.rpa.view.service.taksqueue;

import com.mobileleader.rpa.view.model.form.TaskQueueSearchForm;
import com.mobileleader.rpa.view.model.response.TaskQueueFileterResponse;
import com.mobileleader.rpa.view.model.response.TaskQueueListResponse;

public interface TaskQueueWebService {

    /**
     * 업무시퀀스 기준 작업대기열 조회.
     *
     * @param form 검색 폼
     * @return
     */
    public TaskQueueListResponse getTaskQueueList(TaskQueueSearchForm form);

    /**
     * 업무 시퀀스 기준 검색 필터 조회.
     *
     * @param workSequence 작업 시퀀스
     * @return
     */
    public TaskQueueFileterResponse getTaskQueueFilterList(Integer workSequence);


}
