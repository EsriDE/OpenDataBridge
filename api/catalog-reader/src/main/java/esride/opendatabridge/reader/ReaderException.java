package esride.opendatabridge.reader;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 11.04.13
 * Time: 07:50
 * To change this template use File | Settings | File Templates.
 */
public class ReaderException extends Exception {
    public ReaderException() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public ReaderException(String message) {
        super(message);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public ReaderException(String message, Throwable cause) {
        super(message, cause);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public ReaderException(Throwable cause) {
        super(cause);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
