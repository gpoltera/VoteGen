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
    public static BigInteger getRandom(int bitlength) {
        BigInteger random = new BigInteger(bitlength, new SecureRandom());
        
        return random;
    }
    
    public static BigInteger getPrime(int bitlength) {
        BigInteger random = getRandom(bitlength); 
        
        while (!isPrime(random, 3)) {
            random = random.nextProbablePrime();
        }
        
        BigInteger prime = random;

        return prime;
    }
    
    /**
     * Create a safe-prime-number with individual bit-length
     * 
     * @param bitlength the length of the prime-number
     * @return safe-prime-number
     */
    public static BigInteger getSafePrime(int bitlength) {   
        BigInteger prime = getPrime(bitlength);
   
        while(!isSafePrime(prime, 3)) {
            prime = prime.nextProbablePrime();
        }
        BigInteger safeprime = prime;

        return safeprime;
    }
    
    /**
     * Check if a number is a prime, safety 95%
     * 
     * @param value the number to check
     * @return boolean value true if is a prime and false if is not a prime
     */
    static boolean isPrime(BigInteger value, int secure) {       
        
        return MillerRabin.millerRabinTest(value, secure);
    }
    
    /**
     * Check if a prime-number is a safe-prime 2p+1
     * 
     * @param prime the prime-number to check
     * @return boolean value true if is safe-prime and false if is not a safe-prime
     */
    static boolean isSafePrime(BigInteger prime, int secure) {
        BigInteger value = prime.multiply(new BigInteger("2")).add(BigInteger.ONE);
        
        return MillerRabin.millerRabinTest(value, secure);
    }
}