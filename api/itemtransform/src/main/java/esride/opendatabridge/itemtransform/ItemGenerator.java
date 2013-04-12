package esride.opendatabridge.itemtransform;


import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 03.04.13
 * Time: 18:10
 * To change this template use File | Settings | File Templates.
 */
public class ItemGenerator {
    
    private static Logger sLogger = Logger.getLogger(ItemGenerator.class);

    private XPathFactory xPathFactory;
    private XPath xPath;
    
    private IItemGeneratorConfiguration generatorConfiguration;

    public void setGeneratorConfiguration(IItemGeneratorConfiguration generatorConfiguration) {
        this.generatorConfiguration = generatorConfiguration;
    }

    public ItemGenerator(){
        xPathFactory = XPathFactory.newInstance();
        xPath = xPathFactory.newXPath();
    }

    public HashMap getItemElementsFromDoc(HashMap<String, Document> xmlDoc, String processId, String targetResource) throws ItemGenerationException {
    //public HashMap getItemElementsFromDoc(Document xmlDoc) throws ItemGenerationException{
        HashMap itemMap = new HashMap();
        Properties properties = generatorConfiguration.getItemGeneratorConfiguration(processId, targetResource);

        String countString = (String)properties.get("item.count");
        int count = Integer.parseInt(countString);
        for(int i=0; i<count; i++){
            String itemMetadataType = properties.getProperty("item[" + i + "].md.type");

            String itemKey = properties.getProperty("item[" + i + "].md.id");
            String itemType = properties.getProperty("item[" + i + "].value.type");
            if(itemType.equals("string")){
                try {
                    itemMap.put(itemKey, (String)xPath.evaluate(properties.getProperty("item[" + i + "].md.value"), xmlDoc.get(itemMetadataType), XPathConstants.STRING));
                } catch (XPathExpressionException e) {
                    String lMessage = "Cannot evaluate xpath as a string: " + properties.getProperty("item[" + i + "].md.value");
                    sLogger.error(lMessage);
                    throw new ItemGenerationException(lMessage, e);
                }
            } else if(itemType.equals("default")){
                itemMap.put(itemKey, properties.getProperty("item[" + i + "].md.value"));
            } else if(itemType.equals("nodeset")){
                try {
                    NodeList nodeList = (NodeList)xPath.evaluate(properties.getProperty("item[" + i + "].md.value"), xmlDoc.get(itemMetadataType), XPathConstants.NODESET);
                    StringBuffer buffer = new StringBuffer();
                    int nodeListLength = nodeList.getLength();
                    for(int j=0; j<nodeListLength; j++){
                        buffer.append(nodeList.item(j).getNodeValue());
                        if(j < nodeListLength-1){
                            buffer.append(",");
                        }
                    }
                    itemMap.put(itemKey, buffer.toString());

                } catch (XPathExpressionException e) {
                    String lMessage = "Cannot evaluate xpath as a nodeset: " + properties.getProperty("item[" + i + "].md.value");
                    sLogger.error(lMessage);
                    throw new ItemGenerationException(lMessage, e);
                }
            }else{
                String lMessage = "Wrong item Type: " + properties.getProperty("item[" + i + "].value.type");
                sLogger.error(lMessage);
                throw new ItemGenerationException(lMessage);
            }
            
            
        }
        return itemMap;
    }
}
