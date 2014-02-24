/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.EncryptionKey;
import ch.hsr.univote.unigen.board.ElectionBoard;
import static ch.hsr.univote.unigen.board.ElectionBoard.ek;
import static ch.hsr.univote.unigen.board.ElectionBoard.encryptionParameters;
import static ch.hsr.univote.unigen.board.ElectionBoard.talliers;
import ch.hsr.univote.unigen.db.DB4O;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.ElGamal;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class EncryptionKeyTask extends ElectionBoard {

    public static void run() throws Exception {
        EncryptionKey encryptionKey = new EncryptionKey();
        encryptionKey.setElectionId(ConfigHelper.getElectionId());
        BigInteger y = null;
        for (int i = 0; i < talliers.length; i++) {
            BigInteger keyPair[] = ElGamal.getKeyPair(
                    encryptionParameters.getPrime(),
                    encryptionParameters.getGroupOrder(), 
                    encryptionParameters.getGenerator());
            talliersDecryptionKey[i] = keyPair[0];
            talliersEncryptionKey[i] = keyPair[1];
            
            if (y == null) {
                y = talliersEncryptionKey[i];
            } else {
                y = y.multiply(talliersEncryptionKey[i]);
            }
        }
        encryptionKey.setKey(y.mod(encryptionParameters.getPrime()));
        ek = encryptionKey;
        ek.setSignature(SignatureGenerator.createSignature(encryptionKey, electionManagerPrivateKey));
        
        /*save in db*/
        DB4O.storeDB(ConfigHelper.getElectionId(), ek);
    }
}