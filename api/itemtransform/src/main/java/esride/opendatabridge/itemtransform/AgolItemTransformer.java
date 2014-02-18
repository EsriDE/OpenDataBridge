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
 *
 * User: sma
 * Date: 02.04.13
 * Time: 14:14
 * To change this template use File | Settings | File Templates.
 */
public class AgolItemTransformer implements IItemTransformer {
    private static Logger sLogger = Logger.getLogger(AgolItemTransformer.class);

    private Transformer trans;

    private IItemGeneratorConfiguration generatorConfiguration;

    private HashMap<String, IElemHandler> elemHandlerMap;

    public void setGeneratorConfiguration(IItemGeneratorConfiguration generatorConfiguration) {
        this.generatorConfiguration = generatorConfiguration;
    }

    public void setElemHandlerMap(HashMap<String, IElemHandler> elemHandlerMap) {
        this.elemHandlerMap = elemHandlerMap;
    }

    public AgolItemTransformer() throws TransformerConfigurationException {

        TransformerFactory fac = TransformerFactory.newInstance();
        trans = fac.newTransformer();

    }

    public HashMap<String, String> transform2AgolItem(MetadataResource resource, String processId) throws ItemTransformationException, ItemGenerationException {

        HashMap<String, String> itemMap = new HashMap<String, String>();
        Properties properties = generatorConfiguration.getItemGeneratorConfiguration(processId, resource.getResourceType());

        String countString = (String)properties.get("item.count");
        int count = Integer.parseInt(countString);
        for(int i=0; i<count; i++){
            String itemMetadataType = properties.getProperty("item[" + i + "].md.type");

            if(sLogger.isTraceEnabled()){
                Document doc = resource.getDocMap().get(itemMetadataType);
                try {
                    StringWriter write = new StringWriter();
                    trans.transform(new DOMSource(doc), new StreamResult(write));
                    sLogger.trace("GetItem from Document: " + write.toString());
                } catch (TransformerException e) {
                    sLogger.warn("Could not transform document for logging.");
                }
            }


            String itemKey = properties.getProperty("item[" + i + "].md.id");
            String itemHandler = properties.getProperty("item[" + i + "].value.handler");

            if(elemHandlerMap.containsKey(itemHandler)){
                IElemHandler handler = elemHandlerMap.get(itemHandler);
                itemMap.put(itemKey, handler.handleElement(properties.getProperty("item[" + i + "].md.value"), resource.getDocMap().get(itemMetadataType)));
            } else{
                String lMessage = "Wrong item Type: " + properties.getProperty("item[" + i + "].value.handler");
                sLogger.error(lMessage);
                throw new ItemGenerationException(lMessage);
            }

        }
        return itemMap;
    }


}