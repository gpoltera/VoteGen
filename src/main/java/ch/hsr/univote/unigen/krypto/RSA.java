/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.krypto;

import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;

/**
 *
 * @author Gian
 */
public class RSA {

    ConfigHelper config = new ConfigHelper();
    
    /**
     * 
     * @return a new RSAKeyPair
     * @throws java.lang.Exception
     */
    public KeyPair getRSAKeyPair() throws Exception {
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(config.getSignatureKeyType());
        keyGenerator.initialize(config.getSignatureKeyLength());
        KeyPair keyPair = keyGenerator.generateKeyPair();

        return keyPair;
    }

    /**
     *
     * @param value to sign
     * @param privateKey
     * @return the singature
     * @throws java.security.NoSuchAlgorithmException
     */
    public BigInteger signRSA(String value, RSAPrivateKey privateKey) throws NoSuchAlgorithmException {
        BigInteger hash = new Hash().getHash(value, config.getHashAlgorithm(), config.getCharEncoding());
        BigInteger signature = hash.modPow(privateKey.getPrivateExponent(), privateKey.getModulus());

        return signature;
    }
}