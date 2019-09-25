package com.jin.utils;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class HttpUtil {
    private static final String TAG = "HttpUtil";

    public static String get(String url) {
        String result = "";
        try {
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
//            conn.setDoOutput(true);

            InputStream is = conn.getInputStream();
            result = IOUtils.toString(is, "GB2312");
            is.close();
//            Log.e(TAG, "get: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String get(String url, Map<String, String> headers) {
        String result = "";
        try {
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");
//            conn.setDoInput(true);
//            conn.setDoOutput(true);
            for (String key : headers.keySet()) {
                String value = headers.get(key);
                conn.addRequestProperty(key, value);
            }
            InputStream is = conn.getInputStream();
            result = IOUtils.toString(is, "GB2312");
            is.close();
            conn.disconnect();
//            Log.e(TAG, "get: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String post(String url, Map<String, Object> paramMap) {
        String result = "";
        try {
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("POST");
            String paramStr = prepareParam(paramMap);
//            Log.e(TAG, "post:paramStr= " + paramStr);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            os.write(paramStr.getBytes("GB2312"));
            os.close();
            InputStream is = conn.getInputStream();
            result = IOUtils.toString(is, "GB2312");
            is.close();
            conn.disconnect();
//            Log.e(TAG, "post: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String post(String url, Map<String, Object> paramMap, Map<String, String> headers) {
        String result = "";
        try {
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            for (String key : headers.keySet()) {
                String value = headers.get(key);
                conn.addRequestProperty(key, value);
            }

            conn.setRequestMethod("POST");
            String paramStr = prepareParam(paramMap);
//            Log.e(TAG, "post:paramStr=" + paramStr);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            os.write(paramStr.getBytes("GB2312"));
            os.close();
            InputStream is = conn.getInputStream();
            result = IOUtils.toString(is, "GB2312");
            is.close();
            conn.disconnect();
//            Log.e(TAG, "post: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String prepareParam(Map<String, Object> paramMap) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        if (paramMap == null || paramMap.isEmpty()) {
            return "";
        } else {
            for (String key : paramMap.keySet()) {
                String value = (String) paramMap.get(key);
                if (sb.length() < 1) {
                    sb.append(key).append("=").append(URLEncoder.encode(value, "GB2312"));
                } else {
                    sb.append("&").append(key).append("=").append(URLEncoder.encode(value, "GB2312"));
                }
            }
            return sb.toString();
        }
    }
}
