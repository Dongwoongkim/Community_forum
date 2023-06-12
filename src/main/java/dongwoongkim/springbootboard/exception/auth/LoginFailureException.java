package dongwoongkim.springbootboard.exception;

public class LoginFailureException extends RuntimeException {
    public LoginFailureException(String s) {
        super(s);
    }
}
