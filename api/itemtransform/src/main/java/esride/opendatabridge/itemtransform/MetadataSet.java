package esride.opendatabridge.itemtransform;

import org.w3c.dom.Document;



/**
 *
 * User: sma
 * Date: 04.04.13
 * Time: 14:54
 *
 */
public class MetadataSet {

    /**
     * could be json or xml
     */
    //private String encodingType;

    /**
     * could be csw, ckan, capabilities
     */
    private String metadataType;

    /**
     * the original metadata document (iso metadata set (xml), capabilities (xml), ckan (xml), ...)
     */
    //private InputStream inputStream;

    /**
     * the original metadata document (iso metadata set (xml), capabilities (xml), ckan (tranformed from json to xml), ...)
     */
    private Document xmlDoc;

    /*public String getEncodingType() {
        return encodingType;
    }

    public void setEncodingType(String encodingType) {
        this.encodingType = encodingType;
    } */

    /*public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    } */

    public String getMetadataType() {
        return metadataType;
    }

    public void setMetadataType(String metadataType) {
        this.metadataType = metadataType;
    }

    public Document getXmlDoc() {
        return xmlDoc;
    }

    public void setXmlDoc(Document xmlDoc) {
        this.xmlDoc = xmlDoc;
    }
}
