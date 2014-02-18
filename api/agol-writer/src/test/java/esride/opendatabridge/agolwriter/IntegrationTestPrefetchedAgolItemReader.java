package esride.opendatabridge.agolwriter;

import esride.opendatabridge.agolreader.PrefetchedAgolItemReader;
import esride.opendatabridge.item.AgolItem;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import org.junit.Test;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sma
 * Date: 06.02.14
 * Time: 17:51
 * To change this template use File | Settings | File Templates.
 */
@ContextConfiguration(locations = {"classpath:spring-config.xml"})
public class IntegrationTestPrefetchedAgolItemReader extends AbstractJUnit4SpringContextTests {

    @Autowired
    private PrefetchedAgolItemReader itemReader;

    @Test
    public void testPrefetchItem(){
        //Test this item
        AgolItem item = itemReader.getAgolItemById("2898119a298c43b786ffc8af26858a59");
        Assert.assertNotNull(item);
        //ask for correct items
        List<String> titleList = itemReader.getAgolItemIdByTitle("Schwangerschaftsabbr\u00FCche: Bundesl\u00E4nder, Jahre, Familienstand, Herkunfts-Bundesland oder Ausland");
        Assert.assertTrue(titleList.size() > 0);
        List<String> nullTitleList = itemReader.getAgolItemIdByTitle("Gibbet nicht");
        Assert.assertNull(nullTitleList);
        List<String> urlList = itemReader.getAgolItemIdByUrl("https://www-genesis.destatis.de/gis/cgi-bin/mapserv?map=/home/fgs/gis/gisdocs/wms/23311-0007.map&language=ger&");
        Assert.assertTrue(urlList.size() > 0);
        List<String> nullUrlList = itemReader.getAgolItemIdByUrl("Gibbet nicht");
        Assert.assertNull(nullUrlList);
    }

}
