/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.Certificate;
import ch.bfh.univote.common.VoterCertificates;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.crypto.CertificateGenerator;
import ch.hsr.univote.unigen.crypto.RSASignatureGenerator;
import ch.hsr.univote.unigen.crypto.Schnorr;
import java.security.KeyPair;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;

/**
 *
 * @author Gian Poltéra
 */
public class VoterCertsTask {
    private ConfigHelper config;
    private ElectionBoard electionBoard;
     private KeyStore keyStore;
            
    public VoterCertsTask () {
        this.config = VoteGenerator.config;
        this.electionBoard = VoteGenerator.electionBoard;
        this.keyStore = VoteGenerator.keyStore;
        
        run();
    }
    
    

    private void run() {
        /*create VoterCertificates*/
        VoterCertificates voterCertificates = createVoterCertificates();

        /*sign by ElectionaManger*/
        voterCertificates.setSignature(new RSASignatureGenerator().createSignature(voterCertificates, keyStore.getEMSignatureKey()));

        /*submit to ElectionBoard*/
        electionBoard.setVoterCertificates(voterCertificates);
    }

    private VoterCertificates createVoterCertificates() {
        VoterCertificates voterCertificates = new VoterCertificates();
        voterCertificates.setElectionId(config.getElectionId());
        for (int i = 0; i < config.getVotersNumber(); i++) {
            createVoterKeys(i);
            Certificate certificate = new Certificate();
            certificate.setValue(new CertificateGenerator().getCertficate("voter" + i + 1, keyStore.getCASignatureKey(), keyStore.getVoterVerificationKey(i)).getBytes());
            voterCertificates.getCertificate().add(certificate);
        }
        return voterCertificates;
    }

    private void createVoterKeys(int i) {
        // Schnorr Keys
        KeyPair keyPair = new Schnorr(config).getKeyPair(electionBoard.getSignatureParameters());

        keyStore.setVoterSignatureKey(i, (DSAPrivateKey) keyPair.getPrivate());
        keyStore.setVoterVerificationKey(i, (DSAPublicKey) keyPair.getPublic());
    }
}
