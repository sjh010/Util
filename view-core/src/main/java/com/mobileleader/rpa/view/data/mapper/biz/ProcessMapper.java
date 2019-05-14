package com.mobileleader.rpa.view.data.mapper.biz;

import com.mobileleader.rpa.view.data.dto.DashboardProcessExecuteInfo;
import com.mobileleader.rpa.view.data.dto.DashboardProcessResultInfo;
import com.mobileleader.rpa.view.data.dto.ProcessInfo;
import com.mobileleader.rpa.view.model.form.ProcessSearchForm;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProcessMapper {

    Integer selectCount(ProcessSearchForm processSearchForm);

    List<ProcessInfo> selectProcessList(ProcessSearchForm processSearchForm);

    List<DashboardProcessExecuteInfo> selectProcessExecuteInfo();

    List<DashboardProcessResultInfo> selectProcessResultInfo();

    int updateConfigManagement(@Param("configManagementStatusCode") String configManagementStatusCode,
            @Param("processVersionSequence") Integer processVersionSequence);

    Integer selectProcessCount();

    int updateRemoveYn(@Param("processSequence") Integer processSequence, @Param("removeYn") String removeYn,
            @Param("removeId") String removeId);
}
