package com.mobileleader.rpa.view.data.mapper.biz;

import com.mobileleader.rpa.data.dto.biz.Work;
import com.mobileleader.rpa.view.data.dto.WorkAssignedRobot;
import com.mobileleader.rpa.view.data.dto.WorkAssignmentList;
import com.mobileleader.rpa.view.data.dto.WorkModify;
import com.mobileleader.rpa.view.data.dto.WorkRegist;
import com.mobileleader.rpa.view.model.form.WorkAssignmentSearchForm;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WorkMapper {

    Work selectByPrimaryKey(Integer workSequence);

    List<Work> selectByProcessVersionSequence(@Param("processVersionSequence") Integer processVersionSequence);

    /**
     * 업무를 등록함.
     *
     * @param workRegist 업무.
     * @return
     */
    Integer registWork(WorkRegist workRegist);

    int updateRemoveStatus(Work work);

    /**
     * 업무 활성여부 상태를 변경한다.
     *
     * @param workSequence 업무 순번
     * @param activationYn 활성화여부 (Y|N)
     * @param modifyId 수정자 ID
     * @return
     */
    int updateActivationYn(@Param("workSequence") Integer workSequence, @Param("activationYn") String activationYn,
            @Param("modifyId") String modifyId);

    Integer selectActivationWorkCount();

    int selectCount(WorkAssignmentSearchForm form);

    List<WorkAssignmentList> selectWorkAssignmentList(WorkAssignmentSearchForm form);

    /**
     * 작업에 할당된 로봇 조회.
     *
     * @param workSequence 작업 순번
     * @return
     */
    List<WorkAssignedRobot> selectWorkAssignedRobot(@Param("workSequence") Integer workSequence);

    /**
     * 업무를 수정함.
     *
     * @param workModify 업무 수정.
     */
    int updateWork(WorkModify workModify);

    /**
     * 로봇이 가진 업무개수 조회(비활성/활성 포함).
     *
     * @param robotSequence 로봇 순번
     * @return
     */
    int selectWorkCountByRobotSequence(Integer robotSequence);


}
