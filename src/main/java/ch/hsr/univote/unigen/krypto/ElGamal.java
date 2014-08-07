/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.krypto;

import ch.bfh.univote.common.EncryptionParameters;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class ElGamal {

    /**
     * 
     * @param keyLength
     * @return ElGamal public parameters (p,q,g), (prime, group order, generator)
     */
    public EncryptionParameters getPublicParameters(int keyLength) {
        EncryptionParameters encryptionParameters = new EncryptionParameters();
        
        BigInteger q = PrimeGenerator.getSafePrime(keyLength - 1);
        BigInteger p = q.multiply(new BigInteger("2")).add(BigInteger.ONE);
        BigInteger g = new BigInteger("2");
        
        //while group order g^q mod p = 1
        while (!g.modPow(q, p).equals(BigInteger.ONE)) {
            g = g.add(BigInteger.ONE);
        }

        encryptionParameters.setPrime(p);
        encryptionParameters.setGroupOrder(q);
        encryptionParameters.setGenerator(g);

        return encryptionParameters;
    }

    /**
     *
     * @param encryptionParameters
     * @return KeyPair (x,y), (private-key, public-key)
     */
    public BigInteger[] getKeyPair(EncryptionParameters encryptionParameters) {
        BigInteger p = encryptionParameters.getPrime();
        BigInteger q = encryptionParameters.getGroupOrder();
        BigInteger g = encryptionParameters.getGenerator();
        
        BigInteger x = PrimeGenerator.getPrime(q.bitLength() - 1);
        BigInteger y = g.modPow(x, p);
        
        BigInteger[] keyPair = new BigInteger[2];   
        keyPair[0] = x;
        keyPair[1] = y;

        return keyPair;
    }

    /**
     *
     * @param m message to encrypt
     * @param y public key
     * @param encryptionParameters
     * @return encrypted pair (a,b)
     */
    public BigInteger[] getEncryption(BigInteger m, BigInteger y, EncryptionParameters encryptionParameters) {
        BigInteger p = encryptionParameters.getPrime();
        BigInteger q = encryptionParameters.getGroupOrder();
        BigInteger g = encryptionParameters.getGenerator();
        
        BigInteger r = PrimeGenerator.getPrime(q.bitLength() - 1);
        BigInteger a = g.modPow(r, p);
        BigInteger b = m.multiply(y.modPow(r, p)).mod(p);

        BigInteger[] encrypted = new BigInteger[2];
        encrypted[0] = a;
        encrypted[1] = b;

        return encrypted;
    }

    /**
     * 
     * @param a Encrypted a
     * @param b Encrypted b
     * @param x private key
     * @param encryptionParameters
     * @return decrypted m
     */
    public BigInteger getDecryption(BigInteger a, BigInteger b, BigInteger x, EncryptionParameters encryptionParameters) {
        BigInteger p = encryptionParameters.getPrime();
        
        BigInteger m = a.modPow(p.subtract(BigInteger.ONE).subtract(x).mod(p), p).multiply(b).mod(p);

        return m;
    }
}
