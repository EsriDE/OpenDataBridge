package esride.opendatabridge.reader;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 17.04.13
 * Time: 10:51
 * To change this template use File | Settings | File Templates.
 */
public class ResourceException extends Exception {

    public ResourceException() {
        super();
    }

    public ResourceException(String message) {
        super(message);
    }

    public ResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceException(Throwable cause) {
        super(cause);
    }
}
