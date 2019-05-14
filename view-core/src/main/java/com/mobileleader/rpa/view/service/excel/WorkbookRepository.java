package com.mobileleader.rpa.view.service.excel;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Repository;

@Repository
public class WorkbookRepository {
    private Cache<String, ExcelDownloadInfo> workbookMap = CacheBuilder.newBuilder().maximumSize(1000)
            .expireAfterWrite(30, TimeUnit.MINUTES).build();

    /**
     * Put workbook map.
     *
     * @param workbook workbook
     * @param fileName fileName
     * @return uuid
     */
    public String put(Workbook workbook, String fileName) {
        String uuid = UUID.randomUUID().toString();
        workbookMap.put(uuid, new ExcelDownloadInfo(workbook, fileName));
        return uuid;
    }

    public ExcelDownloadInfo get(String uuid) {
        return workbookMap.getIfPresent(uuid);
    }

    public void remove(String uuid) {
        workbookMap.invalidate(uuid);
    }
}
