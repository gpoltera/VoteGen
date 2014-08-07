/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.PartiallyDecryptedVotes;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.krypto.ElGamal;
import ch.hsr.univote.unigen.krypto.NIZKP;
import ch.hsr.univote.unigen.krypto.RSASignatureGenerator;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian Poltéra
 */
public class PartiallyDecryptedVotesTask extends VoteGenerator {

    public void run() {
        List<PartiallyDecryptedVotes> partiallyDecryptedVotesList = new ArrayList<>();

        /*for each Tallier*/
        for (int i = 0; i < electionBoard.talliers.length; i++) {
            /*create PartiallyDecryptedVotes*/
            PartiallyDecryptedVotes partiallyDecryptedVotes = createPartiallyDecryptedVotes(i);

            /*sign by Tallier*/
            partiallyDecryptedVotes.setSignature(new RSASignatureGenerator().createSignature(electionBoard.talliers[i], partiallyDecryptedVotes, keyStore.getTallierPrivateKey(i)));

            /*add to List*/
            partiallyDecryptedVotesList.add(i, partiallyDecryptedVotes);
        }
        /*submit to ElectionBoard*/
        electionBoard.setPartiallyDecryptedVotesList(partiallyDecryptedVotesList);
    }

    private PartiallyDecryptedVotes createPartiallyDecryptedVotes(int i) {
        PartiallyDecryptedVotes partiallyDecryptedVotes = new PartiallyDecryptedVotes();
        partiallyDecryptedVotes.setElectionId(config.getElectionId());

        /*for each Vote*/
        for (int j = 0; j < electionBoard.getMixedEncryptedVotes().getVote().size(); j++) {
            partiallyDecryptedVotes.getVote().add(new ElGamal().getDecryption(
                    electionBoard.getMixedEncryptedVotes().getVote().get(j).getFirstValue(),
                    electionBoard.getMixedEncryptedVotes().getVote().get(j).getSecondValue(),
                    keyStore.getTallierDecryptionKey(i),
                    electionBoard.getEncryptionParameters()));
        }

        partiallyDecryptedVotes.setProof(new NIZKP().getProof(
                electionBoard.talliers[i],
                keyStore.getTallierDecryptionKey(i),
                keyStore.getTallierEncryptionKey(i),
                electionBoard.getEncryptionParameters().getPrime(),
                electionBoard.getEncryptionParameters().getGroupOrder(),
                electionBoard.getEncryptionParameters().getGenerator()));

        return partiallyDecryptedVotes;
    }
}
