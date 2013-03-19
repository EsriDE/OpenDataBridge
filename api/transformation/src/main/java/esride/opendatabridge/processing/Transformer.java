package esride.opendatabridge.processing;

import esride.opendatabridge.agolwriter.AGOLService;
import esride.opendatabridge.item.AGOLItem;

import java.util.HashMap;
import java.util.List;



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

        AGOLService agolService = new AGOLService(subscription, user, password, "http://www.esri.de");

    }

    /*
    Get External Items
    @returns: HashMap<url, AGOLItem>
     */

    /*
    Get AGOL Items
    @returns: HashMap<url, AGOLItem>
     */

    /*
    Compare Item Lists
    @returns: CompareListContainer {
        List<AGOLItem> addList;
        List<AGOLItem> updateList;
        List<AGOLItem> deleteList;
    }
     */

    /*
    public Write Comparison Results to AGOL {
        private addAGOLItems(List<AGOLItem> addList);
        private updateAGOLItems(List<AGOLItem> updateList);
        private deleteAGOLItems(List<AGOLItem> deleteList);
     }
     */

    /*
    Synchronize Accounts
    - get both listst
    - compare
    - write to AGOL
     */

}
