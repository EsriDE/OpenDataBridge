package esride.opendatabridge.itemtransform;

/**
 * Die Exception wird geworfen, wenn bei der Transformation ein technischer Fehler auftritt.
 * User: sma
 * Date: 03.04.13
 * Time: 17:23
 *
 */
public class ItemTransformationException extends RuntimeException{

    public ItemTransformationException() {
        super();
    }

    public ItemTransformationException(String message) {
        super(message);
    }

    public ItemTransformationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemTransformationException(Throwable cause) {
        super(cause);
    }
}
