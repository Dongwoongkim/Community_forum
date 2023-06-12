package dongwoongkim.springbootboard.exception;

public class IllegalAuthenticationException extends RuntimeException {
    public IllegalAuthenticationException(String s) {
        super(s);
    }
}
