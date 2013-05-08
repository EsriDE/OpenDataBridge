package esride.opendatabridge.itemtransform.elemhandler;

import esride.opendatabridge.itemtransform.ItemGenerationException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 07.05.13
 * Time: 17:12
 * To change this template use File | Settings | File Templates.
 */
public class StringElemHandler implements IElemHandler{
    private static Logger sLogger = Logger.getLogger(StringElemHandler.class);

    private XPathFactory xPathFactory;
    private XPath xPath;
    
    public StringElemHandler() {
        xPathFactory = XPathFactory.newInstance();
        xPath = xPathFactory.newXPath();    
    }

    public String handleElement(String element, Document xmlDoc) {
        try {
            if(xmlDoc != null){
                return (String)xPath.evaluate(element, xmlDoc, XPathConstants.STRING);
            }else{
                sLogger.warn("No document found for");
                return null;
            }
        } catch (XPathExpressionException e) {
            String lMessage = "Cannot evaluate xpath as a string: " + element;
            sLogger.error(lMessage);
            throw new ItemGenerationException(lMessage, e);
        }
    }
}
