/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.Certificate;
import ch.bfh.univote.common.ElectionSystemInfo;
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
public class ElectionSystemInfoTask extends VoteGenerator {

    public void run() throws Exception {
        /*create ElectionSystemInfo*/
        ElectionSystemInfo electionSystemInfo = createElectionSystemInfo();

        /*sign by ElectionaManger*/
        electionSystemInfo.setSignature(SignatureGenerator.createSignature(electionSystemInfo, keyStore.electionManagerPrivateKey));

        /*submit to ElectionBoard*/
        electionBoard.electionSystemInfo = electionSystemInfo;

        /*save in db*/
        DB4O.storeDB(ConfigHelper.getElectionId(), electionSystemInfo);
    }

    private ElectionSystemInfo createElectionSystemInfo() {
        try {
            ElectionSystemInfo electionSystemInfo = new ElectionSystemInfo();
            /*set election id*/
            electionSystemInfo.setElectionId(ConfigHelper.getElectionId());

            /*CertificateAuthority*/
            KeyPair caKeyPair = RSA.getRSAKeyPair();
            keyStore.caKeyPair = caKeyPair;
            keyStore.certificateAuthorityPrivateKey = (RSAPrivateKey) caKeyPair.getPrivate();
            keyStore.certificateAuthorityPublicKey = (RSAPublicKey) caKeyPair.getPublic();
            Certificate caCertificate = new Certificate();
            caCertificate.setValue(CertificateGenerator.main(ConfigHelper.getCertificateAuthorityId(), keyStore.certificateAuthorityPrivateKey, keyStore.certificateAuthorityPublicKey).getBytes());
            electionSystemInfo.setCertificateAuthority(caCertificate);

            /*ElectionManager*/
            KeyPair emKeyPair = RSA.getRSAKeyPair();
            keyStore.electionManagerPrivateKey = (RSAPrivateKey) emKeyPair.getPrivate();
            keyStore.electionManagerPublicKey = (RSAPublicKey) emKeyPair.getPublic();
            Certificate emCertificate = new Certificate();
            emCertificate.setValue(CertificateGenerator.main(ConfigHelper.getElectionManagerId(), keyStore.electionManagerPrivateKey, keyStore.electionManagerPublicKey).getBytes());
            electionSystemInfo.setElectionManager(emCertificate);

            /*ElectionAdministrator*/
            KeyPair eaKeyPair = RSA.getRSAKeyPair();
            keyStore.electionAdministratorPrivateKey = (RSAPrivateKey) eaKeyPair.getPrivate();
            keyStore.electionAdministratorPublicKey = (RSAPublicKey) eaKeyPair.getPublic();
            Certificate eaCertificate = new Certificate();
            eaCertificate.setValue(CertificateGenerator.main(ConfigHelper.getElectionAdministratorId(), keyStore.electionAdministratorPrivateKey, keyStore.electionAdministratorPublicKey).getBytes());
            electionSystemInfo.setElectionAdministration(eaCertificate);

            /*Mixers*/
            for (int i = 0; i < electionBoard.mixers.length; i++) {
                KeyPair keyPair = RSA.getRSAKeyPair();
                keyStore.mixersPrivateKey[i] = (RSAPrivateKey) keyPair.getPrivate();
                keyStore.mixersPublicKey[i] = (RSAPublicKey) keyPair.getPublic();
                Certificate certificate = new Certificate();
                certificate.setValue(CertificateGenerator.main(electionBoard.mixers[i], keyStore.mixersPrivateKey[i], keyStore.mixersPublicKey[i]).getBytes());
                electionSystemInfo.getMixer().add(certificate);
            }

            /*Talliers*/
            for (int i = 0; i < electionBoard.talliers.length; i++) {
                KeyPair keyPair = RSA.getRSAKeyPair();
                keyStore.talliersPrivateKey[i] = (RSAPrivateKey) keyPair.getPrivate();
                keyStore.talliersPublicKey[i] = (RSAPublicKey) keyPair.getPublic();
                Certificate certificate = new Certificate();
                certificate.setValue(CertificateGenerator.main(electionBoard.talliers[i], keyStore.talliersPrivateKey[i], keyStore.talliersPublicKey[i]).getBytes());
                electionSystemInfo.getTallier().add(certificate);
            }

            return electionSystemInfo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}