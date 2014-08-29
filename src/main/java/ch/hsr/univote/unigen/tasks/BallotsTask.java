/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.Ballot;
import ch.bfh.univote.common.Ballots;
import ch.bfh.univote.common.Candidate;
import ch.bfh.univote.common.Choice;
import ch.bfh.univote.common.EncryptedVote;
import ch.bfh.univote.common.PoliticalList;
import ch.bfh.univote.common.VoterSignature;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.helper.StringConcatenator;
import ch.hsr.univote.unigen.krypto.ElGamal;
import ch.hsr.univote.unigen.krypto.NIZKP;
import ch.hsr.univote.unigen.krypto.RSASignatureGenerator;
import ch.hsr.univote.unigen.krypto.SchnorrSignatureGenerator;
import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author Gian Poltéra
 */
public class BallotsTask {

    private ConfigHelper config;
    private ElectionBoard electionBoard;
    private KeyStore keyStore;

    public BallotsTask() {
        this.config = VoteGenerator.config;
        this.electionBoard = VoteGenerator.electionBoard;
        this.keyStore = VoteGenerator.keyStore;

        run();
    }

    private void run() {
        /*create Ballots*/
        Ballots ballots = createBallots();

        /*sign by ElectionManager*/
        ballots.setSignature(new RSASignatureGenerator().createSignature(ballots, keyStore.getEMSignatureKey()));

        /*submit to ElectionBoard*/
        electionBoard.setBallots(ballots);
    }

    private Ballots createBallots() {
        Ballots ballots = new Ballots();
        ballots.setElectionId(electionBoard.getElectionSystemInfo().getElectionId());

        /*for each Voter*/
        for (int i = 0; i < config.getVotersNumber(); i++) {
            Ballot ballot = createBallot(i);
            ballots.getBallot().add(ballot);
        }

        return ballots;
    }

    private Ballot createBallot(int i) {
        Ballot ballot = new Ballot();
        ballot.setElectionId(config.getElectionId());
        ballot.setVerificationKey(getAnonymousVerificationKey(i));
        ballot.setEncryptedVote(createEncryptedVote(i));
        ballot.setProof(new NIZKP().getBallotProof(ballot.getEncryptedVote().getFirstValue(), ballot.getVerificationKey(), electionBoard.getEncryptionParameters()));
        ballot.setSignature(createVoterSignature(i, ballot));
        
        return ballot;
    }

    private BigInteger getAnonymousVerificationKey(int i) {
        BigInteger signatureKey = keyStore.getVoterSignatureKey(i).getX();
        BigInteger g = electionBoard.getBlindedGenerator(electionBoard.mixers[electionBoard.mixers.length - 1]).getGenerator();
        BigInteger verificationKey = g.modPow(signatureKey,electionBoard.getSignatureParameters().getPrime());       
        
        return verificationKey;
    }
    
    private EncryptedVote createEncryptedVote(int i) {
        EncryptedVote encryptedVote = new EncryptedVote();
        BigInteger encodedVote = createEncodedVote(i);

        /*Encryption*/
        BigInteger[] encryption = new ElGamal().getEncryption(encodedVote, electionBoard.getEncryptionKey().getKey(), electionBoard.getEncryptionParameters());
        encryptedVote.setFirstValue(encryption[0]);
        encryptedVote.setSecondValue(encryption[1]);
        
        return encryptedVote;
    }

    private BigInteger createEncodedVote(int i) {
        Random generator = new Random();
        /*concatenate to cnln..c2c1li BitString*/
        StringConcatenator sc = new StringConcatenator();

        /*loop each choice and generate a vote*/
        for (int c = 0; c < electionBoard.getElectionOptions().getChoice().size(); c++) {
            Choice choice = electionBoard.getElectionOptions().getChoice().get(electionBoard.getElectionOptions().getChoice().size() - c - 1);
            if (choice instanceof PoliticalList) {
                PoliticalList politicalList = (PoliticalList) choice;
                electionBoard.addPoliticalList(politicalList);
                sc.pushObject(1);
            } else if (choice instanceof Candidate) {
                Candidate candidate = (Candidate) choice;
                electionBoard.addCandidate(candidate);
                int ramdonCount = generator.nextInt(config.getMaxCumulation());
                String maxBinCan = Integer.toBinaryString(config.getMaxCumulation());
                String binChoice = Integer.toBinaryString(ramdonCount);
                /*fill with 0 for correct BitString*/
                while (binChoice.length() < maxBinCan.length()) {
                    binChoice = "0" + binChoice;
                }
                sc.pushObject(binChoice);
            }
        }
        /*convert from BitString to decimal*/
        BigInteger encodedVote = new BigInteger(sc.pullAll(), 2);

        //     00  0  00 00 00 0
        //     --  -  -- -- -- -
        //     C4  L2 C3 C2 C1 L1
        //
        // cId 6  5  4  3  2  1
        System.out.println("EncodedeVote:");
        System.out.println(encodedVote);
        
        return encodedVote;
    }

    private VoterSignature createVoterSignature(int i, Ballot ballot) {
        VoterSignature voterSignature = new VoterSignature();
        BigInteger[] S = new SchnorrSignatureGenerator().createSignature(ballot, keyStore.getVoterSignatureKey(i));
        voterSignature.setFirstValue(S[0]);
        voterSignature.setSecondValue(S[1]);

        return voterSignature;
    }
}
