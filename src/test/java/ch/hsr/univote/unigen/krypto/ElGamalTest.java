/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.krypto;

import java.math.BigInteger;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author Gian Poltéra
 */
public class ElGamalTest extends TestCase {
    
    public ElGamalTest(String testName) {
        super(testName);
    }
    
    public static Test suite() {
        return new TestSuite(ElGamalTest.class);
    }
    
    /**
     * Test of ElGamal class.
     */
    public void testElGamal() {
        BigInteger m = new BigInteger("819216351");
        System.out.println("ElGamal encryption of: " + m);
        BigInteger[] parameters = ElGamal.getPublicParameters(256);
        BigInteger[] keyPair = ElGamal.getKeyPair(parameters[0], parameters[1], parameters[2]);
        BigInteger[] encryption = ElGamal.getEncryption(m, keyPair[1], parameters[0], parameters[1], parameters[2]);
        BigInteger decryption = ElGamal.getDecryption(encryption[0], encryption[1], keyPair[0], parameters[0]);
        System.out.println("ElGamal decryption = " + decryption);
        
        //check the results
        if (m.equals(decryption)) {
            assertTrue(true);
        } else {
            assertTrue("ElGamal not works well", false);
        }
    }    
}
