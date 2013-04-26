package esride.opendatabridge.processing;

import esride.opendatabridge.agolwriter.AgolService;
import esride.opendatabridge.item.AgolItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: gvs
 * Date: 06.03.13
 * Time: 15:15
 * To change this template use File | Settings | File Templates.
 */
public class Transformer {

    public static void main(String[] args) {
        String subscription  = args[0];
        String user = args[1];
        String password = args[2];
        String logpath = args[3];

        AgolService agolService = new AgolService(subscription, user, password, "http://www.esri.de", logpath);
        Map<String, ArrayList<AgolItem>> agolItems = agolService.getAllItems("WMS");

    }

    /*
    Get External Items
    @returns: HashMap<url, AgolItem>
     */

    /*
    Get AGOL Items
    @returns: HashMap<url, AgolItem>
     */

    /*
    Compare Item Lists
    @returns: CompareListContainer {
        List<AgolItem> addList;
        List<AgolItem> updateList;
        List<AgolItem> deleteList;
    }
     */

    /*
    public Write Comparison Results to AGOL {
        private addAgolItems(List<AgolItem> addList);
        private updateAgolItems(List<AgolItem> updateList);
        private deleteAgolItems(List<AgolItem> deleteList);
     }
     */

    /*
    Synchronize Accounts
    - get both listst
    - compare
    - write to AGOL
     */

}
