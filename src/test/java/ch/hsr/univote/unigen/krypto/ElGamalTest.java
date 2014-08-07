/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.krypto;

import ch.bfh.univote.common.EncryptionParameters;
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
        
        ElGamal elGamal = new ElGamal();
        //generate the public parameters
        EncryptionParameters encryptionParameters = elGamal.getPublicParameters(256);

        //generate a key pair
        BigInteger[] keyPair = elGamal.getKeyPair(encryptionParameters);
        
        //encryption of m
        BigInteger[] encryption = elGamal.getEncryption(m, keyPair[1], encryptionParameters);
        
        //decryption of m
        BigInteger decryption = elGamal.getDecryption(encryption[0], encryption[1], keyPair[0], encryptionParameters);
        System.out.println("ElGamal decryption = " + decryption);

        //check the results
        if (m.equals(decryption)) {
            assertTrue(true);
        } else {
            assertTrue("ElGamal not works well", false);
        }
    }
}
