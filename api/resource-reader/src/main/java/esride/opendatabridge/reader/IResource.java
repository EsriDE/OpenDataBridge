package esride.opendatabridge.reader;

import org.w3c.dom.Document;

/**
 *
 * User: sma
 * Date: 17.04.13
 * Time: 09:05
 * To change this template use File | Settings | File Templates.
 */
public interface IResource {
    
    public Document getRecourceMetadata(String url, String serviceType) throws ResourceException;
}
