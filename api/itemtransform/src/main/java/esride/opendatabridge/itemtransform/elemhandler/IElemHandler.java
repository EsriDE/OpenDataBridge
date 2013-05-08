package esride.opendatabridge.itemtransform.elemhandler;

import org.w3c.dom.Document;

/**
 * Interface for getting information (via XPath, etc...) from a document.
 * User: sma
 * Date: 07.05.13
 * Time: 17:10
 * To change this template use File | Settings | File Templates.
 */
public interface IElemHandler {

    public String handleElement(String element, Document doc);
}
