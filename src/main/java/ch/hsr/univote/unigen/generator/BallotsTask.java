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
import ch.bfh.univote.common.VoterSignature;
import ch.hsr.univote.unigen.generator.prov.WahlGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.bts;
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
public class BallotsTask extends WahlGenerator{

    public static void run() throws Exception {

        bts.setElectionId(ConfigHelper.getElectionId());

        for (int i = 0; i < ConfigHelper.getVotersNumber(); i++) {
            // New Ballot
            Ballot bt = new Ballot();
            bt.setElectionId(ConfigHelper.getElectionId());

            // Verification Key
            bt.setVerificationKey(er.getVoterHash().get(i));

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
        bts.setSignature(SignatureGenerator.createSignature(bts, electionManagerPrivateKey));
    }
}