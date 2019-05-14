package com.mobileleader.rpa.view.model.form;

import com.mobileleader.rpa.view.query.QueryBuilder;

public class RobotSearchForm extends BaseSearchForm {

    public RobotSearchForm() {
        setSortOrder("DESC");
    }

    // 로봇순번
    private Integer robotSequence;

    // 로봇명
    private String robotName;

    // 연결PC명
    private String pcName;

    // 로봇상태코드
    private String robotStatusCode;

    public Integer getRobotSequence() {
        return robotSequence;
    }

    public void setRobotSequence(Integer robotSequence) {
        this.robotSequence = robotSequence;
    }

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public String getPcName() {
        return pcName;
    }

    public void setPcName(String pcName) {
        this.pcName = pcName;
    }

    public String getRobotStatusCode() {
        return robotStatusCode;
    }

    public void setRobotStatusCode(String robotStatusCode) {
        this.robotStatusCode = robotStatusCode;
    }

    /**
     * 로봇 이름 검색을 쿼리생성 위한 함수.
     *
     * @return
     */
    public String getRobotNameQuery() {

        return QueryBuilder.getWhereLikeClause("R.RBT_NM", this.robotName);

    }
}
