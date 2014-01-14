/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator.prov;

import ch.bfh.univote.common.Ballot;
import ch.bfh.univote.common.Ballots;
import ch.bfh.univote.common.BlindedGenerator;
import ch.bfh.univote.common.Candidate;
import ch.bfh.univote.common.Certificate;
import ch.bfh.univote.common.DecodedVotes;
import ch.bfh.univote.common.DecryptedVotes;
import ch.bfh.univote.common.ElectionData;
import ch.bfh.univote.common.ElectionDefinition;
import ch.bfh.univote.common.ElectionGenerator;
import ch.bfh.univote.common.ElectionOptions;
import ch.bfh.univote.common.ElectionSystemInfo;
import ch.bfh.univote.common.ElectoralRoll;
import ch.bfh.univote.common.EncryptedVote;
import ch.bfh.univote.common.EncryptedVotes;
import ch.bfh.univote.common.EncryptionKey;
import ch.bfh.univote.common.EncryptionKeyShare;
import ch.bfh.univote.common.EncryptionParameters;
import ch.bfh.univote.common.KnownElectionIds;
import ch.bfh.univote.common.MixedEncryptedVotes;
import ch.bfh.univote.common.MixedVerificationKey;
import ch.bfh.univote.common.MixedVerificationKeys;
import ch.bfh.univote.common.PartiallyDecryptedVotes;
import ch.bfh.univote.common.PoliticalList;
import ch.bfh.univote.common.Signature;
import ch.bfh.univote.common.SignatureParameters;
import ch.bfh.univote.common.VerificationKeys;
import ch.bfh.univote.common.VoterCertificates;
import ch.hsr.univote.unigen.generator.BallotsTask;
import ch.hsr.univote.unigen.generator.BlindedGeneratorTask;
import ch.hsr.univote.unigen.generator.DecodedVotesTask;
import ch.hsr.univote.unigen.generator.DecryptedVotesTask;
import ch.hsr.univote.unigen.generator.ElectionDataTask;
import ch.hsr.univote.unigen.generator.ElectionDefinitionTask;
import ch.hsr.univote.unigen.generator.ElectionGeneratorTask;
import ch.hsr.univote.unigen.generator.ElectionOptionsTask;
import ch.hsr.univote.unigen.generator.ElectionResultsTask;
import ch.hsr.univote.unigen.generator.ElectionSystemInfoTask;
import ch.hsr.univote.unigen.generator.ElectoralRollTask;
import ch.hsr.univote.unigen.generator.EncryptedVotesTask;
import ch.hsr.univote.unigen.generator.EncryptionKeyShareTask;
import ch.hsr.univote.unigen.generator.EncryptionKeyTask;
import ch.hsr.univote.unigen.generator.EncryptionParametersTask;
import ch.hsr.univote.unigen.generator.LatelyMixedVerificationKeysByTask;
import ch.hsr.univote.unigen.generator.LatelyMixedVerificationKeysTask;
import ch.hsr.univote.unigen.generator.LatelyRegistredVoterCertsTask;
import ch.hsr.univote.unigen.generator.MixedEncryptedVotesByTask;
import ch.hsr.univote.unigen.generator.MixedVerificationKeysByTask;
import ch.hsr.univote.unigen.generator.MixedVerificationKeysTask;
import ch.hsr.univote.unigen.generator.PartiallyDecryptedVotesTask;
import ch.hsr.univote.unigen.generator.SignatureParametersTask;
import ch.hsr.univote.unigen.generator.SingleBallotTask;
import ch.hsr.univote.unigen.generator.VoterCertsTask;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.ElGamal;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian Poltéra
 */
public class WahlGenerator {

