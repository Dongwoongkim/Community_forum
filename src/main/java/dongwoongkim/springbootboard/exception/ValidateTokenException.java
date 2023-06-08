package dongwoongkim.springbootboard.exception;

public class ValidateTokenException extends RuntimeException {
    public ValidateTokenException(String message) {
        super(message);
    }
}
