package esride.opendatabridge.itemtransform;




import org.apache.log4j.Logger;
import org.w3c.dom.Document;



import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import java.util.HashMap;
import java.util.List;

/**
 * ToDo: JSON Bearbeitung herausnehmen und nur mit documents arbeiten (Modul umbennen von jsontransform nach agoltransform)
 * User: sma
 * Date: 02.04.13
 * Time: 14:14
 * To change this template use File | Settings | File Templates.
 */
public class AgolItemTransformer implements IItemTransformer {
    private static Logger sLogger = Logger.getLogger(AgolItemTransformer.class);
    
    //private JsonFactory jsonFactory = new JsonFactory();
    
    private DocumentBuilderFactory builderFactory;
    private DocumentBuilder builder;
    
    private ItemGenerator mItemElemExtractor;

    public void setItemElemExtractor(ItemGenerator pItemElemExtractor) {
        mItemElemExtractor = pItemElemExtractor;
    }

    public AgolItemTransformer() {

        builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(false);
        builderFactory.setIgnoringElementContentWhitespace(false);
        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            String message = "AgolItemTransformer could not be initialized. DocumentBuilder failed.";
            sLogger.error(message, e);
            throw new ItemTransformationException(message, e);
        }

    }

    public HashMap<String, String> transform2AgolItem(MetadataResource resource, String processId) throws ItemTransformationException, ItemGenerationException {
        
        HashMap<String, Document> docMap = new HashMap<String, Document>();
        List<MetadataSet> container = resource.getContainer();
        for(int i=0; i<container.size(); i++){
            //String metadataEncoding = container.get(i).getEncodingType();
            //Document doc;
            /*if(metadataEncoding.equals("json")){
                doc = transformJsonStream2Doc( container.get(i).getInputStream());                
            }else if(metadataEncoding.equals("xml")){
                doc = transformXmlStream2Doc(container.get(i).getInputStream());
            }else{
                String message = "Wrong metadata encoding type: " + metadataEncoding;
                sLogger.error(message);
                throw new ItemTransformationException(message);
            } */
            docMap.put(container.get(i).getMetadataType(), container.get(i).getXmlDoc());
        }

        return mItemElemExtractor.getItemElementsFromDoc(docMap, processId, resource.getResourceType());
    }

    /*private Document transformJsonStream2Doc(InputStream jsonInput) throws ItemTransformationException, ItemGenerationException {

        //1. JSON nach XML transformieren
        ByteArrayOutputStream jsonOutputStream = new ByteArrayOutputStream();
        //createXmlFromJson(jsonInput, jsonOutputStream);
        try {
            JsonParser jsonParser = jsonFactory.createJsonParser(jsonInput);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = null;
            node = mapper.readTree(jsonParser);


            XmlMapper xmlmapper = new XmlMapper();
            xmlmapper.writeValue(jsonOutputStream ,node);
        } catch (IOException e) {
            String message = "Could not parse JSON Object";
            sLogger.error(message, e);
            throw new ItemTransformationException(message, e);
        }

        //2. XML Input Stream erzeugen und an unten stehende Methode übergeben 
        ByteArrayInputStream xmlStream = new ByteArrayInputStream(jsonOutputStream.toByteArray());
        return transformXmlStream2Doc(xmlStream);
        

    }

    private Document transformXmlStream2Doc(InputStream xmlInput) throws ItemTransformationException, ItemGenerationException {
        
        Document document = null;
        try {            
            document = builder.parse(xmlInput);
        } catch (SAXException e) {
            String message = "Could not build Document";
            sLogger.error(message, e);
            throw new ItemTransformationException(message, e);
        } catch (IOException e) {
            String message = "Could not build Document";
            sLogger.error(message, e);
            throw new ItemTransformationException(message, e);
        }
        
        return document;

       
        
    }
    */

    /**
    protected void createXmlFromJson(InputStream jsonInput, OutputStream xmlOutput) {
        //transform JSON to XML
        try {
            JsonParser jsonParser = jsonFactory.createJsonParser(jsonInput);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = null;
            node = mapper.readTree(jsonParser);


            XmlMapper xmlmapper = new XmlMapper();
            xmlmapper.writeValue(xmlOutput ,node);
        } catch (IOException e) {
            String message = "Could not parse JSON Object";
            sLogger.error(message, e);
            throw new ItemTransformationException(message, e);
        }
    }
    */
}