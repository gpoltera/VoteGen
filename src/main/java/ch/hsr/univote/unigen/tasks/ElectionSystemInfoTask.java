/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.Certificate;
import ch.bfh.univote.common.ElectionSystemInfo;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.CertificateGenerator;
import ch.hsr.univote.unigen.krypto.RSA;
import ch.hsr.univote.unigen.krypto.RSASignatureGenerator;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 *
 * @author Gian Poltéra
 */
public class ElectionSystemInfoTask {
    private ConfigHelper config;
    private ElectionBoard electionBoard;
    private KeyStore keyStore;

    public ElectionSystemInfoTask() {
        this.config = VoteGenerator.config;
        this.electionBoard = VoteGenerator.electionBoard;
        this.keyStore = VoteGenerator.keyStore;
        
        run();
    }
    
    public void run() {
        /*create ElectionSystemInfo*/
        ElectionSystemInfo electionSystemInfo = createElectionSystemInfo();

        /*sign by ElectionaManger*/
        electionSystemInfo.setSignature(new RSASignatureGenerator().createSignature(electionSystemInfo, keyStore.getEMSignatureKey()));

        /*submit to ElectionBoard*/
        electionBoard.setElectionSystemInfo(electionSystemInfo);
    }

    private ElectionSystemInfo createElectionSystemInfo() {
        ElectionSystemInfo electionSystemInfo = new ElectionSystemInfo();
        /*set election id*/
        electionSystemInfo.setElectionId(config.getElectionId());
        /*CertificateAuthority*/
        createCAKeys();
        Certificate caCertificate = new Certificate();
        caCertificate.setValue(new CertificateGenerator().getCertficate(config.getCertificateAuthorityId(), keyStore.getCASignatureKey(), keyStore.getCAVerificationKey()).getBytes());
        electionSystemInfo.setCertificateAuthority(caCertificate);
        
        /*ElectionManager*/
        createEMKeys();
        Certificate emCertificate = new Certificate();
        emCertificate.setValue(new CertificateGenerator().getCertficate(config.getElectionManagerId(), keyStore.getEMSignatureKey(), keyStore.getEMVerificationKey()).getBytes());
        electionSystemInfo.setElectionManager(emCertificate);

        /*ElectionAdministrator*/
        createEAKeys();
        Certificate eaCertificate = new Certificate();
        eaCertificate.setValue(new CertificateGenerator().getCertficate(config.getElectionAdministratorId(), keyStore.getEASignatureKey(), keyStore.getEAVerificationKey()).getBytes());
        electionSystemInfo.setElectionAdministration(eaCertificate);

        /*Mixers*/
        for (int i = 0; i < electionBoard.mixers.length; i++) {
            createMixerKeys(i);
            Certificate certificate = new Certificate();
            certificate.setValue(new CertificateGenerator().getCertficate(electionBoard.mixers[i], keyStore.getMixerSignatureKey(i), keyStore.getMixerVerificationKey(i)).getBytes());
            electionSystemInfo.getMixer().add(certificate);
        }

        /*Talliers*/
        for (int i = 0; i < electionBoard.talliers.length; i++) {
            createTallierKeys(i);
            Certificate certificate = new Certificate();
            certificate.setValue(new CertificateGenerator().getCertficate(electionBoard.talliers[i], keyStore.getTallierSignatureKey(i), keyStore.getTallierVerificationKey(i)).getBytes());
            electionSystemInfo.getTallier().add(certificate);
        }

        return electionSystemInfo;
    }

    private void createCAKeys() {
        KeyPair caKeyPair = new RSA().getKeyPair();
        keyStore.setCASignatureKey((RSAPrivateKey) caKeyPair.getPrivate());
        keyStore.setCAVerificationKey((RSAPublicKey) caKeyPair.getPublic());
    }

    private void createEMKeys() {
        KeyPair emKeyPair = new RSA().getKeyPair();
        keyStore.setEMSignatureKey((RSAPrivateKey) emKeyPair.getPrivate());
        keyStore.setEMVerificationKey((RSAPublicKey) emKeyPair.getPublic());
    }

    private void createEAKeys() {
        KeyPair eaKeyPair = new RSA().getKeyPair();
        keyStore.setEASignatureKey((RSAPrivateKey) eaKeyPair.getPrivate());
        keyStore.setEAVerificationKey((RSAPublicKey) eaKeyPair.getPublic());
    }

    private void createMixerKeys(int i) {
        KeyPair keyPair = new RSA().getKeyPair();
        keyStore.setMixerSignatureKey(i, (RSAPrivateKey) keyPair.getPrivate());
        keyStore.setMixerVerificationKey(i, (RSAPublicKey) keyPair.getPublic());
    }

    private void createTallierKeys(int i) {
        KeyPair keyPair = new RSA().getKeyPair();
        keyStore.setTallierSignatureKey(i, (RSAPrivateKey) keyPair.getPrivate());
        keyStore.setTallierVerificationKey(i, (RSAPublicKey) keyPair.getPublic());
    }
}
