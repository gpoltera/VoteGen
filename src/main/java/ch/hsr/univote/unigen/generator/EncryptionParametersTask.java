/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.EncryptionParameters;
import ch.hsr.univote.unigen.generator.prov.WahlGenerator;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.ElGamal;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class EncryptionParametersTask extends WahlGenerator {

    //elgamal parameters
    public static void run() throws Exception {
        
        encryptionParameters.setElectionId(ConfigHelper.getElectionId());

        BigInteger[] keys = ElGamal.getPublicParameters();
        //ElGamal's p
        encryptionParameters.setPrime(keys[0]);
        //ElGamal's q
        encryptionParameters.setGroupOrder(keys[1]);
        //ElGamal's g
        encryptionParameters.setGenerator(keys[2]);

        //sign by electionamanger
        WahlGenerator.encryptionParameters.setSignature(SignatureGenerator.createSignature(encryptionParameters, electionManagerPrivateKey));
    }
}
