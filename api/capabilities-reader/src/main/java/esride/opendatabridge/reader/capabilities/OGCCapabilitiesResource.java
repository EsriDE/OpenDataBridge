package esride.opendatabridge.reader.capabilities;

import esride.opendatabridge.httptransport.IHTTPRequest;
import esride.opendatabridge.reader.IResource;
import esride.opendatabridge.reader.ResourceException;
import org.apache.xml.resolver.tools.CatalogResolver;
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
 * Date: 17.04.13
 * Time: 10:47
 * To change this template use File | Settings | File Templates.
 */
public class OGCCapabilitiesResource implements IResource{

    private IHTTPRequest request;

    private DocumentBuilder builder;

    public IHTTPRequest getRequest() {
        return request;
    }

    public void setRequest(IHTTPRequest request) {
        this.request = request;
    }

    public OGCCapabilitiesResource() throws ParserConfigurationException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

        builderFactory.setNamespaceAware(false);
        builderFactory.setIgnoringElementContentWhitespace(false);
        builder = builderFactory.newDocumentBuilder();
        builder.setEntityResolver(new CatalogResolver());
    }

    public Document getRecourceMetadata(String url) throws ResourceException {
        InputStream inputStream = null;
        try {
            inputStream = getRequest().executeGetRequest(url, null);
            return builder.parse(inputStream);
        } catch (IOException e) {
            throw new ResourceException("No Capabilities from Resource with the url : " + url + " is availabe", e);
        } catch (SAXException e) {
            throw new ResourceException("No Capabilities from Resource with the url : " + url + " is availabe", e);
        }       
    }
}
