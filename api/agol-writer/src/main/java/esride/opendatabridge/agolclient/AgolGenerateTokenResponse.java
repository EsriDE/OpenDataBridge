package esride.opendatabridge.agolclient;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: sma
 * Date: 04.12.13
 * Time: 19:12
 * To change this template use File | Settings | File Templates.
 */
public class AgolGenerateTokenResponse {

    private String token;

    private Date tokenExpires;

    public AgolGenerateTokenResponse(String pToken, Date pTokenExpires) {
        token = pToken;
        tokenExpires = pTokenExpires;

    }

    public String getToken() {
        return token;
    }

    public Date getTokenExpires() {
        return tokenExpires;
    }
}
