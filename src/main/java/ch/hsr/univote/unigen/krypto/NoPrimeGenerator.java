/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.krypto;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 *
 * @author Gian
 */
public class NoPrimeGenerator {
    public static BigInteger getNoPrime() {
        boolean isprime = true;
        BigInteger probableprime = BigInteger.ZERO;
        
        while(isprime == true) {
            SecureRandom random = new SecureRandom();
            probableprime = new BigInteger(String.valueOf(random.nextLong()));
            
            isprime = isPrime(probableprime);
        }
        BigInteger noprime = probableprime;
        System.out.println(noprime);
        
        return noprime;
    }
    
    static boolean isPrime(BigInteger value) {       
        
        return value.isProbablePrime(100);
    }
}