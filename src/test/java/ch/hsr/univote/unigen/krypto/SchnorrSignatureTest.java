/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.krypto;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author Gian Poltéra
 */
public class SchnorrSignatureTest extends TestCase {

    public SchnorrSignatureTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(SchnorrSignatureTest.class);
    }

    /**
     * Test of SchnorrSignature class.
     */
    public void testSchnorrSignature() throws NoSuchAlgorithmException {
        BigInteger m = new BigInteger("954216454884");
        System.out.println("Schnorr Signature of: " + m);
  
        BigInteger p = new BigInteger("161931481198080639220214033595931441094586304918402813506510547237223787775475425991443924977419330663170224569788019900180050114468430413908687329871251101280878786588515668012772798298511621634145464600626619548823238185390034868354933050128115662663653841842699535282987363300852550784188180264807606304297");
        BigInteger q = new BigInteger("65133683824381501983523684796057614145070427752690897588060462960319251776021");
        BigInteger g = new BigInteger("109291242937709414881219423205417309207119127359359243049468707782004862682441897432780127734395596275377218236442035534825283725782836026439537687695084410797228793004739671835061419040912157583607422965551428749149162882960112513332411954585778903685207256083057895070357159920203407651236651002676481874709");
 
        BigInteger[] keyPair = SchnorrSignature.getKeyPair(p, q, g);
        BigInteger sk = keyPair[0];
        BigInteger vk = keyPair[1];
        
        BigInteger[] S = SchnorrSignature.sign(m, p, q, g, sk);
        boolean result = SchnorrSignature.verify(m, S, p, q, g, vk);
        
        //check the results
        if (result) {
            assertTrue(true);
        } else {
            assertTrue("Schnorr-Signature not works well", false);
        }
    }
}
