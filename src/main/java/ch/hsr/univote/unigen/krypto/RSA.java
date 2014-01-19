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

    /**
     * 
     * @return a new RSAKeyPair
     */
    public static KeyPair getRSAKeyPair() throws Exception {
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(ConfigHelper.getSignatureKeyType());
        keyGenerator.initialize(ConfigHelper.getSignatureKeyLength());
        KeyPair keyPair = keyGenerator.generateKeyPair();

        return keyPair;
    }

    /**
     *
     * @param value to sign
     * @param RSAPivateKey
     * @return the singature
     */
    public static BigInteger signRSA(String value, RSAPrivateKey privateKey) throws NoSuchAlgorithmException {
        BigInteger hash = Hash.getHash(value);
        BigInteger signature = hash.modPow(privateKey.getPrivateExponent(), privateKey.getModulus());

        return signature;
    }
}
