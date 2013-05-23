package esride.opendatabridge.itemtransform.elemhandler;

import esride.opendatabridge.itemtransform.ItemGenerationException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * User: sma
 * Date: 21.05.13
 * Time: 15:21
 */
public class WmsUrlHandler implements IElemHandler{
    private static Logger sLogger = Logger.getLogger(WmsUrlHandler.class);

    private XPathFactory xPathFactory;
    private XPath xPath;

    public WmsUrlHandler() {
        xPathFactory = XPathFactory.newInstance();
        xPath = xPathFactory.newXPath();
    }

    public String handleElement(String element, Document xmlDoc) {
        try {
            if(xmlDoc != null){
                String wmsUrl =  (String)xPath.evaluate(element, xmlDoc, XPathConstants.STRING);
                if(wmsUrl != null && wmsUrl.contains("?")){
                    String baseWmsUrl = wmsUrl.substring(0, wmsUrl.indexOf('?'));
                    return baseWmsUrl;
                }else{
                    return wmsUrl;
                }
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
