package fr.bonamy.repertoire_back.util;

import org.springframework.http.HttpStatus;

import java.util.Date;


public record ErrorMessage(HttpStatus status, Date timestamp, String message) {

    public enum Message {
        USER_NOT_FOUND("User with %s id not found."),
        USER_ALREADY_EXIST("User with %s email already exist."),
        USER_NOT_VALID("User is note valid.\n %s");

        private final String message;

        Message(String message) {
            this.message = message;
        }

        public String getMessage(String value) {
            return String.format(message, value);
        }
    }

}