/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.EncryptionKeyShare;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.db.DB4O;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.NIZKP;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;

/**
 *
 * @author Gian Poltéra
 */
public class EncryptionKeyShareTask extends VoteGenerator {

    public void run() throws Exception {
        EncryptionKeyShare[] encryptionKeyShareList = new EncryptionKeyShare[electionBoard.talliers.length];

        /*for each tallier*/
        for (int i = 0; i < electionBoard.talliers.length; i++) {
            /*create EncryptionKeyShare*/
            EncryptionKeyShare encryptionKeyShare = createEncryptionKeyShare(i);

            /*set the proof*/
            encryptionKeyShare.setProof(NIZKP.getProof(
                    electionBoard.talliers[i],
                    keyStore.talliersDecryptionKey[i],
                    keyStore.talliersEncryptionKey[i],
                    electionBoard.getEncryptionParameters().getPrime(),
                    electionBoard.getEncryptionParameters().getGroupOrder(),
                    electionBoard.getEncryptionParameters().getGenerator()));

            /*sign by tallier*/
            encryptionKeyShare.setSignature(SignatureGenerator.createSignature(electionBoard.talliers[i], encryptionKeyShare, keyStore.talliersPrivateKey[i]));

            /*add to list*/
            encryptionKeyShareList[i] = encryptionKeyShare;
        }
        /*submit to ElectionBoard*/
        electionBoard.setEncryptionKeyShareList(encryptionKeyShareList);

        /*save in db*/
        DB4O.storeDB(config.getElectionId(), encryptionKeyShareList);
    }

    private EncryptionKeyShare createEncryptionKeyShare(int i) {
        EncryptionKeyShare encryptionKeyShare = new EncryptionKeyShare();
        encryptionKeyShare.setElectionId(config.getElectionId());
        encryptionKeyShare.setKey(keyStore.talliersEncryptionKey[i]);

        return encryptionKeyShare;
    }
}
