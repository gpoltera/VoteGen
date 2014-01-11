/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.EncryptionParameters;
import ch.bfh.univote.common.Signature;
import ch.hsr.univote.unigen.generator.prov.TimestampGenerator;
import ch.hsr.univote.unigen.generator.prov.WahlGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.ep;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.ElGamal;
import ch.hsr.univote.unigen.krypto.PrimeGenerator;
import ch.hsr.univote.unigen.krypto.RSA;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;

/**
 *
 * @author Gian Poltéra
 */
public class EncryptionParametersTask extends WahlGenerator {

    //elgamal parameters
    public static void run() throws Exception {
        EncryptionParameters encryptionParameters = new EncryptionParameters();
        encryptionParameters.setElectionId(ConfigHelper.getElectionId());

        BigInteger[] keys = ElGamal.generateKeys();
        //ElGamal's p
        encryptionParameters.setPrime(keys[0]);
        //ElGamal's q
        encryptionParameters.setGroupOrder(keys[1]);
        //ElGamal's g
        encryptionParameters.setGenerator(keys[2]);

        ep = encryptionParameters;

        //sign by electionamanger
        ep.setSignature(SignatureGenerator.createSignature(encryptionParameters, electionManagerPrivateKey));
    }
}
