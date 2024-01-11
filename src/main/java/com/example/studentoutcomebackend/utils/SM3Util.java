package com.example.studentoutcomebackend.utils;

import com.example.studentoutcomebackend.entity.StudentInfo;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.util.encoders.Hex;

import java.util.Random;

// 生成国密SM3摘要
public class SM3Util {

    public static String getHashString(String data){
        return getHashString(data.getBytes());
    }

    public static String getHashString(byte[] data){
        return Hex.toHexString(getHashByte(data));
    }

    public static byte[] getHashByte(String data){
        return getHashByte(data.getBytes());
    }

    public static byte[] getHashByte(byte[] data){
        SM3Digest sm3Digest=new SM3Digest();
        sm3Digest.update(data,0,data.length);
        byte[] hash = new byte[sm3Digest.getDigestSize()];
        sm3Digest.doFinal(hash, 0);
        return hash;
    }

    /**
     * 加密密码的函数
     *
     */
    public static String hashPassword(String password, StudentInfo studentInfo){
        String salt = String.valueOf(studentInfo.getUser_id());
        String s = salt + password + salt;
        String ans = SM3Util.getHashString(s);
        return ans;
    }

    public static String getRandomString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 16) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}