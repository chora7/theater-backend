package com.example.backend.enums;

import java.util.Locale;

import org.springframework.context.MessageSource;

public enum ProjectStatus {
    PLANNED,
    IN_PROGRESS,
    COMPLETED,
    CANCELED;

    public String getLocalized (MessageSource messageSource, Locale locale) {
        return messageSource.getMessage("project.status." + this.name(), null, this.name(), locale);
    }
}
