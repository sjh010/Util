package com.mobileleader.rpa.view.data.mapper.biz;

import com.mobileleader.rpa.data.dto.biz.ProcessVersion;
import com.mobileleader.rpa.view.data.dto.ProcessNameVersion;
import com.mobileleader.rpa.view.model.form.ProcessHistorySearchForm;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ProcessVersionMapper {

    ProcessVersion selectByPrimaryKey(Integer processVersionSequence);

    int selectCount(ProcessHistorySearchForm processHistorySearchForm);

    List<ProcessVersion> selectProcessHistory(ProcessHistorySearchForm processHistorySearchForm);

    int updateRemoveStatus(@Param("processVersionSequence") Integer processVersionSequence,
            @Param("removeYn") String removeYn, @Param("removeId") String removeId);

    Integer updateActivationYn(@Param("activationYn") String activationYn,
            @Param("processVersionSequence") Integer processVersionSequence);

    int insertProcessVersion(ProcessVersion processVersion);

    /**
     * (선택한 업무 순번을 포함한) 선택가능한 프로세스 목록.
     *
     * @param workSequence 업무순번
     * @return
     */
    List<ProcessNameVersion> selectActiveProcess(@Param("workSequence") Integer workSequence);

    /**
     * 신규 추가가능한(사용중이고, 업무가 미할당된) 프로세스/버전 개수 조회.
     *
     * @return
     */
    int selectActiveProcessCount();

    int selectNextMajorVersion(Integer processSequence);

    /**
     * 프로세스 명과 버전 조회.
     *
     * @param processVersionSequence 프로세스 버전 순번
     * @return
     */
    ProcessNameVersion selectProcessNameVersionByPrimaryKey(int processVersionSequence);

    /**
     * 프로세스 순번 조회.
     *
     * @param processVersionSequence 프로세스버전 순번
     * @return
     */
    Integer selectProcessSequence(Integer processVersionSequence);

    Integer selectNotDeletedProcessCount(Integer processSequence);

}
