package esride.opendatabridge.reader.csw;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 10.04.13
 * Time: 15:47
 * To change this template use File | Settings | File Templates.
 */
public class CSWResponseObj {

    private int numbOfRecordsMatchedInt;
    private int numbOfRecordsReturnedInt;
    private List<MetadataObject> metadataObjectList;

    public void setNumbOfRecordsMatchedInt(int numbOfRecordsMatchedInt) {
        this.numbOfRecordsMatchedInt = numbOfRecordsMatchedInt;
    }

    public void setNumbOfRecordsReturnedInt(int numbOfRecordsReturnedInt) {
        this.numbOfRecordsReturnedInt = numbOfRecordsReturnedInt;
    }

    public void setMetadataObjectList(List<MetadataObject> metadataObjectList) {
        this.metadataObjectList = metadataObjectList;
    }

    public List<MetadataObject> getMetadataDocuments(){
        return metadataObjectList;
    }

    public int getNumbersOfRecordMatched(){
        return numbOfRecordsMatchedInt;
    }

    public int getNumbersOfRecordReturned(){
        return numbOfRecordsReturnedInt;
    }
}
