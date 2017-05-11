package com.ruitu.entrance_guard.support.utils;

import android.util.Log;

import com.beanu.arad.utils.Base64Coder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by wubin on 2017/5/11.
 */

public class SecretUtils {
    private static final String MAC_NAME = "HmacSHA1";
    private static final String ENCODING = "UTF-8";

    /**
     * 使用 HMAC-SHA1 签名方法对对encryptText进行签名
     *
     * @param encryptText 被签名的字符串
     * @param encryptKey  密钥
     * @return
     * @throws Exception
     */
    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception {
        byte[] data = encryptKey.getBytes(ENCODING);
        //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
        //生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance(MAC_NAME);
        //用给定密钥初始化 Mac 对象
        mac.init(secretKey);

        byte[] text = encryptText.getBytes(ENCODING);
        Log.i("wubin", "HmacSha1:" + new String(mac.doFinal(text)));
        //完成 Mac 操作
        return mac.doFinal(text);
    }

    /**
     * Base64编码
     */
    public static String Base64Encode(String str) {
        String result = Base64Coder.encodeString(str);
        Log.i("wubin", "Base64:" + result);
        return result;
    }

    /**
     * UrlEncode
     */
    public static String UrlEncode(String str) throws UnsupportedEncodingException {
        String result = URLEncoder.encode(str, "utf-8");
        Log.i("wubin", "UrlEncode:" + result);
        return result;
    }

    /**
     * 获取心知天气的签名
     */
    public static String getXinzhiSig(String str, String key) throws Exception {
        String hmacSha1_str = new String(HmacSHA1Encrypt(str, key));
        String base64_str = Base64Encode(hmacSha1_str);
        String url_str = UrlEncode(base64_str);

        Log.i("wubin", "最终的签名是:" + url_str);
        return url_str;
    }
}
