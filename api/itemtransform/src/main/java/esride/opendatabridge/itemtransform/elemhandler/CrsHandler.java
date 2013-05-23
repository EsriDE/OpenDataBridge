package esride.opendatabridge.itemtransform.elemhandler;

import esride.opendatabridge.itemtransform.ItemGenerationException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.HashMap;


/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 23.05.13
 * Time: 16:48
 * To change this template use File | Settings | File Templates.
 */
public class CrsHandler implements IElemHandler{
    private static Logger sLogger = Logger.getLogger(CodeListElemHandler.class);

    private XPathFactory xPathFactory;
    private XPath xPath;

    private HashMap<String,String> codeListMap;

    public void setCodeListMap(HashMap<String, String> codeListMap) {
        this.codeListMap = codeListMap;
    }

    public CrsHandler() {
        xPathFactory = XPathFactory.newInstance();
        xPath = xPathFactory.newXPath();
    }

    public String handleElement(String element, Document xmlDoc) {
        try {
            if(xmlDoc != null){
                NodeList nodeList = (NodeList)xPath.evaluate(element, xmlDoc, XPathConstants.NODESET);
                StringBuffer buffer = new StringBuffer();
                int nodeListLength = nodeList.getLength();
                for(int j=0; j<nodeListLength; j++){
                    String value = "";
                    if(!codeListMap.containsKey(nodeList.item(j).getNodeValue())){
                        String crsValue = nodeList.item(j).getNodeValue();
                        if(crsValue.startsWith("CRS:")){
                            value = crsValue.substring(4);
                        }
                        if(crsValue.startsWith("AUTO2:")){
                            value = crsValue.substring(6);
                        }
                        if(crsValue.startsWith("EPSG:")){
                            value = crsValue.substring(5);
                        }
                        
                    }else{
                        value = codeListMap.get(nodeList.item(j).getNodeValue());
                    }

                    buffer.append(value);
                    if(j < nodeListLength-1){
                        buffer.append(",");
                    }
                }
                return buffer.toString();
            } else{
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
