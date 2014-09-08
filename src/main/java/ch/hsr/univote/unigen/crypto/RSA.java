/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.crypto;

import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gian
 */
public class RSA {

    private ConfigHelper config;

    public RSA(ConfigHelper config) {
        this.config = config;
    }

    /**
     *
     * @return a new RSAKeyPair
     */
    public KeyPair getKeyPair() {
        KeyPair keyPair = null;
        try {
            KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(config.getSignatureKeyType());
            keyGenerator.initialize(config.getSignatureKeyLength());
            keyPair = keyGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        }
        return keyPair;
    }

    /**
     *
     * @param value to sign
     * @param privateKey
     * @return the singature
     */
    public BigInteger signRSA(String value, RSAPrivateKey privateKey) {
        BigInteger hash = new Hash().getHash(value, config.getHashAlgorithm(), config.getCharEncoding());
        BigInteger signature = hash.modPow(privateKey.getPrivateExponent(), privateKey.getModulus());

        return signature;
    }

    /**
     *
     * @param value
     * @param signature to verify
     * @param publicKey
     * @return the singature
     */
    public Boolean verifyRSA(String value, BigInteger signature, RSAPublicKey publicKey) {
        Boolean result = false;
        BigInteger hash = new Hash().getHash(value, config.getHashAlgorithm(), config.getCharEncoding());
        BigInteger verification = signature.modPow(publicKey.getPublicExponent(), publicKey.getModulus());
        
        if (hash.equals(verification)) {
            result = true;
        }

        return result;
    }
}
