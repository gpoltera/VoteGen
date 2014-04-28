/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.Certificate;
import ch.bfh.univote.common.VoterCertificates;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.db.DB4O;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.CertificateGenerator;
import ch.hsr.univote.unigen.krypto.RSA;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 *
 * @author Gian Poltéra
 */
public class VoterCertsTask extends VoteGenerator {

    public void run() throws Exception {
        /*create VoterCertificates*/
        VoterCertificates voterCertificates = createVoterCertificates();

        /*sign by ElectionaManger*/
        voterCertificates.setSignature(SignatureGenerator.createSignature(voterCertificates, keyStore.electionManagerPrivateKey));

        /*submit to ElectionBoard*/
        electionBoard.voterCertificates = voterCertificates;

        /*save in db*/
        DB4O.storeDB(ConfigHelper.getElectionId(), voterCertificates);
    }

    private VoterCertificates createVoterCertificates() {
        try {
            VoterCertificates voterCertificates = new VoterCertificates();
            voterCertificates.setElectionId(ConfigHelper.getElectionId());
            for (int i = 0; i < ConfigHelper.getVotersNumber(); i++) {
                KeyPair keyPair = RSA.getRSAKeyPair();
                keyStore.votersPrivateKey[i] = (RSAPrivateKey) keyPair.getPrivate();
                keyStore.votersPublicKey[i] = (RSAPublicKey) keyPair.getPublic();

                keyStore.votersSignatureKey[i] = keyStore.votersPrivateKey[i].getPrivateExponent();
                keyStore.votersVerificationKey[i] = electionBoard.signatureParameters.getGenerator().modPow(keyStore.votersSignatureKey[i], electionBoard.signatureParameters.getPrime());
                
                Certificate certificate = new Certificate();
                certificate.setValue(CertificateGenerator.main("voter" + i + 1, keyStore.certificateAuthorityPrivateKey, keyStore.votersPublicKey[i]).getBytes());
                voterCertificates.getCertificate().add(certificate);
            }
            return voterCertificates;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}