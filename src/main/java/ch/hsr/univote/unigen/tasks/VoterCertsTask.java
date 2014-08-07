/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.Certificate;
import ch.bfh.univote.common.VoterCertificates;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.krypto.CertificateGenerator;
import ch.hsr.univote.unigen.krypto.RSA;
import ch.hsr.univote.unigen.krypto.RSASignatureGenerator;
import ch.hsr.univote.unigen.krypto.Schnorr;
import ch.hsr.univote.unigen.krypto.SchnorrSignatureKey;
import ch.hsr.univote.unigen.krypto.SchnorrVerificationKey;
import java.math.BigInteger;
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
        voterCertificates.setSignature(new RSASignatureGenerator().createSignature(voterCertificates, keyStore.getElectionManagerPrivateKey()));

        /*submit to ElectionBoard*/
        electionBoard.setVoterCertificates(voterCertificates);
    }

    private VoterCertificates createVoterCertificates() {
        VoterCertificates voterCertificates = new VoterCertificates();
        voterCertificates.setElectionId(config.getElectionId());
        for (int i = 0; i < config.getVotersNumber(); i++) {
            createVoterKeys(i);
            Certificate certificate = new Certificate();
            certificate.setValue(new CertificateGenerator().getCertficate("voter" + i + 1, keyStore.getCertificateAuthorityPrivateKey(), keyStore.getVoterPublicKey(i)).getBytes());
            voterCertificates.getCertificate().add(certificate);
        }
        return voterCertificates;
    }

    private void createVoterKeys(int i) {
        // RSA Keys
        KeyPair keyPair = new RSA().getRSAKeyPair();

        keyStore.setVoterPrivateKey(i, (RSAPrivateKey) keyPair.getPrivate());
        keyStore.setVoterPublicKey(i, (RSAPublicKey) keyPair.getPublic());

        // Schnorr Keys
        BigInteger[] skp = new Schnorr().getKeyPair(electionBoard.getSignatureParameters());

        keyStore.setVoterSignatureKey(i, new SchnorrSignatureKey(electionBoard.getSignatureParameters(), skp[0]));
        keyStore.setVoterVerificationKey(i, new SchnorrVerificationKey(electionBoard.getSignatureParameters(), skp[1]));
    }
}
