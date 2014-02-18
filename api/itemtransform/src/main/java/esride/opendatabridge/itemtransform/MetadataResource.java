package esride.opendatabridge.itemtransform;

import org.w3c.dom.Document;

import java.util.HashMap;


/**
 * The class MetadataResource represents a bunch of documents which are necessary to create an ArcGIS Online
 * Item. Each MetadataResource has a special resourceType (like 'WMS', 'view', ..). For the AGOL Item generation
 * one or more documents are necessary to extract all information (like the keywords from the iso MetadataSet and from the capabilities
 * document the Width and Height information)
 * User: sma
 * Date: 05.04.13
 * Time: 10:13
 *
 */
public class MetadataResource {

    /**
     * could be one of these types http://www.arcgis.com/apidocs/rest/index.html?itemtypes.html
     */
    private String resourceType;

    /**
     * key=csw, ckan or capabilities, the value contains the corresponding document
     */
    private HashMap<String, Document> docMap;


    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public HashMap<String, Document> getDocMap() {
        return docMap;
    }

    public void setDocMap(HashMap<String, Document> docMap) {
        this.docMap = docMap;
    }

    public void addDoc(String key, Document doc){
        if(docMap == null){
            docMap = new HashMap<String, Document>();
        }
        docMap.put(key, doc);
    }
}
