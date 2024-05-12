package fr.bonamy.repertoire_back.util;

import org.springframework.http.HttpStatus;

import java.util.Date;


public record ErrorMessage(HttpStatus status, Date timestamp, String message) {
}