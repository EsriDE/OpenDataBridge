package esride.opendatabridge.reader;

import org.w3c.dom.Document;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 10.04.13
 * Time: 13:51
 * To change this template use File | Settings | File Templates.
 */
public class MetadataObject {

    /**
     * the internal file identifier of this metadata set
     */
    private String metadataFileIdentifier;

    /**
     * the resource type of the resource (like WMS, view, ....)
     */
    private String resourceType;

    /**
     * The URL of the resource
     */
    private String resourceUrl;

    /**
     * the URL for the resource capabilities
     */
    private String capabilitiesUrl;

    /**
     * the metadata document
     */
    private Document metadataDoc;


    public String getMetadataFileIdentifier() {
        return metadataFileIdentifier;
    }

    public void setMetadataFileIdentifier(String metadataFileIdentifier) {
        this.metadataFileIdentifier = metadataFileIdentifier;
    }

    public String getCapabilitiesUrl() {
        return capabilitiesUrl;
    }

    public void setCapabilitiesUrl(String capabilitiesUrl) {
        this.capabilitiesUrl = capabilitiesUrl;
    }

    public Document getMetadataDoc() {
        return metadataDoc;
    }

    public void setMetadataDoc(Document cswMetadataDoc) {
        this.metadataDoc = cswMetadataDoc;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
}
