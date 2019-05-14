package com.mobileleader.rpa.view.service.excel;

import org.apache.poi.ss.usermodel.Workbook;

public class ExcelDownloadInfo {
    private Workbook workbook;

    private String fileName;

    public ExcelDownloadInfo(Workbook workbook, String fileName) {
        this.workbook = workbook;
        this.fileName = fileName;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
        result = prime * result + ((workbook == null) ? 0 : workbook.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ExcelDownloadInfo other = (ExcelDownloadInfo) obj;
        if (fileName == null) {
            if (other.fileName != null) {
                return false;
            }
        } else if (!fileName.equals(other.fileName)) {
            return false;
        }
        if (workbook == null) {
            if (other.workbook != null) {
                return false;
            }
        } else if (!workbook.equals(other.workbook)) {
            return false;
        }
        return true;
    }
}
