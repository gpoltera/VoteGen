package ch.hsr.univote.unigen;

import ch.hsr.univote.unigen.tasks.ElectionDefinitionTask;
import ch.hsr.univote.unigen.tasks.ElectionOptionsTask;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import static ch.hsr.univote.unigen.gui.VoteGeneration.appendText;
import static ch.hsr.univote.unigen.gui.VoteGeneration.updateProgress;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.tasks.BallotsTask;
import ch.hsr.univote.unigen.tasks.BlindedGeneratorTask;
import ch.hsr.univote.unigen.tasks.DecodedVotesTask;
import ch.hsr.univote.unigen.tasks.DecryptedVotesTask;
import ch.hsr.univote.unigen.tasks.ElectionDataTask;
import ch.hsr.univote.unigen.tasks.ElectionGeneratorTask;
import ch.hsr.univote.unigen.tasks.ElectionResultsTask;
import ch.hsr.univote.unigen.tasks.ElectionSystemInfoTask;
import ch.hsr.univote.unigen.tasks.ElectoralRollTask;
import ch.hsr.univote.unigen.tasks.EncryptionKeyTask;
import ch.hsr.univote.unigen.tasks.EncryptionParametersTask;
import ch.hsr.univote.unigen.tasks.MixedVerificationKeysByTask;
import ch.hsr.univote.unigen.tasks.MixedVerificationKeysTask;
import ch.hsr.univote.unigen.tasks.PartiallyDecryptedVotesTask;
import ch.hsr.univote.unigen.tasks.SignatureParametersTask;
import ch.hsr.univote.unigen.tasks.SingleBallotTask;
import ch.hsr.univote.unigen.tasks.VoterCertsTask;
import ch.hsr.univote.unigen.tasks.EncryptedVotesTask;
import ch.hsr.univote.unigen.tasks.EncryptionKeyShareTask;
import ch.hsr.univote.unigen.tasks.FaultGeneratorTask;
import ch.hsr.univote.unigen.tasks.LatelyMixedVerificationKeysByTask;
import ch.hsr.univote.unigen.tasks.LatelyMixedVerificationKeysTask;
import ch.hsr.univote.unigen.tasks.LatelyRegistredVoterCertsTask;
import ch.hsr.univote.unigen.tasks.MixedEncryptedVotesByTask;



/**
 * VoteGenerator
 *
 */
public class VoteGenerator {
    public ElectionBoard electionBoard = new ElectionBoard();
    public KeyStore keyStore = new KeyStore();
    
    public static void electionSequence() throws Exception { 
        VoteGenerator voteGenerator = new VoteGenerator();
        
        voteGenerator.phase1();
        updateProgress();
        appendText("Phase 1 abgeschlossen");

        voteGenerator.phase2();
        updateProgress();
        appendText("Phase 2 abgeschlossen");

        voteGenerator.phase3();
        updateProgress();
        appendText("Phase 3 abgeschlossen");
                
        voteGenerator.phase4();
        updateProgress();
        appendText("Phase 4 abgeschlossen");
        
        voteGenerator.phase5();
        updateProgress();
        appendText("Phase 5 abgeschlossen");
        
        voteGenerator.phase6();
        updateProgress();
        appendText("Phase 6 abgeschlossen");
        
        voteGenerator.phase7();
        updateProgress();
        appendText("Phase 7 abgeschlossen");
        
        voteGenerator.phase8();
        updateProgress();
        appendText("Phase 8 abgeschlossen");
    }

    /* 1.3.1 Public Parameters */
    private void phase1() throws Exception {
        //Set the Signature Parameters
        SignatureParametersTask signatureParametersTask = new SignatureParametersTask();
        signatureParametersTask.run(); //Set the Schnorr Parameters -> OK
    }

    /* 1.3.2 Public Indentifiers and Keys */
    private void phase2() throws Exception {
        //Generate the certificates and keys
        ElectionSystemInfoTask electionSystemInfoTask = new ElectionSystemInfoTask();
        electionSystemInfoTask.run(); // -> OK
    }

    /* 1.3.3 Registration */
    private void phase3() throws Exception {
        //Generate the voters certificates
        VoterCertsTask voterCertsTask = new VoterCertsTask();
        voterCertsTask.run();  // -> OK
    }

