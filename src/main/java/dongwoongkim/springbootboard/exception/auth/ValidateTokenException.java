package dongwoongkim.springbootboard.exception.auth;

public class ValidateTokenException extends RuntimeException {
    public ValidateTokenException(String message) {
        super(message);
    }
}
