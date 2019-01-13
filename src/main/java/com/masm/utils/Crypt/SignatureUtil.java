package com.masm.utils.Crypt;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

/**
 * 签名/验签工具类：
 *      1、数字签名是消息摘要和非对称加密的组合
 *      2、签名使用私钥，验签使用公钥
 * create by masiming in 2019/1/13 14:58:12
 */
public class SignatureUtil {

    /**
     * 算法：SHA256WITHRSA
     */
    private static final String ALGORITHM = "SHA256WITHRSA";

    private static final String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCxNiIjGqs7wMe+DAy0P109V6zAq9EfRhvtok+8HcqKNwiYKtqrnCevnLGH2c3S0HoHg7uER/imZuZ4BZzOtw3br96B8hukZNVXxwwdESMvIalE67ACmZOnz4Y56uHBfqnf9fV9su+L/pTXP10DNcIujMXDV4T+K1iF2nuBoVErEQIDAQAB";
    private static final String privateKeyStr = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALE2IiMaqzvAx74MDLQ/XT1XrMCr0R9GG+2iT7wdyoo3CJgq2qucJ6+csYfZzdLQegeDu4RH+KZm5ngFnM63Dduv3oHyG6Rk1VfHDB0RIy8hqUTrsAKZk6fPhjnq4cF+qd/19X2y74v+lNc/XQM1wi6MxcNXhP4rWIXae4GhUSsRAgMBAAECgYBAm/H6RoDjujeY7J+9H9UIxmxvmrUV5+PK0gCsH1h25/W0NPug0cAOYNzBIQHPuL6k+rxlUDAxyxZ/79vp6gec0llDAzwKvbacbGkfDXct7Mvjo+KDBMMlUXhwqOp6FRIFd1/5r1+MWoDIaek/F3YNbnZxlQiMOl6d0omoo4eTeQJBAPPIqbPBMGDSUDW6If54KvAAeEyPVJ151m8oU6Ev+WrrVfxtB4x/X+nNduqqh5rkN3eV3V5ocKduzk18VE7ppfMCQQC6F3VplWxY2iS92jm+UzYJJBzUM/97rmYJgBji3vDm+urQcK/BadqXDHjAVWOTnLUZuy3/hGEV1BKbd1qbnxfrAkAhEpkLPOtOR1oAX/caqbzRgI7RfCRzlMLlo6fs1zBUNAcfTr9WbTVa57f5UBdDiTCJNbiphSu7W0n7syFQ71vHAkBS0E+6/fkqjWGeb3gU/9jjNoPx13YRp2nXPn9UrxwL3owS2KMOcroJUE6IYTegIDPj0I3G0TWXTjo9n17eNG2jAkEAnGaL1Mc1XKMw/kszuXKgszFJVNOzI06ecP4XdbQaBM+quzgfo6yg7leEMFA5zEQ/JT0SADlCWnjXyzCfa4kpSQ==";

    /**
     * 签名
     * @param input 原文
     * @return 签名后密文（Base64字符串）
     */
    public static String sign(String input) {
        try {
            //***********************签名四部曲*******************
            //1、创建数字签名对象
            Signature signature = Signature.getInstance(ALGORITHM);

            //2、初始化签名
            PrivateKey privateKey = RSACrypt.getPrivateKey(privateKeyStr);
            signature.initSign(privateKey);

            //3、传入原文
            signature.update(input.getBytes());

            //4、开始签名
            byte[] sign = signature.sign();

            return Base64.getEncoder().encodeToString(sign);
        } catch (Exception e) {
            throw new RuntimeException("签名出错，" + e.getMessage());
        }
    }

    /**
     * 验签
     * @param input 原文
     * @param base64Str 密文（Base64字符串）
     * @return boolean
     */
    public static boolean verify(String input, String base64Str) {
        try {
            //***********************验签四部曲*******************
            //1、创建数 字签名对象
            Signature signature = Signature.getInstance(ALGORITHM);

            //2、初始化校验
            PublicKey publicKey = RSACrypt.getPublicKey(publicKeyStr);
            signature.initVerify(publicKey);

            //3、传入原文
            signature.update(input.getBytes());

            //4、验签
            return signature.verify(Base64.getDecoder().decode(base64Str));
        } catch (Exception e) {
            throw new RuntimeException("验签出错，" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String input = "userid=123&price=6888&count=1";
        String sign = SignatureUtil.sign(input);
        System.out.println("签名：" + sign);

        boolean verify = SignatureUtil.verify("userid=123&price=6888&count=1", sign);
        System.out.println("验签：" + verify);
    }
}
