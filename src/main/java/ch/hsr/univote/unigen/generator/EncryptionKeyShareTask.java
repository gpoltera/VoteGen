/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.EncryptionKeyShare;
import ch.hsr.univote.unigen.generator.prov.WahlGenerator;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.helper.XMLHelper;
import ch.hsr.univote.unigen.krypto.ProofGenerator;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Gian Poltéra
 */
public class EncryptionKeyShareTask extends WahlGenerator {

    public static void run() throws Exception {
        for (int i = 0; i < talliers.length; i++) {
            EncryptionKeyShare encryptionKeyShare = new EncryptionKeyShare();
            encryptionKeyShare.setElectionId(ConfigHelper.getElectionId());
            encryptionKeyShare.setKey(talliersEncryptionKey[i]);
            
            //set the proof
            encryptionKeyShare.setProof(ProofGenerator.getProof(
                    talliers[i], 
                    talliersDecryptionKey[i], 
                    talliersEncryptionKey[i], 
                    WahlGenerator.encryptionParameters.getPrime(), 
                    WahlGenerator.encryptionParameters.getGroupOrder(), 
                    WahlGenerator.encryptionParameters.getGenerator()));
            //set the signature
            encryptionKeyShare.setSignature(SignatureGenerator.createSignature(talliers[i], encryptionKeyShare, talliersPrivateKey[i]));
            encryptionKeyShareList[i] = encryptionKeyShare;
        }
    }
}
