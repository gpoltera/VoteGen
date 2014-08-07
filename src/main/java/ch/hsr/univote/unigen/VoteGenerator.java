package ch.hsr.univote.unigen;

import ch.bfh.univote.common.KnownElectionIds;
import ch.hsr.univote.unigen.tasks.ElectionDefinitionTask;
import ch.hsr.univote.unigen.tasks.ElectionOptionsTask;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.db.DBKeyStoreManager;
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
    public ConfigHelper config = new ConfigHelper();
    public ElectionBoard electionBoard = new ElectionBoard();
    public KeyStore keyStore = new KeyStore();
    
    public static void electionSequence() throws Exception { 
        VoteGenerator voteGenerator = new VoteGenerator();
        
        voteGenerator.phase1();
        updateProgress();
        appendText("----------------------------------");

        voteGenerator.phase2();
        updateProgress();
        appendText("----------------------------------");

        voteGenerator.phase3();
        updateProgress();
        appendText("----------------------------------");
                
        voteGenerator.phase4();
        updateProgress();
        appendText("----------------------------------");
        
        voteGenerator.phase5();
        updateProgress();
        appendText("----------------------------------");
        
        voteGenerator.phase6();
        updateProgress();
        appendText("----------------------------------");
        
        voteGenerator.phase7();
        updateProgress();
        appendText("----------------------------------");
        
        voteGenerator.phase8();
        updateProgress();
        appendText("----------------------------------");
        
        voteGenerator.phaseStore();
        appendText("----------------------------------");
    }

    /* 1.3.1 Public Parameters */
    private void phase1() throws Exception {
        appendText("1. Public Parameters");
        //Set the Signature Parameters
        appendText(" a) Set the Signature Parameters");
        new SignatureParametersTask().run(); //Set the Schnorr Parameters -> OK
    }

    /* 1.3.2 Public Indentifiers and Keys */
    private void phase2() throws Exception {
        appendText("2. Public Indentifiers and Keys");
        //Generate the certificates and keys
        appendText(" a) Generate the certificates and keys");
        new ElectionSystemInfoTask().run(); //-> OK
    }

    /* 1.3.3 Registration */
    private void phase3() throws Exception {
        appendText("3. Registration");
        //Generate the voters certificates
        appendText(" a) Generate the voters certificates");
        new VoterCertsTask().run();  // -> OK
    }

    /* 1.3.4 Election Setup */
    private void phase4() throws Exception {
        appendText("4. Election Setup");
        //a) Initialization
        appendText(" a) Initialization");
        KnownElectionIds knownElectionIds = new KnownElectionIds();
        knownElectionIds.getElectionId().add(config.getElectionId()); //Add the election id
        electionBoard.setKnownElectionIds(knownElectionIds);
        
        //b) Election Definition
        appendText(" b) Election Definition");
        new ElectionDefinitionTask().run(); // -> OK
        
        //c) Parameter Generation
        appendText(" c) Parameter Generation");
        new EncryptionParametersTask().run(); //Set the ElGamal Parameters -> OK
        
        //d) Distributed Key Generation
        appendText(" d) Distributed Key Generation");
        new EncryptionKeyShareTask().run(); // -> OK
        new EncryptionKeyTask().run(); // -> OK
        
        //e) Constructing the Election Generator
        appendText(" e) Constructing the Election Generator");
        new BlindedGeneratorTask().run(); // -> OK
        new ElectionGeneratorTask().run(); // -> OK   
    }

    /* 1.3.5 Election Preparation */
    private void phase5() throws Exception {
        appendText("5. Election Preparation");
        //a) Definition of Election Options
        appendText(" a) Definition of Election Options");
        ElectionOptionsTask electionOptionsTask = new ElectionOptionsTask();
        electionOptionsTask.run(); //Set the ElectionOptions -> OK

        //b) Publication of Election Data
        appendText(" b) Publication of Election Data");
        ElectionDataTask electionDataTask = new ElectionDataTask();
        electionDataTask.run(); //Add the results to the electionData -> OK

        //c) Electoral Roll Preparation 
        appendText(" c) Electoral Roll Preparation ");
        ElectoralRollTask electoralRollTask = new ElectoralRollTask();
        electoralRollTask.run(); //Lists the votersid -> OK

        //d) Mixing the Public Verification Keys
        appendText(" d) Mixing the Public Verification Keys");
        MixedVerificationKeysByTask mixedVerificationKeysByTask = new MixedVerificationKeysByTask();
        mixedVerificationKeysByTask.run(); //NOT YET IMPLEMENTED
        MixedVerificationKeysTask mixedVerificationKeysTask = new MixedVerificationKeysTask();
        mixedVerificationKeysTask.run(); //NOT YET IMPLEMENTED
    }

    /* 1.3.6 Election Period */
    private void phase6() throws Exception {
        appendText("6. Election Period");
        
        //a) Late Registration
        appendText(" a) Late Registration");
        LatelyRegistredVoterCertsTask latelyRegistredVoterCertsTask = new LatelyRegistredVoterCertsTask();
        latelyRegistredVoterCertsTask.run(); //Add the Certificates of the voters to the list -> OK
        LatelyMixedVerificationKeysByTask latelyMixedVerificationKeysByTask = new LatelyMixedVerificationKeysByTask();
        latelyMixedVerificationKeysByTask.run(); //NOT YET IMPLEMENTED
        LatelyMixedVerificationKeysTask latelyMixedVerificationKeysTask = new LatelyMixedVerificationKeysTask();
        latelyMixedVerificationKeysTask.run(); //NOT YET IMPLEMENTED

        //c) Vote Creation and Casting
        appendText(" b) Vote Creation and Casting");
        BallotsTask ballotsTask = new BallotsTask();
        ballotsTask.run(); //Create the ballots
        SingleBallotTask singleBallotTask = new SingleBallotTask();
        singleBallotTask.run();
        EncryptedVotesTask encryptedVotesTask = new EncryptedVotesTask();
        encryptedVotesTask.run();
    }

    /* 1.3.7 Mixing and Tallying */
    private void phase7() throws Exception {
        appendText("7. Mixing and Tallying");
        
        //a) Mixing the Encryptions
        appendText(" a) Mixing the Encryptions");
        MixedEncryptedVotesByTask mixedEncryptedVotesByTask = new MixedEncryptedVotesByTask();
        mixedEncryptedVotesByTask.run();

        //b) Decrypting the Votes
        appendText(" b) Decrypting the Votes");
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
        appendText("8. Fault Implementation");
        
        //Implements the Faults
        appendText(" a) Implements the Faults");
        //FaultGeneratorTask faultGeneratorTask = new FaultGeneratorTask();
        //faultGeneratorTask.run();
    }
    
    /* Store in DB */
    private void phaseStore() throws Exception {
        appendText("Store the Keys in the DB");
        DBKeyStoreManager dbksm = new DBKeyStoreManager();
        dbksm.saveInDB(config.getElectionId());
        appendText("Store the ElectionBoard in the DB");
    }
}
