/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.Certificate;
import ch.bfh.univote.common.Signature;
import ch.hsr.univote.unigen.generator.prov.TimestampGenerator;
import ch.hsr.univote.unigen.generator.prov.WahlGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.esi;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.talliers;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.talliersPrivateKey;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.talliersPublicKey;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.vc;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator2.electionSystemInfo;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.CertificateGenerator;
import ch.hsr.univote.unigen.krypto.CertificateHelperOrig;
import ch.hsr.univote.unigen.krypto.RSA;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 *
 * @author Gian Poltéra
 */
public class VoterCertsTask extends WahlGenerator{

    public static void run() throws Exception {
        vc.setElectionId(ConfigHelper.getElectionId());
        
        for (int i = 0; i < ConfigHelper.getVotersNumber(); i++) {
            KeyPair keyPair = RSA.getRSAKeyPair();
            votersPrivateKey[i] = (RSAPrivateKey) keyPair.getPrivate();
            votersPublicKey[i] = (RSAPublicKey) keyPair.getPublic();
            Certificate certificate = new Certificate();
            certificate.setValue(CertificateGenerator.main("voter" + i+1, keyPair).getBytes());
            vc.getCertificate().add(certificate);
        }
        
        /*sign by electionamanger*/
        vc.setSignature(SignatureGenerator.createSignature(vc, electionManagerPrivateKey));
    }
}
