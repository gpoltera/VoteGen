/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.board;

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
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian Poltéra
 */
public class ElectionBoard {

    public static SignatureParameters signatureParameters = new SignatureParameters();
    public static Certificate certificate = new Certificate();
    public static ElectionSystemInfo electionSystemInfo = new ElectionSystemInfo();
    public static ElectionDefinition electionDefinition = new ElectionDefinition();
    public static EncryptionParameters encryptionParameters = new EncryptionParameters();
    public static EncryptionKeyShare encryptionKeyShare = new EncryptionKeyShare();
    public static KnownElectionIds knownElectionIds = new KnownElectionIds();
    public static Ballots ballots = new Ballots();
    public static Ballot ballot = new Ballot();
    public static VoterCertificates voterCertificates = new VoterCertificates();
    public static Signature signatures = new Signature();
    public static ElectionOptions electionOptions = new ElectionOptions();
    public static ElectionGenerator electionGenerator = new ElectionGenerator();
    public static EncryptionKey encryptionKey = new EncryptionKey();
    public static BlindedGenerator blindedGenerator = new BlindedGenerator();
    public static ElectionData electionData = new ElectionData();
    public static ElectoralRoll electoralRoll = new ElectoralRoll();
    
    public static List<Certificate> listCertificate = new ArrayList<>();

    public static MixedEncryptedVotes mixedEncryptedVotes = new MixedEncryptedVotes();
    public static EncryptedVotes encryptedVotes = new EncryptedVotes();
    public static PartiallyDecryptedVotes partiallyDecryptedVotes = new PartiallyDecryptedVotes();
    public static DecryptedVotes decryptedVotes = new DecryptedVotes();
    public static DecodedVotes decodedVotes = new DecodedVotes();
    public static EncryptedVote encryptedVote = new EncryptedVote();
    public static final String[] mixers = ConfigHelper.getMixerIds();
    public static final String[] talliers = ConfigHelper.getTallierIds();

    public static List<PoliticalList> politicalLists = new ArrayList<>();
    public static List<Candidate> candidateList = new ArrayList<>();

    public static BlindedGenerator[] blindedGeneratorsList = new BlindedGenerator[mixers.length];
    public static EncryptionKeyShare[] encryptionKeyShareList = new EncryptionKeyShare[talliers.length];

    public static PartiallyDecryptedVotes[] partiallyDecryptedVotesList = new PartiallyDecryptedVotes[talliers.length];
    public static MixedEncryptedVotes[] mixedEncryptedVotesList = new MixedEncryptedVotes[mixers.length];

    public static VerificationKeys verificationKeys = new VerificationKeys();
    public static List<MixedVerificationKey> listMixedVerificationKey = new ArrayList<>();
    public static List<MixedVerificationKeys> listMixedVerificationKeys = new ArrayList<>();
}
