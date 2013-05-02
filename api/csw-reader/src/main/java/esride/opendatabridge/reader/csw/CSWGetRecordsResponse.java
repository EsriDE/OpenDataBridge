package esride.opendatabridge.reader.csw;

import esride.opendatabridge.reader.MetadataObject;
import esride.opendatabridge.reader.request.CatalogResponseObj;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.Properties;


/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 10.04.13
 * Time: 13:58
 * To change this template use File | Settings | File Templates.
 */
public class CSWGetRecordsResponse {

    private XPath xPath;
    private DocumentBuilder builder;

    //private String numbOfRecordsReturnedXPath = "/GetRecordsResponse/SearchResults/@numberOfRecordsReturned";
    //private String numbOfRecordsMatchedXPath = "/GetRecordsResponse/SearchResults/@numberOfRecordsMatched";
    
    //private String metadataXPath = "/GetRecordsResponse/SearchResults/MD_Metadata";

    Properties xpathValue;

    public void setXpathValue(Properties xpathValue) {
        this.xpathValue = xpathValue;
    }

    public CSWGetRecordsResponse() throws ParserConfigurationException, IOException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(false);
        builderFactory.setIgnoringElementContentWhitespace(false);
        builder = builderFactory.newDocumentBuilder();

        xpathValue = new Properties();
        xpathValue.load(this.getClass().getResourceAsStream("/isoxpath.properties"));
        
        
        XPathFactory xPathFactory = XPathFactory.newInstance();
        xPath = xPathFactory.newXPath();
    }
    
    public CatalogResponseObj createCSWResponse(Document responseDoc){
        CatalogResponseObj responseObj = new CatalogResponseObj();
        try {
            NodeList documentList = (NodeList)xPath.evaluate(xpathValue.getProperty("csw.reader.metadata.xpath"), responseDoc, XPathConstants.NODESET);
            int nodelListLength = documentList.getLength();
            if(nodelListLength != 0){
                List<MetadataObject> metadataObjectList = new ArrayList<MetadataObject>(nodelListLength);
                for(int i=0; i<nodelListLength; i++){

                    MetadataObject object = new MetadataObject();
                    Node documentNode = documentList.item(i);
                    //whole document
                    Document metaDocument = builder.newDocument();
                    Node importedNode = metaDocument.importNode(documentList.item(i), true);
                    metaDocument.appendChild(importedNode);
                    object.setMetadataDoc(metaDocument);
                    //get ResourceType (WMS, ...)
                    String resourceTypeXPath = xpathValue.getProperty("csw.reader.resourcetype.xpath");
                    String resourceType = xPath.evaluate(resourceTypeXPath, metaDocument);

                    String resourceUrl = xpathValue.getProperty("csw.reader." + resourceType + ".resourceurl.xpath");
                    //fileIdentifier
                    object.setMetadataFileIdentifier(xPath.evaluate(xpathValue.getProperty("csw.reader.fileidentifier.xpath"), metaDocument));
                    //resourceUrl
                    object.setResourceUrl(xPath.evaluate(resourceUrl, metaDocument));
                    //capabilitiesUrl
                    String capabilitiesXPath = xpathValue.getProperty("csw.reader." + resourceTypeXPath + ".capbilitiesurl.xpath");
                    if(capabilitiesXPath != null && capabilitiesXPath.trim().length() > 0){
                        object.setCapabilitiesUrl(xPath.evaluate(capabilitiesXPath, metaDocument));
                    }

                    //resourceType
                    object.setResourceType(xPath.evaluate(resourceTypeXPath, metaDocument));

                    metadataObjectList.add(object);
                }
                responseObj.setMetadataObjectList(metadataObjectList);
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        try {
            String numbOfRecordsMatched = xPath.evaluate(xpathValue.getProperty("csw.reader.numbOfRecordsMatched.xpath"), responseDoc);
            responseObj.setNumbOfRecordsMatchedInt(Integer.parseInt(numbOfRecordsMatched));

        } catch (XPathExpressionException e) {
            responseObj.setNumbOfRecordsMatchedInt(-1);
        }

        try {
            String numbOfRecordsReturned = xPath.evaluate(xpathValue.getProperty("csw.reader.numbOfRecordsReturned.xpath"), responseDoc);
            responseObj.setNumbOfRecordsReturnedInt(Integer.parseInt(numbOfRecordsReturned));

        } catch (XPathExpressionException e) {
            responseObj.setNumbOfRecordsReturnedInt(-1);
        }
        return responseObj;
    }


}
