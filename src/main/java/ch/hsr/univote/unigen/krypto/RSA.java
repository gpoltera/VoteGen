/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.krypto;

import ch.bfh.univote.common.ElectionDefinition;
import ch.hsr.univote.unigen.helper.TimestampGenerator;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author Gian
 */
public class RSA {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    //get a new RSA KeyPair
    public static KeyPair getRSAKeyPair() throws Exception {
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(ConfigHelper.getSignatureKeyType());
        keyGenerator.initialize(ConfigHelper.getSignatureKeyLength());
        KeyPair keyPair = keyGenerator.generateKeyPair();

        return keyPair;
    }

    //sign the hash over the value with RSA
    public static BigInteger signRSA(String value, RSAPrivateKey privateKey) throws NoSuchAlgorithmException {
        BigInteger hash = Hash.getSHA256(value);
        BigInteger signature = hash.modPow(privateKey.getPrivateExponent(), privateKey.getModulus());

        return signature;
    }
}
