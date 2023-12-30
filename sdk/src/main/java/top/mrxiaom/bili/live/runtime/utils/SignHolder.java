package top.mrxiaom.bili.live.runtime.utils;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.InputStreamEntity;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class SignHolder {
    private static String accessKeySecret;
    private static String accessKeyId;

    public static void push(String accessKeyId, String accessKeySecret) {
        SignHolder.accessKeyId = accessKeyId;
        SignHolder.accessKeySecret = accessKeySecret;
    }

    public static Map<String, String> orderAndMd5(String jsonParam) {
        var keyValuePairs = new HashMap<String, String>();
        keyValuePairs.put("x-bili-accesskeyid", accessKeyId);
        keyValuePairs.put("x-bili-content-md5", md5(jsonParam));
        keyValuePairs.put("x-bili-signature-method", "HMAC-SHA256");
        keyValuePairs.put("x-bili-signature-nonce", UUID.randomUUID().toString());
        keyValuePairs.put("x-bili-signature-version", "1.0");
        keyValuePairs.put("x-bili-timestamp", String.valueOf(System.currentTimeMillis() / 1000L));
        return keyValuePairs;
    }

    public static String md5(String source) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(source.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String calculateSignature(Map<String, String> keyValuePairs) {
        List<String> sig = new ArrayList<>();
        List<String> keys = new ArrayList<>(keyValuePairs.keySet());
        Collections.sort(keys);
        for (var key : keys) {
            sig.add(key + ":" + keyValuePairs.get(key));
        }
        return hmacSHA256(String.join("\n", sig), accessKeySecret);
    }

    public static String hmacSHA256(String message, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(key);
            byte[] bytes = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(bytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setReqHeader(HttpEntityEnclosingRequestBase req, String jsonParam, String cookie) {
        var sortDic = orderAndMd5(jsonParam);
        var auth = calculateSignature(sortDic);

        List<String> keys = new ArrayList<>(sortDic.keySet());
        Collections.sort(keys);
        for (var key : keys) {
            req.setHeader(key, sortDic.get(key));
        }

        req.setHeader("Authorization", auth);
        req.setHeader("Accept", "application/json");
        req.setHeader("Content-Type", "application/json");


        if (cookie != null) {
            req.setHeader("Cookie", cookie);
        }

        var bytes = jsonParam.getBytes(StandardCharsets.UTF_8);
        //req.setHeader("Content-Length", String.valueOf(bytes.length));
        InputStreamEntity entity = new InputStreamEntity(new ByteArrayInputStream(bytes));
        req.setEntity(entity);
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString().toLowerCase();
    }
}
