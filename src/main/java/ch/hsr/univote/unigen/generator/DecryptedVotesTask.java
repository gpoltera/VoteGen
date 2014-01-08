/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.DecryptedVotes;
import ch.bfh.univote.common.Signature;
import ch.hsr.univote.unigen.generator.prov.TimestampGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.dyv;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.helper.XMLHelper;
import ch.hsr.univote.unigen.krypto.RSAGenerator;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;

/**
 *
 * @author Gian Poltéra
 */
public class DecryptedVotesTask {

    public static void run() throws Exception {
        dyv.setElectionId(ConfigHelper.getElectionId());
        
        for (int i = 1; i <= ConfigHelper.getVotersNumber(); i++) {
            dyv.getVote().add(BigInteger.ONE);
        }

        signDecryptedVotes(dyv);
        writeDecryptedVoted(dyv);
    }

    private static void signDecryptedVotes(DecryptedVotes decryptedVotes) throws Exception {      
        RSAPrivateKey privateKey = RSAGenerator.getPrivateKey();
        Signature signature = SignatureGenerator.createSignature(decryptedVotes, privateKey);
        signature.setSignerId(ConfigHelper.getAdministrationId());
        signature.setTimestamp(TimestampGenerator.generateTimestamp());
        decryptedVotes.setSignature(signature);
        dyv.setSignature(signature);
    }

    private static void writeDecryptedVoted(DecryptedVotes decryptedVotes) {
        try {
            PrintWriter writer = new PrintWriter(ConfigHelper.getDecryptedVotesPath(), ConfigHelper.getCharEncoding());
            writer.println(XMLHelper.serialize(decryptedVotes));
            writer.close();
            System.out.println("Die Wahlzettel wurden in die Datei " + ConfigHelper.getDecryptedVotesPath() + " geschrieben");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
