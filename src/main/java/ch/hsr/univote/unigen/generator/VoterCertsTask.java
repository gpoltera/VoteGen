/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.Certificate;
import ch.bfh.univote.common.Signature;
import ch.hsr.univote.unigen.generator.prov.TimestampGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.vc;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator2.electionSystemInfo;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.CertificateGenerator;
import ch.hsr.univote.unigen.krypto.CertificateHelperOrig;
import ch.hsr.univote.unigen.krypto.RSAGenerator;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.security.interfaces.RSAPrivateKey;

/**
 *
 * @author Gian Poltéra
 */
public class VoterCertsTask {

    public static void run() throws Exception {
        vc.setElectionId(ConfigHelper.getElectionId());
        for (int i = 0; i < ConfigHelper.getVotersNumber(); i++) {
            Certificate certificate = new Certificate();
            certificate.setValue(CertificateGenerator.main("Voter").getBytes());
            vc.getCertificate().add(certificate);
        }

        RSAPrivateKey privateKey = RSAGenerator.getPrivateKey();
        Signature signature = SignatureGenerator.createSignature(vc, privateKey);
        signature.setSignerId(ConfigHelper.getAdministrationId());
        signature.setTimestamp(TimestampGenerator.generateTimestamp());
        electionSystemInfo.setSignature(signature);
        vc.setSignature(signature);
    }
}
