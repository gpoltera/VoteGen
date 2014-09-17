/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.Ballot;
import ch.bfh.univote.common.EncryptedVote;
import ch.bfh.univote.common.MixedEncryptedVotes;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.crypto.ElGamal;
import ch.hsr.univote.unigen.crypto.NIZKP;
import ch.hsr.univote.unigen.crypto.PrimeGenerator;
import ch.hsr.univote.unigen.crypto.RSASignatureGenerator;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Gian Poltéra
 */
public class MixedEncryptedVotesByTask {

    private ConfigHelper config;
    private ElectionBoard electionBoard;
    private KeyStore keyStore;
    private List<EncryptedVote> previous_encryptedVotes;
    
    public MixedEncryptedVotesByTask() {
        this.config = VoteGenerator.config;
        this.electionBoard = VoteGenerator.electionBoard;
        this.keyStore = VoteGenerator.keyStore;
        this.previous_encryptedVotes = new ArrayList<>();
        
        run();
    }

    private void run() {
        Map<String, MixedEncryptedVotes> mixedEncryptedVotesList = new HashMap<>();
        
        /*load the EncryptedVotes*/
        previous_encryptedVotes = getEncryptedVotes();
        
        /*for each Mixer*/
        for (int k = 0; k < electionBoard.mixers.length; k++) {
            MixedEncryptedVotes mixedEncryptedVotes = createMixedEncryptedVotes(k);
            mixedEncryptedVotesList.put(electionBoard.mixers[k], mixedEncryptedVotes);
        }
        /*submit to ElectionBoard*/
        electionBoard.setEncryptedVotesMixedBy(mixedEncryptedVotesList);
    }

    private List<EncryptedVote> getEncryptedVotes() {
        List<EncryptedVote> encryptedVotes = new ArrayList<>();
        
        List<Ballot> ballots = electionBoard.getBallots().getBallot();
        for (Ballot ballot : ballots) {
            EncryptedVote encryptedVote = ballot.getEncryptedVote();
            encryptedVotes.add(encryptedVote);
        }
        
        return encryptedVotes;
    }
    
    private MixedEncryptedVotes createMixedEncryptedVotes(int k) {
        MixedEncryptedVotes mixedEncryptedVotes = new MixedEncryptedVotes();
        mixedEncryptedVotes.setElectionId(config.getElectionId());        
        BigInteger p = electionBoard.getEncryptionParameters().getPrime();
        BigInteger q = electionBoard.getEncryptionParameters().getGroupOrder();
        List<EncryptedVote> new_encryptedVotes = new ArrayList<>();
        
        //Shuffle
        Collections.shuffle(previous_encryptedVotes, new SecureRandom());
            
        for (EncryptedVote encryptedVote : previous_encryptedVotes) {
            BigInteger[] encryption = new BigInteger[2];
            
            encryption[0] = encryptedVote.getFirstValue();
            encryption[1] = encryptedVote.getSecondValue();   
            
            //ReEncryption
            BigInteger r = new PrimeGenerator().getPrime(q.bitLength() - 1);
            BigInteger[] reEncryption = new ElGamal().getEncryption(BigInteger.ONE, electionBoard.getEncryptionKey().getKey(),r,electionBoard.getEncryptionParameters());

            EncryptedVote new_encryptedVote = new EncryptedVote();
            new_encryptedVote.setFirstValue(reEncryption[0].multiply(encryption[0]).mod(p));
            new_encryptedVote.setSecondValue(reEncryption[1].multiply(encryption[1]).mod(p));
            mixedEncryptedVotes.getVote().add(new_encryptedVote);
            new_encryptedVotes.add(new_encryptedVote);
        }
        previous_encryptedVotes = new_encryptedVotes;
        
        //Proof is not yet implemented
        mixedEncryptedVotes.setProof(new NIZKP().getMixedEncryptedVotesProof(electionBoard.mixers[k]));
        mixedEncryptedVotes.setSignature(new RSASignatureGenerator().createSignature(electionBoard.mixers[k], mixedEncryptedVotes, keyStore.getMixerSignatureKey(k)));

        return mixedEncryptedVotes;
    }   
}
