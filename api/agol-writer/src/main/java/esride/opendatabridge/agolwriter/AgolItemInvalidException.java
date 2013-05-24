package esride.opendatabridge.agolwriter;

/**
 * Created with IntelliJ IDEA.
 * User: nik
 * Date: 24.05.13
 * Time: 17:33
 * To change this template use File | Settings | File Templates.
 */
public class AgolItemInvalidException extends Exception {
    public AgolItemInvalidException() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public AgolItemInvalidException(String message) {
        super(message);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public AgolItemInvalidException(String message, Throwable cause) {
        super(message, cause);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public AgolItemInvalidException(Throwable cause) {
        super(cause);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
