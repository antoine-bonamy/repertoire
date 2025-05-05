package fr.bonamy.repertoire_back.exception;

public class MappingException extends RuntimeException {
    public MappingException(Throwable e) {
        super("Mapping failed", e);
    }
}
