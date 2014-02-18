package esride.opendatabridge.agolclient;

import esride.opendatabridge.item.AgolItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sma
 * Date: 29.01.14
 * Time: 17:40
 * To change this template use File | Settings | File Templates.
 */
public class AgolSearchResponse {

    private int nextStart;

    private int total;

    private List<AgolItem> agolItemList;

    public AgolSearchResponse(int pNextStart, int pTotal) {
        nextStart = pNextStart;
        total = pTotal;
    }

    public int getTotal() {
        return total;
    }

    public int getNextStart() {
        return nextStart;
    }

    public void addAgolItemToList(AgolItem pAgolItem){
        if(agolItemList == null){
            agolItemList = new ArrayList<AgolItem>();
        }
        agolItemList.add(pAgolItem);
    }

    public List<AgolItem> getAgolItemList() {
        return agolItemList;
    }
}
