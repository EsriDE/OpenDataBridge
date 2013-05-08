package esride.opendatabridge.itemtransform.elemhandler;

import org.w3c.dom.Document;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 07.05.13
 * Time: 17:11
 * To change this template use File | Settings | File Templates.
 */
public class DefaultElemHandler implements IElemHandler{

    public String handleElement(String element, Document doc) {
        return element;
    }
}
