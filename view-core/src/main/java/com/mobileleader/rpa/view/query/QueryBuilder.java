package com.mobileleader.rpa.view.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryBuilder {

    private static final Logger logger = LoggerFactory.getLogger(QueryBuilder.class);

    /**
     * 쿼리 생성 함수.
     *
     * @param columnName 컬럼 이름
     * @param columnValue 컬럼 데이터
     * @return
     */
    public static String getWhereLikeClause(String columnName, String columnValue) {

        String escapedText = "";
        if (columnValue != null) {
            escapedText = columnValue.toUpperCase(); // 대문자로 비교.
            escapedText = escapedText.replace("_", "\\_");
            escapedText = escapedText.replace("%", "\\%");
        }

        String query = String.format(" upper(%s) like '%%' || '%s' || '%%' escape '\\' ", columnName, escapedText);

        logger.debug(query);

        return query;
    }

}
