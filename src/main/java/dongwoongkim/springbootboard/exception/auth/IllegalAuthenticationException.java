package dongwoongkim.springbootboard.exception.auth;

public class IllegalAuthenticationException extends RuntimeException {
    public IllegalAuthenticationException(String s) {
        super(s);
    }
}
