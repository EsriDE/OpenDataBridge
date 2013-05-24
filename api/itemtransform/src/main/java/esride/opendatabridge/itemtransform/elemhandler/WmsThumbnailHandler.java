package esride.opendatabridge.itemtransform.elemhandler;

import esride.opendatabridge.itemtransform.ItemGenerationException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 21.05.13
 * Time: 19:48
 * To change this template use File | Settings | File Templates.
 */
public class WmsThumbnailHandler implements IElemHandler {

    private XPathFactory xPathFactory;
    private XPath xPath;
    
    public WmsThumbnailHandler() {
        xPathFactory = XPathFactory.newInstance();
        xPath = xPathFactory.newXPath();
    }

    private static Logger sLogger = Logger.getLogger(WmsThumbnailHandler.class);
    
    public String handleElement(String element, Document xmlDoc) {
        try {
            StringBuffer wmsThumbnailUrl = new StringBuffer();
            wmsThumbnailUrl.append((String)xPath.evaluate("/WMT_MS_Capabilities/Capability/Request/GetMap/DCPType/HTTP/Get/OnlineResource/@href|/WMS_Capabilities/Capability/Request/GetMap/DCPType/HTTP/Get/OnlineResource/@href", xmlDoc, XPathConstants.STRING));
            wmsThumbnailUrl.append("?");
            wmsThumbnailUrl.append("REQUEST=GetMap&SERVICE=WMS&WIDTH=200&HEIGHT=133&FORMAT=image/png&TRANSPARENT=TRUE&STYLES=");

            wmsThumbnailUrl.append("&VERSION=");
            String wmsVersion = (String)xPath.evaluate("/WMS_Capabilities/@version | /WMT_MS_Capabilities/@version", xmlDoc, XPathConstants.STRING);
            wmsThumbnailUrl.append((String)xPath.evaluate("/WMS_Capabilities/@version | /WMT_MS_Capabilities/@version", xmlDoc, XPathConstants.STRING));

            wmsThumbnailUrl.append("&LAYERS=");
            NodeList layerList = (NodeList)xPath.evaluate("/WMT_MS_Capabilities/Capability//Layer/Name/text() | /WMS_Capabilities/Capability//Layer/Name/text()", xmlDoc, XPathConstants.NODESET);
            StringBuffer layerBuffer = new StringBuffer();
            int nodeListLength = layerList.getLength();
            for(int j=0; j<nodeListLength; j++){
                layerBuffer.append(layerList.item(j).getNodeValue());
                if(j < nodeListLength-1){
                    layerBuffer.append(",");
                }
            }
            wmsThumbnailUrl.append(layerBuffer.toString());
            String bboxXPath = null;
            

            if(wmsVersion.equals("1.3.0")){
                wmsThumbnailUrl.append("&CRS=");                
            }else{
                wmsThumbnailUrl.append("&SRS=");    
            }            
            HashSet<String> srsSet = new HashSet<String>();
            NodeList srsList = (NodeList)xPath.evaluate("/WMS_Capabilities/Capability/Layer/CRS/text()|/WMT_MS_Capabilities/Capability/Layer/SRS/text()", xmlDoc, XPathConstants.NODESET);
            int srsListLength = srsList.getLength();
            for(int j=0; j<srsListLength; j++){
                srsSet.add(srsList.item(j).getNodeValue());
            }            
            if(srsSet.contains("EPSG:4326")){
                wmsThumbnailUrl.append("EPSG:4326");
                bboxXPath = "concat(/WMT_MS_Capabilities/Capability/Layer/BoundingBox[@SRS='EPSG:4326']/@minx|/WMS_Capabilities/Capability/Layer/BoundingBox[@CRS='EPSG:4326']/@minx, ',', /WMT_MS_Capabilities/Capability/Layer/BoundingBox[@SRS='EPSG:4326']/@miny|/WMS_Capabilities/Capability/Layer/BoundingBox[@CRS='EPSG:4326']/@miny, ',', /WMT_MS_Capabilities/Capability/Layer/BoundingBox[@SRS='EPSG:4326']/@maxx|/WMS_Capabilities/Capability/Layer/BoundingBox[@CRS='EPSG:4326']/@maxx, ',', /WMT_MS_Capabilities/Capability/Layer/BoundingBox[@SRS='EPSG:4326']/@maxy|/WMS_Capabilities/Capability/Layer/BoundingBox[@CRS='EPSG:4326']/@maxy)";
            }else{
                String srsCode = srsList.item(0).getNodeValue();
                wmsThumbnailUrl.append(srsCode);
                StringBuffer bboxBuffer = new StringBuffer();
                bboxBuffer.append("concat(/WMT_MS_Capabilities/Capability/Layer/BoundingBox[@SRS='");
                bboxBuffer.append(srsCode);
                bboxBuffer.append("']/@minx|/WMS_Capabilities/Capability/Layer/BoundingBox[@CRS='");
                bboxBuffer.append(srsCode);
                bboxBuffer.append("']/@minx, ',', /WMT_MS_Capabilities/Capability/Layer/BoundingBox[@SRS='");
                bboxBuffer.append(srsCode);
                bboxBuffer.append("']/@miny|/WMS_Capabilities/Capability/Layer/BoundingBox[@CRS='");
                bboxBuffer.append(srsCode);
                bboxBuffer.append("']/@miny, ',', /WMT_MS_Capabilities/Capability/Layer/BoundingBox[@SRS='");
                bboxBuffer.append(srsCode);
                bboxBuffer.append("']/@maxx|/WMS_Capabilities/Capability/Layer/BoundingBox[@CRS='");
                bboxBuffer.append(srsCode);
                bboxBuffer.append("']/@maxx, ',', /WMT_MS_Capabilities/Capability/Layer/BoundingBox[@SRS='");
                bboxBuffer.append(srsCode);
                bboxBuffer.append("']/@maxy|/WMS_Capabilities/Capability/Layer/BoundingBox[@CRS='");
                bboxBuffer.append(srsCode);
                bboxBuffer.append("']/@maxy)");
                bboxXPath = bboxBuffer.toString();
            }

            wmsThumbnailUrl.append("&BBOX=");
            wmsThumbnailUrl.append((String)xPath.evaluate(bboxXPath, xmlDoc, XPathConstants.STRING));
            return wmsThumbnailUrl.toString();
        } catch (XPathExpressionException e) {
            String lMessage = "Cannot evaluate xpath as a string: " + element;
            sLogger.error(lMessage);
            throw new ItemGenerationException(lMessage, e);
        }


    }
}
