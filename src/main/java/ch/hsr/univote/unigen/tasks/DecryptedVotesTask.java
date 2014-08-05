/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.Candidate;
import ch.bfh.univote.common.Choice;
import ch.bfh.univote.common.DecryptedVotes;
import ch.bfh.univote.common.PoliticalList;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.helper.StringConcatenator;
import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author Gian Poltéra
 */
public class DecryptedVotesTask extends VoteGenerator {

    public void run() throws Exception {
        /*create DecryptedVotes*/
        DecryptedVotes decryptedVotes = createDecryptedVotes();
        
        /*sign by ElectionManager TBA*/
        //decryptedVotes.setSignature(SignatureGenerator.createSignature(decryptedVotes, keyStore.electionManagerPrivateKey));
        
        /*submit to ElectionBoard*/
        electionBoard.setDecryptedVotes(decryptedVotes);        
    }

    private DecryptedVotes createDecryptedVotes() {
        DecryptedVotes decryptedVotes = new DecryptedVotes();
        decryptedVotes.setElectionId(config.getElectionId());

        /*for each Voter*/
        for (int i = 0; i < config.getVotersNumber(); i++) {
            Random generator = new Random();
            /*concatenate to cnln..c2c1li BitString*/
            StringConcatenator sc = new StringConcatenator();

            /*loop each choice and generate a vote*/
            for (int j = 0; j < electionBoard.getElectionOptions().getChoice().size(); j++) {
                Choice choice = electionBoard.getElectionOptions().getChoice().get(electionBoard.getElectionOptions().getChoice().size() - j - 1);
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
            BigInteger vote = new BigInteger(sc.pullAll(), 2);

            decryptedVotes.getVote().add(vote);

            //     00  0  00 00 00 0
            //     --  -  -- -- -- -
            //     C4  L2 C3 C2 C1 L1
            //
            // cId 6  5  4  3  2  1                      
        }
        return decryptedVotes; 
    }
}
