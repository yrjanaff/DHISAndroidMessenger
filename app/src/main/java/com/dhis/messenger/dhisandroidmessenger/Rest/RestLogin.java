package com.dhis.messenger.dhisandroidmessenger.Rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.sql.SQLOutput;

import javax.net.ssl.SSLHandshakeException;

/**
 * Created by yrjanaff on 16.03.2016.
 */
public class RestLogin {

    private final static int IO_EXCEPTION = 10;
    private final static int SOCKET_TIMEOUT_EXCEPTION = 11;
    private final static int OTHER_EXCEPTION = 12;
    private final static int SSL_HANDSHAKE_EXCEPTION = 13;
    private final static int SERVICE_UNAVAILABLE_EXCEPTION = 503;
    public final static int JSON_EXCEPTION = 14;
    public final static int MALFORMED_URL_EXCEPTION = 15;

    public String DHISLogin(String server, String userCredentials) {
        int code = -1;
        String body = "";
        HttpURLConnection connection = null;
        try {
            URL url = new URL(server);
            connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.setConnectTimeout(3000);
            connection.setRequestProperty("Authorization", "Basic " + userCredentials);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            code = connection.getResponseCode();
            body = readInputStream(connection.getInputStream());
        } catch (SSLHandshakeException e) {
            code = SSL_HANDSHAKE_EXCEPTION;
        } catch (MalformedURLException e) {
            code = HttpURLConnection.HTTP_NOT_FOUND;
        } catch (SocketTimeoutException e) {
            code = HttpURLConnection.HTTP_GATEWAY_TIMEOUT;
        } catch (IOException one) {
            try {
                if (connection != null) {
                    code = connection.getResponseCode();
                } else
                    code = IO_EXCEPTION;
            } catch (IOException two) {
                code = IO_EXCEPTION;
            }
        } catch (Exception e) {
            code = OTHER_EXCEPTION;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        System.out.println("\n\nCode: " + code + "\n\nBody: " + body);
        return body;
    }


    private static String readInputStream(InputStream stream)
            throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream));
        try {
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }

            return builder.toString();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
