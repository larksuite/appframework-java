/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class SimpleHttpClient implements HttpClient {

    @Override
    public String doPostJson(String url, int connectTimeout, int readTimeout, Map<String, String> headers, String data) throws HttpException {
        return doRequest(url, connectTimeout, readTimeout, headers, true, data);
    }

    @Override
    public String doGet(String url, int connectTimeout, int readTimeout, Map<String, String> headers) throws HttpException {
        return doRequest(url, connectTimeout, readTimeout, headers, false, null);
    }

    @Override
    public String doPostFile(String url, int connectTimeout, int readTimeout, Map<String, String> headers, List<FileField> files) throws HttpException {

        HttpURLConnection conn = null;
        try {
            conn = newHttpConnection(url, connectTimeout, readTimeout, headers);

            MultiPartHttpRequester multiPartHttpRequester = new MultiPartHttpRequester(conn);

            for (FileField f : files) {
                multiPartHttpRequester.addFile(f);
            }

            multiPartHttpRequester.finish();

            String respStr = streamToString(conn.getInputStream());
            checkHttpCode(conn.getResponseCode(), respStr);
            return respStr;

        } catch (IOException ioe) {
            throw new HttpException(ioe);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private String doRequest(String url, int connectTimeout, int readTimeout, Map<String, String> header, boolean isPost, String requestData) throws HttpException {

        HttpURLConnection conn = null;
        try {
            conn = newHttpConnection(url, connectTimeout, readTimeout, header);

            if (isPost) {
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(requestData.getBytes(StandardCharsets.UTF_8));
                }
            } else {
                conn.setRequestMethod("GET");
            }

            String respStr = streamToString(conn.getInputStream());

            checkHttpCode(conn.getResponseCode(), respStr);

            return respStr;

        } catch (IOException ioe) {
            throw new HttpException(ioe);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private void checkHttpCode(int httpCode, String respStr) throws HttpException {
        if (httpCode != HttpURLConnection.HTTP_OK) {
            throw new HttpException(httpCode, respStr);
        }
    }

    private HttpURLConnection newHttpConnection(String url, int connectTimeout, int readTimeout, Map<String, String> header) throws IOException {
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();

        conn.setConnectTimeout(connectTimeout);
        conn.setReadTimeout(readTimeout);
        conn.setUseCaches(false);
        conn.setDoInput(true);

        if (header != null) {
            final HttpURLConnection c = conn;
            header.forEach(c::setRequestProperty);
        }

        return conn;
    }

    private String streamToString(InputStream is) throws IOException {
        final StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        return response.toString();
    }

    private static class MultiPartHttpRequester {
        final static String LINE_FEED = "\r\n";

        private String boundary;

        private DataOutputStream dos;

        MultiPartHttpRequester(HttpURLConnection conn) throws IOException {
            boundary = "*****" + System.currentTimeMillis() + "*****";

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            dos = new DataOutputStream(conn.getOutputStream());
        }

        void addFile(FileField f) throws IOException {
            dos.writeBytes("--" + boundary + LINE_FEED);
            dos.writeBytes("Content-Disposition: form-data; name=\"" + f.fieldName + "\"; filename=\"" + f.fileName + "\"" + LINE_FEED);
            dos.writeBytes("Content-Type: " + URLConnection.guessContentTypeFromName(f.fileName) + LINE_FEED);
            dos.writeBytes("Content-Transfer-Encoding: binary" + LINE_FEED);
            dos.writeBytes(LINE_FEED);
            dos.flush();

            MixUtils.copyStream(f.is, dos);

            dos.writeBytes(LINE_FEED);
            dos.flush();
        }

        void finish() throws IOException {
            dos.writeBytes(LINE_FEED);
            dos.writeBytes("--" + boundary + "--" + LINE_FEED);
            dos.close();
        }
    }
}
