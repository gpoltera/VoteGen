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
 * Test
 *
 */
public class VoteGenerator {

    public static void main(String[] args) throws JAXBException, FileNotFoundException, SAXException, DatatypeConfigurationException, CertificateException, Exception {

        electionSequence();
        Publisher.main(args);
        System.out.println("WebService gestartet");
        
        //Runtime.getRuntime().exec(new String[]{"java","-jar","C:/NetBeans/VoteVerifier/target/VoteVerifier-1.1-SNAPSHOT-demo-jar-with-dependencies.jar"});
        Thread.sleep(60000);
        System.exit(0);
    }
    
    private static void electionSequence() throws Exception {
        //Add the election id
        ElectionBoard.knownElectionIds.getElectionId().add(ConfigHelper.getElectionId());
        
        //Set the Signature Parameters
        SignatureParametersTask.run(); //Set the Schnorr Parameters -> OK
        
        //Generate the certificates
        ElectionSystemInfoTask.run(); // -> OK

        /* 1.3.3 Registration */
        VoterCertsTask.run(); //Generate the voters certificates -> OK
                
        /* 1.3.4 Election Setup */
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
        
        /* 1.3.5 Election Preparation */
        //a) Definition of Election Options
        ElectionOptionsTask.run(); //Set the ElectionOptions -> OK
        
        //b) Publication of Election Data
        ElectionDataTask.run(); //Add the results to the electionData -> OK

        //c) Electoral Roll Preparation       
        ElectoralRollTask.run(); //Lists the votersid -> OK
        
        //d) Mixing the Public Verification Keys
        MixedVerificationKeysByTask.run(); //NOT YET IMPLEMENTED
        MixedVerificationKeysTask.run(); //NOT YET IMPLEMENTED
                
        /* 1.3.6 Election Period */
        //a) Late Registration
        LatelyRegistredVoterCertsTask.run(); //NOT YET IMPLEMENTED
        LatelyMixedVerificationKeysByTask.run(); //NOT YET IMPLEMENTED
        LatelyMixedVerificationKeysTask.run(); //NOT YET IMPLEMENTED
        
        //c) Vote Creation and Casting
        BallotsTask.run(); //Create the ballots
        SingleBallotTask.run();
        EncryptedVotesTask.run();
        
        /* 1.3.7 Mixing and Tallying */
        //a) Mixing the Encryptions
        MixedEncryptedVotesByTask.run();
        
        //b) Decrypting the Votes
        PartiallyDecryptedVotesTask.run();  
        DecryptedVotesTask.run();
        DecodedVotesTask.run();
        
        ElectionResultsTask.run(); //Performing the election results 
        
        //Implements the Faults
        FaultGeneratorTask.run();
    }
}
