package com.example.studentoutcomebackend.utils;

import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.util.encoders.Hex;

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
}