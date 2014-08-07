/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.EncryptionKeyShare;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.krypto.ElGamal;
import ch.hsr.univote.unigen.krypto.NIZKP;
import ch.hsr.univote.unigen.krypto.RSASignatureGenerator;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian Poltéra
 */
public class EncryptionKeyShareTask extends VoteGenerator {

    /*1.3.4 d) Distributed Key Generation*/
    public void run() {
        List<EncryptionKeyShare> encryptionKeyShareList = new ArrayList<>();

        /*for each tallier*/
        for (int i = 0; i < electionBoard.talliers.length; i++) {

            /*create EncryptionKeyShare*/
            EncryptionKeyShare encryptionKeyShare = createEncryptionKeyShare(i);

            /*set the proof*/
            encryptionKeyShare.setProof(new NIZKP().getProof(
                    electionBoard.talliers[i],
                    keyStore.getTallierDecryptionKey(i),
                    keyStore.getTallierEncryptionKey(i),
                    electionBoard.getEncryptionParameters()));

            /*sign by tallier*/
            encryptionKeyShare.setSignature(new RSASignatureGenerator().createSignature(electionBoard.talliers[i], encryptionKeyShare, keyStore.getTallierPrivateKey(i)));

            /*add to list*/
            encryptionKeyShareList.add(i, encryptionKeyShare);
        }
        /*submit to ElectionBoard*/
        electionBoard.setEncryptionKeyShareList(encryptionKeyShareList);
    }

    private EncryptionKeyShare createEncryptionKeyShare(int i) {
        EncryptionKeyShare encryptionKeyShare = new EncryptionKeyShare();
        createTallierKeys(i);
        encryptionKeyShare.setElectionId(config.getElectionId());
        encryptionKeyShare.setKey(keyStore.getTallierEncryptionKey(i));

        return encryptionKeyShare;
    }

    private void createTallierKeys(int i) {
        BigInteger keyPair[] = new ElGamal().getKeyPair(electionBoard.getEncryptionParameters());
        keyStore.setTallierDecryptionKey(i, keyPair[0]);
        keyStore.setTallierEncryptionKey(i, keyPair[1]);
    }
}
