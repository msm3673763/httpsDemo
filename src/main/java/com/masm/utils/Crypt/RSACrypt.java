package com.masm.utils.Crypt;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA加解密工具类
 * create by masiming in 2019/1/13 01:19:12
 */
public class RSACrypt {

    /**
     * 算法
     */
    private static final String ALGORITHM = "RSA";

    /**
     * RSA加密原文长度不能超过117字节
     */
    private static final int ENCRYPT_MAX_SIZE = 117;

    /**
     * RSA解密长度不能超过128字节
     */
    private static final int DECRYPT_MAX_SIZE = 128;

    /**
     * 密钥长度，用来初始化
     */
    private static final int KEYSIZE = 1024;

    /**
     * 生成秘钥对
     */
    public static void generateKeyPair() {
        try {
            //秘钥对生成器
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);

            //RSA算法要求有一个可信任的随机数源
            SecureRandom secureRandom = new SecureRandom();

            //利用上面的随机数据源初始化这个KeyPairGenerator对象
            keyPairGenerator.initialize(KEYSIZE, secureRandom);

            //秘钥生成对象
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            //生成秘钥对：公钥和私钥
            PrivateKey privateKey = keyPair.getPrivate();
            System.out.println("私钥：" + Base64.getEncoder().encodeToString(privateKey.getEncoded()));
            PublicKey publicKey = keyPair.getPublic();
            System.out.println("公钥：" + Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        } catch (Exception e) {
            throw new RuntimeException("加密出错，" + e.getMessage());
        }
    }

