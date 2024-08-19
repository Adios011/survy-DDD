package org.survy.domain.core.exception;

import org.survy.domain.core.entity.Participation;

public class QuotaEnabledException extends ParticipationDomainException {

    public QuotaEnabledException(String message) {
        super(message);
    }
}
