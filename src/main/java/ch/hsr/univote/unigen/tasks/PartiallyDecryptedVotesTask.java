/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.PartiallyDecryptedVotes;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.db.DB4O;
import ch.hsr.univote.unigen.helper.ConfigHelper;
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
            partiallyDecryptedVotes.setSignature(SignatureGenerator.createSignature(electionBoard.talliers[i], partiallyDecryptedVotes, keyStore.talliersPrivateKey[i]));
            
            /*add to List*/
            partiallyDecryptedVotesList[i] = partiallyDecryptedVotes;
        }
         /*submit to ElectionBoard*/
        electionBoard.setPartiallyDecryptedVotesList(partiallyDecryptedVotesList);
        
        /*save in db*/
        DB4O.storeDB(config.getElectionId(),partiallyDecryptedVotesList); 
    }
    
    private PartiallyDecryptedVotes createPartiallyDecryptedVotes(int i) {
        try {
        PartiallyDecryptedVotes partiallyDecryptedVotes = new PartiallyDecryptedVotes();
            partiallyDecryptedVotes.setElectionId(config.getElectionId());

            /*for each Vote*/
            for (int j = 0; j < electionBoard.mixedEncryptedVotes.getVote().size(); j++) {
                partiallyDecryptedVotes.getVote().add(ElGamal.getDecryption(
                        electionBoard.mixedEncryptedVotes.getVote().get(j).getFirstValue(),
                        electionBoard.mixedEncryptedVotes.getVote().get(j).getSecondValue(),
                        keyStore.talliersDecryptionKey[i],
                        electionBoard.encryptionParameters.getPrime()));
            }

            partiallyDecryptedVotes.setProof(NIZKP.getProof(
                    electionBoard.talliers[i], 
                    keyStore.talliersDecryptionKey[i], 
                    electionBoard.encryptionKeyShareList[i], 
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
