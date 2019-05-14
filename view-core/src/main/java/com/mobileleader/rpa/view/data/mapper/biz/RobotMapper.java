package com.mobileleader.rpa.view.data.mapper.biz;

import com.mobileleader.rpa.data.dto.biz.Robot;
import com.mobileleader.rpa.view.data.dto.DashboardRobotErrorInfo;
import com.mobileleader.rpa.view.data.dto.DashboardRobotInfo;
import com.mobileleader.rpa.view.data.dto.RobotList;
import com.mobileleader.rpa.view.data.dto.RobotName;
import com.mobileleader.rpa.view.model.form.RobotAddForm;
import com.mobileleader.rpa.view.model.form.RobotSearchForm;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RobotMapper {
    int selectCount(RobotSearchForm form);

    List<RobotList> selectRobotList(RobotSearchForm form);

    int insert(Robot record);

    int updateByPrimaryKey(Robot record);

    int removeRobot(@Param("robotSequence") Integer robotSequence, @Param("userId") String userId);

    List<String> selectPcNameList();

    Robot selectByPrimaryKey(@Param("robotSequence") Integer robotSequence);

    DashboardRobotInfo selectRobotStatusInfo();

    List<DashboardRobotErrorInfo> selectRobotErrorInfo();

    /**
     * 활성화된 업무에 할당되지 않은 로봇 목록 조회.
     *
     * @param workSequence 편집중인 업무 시퀀스
     * @return
     */
    List<RobotName> selectRobotNotAssigned(@Param("workSequence") Integer workSequence);

    /**
     * 작업대기열의 로봇이름 조회.
     *
     * @param workSequence 작업 시퀀스
     * @return
     */
    List<RobotName> selectTaskQueueRobotNameByWorkSequence(@Param("workSequence") Integer workSequence);

    /**
     * 업무할당이 가능한 로봇목록 조회. + 업무에 할당된 로봇.
     *
     * @param workSequence 업무 순번
     * @return
     */
    List<RobotName> selectAvailableTargetRobot(@Param("workSequence") Integer workSequence);

    Integer selectByPcNameAndIp(RobotAddForm form);

    Integer selectByRobotName(@Param("robotName") String robotName);

    /**
     * 작업중인 로봇 조회.
     *
     * @param robotSequenceList 대상 로봇 순번
     * @return
     */
    List<Integer> selectWorkingRobotSequence(@Param("robotSequenceList") List<Integer> robotSequenceList);

    /**
     * 작업중인 로봇 개수 조회.
     *
     * @param workSequenceList 업무 순번 목록
     * @return
     */
    int selectWorkingRobotCount(@Param("workSequenceList") List<Integer> workSequenceList);

    /**
     * 다른 업무에 할당된 로봇순번 목록 조회.
     *
     * @param workSequenceList 활성화하려는 업무순번 목록
     * @return
     */
    List<Integer> selectOtherWorkAssignedRobotList(@Param("workSequenceList") List<Integer> workSequenceList);

    /**
     * 다른 업무에 할당된 로봇 이름 조회.
     *
     * @param workSequence 활성화하려는 업무순번
     * @return
     */
    List<String> selectOtherWorkAssignedRobotName(@Param("workSequence") Integer workSequence,
            @Param("robotSequenceList") List<Integer> robotSequenceList);
}
