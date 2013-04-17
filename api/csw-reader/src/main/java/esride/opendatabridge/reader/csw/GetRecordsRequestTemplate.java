package esride.opendatabridge.reader.csw;

import org.stringtemplate.v4.ST;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 09.04.13
 * Time: 15:44
 * To change this template use File | Settings | File Templates.
 */
public class GetRecordsRequestTemplate {

    //private ST templateObj;
    private StringBuilder sb;
    
    private IGetRecordsTemplateLocation templateLocation;

    public void setTemplateLocation(IGetRecordsTemplateLocation templateLocation) {
        this.templateLocation = templateLocation;
    }

    public void setGetRecordsTemplate(String pocessId) throws IOException {
        InputStream stream = templateLocation.getTemplateFile(pocessId);
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
    }

    public String generateGetRecordsTemplate(HashMap<String, String> template){
        ST templateObj = new ST(sb.toString(), '$', '$');
        Set<String> templateKeys = template.keySet();
        Iterator<String> iter =  templateKeys.iterator();
        while(iter.hasNext()){
            String key = iter.next();
            templateObj.add(key, template.get(key));
        }

        return templateObj.render();
    }
}
