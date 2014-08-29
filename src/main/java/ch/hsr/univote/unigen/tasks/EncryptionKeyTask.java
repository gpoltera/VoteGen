/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.EncryptionKey;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.RSASignatureGenerator;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class EncryptionKeyTask {

    private ConfigHelper config;
    private ElectionBoard electionBoard;
    private KeyStore keyStore;

    public EncryptionKeyTask() {
        this.config = VoteGenerator.config;
        this.electionBoard = VoteGenerator.electionBoard;
        this.keyStore = VoteGenerator.keyStore;

        run();
    }
    
    /*1.3.4 d) Distributed Key Generation*/
    private void run() {
        /*create EncryptionKey*/
        EncryptionKey encryptionKey = createEncryptionKey();

        /*sign by ElectionManager*/
        encryptionKey.setSignature(new RSASignatureGenerator().createSignature(encryptionKey, keyStore.getEMSignatureKey()));

        /*submit to ElectionBoard*/
        electionBoard.setEncryptionKey(encryptionKey);
    }

    // Create the EncryptionKey
    private EncryptionKey createEncryptionKey() {
        BigInteger p = electionBoard.getEncryptionParameters().getPrime();
        EncryptionKey encryptionKey = new EncryptionKey();
        encryptionKey.setElectionId(config.getElectionId());
        BigInteger y = null;

        //Foreach Tallier
        for (int j = 0; j < electionBoard.talliers.length; j++) {

            if (y == null) {
                y = keyStore.getTallierEncryptionKey(j).getY();
            } else {
                y = y.multiply(keyStore.getTallierEncryptionKey(j).getY());
            }
        }
        y = y.mod(p);
        encryptionKey.setKey(y);

        return encryptionKey;
    }
}
