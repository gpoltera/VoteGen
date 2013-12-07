/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.DecodedVote;
import ch.bfh.univote.common.DecodedVoteEntry;
import ch.bfh.univote.common.DecodedVotes;
import ch.bfh.univote.common.Signature;
import ch.hsr.univote.unigen.generator.prov.TimestampGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.dov;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.helper.XMLHelper;
import ch.hsr.univote.unigen.krypto.RSAGenerator;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.interfaces.RSAPrivateKey;

/**
 *
 * @author Gian
 */
public class DecodedVotesTask {

    public static void run() throws Exception {
        // election id
        dov.setElectionId(ConfigHelper.getElectionId());
        
        for (int i = 1; i <= 100; i++) {
            DecodedVote dv = new DecodedVote();
            DecodedVoteEntry dve = new DecodedVoteEntry();
            dve.setChoiceId(1);
            dve.setCount(1);
            dv.getEntry().add(dve);

            dov.getDecodedVote().add(dv);
        }
        
        signDecodedVotes(dov);
        writeDecodedVotes(dov);
    }
        private static void signDecodedVotes(DecodedVotes dov) throws Exception {
        RSAPrivateKey privateKey = RSAGenerator.getPrivateKey();
        Signature signature = SignatureGenerator.createSignature(dov, privateKey);
        signature.setSignerId(ConfigHelper.getAdministrationId());
        signature.setTimestamp(TimestampGenerator.generateTimestamp());
        dov.setSignature(signature);
    }

    private static void writeDecodedVotes(DecodedVotes dov) {
        try {
            PrintWriter writer = new PrintWriter(ConfigHelper.getDecodedVotesPath(), ConfigHelper.getCharEncoding());
            writer.println(XMLHelper.serialize(dov));
            writer.close();
            System.out.println("Die Wahlzettel wurden in die Datei " + ConfigHelper.getDecodedVotesPath() + " geschrieben");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
