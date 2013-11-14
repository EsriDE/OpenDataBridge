package esride.opendatabridge.agolwriter;

import esride.opendatabridge.item.AgolItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nik
 * Date: 16.05.13
 * Time: 09:51
 * To change this template use File | Settings | File Templates.
 */
public interface IAgolService {
    /**
     * Get IDs of the groups the logged-in user is a member of
     * @return Comma-separated userGroupIds
     */
    String getUserGroupIds() throws IOException;

    /**
     * Get a specific item including Sharing information
     * @param itemId
     * @return
     */
    AgolItem getItem(String itemId) throws IOException, AgolTransactionFailedException, AgolItemInvalidException;

    /**
     * Get all items of a specific type, that are owned by logged-in user. If logged-in user is not an admin, he has only write permission to his own items. This is probably the standard use case.
     * @param itemTypes: http://resources.arcgis.com/en/help/arcgis-rest-api/index.html#//02r3000000ms000000
     * @return
     * @throws java.io.IOException
     */
    Map<String, ArrayList<AgolItem>> searchItems(List<String> itemTypes) throws IOException, AgolItemInvalidException;

    /**
     * Get all items with selectable access type
     * @param itemTypes
     * @param ownerType
     * @return
     * @throws java.io.IOException
     */
    Map<String, ArrayList<AgolItem>> searchItems(List<String> itemTypes, OwnerType ownerType) throws IOException, AgolItemInvalidException;

    /**
     * Get all items with selectable access type
     * @param itemTypes
     * @param ownerType
     * @param addendum String that is added to the end of the generated search String as a restriction ("AND")
     * @return
     * @throws java.io.IOException
     */
    Map<String, ArrayList<AgolItem>> searchItems(List<String> itemTypes, OwnerType ownerType, String addendum) throws IOException, AgolItemInvalidException;

    /**
     * Get all items that match the search string
     * @param searchString
     * @return
     * @throws java.io.IOException
     */
    Map<String, ArrayList<AgolItem>> searchItems(String searchString, OwnerType ownerType) throws IOException, AgolItemInvalidException;

    /**
     * Add a list of items and share publically
     * @param agolItems
     * @return Comma-separated list of added itemIDs
     * @throws esride.opendatabridge.agolwriter.AgolPublishBatchPartlyFailedException
     * @throws java.io.IOException
     */
    void addItems(List<AgolItem> agolItems) throws AgolPublishBatchPartlyFailedException;

    /**
     * Add a list of items and share with selectable access type
     * @param agolItems
     * @param accessType
     * @return Comma-separated list of added itemIDs
     * @throws esride.opendatabridge.agolwriter.AgolPublishBatchPartlyFailedException
     * @throws java.io.IOException
     */
    void addItems(List<AgolItem> agolItems, AccessType accessType) throws AgolPublishBatchPartlyFailedException;

    /**
     * Add a list of items and share with selectable access type and groups
     * @param agolItems
     * @param accessType
     * @param groupIds: Comma-separated list of groupIDs the items shall be shared with
     * @return Comma-separated list of added itemIDs
     * @throws esride.opendatabridge.agolwriter.AgolPublishBatchPartlyFailedException
     */
    void addItems(List<AgolItem> agolItems, AccessType accessType, String groupIds) throws AgolPublishBatchPartlyFailedException;

    /**
     * Update a list of items, don't touch the Share settings
     * @param agolItems
     */
    void updateItems(List<AgolItem> agolItems) throws AgolPublishBatchPartlyFailedException;

    /**
     * Update a list of items and adjust the Share settings for a selectable access type
     * @param agolItems
     * @param accessType
     * @throws java.io.IOException
     * @throws esride.opendatabridge.agolwriter.AgolTransactionFailedException
     */
    //void updateItems(List<AgolItem> agolItems, AccessType accessType) throws IOException, AgolTransactionFailedException;

    /**
     * Update a list of items and adjust the Share settings for a selectable access type and groups
     * @param agolItems
     * @param accessType
     * @param groupIds
     * @throws java.io.IOException
     * @throws esride.opendatabridge.agolwriter.AgolPublishBatchPartlyFailedException
     */
    void updateItems(List<AgolItem> agolItems, AccessType accessType, String groupIds) throws AgolPublishBatchPartlyFailedException;

    /**
     * Delete ArcgGIS Online items
     * @param agolItems
     * @throws java.io.IOException
     * @throws esride.opendatabridge.agolwriter.AgolPublishBatchPartlyFailedException
     */
    void deleteItems(List<AgolItem> agolItems) throws AgolPublishBatchPartlyFailedException;

    /**
     * Delete ArcgGIS Online items
     * @param itemIds as comma-separated list
     * @throws java.io.IOException
     * @throws esride.opendatabridge.agolwriter.AgolPublishBatchPartlyFailedException
     */
    void deleteItems(String itemIds) throws AgolPublishBatchPartlyFailedException;

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
