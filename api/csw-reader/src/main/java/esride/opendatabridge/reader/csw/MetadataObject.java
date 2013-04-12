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
    
    private String ogcServiceUrl;
    
    private Document cswMetadataDoc;
    
    private Document ogcCapabilitiesDoc;

    public String getMetadataFileIdentifier() {
        return metadataFileIdentifier;
    }

    public void setMetadataFileIdentifier(String metadataFileIdentifier) {
        this.metadataFileIdentifier = metadataFileIdentifier;
    }

    public String getOgcServiceUrl() {
        return ogcServiceUrl;
    }

    public void setOgcServiceUrl(String wmsUrl) {
        this.ogcServiceUrl = wmsUrl;
    }

    public Document getCswMetadataDoc() {
        return cswMetadataDoc;
    }

    public void setCswMetadataDoc(Document cswMetadataDoc) {
        this.cswMetadataDoc = cswMetadataDoc;
    }

    public Document getOgcCapabilitiesDoc() {
        return ogcCapabilitiesDoc;
    }

    public void setOgcCapabilitiesDoc(Document wmsCapabilitiesDoc) {
        this.ogcCapabilitiesDoc = wmsCapabilitiesDoc;
    }

    public String getMetadataResource() {
        return metadataResource;
    }

    public void setMetadataResource(String metadataResource) {
        this.metadataResource = metadataResource;
    }
}
