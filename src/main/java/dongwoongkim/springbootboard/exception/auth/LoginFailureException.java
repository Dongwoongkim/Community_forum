package dongwoongkim.springbootboard.exception.auth;

public class LoginFailureException extends RuntimeException {
    public LoginFailureException(String s) {
        super(s);
    }
}