    /* 1.3.4 Election Setup */
    private void phase4() throws Exception {
        //a) Initialization
        electionBoard.knownElectionIds.getElectionId().add(ConfigHelper.getElectionId()); //Add the election id

        //b) Election Definition
        ElectionDefinitionTask electionDefinitionTask = new ElectionDefinitionTask();
        electionDefinitionTask.run(); // -> OK

        //c) Parameter Generation
        EncryptionParametersTask encryptionParametersTask = new EncryptionParametersTask();
        encryptionParametersTask.run(); //Set the ElGamal Parameters -> OK

        //d) Distributed Key Generation
        EncryptionKeyTask encryptionKeyTask = new EncryptionKeyTask();
        encryptionKeyTask.run(); // -> OK
        EncryptionKeyShareTask encryptionKeyShareTask = new EncryptionKeyShareTask();
        encryptionKeyShareTask.run(); // -> OK

        //e) Constructing the Election Generator
        ElectionGeneratorTask electionGeneratorTask = new ElectionGeneratorTask();
        electionGeneratorTask.run(); // -> OK
        BlindedGeneratorTask blindedGeneratorTask = new BlindedGeneratorTask();
        blindedGeneratorTask.run(); // -> OK
    }

    /* 1.3.5 Election Preparation */
    private void phase5() throws Exception {

        //a) Definition of Election Options
        ElectionOptionsTask electionOptionsTask = new ElectionOptionsTask();
        electionOptionsTask.run(); //Set the ElectionOptions -> OK

        //b) Publication of Election Data
        ElectionDataTask electionDataTask = new ElectionDataTask();
        electionDataTask.run(); //Add the results to the electionData -> OK

        //c) Electoral Roll Preparation     
        ElectoralRollTask electoralRollTask = new ElectoralRollTask();
        electoralRollTask.run(); //Lists the votersid -> OK

        //d) Mixing the Public Verification Keys
        MixedVerificationKeysByTask mixedVerificationKeysByTask = new MixedVerificationKeysByTask();
        mixedVerificationKeysByTask.run(); //NOT YET IMPLEMENTED
        MixedVerificationKeysTask mixedVerificationKeysTask = new MixedVerificationKeysTask();
        mixedVerificationKeysTask.run(); //NOT YET IMPLEMENTED
    }

    /* 1.3.6 Election Period */
    private void phase6() throws Exception {

        //a) Late Registration
        LatelyRegistredVoterCertsTask latelyRegistredVoterCertsTask = new LatelyRegistredVoterCertsTask();
        latelyRegistredVoterCertsTask.run(); //NOT YET IMPLEMENTED
        LatelyMixedVerificationKeysByTask latelyMixedVerificationKeysByTask = new LatelyMixedVerificationKeysByTask();
        latelyMixedVerificationKeysByTask.run(); //NOT YET IMPLEMENTED
        LatelyMixedVerificationKeysTask latelyMixedVerificationKeysTask = new LatelyMixedVerificationKeysTask();
        latelyMixedVerificationKeysTask.run(); //NOT YET IMPLEMENTED

        //c) Vote Creation and Casting
        BallotsTask ballotsTask = new BallotsTask();
        ballotsTask.run(); //Create the ballots
        SingleBallotTask singleBallotTask = new SingleBallotTask();
        singleBallotTask.run();
        EncryptedVotesTask encryptedVotesTask = new EncryptedVotesTask();
        encryptedVotesTask.run();
    }

    /* 1.3.7 Mixing and Tallying */
    private void phase7() throws Exception {

        //a) Mixing the Encryptions
        MixedEncryptedVotesByTask mixedEncryptedVotesByTask = new MixedEncryptedVotesByTask();
        mixedEncryptedVotesByTask.run();

        //b) Decrypting the Votes
        PartiallyDecryptedVotesTask partiallyDecryptedVotesTask = new PartiallyDecryptedVotesTask();
        partiallyDecryptedVotesTask.run();
        DecryptedVotesTask decryptedVotesTask = new DecryptedVotesTask();
        decryptedVotesTask.run();
        DecodedVotesTask decodedVotesTask = new DecodedVotesTask();
        decodedVotesTask.run();

        ElectionResultsTask electionResultsTask = new ElectionResultsTask();
        electionResultsTask.run(); //Performing the election results  
    }

    /* Fault Implementation */
    private void phase8() throws Exception {
        //Implements the Faults
        FaultGeneratorTask faultGeneratorTask = new FaultGeneratorTask();
        faultGeneratorTask.run();
    }
}
