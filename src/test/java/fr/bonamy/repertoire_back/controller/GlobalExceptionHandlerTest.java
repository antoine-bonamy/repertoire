package fr.bonamy.repertoire_back.controller;

import fr.bonamy.repertoire_back.exception.ResourceAlreadyExist;
import fr.bonamy.repertoire_back.exception.ResourceNotFoundException;
import fr.bonamy.repertoire_back.util.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @InjectMocks
    GlobalExceptionHandler globalExceptionHandler;

    @Test
    public void resourceNotFoundExceptionShouldReturn404() {
        ResourceNotFoundException e = new ResourceNotFoundException("2");
        ResponseEntity<ErrorMessage> result = globalExceptionHandler.resourceNotFoundException(e);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void resourceAlreadyExistShouldReturn409() {
        ResourceAlreadyExist e = new ResourceAlreadyExist("user");
        ResponseEntity<ErrorMessage> result = globalExceptionHandler.resourceAlreadyExist(e);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    }

}
