package fr.bonamy.repertoire_back.exception;

public class ResourceNotFoundException extends RuntimeException {

    private final Long id;
    public ResourceNotFoundException(Long id) {
        super("Resource not found.");
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
