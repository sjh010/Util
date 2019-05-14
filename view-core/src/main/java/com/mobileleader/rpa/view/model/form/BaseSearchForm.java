package com.mobileleader.rpa.view.model.form;

public class BaseSearchForm {

    // 페이지 번호
    private int pageNo = 1;

    // 페이지당 항목 수
    private int pageRowCount = 15;

    // 정렬키
    private String sortKey = null;

    // 정렬순서
    private String sortOrder = null;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageRowCount() {
        return pageRowCount;
    }

    public void setPageRowCount(int pageRowCount) {
        this.pageRowCount = pageRowCount;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getStartRowNo() {
        return (pageNo - 1) * pageRowCount + 1;
    }

    public int getEndRowNo() {
        return pageNo * pageRowCount;
    }

    /**
     * 조회하려는 페이지 범위 체크.
     *
     * @param count 조회가능한 총 개수.
     */
    public void resetPageNoIfNotValid(int count) {
        if ((this.pageNo - 1) * this.pageRowCount >= count) {
            this.pageNo = 1;
        }
    }
}
