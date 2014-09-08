/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.krypto;

import ch.hsr.univote.unigen.crypto.RSA;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import static org.junit.Assert.*;

/**
 *
 * @author Gian Poltéra
 */
public class RSATest extends TestCase {
    

    public RSATest (String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(RSATest.class);
    }

    /**
     * Test of RSA class.
     */
    public void testRSA() {
        ConfigHelper config = new ConfigHelper();
        String m = "This is a test of the RSA Signature";
        System.out.println("RSA Signature of: " + m);
  
        RSA rsa = new RSA(config);
        KeyPair keyPair = rsa.getKeyPair();

        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        
        BigInteger s = rsa.signRSA(m, privateKey);
        boolean result = rsa.verifyRSA(m, s, publicKey);
        
        //check the results
        if (result) {
            assertTrue(true);
        } else {
            assertTrue("RSA-Signature not works well", false);
        }
    }
    
}
