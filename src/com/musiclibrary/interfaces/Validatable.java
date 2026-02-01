package com.musiclibrary.interfaces;
public interface Validatable {
    boolean validate();

    default String getValidationMessage() {
        return validate() ? "Valid" : "Invalid";
    }

    static boolean isValidYear(int year) {
        return year >= 1900 && year <= java.time.Year.now().getValue();
    }

    static boolean isValidDuration(int duration) {
        return duration > 0 && duration <= 36000;
    }
}