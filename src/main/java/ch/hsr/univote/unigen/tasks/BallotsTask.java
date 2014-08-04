/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.Ballot;
import ch.bfh.univote.common.Ballots;
import ch.bfh.univote.common.EncryptedVote;
import ch.bfh.univote.common.Proof;
import ch.bfh.univote.common.VoterSignature;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.db.DB4O;
import ch.hsr.univote.unigen.krypto.ElGamal;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class BallotsTask extends VoteGenerator {

    public void run() throws Exception {
        /*create Ballots*/
        Ballots ballots = createBallots();

        /*sign by ElectionManager*/
        ballots.setSignature(SignatureGenerator.createSignature(ballots, keyStore.electionManagerPrivateKey));

        /*submit to ElectionBoard*/
        electionBoard.setBallots(ballots);

        /*save in db*/
        DB4O.storeDB(config.getElectionId(), ballots);
    }

    private Ballots createBallots() {
        Ballots ballots = new Ballots();
        ballots.setElectionId(config.getElectionId());

        /*for each Voter*/
        for (int i = 0; i < config.getVotersNumber(); i++) {
            /*create Ballot*/
            Ballot ballot = createBallot(i);

            /*set proof*/
            Proof proof = new Proof();
            proof.getCommitment().add(BigInteger.TEN);
            proof.getResponse().add(BigInteger.TEN);
            ballot.setProof(proof);

            /*sign by voter*/
            VoterSignature voterSignature = new VoterSignature();
            voterSignature.setFirstValue(BigInteger.TEN);
            voterSignature.setSecondValue(BigInteger.TEN);
            ballot.setSignature(voterSignature);

            /*add to ballots*/
            ballots.getBallot().add(ballot);
        }

        return ballots;
    }

    private Ballot createBallot(int i) {
        Ballot ballot = new Ballot();
        ballot.setElectionId(config.getElectionId());

        /*Verification Key*/
        ballot.setVerificationKey(keyStore.votersVerificationKey[i]);

        /*Encryption*/
        BigInteger[] ecVote = ElGamal.getEncryption(
                BigInteger.TEN, //Encryption of ??
                electionBoard.getEncryptionKey().getKey(), //EncryptionKey
                electionBoard.getEncryptionParameters().getPrime(), //ELGamal p
                electionBoard.getEncryptionParameters().getGroupOrder(), //ElGamal q
                electionBoard.getEncryptionParameters().getGenerator()); //ElGamal g       

        /*EncryptedVote*/
        EncryptedVote encryptedVote = new EncryptedVote();

        encryptedVote.setFirstValue(ecVote[0]);
        encryptedVote.setSecondValue(ecVote[1]);
        ballot.setEncryptedVote(encryptedVote);

        return ballot;
    }
}
