package esride.opendatabridge.itemtransform;

import java.util.List;

/**
 * The class MetadataResource represents a bunch of MetadataSet which are necessary to create an ArcGIS Online
 * Item. Each MetadataResource has a special resourceType (like 'WMS', 'view', ..). For the AGOL Item generation
 * one or more metadata sets are necessary to extract all information (like the keywords from the iso MetadataSet and from the capabilities
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

    private List<MetadataSet> container;

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public List<MetadataSet> getContainer() {
        return container;
    }

    public void setContainer(List<MetadataSet> container) {
        this.container = container;
    }
}
