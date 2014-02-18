package esride.opendatabridge.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import esride.opendatabridge.agolclient.AgolGenerateTokenRequest;
import esride.opendatabridge.agolclient.AgolGenerateTokenResponse;
import esride.opendatabridge.httptransport.HTTPRequest;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: sma
 * Date: 11.02.14
 * Time: 17:50
 * To change this template use File | Settings | File Templates.
 */
public class AgolTokenService {

    private String tokenUrl;
    private String username;
    private String password;
    private String referer;

    private Long tokenExpires;

    private String token;

    private ObjectMapper objectMapper;
    private HTTPRequest httpRequest;

    public void setObjectMapper(ObjectMapper pObjectMapper) {
        objectMapper = pObjectMapper;
    }

    public void setHttpRequest(HTTPRequest pHttpRequest) {
        httpRequest = pHttpRequest;
    }

    public AgolTokenService(String pTokenUrl, String pUsername, String pPassword, String pReferer) {
        tokenUrl = pTokenUrl;
        username = pUsername;
        password = pPassword;
        referer = pReferer;
    }

    private void createToken() throws IOException {
        AgolGenerateTokenRequest tokenRequest = new AgolGenerateTokenRequest(username, password, referer, tokenUrl);
        AgolGenerateTokenResponse tokenResponse =  tokenRequest.excReqWithJsonResp(httpRequest, objectMapper);

        token = tokenResponse.getToken();
        tokenExpires = tokenResponse.getTokenExpires().getTime();
    }

    public String getToken() throws IOException {
        if ((token == null) || (System.currentTimeMillis() >= tokenExpires)) {
            createToken();
        }
        return token;
    }
}
