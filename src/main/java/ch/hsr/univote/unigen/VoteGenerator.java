package ch.hsr.univote.unigen;

import ch.bfh.univote.common.KnownElectionIds;
import ch.hsr.univote.unigen.tasks.ElectionDefinitionTask;
import ch.hsr.univote.unigen.tasks.ElectionOptionsTask;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.board.Publisher;
import ch.hsr.univote.unigen.db.DBKeyStoreManager;
import ch.hsr.univote.unigen.gui.votegeneration.VoteGenerationPanel;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.tasks.BallotsTask;
import ch.hsr.univote.unigen.tasks.BlindedGeneratorTask;
import ch.hsr.univote.unigen.tasks.DecodedVotesTask;
import ch.hsr.univote.unigen.tasks.DecryptedVotesTask;
import ch.hsr.univote.unigen.tasks.ElectionDataTask;
import ch.hsr.univote.unigen.tasks.ElectionGeneratorTask;
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

    public static ConfigHelper config;
    public static ElectionBoard electionBoard;
    public static KeyStore keyStore;
    public VoteGenerationPanel voteGenerationPanel;

    public VoteGenerator(VoteGenerationPanel voteGenerationPanel) {
        config = new ConfigHelper();
        electionBoard = new ElectionBoard();
        keyStore = new KeyStore();
        this.voteGenerationPanel = voteGenerationPanel;
        electionSequence();
    }

    private void electionSequence() {
        phase1();
        //updateProgress();
        voteGenerationPanel.appendText("----------------------------------");

        phase2();
        //updateProgress();
        voteGenerationPanel.appendText("----------------------------------");

        phase3();
        //updateProgress();
        voteGenerationPanel.appendText("----------------------------------");

        phase4();
        //updateProgress();
        voteGenerationPanel.appendText("----------------------------------");

        phase5();
        //updateProgress();
        voteGenerationPanel.appendText("----------------------------------");

        phase6();
        //updateProgress();
        voteGenerationPanel.appendText("----------------------------------");

        phase7();
        //updateProgress();
        voteGenerationPanel.appendText("----------------------------------");

        phase8();
        //updateProgress();
        voteGenerationPanel.appendText("----------------------------------");

        phaseStore();
        voteGenerationPanel.appendText("----------------------------------");
        
        Publisher publisher = new Publisher(electionBoard);
        publisher.startWebSrv();
    }

    /* 1.3.1 Public Parameters */
    private void phase1() {
        voteGenerationPanel.appendText("1. Public Parameters");
        //Set the Signature Parameters
        voteGenerationPanel.appendText(" a) Set the Signature Parameters");
        try {
            new SignatureParametersTask();
            voteGenerationPanel.appendSuccess();
        } catch (Exception e) {
            voteGenerationPanel.appendFailure(e.getMessage().toString());
        }
    }

    /* 1.3.2 Public Identifiers and Keys */
    private void phase2() {
        voteGenerationPanel.appendText("2. Public Identifiers and Keys");
        //Generate the certificates and keys
        voteGenerationPanel.appendText(" a) Generate the certificates and keys");
        try {
            new ElectionSystemInfoTask();
            voteGenerationPanel.appendSuccess();
        } catch (Exception e) {
            voteGenerationPanel.appendFailure(e.getMessage().toString());
        }
    }

    /* 1.3.3 Registration */
    private void phase3() {
        voteGenerationPanel.appendText("3. Registration");
        //Generate the voters certificates
        voteGenerationPanel.appendText(" a) Generate the voters certificates and keys");
        try {
            new VoterCertsTask();
            voteGenerationPanel.appendSuccess();
        } catch (Exception e) {
            voteGenerationPanel.appendFailure(e.getMessage().toString());
        }
    }

    /* 1.3.4 Election Setup */
    private void phase4() {
        voteGenerationPanel.appendText("4. Election Setup");
        //a) Initialization
        voteGenerationPanel.appendText(" a) Initialization");

        KnownElectionIds knownElectionIds = new KnownElectionIds();
        knownElectionIds.getElectionId().add(config.getElectionId()); //Add the election id
        electionBoard.setKnownElectionIds(knownElectionIds);
        voteGenerationPanel.appendSuccess();

        //b) Election Definition
        voteGenerationPanel.appendText(" b) Election Definition");
        try {
            new ElectionDefinitionTask();
            voteGenerationPanel.appendSuccess();
        } catch (Exception e) {
            voteGenerationPanel.appendFailure(e.getMessage().toString());
        }

        //c) Parameter Generation
        voteGenerationPanel.appendText(" c) Parameter Generation");
        try {
            new EncryptionParametersTask();
            voteGenerationPanel.appendSuccess();
        } catch (Exception e) {
            voteGenerationPanel.appendFailure(e.getMessage().toString());
        } //Set the ElGamal Parameters -> OK

        //d) Distributed Key Generation
        voteGenerationPanel.appendText(" d) Distributed Key Generation");
        try {
            new EncryptionKeyShareTask();
            new EncryptionKeyTask();
            voteGenerationPanel.appendSuccess();
        } catch (Exception e) {
            voteGenerationPanel.appendFailure(e.getMessage().toString());
        } // OK

        //e) Constructing the Election Generator
        voteGenerationPanel.appendText(" e) Constructing the Election Generator");
        try {
            new BlindedGeneratorTask();
            new ElectionGeneratorTask();
            voteGenerationPanel.appendSuccess();
        } catch (Exception e) {
            voteGenerationPanel.appendFailure(e.getMessage().toString());
        } // OK
    }

    /* 1.3.5 Election Preparation */
    private void phase5() {
        voteGenerationPanel.appendText("5. Election Preparation");
        //a) Definition of Election Options
        voteGenerationPanel.appendText(" a) Definition of Election Options");
        try {
            new ElectionOptionsTask();
            voteGenerationPanel.appendSuccess();
        } catch (Exception e) {
            voteGenerationPanel.appendFailure(e.getMessage().toString());
        } //Set the ElectionOptions -> OK

        //b) Publication of Election Data
        voteGenerationPanel.appendText(" b) Publication of Election Data");
        try {
            new ElectionDataTask();
            voteGenerationPanel.appendSuccess();
        } catch (Exception e) {
            voteGenerationPanel.appendFailure(e.getMessage().toString());
        } //Add the results to the electionData -> OK

        //c) Electoral Roll Preparation 
        voteGenerationPanel.appendText(" c) Electoral Roll Preparation ");
        try {
            new ElectoralRollTask(); //??????
            voteGenerationPanel.appendSuccess();
        } catch (Exception e) {
            voteGenerationPanel.appendFailure(e.getMessage().toString());
        } //Lists the votersid -> OK

        //d) Mixing the Public Verification Keys
        voteGenerationPanel.appendText(" d) Mixing the Public Verification Keys");
        try {
            new MixedVerificationKeysByTask();
            new MixedVerificationKeysTask();
            voteGenerationPanel.appendSuccess();
        } catch (Exception e) {
            voteGenerationPanel.appendFailure(e.getMessage().toString());
        }
    }

    /* 1.3.6 Election Period */
    private void phase6() {
        voteGenerationPanel.appendText("6. Election Period");

        //a) Late Registration
        voteGenerationPanel.appendText(" a) Late Registration");
        try {
            new LatelyRegistredVoterCertsTask();
            new LatelyMixedVerificationKeysByTask();
            new LatelyMixedVerificationKeysTask();
            voteGenerationPanel.appendSuccess();
        } catch (Exception e) {
            voteGenerationPanel.appendFailure(e.getMessage().toString());
        }

        //c) Vote Creation and Casting
        voteGenerationPanel.appendText(" b) Vote Creation and Casting");
        try {
            new BallotsTask(); //Create the ballots
            voteGenerationPanel.appendSuccess();
        } catch (Exception e) {
            voteGenerationPanel.appendFailure(e.getMessage().toString());
        }
    }

    /* 1.3.7 Mixing and Tallying */
    private void phase7() {
        voteGenerationPanel.appendText("7. Mixing and Tallying");

        //a) Mixing the Encryptions
        voteGenerationPanel.appendText(" a) Mixing the Encryptions");
        try {
            new MixedEncryptedVotesByTask();
            new EncryptedVotesTask();
            voteGenerationPanel.appendSuccess();
        } catch (Exception e) {
            voteGenerationPanel.appendFailure(e.getMessage().toString());
        }

        //b) Decrypting the Votes
        voteGenerationPanel.appendText(" b) Decrypting the Votes");
        try {
            new PartiallyDecryptedVotesTask();
            new DecryptedVotesTask();
            new DecodedVotesTask();
            voteGenerationPanel.appendSuccess();
        } catch (Exception e) {
            voteGenerationPanel.appendFailure(e.getMessage().toString());
        }
    }

    /* Fault Implementation */
    private void phase8() {
        voteGenerationPanel.appendText("8. Fault Implementation");

        //Implements the Faults
        voteGenerationPanel.appendText(" a) Implements the Faults");
        try {
            new FaultGeneratorTask();
            voteGenerationPanel.appendSuccess();
        } catch (Exception e) {
            voteGenerationPanel.appendFailure(e.getMessage().toString());
        }
    }

    /* Store in DB */
    private void phaseStore() {
        voteGenerationPanel.appendText("Store the Keys in the DB");
        try {
            new DBKeyStoreManager().saveInDB(config.getElectionId());
            voteGenerationPanel.appendSuccess();
        } catch (Exception e) {
            voteGenerationPanel.appendFailure(e.getMessage().toString());
        }
    }
}
