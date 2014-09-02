/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.EncryptionKeyShare;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.ElGamal;
import ch.hsr.univote.unigen.krypto.NIZKP;
import ch.hsr.univote.unigen.krypto.RSASignatureGenerator;
import java.security.KeyPair;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Gian Poltéra
 */
public class EncryptionKeyShareTask {

    private ConfigHelper config;
    private ElectionBoard electionBoard;
    private KeyStore keyStore;

    public EncryptionKeyShareTask() {
        this.config = VoteGenerator.config;
        this.electionBoard = VoteGenerator.electionBoard;
        this.keyStore = VoteGenerator.keyStore;

        run();
    }

    /*1.3.4 d) Distributed Key Generation*/
    private void run() {
        Map<String,EncryptionKeyShare> encryptionKeyShareList = new HashMap<>();

        /*for each tallier*/
        for (int j = 0; j < electionBoard.talliers.length; j++) {

            /*create EncryptionKeyShare*/
            EncryptionKeyShare encryptionKeyShare = createEncryptionKeyShare(j);

            /*add to list*/
            encryptionKeyShareList.put(electionBoard.talliers[j], encryptionKeyShare);
        }
        /*submit to ElectionBoard*/
        electionBoard.setEncryptionKeyShareList(encryptionKeyShareList);
    }

    private EncryptionKeyShare createEncryptionKeyShare(int j) {
        EncryptionKeyShare encryptionKeyShare = new EncryptionKeyShare();
        encryptionKeyShare.setElectionId(config.getElectionId());
        
        createTallierKeys(j);
        encryptionKeyShare.setKey(keyStore.getTallierEncryptionKey(j).getY());
        

        encryptionKeyShare.setProof(new NIZKP().getEncryptionKeyShareProof(
            electionBoard.talliers[j],
            keyStore.getTallierDecryptionKey(j),
            keyStore.getTallierEncryptionKey(j)));

        encryptionKeyShare.setSignature(new RSASignatureGenerator().createSignature(electionBoard.talliers[j], encryptionKeyShare, keyStore.getTallierSignatureKey(j)));

        return encryptionKeyShare;
    }

    private void createTallierKeys(int j) {
        KeyPair keyPair = new ElGamal().getKeyPair(electionBoard.getEncryptionParameters());
        
        keyStore.setTallierDecryptionKey(j, (DSAPrivateKey) keyPair.getPrivate());
        keyStore.setTallierEncryptionKey(j, (DSAPublicKey) keyPair.getPublic());
    }
}
