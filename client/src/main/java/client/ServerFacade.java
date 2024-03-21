package client;

import com.google.gson.Gson;

import com.google.gson.Gson;
import model.UserData;
import server.responses.*;

import java.io.*;
import java.net.*;



public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }


    public void signIn(String username, String password) throws Exception{
        var path = "/session";
        UserData user = new UserData(username, password, null);
        RegisterUserResponse res = this.makeRequest("GET", path, user, RegisterUserResponse.class);
        System.out.println(res.toString());
    }

    public void register(String username, String password, String email) throws Exception {
        var path = "/user";
        UserData user = new UserData(username, password, email);
        RegisterUserResponse res = this.makeRequest("POST", path, user, RegisterUserResponse.class);
        System.out.println(res.toString());
    }


    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            if (request != null){
                http.setDoOutput(true);
            }
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, Exception {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new Exception("Error: Not successful");
        }
    }


    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }


}



