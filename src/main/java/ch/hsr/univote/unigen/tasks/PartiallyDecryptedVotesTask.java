/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.EncryptedVote;
import ch.bfh.univote.common.EncryptedVotes;
import ch.bfh.univote.common.PartiallyDecryptedVotes;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.crypto.NIZKP;
import ch.hsr.univote.unigen.crypto.RSASignatureGenerator;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Gian Poltéra
 */
public class PartiallyDecryptedVotesTask {

    private ConfigHelper config;
    private ElectionBoard electionBoard;
    private KeyStore keyStore;

    public PartiallyDecryptedVotesTask() {
        this.config = VoteGenerator.config;
        this.electionBoard = VoteGenerator.electionBoard;
        this.keyStore = VoteGenerator.keyStore;

        run();
    }

    private void run() {
        Map<String,PartiallyDecryptedVotes> partiallyDecryptedVotesList = new HashMap<>();

        /*for each Tallier*/
        for (int j = 0; j < electionBoard.talliers.length; j++) {
            /*create PartiallyDecryptedVotes*/
            PartiallyDecryptedVotes partiallyDecryptedVotes = createPartiallyDecryptedVotes(j);

            /*add to List*/
            partiallyDecryptedVotesList.put(electionBoard.talliers[j], partiallyDecryptedVotes);
        }
        /*submit to ElectionBoard*/
        electionBoard.setPartiallyDecryptedVotesList(partiallyDecryptedVotesList);
    }

    private PartiallyDecryptedVotes createPartiallyDecryptedVotes(int j) {
        PartiallyDecryptedVotes partiallyDecryptedVotes = new PartiallyDecryptedVotes();
        partiallyDecryptedVotes.setElectionId(config.getElectionId());

        EncryptedVotes encryptedVotes = electionBoard.getEncryptedVotes();
        BigInteger p = electionBoard.getEncryptionParameters().getPrime();
        for (EncryptedVote encryptedVote : encryptedVotes.getVote()) {
            BigInteger a_i = encryptedVote.getFirstValue();
            BigInteger a_ij = a_i.modPow(keyStore.getTallierDecryptionKey(j).getX().negate(), p);
            partiallyDecryptedVotes.getVote().add(a_ij);
        }

        partiallyDecryptedVotes.setProof(new NIZKP().getPartiallyDecryptedVotesProof(
                electionBoard.talliers[j],
                encryptedVotes,
                partiallyDecryptedVotes,
                keyStore.getTallierDecryptionKey(j),
                keyStore.getTallierEncryptionKey(j)));

        /*sign by Tallier*/
        partiallyDecryptedVotes.setSignature(new RSASignatureGenerator().createSignature(electionBoard.talliers[j], partiallyDecryptedVotes, keyStore.getTallierSignatureKey(j)));

        return partiallyDecryptedVotes;
    }
}
