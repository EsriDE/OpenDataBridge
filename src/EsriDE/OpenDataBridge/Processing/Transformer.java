package Processing;

import java.util.List;

import AGOLWriter.AGOLService;

/**
 * Created with IntelliJ IDEA.
 * User: gvs
 * Date: 06.03.13
 * Time: 15:15
 * To change this template use File | Settings | File Templates.
 */
public class Transformer {

    public static void main(String[] args) {
        AGOLService agolService = new AGOLService("https://esri-de-1.maps.arcgis.com", "gerdatesride1", "gerdpwesride1", "http://www.esri.de");
        List<String> resourceUrls = agolService.getResourceUrls();
    }
}
