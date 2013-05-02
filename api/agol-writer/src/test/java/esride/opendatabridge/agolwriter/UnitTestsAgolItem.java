package esride.opendatabridge.agolwriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import esride.opendatabridge.item.AgolItem;
import esride.opendatabridge.item.AgolItemFactory;
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

    ObjectMapper objectMapper;

    @Autowired
    private AgolItemFactory agolItemFactory;
    @Autowired
    private Map<String,String> jsonMap;

    @Test
    public void testCreateAgolItemFromJson() {
        AgolItem agolItem = agolItemFactory.createAgolItem(jsonMap.get("test01"));
        HashMap _expected;
        HashMap _returned;
        try {
            _expected = objectMapper.readValue(jsonMap.get("test01"), HashMap.class);
            _returned = agolItem.getAttributes();

            for (Object oneExpected : _expected.entrySet()) {
                Map.Entry expectedEntry = (Map.Entry) oneExpected;
                String oneKey = (String) expectedEntry.getKey();
/*
ToDo: Vernünftige Tests definieren.
json-Transformation funktioniert, aber die Properties werden in anderer Reihenfolge serialisiert. => String-Vergleich nicht möglich.
Was testet man eigentlich, wenn man auch hier den ObjectMapper verwendet..?
 */
                Assert.assertTrue("Result is missing key " + oneKey, _returned.containsKey(oneKey));
                Assert.assertEquals("Result has a a different result in entry '" + oneKey + "'.", _expected.get(oneKey), _returned.get(oneKey));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testToJson() {
//        // ToDo: the following line doesn't belong to a unit test!
//        String jsonResultItem = agolItemFactory.agolItemToJson(agolItemFactory.createAgolItem(jsonMap.get("test01")));
//        HashMap _expected;
//        HashMap _returned;
//        try {
//            _expected = objectMapper.readValue("{\"id\":\"cc9d0d626f264254b4ea912376ac6892\",\"item\":\"cc9d0d626f264254b4ea912376ac6892\",\"itemType\":\"text\",\"owner\":\"gerdatesride1\",\"uploaded\":1362625632000,\"modified\":1362643735000,\"guid\":null,\"name\":null,\"title\":\"DK5\",\"type\":\"WMS\",\"typeKeywords\":[\"Data\",\"OGC\",\"Service\",\"Web Map Service\"],\"description\":\"WebMapService des LGV Hamburg für die DK5\",\"tags\":[\"WMS\",\"Hamburg\",\"DK5\"],\"snippet\":\"WMS-Dienst für DK5\",\"thumbnail\":\"thumbnail/ago_downloaded.png\",\"documentation\":null,\"extent\":[[8.3817,52.4834],[11.7748,54.4899]],\"lastModified\":-1,\"spatialReference\":null,\"accessInformation\":\"Datenlizenz Deutschland - Namensnennung - Version 1.0; https://github.com/fraunhoferfokus/ogd-metadata/blob/master/lizenzen/BMI/Datenlizenz_Deutschland_Namensnennung_V1.md http://www.daten-deutschland.de/bibliothek/Datenlizenz_Deutschland/dl-de-by-1.0; dl-de-by-1.0; Namensnennung: Freie und Hansestadt Hamburg, Landesbetrieb Geoinformation und Vermessung\", \"licenseInfo\":null,\"culture\":\"de-de\",\"properties\":null,\"url\":\"http://gateway.hamburg.de/OGCFassade/HH_WMS_DK5.aspx\",\"access\":\"public\",\"size\":-1,\"numComments\":0,\"numRatings\":0,\"avgRating\":0.0,\"numViews\":0}", HashMap.class);
//            _returned = objectMapper.readValue(jsonResultItem, HashMap.class);
//
//            for (Object oneExpected : _expected.entrySet()) {
//                Map.Entry expectedEntry = (Map.Entry) oneExpected;
//                String oneKey = (String) expectedEntry.getKey();
///*
//ToDo: Vernünftige Tests definieren.
//json-Transformation funktioniert, aber die Properties werden in anderer Reihenfolge serialisiert. => String-Vergleich nicht möglich.
//Was testet man eigentlich, wenn man auch hier den ObjectMapper verwendet..?
// */
//                Assert.assertTrue("Result is missing key " + oneKey, _returned.containsKey(oneKey));
//                Assert.assertEquals("Result has a a different result in entry '" + oneKey + "'.", _expected.get(oneKey), _returned.get(oneKey));
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
