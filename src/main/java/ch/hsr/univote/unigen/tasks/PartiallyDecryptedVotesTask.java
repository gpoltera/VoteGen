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
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Gian Poltéra
 */
public class PartiallyDecryptedVotesTask extends VoteGenerator {

    public void run() throws Exception {
        PartiallyDecryptedVotes[] partiallyDecryptedVotesList = new PartiallyDecryptedVotes[electionBoard.talliers.length];
        
        /*for each Tallier*/
        for (int i = 0; i < electionBoard.talliers.length; i++) {
            /*create PartiallyDecryptedVotes*/
            PartiallyDecryptedVotes partiallyDecryptedVotes = createPartiallyDecryptedVotes(i);
        
            /*sign by Tallier*/
            partiallyDecryptedVotes.setSignature(new SignatureGenerator().createSignature(electionBoard.talliers[i], partiallyDecryptedVotes, keyStore.getTallierPrivateKey(i)));
            
            /*add to List*/
            partiallyDecryptedVotesList[i] = partiallyDecryptedVotes;
        }
         /*submit to ElectionBoard*/
        electionBoard.setPartiallyDecryptedVotesList(partiallyDecryptedVotesList);
    }
    
    private PartiallyDecryptedVotes createPartiallyDecryptedVotes(int i) {
        try {
        PartiallyDecryptedVotes partiallyDecryptedVotes = new PartiallyDecryptedVotes();
            partiallyDecryptedVotes.setElectionId(config.getElectionId());

            /*for each Vote*/
            for (int j = 0; j < electionBoard.getMixedEncryptedVotes().getVote().size(); j++) {
                partiallyDecryptedVotes.getVote().add(ElGamal.getDecryption(
                        electionBoard.getMixedEncryptedVotes().getVote().get(j).getFirstValue(),
                        electionBoard.getMixedEncryptedVotes().getVote().get(j).getSecondValue(),
                        keyStore.getTallierDecryptionKey(i),
                        electionBoard.getEncryptionParameters().getPrime()));
            }

            partiallyDecryptedVotes.setProof(new NIZKP().getProof(
                    electionBoard.talliers[i], 
                    keyStore.getTallierDecryptionKey(i), 
                    electionBoard.getEncryptionKeyShare(i), 
                    partiallyDecryptedVotes, 
                    electionBoard.getEncryptionParameters().getPrime(), 
                    electionBoard.getEncryptionParameters().getGroupOrder(),  
                    electionBoard.getEncryptionParameters().getGenerator()));
        
        return partiallyDecryptedVotes;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
