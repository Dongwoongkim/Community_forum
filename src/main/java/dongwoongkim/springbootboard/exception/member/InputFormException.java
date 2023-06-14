package dongwoongkim.springbootboard.exception.member;

public class InputFormException extends RuntimeException {
    public InputFormException() {
    }

    public InputFormException(String message) {
        super(message);
    }
}
