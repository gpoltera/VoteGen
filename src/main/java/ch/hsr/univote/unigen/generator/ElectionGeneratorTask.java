/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.Ballots;
import ch.bfh.univote.common.ElectionGenerator;
import ch.bfh.univote.common.Signature;
import ch.hsr.univote.unigen.generator.prov.TimestampGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.eg;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.helper.XMLHelper;
import ch.hsr.univote.unigen.krypto.RSAGenerator;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;

/**
 *
 * @author Gian Poltéra
 */
public class ElectionGeneratorTask {
     public static void run() throws Exception {
        eg.setElectionId(ConfigHelper.getElectionId());
        eg.setGenerator(BigInteger.TEN);
        
        signElectionGenerator(eg);
        writeElectionGenerator(eg);
    }

    private static void signElectionGenerator(ElectionGenerator electionGenerator) throws Exception {
        RSAPrivateKey privateKey = RSAGenerator.getPrivateKey();
        Signature signature = SignatureGenerator.createSignature(electionGenerator, privateKey);
        signature.setSignerId(ConfigHelper.getAdministrationId());
        signature.setTimestamp(TimestampGenerator.generateTimestamp());
        eg.setSignature(signature);
    }

    private static void writeElectionGenerator(ElectionGenerator electionGenerator) {
        try {
            PrintWriter writer = new PrintWriter(ConfigHelper.getElectionGeneratorPath(), ConfigHelper.getCharEncoding());
            writer.println(XMLHelper.serialize(electionGenerator));
            writer.close();
            System.out.println("Der ElectionGenerator wurde in die Datei " + ConfigHelper.getElectionGeneratorPath() + " geschrieben");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
