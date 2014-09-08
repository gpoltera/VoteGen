/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.crypto;

import ch.bfh.univote.common.SignatureParameters;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.helper.StringConcatenator;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gian
 */
public class Schnorr {

    private ConfigHelper config;

    public Schnorr(ConfigHelper config) {
        this.config = config;
    }

    /**
     *
     * @param signatureParameters
     * @return keyPair
     */
    public KeyPair getKeyPair(SignatureParameters signatureParameters) {
        KeyPair keyPair = null;
        try {
            BigInteger p = signatureParameters.getPrime();
            BigInteger q = signatureParameters.getGroupOrder();
            BigInteger g = signatureParameters.getGenerator();

            BigInteger x = PrimeGenerator.getPrime(q.bitLength() - 1);
            BigInteger y = g.modPow(x, p);

            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            PrivateKey privateKey = keyFactory.generatePrivate(new DSAPrivateKeySpec(x, p, q, g));
            PublicKey publicKey = keyFactory.generatePublic(new DSAPublicKeySpec(y, p, q, g));
            
            keyPair = new KeyPair(publicKey, privateKey);
            
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(Schnorr.class.getName()).log(Level.SEVERE, null, ex);
        }

        return keyPair;
    }

    /**
     *
     * @param m Message to sign
     * @param electionGenerator
     * @param privateKey Schnorr PrivateKey
     * @return Signature (b,a)
     */
    public BigInteger[] signSchnorr(String m, BigInteger electionGenerator, DSAPrivateKey privateKey) {

        BigInteger p = privateKey.getParams().getP();
        BigInteger q = privateKey.getParams().getQ();
        BigInteger g = privateKey.getParams().getG();
        BigInteger x = privateKey.getX();
        BigInteger[] s = new BigInteger[2];

        BigInteger r = PrimeGenerator.getPrime(q.bitLength() - 1);
        BigInteger gr = electionGenerator.modPow(r, p);
        
        StringConcatenator sc = new StringConcatenator();
        sc.pushObjectDelimiter(m, StringConcatenator.INNER_DELIMITER);
        sc.pushObject(gr);
        
        String concat = sc.pullAll();

        BigInteger a = new Hash().getHash(concat, config.getHashAlgorithm(), config.getCharEncoding());
        BigInteger b = r.subtract(x.multiply(a)).mod(q);

        s[0] = a;
        s[1] = b;

        return s;
    }

    /**
     *
     * @param m Message to verify
     * @param s Signature (b,a)
     * @param electionGenerator
     * @param publicKey Schnorr PublicKey
     * @return boolean match true/false
     */
    public boolean verifySchnorr(String m, BigInteger[] s, BigInteger electionGenerator, DSAPublicKey publicKey) {

        BigInteger a = s[0];
        BigInteger b = s[1];

        BigInteger p = publicKey.getParams().getP();
        BigInteger q = publicKey.getParams().getQ();
        BigInteger g = publicKey.getParams().getG();
        BigInteger y = publicKey.getY();

        BigInteger rv = electionGenerator.modPow(b, p).multiply(y.modPow(a, p)).mod(p);
        
        StringConcatenator sc = new StringConcatenator();
        sc.pushObjectDelimiter(m, StringConcatenator.INNER_DELIMITER);
        sc.pushObject(rv);
        
        String concat = sc.pullAll();

        BigInteger av = new Hash().getHash(concat, config.getHashAlgorithm(), config.getCharEncoding());

        if (av.equals(a)) {
            return true;
        } else {
            return false;
        }
    }
}
