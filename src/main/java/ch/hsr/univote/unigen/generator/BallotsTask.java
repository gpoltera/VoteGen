/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.Ballot;
import ch.bfh.univote.common.Ballots;
import ch.bfh.univote.common.EncryptedVote;
import ch.bfh.univote.common.Proof;
import ch.bfh.univote.common.Signature;
import ch.bfh.univote.common.VoterSignature;
import ch.hsr.univote.unigen.generator.prov.TimestampGenerator;
import ch.hsr.univote.unigen.generator.prov.WahlGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.bts;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.helper.XMLHelper;
import ch.hsr.univote.unigen.krypto.RSA;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.PrivateKey;

/**
 *
 * @author Gian Poltéra
 */
public class BallotsTask extends WahlGenerator{

    public static void run() throws Exception {

        bts.setElectionId(ConfigHelper.getElectionId());

        for (int i = 1; i <= ConfigHelper.getVotersNumber(); i++) {
            // New Ballot
            Ballot bt = new Ballot();
            bt.setElectionId(ConfigHelper.getElectionId());

            // Verification Key
            bt.setVerificationKey(BigInteger.TEN);

            // EncryptedVote
            EncryptedVote ev = new EncryptedVote();
            ev.setFirstValue(BigInteger.TEN);
            ev.setSecondValue(BigInteger.TEN);
            bt.setEncryptedVote(ev);

            //Proof
            Proof pf = new Proof();
            pf.getCommitment().add(BigInteger.TEN);
            pf.getResponse().add(BigInteger.TEN);
            bt.setProof(pf);
            
            //Signature
            VoterSignature vs = new VoterSignature();
            vs.setFirstValue(BigInteger.TEN);
            vs.setSecondValue(BigInteger.TEN);
            bt.setSignature(vs);

            // Add to the Ballots
            bts.getBallot().add(bt);
        }
        signBallots(bts);
        writeBallots(bts);
    }

    private static void signBallots(Ballots bts) throws Exception {
         bts.setSignature(SignatureGenerator.createSignature(bts, electionManagerPrivateKey));
    }

    private static void writeBallots(Ballots bts) {
        try {
            PrintWriter writer = new PrintWriter(ConfigHelper.getBallotsPath(), ConfigHelper.getCharEncoding());
            writer.println(XMLHelper.serialize(bts));
            writer.close();
            System.out.println("Die Wahlzettel wurden in die Datei " + ConfigHelper.getBallotsPath() + " geschrieben");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}