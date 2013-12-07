/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.krypto;

import java.io.FileWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author Gian
 */
public class RSAGenerator {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static RSAPrivateKey getPrivateKey() throws Exception {
        KeyPairGenerator kpGen = KeyPairGenerator.getInstance("RSA", "BC");
        kpGen.initialize(1024, new SecureRandom());

        return (RSAPrivateKey) kpGen.generateKeyPair().getPrivate();
    }
}
