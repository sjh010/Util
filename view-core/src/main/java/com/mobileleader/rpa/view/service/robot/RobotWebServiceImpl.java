package com.mobileleader.rpa.view.service.robot;

import com.mobileleader.rpa.auth.data.mapper.AuthenticationTokenMapper;
import com.mobileleader.rpa.auth.type.AuthenticationType;
import com.mobileleader.rpa.auth.type.RpaAuthority;
import com.mobileleader.rpa.data.dto.base.Authentication;
import com.mobileleader.rpa.data.dto.biz.Robot;
import com.mobileleader.rpa.data.dto.biz.RobotLog;
import com.mobileleader.rpa.data.type.GroupCode;
import com.mobileleader.rpa.data.type.RobotLogStatusCode;
import com.mobileleader.rpa.data.type.RobotLogTypeCode;
import com.mobileleader.rpa.data.type.RobotStatusCode;
import com.mobileleader.rpa.repository.file.FileGroupRepository;
import com.mobileleader.rpa.repository.file.ZipFileInfo;
import com.mobileleader.rpa.utils.datetime.DateTimeUtils;
import com.mobileleader.rpa.view.data.dto.RobotLogList;
import com.mobileleader.rpa.view.data.mapper.biz.RobotLogMapper;
import com.mobileleader.rpa.view.data.mapper.biz.RobotMapper;
import com.mobileleader.rpa.view.data.mapper.biz.WorkMapper;
import com.mobileleader.rpa.view.data.mapper.common.CommonCodeMapper;
import com.mobileleader.rpa.view.exception.RpaViewError;
import com.mobileleader.rpa.view.exception.RpaViewException;
import com.mobileleader.rpa.view.model.form.RobotAddForm;
import com.mobileleader.rpa.view.model.form.RobotLogSearchForm;
import com.mobileleader.rpa.view.model.form.RobotSearchForm;
import com.mobileleader.rpa.view.model.response.RobotAddCheckResponse;
import com.mobileleader.rpa.view.model.response.RobotListResponse;
import com.mobileleader.rpa.view.model.response.RobotLogFileterResponse;
import com.mobileleader.rpa.view.model.response.RobotLogListResponse;
import com.mobileleader.rpa.view.service.excel.ExcelDownloadInfo;
import com.mobileleader.rpa.view.service.excel.WorkbookRepository;
import com.mobileleader.rpa.view.support.UserDetailsSupport;
import com.mobileleader.rpa.view.util.FileDownloadUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Service
public class RobotWebServiceImpl implements RobotWebService {

    private static final Logger logger = LoggerFactory.getLogger(RobotWebServiceImpl.class);

    @Autowired
    private RobotMapper robotMapper;

    @Autowired
    private RobotLogMapper robotLogMapper;

    @Autowired
    private AuthenticationTokenMapper authenticationTokenMapper;

    @Autowired
    private CommonCodeMapper commonCodeMapper;

    @Autowired
    private WorkMapper workMapper;

    @Autowired
    private FileGroupRepository fileGroupRepository;

