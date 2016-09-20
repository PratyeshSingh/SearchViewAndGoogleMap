package com.example.pratyeshsingh.accoliteassignment.api;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Hashtable;

public class MyDownloader implements Runnable {

    HttpListener httpListener;
    String resMessage = "";
    int resCode = -11;
    String urlstring;
    String requeststring = null;
    String requestType = "";
    Hashtable<String, String> header;
    int reqID;
    String filename;
    String stateHTTP = "";
    String uuid = "";
    Context context = null;

    /**
     * Constructor : MPDownloader
     * <p>
     * For POST request
     *
     * @param urlstring
     * @param requeststring
     */
    public MyDownloader(String urlstring, String requeststring, String filename, int reqID, String requestType, Hashtable<String, String> header) {
        this.urlstring = urlstring;
        this.requeststring = requeststring;
        this.reqID = reqID;
        this.filename = filename;
        this.requestType = requestType;
        this.header = header;
    }

    /**
     * For GET request
     *
     * @param urlstring
     */
    public MyDownloader(String urlstring) {
        this.urlstring = urlstring;
        header = new Hashtable<String, String>();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Return requested url
     *
     * @return
     */

    public String getURL() {
        return urlstring;
    }

    /**
     * @return filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @return request id
     */
    public int getReqID() {
        return reqID;
    }

    /**
     * Return Response String
     *
     * @return
     */
    public String getResponse() {
        return resMessage;
    }

    /**
     * Return Response Code
     *
     * @return
     */
    public int getResCode() {
        return resCode;
    }

    /**
     * Add header to SOAP Envelope.
     *
     * @param key
     * @param value
     */
    public void addHeader(String key, String value) {
        this.header.put(key, value);
    }

    /**
     * @param httpListener
     */

    public void addHttpLIstener(HttpListener httpListener) {
        this.httpListener = httpListener;
    }

    /**
     * Send the POST request to requested url of non-blocking
     */
    public void sendPost() {
        stateHTTP = "Non-Blocking state: ";
        Log.i(">>>>>>>>>>>>>>", "in the send post method");
        Thread t = new Thread(this);
        t.start();
    }

    /**
     * Send the POST request to requested url of blocking
     */
    public void sendPostBlocking() {
        stateHTTP = "Blocking state: ";
        send();
    }

    public void run() {
        send();
    }

    /**
     * Called to download the XML file from the server.
     */

    public void send() {
        try {

            URL connectURL = new URL(urlstring);

            HttpURLConnection hc = (HttpURLConnection) connectURL.openConnection();

            try {

                hc.setRequestMethod("GET");
                hc.setConnectTimeout(5 * 1000);

                Enumeration<String> kyes = header.keys();
                while (kyes.hasMoreElements()) {
                    String key = (String) kyes.nextElement();
                    String value = (String) header.get(key);
                    hc.setRequestProperty(key, value);
                }

                resCode = hc.getResponseCode();
                if (resCode == 200) {
                    InputStream is = hc.getInputStream();
                    try {
                        int length;
                        StringBuffer sb = new StringBuffer();
                        byte[] readChunk = new byte[1024 * 2];
                        while ((length = is.read(readChunk)) > 0) {
                            sb.append(new String(readChunk, 0, length));
                        }
                        resMessage = sb.toString();
                        is.close();
                    } finally {
                        try {
                            is.close();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }
                } else if (resCode == 400) {
                    InputStream is = hc.getInputStream();
                    try {
                        int length;
                        StringBuffer sb = new StringBuffer();
                        byte[] readChunk = new byte[1024 * 2];
                        while ((length = is.read(readChunk)) > 0) {
                            sb.append(new String(readChunk, 0, length));
                        }
                        resMessage = sb.toString();
                        is.close();
                    } finally {
                        try {
                            is.close();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }
                } else {
                    resMessage = "ERROR";
                }
            } catch (SocketTimeoutException ss) {
                resMessage = "TIMEOUT";
            } catch (Exception e) {
                resMessage = "ERROR";
                e.printStackTrace();
            } finally {
                hc.disconnect();
                httpListener.notifyRespons(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @param uuid the uuid to set
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
