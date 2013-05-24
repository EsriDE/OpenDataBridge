package esride.opendatabridge.itemtransform.elemhandler;

import junit.framework.TestCase;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 23.05.13
 * Time: 18:37
 * To change this template use File | Settings | File Templates.
 */
public class UnitTestWmsThumbnailHandler extends TestCase {
    private InputStream capabilities111 = this.getClass().getResourceAsStream("/capabilities/Capabilities-1.1.1.xml");
    private InputStream capabilities130 = this.getClass().getResourceAsStream("/capabilities/Capabilities-1.0.3.xml");
    
    public void testGetMapVersion111Url(){
        WmsThumbnailHandler handler = new WmsThumbnailHandler();
        String wmsUrl = handler.handleElement("", createDoc(capabilities111));
        assertTrue(wmsUrl.length() > 0);
    }

    public void testGetMapVersion130Url(){
        WmsThumbnailHandler handler = new WmsThumbnailHandler();
        String wmsUrl = handler.handleElement("", createDoc(capabilities130));
        assertTrue(wmsUrl.length() > 0);
    }
    
    private Document createDoc(InputStream stream){
        DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
        fac.setValidating(false);
        fac.setNamespaceAware(false);
        try {
            DocumentBuilder builder = fac.newDocumentBuilder();
            return builder.parse(stream);         
        } catch (ParserConfigurationException e) {
            fail(e.getMessage());
        } catch (SAXException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }        
        return null;
    }
}
