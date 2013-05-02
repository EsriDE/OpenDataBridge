package esride.opendatabridge.agolwriter;

import esride.opendatabridge.item.AgolItem;

import java.util.ArrayList;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: gvs
 * Date: 06.03.13
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 */

//ToDo: Umbenennen und vervollständigen
public interface IAgolService {
    public Map<String, ArrayList<AgolItem>> getAllItems(String itemType);
    public Map<String, ArrayList<AgolItem>> getAllItems(String itemType, String accessType);
    public void addItem(AgolItem agolItem) throws AgolItemTransactionFailedException;
    public void updateItem(AgolItem agolItem);
    public void deleteItem(AgolItem agolItem);
}
