package esride.opendatabridge.reader.ckan;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import esride.opendatabridge.httptransport.IHTTPRequest;
import esride.opendatabridge.reader.request.CatalogRequestObj;
import esride.opendatabridge.reader.request.CatalogResponseObj;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 24.04.13
 * Time: 12:49
 * To change this template use File | Settings | File Templates.
 */
public class CkanSearchRequest {

    private static Logger sLogger = Logger.getLogger(CkanSearchRequest.class);

    private CkanSearchResponse searchResponse;

    private IHTTPRequest httpRequest;

    private DocumentBuilder builder;
    private  JsonFactory jsonFactory;

    public CkanSearchResponse getSearchResponse() {
        return searchResponse;
    }

    public void setSearchResponse(CkanSearchResponse searchResponse) {
        this.searchResponse = searchResponse;
    }

    public IHTTPRequest getHttpRequest() {
        return httpRequest;
    }

    public void setHttpRequest(IHTTPRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public CkanSearchRequest() throws ParserConfigurationException {

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(false);
        builderFactory.setIgnoringElementContentWhitespace(false);
        builder = builderFactory.newDocumentBuilder();

        jsonFactory = new JsonFactory();
    }

    public CatalogResponseObj executeCkanRequest(CatalogRequestObj requestObj) throws IOException {

        InputStream stream = httpRequest.executeGetRequest(requestObj.getCatalogUrl(), requestObj.getParameters(), null);
        Document responseDoc = this.createDocumentFromJsonInputStream(stream);
        stream.close();
        return searchResponse.createCkanResponse(responseDoc);
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
            //throw new ItemTransformationException(message, e);
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
