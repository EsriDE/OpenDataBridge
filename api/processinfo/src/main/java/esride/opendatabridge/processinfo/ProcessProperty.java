package esride.opendatabridge.processinfo;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 20.03.13
 * Time: 07:55
 * To change this template use File | Settings | File Templates.
 */
public class ProcessProperty {

    protected String propertyKey;
    protected String propertyValue;

    public String getPropertyKey() {
        return propertyKey;
    }

    public void setPropertyKey(String pPropertyKey) {
        propertyKey = pPropertyKey;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String pPropertyValue) {
        propertyValue = pPropertyValue;
    }
}
