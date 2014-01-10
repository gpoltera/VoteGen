/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.Signature;
import ch.hsr.univote.unigen.generator.prov.TimestampGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.signatureParameters;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.PrimeGenerator;
import ch.hsr.univote.unigen.krypto.RSAGenerator;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;

/**
 *
 * @author Gian Poltéra
 */
public class SignatureParametersTask {

    public static void run() throws Exception {
        //Schnorr Signature Parameters (Public)
        //Schnorr p (SafePrime)
        signatureParameters.setPrime(PrimeGenerator.getSafePrime(ConfigHelper.getSignatureKeyLength()));
        //Schnorr g?
        signatureParameters.setGroupOrder(PrimeGenerator.getPrime(ConfigHelper.getSignatureKeyLength()));
        //Schnorr q?
        signatureParameters.setGenerator(PrimeGenerator.getPrime(ConfigHelper.getSignatureKeyLength()));
        
        // Signature of the Parameters
        Signature signature = new Signature();
        
        // False -> Sign with Administrators private key?
        RSAPrivateKey privateKey = RSAGenerator.getPrivateKey();
        
        signature = SignatureGenerator.createSignature(signatureParameters, privateKey);
        signature.setSignerId(ConfigHelper.getAdministrationId());
        signature.setTimestamp(TimestampGenerator.generateTimestamp());
        signatureParameters.setSignature(signature);
    }
}
