/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.EncryptionKey;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.krypto.ElGamal;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class EncryptionKeyTask extends VoteGenerator {

    public void run() throws Exception {
        /*create EncryptionKey*/
        EncryptionKey encryptionKey = createEncryptionKey();        
        
        /*sign by ElectionManager*/
        encryptionKey.setSignature(new SignatureGenerator().createSignature(encryptionKey, keyStore.getElectionManagerPrivateKey()));
        
        /*submit to ElectionBoard*/
        electionBoard.setEncryptionKey(encryptionKey);
    }
    
    // Create the ElectionDefinition
    private EncryptionKey createEncryptionKey() {
        EncryptionKey encryptionKey = new EncryptionKey();
        encryptionKey.setElectionId(config.getElectionId());
        BigInteger y = null;
        //Foreach Tallier generate keys
        for (int i = 0; i < electionBoard.talliers.length; i++) {
            BigInteger keyPair[] = ElGamal.getKeyPair(
                    electionBoard.getEncryptionParameters().getPrime(),
                    electionBoard.getEncryptionParameters().getGroupOrder(), 
                    electionBoard.getEncryptionParameters().getGenerator());
            keyStore.setTallierDecryptionKey(i, keyPair[0]);
            keyStore.setTallierEncryptionKey(i, keyPair[1]);
            
            if (y == null) {
                y = keyStore.getTallierEncryptionKey(i);
            } else {
                y = y.multiply(keyStore.getTallierEncryptionKey(i));
            }
        }
        encryptionKey.setKey(y.mod(electionBoard.getEncryptionParameters().getPrime()));
        
        return encryptionKey;
    }
}