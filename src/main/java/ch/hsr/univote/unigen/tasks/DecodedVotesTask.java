/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.Candidate;
import ch.bfh.univote.common.Choice;
import ch.bfh.univote.common.DecodedVote;
import ch.bfh.univote.common.DecodedVoteEntry;
import ch.bfh.univote.common.DecodedVotes;
import ch.bfh.univote.common.DecryptedVotes;
import ch.bfh.univote.common.ForallRule;
import ch.bfh.univote.common.PoliticalList;
import ch.bfh.univote.common.Rule;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.helper.StringConcatenator;
import ch.hsr.univote.unigen.krypto.RSASignatureGenerator;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Gian
 */
public class DecodedVotesTask {

    private ConfigHelper config;
    private ElectionBoard electionBoard;
    private KeyStore keyStore;

    public DecodedVotesTask() {
        this.config = VoteGenerator.config;
        this.electionBoard = VoteGenerator.electionBoard;
        this.keyStore = VoteGenerator.keyStore;

        run();
    }

    private void run() {
        /*create DecodedVotes*/
        DecodedVotes decodedVotes = createDecodedVotes();

        /*sign by ElectionManager*/
        decodedVotes.setSignature(new RSASignatureGenerator().createSignature(decodedVotes, keyStore.getEMSignatureKey()));

        /*submit to ElectionBoard*/
        electionBoard.setDecodedVotes(decodedVotes);
    }

    private DecodedVotes createDecodedVotes() {
        DecodedVotes decodedVotes = new DecodedVotes();

        DecryptedVotes decrytpedVotes = electionBoard.getDecryptedVotes();
        List<Choice> choices = electionBoard.getElectionData().getChoice();
        int maxBinCan = Integer.toBinaryString(config.getMaxCandidates()).length();
        //List<Rule> rules = electionBoard.getElectionData().getRule();
        int length = 0;
        Stack<String> typs = new Stack<>();
        for (Choice choice : choices) {
            if (choice instanceof PoliticalList) {
                length++;
                typs.push("PL");
            } else if (choice instanceof Candidate) {
                length = length + maxBinCan;
                typs.push("CD");
            }
        }
        //decode each vote
        for (BigInteger decrytpedVote : decrytpedVotes.getVote()) {
            //convert from decimal to BitString
            String bsVote = decrytpedVote.toString(2);
            //fill with 0 for correct BitString
            while (bsVote.length() < length) {
                bsVote = "0" + bsVote;
            }

            int iterator = length;
            int choiceId = 0;
            for (String typ : typs) {
                if (typ.equals("PL")) {
                    choiceId++;
                    String politicalList = bsVote.substring((iterator - 1), iterator);
                    if (Integer.parseInt(politicalList) >= 1) {
                        decodedVotes.getDecodedVote().add(createDecodedVote(choiceId, politicalList));
                    }
                    iterator--;
                } else if (typ.equals("CD")) {
                    choiceId++;
                    String candidate = bsVote.substring((iterator - maxBinCan), iterator);
                    if (Integer.parseInt(candidate) >= 1) {
                        decodedVotes.getDecodedVote().add(createDecodedVote(choiceId, candidate));
                    }
                    iterator = iterator - maxBinCan;
                }
            }
        }

        return decodedVotes;
    }

    private DecodedVote createDecodedVote(int choiceId, String count) {
        DecodedVote decodedVote = new DecodedVote();
        BigInteger dcount = new BigInteger(count, 2);

        decodedVote.getEntry().add(createDecodedVoteEntry(choiceId, dcount.intValue()));

        return decodedVote;
    }

    private DecodedVoteEntry createDecodedVoteEntry(int choiceId, int count) {
        DecodedVoteEntry decodedVoteEntry = new DecodedVoteEntry();

        decodedVoteEntry.setChoiceId(choiceId);
        decodedVoteEntry.setCount(count);

        return decodedVoteEntry;
    }
}
