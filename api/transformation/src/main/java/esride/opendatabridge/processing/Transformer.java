package esride.opendatabridge.processing;

import esride.opendatabridge.agolwriter.AgolService;


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
        private addAGOLItems(List<AgolItem> addList);
        private updateAGOLItems(List<AgolItem> updateList);
        private deleteAGOLItems(List<AgolItem> deleteList);
     }
     */

    /*
    Synchronize Accounts
    - get both listst
    - compare
    - write to AGOL
     */

}
