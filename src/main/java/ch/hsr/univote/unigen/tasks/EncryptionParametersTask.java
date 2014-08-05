/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.EncryptionParameters;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.krypto.ElGamal;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class EncryptionParametersTask extends VoteGenerator {

    //elgamal parameters
    public void run() throws Exception {
        /*create EncryptionParameters*/
        EncryptionParameters encryptionParameters = createEncryptionParameters();
        
        /*sign by electionamanger*/
        encryptionParameters.setSignature(new SignatureGenerator().createSignature(encryptionParameters, keyStore.getElectionManagerPrivateKey()));
        
        /*submit to ElectionBoard*/
        electionBoard.setEncryptionParameters(encryptionParameters);
    }
    
    private EncryptionParameters createEncryptionParameters() {
        EncryptionParameters encryptionParameters = new EncryptionParameters();
        encryptionParameters.setElectionId(config.getElectionId());
        BigInteger[] keys = ElGamal.getPublicParameters(config.getEncryptionKeyLength());
        encryptionParameters.setPrime(keys[0]); //ElGamal's p
        encryptionParameters.setGroupOrder(keys[1]); //ElGamal's q
        encryptionParameters.setGenerator(keys[2]); //ElGamal's g
        
        return encryptionParameters;       
    }
}
