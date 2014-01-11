/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.BlindedGenerator;
import ch.bfh.univote.common.EncryptionKeyShare;
import ch.bfh.univote.common.Proof;
import ch.hsr.univote.unigen.generator.prov.WahlGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.bg;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.bts;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.sg;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.helper.XMLHelper;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class EncryptionKeyShareTask extends WahlGenerator {

    public static void run() throws Exception {
        for (int i = 0; i < talliers.length; i++) {
            EncryptionKeyShare encryptionKeyShare = new EncryptionKeyShare();
            encryptionKeyShare.setElectionId(ConfigHelper.getElectionId());
            encryptionKeyShare.setKey(BigInteger.TEN);
            Proof proof = new Proof();
            proof.getCommitment().add(BigInteger.TEN);
            proof.getResponse().add(BigInteger.ONE);
            
            encryptionKeyShare.setProof(proof);
            encryptionKeyShare.setSignature(SignatureGenerator.createSignature(talliers[i], encryptionKeyShare, talliersPrivateKey[i]));
            encryptionKeyShareList[i] = encryptionKeyShare;
        }
        writeEncryptionKeyShare(encryptionKeyShareList);
    }

    private static void writeEncryptionKeyShare(EncryptionKeyShare[] encryptionKeyShare) {
        try {
            PrintWriter writer = new PrintWriter(ConfigHelper.getEncryptionKeySharePath(), ConfigHelper.getCharEncoding());
            writer.println(XMLHelper.serialize(encryptionKeyShare));
            writer.close();
            System.out.println("Der EncryptionKeyShare wurde in die Datei " + ConfigHelper.getEncryptionKeySharePath() + " geschrieben");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
