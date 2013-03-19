package esride.opendatabridge.agolwriter;

import esride.opendatabridge.item.AGOLItem;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 12.03.13
 * Time: 17:10
 * To change this template use File | Settings | File Templates.
 */
@ContextConfiguration(locations = {"classpath:spring-config.xml"})
public class IntegrationTestAgolService extends AbstractJUnit4SpringContextTests {

    @Autowired
    private AGOLService agolService;

    @Test
    public void testAll() {
        HashMap<String, AGOLItem> agolItems = agolService.getAllItems("WMS");

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

    }

    @Test
    public void testAddItem(){
        //....
    }
}
