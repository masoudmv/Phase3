package server.globalExceptionHandling;

public class SquadNotFoundException extends RuntimeException {
    public SquadNotFoundException(String message) {
        super(message);
    }
}

