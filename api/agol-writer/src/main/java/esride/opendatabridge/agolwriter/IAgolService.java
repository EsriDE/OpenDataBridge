package esride.opendatabridge.agolwriter;

import esride.opendatabridge.item.AgolItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nik
 * Date: 10.05.13
 * Time: 18:12
 * To change this template use File | Settings | File Templates.
 */
public interface IAgolService {
    String getUserGroupIds() throws IOException;

    AgolItem getItem(String itemId);

    Map<String, ArrayList<AgolItem>> getAllItems(List<String> itemTypes) throws IOException;

    Map<String, ArrayList<AgolItem>> getAllItems(List<String> itemTypes, AccessType accessType) throws IOException;

    Map<String, ArrayList<AgolItem>> getAllItems(String searchString) throws IOException;

    String addItems(List<AgolItem> agolItems) throws AgolItemTransactionFailedException, IOException;

    String addItems(List<AgolItem> agolItems, AccessType accessType) throws AgolItemTransactionFailedException, IOException;

    String addItems(List<AgolItem> agolItems, AccessType accessType, String groupIds) throws AgolItemTransactionFailedException, IOException;

    void unshareItems(String itemIds, String groupIds);

    void updateItem(AgolItem agolItem) throws IOException, AgolItemTransactionFailedException;

    void deleteItem(List<AgolItem> agolItems) throws IOException, AgolItemTransactionFailedException;
}
