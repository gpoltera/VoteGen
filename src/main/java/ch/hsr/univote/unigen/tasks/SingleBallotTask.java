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
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.db.DB4O;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class SingleBallotTask extends VoteGenerator {

    public void run() throws Exception {
        /*create Ballot*/
        Ballot ballot = createBallot();
        
        /*set proof*/
        Proof proof = new Proof();
        proof.getCommitment().add(BigInteger.TEN);
        proof.getResponse().add(BigInteger.TEN);
        ballot.setProof(proof);
        
        /*sign by Voter*/
        VoterSignature voterSignature = new VoterSignature();
        voterSignature.setFirstValue(BigInteger.TEN);
        voterSignature.setSecondValue(BigInteger.TEN);
        ballot.setSignature(voterSignature);
        
        /*NOT IN USE???*/
        /*submit to ElectionBoard*/
        //electionBoard.ballot = ballot;
        
        /*save in db*/
        DB4O.storeDB(config.getElectionId(),ballot);
    }

    private Ballot createBallot() {
        Ballot ballot = new Ballot();
        ballot.setElectionId(config.getElectionId());
        ballot.setVerificationKey(BigInteger.TEN);
        EncryptedVote encryptedVote = new EncryptedVote();
        encryptedVote.setFirstValue(BigInteger.TEN);
        encryptedVote.setSecondValue(BigInteger.TEN);
        ballot.setEncryptedVote(encryptedVote);
        
        return ballot;
    }
}
