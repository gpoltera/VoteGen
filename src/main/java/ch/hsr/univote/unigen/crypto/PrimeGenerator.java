/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.crypto;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * This class generates the prime-numbers and safe-prime-numbers
 * 
 * @author Gian Poltéra
 */
public class PrimeGenerator {
    MillerRabin millerRabin;
    
    public PrimeGenerator() {
        millerRabin = new MillerRabin();
    }
    
    
    /**
     * Create a prime-number with individual bit-length
     * 
     * @param bitlength the length of the prime-number
     * @return prime-number
     */
    public BigInteger getRandom(int bitlength) {
        BigInteger random = new BigInteger(bitlength, new SecureRandom());
        //Set the first two bits to 1
        random = new BigInteger("11".concat(random.toString(2).subSequence(2, random.bitLength()).toString()), 2);
        
        while (random.bitLength() != bitlength) {
            random = new BigInteger(bitlength, new SecureRandom());
            //Set the first two bits to 1
            random = new BigInteger("11".concat(random.toString(2).subSequence(2, random.bitLength()).toString()), 2);
        }

        return random;
    }
    
    public BigInteger getPrime(int bitlength) {
        BigInteger random = getRandom(bitlength); 
        
        while (!isPrime(random, 10)) {
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
    public BigInteger getSafePrime(int bitlength) {   
        BigInteger prime = getPrime(bitlength - 1);
        BigInteger safeprime = prime.multiply(new BigInteger("2")).add(BigInteger.ONE);
         
        while(!millerRabin.millerRabinTest(safeprime, 10)) {
            prime = prime.nextProbablePrime();
            safeprime = prime.multiply(new BigInteger("2")).add(BigInteger.ONE);
        }

        return safeprime;
    }
    
    /**
     * Check if a number is a prime, safety 95%
     * 
     * @param value the number to check
     * @return boolean value true if is a prime and false if is not a prime
     */
    private boolean isPrime(BigInteger value, int secure) {       
        
        return millerRabin.millerRabinTest(value, secure);
    }
}