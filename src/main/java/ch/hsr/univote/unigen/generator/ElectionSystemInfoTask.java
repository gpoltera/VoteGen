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
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.cert;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.esi;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.helper.KeystoreHelper;
import static ch.hsr.univote.unigen.helper.KeystoreHelper.createKeyPair;
import static ch.hsr.univote.unigen.helper.KeystoreHelper.getPublicKey;
import ch.hsr.univote.unigen.krypto.CertificateCreationHelperOrig;
import ch.hsr.univote.unigen.krypto.CertificateGenerator;
import ch.hsr.univote.unigen.krypto.CertificateHelperOrig;
import ch.hsr.univote.unigen.krypto.RSAGenerator;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.security.interfaces.RSAPrivateKey;

/**
 *
 * @author Gian Poltéra
 */
public class ElectionSystemInfoTask {

    public static void run() throws Exception {
        
        
        //EDIT THIS FILE
        esi.setElectionId(ConfigHelper.getElectionId());
        Certificate certificate = new Certificate();
        
        certificate.setValue(CertificateGenerator.main("CertificateAuthority").getBytes());
        esi.setCertificateAuthority(certificate);
        
        certificate.setValue(CertificateGenerator.main("ElectionManager").getBytes());
        esi.setElectionAdministration(certificate);
        
        certificate.setValue(KeystoreHelper.createKeyPair().getEncoded());        
        esi.setElectionManager(certificate);
        
        certificate.setValue(CertificateGenerator.main("Mixer").getBytes());
        esi.getMixer().add(certificate);
        
        certificate.setValue(CertificateGenerator.main("Mixer").getBytes());
        esi.getMixer().add(certificate);
        
        certificate.setValue(CertificateGenerator.main("Mixer").getBytes());
        esi.getMixer().add(certificate);
        
        certificate.setValue(CertificateGenerator.main("Mixer").getBytes());
        esi.getMixer().add(certificate);
        
        certificate.setValue(CertificateGenerator.main("Mixer").getBytes());
        esi.getMixer().add(certificate);
        
        certificate.setValue(CertificateGenerator.main("Tallier").getBytes());
        esi.getTallier().add(certificate);
        
        certificate.setValue(CertificateGenerator.main("Tallier").getBytes());
        esi.getTallier().add(certificate);
        
        certificate.setValue(CertificateGenerator.main("Tallier").getBytes());
        esi.getTallier().add(certificate);
        
        certificate.setValue(CertificateGenerator.main("Tallier").getBytes());
        esi.getTallier().add(certificate);

        signElectionSystemInfo(esi);
    }

    private static void signElectionSystemInfo(ElectionSystemInfo electionSystemInfo) throws Exception {
        //RSAPrivateKey privateKey = RSAGenerator.getPrivateKey();
        RSAPrivateKey privateKey = KeystoreHelper.getPrivateKey();
        Signature signature = SignatureGenerator.createSignature(electionSystemInfo, privateKey);
        signature.setSignerId(ConfigHelper.getAdministrationId());
        signature.setTimestamp(TimestampGenerator.generateTimestamp());
        electionSystemInfo.setSignature(signature);
    }
}
