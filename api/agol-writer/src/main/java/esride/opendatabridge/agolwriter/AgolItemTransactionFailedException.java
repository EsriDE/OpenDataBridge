package esride.opendatabridge.agolwriter;

/**
 * Created with IntelliJ IDEA.
 * User: nik
 * Date: 02.05.13
 * Time: 11:41
 * To change this template use File | Settings | File Templates.
 */
public class AgolItemTransactionFailedException extends Exception {
    public AgolItemTransactionFailedException() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public AgolItemTransactionFailedException(String message) {
        super(message);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public AgolItemTransactionFailedException(String message, Throwable cause) {
        super(message, cause);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public AgolItemTransactionFailedException(Throwable cause) {
        super(cause);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
