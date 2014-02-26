package esride.opendatabridge.agolclient;

import java.util.Date;

/**
 * The attributes for the Generate Token Response
 * User: Markus Stecker, con terra GmbH
 * 04.12.13
 * Time: 19:12
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
