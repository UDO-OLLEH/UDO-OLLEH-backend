package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class NotFoundHarborTimetableException extends RuntimeException {
    public NotFoundHarborTimetableException() {
        super(ErrorCode.NOT_FOUND_HARBOR_TIMETABLE.getMessage());
    }

    public NotFoundHarborTimetableException(Exception ex) {
        super(ex);
    }
}
