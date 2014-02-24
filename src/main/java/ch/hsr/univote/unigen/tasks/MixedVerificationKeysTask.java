/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.db.DB4O;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;

/**
 *
 * @author Gian Poltéra
 */
public class MixedVerificationKeysTask extends ElectionBoard {

    public static void run() throws Exception {

        vk.setElectionId(ConfigHelper.getElectionId());
        
        
        for (int i = 0; i < mixedVerificationKeysList[mixers.length - 1].getKey().size(); i++) {
            vk.getKey().add(mixedVerificationKeysList[mixers.length - 1].getKey().get(i));
        }
        
        vk.setSignature(SignatureGenerator.createSignature(vk, electionManagerPrivateKey));
        
        /*save in db*/
        DB4O.storeDB(ConfigHelper.getElectionId(), vk);
    }
}