    @Autowired
    private WorkbookRepository workBookRepository;

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_ROBOT_READ)
    @Transactional
    public RobotListResponse getRobotList(RobotSearchForm form) {
        RobotListResponse response = new RobotListResponse();
        int count = robotMapper.selectCount(form);
        response.setTotalCount(count);
        form.resetPageNoIfNotValid(count);

        if (count > 0) {
            response.setList(robotMapper.selectRobotList(form));
        }
        response.setForm(form);

        return response;
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_ROBOT_READ)
    @Transactional
    public List<String> getPcNameList() {
        return robotMapper.selectPcNameList();
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_ROBOT_MODIFY)
    @Transactional
    public int addRobot(RobotAddForm form) {
        Robot robot = new Robot();
        robot.setRobotName(form.getRobotName());
        robot.setPcIpAddress(form.getPcIpAddress());
        robot.setPcName(form.getPcName());
        robot.setRegisterId(SecurityContextHolder.getContext().getAuthentication().getName());
        robot.setRobotStatusCode(RobotStatusCode.REGIST.getCode());
        robot.setRemoveYn("N");

        int result = robotMapper.insert(robot);
        logger.info("addRobot() robotSequence : {}", robot.getRobotSequence());
        if (result > 0) {
            // 등록한 로봇을 권한 테이블에 등록해준다.
            String uuid = UUID.randomUUID().toString();
            Authentication authentication = new Authentication();
            authentication.setAuthenticationTypeCode(AuthenticationType.ROBOT.getCode());
            authentication.setAuthenticationTargetSequence(robot.getRobotSequence());
            authentication.setUuid(uuid);
            authenticationTokenMapper.insertAuthentication(authentication);
            insertRobotLog(robot.getRobotSequence(), robot.getRobotName(), RobotLogTypeCode.REGIST,
                    RobotLogStatusCode.SUCCESS, null);
        }
        return result;
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_ROBOT_MODIFY)
    @Transactional
    public int deleteRobot(List<Integer> robotSequenceList) {
        int result = 0;
        for (Integer robotSequence : robotSequenceList) {
            Robot robot = robotMapper.selectByPrimaryKey(robotSequence);

            if (robot != null) {

                int workCount = workMapper.selectWorkCountByRobotSequence(robotSequence);
                Assert.isTrue(workCount == 0, "업무에 할당된 로봇은 삭제할 수 없습니다.\n업무 할당 정보 수정 후 다시 시도해 주십시오.");

                // 작업중인 로봇은 삭제 불가능
                if (!RobotStatusCode.WORKING.getCode().equals(robot.getRobotStatusCode())) {
                    robotMapper.removeRobot(robotSequence, UserDetailsSupport.getUserId());
                    authenticationTokenMapper.deleteAuthenticationByPrimaryKey(AuthenticationType.ROBOT.getCode(),
                            robotSequence);
                    insertRobotLog(robot.getRobotSequence(), robot.getRobotName(), RobotLogTypeCode.REMOVE,
                            RobotLogStatusCode.SUCCESS, null);
                    result++;
                } else {
                    insertRobotLog(robot.getRobotSequence(), robot.getRobotName(), RobotLogTypeCode.REMOVE,
                            RobotLogStatusCode.FAIL, RobotStatusCode.WORKING.getDescription());
                }
            }
        }
        return result;
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_ROBOT_READ)
    @Transactional
    public RobotLogListResponse getRobotLogList(RobotLogSearchForm form) {
        RobotLogListResponse response = new RobotLogListResponse();
        int count = robotLogMapper.selectCount(form);
        response.setCount(count);
        form.resetPageNoIfNotValid(count);
        if (count > 0) {
            response.setRobotLogList(robotLogMapper.selectRobotLogList(form));
        }
        response.setForm(form);
        return response;

    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_ROBOT_READ)
    @Transactional
    public RobotLogFileterResponse getRobotLogFilterList(Integer robotSequence) {
        RobotLogFileterResponse response = new RobotLogFileterResponse();

        response.setRobotLogTypeList(robotLogMapper.selectRobotLogTypeCode(robotSequence));
        response.setRobotLogStatusList(commonCodeMapper.selectByGroupCode(GroupCode.ROBOT_LOG_STATUS_CODE.getCode()));

        return response;
    }

    private int insertRobotLog(Integer robotSequence, String robotName, RobotLogTypeCode robotLogTypeCode,
            RobotLogStatusCode robotLogStatusCode, String remarksContent) {
        RobotLog robotLog = new RobotLog();
        robotLog.setRobotLogTypeCode(robotLogTypeCode.getCode());
        robotLog.setRemarksContent(remarksContent);
        robotLog.setRobotLogStatusCode(robotLogStatusCode.getCode());
        robotLog.setRobotSequence(robotSequence);
        robotLog.setRobotName(robotName);
        robotLogMapper.insert(robotLog);
        return robotLog.getRobotLogSequence();
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_ROBOT_READ)
    public String createRobotLogExcel(RobotLogSearchForm form) {

        StringBuffer fileName = new StringBuffer();
        fileName.append(form.getRobotName());
        fileName.append("_Log_");
        fileName.append(DateTimeUtils.getNowString("yyyyMMdd"));
        fileName.append(".xls");

        return workBookRepository.put(createExcel(robotLogMapper.selectRobotLogListForExcel(form)),
                fileName.toString());
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_ROBOT_READ)
    public void downloadExcel(String uuid, HttpServletRequest request, HttpServletResponse response) {

        ExcelDownloadInfo excelDownloadInfo = workBookRepository.get(uuid);
        Assert.notNull(excelDownloadInfo, "excel file info not found");

        Workbook workbook = excelDownloadInfo.getWorkbook();
        String fileName = excelDownloadInfo.getFileName();

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);

            FileDownloadUtils.setFileDownloadHeader(request, response, fileName);
            FileDownloadUtils.writeBinaryResponse(response, byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RpaViewException(RpaViewError.INTERNAL_SERVER_ERROR, "file read exception");
        }
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_ROBOT_READ)
    public void downloadErrorLogFile(Integer fileGroupSequence, String robotName, HttpServletRequest request,
            HttpServletResponse response) {

        String zipName = robotName + "_Error_Log_" + DateTimeUtils.toString(new DateTime(), "yyyyMMdd") + ".zip";

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            fileGroupRepository.getZipFile(
                    Arrays.asList(new ZipFileInfo.Builder().fileGroupSequence(fileGroupSequence).build()), bos);
        } catch (IOException e) {
            throw new RpaViewException(RpaViewError.INTERNAL_SERVER_ERROR);
        }
        FileDownloadUtils.setFileDownloadHeader(request, response, zipName);
        FileDownloadUtils.writeBinaryResponse(response, bos.toByteArray());
    }

    private Workbook createExcel(List<RobotLogList> robotLogList) {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();

        Row headerRow = sheet.createRow(0);
        headerRow.setHeight((short) 350);
        Cell cell = null;

        // column width
        int widthIdx = 0;
        sheet.setColumnWidth(widthIdx++, 6000);
        sheet.setColumnWidth(widthIdx++, 8000);
        sheet.setColumnWidth(widthIdx++, 5000);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFillForegroundColor(HSSFColorPredefined.AQUA.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setBorderBottom(BorderStyle.MEDIUM);

        String[] subjects = { "날짜", "내용", "상태" };

        int subjectLength = subjects.length;

        for (int i = 0; i < subjectLength; i++) {
            cell = headerRow.createCell(i);
            cell.setCellValue(subjects[i]);
            cell.setCellStyle(headerStyle);
        }

        int robotLogListSize = robotLogList.size();

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        for (int rownum = 0; rownum < robotLogListSize; rownum++) {
            RobotLogList log = robotLogList.get(rownum);

            String registerDateTime = DateTimeUtils.toString(log.getRegisterDateTime(), "yyyy-MM-dd HH:mm");

            String[] values = { registerDateTime, log.getRobotLogTypeCodeName(), log.getRobotLogStatusCodeName() };

            values = checkNullString(values);

            Row row = sheet.createRow(rownum + 1);
            row.setHeight((short) 300);

            for (int i = 0; i < values.length; i++) {
                cell = row.createCell(i);
                cell.setCellValue(values[i]);
                cell.setCellStyle(cellStyle);
            }
        }

        return workbook;
    }

    private String[] checkNullString(String[] strArray) {
        for (String str : strArray) {
            if (StringUtils.isEmpty(str) || "".equals(str)) {
                str = "";
            }
        }
        return strArray;
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_ROBOT_READ)
    public RobotAddCheckResponse checkRobotAddValidity(RobotAddForm form) {

        RobotAddCheckResponse response = new RobotAddCheckResponse();

        if (robotMapper.selectByRobotName(form.getRobotName()) > 0) {
            response.setRobotExistYn(true);
        } else {
            response.setRobotExistYn(false);

            if (robotMapper.selectByPcNameAndIp(form) > 0) {
                response.setPcExistYn(true);
            } else {
                response.setPcExistYn(false);
            }
        }

        return response;
    }

}
