/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.krypto;

import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author Gian
 */
public class RSA {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    // Noch anpassen
    public static KeyPair getRSAKeyPair() throws Exception {

        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(ConfigHelper.getSignatureKeyType());
        keyGenerator.initialize(ConfigHelper.getSignatureKeyLength());
        KeyPair keyPair = keyGenerator.generateKeyPair();

        
        return keyPair;
    }
    
    // todo save in db
}
