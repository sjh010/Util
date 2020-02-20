package com.mobileleader.rpa.api.schedule.job;

import com.mobileleader.rpa.api.data.mapper.base.AuthenticationMapper;
import com.mobileleader.rpa.auth.type.AuthenticationType;
import com.mobileleader.rpa.repository.file.FileReadWriteSupport;
import com.mobileleader.rpa.utils.datetime.DateTimeUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TempAuthenticationDeleteJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(TempAuthenticationDeleteJob.class);

    @Value("#{apiProperties['common.file.tempFileExpireSeconds']}")
    private Integer tempFileExpireSeconds = 86400;

    @Autowired
    private FileReadWriteSupport fileReadWriteSupport;

    @Autowired
    private AuthenticationMapper authenticationMapper;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        File tempFileRoot = fileReadWriteSupport.getTempRepositoryRootFile();
        File[] tempFiles = tempFileRoot.listFiles();
        if (tempFiles == null) {
            logger.info("[Temp file path not found]");
            return;
        }
        DateTime expirationDateTime = DateTime.now().minusSeconds(tempFileExpireSeconds);
        logger.info("[Start TempFileDeleteScheduler] currentTempFileCount : {}, expirationDateTime : {}",
                tempFileRoot.listFiles().length, DateTimeUtils.toString(expirationDateTime, "yyyy-MM-dd HH:mm:ss.SSS"));
        for (File file : tempFileRoot.listFiles()) {
            if (isExpired(expirationDateTime, file)) {
                deleteTempAuthentication(file.getName());
            }
        }
    }

    @Transactional
    private void deleteTempAuthentication(String uuid) {
        try {
            fileReadWriteSupport.deleteTempFile(uuid);
            authenticationMapper.deleteByUuid(AuthenticationType.STUDIO_SIGNING_IN.getCode(), uuid);
        } catch (IOException e) {
            logger.error("tempFileDeleteException", e.getMessage());
        }
    }

    private boolean isExpired(DateTime expirationDateTime, File file) {
        boolean isExpired = false;
        try {
            BasicFileAttributes attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            FileTime fileTime = attributes.creationTime();
            DateTime creationDateTime = new DateTime(fileTime.toMillis());
            if (creationDateTime.isBefore(expirationDateTime)) {
                logger.info("[Expired file] filePath : {}, creationTime : {}", file.getPath(),
                        DateTimeUtils.toString(creationDateTime, "yyyy-MM-dd HH:mm:ss.SSS"));
                isExpired = true;
            }
        } catch (IOException e) {
            logger.error("[File IO Exception]", e);
            isExpired = false;
        }
        return isExpired;
    }
}
