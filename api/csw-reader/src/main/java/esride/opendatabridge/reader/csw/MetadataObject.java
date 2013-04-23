package esride.opendatabridge.reader.csw;

import org.w3c.dom.Document;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 10.04.13
 * Time: 13:51
 * To change this template use File | Settings | File Templates.
 */
public class MetadataObject {
    
    private String metadataFileIdentifier;
    
    private String metadataResource;
    
    private String resourceUrl;
    
    private String capabilitiesUrl;
    
    private Document cswMetadataDoc;
    
    private Document capabilitiesDoc;

    public String getMetadataFileIdentifier() {
        return metadataFileIdentifier;
    }

    public void setMetadataFileIdentifier(String metadataFileIdentifier) {
        this.metadataFileIdentifier = metadataFileIdentifier;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String wmsUrl) {
        this.resourceUrl = wmsUrl;
    }

    public String getCapabilitiesUrl() {
        return capabilitiesUrl;
    }

    public void setCapabilitiesUrl(String capabilitiesUrl) {
        this.capabilitiesUrl = capabilitiesUrl;
    }

    public Document getCswMetadataDoc() {
        return cswMetadataDoc;
    }

    public void setCswMetadataDoc(Document cswMetadataDoc) {
        this.cswMetadataDoc = cswMetadataDoc;
    }

    public Document getCapabilitiesDoc() {
        return capabilitiesDoc;
    }

    public void setCapabilitiesDoc(Document capabilitiesDoc) {
        this.capabilitiesDoc = capabilitiesDoc;
    }

    public String getMetadataResource() {
        return metadataResource;
    }

    public void setMetadataResource(String metadataResource) {
        this.metadataResource = metadataResource;
    }
}
