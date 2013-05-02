package esride.opendatabridge.reader.ckan;

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
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 24.04.13
 * Time: 14:15
 * To change this template use File | Settings | File Templates.
 */
public class CkanSearchResponse {

    private XPath xPath;
    private DocumentBuilder builder;

    Properties xpathValue;

    public void setXpathValue(Properties xpathValue) {
        this.xpathValue = xpathValue;
    }

    private List<String> supportedResourceTypes;

    public void setSupportedResourceTypes(List<String> supportedResourceTypes) {
        this.supportedResourceTypes = supportedResourceTypes;
    }

    //private LinkedList<String> resourceTypeQueue = new LinkedList<String>();

    public CkanSearchResponse() throws ParserConfigurationException, IOException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(false);
        builderFactory.setIgnoringElementContentWhitespace(false);
        builder = builderFactory.newDocumentBuilder();

        xpathValue = new Properties();
        xpathValue.load(this.getClass().getResourceAsStream("/ckanxpath.properties"));

        XPathFactory xPathFactory = XPathFactory.newInstance();
        xPath = xPathFactory.newXPath();


    }

    public CatalogResponseObj createCkanResponse(Document responseDoc){
        CatalogResponseObj responseObj = new CatalogResponseObj();
        try {
            NodeList documentList = (NodeList)xPath.evaluate(xpathValue.getProperty("metadata.xpath"), responseDoc, XPathConstants.NODESET);
            int nodelListLength = documentList.getLength();
            if(nodelListLength != 0){
                List<MetadataObject> metadataObjectList = new ArrayList<MetadataObject>(nodelListLength);
                for(int i=0; i<nodelListLength; i++){


                    Document metaDocument = builder.newDocument();
                    Node importedNode = metaDocument.importNode(documentList.item(i), true);
                    metaDocument.appendChild(importedNode);

                    //get ResourceType and put the resourceType to the list
                    String resourceTypeXPath = xpathValue.getProperty("resourcetype.xpath");
                    List<String> resourceTypeList = new ArrayList<String>();
                    NodeList resourceTypeNodes = (NodeList)xPath.evaluate(resourceTypeXPath, metaDocument, XPathConstants.NODESET);
                    for(int j=0; j<resourceTypeNodes.getLength(); j++){
                        String resourceType = resourceTypeNodes.item(j).getTextContent();
                        if(supportedResourceTypes.contains(resourceType)){
                            resourceTypeList.add(resourceType);
                        }
                    }

                    for(int k=0; k<resourceTypeList.size(); k++){
                        MetadataObject object = new MetadataObject();
                        object.setMetadataDoc(metaDocument);
                        object.setResourceType(resourceTypeList.get(k));

                        String conactedXPath = xpathValue.getProperty("resourceurl.prefix.xpath") + resourceTypeList.get(k) + xpathValue.getProperty("resourceurl.suffix.xpath");
                        String resourceUrl = xPath.evaluate(conactedXPath, metaDocument);
                        object.setResourceUrl(resourceUrl);

                        String fileIdentifier = xPath.evaluate(xpathValue.getProperty("fileidentifier.xpath"), metaDocument) + "-" + resourceTypeList.get(k);
                        object.setMetadataFileIdentifier(fileIdentifier);

                        if(resourceTypeList.get(k).equals("WMS") || resourceTypeList.get(k).equals("KML")){
                            object.setCapabilitiesUrl(resourceUrl);
                        }
                        
                        metadataObjectList.add(object);
                        //Knoten ermitteln
                    }

                }
                responseObj.setMetadataObjectList(metadataObjectList);
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        try {
            String numbOfRecordsMatched = xPath.evaluate(xpathValue.getProperty("numbOfRecordsMatched.xpath"), responseDoc);
            responseObj.setNumbOfRecordsMatchedInt(Integer.parseInt(numbOfRecordsMatched));

        } catch (XPathExpressionException e) {
            responseObj.setNumbOfRecordsMatchedInt(-1);
        }

        try {
            String numbOfRecordsReturned = xPath.evaluate(xpathValue.getProperty("numbOfRecordsReturned.xpath"), responseDoc);
            responseObj.setNumbOfRecordsReturnedInt(Integer.parseInt(numbOfRecordsReturned));

        } catch (XPathExpressionException e) {
            responseObj.setNumbOfRecordsReturnedInt(-1);
        }

        return responseObj;
    }
}
