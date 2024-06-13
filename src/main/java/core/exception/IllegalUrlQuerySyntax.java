package core.exception;

public class IllegalUrlQuerySyntax extends IllegalArgumentException{
    public IllegalUrlQuerySyntax() {
        super();
    }

    public IllegalUrlQuerySyntax(String s) {
        super(s);
    }

    public IllegalUrlQuerySyntax(String message, Throwable cause) {
        super(message, cause);
    }
}
