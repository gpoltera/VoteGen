package ch.hsr.univote.unigen;

import ch.hsr.univote.unigen.tasks.ElectionDefinitionTask;
import ch.hsr.univote.unigen.tasks.ElectionOptionsTask;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.Publisher;
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
import java.io.FileNotFoundException;
import java.security.cert.CertificateException;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xml.sax.SAXException;

/**
 * VoteGenerator
 *
 */
public class VoteGenerator {

    public static void main(String[] args) throws JAXBException, FileNotFoundException, SAXException, DatatypeConfigurationException, CertificateException, Exception {

        electionSequence();
        Publisher.main(args);
        System.out.println("WebService gestartet");

        //Runtime.getRuntime().exec(new String[]{"java","-jar","C:/NetBeans/VoteVerifier/target/VoteVerifier-1.1-SNAPSHOT-demo-jar-with-dependencies.jar"});
    }

    
    private static void electionSequence() throws Exception {    
        phase1();
        phase2();
        phase3();
        phase4();
        phase5();
        phase6();
        phase7();
        phase8();
    }
    
    /* 1.3.1 Public Parameters */
    private static void phase1() throws Exception {
        //Set the Signature Parameters
        SignatureParametersTask.run(); //Set the Schnorr Parameters -> OK
    }
    
    /* 1.3.2 Public Indentifiers and Keys */
    private static void phase2() throws Exception {
        //Generate the certificates and keys
        ElectionSystemInfoTask.run(); // -> OK
    }
    
    /* 1.3.3 Registration */
    private static void phase3() throws Exception {
        VoterCertsTask.run(); //Generate the voters certificates -> OK
    }

    /* 1.3.4 Election Setup */
    private static void phase4() throws Exception {
        //a) Initialization
        ElectionBoard.knownElectionIds.getElectionId().add(ConfigHelper.getElectionId()); //Add the election id
        
        //b) Election Definition 
        ElectionDefinitionTask.run(); // -> OK

        //c) Parameter Generation
        EncryptionParametersTask.run(); //Set the ElGamal Parameters -> OK

        //d) Distributed Key Generation
        EncryptionKeyTask.run(); // -> OK
        EncryptionKeyShareTask.run(); // -> OK

        //e) Constructing the Election Generator
        ElectionGeneratorTask.run(); // -> OK
        BlindedGeneratorTask.run(); // -> OK
    }
    
    /* 1.3.5 Election Preparation */
    private static void phase5() throws Exception {
        
        //a) Definition of Election Options
        ElectionOptionsTask.run(); //Set the ElectionOptions -> OK

        //b) Publication of Election Data
        ElectionDataTask.run(); //Add the results to the electionData -> OK

        //c) Electoral Roll Preparation       
        ElectoralRollTask.run(); //Lists the votersid -> OK

        //d) Mixing the Public Verification Keys
        MixedVerificationKeysByTask.run(); //NOT YET IMPLEMENTED
        MixedVerificationKeysTask.run(); //NOT YET IMPLEMENTED
    }
    
    /* 1.3.6 Election Period */
    private static void phase6() throws Exception {
        
        //a) Late Registration
        LatelyRegistredVoterCertsTask.run(); //NOT YET IMPLEMENTED
        LatelyMixedVerificationKeysByTask.run(); //NOT YET IMPLEMENTED
        LatelyMixedVerificationKeysTask.run(); //NOT YET IMPLEMENTED

        //c) Vote Creation and Casting
        BallotsTask.run(); //Create the ballots
        SingleBallotTask.run();
        EncryptedVotesTask.run();
    }
    
    /* 1.3.7 Mixing and Tallying */
    private static void phase7() throws Exception {
        
        //a) Mixing the Encryptions
        MixedEncryptedVotesByTask.run();

        //b) Decrypting the Votes
        PartiallyDecryptedVotesTask.run();
        DecryptedVotesTask.run();
        DecodedVotesTask.run();

        ElectionResultsTask.run(); //Performing the election results  
    }
    
    /* Fault Implementation */
    private static void phase8() throws Exception {
        //Implements the Faults
        FaultGeneratorTask.run();
    }
}
