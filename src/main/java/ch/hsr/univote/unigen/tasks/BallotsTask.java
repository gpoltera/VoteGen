/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.Ballot;
import ch.bfh.univote.common.EncryptedVote;
import ch.bfh.univote.common.Proof;
import ch.bfh.univote.common.VoterSignature;
import ch.hsr.univote.unigen.board.ElectionBoard;
import static ch.hsr.univote.unigen.board.ElectionBoard.bts;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.ElGamal;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class BallotsTask extends ElectionBoard{
   public static void run() throws Exception {

        bts.setElectionId(ConfigHelper.getElectionId());

        for (int i = 0; i < ConfigHelper.getVotersNumber(); i++) {
            // New Ballot
            Ballot bt = new Ballot();
            bt.setElectionId(ConfigHelper.getElectionId());

            // Verification Key
            bt.setVerificationKey(votersVerificationKey[i]);
            
            // Encryption
            BigInteger[] ecVote = ElGamal.getEncryption(
                    BigInteger.TEN, //Encryption of ??
                    ek.getKey(), //EncryptionKey
                    encryptionParameters.getPrime(), //ELGamal p
                    encryptionParameters.getGroupOrder(), //ElGamal q
                    encryptionParameters.getGenerator()); //ElGamal g       
            
            // EncryptedVote
            EncryptedVote ev = new EncryptedVote();
            
            ev.setFirstValue(ecVote[0]);
            ev.setSecondValue(ecVote[1]);
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