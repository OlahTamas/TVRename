import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class TVDBConnector extends APIConnectorBase {
    protected String k;
    protected String token;
    protected final String baseUrl = "https://api.thetvdb.com";

    public TVDBConnector() {
        this.k = null;
        this.token = null;
        try {
            this.k = this.ldi("tvdb");
        } catch (IOException e) {
        }
    }

    public String authenticate() throws MalformedURLException {
        this.getToken();

        return this.token;
    }

    protected void getToken() throws MalformedURLException {

        String data =  "{\"apikey\": \"".concat(this.k).concat("\"}");
        JSONObject responseObject = new JSONObject(this.sendJsonRequest("/login", data));
        this.token = responseObject.getString("token");
    }

    public JSONObject searchSeriesByName(String seriesName) throws MalformedURLException {
        return new JSONObject(this.sendGetRequest("/search/series", "?name=supernatural"));
    }

    public JSONObject searchSeriesById(int id) throws MalformedURLException {
        return new JSONObject(this.sendGetRequest("/series/".concat(Integer.toString(id)), ""));
    }

    protected String sendJsonRequest(String path, String request) throws MalformedURLException {
        if (this.k == null) {
            return null;
        }
        URL url = new URL(this.baseUrl.concat(path));
        try {
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            if (this.token != null) {
                connection.setRequestProperty("Authorization", "Bearer ".concat(this.token));
            }
            connection.setDoInput(true);
            connection.setDoOutput(true);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.write(request.getBytes());
            DataInputStream dataInputStream = new DataInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(dataInputStream));
            StringBuilder responseBuilder = new StringBuilder();
            String response;
            while ((response = reader.readLine()) != null) {
                responseBuilder.append(response);
            }

            return responseBuilder.toString();
        } catch (IOException e) {
            return null;
        }
    }

    protected String sendGetRequest(String path, String request) throws MalformedURLException {
        if (this.k == null) {
            return null;
        }
        URL url = new URL(this.baseUrl.concat(path).concat(request));
        try {
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            if (this.token != null) {
                connection.setRequestProperty("Authorization", "Bearer ".concat(this.token));
            }
            connection.setDoInput(true);
            connection.setDoOutput(true);
            DataInputStream dataInputStream = new DataInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(dataInputStream));
            StringBuilder responseBuilder = new StringBuilder();
            String response;
            while ((response = reader.readLine()) != null) {
                responseBuilder.append(response);
            }

            return responseBuilder.toString();
        } catch (IOException e) {
            return null;
        }
    }

}
