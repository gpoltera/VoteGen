/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.PartiallyDecryptedVotes;
import ch.bfh.univote.common.Proof;
import ch.bfh.univote.common.Signature;
import ch.hsr.univote.unigen.generator.prov.TimestampGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.partiallyDecryptedVotesList;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.talliers;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.RSAGenerator;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;

/**
 *
 * @author Gian Poltéra
 */
public class PartiallyDecryptedVotesTask {

    public static void run() throws Exception {
        for (int i = 0; i < talliers.length; i++) {
            PartiallyDecryptedVotes partiallyDecryptedVotes = new PartiallyDecryptedVotes();
            partiallyDecryptedVotes.setElectionId(ConfigHelper.getElectionId());
            for (int j = 0; j < 100; j++) {
                partiallyDecryptedVotes.getVote().add(BigInteger.TEN);
            }
            Proof proof = new Proof();
            for (int j = 0; j < 100; j++) {
                proof.getCommitment().add(BigInteger.TEN);
            }
            proof.getResponse().add(BigInteger.TEN);
            partiallyDecryptedVotes.setProof(proof);
            Signature signature = new Signature();
            RSAPrivateKey privateKey = RSAGenerator.getPrivateKey();
            signature = SignatureGenerator.createSignature(partiallyDecryptedVotes, privateKey);
            signature.setSignerId(talliers[i]);
            signature.setTimestamp(TimestampGenerator.generateTimestamp());
            partiallyDecryptedVotes.setSignature(signature);
            partiallyDecryptedVotesList[i] = partiallyDecryptedVotes;
        }
    }
}
