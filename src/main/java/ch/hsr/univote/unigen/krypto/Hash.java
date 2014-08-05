/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.krypto;

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
     * @param hashAlgorithm
     * @param charEncoding
     * @return hashed value
     * @throws java.security.NoSuchAlgorithmException
     */
    public BigInteger getHash(String value, String hashAlgorithm, String charEncoding) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
        md.reset();
        md.update(value.getBytes(Charset.forName(charEncoding)));
        return new BigInteger(1, md.digest());
    }
    
    public BigInteger getHash(byte[] value, String hashAlgorithm) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
        md.reset();
        md.update(value);
        return new BigInteger(1, md.digest());
    }
}