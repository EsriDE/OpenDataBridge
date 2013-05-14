package esride.opendatabridge.agolwriter;

import esride.opendatabridge.item.AgolItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nik
 * Date: 14.05.13
 * Time: 12:01
 * To change this template use File | Settings | File Templates.
 */
public interface IAgolService {
    String getUserGroupIds() throws IOException;

    AgolItem getItem(String itemId) throws IOException, AgolTransactionFailedException;

    Map<String, ArrayList<AgolItem>> getAllItems(List<String> itemTypes) throws IOException;

    Map<String, ArrayList<AgolItem>> getAllItems(List<String> itemTypes, OwnerType ownerType) throws IOException;

    Map<String, ArrayList<AgolItem>> getAllItems(List<String> itemTypes, OwnerType ownerType, String addendum) throws IOException;

    Map<String, ArrayList<AgolItem>> getAllItems(String searchString) throws IOException;

    String addItems(List<AgolItem> agolItems) throws AgolTransactionFailedException, IOException;

    String addItems(List<AgolItem> agolItems, AccessType accessType) throws AgolTransactionFailedException, IOException;

    String addItems(List<AgolItem> agolItems, AccessType accessType, String groupIds) throws AgolTransactionFailedException, IOException;

    void updateItems(List<AgolItem> agolItems) throws IOException, AgolTransactionFailedException;

    void updateItems(List<AgolItem> agolItems, AccessType accessType) throws IOException, AgolTransactionFailedException;

    void updateItems(List<AgolItem> agolItems, AccessType accessType, String groupIds) throws IOException, AgolTransactionFailedException;

    void deleteItems(List<AgolItem> agolItems) throws IOException, AgolTransactionFailedException;

    void deleteItems(String itemIds) throws IOException, AgolTransactionFailedException;
}
