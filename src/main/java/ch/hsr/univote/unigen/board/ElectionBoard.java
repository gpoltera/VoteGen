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
public class ElectionBoard {
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
    
    public static BigInteger[] votersSignatureKey = new BigInteger[ConfigHelper.getVotersNumber()];
    public static BigInteger[] votersVerificationKey = new BigInteger[ConfigHelper.getVotersNumber()];
}
