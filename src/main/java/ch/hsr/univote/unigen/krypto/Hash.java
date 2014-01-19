/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.krypto;

import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Gian Poltéra
 */
public class Hash {
    /**
     *
     * @param value String to hashing
     * @return hashed value
     */
    public static BigInteger getHash(String value) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(ConfigHelper.getHashAlgorithm());
        md.reset();
        md.update(value.getBytes(Charset.forName(ConfigHelper.getCharEncoding())));
        return new BigInteger(1, md.digest());
    }
    
    public static BigInteger getHash(byte[] value) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(ConfigHelper.getHashAlgorithm());
        md.reset();
        md.update(value);
        return new BigInteger(1, md.digest());
    }
}
