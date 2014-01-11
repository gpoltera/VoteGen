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

    public static BigInteger[] generateKeys() {
        BigInteger q = PrimeGenerator.getSafePrime(256);
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
}
