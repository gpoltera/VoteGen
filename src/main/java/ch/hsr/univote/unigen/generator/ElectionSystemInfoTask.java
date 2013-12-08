/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.ElectionSystemInfo;
import ch.bfh.univote.common.Signature;
import ch.hsr.univote.unigen.generator.prov.TimestampGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.cert;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.esi;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.RSAGenerator;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.security.interfaces.RSAPrivateKey;

/**
 *
 * @author Gian Poltéra
 */
public class ElectionSystemInfoTask {

    public static void run() throws Exception {
        esi.setElectionId(ConfigHelper.getElectionId());
        esi.setCertificateAuthority(cert);
        esi.setElectionAdministration(cert);
        esi.setElectionManager(cert);
        esi.getMixer().add(cert);
        esi.getMixer().add(cert);
        esi.getMixer().add(cert);
        esi.getMixer().add(cert);
        esi.getMixer().add(cert);
        esi.getTallier().add(cert);
        esi.getTallier().add(cert);
        esi.getTallier().add(cert);
        esi.getTallier().add(cert);

        signElectionSystemInfo(esi);
    }

    private static void signElectionSystemInfo(ElectionSystemInfo electionSystemInfo) throws Exception {
        RSAPrivateKey privateKey = RSAGenerator.getPrivateKey();
        // RSAPrivateKey privateKey = KeystoreHelper.getPrivateKey(); -> Original Zeile
        Signature signature = SignatureGenerator.createSignature(electionSystemInfo, privateKey);
        signature.setSignerId(ConfigHelper.getAdministrationId());
        signature.setTimestamp(TimestampGenerator.generateTimestamp());
        electionSystemInfo.setSignature(signature);
    }
}
