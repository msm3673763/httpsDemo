package com.masm.utils.Crypt;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * DES加解密工具类
 * create by masiming in 2019/1/13 00:51:57
 */
public class DesCrypt {

    /**
     * 算法
     */
    private static final String ALGORITHM = "DES";

    /**
     * DES加密
     * @param input 原始字符串
     * @param key 秘钥(DES秘钥长度8位)
     * @return 加密后的字符串
     */
    public static String encrypt(String input, String key) {
        try {
            //加密算法三部曲
            //1.创建Cipher对象
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            //秘钥工厂
            SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
            KeySpec keySpec = new DESKeySpec(key.getBytes());//秘钥规则对象
            SecretKey secretKey = skf.generateSecret(keySpec);

            //2.初始化模式：加密/解密
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            //3.加密/解密
            byte[] bytes = cipher.doFinal(input.getBytes());
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException("加密出错，" + e.getMessage());
        }
    }

    /**
     * DES加密
     * @param base64Str 加密字符串
     * @param key 秘钥(DES秘钥长度8位)
     * @return 解密后字符串
     */
    public static String decrypt(String base64Str, String key) {
        try {
            byte[] bytes = Base64.getDecoder().decode(base64Str.getBytes());

            //加密算法三部曲
            //1.创建Cipher对象
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            //秘钥工厂
            SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
            KeySpec keySpec = new DESKeySpec(key.getBytes());//秘钥规则对象
            SecretKey secretKey = skf.generateSecret(keySpec);

            //2.初始化模式：加密/解密
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            //3.加密/解密
            bytes = cipher.doFinal(bytes);
            return new String(bytes);
        } catch (Exception e) {
            throw new RuntimeException("解密出错，" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String key = "12345678";
        String encrypt = encrypt("欢迎来到黑马程序员", key);
        System.out.println(encrypt + "，长度：" + encrypt.length());
        String decrypt = decrypt(encrypt, key);
        System.out.println(decrypt);
    }
}
