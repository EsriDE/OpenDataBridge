package esride.opendatabridge.reader.factory;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 21.03.13
 * Time: 07:51
 * To change this template use File | Settings | File Templates.
 */
public class ReaderFactoryException extends RuntimeException {

    public ReaderFactoryException() {
        super();
    }

    public ReaderFactoryException(String message) {
        super(message);
    }

    public ReaderFactoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReaderFactoryException(Throwable cause) {
        super(cause);
    }
}
