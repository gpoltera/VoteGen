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
import java.util.logging.Level;
import java.util.logging.Logger;

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
     */
    public BigInteger getHash(String value, String hashAlgorithm, String charEncoding) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(hashAlgorithm);
            md.reset();
            md.update(value.getBytes(Charset.forName(charEncoding))); 
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Hash.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BigInteger(1, md.digest());
    }
}