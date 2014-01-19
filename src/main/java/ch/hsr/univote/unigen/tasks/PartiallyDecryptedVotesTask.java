/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.PartiallyDecryptedVotes;
import ch.hsr.univote.unigen.board.ElectionBoard;
import static ch.hsr.univote.unigen.board.ElectionBoard.partiallyDecryptedVotesList;
import static ch.hsr.univote.unigen.board.ElectionBoard.talliers;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.ElGamal;
import ch.hsr.univote.unigen.krypto.NIZKP;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;

/**
 *
 * @author Gian Poltéra
 */
public class PartiallyDecryptedVotesTask extends ElectionBoard {

    public static void run() throws Exception {
        for (int i = 0; i < talliers.length; i++) {
            PartiallyDecryptedVotes partiallyDecryptedVotes = new PartiallyDecryptedVotes();
            partiallyDecryptedVotes.setElectionId(ConfigHelper.getElectionId());

            for (int j = 0; j < mev.getVote().size(); j++) {
                partiallyDecryptedVotes.getVote().add(ElGamal.getDecryption(
                        mev.getVote().get(j).getFirstValue(),
                        mev.getVote().get(j).getSecondValue(),
                        talliersDecryptionKey[i],
                        encryptionParameters.getPrime()));
            }

            partiallyDecryptedVotes.setProof(NIZKP.getProof(
                    talliers[i], 
                    talliersDecryptionKey[i], 
                    encryptionKeyShareList[i], 
                    partiallyDecryptedVotes, 
                    encryptionParameters.getPrime(), 
                    encryptionParameters.getGroupOrder(),  
                    encryptionParameters.getGenerator()));
            partiallyDecryptedVotes.setSignature(SignatureGenerator.createSignature(talliers[i], partiallyDecryptedVotes, talliersPrivateKey[i]));
            partiallyDecryptedVotesList[i] = partiallyDecryptedVotes;
        }
    }
}
