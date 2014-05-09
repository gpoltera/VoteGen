/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.krypto;

import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class ElGamal {

    /**
     * 
     * @param keylength
     * @return ElGamal public parameters (p,q,g), (prime, group order, generator)
     */
    public static BigInteger[] getPublicParameters(int keylength) {
        
        BigInteger q = PrimeGenerator.getSafePrime(keylength - 1);
        BigInteger p = q.multiply(new BigInteger("2")).add(BigInteger.ONE);
        BigInteger g = new BigInteger("2");

        System.out.println("ElGamal p, Länge: " + p.bitLength());
        System.out.println("ElGamal q, Länge: " + q.bitLength());
        System.out.println("ElGamal g, Länge: " + g.bitLength());
        //group order g^q mod p <> 1 evtl. noch (g^2 mod p <> 1
        while (!g.modPow(q, p).equals(BigInteger.ONE)) {
            g = g.add(BigInteger.ONE);
        }

        BigInteger[] parameters = new BigInteger[3];
        parameters[0] = p;
        parameters[1] = q;
        parameters[2] = g;

        return parameters;
    }

    /**
     *
     * @param p ElGamal prime
     * @param q ElGamal group order
     * @param g ElGamal generator
     * @return KeyPair (x,y), (private-key, public-key)
     */
    public static BigInteger[] getKeyPair(BigInteger p, BigInteger q, BigInteger g) {
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
     * @param p ElGamal prime
     * @param q ElGamal group order
     * @param g ElGamal generator
     * @return encrypted pair (a,b)
     */
    public static BigInteger[] getEncryption(BigInteger m, BigInteger y, BigInteger p, BigInteger q, BigInteger g) {
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
     * @param p ElGamal prime
     * @return decrypted m
     */
    public static BigInteger getDecryption(BigInteger a, BigInteger b, BigInteger x, BigInteger p) {
        BigInteger m = a.modPow(p.subtract(BigInteger.ONE).subtract(x).mod(p), p).multiply(b).mod(p);

        return m;
    }
}
