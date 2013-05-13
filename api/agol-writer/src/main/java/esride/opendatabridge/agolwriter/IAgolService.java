package esride.opendatabridge.agolwriter;

import esride.opendatabridge.item.AgolItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nik
 * Date: 13.05.13
 * Time: 18:44
 * To change this template use File | Settings | File Templates.
 */
public interface IAgolService {
    String getUserGroupIds() throws IOException;

    AgolItem getItem(String itemId) throws IOException, AgolTransactionFailedException;

    Map<String, ArrayList<AgolItem>> getAllItems(List<String> itemTypes) throws IOException;

    Map<String, ArrayList<AgolItem>> getAllItems(List<String> itemTypes, AccessType accessType) throws IOException;

    Map<String, ArrayList<AgolItem>> getAllItems(List<String> itemTypes, AccessType accessType, String addendum) throws IOException;

    Map<String, ArrayList<AgolItem>> getAllItems(String searchString) throws IOException;

    String addItems(List<AgolItem> agolItems) throws AgolTransactionFailedException, IOException;

    String addItems(List<AgolItem> agolItems, AccessType accessType) throws AgolTransactionFailedException, IOException;

    String addItems(List<AgolItem> agolItems, AccessType accessType, String groupIds) throws AgolTransactionFailedException, IOException;

    void unshareItems(List<AgolItem> agolItems, String groupIds) throws IOException, AgolTransactionFailedException;

    void unshareItems(String itemIds, String groupIds) throws IOException, AgolTransactionFailedException;

    void updateItems(List<AgolItem> agolItems) throws IOException, AgolTransactionFailedException;

    void updateItem(AgolItem agolItem) throws IOException, AgolTransactionFailedException;

    void deleteItems(List<AgolItem> agolItems) throws IOException, AgolTransactionFailedException;

    void deleteItems(String itemIds) throws IOException, AgolTransactionFailedException;
}
