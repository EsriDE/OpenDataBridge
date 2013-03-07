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
        String subscription  = args[0];
        String user = args[1];
        String password = args[2];

        AGOLService agolService = new AGOLService(subscription, user, password, "http://www.esri.de");
        List<String> resourceUrls = agolService.getResourceUrls();
    }
}
