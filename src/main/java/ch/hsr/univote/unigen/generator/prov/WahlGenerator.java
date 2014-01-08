/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator.prov;

import ch.bfh.univote.common.Ballot;
import ch.bfh.univote.common.Ballots;
import ch.bfh.univote.common.BlindedGenerator;
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
import java.math.BigInteger;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian Poltéra
 */
public class WahlGenerator {

    public static SignatureParameters signatureParameters = new SignatureParameters();
    public static Certificate cert = new Certificate();
    public static Certificate cert2 = new Certificate();
    public static ElectionSystemInfo esi = new ElectionSystemInfo();
    public static ElectionDefinition ed = new ElectionDefinition();
    public static EncryptionParameters ep = new EncryptionParameters();
    public static EncryptionKeyShare eks = new EncryptionKeyShare();
    public static KnownElectionIds kei = new KnownElectionIds();
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

    public static void run() throws Exception {
        sg.setSignerId("Gian Poltera");
        sg.setTimestamp(TimestampGenerator.generateTimestamp());
        sg.setValue(new BigInteger("545465465465423121245484546512154487485452"));
        
        kei.getElectionId().add(ConfigHelper.getElectionId());
        BallotsTask.run();
        BlindedGeneratorTask.run();
        DecodedVotesTask.run();
        DecryptedVotesTask.run();
        ElectionDataTask.run();
        ElectionDefinitionTask.run();
        ElectionGeneratorTask.run();
        ElectionOptionsTask.run();
        ElectionSystemInfoTask.run();
        ElectoralRollTask.run();
        EncryptedVotesTask.run();
        EncryptionKeyTask.run();
        EncryptionKeyShareTask.run();
        EncryptionParametersTask.run();
        LatelyMixedVerificationKeysTask.run();
        LatelyMixedVerificationKeysByTask.run();
        LatelyRegistredVoterCertsTask.run();
        MixedEncryptedVotesByTask.run();
        MixedVerificationKeysTask.run();
        MixedVerificationKeysByTask.run();
        PartiallyDecryptedVotesTask.run();
        SignatureParametersTask.run();    
        SingleBallotTask.run();      
        VoterCertsTask.run();
    }

    public static void addElectionDefinition(ElectionDefinition definition) {
        ed = definition;
    }

    public static void addElectionOptions(ElectionOptions options) {
        eo = options;
    }

    public static void addElectionRoll(ElectoralRoll roll) {
        er = roll;
    }
}
