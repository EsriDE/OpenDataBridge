package esride.opendatabridge.reader;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 21.03.13
 * Time: 07:46
 * To change this template use File | Settings | File Templates.
 */
public interface IReader {

    public TransformedItemResult getTramsformedItemsFromCatalog(int startPos) throws ReaderException;

    public MetadataObjectResult getItemsFromCatalog(int startPos) throws ReaderException;

}
