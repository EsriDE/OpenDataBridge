package esride.opendatabridge.pipeline.validator;

import esride.opendatabridge.pipeline.IPipeline;
import esride.opendatabridge.pipeline.InvalidObjectException;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sma
 * Date: 18.02.14
 * Time: 19:24
 * To change this template use File | Settings | File Templates.
 */
public class PublicLicenseValidator implements IPipeline {

    private static Logger sLogger = Logger.getLogger(PublicLicenseValidator.class);

    public List<String> publicLicenseList;

    public String licenseInfoName;

    public List<String> getPublicLicenseList() {
        return publicLicenseList;
    }

    public void setPublicLicenseList(List<String> publicLicenseList) {
        this.publicLicenseList = publicLicenseList;
    }

    public String getLicenseInfoName() {
        return licenseInfoName;
    }

    public void setLicenseInfoName(String licenseInfoName) {
        this.licenseInfoName = licenseInfoName;
    }

    public void examineAndChangeObject(HashMap<String, String> elements) throws InvalidObjectException {
        String licenseInfoValue = elements.get(licenseInfoName);
        if(sLogger.isDebugEnabled()){
            sLogger.debug("PublicLicense-Validator (licenseInfo): " + licenseInfoValue);
        }

        if(licenseInfoValue != null && licenseInfoValue.trim().length() > 0){
            boolean hasPublicLisence = false;
            for(int i=0; i<publicLicenseList.size(); i++){
                String publicId = publicLicenseList.get(i);
                if(licenseInfoValue.contains(publicId)){
                    hasPublicLisence = true;
                    break;
                }
            }
            if(!hasPublicLisence){
                sLogger.info("PublicLicense-Validator: No valid license available");
                throw new InvalidObjectException("PublicLicense-Validator: No public license found. Public license are: " + publicLicenseList);
            }
        }

    }
}
