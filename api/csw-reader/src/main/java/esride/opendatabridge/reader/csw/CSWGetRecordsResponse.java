package esride.opendatabridge.reader.csw;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 10.04.13
 * Time: 13:58
 * To change this template use File | Settings | File Templates.
 */
public class CSWGetRecordsResponse {

    private XPath xPath;

    private String numbOfRecordsReturnedXPath = "/GetRecordsResponse/SearchResults/@numberOfRecordsReturned";
    private String numbOfRecordsMatchedXPath = "/GetRecordsResponse/SearchResults/@numberOfRecordsMatched";
    
    private String metadataXPath = "/GetRecordsResponse/SearchResults/MD_Metadata";

    private String resourceTypeXPath = "/MD_Metadata/identificationInfo/SV_ServiceIdentification/serviceType/LocalName";
    private String fileIdentifierXPath = "/MD_Metadata/fileIdentifier/CharacterString/text()";

    private String wmsUrlXPath = "/MD_Metadata/identificationInfo/SV_ServiceIdentification/containsOperations[SV_OperationMetadata/operationName/CharacterString = 'GetCapabilities']/SV_OperationMetadata/connectPoint/CI_OnlineResource/linkage/URL";


    public void setXPathValues(HashMap<String, String> xPathMap){
        if(xPathMap.containsKey("csw.response.xpath.numbOfRecordsReturnedXPath")){
            numbOfRecordsReturnedXPath = xPathMap.get("csw.response.xpath.numbOfRecordsReturnedXPath");
        }
        if(xPathMap.containsKey("csw.response.xpath.numbOfRecordsMatchedXPath")){
            numbOfRecordsMatchedXPath = xPathMap.get("csw.response.xpath.numbOfRecordsMatchedXPath");
        }
        if(xPathMap.containsKey("csw.response.xpath.metadataXPath")){
            metadataXPath = xPathMap.get("csw.response.xpath.metadataXPath");
        }
        if(xPathMap.containsKey("csw.response.xpath.resourceTypeXPath")){
            resourceTypeXPath = xPathMap.get("csw.response.xpath.resourceTypeXPath");
        }
        if(xPathMap.containsKey("csw.response.xpath.fileIdentifierXPath")){
            fileIdentifierXPath = xPathMap.get("csw.response.xpath.fileIdentifierXPath");
        }
        if(xPathMap.containsKey("csw.response.xpath.wmsUrlXPath")){
            wmsUrlXPath = xPathMap.get("csw.response.xpath.wmsUrlXPath");
        }
    }

    public CSWGetRecordsResponse() {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        xPath = xPathFactory.newXPath();
    }
    
    public CSWResponseObj createCSWResponse(Document responseDoc){
        CSWResponseObj responseObj = new CSWResponseObj();
        try {
            NodeList documentList = (NodeList)xPath.evaluate(metadataXPath, responseDoc, XPathConstants.NODESET);
            int nodelListLength = documentList.getLength();
            if(nodelListLength != 0){
                List<MetadataObject> metadataObjectList = new ArrayList<MetadataObject>(nodelListLength);
                for(int i=0; i<nodelListLength; i++){

                    MetadataObject object = new MetadataObject();
                    Node documentNode = documentList.item(i);
                    //whole document
                    object.setCswMetadataDoc((Document)documentList.item(i));
                    //fileIdentifier
                    object.setMetadataFileIdentifier(xPath.evaluate(fileIdentifierXPath, documentNode));
                    //wmsUrl
                    object.setOgcServiceUrl(xPath.evaluate(wmsUrlXPath, documentNode));
                    //resourceType
                    object.setMetadataResource(xPath.evaluate(resourceTypeXPath, documentNode));

                    metadataObjectList.add(object);
                }
                responseObj.setMetadataObjectList(metadataObjectList);
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        try {
            String numbOfRecordsMatched = xPath.evaluate(numbOfRecordsMatchedXPath, responseDoc);
            responseObj.setNumbOfRecordsMatchedInt(Integer.parseInt(numbOfRecordsMatched));

        } catch (XPathExpressionException e) {
            responseObj.setNumbOfRecordsMatchedInt(-1);
        }

        try {
            String numbOfRecordsReturned = xPath.evaluate(numbOfRecordsReturnedXPath, responseDoc);
            responseObj.setNumbOfRecordsReturnedInt(Integer.parseInt(numbOfRecordsReturned));

        } catch (XPathExpressionException e) {
            responseObj.setNumbOfRecordsReturnedInt(-1);
        }
        return responseObj;
    }


}
