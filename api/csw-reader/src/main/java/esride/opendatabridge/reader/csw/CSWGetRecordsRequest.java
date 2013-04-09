package esride.opendatabridge.reader.csw;


import org.stringtemplate.v4.ST;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 08.04.13
 * Time: 18:20
 * To change this template use File | Settings | File Templates.
 */
public class CSWGetRecordsRequest {
    
    private boolean isLastPage = false;
    
    private int nextStartPosition;
    private int actualStartPosition;
    private int maxRecordsSize;
            
    private String filter;
    private String outputSchema;
    private String typeNames;

    private String numbOfRecordsReturnedXPath;
    private String numbOfRecordsMatchedXPath;
    
    private String resourceType;
    private String additionalMetadataUrlXPath;
    private String additionalMetadataUrl;

    /*public void executeGetRecordsRequest(){
        ST getRecTemplate = new ST()
    } */
    
}
