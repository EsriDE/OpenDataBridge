package esride.opendatabridge.processing;

/**
 * The Transformer Exception is thrown when the transformation process cannot be executed.
 * User: sma
 * Date: 24.05.13
 * Time: 09:22
 * To change this template use File | Settings | File Templates.
 */
public class TransformerException extends Exception {

    public TransformerException() {
        super();
    }

    public TransformerException(String message) {
        super(message);
    }

    public TransformerException(String message, Throwable cause) {
        super(message, cause);
    }
}
