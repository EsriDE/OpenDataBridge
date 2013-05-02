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
     * the type of the capabilities (wms, kml, etc...)
     */
    private String capabilitiesType;

    /**
     * the metadata document
     */
    private Document metadataDoc;

    /**
     * the capabilities document (like for WMS)
     */
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

    public Document getMetadataDoc() {
        return metadataDoc;
    }

    public void setMetadataDoc(Document cswMetadataDoc) {
        this.metadataDoc = cswMetadataDoc;
    }

    public Document getCapabilitiesDoc() {
        return capabilitiesDoc;
    }

    public void setCapabilitiesDoc(Document capabilitiesDoc) {
        this.capabilitiesDoc = capabilitiesDoc;
    }

    public String getCapabilitiesType() {
        return capabilitiesType;
    }

    public void setCapabilitiesType(String capabilitiesType) {
        this.capabilitiesType = capabilitiesType;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
}
