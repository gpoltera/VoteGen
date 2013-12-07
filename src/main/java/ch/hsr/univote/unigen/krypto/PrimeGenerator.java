/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.krypto;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * This class generates the prime-numbers and safe-prime-numbers
 * 
 * @author Gian Poltéra
 */
public class PrimeGenerator {

    /**
     * Create a prime-number with individual bit-length
     * 
     * @param bitlength the length of the prime-number
     * @return prime-number
     */
    public static BigInteger getPrime(int bitlength) {
        SecureRandom random = new SecureRandom();
        BigInteger prime = BigInteger.probablePrime(bitlength, random);
        System.out.println("Prime: " + prime);
        
        return prime;
    }
    
    /**
     * Create a safe-prime-number with individual bit-length
     * 
     * @param bitlength the length of the prime-number
     * @return safe-prime-number
     */
    public static BigInteger getSafePrime(int bitlength) {
        boolean issafeprime = false;        
        BigInteger prime = BigInteger.ZERO;
   
        while(issafeprime == false) {
            prime = getPrime(bitlength);
            issafeprime = isSafePrime(prime);
        }
        BigInteger safeprime = prime;
        System.out.println("SafePrime: " + safeprime);
        
        return safeprime;
    }
    
    /**
     * Check if a number is a prime, safety 95%
     * 
     * @param value the number to check
     * @return boolean value true if is a prime and false if is not a prime
     */
    static boolean isPrime(BigInteger value) {       
        
        return value.isProbablePrime(95);
    }
    
    /**
     * Check if a prime-number is a safe-prime 2p+1
     * 
     * @param prime the prime-number to check
     * @return boolean value true if is safe-prime and false if is not a safe-prime
     */
    static boolean isSafePrime(BigInteger prime) {
        BigInteger value = prime.multiply(new BigInteger("2")).add(BigInteger.ONE);
        
        return value.isProbablePrime(95);
    }
}