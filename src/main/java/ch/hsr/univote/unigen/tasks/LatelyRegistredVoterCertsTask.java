/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.Certificate;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.crypto.CertificateGenerator;
import ch.hsr.univote.unigen.crypto.Schnorr;
import java.security.KeyPair;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian Poltéra
 */
public class LatelyRegistredVoterCertsTask {

    private ConfigHelper config;
    private ElectionBoard electionBoard;
    private KeyStore keyStore;

    public LatelyRegistredVoterCertsTask() {
        this.config = VoteGenerator.config;
        this.electionBoard = VoteGenerator.electionBoard;
        this.keyStore = VoteGenerator.keyStore;

        run();
    }

    private void run() {
        /*read VoterCertificates from ElectionBoard*/
        List<Certificate> latelyVoterCertificates = new ArrayList<>();

        /*create certificates*/
        for (int i = 0; i < config.getLatelyVotersNumber(); i++) {
            createVoterKeys(i);
            Certificate certificate = new Certificate();
            certificate.setValue(new CertificateGenerator().getCertficate("latelyvoter" + i + 1, keyStore.getCASignatureKey(), keyStore.getLatelyVoterVerificationKey(i)).getBytes());
            latelyVoterCertificates.add(certificate);
        }
        
        /*submit to ElectionBoard*/
        electionBoard.setLatelyRegisteredVoterCertificates(latelyVoterCertificates);
    }

    private void createVoterKeys(int i) {
        // Schnorr Keys
        KeyPair keyPair = new Schnorr(config).getKeyPair(electionBoard.getSignatureParameters());

        keyStore.setLatelyVoterSignatureKey(i, (DSAPrivateKey) keyPair.getPrivate());
        keyStore.setLatelyVoterVerificationKey(i, (DSAPublicKey) keyPair.getPublic());
    }
}
