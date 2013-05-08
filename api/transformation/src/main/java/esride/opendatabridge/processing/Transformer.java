package esride.opendatabridge.processing;


import esride.opendatabridge.reader.IReader;
import esride.opendatabridge.reader.ReaderException;
import esride.opendatabridge.reader.TransformedItem;
import org.apache.log4j.Logger;

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
    
    private static Logger sLogger = Logger.getLogger(Transformer.class);
    
    private HashMap<String, ItemContainer> itemContainer = new HashMap<String, ItemContainer>();


    public void processTransformation(IReader reader){        

        //hole die Items vom Katalog
        List<TransformedItem> transformedItemList = null;
        try {
            transformedItemList = reader.getItemsFromCatalog();
        } catch (ReaderException e) {
            e.printStackTrace();
        }

        if(transformedItemList != null){
            int listSize = transformedItemList.size();
            for(int i=0; i<listSize; i++){
                TransformedItem item = transformedItemList.get(i);
                ItemContainer container = new ItemContainer();
                container.setCatalogItem(item);
                itemContainer.put(item.getResourceUrl(), container);
            }
        }

        //hole die AGOL Items des Kunden und iteriere ueber die Liste und fuege zum Container hinzu

       //iteriere ueber HashMap und holeItem raus

    }

    public void testProcessTransformation(){
        //hole nun die Items vom Katalog
        
        //persistiere die Daten

    }

    /*public static void main(String[] args) {
        String subscription  = args[0];
        String user = args[1];
        String password = args[2];

        AGOLService agolService = new AGOLService(subscription, user, password, "http://www.esri.de");
        List<String> resourceUrls = agolService.getResourceUrls();

        AGOLItem agolItem = new AGOLItem();
        agolItem.getAttributes().put("agol.accessInformation", "Text zu Credits");
        agolItem.getAttributes().put("agol.licenseInfo", "Die Zugangsbeschränkungen");
        agolItem.getAttributes().put("agol.description", "Die Beschreibung");
        agolItem.getAttributes().put("agol.extent", "5.725,50.1506,9.5315,52.602");
        agolItem.getAttributes().put("agol.tags", "WMS,NRW,DOP");
        // agolItem.getAttributes().put("agol.text", "{\"title\":\"NW_DTK100\",\"url\":\"http://www.wms.nrw.de/geobasis/adv_dtk100\",\"mapUrl\":\"http://www.wms.nrw.de/geobasis/adv_dtk100?\",\"version\":\"1.1.1\",\"layers\":[{\"name\":\"DTK100\",\"title\":\"DTK-V 100\"}],\"copyright\":\"Text zu Nutzungsbedingungen\",\"maxHeight\":5000,\"maxWidth\":5000,\"spatialReferences\":[25832,31466,3034,3035,3043,3044,3045,4258,4326,25831,25833,28992,31467],\"format\":null}");
        agolItem.getAttributes().put("agol.thumbnailURL", "http://www.wms.nrw.de/geobasis/DOP?SERVICE=WMS&REQUEST=GetMap&FORMAT=image/png&TRANSPARENT=TRUE&STYLES=&VERSION=1.1.1&LAYERS=0,Metadaten&WIDTH=200&HEIGHT=133&SRS=EPSG:4326&BBOX=5.59334,50.0578,9.74158,52.7998");
        agolItem.getAttributes().put("agol.title","Der Titel");
        agolItem.getAttributes().put("agol.type","WMS");
        agolItem.getAttributes().put("agol.typeKeywords","Data, Service, Web Map Service, OGC");
        agolItem.getAttributes().put("agol.url", "http://www.wms.nrw.de/geobasis/DOP");

        agolService.addItem(agolItem);

    } */
}