    /**
     * 获取私钥
     * @param privateKeyStr 私钥（Base64字符串）
     * @return PrivateKey
     * @throws Exception 异常
     */
    public static PrivateKey getPrivateKey(String privateKeyStr) throws Exception {
        //base64解码
        byte[] decodeBytes = Base64.getDecoder().decode(privateKeyStr);
        //秘钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        //私钥
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decodeBytes));
    }

    /**
     * 获取公钥
     * @param publicKeyStr 公钥（Base64字符串）
     * @return PublicKey
     * @throws Exception 异常
     */
    public static PublicKey getPublicKey(String publicKeyStr) throws Exception {
        //base64解码
        byte[] decodeBytes = Base64.getDecoder().decode(publicKeyStr);
        //秘钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        //私钥
        return keyFactory.generatePublic(new X509EncodedKeySpec(decodeBytes));
    }

    /**
     * 私钥加密
     * @param input 原始字符串
     * @param privateKeyStr 私钥（base64编码）
     * @return 加密后的字符串
     */
    public static String encryptByPrivateKey(String input, String privateKeyStr) {
        try {
            //Cipher加密三部曲
            //1.创建Cipher对象
            Cipher cipher = Cipher.getInstance(ALGORITHM);

            //获取私钥
            PrivateKey privateKey = getPrivateKey(privateKeyStr);

            //2.初始化模式：加密/解密
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);

            //3.加密/解密
            byte[] doFinal = doFinal(input.getBytes(), cipher, ENCRYPT_MAX_SIZE);

            return Base64.getEncoder().encodeToString(doFinal);
        } catch (Exception e) {
            throw new RuntimeException("加密出错，" + e.getMessage());
        }
    }

    /**
     * 公钥加密
     * @param input 原始字符串
     * @param publicKeyStr 公钥（base64编码）
     * @return 加密后的字符串
     */
    public static String encryptByPublicKey(String input, String publicKeyStr) {
        try {
            //Cipher加密三部曲
            //1.创建Cipher对象
            Cipher cipher = Cipher.getInstance(ALGORITHM);

            //获取公钥
            PublicKey publicKey = getPublicKey(publicKeyStr);

            //2.初始化模式：加密/解密
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            //3.加密/解密
            byte[] doFinal = doFinal(input.getBytes(), cipher, ENCRYPT_MAX_SIZE);

            return Base64.getEncoder().encodeToString(doFinal);
        } catch (Exception e) {
            throw new RuntimeException("加密出错，" + e.getMessage());
        }
    }

    /**
     * 公钥解密
     * @param base64Str 加密字符串
     * @param publicKeyStr 公钥
     * @return 解密后字符串
     */
    public static String decryptByPublicKey(String base64Str, String publicKeyStr) {
        try {
            byte[] bytes = Base64.getDecoder().decode(base64Str.getBytes());

            //加密算法三部曲
            //1.创建Cipher对象
            Cipher cipher = Cipher.getInstance(ALGORITHM);

            //获取公钥
            PublicKey publicKey = getPublicKey(publicKeyStr);

            //2.初始化模式：加密/解密
            cipher.init(Cipher.DECRYPT_MODE, publicKey);

            //3.加密/解密
            byte[] doFinal = doFinal(bytes, cipher, DECRYPT_MAX_SIZE);
            return new String(doFinal);
        } catch (Exception e) {
            throw new RuntimeException("解密出错，" + e.getMessage());
        }
    }

    /**
     * 私钥解密
     * @param base64Str 加密字符串
     * @param privateKeyStr 私钥
     * @return 解密后字符串
     */
    public static String decryptByPrivateKey(String base64Str, String privateKeyStr) {
        try {
            byte[] bytes = Base64.getDecoder().decode(base64Str.getBytes());

            //加密算法三部曲
            //1.创建Cipher对象
            Cipher cipher = Cipher.getInstance(ALGORITHM);

            //获取私钥
            PrivateKey privateKey = getPrivateKey(privateKeyStr);

            //2.初始化模式：加密/解密
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            //3.加密/解密
            byte[] doFinal = doFinal(bytes, cipher, DECRYPT_MAX_SIZE);
            return new String(doFinal);
        } catch (Exception e) {
            throw new RuntimeException("解密出错，" + e.getMessage());
        }
    }

    /**
     * 加解密
     * @param bytes 加解密字节数组
     * @param cipher cipher
     * @param maxSize 分段最大位数
     * @return byte[]
     */
    private static byte[] doFinal(byte[] bytes, Cipher cipher, int maxSize) throws Exception {
        int offset = 0;
        byte[] buffer;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (bytes.length - offset > 0) {
            if (bytes.length - offset >= maxSize) {
                buffer = cipher.doFinal(bytes, offset, maxSize);
                offset += maxSize;
            } else {
                buffer = cipher.doFinal(bytes, offset, bytes.length - offset);
                offset = bytes.length;
            }
            byteArrayOutputStream.write(buffer);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static void main(String[] args) {
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCxNiIjGqs7wMe+DAy0P109V6zAq9EfRhvtok+8HcqKNwiYKtqrnCevnLGH2c3S0HoHg7uER/imZuZ4BZzOtw3br96B8hukZNVXxwwdESMvIalE67ACmZOnz4Y56uHBfqnf9fV9su+L/pTXP10DNcIujMXDV4T+K1iF2nuBoVErEQIDAQAB";
        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALE2IiMaqzvAx74MDLQ/XT1XrMCr0R9GG+2iT7wdyoo3CJgq2qucJ6+csYfZzdLQegeDu4RH+KZm5ngFnM63Dduv3oHyG6Rk1VfHDB0RIy8hqUTrsAKZk6fPhjnq4cF+qd/19X2y74v+lNc/XQM1wi6MxcNXhP4rWIXae4GhUSsRAgMBAAECgYBAm/H6RoDjujeY7J+9H9UIxmxvmrUV5+PK0gCsH1h25/W0NPug0cAOYNzBIQHPuL6k+rxlUDAxyxZ/79vp6gec0llDAzwKvbacbGkfDXct7Mvjo+KDBMMlUXhwqOp6FRIFd1/5r1+MWoDIaek/F3YNbnZxlQiMOl6d0omoo4eTeQJBAPPIqbPBMGDSUDW6If54KvAAeEyPVJ151m8oU6Ev+WrrVfxtB4x/X+nNduqqh5rkN3eV3V5ocKduzk18VE7ppfMCQQC6F3VplWxY2iS92jm+UzYJJBzUM/97rmYJgBji3vDm+urQcK/BadqXDHjAVWOTnLUZuy3/hGEV1BKbd1qbnxfrAkAhEpkLPOtOR1oAX/caqbzRgI7RfCRzlMLlo6fs1zBUNAcfTr9WbTVa57f5UBdDiTCJNbiphSu7W0n7syFQ71vHAkBS0E+6/fkqjWGeb3gU/9jjNoPx13YRp2nXPn9UrxwL3owS2KMOcroJUE6IYTegIDPj0I3G0TWXTjo9n17eNG2jAkEAnGaL1Mc1XKMw/kszuXKgszFJVNOzI06ecP4XdbQaBM+quzgfo6yg7leEMFA5zEQ/JT0SADlCWnjXyzCfa4kpSQ==";

        String input = "后来发现确实是我自己的错误,rsa 加密后返回的字符串使用Base64编码的,忘了解密直接getBytes()就出错了\n" +
                "api 指出:BadPaddingException - 如果此 Cipher 为解密模式，并且未请求填充（或不填充），但解密的数据没有用适当的填充字节进行限制\n" +
                "只要是cipher.doFinal()解密时报 BadPaddingException 肯定是传入的字节数组有问题,传的不是加密时生成的数组\n";
        System.out.println("原文byte长度：" + input.getBytes().length);

        //私钥加密
        String encryptByPrivateKey = RSACrypt.encryptByPrivateKey(input, privateKey);
        System.out.println("私钥加密：" + encryptByPrivateKey);

        //公钥解密
        String decryptByPublicKey = RSACrypt.decryptByPublicKey(encryptByPrivateKey, publicKey);
        System.out.println("公钥解密：" + decryptByPublicKey);

        //公钥加密
        String encryptByPublicKey = RSACrypt.encryptByPublicKey(input, publicKey);
        System.out.println("公钥加密：" + encryptByPublicKey);

        //私钥解密
        String decryptByPrivateKey = RSACrypt.decryptByPrivateKey(encryptByPublicKey, privateKey);
        System.out.println("私钥解密：" + decryptByPrivateKey);

    }
}
