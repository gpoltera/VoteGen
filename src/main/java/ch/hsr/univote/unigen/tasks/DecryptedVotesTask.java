/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.DecryptedVotes;
import ch.bfh.univote.common.EncryptedVote;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian Poltéra
 */
public class DecryptedVotesTask {

    private ConfigHelper config;
    private ElectionBoard electionBoard;
    private KeyStore keyStore;

    public DecryptedVotesTask() {
        this.config = VoteGenerator.config;
        this.electionBoard = VoteGenerator.electionBoard;
        this.keyStore = VoteGenerator.keyStore;

        run();
    }

    private void run() {
        /*create DecryptedVotes*/
        DecryptedVotes decryptedVotes = createDecryptedVotes();

        /*sign by ElectionManager*/
        //decryptedVotes.setSignature(new RSASignatureGenerator().createSignature(decryptedVotes, keyStore.getEMSignatureKey()));
        /*submit to ElectionBoard*/
        electionBoard.setDecryptedVotes(decryptedVotes);
    }

    private DecryptedVotes createDecryptedVotes() {
        DecryptedVotes decryptedVotes = new DecryptedVotes();
        decryptedVotes.setElectionId(config.getElectionId());

        List<EncryptedVote> encryptedVotes = electionBoard.getEncryptedVotes().getVote();
        String[] talliers = electionBoard.talliers;

        BigInteger p = electionBoard.getEncryptionParameters().getPrime();
        BigInteger q = electionBoard.getEncryptionParameters().getGroupOrder();
        List<BigInteger> a = new ArrayList<>();
        List<BigInteger> b = new ArrayList<>();

        /*for each tallier*/
        BigInteger[][] a_ij = new BigInteger[encryptedVotes.size()][talliers.length];

        for (int j = 0; j < talliers.length; j++) {
            List<BigInteger> partiallyDecryptedVotes = electionBoard.getPartiallyDecryptedVotes(talliers[j]).getVote();
            for (int i = 0; i < partiallyDecryptedVotes.size(); i++) {
                a_ij[i][j] = partiallyDecryptedVotes.get(i);
            }
        }

        /*compute a*/
        for (int i = 0; i < a_ij.length; i++) {
            BigInteger multi = new BigInteger("1");
            for (int j = 0; j < a_ij[i].length; j++) {
                multi = multi.multiply(a_ij[i][j]).mod(p);
            }
            a.add(multi);
        }

        /*for each EncryptedVote*/
        for (EncryptedVote encryptedVote : encryptedVotes) {
            b.add(encryptedVote.getSecondValue());
        }

        /*Decryption*/
        for (int i = 0; i < a.size(); i++) {

            BigInteger m = b.get(i).multiply(a.get(i)).mod(p);
            if (m.compareTo(q) < 1) {
                m = m.subtract(BigInteger.ONE);
            } else {
                m = (p.subtract(m)).subtract(BigInteger.ONE);
            }

            decryptedVotes.getVote().add(m);
        }

        return decryptedVotes;
    }
}
