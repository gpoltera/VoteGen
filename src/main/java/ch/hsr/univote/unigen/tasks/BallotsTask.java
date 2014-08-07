/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.Ballot;
import ch.bfh.univote.common.Ballots;
import ch.bfh.univote.common.Choice;
import ch.bfh.univote.common.EncryptedVote;
import ch.bfh.univote.common.Proof;
import ch.bfh.univote.common.VoterSignature;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.krypto.ElGamal;
import ch.hsr.univote.unigen.krypto.RSASignatureGenerator;
import ch.hsr.univote.unigen.krypto.SchnorrSignatureGenerator;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class BallotsTask extends VoteGenerator {

    public void run() throws Exception {
        /*create Ballots*/
        Ballots ballots = createBallots();

        /*sign by ElectionManager*/
        ballots.setSignature(new RSASignatureGenerator().createSignature(ballots, keyStore.getElectionManagerPrivateKey()));

        /*submit to ElectionBoard*/
        electionBoard.setBallots(ballots);
    }

    private Ballots createBallots() {
        Ballots ballots = new Ballots();
        ballots.setElectionId(electionBoard.getElectionSystemInfo().getElectionId());

        /*for each Voter*/
        for (int i = 0; i < config.getVotersNumber(); i++) {
            /*create ballot*/
            Ballot ballot = new Ballot();
            ballot.setElectionId(electionBoard.getElectionSystemInfo().getElectionId());
            
            /*encode vote*/
            Choice choice;
            
            /*represent encodedvote in Gq*/
            
            /*encrypt vote*/
            EncryptedVote encryptedVote = new EncryptedVote();
            encryptedVote.setFirstValue(BigInteger.TEN);
            encryptedVote.setSecondValue(BigInteger.TEN);
            ballot.setEncryptedVote(encryptedVote);
            
            
            /*compute anonymous verification key*/
            ballot.setVerificationKey(keyStore.getVoterVerificationKey(i).getVerificationKey());
            
            
            /*generate NIZKP*/
            Proof proof = new Proof();
            
            
            proof.getCommitment().add(BigInteger.TEN);
            proof.getResponse().add(BigInteger.TEN);
            ballot.setProof(proof);        
                    
                    
                    
                    
                    
                    
            /*generate signature*/
            
            /*add to the ballots*/
            ballots.getBallot().add(ballot);
            
            
            

            
            

            /*sign by voter*/
            VoterSignature voterSignature = new VoterSignature();
            BigInteger[] S = new SchnorrSignatureGenerator().createSignature(ballot, keyStore.getVoterSignatureKey(i));
            voterSignature.setFirstValue(S[0]);
            voterSignature.setSecondValue(S[1]);
            ballot.setSignature(voterSignature);

            /*add to ballots*/
            
        }

        return ballots;
    }

    private Ballot createBallot(int i) {
        
        
        
        
        /*Encode Vote*/
        
        /*Encrypt Vote*/
        ballot.setEncryptedVote(encryptVote(ballot, i));
        
        /*Verification Key*/
        ballot.setVerificationKey(keyStore.getVoterVerificationKey(i).getVerificationKey());



        return ballot;
    }

    
    
    private BigInteger encodeVote(Ballot ballot, int i) {
        
        int[] choiceIds = new int[2];
        
        //Choice ID ist ein Array
        
        String bitstring = "";
        
        // Sortierung nach id
        
       
        // Loop durch alle choices und hinzufügen von accroding bits
        for (int j = 0; j < choiceIds.length; j++) {
            int nbrBitsPerCandidate = Math.floor(Math.log(nbrVoicesPerCandidate)) / (Math.log(2)) + 1;
        
            
        }
        
        
        
        return null;
    }
    
    
    private EncryptedVote encryptVote(Ballot ballot, int i) {
        EncryptedVote encryptedVote = new EncryptedVote();

        /*Encryption*/
        BigInteger[] ecVote = ElGamal.getEncryption(
                BigInteger.TEN, //Encryption of ??
                electionBoard.getEncryptionKey().getKey(), //EncryptionKey
                electionBoard.getEncryptionParameters().getPrime(), //ELGamal p
                electionBoard.getEncryptionParameters().getGroupOrder(), //ElGamal q
                electionBoard.getEncryptionParameters().getGenerator()); //ElGamal g       

        encryptedVote.setFirstValue(ecVote[0]);
        encryptedVote.setSecondValue(ecVote[1]);

        return encryptedVote;
    }
}
