/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.EncryptedVote;
import ch.bfh.univote.common.Proof;
import ch.bfh.univote.common.VoterSignature;
import static ch.hsr.univote.unigen.board.ElectionBoard.bt;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class SingleBallotTask {

    public static void run() throws Exception {
        bt.setElectionId(ConfigHelper.getElectionId());
        bt.setVerificationKey(BigInteger.TEN);
        EncryptedVote encryptedVote = new EncryptedVote();
        encryptedVote.setFirstValue(BigInteger.TEN);
        encryptedVote.setSecondValue(BigInteger.TEN);
        bt.setEncryptedVote(encryptedVote);
        Proof proof = new Proof();
        proof.getCommitment().add(BigInteger.TEN);
        proof.getResponse().add(BigInteger.TEN);
        bt.setProof(proof);
        VoterSignature voterSignature = new VoterSignature();
        voterSignature.setFirstValue(BigInteger.TEN);
        voterSignature.setSecondValue(BigInteger.TEN);
        bt.setSignature(voterSignature);
    }
}
