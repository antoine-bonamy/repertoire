package fr.bonamy.repertoire_back.exception;

public class ResourceAlreadyExist extends RuntimeException {

    private final String value;
    public ResourceAlreadyExist(String value) {
        super("Resource already exist.");
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
