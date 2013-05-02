package esride.opendatabridge.reader.kml;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import esride.opendatabridge.httptransport.IHTTPRequest;
import esride.opendatabridge.reader.IResource;
import esride.opendatabridge.reader.ResourceException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 30.04.13
 * Time: 17:28
 * To change this template use File | Settings | File Templates.
 */
public class KmlResource implements IResource{
    
    private static Logger sLogger = Logger.getLogger(KmlResource.class);
    
    private String utilityServiceUrl;

    private IHTTPRequest httpRequest;
    
    private String outSR;

    private JsonFactory jsonFactory;
    private DocumentBuilder builder;

    public void setOutSR(String outSR) {
        this.outSR = outSR;
    }

    public void setUtilityServiceUrl(String utilityServiceUrl) {
        this.utilityServiceUrl = utilityServiceUrl;
    }

    public void setHttpRequest(IHTTPRequest httpRequest) {
        this.httpRequest = httpRequest;
    }
    
    public KmlResource() throws ParserConfigurationException {

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(false);
        builderFactory.setIgnoringElementContentWhitespace(false);
        builder = builderFactory.newDocumentBuilder();

        jsonFactory = new JsonFactory();
    }

    public Document getRecourceMetadata(String url, String serviceType) throws ResourceException {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("url", url);
        paramMap.put("outSR", outSR);

        try {
            InputStream stream = httpRequest.executeGetRequest(utilityServiceUrl, paramMap, null);
            return this.createDocumentFromJsonInputStream(stream);
            //create document from JSON 
        } catch (IOException e) {
            throw new ResourceException("No KML document from Resource with the url : " + url + " is available", e);
        }

    }

    private Document createDocumentFromJsonInputStream(InputStream inputStream) throws IOException {
        //1. JSON nach XML transformieren
        ByteArrayOutputStream jsonOutputStream = new ByteArrayOutputStream();
        //createXmlFromJson(jsonInput, jsonOutputStream);
        try {
            JsonParser jsonParser = jsonFactory.createJsonParser(inputStream);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = null;
            node = mapper.readTree(jsonParser);


            XmlMapper xmlmapper = new XmlMapper();
            xmlmapper.writeValue(jsonOutputStream ,node);
        } catch (IOException e) {
            inputStream.close();
            String message = "Could not parse JSON Object";
            sLogger.error(message, e);
            
        }

        //2. XML Input Stream erzeugen und an unten stehende Methode übergeben
        ByteArrayInputStream xmlStream = new ByteArrayInputStream(jsonOutputStream.toByteArray());
        Document document = null;
        try {
            document = builder.parse(xmlStream);
        } catch (SAXException e) {
            inputStream.close();
            String message = "Could not build Document";
            sLogger.error(message, e);

        } catch (IOException e) {
            inputStream.close();
            String message = "Could not build Document";
            sLogger.error(message, e);

        }
        return document;

    }
}
