package esride.opendatabridge.agolwriter;

import esride.opendatabridge.item.AgolItem;

import java.io.IOException;

import java.util.HashMap;


/**
 * This interface provides the transaction operations for ArcGIS Online
 * User: nik
 * Date: 16.05.13
 * Time: 09:51
 */
public interface IAgolService {


    public void insertItem(AgolItem agolItem, AccessType accessType) throws IOException;

    public void updateItem(AgolItem agolItem, AccessType accessType, boolean overwriteAccessType) throws IOException;

    public void deleteItem(String agolId) throws IOException;

    /**
     * Wrapper: Create ArcGIS Online Item from HashMap
     * @param agolItemProperties
     * @return
     */
    AgolItem createAgolItem(HashMap agolItemProperties) throws AgolItemInvalidException;

    /**
     * Merge 2 ArcGIS Online Items by copying metadata from source to target and leaving
     * @param sourceItem
     * @param targetItem
     * @return
     */
    AgolItem mergeAgolItems(AgolItem sourceItem, AgolItem targetItem);
}
