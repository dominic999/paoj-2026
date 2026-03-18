package com.pao.laboratory03.bonus.exception;

import com.pao.laboratory03.bonus.model.Status;

public class InvalidTransitionException extends RuntimeException {
    private final Status fromStatus;
    private final Status toStatus;

    public InvalidTransitionException(Status fromStatus, Status toStatus) {
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
    }

    public Status getFromStatus() {
        return fromStatus;
    }

    public Status getToStatus() {
        return toStatus;
    }

    @Override
    public String getMessage() {
        return "Nu se poate trece din " + fromStatus + " în " + toStatus;
    }
}
