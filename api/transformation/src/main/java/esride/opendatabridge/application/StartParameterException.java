package esride.opendatabridge.application;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 03.05.13
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
public class StartParameterException extends Exception {

    public StartParameterException() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public StartParameterException(String message) {
        super(message);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public StartParameterException(String message, Throwable cause) {
        super(message, cause);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public String getMessage() {
        return super.getMessage();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
