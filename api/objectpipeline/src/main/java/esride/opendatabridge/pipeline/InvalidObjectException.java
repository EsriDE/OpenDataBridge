package esride.opendatabridge.pipeline;

/**
 * Created with IntelliJ IDEA.
 * User: sma
 * Date: 11.02.14
 * Time: 09:22
 * To change this template use File | Settings | File Templates.
 */
public class InvalidObjectException extends Exception {
    public InvalidObjectException() {
        super();
    }

    public InvalidObjectException(String message) {
        super(message);
    }

    public InvalidObjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidObjectException(Throwable cause) {
        super(cause);
    }
}
