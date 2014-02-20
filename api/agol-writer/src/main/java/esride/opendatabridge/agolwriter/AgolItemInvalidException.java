package esride.opendatabridge.agolwriter;

/**
 * User: nik
 * Date: 24.05.13
 * Time: 17:33
 */
public class AgolItemInvalidException extends Exception {
    public AgolItemInvalidException() {
        super();
    }

    public AgolItemInvalidException(String message) {
        super(message);
    }

    public AgolItemInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public AgolItemInvalidException(Throwable cause) {
        super(cause);
    }
}
