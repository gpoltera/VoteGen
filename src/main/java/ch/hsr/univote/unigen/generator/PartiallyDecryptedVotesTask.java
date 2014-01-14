/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.PartiallyDecryptedVotes;
import ch.bfh.univote.common.Proof;
import ch.hsr.univote.unigen.generator.prov.WahlGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.partiallyDecryptedVotesList;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.talliers;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.ElGamal;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class PartiallyDecryptedVotesTask extends WahlGenerator {

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

            // -> Proof
            partiallyDecryptedVotes.setSignature(SignatureGenerator.createSignature(talliers[i], partiallyDecryptedVotes, talliersPrivateKey[i]));
            partiallyDecryptedVotesList[i] = partiallyDecryptedVotes;
        }
    }
}
