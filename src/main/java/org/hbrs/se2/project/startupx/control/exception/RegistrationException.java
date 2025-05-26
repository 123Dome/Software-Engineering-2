package org.hbrs.se2.project.startupx.control.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class RegistrationException extends RuntimeException {
    private final List<String> reasons;

    public RegistrationException(List<String> reasons) {
        super(String.join("; ", reasons)); // Optional: für Logging oder Weitergabe
        this.reasons = reasons;
    }
}
