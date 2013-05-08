package esride.opendatabridge.itemtransform;


import esride.opendatabridge.itemtransform.elemhandler.IElemHandler;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;


import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.StringWriter;
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

    private Transformer trans;
    
    private IItemGeneratorConfiguration generatorConfiguration;
    
    private HashMap<String, IElemHandler> elemHandlerMap;

    public void setGeneratorConfiguration(IItemGeneratorConfiguration generatorConfiguration) {
        this.generatorConfiguration = generatorConfiguration;
    }

    public void setElemHandlerMap(HashMap<String, IElemHandler> elemHandlerMap) {
        this.elemHandlerMap = elemHandlerMap;
    }

    public ItemGenerator() throws TransformerConfigurationException {

        TransformerFactory fac = TransformerFactory.newInstance();
        trans = fac.newTransformer();
    }

    public HashMap<String, String> getItemElementsFromDoc(HashMap<String, Document> xmlDoc, String processId, String targetResource) throws ItemGenerationException {
        HashMap<String, String> itemMap = new HashMap<String, String>();
        Properties properties = generatorConfiguration.getItemGeneratorConfiguration(processId, targetResource);

        String countString = (String)properties.get("item.count");
        int count = Integer.parseInt(countString);
        for(int i=0; i<count; i++){
            String itemMetadataType = properties.getProperty("item[" + i + "].md.type");

            if(sLogger.isTraceEnabled()){
                Document doc = xmlDoc.get(itemMetadataType);
                try {
                    StringWriter write = new StringWriter();
                    trans.transform(new DOMSource(doc), new StreamResult(write));
                    sLogger.trace("GetItem from Document: " + write.toString());
                } catch (TransformerException e) {
                    sLogger.warn("Could not transform document for logging.");
                }
            }


            String itemKey = properties.getProperty("item[" + i + "].md.id");
            String itemType = properties.getProperty("item[" + i + "].value.type");

            if(elemHandlerMap.containsKey(itemType)){
                IElemHandler handler = elemHandlerMap.get(itemType);
                itemMap.put(itemKey, handler.handleElement(properties.getProperty("item[" + i + "].md.value"), xmlDoc.get(itemMetadataType)));
            } else{
                String lMessage = "Wrong item Type: " + properties.getProperty("item[" + i + "].value.type");
                sLogger.error(lMessage);
                throw new ItemGenerationException(lMessage);
            }



            
            
        }
        return itemMap;
    }
}
