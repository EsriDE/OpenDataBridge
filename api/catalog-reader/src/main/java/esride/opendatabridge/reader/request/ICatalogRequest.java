package esride.opendatabridge.reader.request;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 11.05.13
 * Time: 21:34
 * To change this template use File | Settings | File Templates.
 */
public interface ICatalogRequest {

    public CatalogResponseObj executeRequest(CatalogRequestObj requestObj) throws IOException;
}
