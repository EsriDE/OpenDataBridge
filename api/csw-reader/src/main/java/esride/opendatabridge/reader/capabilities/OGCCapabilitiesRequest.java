package esride.opendatabridge.reader.capabilities;


import esride.opendatabridge.httptransport.IHTTPRequest;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 11.04.13
 * Time: 08:22
 * To change this template use File | Settings | File Templates.
 */
public class OGCCapabilitiesRequest {

    private IHTTPRequest request;

    private DocumentBuilder builder;

    public IHTTPRequest getRequest() {
        return request;
    }

    public void setRequest(IHTTPRequest request) {
        this.request = request;
    }

    public OGCCapabilitiesRequest() throws ParserConfigurationException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(false);
        builderFactory.setIgnoringElementContentWhitespace(false);
        builder = builderFactory.newDocumentBuilder();
    }
    
    public Document getCapabilitiesDocument(String capabilitiesUrl) throws IOException, SAXException{
        InputStream inputStream = getRequest().executeGetRequest(capabilitiesUrl, null);
        return builder.parse(inputStream);
    }
}
