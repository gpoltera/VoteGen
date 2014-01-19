/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.krypto;

import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

/**
 *
 * @author Gian
 */
public class SchnorrSignature {

    /**
     *
     * @param p SchnorrSignature prime
     * @param q SchnorrSignature group order
     * @param g SchnorrSignature generator
     * @return keyPair
     */
    public static BigInteger[] getKeyPair(BigInteger p, BigInteger q, BigInteger g) {
        BigInteger[] keyPair = new BigInteger[2];
        BigInteger sk = PrimeGenerator.getPrime(q.bitLength() - 1);
        BigInteger vk = g.modPow(sk, p);

        keyPair[0] = sk;
        keyPair[1] = vk;

        return keyPair;
    }

    /**
     *
     * @param m Message to sign
     * @param p SchnorrSignature prime
     * @param q SchnorrSignature group order
     * @param g SchnorrSignature generator
     * @param sk SignatureKey
     * @return Signature (b,a)
     */
    public static BigInteger[] sign(BigInteger m, BigInteger p, BigInteger q, BigInteger g, BigInteger sk) throws NoSuchAlgorithmException {

        BigInteger r = PrimeGenerator.getPrime(q.bitLength() - 1);
        BigInteger gr = g.modPow(r, p);
        byte[] mgr = ByteUtils.concatenate(m.toByteArray(), gr.toByteArray());

        BigInteger a = Hash.getHash(mgr);
        BigInteger b = r.subtract(sk.multiply(a)).mod(q);

        BigInteger[] S = new BigInteger[2];
        S[0] = b;
        S[1] = a;

        return S;
    }

    /**
     *
     * @param m Message to verify
     * @param S Signature (b,a)
     * @param p SchnorrSignature prime
     * @param q SchnorrSignature group order
     * @param g SchnorrSignature generator
     * @param vk VerificationKey
     * @return boolean match true/false
     */
    public static boolean verify(BigInteger m, BigInteger[] S, BigInteger p, BigInteger q, BigInteger g, BigInteger vk) throws NoSuchAlgorithmException {

        BigInteger b = S[0];
        BigInteger a = S[1];

        BigInteger rv = g.modPow(b, p).multiply(vk.modPow(a, p)).mod(p);
        byte[] mrv = ByteUtils.concatenate(m.toByteArray(), rv.toByteArray());

        BigInteger av = Hash.getHash(mrv);

        if (av.equals(a)) {
            return true;
        } else {
            return false;

        }
    }
}
