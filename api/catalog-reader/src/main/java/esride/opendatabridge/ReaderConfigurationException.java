package esride.opendatabridge;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 14.04.13
 * Time: 16:32
 * To change this template use File | Settings | File Templates.
 */
public class ReaderConfigurationException  extends Exception{
    public ReaderConfigurationException() {
        super();
    }

    public ReaderConfigurationException(String message) {
        super(message);
    }

    public ReaderConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReaderConfigurationException(Throwable cause) {
        super(cause);
    }
}
