/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.Certificate;
import ch.bfh.univote.common.ElectionSystemInfo;
import ch.bfh.univote.common.Signature;
import ch.hsr.univote.unigen.generator.prov.TimestampGenerator;
import ch.hsr.univote.unigen.generator.prov.WahlGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.esi;
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
public class ElectionSystemInfoTask extends WahlGenerator {

    public static void run() throws Exception {

        //EDIT THIS FILE
        esi.setElectionId(ConfigHelper.getElectionId());

        //CertificateAuthority
        KeyPair caKeyPair = RSA.getRSAKeyPair();
        certificateAuthorityPrivateKey = (RSAPrivateKey) caKeyPair.getPrivate();
        certificateAuthorityPublicKey = (RSAPublicKey) caKeyPair.getPublic();
        Certificate caCertificate = new Certificate();
        caCertificate.setValue(CertificateGenerator.main(ConfigHelper.getCertificateAuthorityId(), caKeyPair).getBytes());
        esi.setCertificateAuthority(caCertificate);

        //ElectionManager
        KeyPair emKeyPair = RSA.getRSAKeyPair();
        electionManagerPrivateKey = (RSAPrivateKey) emKeyPair.getPrivate();
        electionManagerPublicKey = (RSAPublicKey) emKeyPair.getPublic();
        Certificate emCertificate = new Certificate();
        emCertificate.setValue(CertificateGenerator.main(ConfigHelper.getElectionManagerId(), emKeyPair).getBytes());
        esi.setElectionManager(emCertificate);

        //ElectionAdministrator
        KeyPair eaKeyPair = RSA.getRSAKeyPair();
        electionAdministratorPrivateKey = (RSAPrivateKey) eaKeyPair.getPrivate();
        electionAdministratorPublicKey = (RSAPublicKey) eaKeyPair.getPublic();
        Certificate eaCertificate = new Certificate();
        eaCertificate.setValue(CertificateGenerator.main(ConfigHelper.getElectionAdministratorId(), eaKeyPair).getBytes());
        esi.setElectionAdministration(eaCertificate);

        //Mixers
        for (int i = 0; i < mixers.length; i++) {
            KeyPair keyPair = RSA.getRSAKeyPair();
            mixersPrivateKey[i] = (RSAPrivateKey) keyPair.getPrivate();
            mixersPublicKey[i] = (RSAPublicKey) keyPair.getPublic();
            Certificate certificate = new Certificate();
            certificate.setValue(CertificateGenerator.main(mixers[i], keyPair).getBytes());
            esi.getMixer().add(certificate);
        }

        //Talliers
        for (int i = 0; i < talliers.length; i++) {
            KeyPair keyPair = RSA.getRSAKeyPair();
            talliersPrivateKey[i] = (RSAPrivateKey) keyPair.getPrivate();
            talliersPublicKey[i] = (RSAPublicKey) keyPair.getPublic();
            Certificate certificate = new Certificate();
            certificate.setValue(CertificateGenerator.main(talliers[i], keyPair).getBytes());
            esi.getTallier().add(certificate);
        }

        /*sign by electionamanger*/
        esi.setSignature(SignatureGenerator.createSignature(esi, electionManagerPrivateKey));
    }
}
