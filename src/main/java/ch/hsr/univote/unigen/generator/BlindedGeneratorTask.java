/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.BlindedGenerator;
import ch.bfh.univote.common.Proof;
import ch.hsr.univote.unigen.generator.prov.WahlGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.sg;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.helper.XMLHelper;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class BlindedGeneratorTask {

    public static void run() throws Exception {
        // Mixers from the ConfigFile
        String[] mixers = ConfigHelper.getMixerIds();
        
        
        for (int i = 0; i < mixers.length; i++) {
            BlindedGenerator blindedGenerator = new BlindedGenerator();
            blindedGenerator.setElectionId(ConfigHelper.getElectionId());
            blindedGenerator.setGenerator(BigInteger.TEN);
            Proof proof = new Proof();
            proof.getCommitment().add(BigInteger.TEN);
            proof.getResponse().add(BigInteger.TEN);
            
            blindedGenerator.setProof(proof);
            blindedGenerator.setSignature(sg);
            WahlGenerator.blindedGeneratorsList[i] = blindedGenerator;
        }
        //writeBlindedGenerator(bg);
    }

    private static void writeBlindedGenerator(BlindedGenerator blindedGenerator) {
        try {
            PrintWriter writer = new PrintWriter(ConfigHelper.getBlindedGeneratorPath(), ConfigHelper.getCharEncoding());
            writer.println(XMLHelper.serialize(blindedGenerator));
            writer.close();
            System.out.println("Der BlindedGenerator wurde in die Datei " + ConfigHelper.getBallotsPath() + " geschrieben");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
