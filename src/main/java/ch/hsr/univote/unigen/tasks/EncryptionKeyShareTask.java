/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.EncryptionKeyShare;
import ch.hsr.univote.unigen.VoteGenerator;
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
            encryptionKeyShare.setProof(new NIZKP().getProof(
                    electionBoard.talliers[i],
                    keyStore.getTallierDecryptionKey(i),
                    keyStore.getTallierEncryptionKey(i),
                    electionBoard.getEncryptionParameters().getPrime(),
                    electionBoard.getEncryptionParameters().getGroupOrder(),
                    electionBoard.getEncryptionParameters().getGenerator()));

            /*sign by tallier*/
            encryptionKeyShare.setSignature(new SignatureGenerator().createSignature(electionBoard.talliers[i], encryptionKeyShare, keyStore.getTallierPrivateKey(i)));

            /*add to list*/
            encryptionKeyShareList[i] = encryptionKeyShare;
        }
        /*submit to ElectionBoard*/
        electionBoard.setEncryptionKeyShareList(encryptionKeyShareList);
    }

    private EncryptionKeyShare createEncryptionKeyShare(int i) {
        EncryptionKeyShare encryptionKeyShare = new EncryptionKeyShare();
        encryptionKeyShare.setElectionId(config.getElectionId());
        encryptionKeyShare.setKey(keyStore.getTallierEncryptionKey(i));

        return encryptionKeyShare;
    }
}
