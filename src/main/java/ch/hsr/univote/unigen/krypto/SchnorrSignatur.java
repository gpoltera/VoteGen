/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.krypto;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

/**
 *
 * @author Gian
 */
public class SchnorrSignatur {

    private static final BigInteger[] sign(BigInteger m, BigInteger p, BigInteger q, BigInteger g, BigInteger sk) throws NoSuchAlgorithmException {

        BigInteger r = PrimeGenerator.getPrime(1024).mod(q);
        byte[] gr = g.modPow(r, p).toByteArray();
        byte[] mgr = ByteUtils.concatenate(m.toByteArray(), gr);

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(mgr);

        BigInteger a = new BigInteger(md.digest());
        a = a.mod(q);

        BigInteger b = r.subtract(sk.multiply(a).mod(p));

        BigInteger[] S = new BigInteger[2];
        S[0] = b;
        S[1] = a;

        return S;
    }
}