package com.mobileleader.rpa.api.exception;

public enum RpaApiError {
    // @formatter:off
    // Common Error
    INTERNAL_SERVER_ERROR(700, "내부 서버 오류"),
    ILLEGAL_ARGUMENT(710, "IllegalArgument"),
    INVALID_PARAMETER(711, "잘못된 파라미터"),
    AUTHENTICATION_ERROR(720, "인증실패"),
    AUTHORITY_ERROR(721, "접근 권한이 없는 사용자입니다."),
    SQL_ERROR(730, "SQL 에러"),
    FILE_IO_ERROR(740, "File IO 에러"),
    SCHEDULE_ERROR(750, "스케줄러 Error"),
    ENCODE_ERROR(760, "UnsupportedEncoding Error"),
    MAX_UPLOAD_SIZE_PER_FILE_EXCEEDS(770, "파일 당 업로드 용량 제한 초과"),

    // Process Error
    PROCESS_NOT_FOUND(810, "해당 프로세스가 존재하지 않습니다."),
    ALREADY_CHECKED_OUT_PROCESS(811, "선택한 항목을 체크아웃 할 수 없습니다. 이미 체크아웃된 프로세스입니다."),
    ALREADY_CHECKED_IN_PROCESS(812, "선택한 항목을 체크인 할 수 없습니다. 이미 체크인된 프로세스입니다."),
    NOT_MATCHED_CHECK_OUT_USER(813, "선택한 항목을 체크아웃 할 수 없습니다. 체크아웃한 사용자가 아닙니다."),
    CHECK_OUT_PROCESS_NOT_FOUND(814, "체크아웃한 프로세스가 존재하지 않습니다."),
    REMOVED_PROCESS(815, "삭제된 프로세스입니다."),
    NOT_MATCHED_PROCESS_FILE_SEQUENCE(816, "해당 프로세스의 파일순번이 아닙니다."),
    PROCESS_FILE_NOT_FOUND(817, "프로세스 파일이 존재하지 않습니다."),
    DUPLICATE_PROCESS_NAME(818, "서버에 동일한 프로세스가 존재합니다. 프로젝트명을 다른 이름으로 변경 후 다시 시도하십시오."),
    ALREADY_CANCEL_CHECKED_OUT_PROCESS(819,
            "선택한 항목을 체크아웃 취소할 수 없습니다. 이미 체크아웃 취소된 프로세스입니다"),

    // Login Error
    LOGIN_FAILURE(830, "로그인에 실패하였습니다."),
    INVALID_USER_ID_OR_PASSWORD(831, "아이디 또는 비밀번호를 확인해 주십시오.");


    // @formatter:on

    private final int errorCode;

    private final String errorMessage;

    private RpaApiError(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
