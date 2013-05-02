package esride.opendatabridge.itemtransform;

/**
 * Die Exception wird geworfen, wenn bei der Extraktion eines Elements aus den Originaldaten (Ckan, CSW, Capabilities...) ein Fehler auftritt.
 * User: sma
 * Date: 04.04.13
 * Time: 07:55
 *
 */
public class ItemGenerationException extends RuntimeException {

    public ItemGenerationException() {
        super();
    }

    public ItemGenerationException(String message) {
        super(message);
    }

    public ItemGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemGenerationException(Throwable cause) {
        super(cause);
    }
}
