/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.Certificate;
import ch.bfh.univote.common.ElectionSystemInfo;
import ch.hsr.univote.unigen.VoteGenerator;
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
public class ElectionSystemInfoTask extends VoteGenerator {

    public void run() throws Exception {
        /*create ElectionSystemInfo*/
        ElectionSystemInfo electionSystemInfo = createElectionSystemInfo();

        /*sign by ElectionaManger*/
        electionSystemInfo.setSignature(new SignatureGenerator().createSignature(electionSystemInfo, keyStore.getElectionManagerPrivateKey()));

        /*submit to ElectionBoard*/
        electionBoard.setElectionSystemInfo(electionSystemInfo);
    }

    private ElectionSystemInfo createElectionSystemInfo() {
        try {
            ElectionSystemInfo electionSystemInfo = new ElectionSystemInfo();
            /*set election id*/
            electionSystemInfo.setElectionId(config.getElectionId());
            /*CertificateAuthority*/
            KeyPair caKeyPair = new RSA().getRSAKeyPair();
            keyStore.setCaKeyPair(caKeyPair);
            keyStore.setCertificateAuthorityPrivateKey((RSAPrivateKey) caKeyPair.getPrivate());
            keyStore.setCertificateAuthorityPublicKey((RSAPublicKey) caKeyPair.getPublic());
            Certificate caCertificate = new Certificate();
            caCertificate.setValue(new CertificateGenerator().getCertficate(config.getCertificateAuthorityId(), keyStore.getCertificateAuthorityPrivateKey(), keyStore.getCertificateAuthorityPublicKey()).getBytes());
            electionSystemInfo.setCertificateAuthority(caCertificate);

            /*ElectionManager*/
            KeyPair emKeyPair = new RSA().getRSAKeyPair();
            keyStore.setElectionManagerPrivateKey((RSAPrivateKey) emKeyPair.getPrivate());
            keyStore.setElectionManagerPublicKey((RSAPublicKey) emKeyPair.getPublic());
            Certificate emCertificate = new Certificate();
            emCertificate.setValue(new CertificateGenerator().getCertficate(config.getElectionManagerId(), keyStore.getElectionManagerPrivateKey(), keyStore.getElectionManagerPublicKey()).getBytes());
            electionSystemInfo.setElectionManager(emCertificate);

            /*ElectionAdministrator*/
            KeyPair eaKeyPair = new RSA().getRSAKeyPair();
            keyStore.setElectionAdministratorPrivateKey((RSAPrivateKey) eaKeyPair.getPrivate());
            keyStore.setElectionAdministratorPublicKey((RSAPublicKey) eaKeyPair.getPublic());
            Certificate eaCertificate = new Certificate();
            eaCertificate.setValue(new CertificateGenerator().getCertficate(config.getElectionAdministratorId(), keyStore.getElectionAdministratorPrivateKey(), keyStore.getElectionAdministratorPublicKey()).getBytes());
            electionSystemInfo.setElectionAdministration(eaCertificate);

            /*Mixers*/
            for (int i = 0; i < electionBoard.mixers.length; i++) {               
                KeyPair keyPair = new RSA().getRSAKeyPair();
                keyStore.setMixerPrivateKey(i, (RSAPrivateKey) keyPair.getPrivate());
                keyStore.setMixerPublicKey(i, (RSAPublicKey) keyPair.getPublic());
                Certificate certificate = new Certificate();
                certificate.setValue(new CertificateGenerator().getCertficate(electionBoard.mixers[i], keyStore.getMixerPrivateKey(i), keyStore.getMixerPublicKey(i)).getBytes());
                electionSystemInfo.getMixer().add(certificate);
            }

            /*Talliers*/
            for (int i = 0; i < electionBoard.talliers.length; i++) {
                KeyPair keyPair = new RSA().getRSAKeyPair();
                keyStore.setTallierPrivateKey(i, (RSAPrivateKey) keyPair.getPrivate());
                keyStore.setTallierPublicKey(i, (RSAPublicKey) keyPair.getPublic());
                Certificate certificate = new Certificate();
                certificate.setValue(new CertificateGenerator().getCertficate(electionBoard.talliers[i], keyStore.getTallierPrivateKey(i), keyStore.getTallierPublicKey(i)).getBytes());
                electionSystemInfo.getTallier().add(certificate);
            }

            return electionSystemInfo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}