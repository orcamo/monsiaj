package org.montsuqi.monsiaj.client;

import org.montsuqi.monsiaj.util.Messages;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.Proxy;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * <p>
 * A class that manages OpenID Connect authentication.</p>
 */
public class OpenIdConnect {

    static final Logger logger = LogManager.getLogger(OpenIdConnect.class);
    private String sso_sp_uri;
    private String sso_user;
    private String sso_password;

    private String client_id;
    private String state;
    private String redirect_uri;
    private String nonce;
    private String authentication_request_uri;
    private String request_url;

    public OpenIdConnect(String sso_user, String sso_password, String sso_sp_uri) throws IOException {
        this.sso_sp_uri = sso_sp_uri;
        this.sso_user = sso_user;
        this.sso_password = sso_password;
    }

    public void connect() throws IOException {
      logger.info("try OpenId connect...");
      // バックエンドサーバへのログイン要求
      doAuthenticationRequestToRP();
      // 認証サーバへのログイン要求
      doAuthenticationRequestToIP();
      // 認証サーバへのログイン
      doLoginToIP();
      // バックエンドサーバへのsession id発行要求
      doLoginToRP();
    }

    private void doAuthenticationRequestToRP() throws IOException {
      JSONObject res = request(sso_sp_uri, "GET", new JSONObject());
      this.client_id = res.getString("client_id");
      this.state = res.getString("state");
      this.redirect_uri = res.getString("redirect_uri");
      this.nonce = res.getString("nonce");
      this.authentication_request_uri = res.getJSONObject("header").getString("Location");
    }

    private void doAuthenticationRequestToIP() throws IOException {
      JSONObject params = new JSONObject();
      params.put("response_type", "code");
      params.put("scope", "openid");
      params.put("client_id", client_id);
      params.put("state", state);
      params.put("redirect_uri", redirect_uri);
      params.put("nonce", nonce);
      JSONObject res = request(authentication_request_uri, "POST", params);
      this.request_url = res.getString("request_url");
    }

    private void doLoginToIP() {
    }

    private void doLoginToRP() {
    }

    private JSONObject request(String uri, String method, JSONObject params) throws IOException {
      URL url = new URL(uri);
      HttpURLConnection con = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);
      con.setInstanceFollowRedirects(false);
      con.setRequestProperty("Accept", "application/json");
      if (method == "GET") {
          con.setDoOutput(false);
          con.setRequestMethod("GET");
      } else {
          con.setDoOutput(true);
          con.setRequestMethod("POST");
          con.setRequestProperty("Content-Type", "application/json");
          OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
          osw.write(params.toString());
          osw.close();
      }

      con.connect();
      int resCode = con.getResponseCode();
      switch (resCode) {
          case 200:
              break;
          case 302:
              break;
          default:
              String message = con.getResponseMessage();
              logger.info("http error: " + resCode + " " + message);
              System.exit(1);
      }

      ByteArrayOutputStream body = getHTTPBody(con);
      JSONObject result = new JSONObject(body.toString("UTF-8"));

      JSONObject headerObj = new JSONObject();
      headerObj.put("Location", con.getHeaderField("Location"));
      result.put("header", headerObj);

      con.disconnect();

      return result;
    }

    private ByteArrayOutputStream getHTTPBody(HttpURLConnection con) {
        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream()) {
            try (BufferedOutputStream bos = new BufferedOutputStream(bytes)) {
                BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
                int length;
                while ((length = bis.read()) != -1) {
                    bos.write(length);
                }
            }
            return bytes;
        } catch (IOException ex) {
            return new ByteArrayOutputStream();
        }
    }
}
