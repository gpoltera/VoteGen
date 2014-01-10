/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.EncryptionParameters;
import ch.bfh.univote.common.Signature;
import ch.hsr.univote.unigen.generator.prov.TimestampGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.ep;
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
public class EncryptionParametersTask {
    //elgamal parameters
    public static void run() throws Exception {
        EncryptionParameters encryptionParameters = new EncryptionParameters();
        encryptionParameters.setElectionId(ConfigHelper.getElectionId());
        
        BigInteger q = PrimeGenerator.getSafePrime(256);
        BigInteger p = q.multiply(new BigInteger("2")).add(BigInteger.ONE);
        
        boolean corg = false;
        int i = 0;
        BigInteger g = new BigInteger("2");
        
        while(corg == true) {
            g = g.add(BigInteger.valueOf(i));
            if(g.modPow(q, p).equals(BigInteger.ONE)){
                corg = true;
            } else {
                i++;
            }
        }
        
        
        //ElGamal's p, prime (SafePrime)
        encryptionParameters.setPrime(p);
        //ElGamal's q, 256 BIT
        encryptionParameters.setGroupOrder(q);
        //ElGamal's g, group order (g^2 mod p <> 1 & g^q mod p <> 1)
        encryptionParameters.setGenerator(g);
        
        ep = encryptionParameters;
        signEncryptioKey(encryptionParameters);
    }

    private static void signEncryptioKey(EncryptionParameters encryptionParameters) throws Exception {
        RSAPrivateKey privateKey = RSAGenerator.getPrivateKey();
        Signature signature = SignatureGenerator.createSignature(encryptionParameters, privateKey);
        signature.setSignerId(ConfigHelper.getAdministrationId());
        signature.setTimestamp(TimestampGenerator.generateTimestamp());
        ep.setSignature(signature);
    }
}
