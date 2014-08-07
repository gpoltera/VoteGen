/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.krypto;

import ch.bfh.univote.common.SignatureParameters;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.math.BigInteger;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

/**
 *
 * @author Gian
 */
public class Schnorr {

    ConfigHelper config = new ConfigHelper();

    /**
     *
     * @param signatureParameters
     * @return keyPair
     */
    public BigInteger[] getKeyPair(SignatureParameters signatureParameters) {
        
        BigInteger p = signatureParameters.getPrime();
        BigInteger q = signatureParameters.getGroupOrder();
        BigInteger g = signatureParameters.getGenerator();
        
        BigInteger[] keyPair = new BigInteger[2];
        BigInteger signatureKey = PrimeGenerator.getPrime(q.bitLength() - 1);
        BigInteger verificationKey = g.modPow(signatureKey, p);

        keyPair[0] = signatureKey;
        keyPair[1] = verificationKey;

        return keyPair;
    }

    /**
     *
     * @param m Message to sign
     * @param schnorrSignatureKey
     * @return Signature (b,a)
     */
    public BigInteger[] signSchnorr(String m, SchnorrSignatureKey schnorrSignatureKey) {

        BigInteger p = schnorrSignatureKey.getPrime();
        BigInteger q = schnorrSignatureKey.getGroupOrder();
        BigInteger g = schnorrSignatureKey.getGenerator();
        BigInteger sk = schnorrSignatureKey.getSignatureKey();
        BigInteger[] s = new BigInteger[2];

        BigInteger r = PrimeGenerator.getPrime(q.bitLength() - 1);
        BigInteger gr = g.modPow(r, p);
        byte[] mgr = ByteUtils.concatenate(m.getBytes(), gr.toByteArray());

        BigInteger a = new Hash().getHash(mgr, config.getHashAlgorithm());
        BigInteger b = r.subtract(sk.multiply(a)).mod(q);

        s[0] = b;
        s[1] = a;

        return s;
    }

    /**
     *
     * @param m Message to verify
     * @param s Signature (b,a)
     * @param verificationKey
     * @return boolean match true/false
     */
    public boolean verifySchnorr(String m, BigInteger[] s, SchnorrVerificationKey verificationKey) {

        BigInteger b = s[0];
        BigInteger a = s[1];

        BigInteger p = verificationKey.getPrime();
        BigInteger q = verificationKey.getGroupOrder();
        BigInteger g = verificationKey.getGenerator();
        BigInteger vk = verificationKey.getVerificationKey();

        BigInteger rv = g.modPow(b, p).multiply(vk.modPow(a, p)).mod(p);
        byte[] mrv = ByteUtils.concatenate(m.getBytes(), rv.toByteArray());

        BigInteger av = new Hash().getHash(mrv, config.getHashAlgorithm());

        if (av.equals(a)) {
            return true;
        } else {
            return false;
        }
    }
}
