package esride.opendatabridge.reader.capabilities;

import esride.opendatabridge.httptransport.IHTTPRequest;
import esride.opendatabridge.reader.IResource;
import esride.opendatabridge.reader.ResourceException;
import org.apache.xml.resolver.tools.CatalogResolver;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 17.04.13
 * Time: 10:47
 * To change this template use File | Settings | File Templates.
 */
public class OGCCapabilitiesResource implements IResource{

    private IHTTPRequest request;

    private DocumentBuilder builder;

    public IHTTPRequest getRequest() {
        return request;
    }

    public void setRequest(IHTTPRequest request) {
        this.request = request;
    }

    public OGCCapabilitiesResource() throws ParserConfigurationException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

        builderFactory.setNamespaceAware(false);
        builderFactory.setIgnoringElementContentWhitespace(false);
        builder = builderFactory.newDocumentBuilder();
        builder.setEntityResolver(new CatalogResolver());
    }

    public Document getRecourceMetadata(String url, String serviceType) throws ResourceException {
        //URL untersuchen, was bisher da ist
        String lCheckedUrl = this.checkAndAppendParameter(url, serviceType);
        InputStream inputStream = null;
        try {
            inputStream = getRequest().executeGetRequest(lCheckedUrl, null);
            return builder.parse(inputStream);
        } catch (IOException e) {
            throw new ResourceException("No Capabilities from Resource with the url : " + url + " is available", e);
        } catch (SAXException e) {
            throw new ResourceException("No Capabilities from Resource with the url : " + url + " is available", e);
        }       
    }

    private String checkAndAppendParameter(String url, String serviceType){        
        //check Parameter
        //deconstruct url and put the things into a hashMap
        HashMap<String, String> urlParts = new HashMap<String,String>();
        if(!url.contains("?")){
            urlParts.put("basicUrl", url);    
        }else{
            int charPos = url.indexOf("?");
            urlParts.put("basicUrl", url.substring(0, charPos));
            String paramPart = url.substring(charPos + 1);
            StringTokenizer queryPart = new StringTokenizer(paramPart, "&");
            while(queryPart.hasMoreTokens()){
                String queryItem = queryPart.nextToken();
                int charDelimPos = queryItem.indexOf("=");
                urlParts.put(queryItem.substring(0, charDelimPos).toLowerCase(), queryItem.substring(charDelimPos + 1));
            }
        }

        
        boolean hasVersion = urlParts.containsKey("version");
        boolean hasService = urlParts.containsKey("service");
        boolean hasRequest = urlParts.containsKey("request");

        StringBuffer urlBuffer = new StringBuffer();
        urlBuffer.append(urlParts.get("basicUrl"));
        urlBuffer.append("?");
        urlParts.remove("basicUrl");
        if(hasRequest){
            urlBuffer.append("REQUEST=");
            urlBuffer.append(urlParts.get("request"));
            urlBuffer.append("&");
            urlParts.remove("request");
        }else{
            urlBuffer.append("REQUEST=GetCapabilities&");    
        }

        if(hasService){
            urlBuffer.append("SERVICE=");
            urlBuffer.append(urlParts.get("service"));
            urlBuffer.append("&");
            urlParts.remove("service");
        }else{
            urlBuffer.append("SERVICE=");
            if(serviceType.equalsIgnoreCase("view")){
                urlBuffer.append("WMS");
            }else{
                urlBuffer.append(serviceType.toUpperCase());
            }

            urlBuffer.append("&");
        }

        if(hasVersion){
            urlBuffer.append("VERSION=");
            urlBuffer.append(urlParts.get("version"));
            urlBuffer.append("&");
            urlParts.remove("version");            
        }
        
        if(urlParts.size() > 0){            
            Set<String> keySet = urlParts.keySet();
            Iterator<String> iter = keySet.iterator();
            while(iter.hasNext()){
                String key = iter.next();
                urlBuffer.append(key);
                urlBuffer.append("=");
                urlBuffer.append(urlParts.get(key));
                urlBuffer.append("&");
            }
            //urlParts.get()
        }

        if(urlBuffer.charAt(urlBuffer.length()-1) == '&'){
            urlBuffer.deleteCharAt(urlBuffer.length()-1);
        }

        return urlBuffer.toString();

    }

    
}