    public static SignatureParameters signatureParameters = new SignatureParameters();
    public static Certificate cert = new Certificate();
    public static ElectionSystemInfo esi = new ElectionSystemInfo();
    public static ElectionDefinition ed = new ElectionDefinition();
    public static EncryptionParameters encryptionParameters = new EncryptionParameters();
    public static EncryptionKeyShare eks = new EncryptionKeyShare();
    public static KnownElectionIds knownElectionIds = new KnownElectionIds();
    public static Ballots bts = new Ballots();
    public static Ballot bt = new Ballot();
    public static VoterCertificates vc = new VoterCertificates();
    public static Signature sg = new Signature();
    public static ElectionOptions eo = new ElectionOptions();
    public static ElectionGenerator eg = new ElectionGenerator();
    public static EncryptionKey ek = new EncryptionKey();
    public static BlindedGenerator bg = new BlindedGenerator();
    public static ElectionData edat = new ElectionData();
    public static ElectoralRoll er = new ElectoralRoll();
    public static MixedVerificationKeys mvk = new MixedVerificationKeys();
    public static VerificationKeys vk = new VerificationKeys();
    public static List<Certificate> lcert = new ArrayList<Certificate>();
    public static List<MixedVerificationKey> lmvk = new ArrayList<MixedVerificationKey>();
    public static MixedEncryptedVotes mev = new MixedEncryptedVotes();
    public static EncryptedVotes ev = new EncryptedVotes();
    public static PartiallyDecryptedVotes pdv = new PartiallyDecryptedVotes();
    public static DecryptedVotes dyv = new DecryptedVotes();
    public static DecodedVotes dov = new DecodedVotes();
    public static EncryptedVote ec = new EncryptedVote();
    public static String[] mixers = ConfigHelper.getMixerIds();
    public static String[] talliers = ConfigHelper.getTallierIds();
    public static BlindedGenerator[] blindedGeneratorsList = new BlindedGenerator[mixers.length];
    public static EncryptionKeyShare[] encryptionKeyShareList = new EncryptionKeyShare[talliers.length];
    public static MixedVerificationKeys[] mixedVerificationKeysList = new MixedVerificationKeys[mixers.length];
    public static MixedVerificationKey[] latelyMixedVerificationKeysList = new MixedVerificationKey[mixers.length];
    public static PartiallyDecryptedVotes[] partiallyDecryptedVotesList = new PartiallyDecryptedVotes[talliers.length];
    public static MixedEncryptedVotes[] mixedEncryptedVotesList = new MixedEncryptedVotes[mixers.length];

    public static KeyPair caKeyPair;
    public static RSAPrivateKey certificateAuthorityPrivateKey;
    public static RSAPublicKey certificateAuthorityPublicKey;

    public static RSAPrivateKey electionManagerPrivateKey;
    public static RSAPublicKey electionManagerPublicKey;

    public static RSAPrivateKey electionAdministratorPrivateKey;
    public static RSAPublicKey electionAdministratorPublicKey;

    public static RSAPrivateKey[] mixersPrivateKey = new RSAPrivateKey[mixers.length];
    public static RSAPublicKey[] mixersPublicKey = new RSAPublicKey[mixers.length];

    public static RSAPrivateKey[] talliersPrivateKey = new RSAPrivateKey[talliers.length];
    public static RSAPublicKey[] talliersPublicKey = new RSAPublicKey[talliers.length];

    public static RSAPrivateKey[] votersPrivateKey = new RSAPrivateKey[ConfigHelper.getVotersNumber()];
    public static RSAPublicKey[] votersPublicKey = new RSAPublicKey[ConfigHelper.getVotersNumber()];

    public static List<PoliticalList> politicalLists = new ArrayList<PoliticalList>();
    public static List<Candidate> candidateList = new ArrayList<Candidate>();

    public static BigInteger[] talliersDecryptionKey = new BigInteger[talliers.length];
    public static BigInteger[] talliersEncryptionKey = new BigInteger[talliers.length];

    public static BigInteger[] mixersSignatureKey = new BigInteger[mixers.length];
    public static BigInteger[] mixersVerificationKey = new BigInteger[mixers.length];
    public static BigInteger[] mixersGenerator = new BigInteger[mixers.length];

    public static void run() throws Exception {
            
        //Add the election id
        knownElectionIds.getElectionId().add(ConfigHelper.getElectionId());

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
        
        SignatureParametersTask.run(); //Set the Schnorr Parameters -> OK
        
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
        MixedVerificationKeysTask.run();
        MixedVerificationKeysByTask.run();
        
        /* 1.3.6 Election Period */
        //a) Late Registration
        LatelyRegistredVoterCertsTask.run(); //NOT YET IMPLEMENTED
        LatelyMixedVerificationKeysTask.run(); //NOT YET IMPLEMENTED
        LatelyMixedVerificationKeysByTask.run(); //NOT YET IMPLEMENTED

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
    }
}
