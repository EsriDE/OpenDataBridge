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


}
