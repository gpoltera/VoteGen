/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.krypto;

import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 *
 * @author Gian Poltéra
 */
public class ElGamal {

    /**
     * 
     * @return ElGamal public parameters (p,q,g), (prime, group order, generator)
     */
    public static BigInteger[] getPublicParameters() {
        BigInteger q = PrimeGenerator.getSafePrime(ConfigHelper.getEncryptionKeyLength());
        BigInteger p = q.multiply(new BigInteger("2")).add(BigInteger.ONE);

        int i = 0;
        BigInteger g = new BigInteger("2");

        //group order (g^2 mod p <> 1 & g^q mod p <> 1)
        while (!g.modPow(new BigInteger("2"), p).equals(BigInteger.ONE) && !g.modPow(q, p).equals(BigInteger.ONE)) {
            i++;
            g = g.add(BigInteger.valueOf(i));
        }

        BigInteger[] keys = new BigInteger[3];
        keys[0] = p;
        keys[1] = q;
        keys[2] = g;

        return keys;
    }

    /**
     *
     * @param p ElGamal prime
     * @param q ElGamal group order
     * @param g ElGamal generator
     * @return KeyPair (x,y), (private-key, public-key)
     */
    public static BigInteger[] getKeyPair(BigInteger p, BigInteger q, BigInteger g) {
        BigInteger[] keyPair = new BigInteger[2];
        BigInteger x = PrimeGenerator.getPrime(q.bitLength() - 1);
        BigInteger y = g.modPow(x, p);

        keyPair[0] = x;
        keyPair[1] = y;

        return keyPair;
    }

    /**
     *
     * @param m message to encrypt
     * @param y public key
     * @param p ElGamal prime
     * @param q ElGamal group order
     * @param g ElGamal generator
     * @return Encrypted pair (a,b)
     */
    public static BigInteger[] getEncryption(BigInteger m, BigInteger y, BigInteger p, BigInteger q, BigInteger g) {
        BigInteger[] encrypted = new BigInteger[2];
        BigInteger r = PrimeGenerator.getPrime(q.bitLength() - 1);
        BigInteger a = g.modPow(r, p);
        BigInteger b = m.multiply(y.modPow(r, p)).mod(p);

        encrypted[0] = a;
        encrypted[1] = b;

        return encrypted;
    }

    /**
     * 
     * @param a Encrypted a
     * @param b Encrypted b
     * @param x private key
     * @param p ElGamal prime
     * @return 
     */
    public static BigInteger getDecryption(BigInteger a, BigInteger b, BigInteger x, BigInteger p) {
        BigInteger m = a.modPow(p.subtract(BigInteger.ONE).subtract(x).mod(p), p).multiply(b).mod(p);

        return m;
    }
}
