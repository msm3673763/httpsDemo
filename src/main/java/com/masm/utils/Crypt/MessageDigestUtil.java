package com.masm.utils.Crypt;

import java.security.MessageDigest;

/**
 * 消息摘要算法：
 *    1、不论原文长度多长，加密后密文长度固定
 *    2、MD5密文长度16个字节，转成16进制字符串32个字节
 *    3、SHA-1密文长度20个字节，转成16进制字符串40个字节
 *    4、SHA-256密文长度32个字节，转成16进制字符串64个字节
 * create by masiming in 2019/1/13 14:08:21
 */
public class MessageDigestUtil {

    /**
     * 算法：MD5、SHA-1、SHA-256
     */
    private static final String ALGORITHM = "MD5";

    public static String md5(String input)  {
        try {
            //创建消息摘要对象
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] bytes = digest.digest(input.getBytes());
            //转成16进制
            return toHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException("加密出错，" + e.getMessage());
        }
    }

    /**
     * 转成16进制
     * @param bytes 字符串字节数组
     * @return 16进制字符串
     */
    public static String toHex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            int value = b & 0xff;//转成16进制
            String hexString = Integer.toHexString(value);
            //16进制字符串长度不是2位数，前面补零
            if (hexString.length() == 1) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hexString);
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        String md5 = MessageDigestUtil.md5("dfdkjfdskjfksdjfkdsjfsdkfsdfsdfsdfsdfsdff");
        System.out.println(md5);
    }
}
