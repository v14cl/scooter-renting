package com.vlad.scooterrental.domain.exception;

public class AccessDeniedException extends DomainException {

    public AccessDeniedException(String message) {
        super(message);
    }
}
