/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.krypto;

import ch.bfh.univote.common.ElectionDefinition;
import ch.hsr.univote.unigen.generator.prov.TimestampGenerator;
import java.math.BigInteger;
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
public class RSASignatur {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static BigInteger signRSA(String value, RSAPrivateKey privateKey) throws NoSuchAlgorithmException {
        BigInteger hash = Hash.getSHA256(value);

        BigInteger signature = hash.modPow(privateKey.getPrivateExponent(), privateKey.getModulus());

        return signature;
    }
}
