package com.mobileleader.rpa.view.data.mapper.biz;

import com.mobileleader.rpa.view.data.dto.TaskQueueByWork;
import com.mobileleader.rpa.view.model.form.TaskQueueSearchForm;
import java.util.List;

/**
 * Insert/Update/Delete 쿼리 추가 금지.<br>
 * API에서만 사용
 */
public interface TaskQueueMapper {

    /**
     * 프로세스 버전 시퀀스 기준으로 작업대기열을 조회.
     *
     * @param form 검색 폼
     * @return
     */
    List<TaskQueueByWork> selectByWork(TaskQueueSearchForm form);

    /**
     * 프로세스 버전 시퀀스 기준으로 작업대기열 개수를 조회.
     *
     * @param form 검색 폼
     * @return
     */
    Integer selectCountByWork(TaskQueueSearchForm form);
}
