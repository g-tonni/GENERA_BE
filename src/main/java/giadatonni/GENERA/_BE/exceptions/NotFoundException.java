package giadatonni.GENERA._BE.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super("There is no account associated with this email.");
    }

    public NotFoundException(UUID id) {
        super("The resource with id " + id + " was not found");
    }

    public NotFoundException(String id) {
        super("The resource with id " + id + " was not found");
    }
}
