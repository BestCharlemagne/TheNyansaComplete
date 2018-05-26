package com.candeapps.thenyansacomplete.utils;

import android.os.AsyncTask;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by mchan on 12/03/17.
 */
public abstract class JsonRequestAsyncTask extends AsyncTask<Object, String, ArrayList<String>> {
    private String hostUrl;
    public com.candeapps.thenyansacomplete.utils.JsonResponse delegate = null;
    private String apiToken;

    @Override
    protected ArrayList<String> doInBackground(Object... params) {
        hostUrl = (String) params[0];
        apiToken = (String) params[1];
        String item = (String) params[2];
        OkHttpClient client = getUnsafeOkHttpClient();

        return sendJsonRequest(client, hostUrl, buildApJson(item), buildClientJson(item));
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        delegate.setJsonResponse(result);
    }

    abstract protected String buildApJson(String item);
    abstract protected String buildClientJson(String item);

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private ArrayList<String> sendJsonRequest(Object... params) {
        OkHttpClient client = (OkHttpClient) params[0];
        String serviceUrl = params[1].toString();
        String apInfo = params[2].toString();
        String clientInfo = params[3].toString();

        RequestBody body = RequestBody.create(JSON, apInfo);
        Request request = new Request.Builder()
                .url(serviceUrl)
                .addHeader("api-token", apiToken)
                .post(body)
                .build();

        RequestBody clientBody = RequestBody.create(JSON, clientInfo);
        Request clientRequest = new Request.Builder()
                .url(serviceUrl)
                .addHeader("api-token", apiToken)
                .post(clientBody)
                .build();

        try {



            Response response = client.newCall(request).execute();
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.body().byteStream()));
            String line = "";
            StringBuilder builder = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                builder.append(line);
            }

            Response clientResponse = client.newCall(clientRequest).execute();
            BufferedReader clientRd = new BufferedReader(new InputStreamReader(clientResponse.body().byteStream()));
            String clientLine = "";
            StringBuilder clientBuilder = new StringBuilder();
            while ((clientLine = clientRd.readLine()) != null) {
                clientBuilder.append(clientLine);
            }

            ArrayList<String> whatToReturn2 = new ArrayList<>();
            whatToReturn2.add(builder.toString());
            whatToReturn2.add(clientBuilder.toString());

            return whatToReturn2;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

