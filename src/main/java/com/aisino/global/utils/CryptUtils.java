package com.aisino.global.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.SecureRandom;

/**
 * @Package : com.aisino.global.utils
 * @Class : CryptUtils
 * @Description :
 * @Author : liuya
 * @CreateDate : 2017-08-21 星期一 11:23:09
 * @Version : V1.0.0
 * @Copyright : 2017 liuya Inc. All rights reserved.
 */
public class CryptUtils {
    public static final String S_KEY = "97DC0D40FCFB425EA2A94C3B34ED99F9";

    /**
     * @Method : decryptFile
     * @Description : 解密后转为inputstream
     * @param filePath :
     * @return : java.io.InputStream
     * @author : liuya
     * @CreateDate : 2017-08-21 星期一 13:19:37
     */
    public static InputStream decryptFile(String filePath) {
        InputStream inputStream = null;
        File sourceFile = new File(filePath);
        if (!sourceFile.exists()) {
            try {
                throw new FileNotFoundException("文件不存在！");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            inputStream = new FileInputStream(sourceFile);
            byte[] encryptByte = input2byte(inputStream);
            byte[] decryptByte = decrypt(S_KEY, encryptByte);
            inputStream = byte2Input(decryptByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    /**
     * @Method : encrypt
     * @Description : 加密方法
     * @param key :
     * @param src :
     * @return : byte[]
     * @author : liuya
     * @CreateDate : 2017-08-21 星期一 13:20:05
     */
    public static byte[] encrypt(String key, byte[] src) throws Exception {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(key.getBytes("UTF-8"));
            kgen.init(128, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKey skey = new SecretKeySpec(enCodeFormat, "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            return cipher.doFinal(src);
        } catch (Exception e) {
            throw new Exception("加密时出现异常！", e);
        }
    }

    /**
     * @Method : decrypt
     * @Description : 解密方法
     * @param key :
     * @param src :
     * @return : byte[]
     * @author : liuya
     * @CreateDate : 2017-08-21 星期一 13:20:28
     */
    public static byte[] decrypt(String key, byte[] src) throws Exception {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(key.getBytes("UTF-8"));
            kgen.init(128, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] deCodeFormat = secretKey.getEncoded();
            SecretKey secretkey = new SecretKeySpec(deCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretkey);
            return cipher.doFinal(src);
        } catch (Exception e) {
            throw new Exception("解密时出现异常！", e);
        }
    }

    /**
     * @Method : byte2Input
     * @Description : byte[]转inputStream
     * @param buf :
     * @return : java.io.InputStream
     * @author : liuya
     * @CreateDate : 2017-08-21 星期一 13:20:41
     */
    public static final InputStream byte2Input(byte[] buf) {
        return new ByteArrayInputStream(buf);
    }

    /**
     * @Method : input2byte
     * @Description : inputStream转byte[]
     * @param inStream :
     * @return : byte[]
     * @author : liuya
     * @CreateDate : 2017-08-21 星期一 13:21:10
     */
    public static final byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 1024)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }
}
