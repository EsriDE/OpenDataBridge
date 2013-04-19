package esride.opendatabridge.agolwriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import esride.opendatabridge.item.AGOLItem;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nik
 * Date: 19.04.13
 * Time: 14:09
 * To change this template use File | Settings | File Templates.
 */
@ContextConfiguration(locations = {"classpath:spring-config.xml"})
public class UnitTestsAgolItem extends AbstractJUnit4SpringContextTests {

    @Autowired
    private AGOLItem agolItem;

    @Test
    public void testToJson() {
        String jsonResultItem = agolItem.toJson();
//        Assert.assertEquals(jsonResultItem,"{\"id\":\"cc9d0d626f264254b4ea912376ac6892\",\"item\":\"cc9d0d626f264254b4ea912376ac6892\",\"itemType\":\"text\",\"owner\":\"gerdatesride1\",\"uploaded\":1362625632000,\"modified\":1362643735000,\"guid\":null,\"name\":null,\"title\":\"DK5\",\"type\":\"WMS\",\"typeKeywords\":[\"Data\",\"OGC\",\"Service\",\"Web Map Service\"],\"description\":\"WebMapService des LGV Hamburg für die DK5\",\"tags\":[\"WMS\",\"Hamburg\",\"DK5\"],\"snippet\":\"WMS-Dienst für DK5\",\"thumbnail\":\"thumbnail/ago_downloaded.png\",\"documentation\":null,\"extent\":[[8.3817,52.4834],[11.7748,54.4899]],\"lastModified\":-1,\"spatialReference\":null,\"accessInformation\":\"Datenlizenz Deutschland - Namensnennung - Version 1.0; https://github.com/fraunhoferfokus/ogd-metadata/blob/master/lizenzen/BMI/Datenlizenz_Deutschland_Namensnennung_V1.md http://www.daten-deutschland.de/bibliothek/Datenlizenz_Deutschland/dl-de-by-1.0; dl-de-by-1.0; Namensnennung: Freie und Hansestadt Hamburg, Landesbetrieb Geoinformation und Vermessung\", \"licenseInfo\":null,\"culture\":\"de-de\",\"properties\":null,\"url\":\"http://gateway.hamburg.de/OGCFassade/HH_WMS_DK5.aspx\",\"access\":\"public\",\"size\":-1,\"numComments\":0,\"numRatings\":0,\"avgRating\":0.0,\"numViews\":0}");
/*
ToDo: Vernünftige Tests definieren.
json-Transformation funktioniert, aber die Properties werden in anderer Reihenfolge serialisiert.
Was testet man eigentlich, wenn man auch hier den ObjectMapper verwendet..?
 */

        ObjectMapper objectMapper = new ObjectMapper();
        HashMap _expected;
        HashMap _returned;
        try {
            _expected = objectMapper.readValue("{\"id\":\"cc9d0d626f264254b4ea912376ac6892\",\"item\":\"cc9d0d626f264254b4ea912376ac6892\",\"itemType\":\"text\",\"owner\":\"gerdatesride1\",\"uploaded\":1362625632000,\"modified\":1362643735000,\"guid\":null,\"name\":null,\"title\":\"DK5\",\"type\":\"WMS\",\"typeKeywords\":[\"Data\",\"OGC\",\"Service\",\"Web Map Service\"],\"description\":\"WebMapService des LGV Hamburg für die DK5\",\"tags\":[\"WMS\",\"Hamburg\",\"DK5\"],\"snippet\":\"WMS-Dienst für DK5\",\"thumbnail\":\"thumbnail/ago_downloaded.png\",\"documentation\":null,\"extent\":[[8.3817,52.4834],[11.7748,54.4899]],\"lastModified\":-1,\"spatialReference\":null,\"accessInformation\":\"Datenlizenz Deutschland - Namensnennung - Version 1.0; https://github.com/fraunhoferfokus/ogd-metadata/blob/master/lizenzen/BMI/Datenlizenz_Deutschland_Namensnennung_V1.md http://www.daten-deutschland.de/bibliothek/Datenlizenz_Deutschland/dl-de-by-1.0; dl-de-by-1.0; Namensnennung: Freie und Hansestadt Hamburg, Landesbetrieb Geoinformation und Vermessung\", \"licenseInfo\":null,\"culture\":\"de-de\",\"properties\":null,\"url\":\"http://gateway.hamburg.de/OGCFassade/HH_WMS_DK5.aspx\",\"access\":\"public\",\"size\":-1,\"numComments\":0,\"numRatings\":0,\"avgRating\":0.0,\"numViews\":0}", HashMap.class);
            _returned = objectMapper.readValue(jsonResultItem, HashMap.class);

            for (Object oneExpected : _expected.entrySet()) {
                Map.Entry expectedEntry = (Map.Entry) oneExpected;
                String oneKey = (String) expectedEntry.getKey();
                String oneValue = (String) expectedEntry.getValue();
                Assert.assertTrue("Result is missing key " + oneKey, _returned.containsKey(oneKey));
                Assert.assertEquals("Result has a a different result in entry '" + oneKey + "'.", _expected.get(oneKey), _returned.get(oneKey));
            }

//            Assert.assertEquals(_expected,_returned);


        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("testToJson beendet");


//        AGOLItem agolItem = new AGOLItem();
//        agolItem.getAttributes().put("agol.accessInformation", "Text zu Credits");
//        agolItem.getAttributes().put("agol.licenseInfo", "Die Zugangsbeschränkungen");
//        agolItem.getAttributes().put("agol.description", "Die Beschreibung");
//        agolItem.getAttributes().put("agol.extent", "5.725,50.1506,9.5315,52.602");
//        agolItem.getAttributes().put("agol.tags", "WMS,NRW,DOP");
//        // agolItem.getAttributes().put("agol.text", "{\"title\":\"NW_DTK100\",\"url\":\"http://www.wms.nrw.de/geobasis/adv_dtk100\",\"mapUrl\":\"http://www.wms.nrw.de/geobasis/adv_dtk100?\",\"version\":\"1.1.1\",\"layers\":[{\"name\":\"DTK100\",\"title\":\"DTK-V 100\"}],\"copyright\":\"Text zu Nutzungsbedingungen\",\"maxHeight\":5000,\"maxWidth\":5000,\"spatialReferences\":[25832,31466,3034,3035,3043,3044,3045,4258,4326,25831,25833,28992,31467],\"format\":null}");
//        agolItem.getAttributes().put("agol.thumbnailURL", "http://www.wms.nrw.de/geobasis/DOP?SERVICE=WMS&REQUEST=GetMap&FORMAT=image/png&TRANSPARENT=TRUE&STYLES=&VERSION=1.1.1&LAYERS=0,Metadaten&WIDTH=200&HEIGHT=133&SRS=EPSG:4326&BBOX=5.59334,50.0578,9.74158,52.7998");
//        agolItem.getAttributes().put("agol.title","Der Titel");
//        agolItem.getAttributes().put("agol.type","WMS");
//        agolItem.getAttributes().put("agol.typeKeywords","Data, Service, Web Map Service, OGC");
//        agolItem.getAttributes().put("agol.url", "http://www.wms.nrw.de/geobasis/DOP");
//
//        agolService.addItem(agolItem);

    }

    @Test
    public void testAddItem(){
        //....
    }
}
