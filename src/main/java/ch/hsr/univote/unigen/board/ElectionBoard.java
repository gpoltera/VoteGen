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
import ch.hsr.univote.unigen.db.DB4O;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian Poltéra
 */
public class ElectionBoard {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
    private static Timestamp time = new Timestamp(System.currentTimeMillis());
    private static String filename = sdf.format(time);
    
    private ConfigHelper config;
    private String electionId;      
    public static String[] mixers;
    public static String[] talliers;

    /*variables initialisation*/
    private static Ballots ballots;
    private static BlindedGenerator blindedGenerator = new BlindedGenerator();
    private static BlindedGenerator[] blindedGeneratorsList;
    private static Certificate certificate = new Certificate();
    private static DecodedVotes decodedVotes = new DecodedVotes();
    private static DecryptedVotes decryptedVotes = new DecryptedVotes();
    private static ElectionSystemInfo electionSystemInfo = new ElectionSystemInfo();
    private static ElectionDefinition electionDefinition = new ElectionDefinition();
    private static ElectionGenerator electionGenerator = new ElectionGenerator();
    private static ElectionOptions electionOptions = new ElectionOptions();
    private static ElectionData electionData = new ElectionData();
    private static ElectoralRoll electoralRoll = new ElectoralRoll();
    private static EncryptedVotes encryptedVotes = new EncryptedVotes();
    private static EncryptionParameters encryptionParameters = new EncryptionParameters();
    private static EncryptionKeyShare encryptionKeyShare = new EncryptionKeyShare();
    private static EncryptionKeyShare[] encryptionKeyShareList;
    private static EncryptionKey encryptionKey = new EncryptionKey();
    private static EncryptedVote encryptedVote = new EncryptedVote();
    private static KnownElectionIds knownElectionIds = new KnownElectionIds();
    private static List<Certificate> listCertificate = new ArrayList<>();
    private static List<PoliticalList> politicalLists = new ArrayList<>();
    private static List<Candidate> candidateList = new ArrayList<>();
    private static List<MixedVerificationKey> listMixedVerificationKey = new ArrayList<>();
    private static MixedEncryptedVotes mixedEncryptedVotes = new MixedEncryptedVotes();
    private static MixedEncryptedVotes[] mixedEncryptedVotesList;
    private static PartiallyDecryptedVotes partiallyDecryptedVotes = new PartiallyDecryptedVotes();
    private static PartiallyDecryptedVotes[] partiallyDecryptedVotesList;
    private static Signature signatures = new Signature();
    private static VoterCertificates voterCertificates = new VoterCertificates();
    private static VerificationKeys verificationKeys = new VerificationKeys();

    /*constructor*/
    public ElectionBoard() {
        config = new ConfigHelper();
        electionId = config.getElectionId();
        talliers = config.getTallierIds();
        mixers = config.getMixerIds();
        blindedGeneratorsList = new BlindedGenerator[mixers.length];
        encryptionKeyShareList = new EncryptionKeyShare[talliers.length];
        mixedEncryptedVotesList = new MixedEncryptedVotes[mixers.length];
        partiallyDecryptedVotesList = new PartiallyDecryptedVotes[talliers.length];
    } 
    public void setSignatureParameters(SignatureParameters signatureParameters) {
        DB4O.storeDB(signatureParameters, electionId, filename);
    }

    public SignatureParameters getSignatureParameters() {
        SignatureParameters signatureParameters = new SignatureParameters();
        signatureParameters = (SignatureParameters) DB4O.readDB(signatureParameters, electionId, filename);        
        
        return signatureParameters;
    }

    public void setRootCertificate(Certificate certificate) {
        DB4O.storeDB(certificate, config.getElectionId(), filename);
    }

    public Certificate getRootCertificate() {
        Certificate certificate = new Certificate();
        certificate = (Certificate) DB4O.readDB(certificate, electionId, filename);
        
        return certificate;
    }

    public void setKnownElectionIds(KnownElectionIds knownElectionIds) {
        DB4O.storeDB(knownElectionIds, electionId, filename);
    }

    public KnownElectionIds getKnownElectionIds() {
        KnownElectionIds knownElectionIds = new KnownElectionIds();
        knownElectionIds = (KnownElectionIds) DB4O.readDB(knownElectionIds, electionId, filename);
        
        return knownElectionIds;
    }

    public void setElectionSystemInfo(ElectionSystemInfo electionSystemInfo) {
        this.electionSystemInfo = electionSystemInfo;
    }

    public ElectionSystemInfo getElectionSystemInfo() {
        return this.electionSystemInfo;
    }

    public void setElectionDefinition(ElectionDefinition electionDefinition) {
        this.electionDefinition = electionDefinition;
    }

    public ElectionDefinition getElectionDefinition() {
        return this.electionDefinition;
    }

    public void setEncryptionParameters(EncryptionParameters encryptionParameters) {
        this.encryptionParameters = encryptionParameters;
    }

    public EncryptionParameters getEncryptionParameters() {
        return this.encryptionParameters;
    }

    public void setEncryptionKeyShare(EncryptionKeyShare encryptionKeyShare) {
        this.encryptionKeyShare = encryptionKeyShare;
    }

    public void setEncryptionKeyShareList(EncryptionKeyShare[] encryptionKeyShareList) {
        this.encryptionKeyShareList = encryptionKeyShareList;
    }

    public EncryptionKeyShare getEncryptionKeyShare(String tallierId) {
        EncryptionKeyShare encryptionKeyShare = new EncryptionKeyShare();
        for (int i = 0; i < talliers.length; i++) {
            if (talliers[i].equals(tallierId)) {
                encryptionKeyShare = this.encryptionKeyShareList[i];
            }
        }

        return encryptionKeyShare;
    }

    public void setEncryptionKey(EncryptionKey encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public EncryptionKey getEncryptionKey() {
        return this.encryptionKey;
    }

    public void setBlindedGenerator(BlindedGenerator blindedGenerator) {
        this.blindedGenerator = blindedGenerator;
    }

    public void setBlindedGeneratorList(BlindedGenerator[] blindedGeneratorsList) {
        this.blindedGeneratorsList = blindedGeneratorsList;
    }

    public BlindedGenerator getBlindedGenerator(String mixerId) {
        BlindedGenerator blindedGenerator = new BlindedGenerator();
        for (int i = 0; i < mixers.length; i++) {
            if (mixers[i].equals(mixerId)) {
                blindedGenerator = this.blindedGeneratorsList[i];
            }
        }

        return blindedGenerator;
    }

    public void setElectionGenerator(ElectionGenerator electionGenerator) {
        this.electionGenerator = electionGenerator;
    }

    public ElectionGenerator getElectionGenerator() {
        return this.electionGenerator;
    }

    public void setElectionOptions(ElectionOptions electionOptions) {
        this.electionOptions = electionOptions;
    }

    public ElectionOptions getElectionOptions() {
        return this.electionOptions;
    }

    public void setElectionData(ElectionData electionData) {
        this.electionData = electionData;
    }

    public ElectionData getElectionData() {
        return this.electionData;
    }

    public void setElectoralRoll(ElectoralRoll electoralRoll) {
        this.electoralRoll = electoralRoll;
    }

    public ElectoralRoll getElectoralRoll() {
        return this.electoralRoll;
    }

    public void setVoterCertificates(VoterCertificates voterCertificates) {
        this.voterCertificates = voterCertificates;
    }

    public VoterCertificates getVoterCertificates() {
        return this.voterCertificates;
    }

    public void setVerificationKeysMixedBy() {

    }

    public MixedVerificationKeys getVerificationKeysMixedBy(String mixerId) {
        MixedVerificationKeys mixedVerificationKeys = new MixedVerificationKeys();

        for (int i = 0; i < mixers.length; i++) {
            if (mixers[i].equals(mixerId)) {
                mixedVerificationKeys = this.listMixedVerificationKeys.get(i);
                break;
            }
        }

        return mixedVerificationKeys;
    }

    public static List<MixedVerificationKeys> listMixedVerificationKeys = new ArrayList<>();

    public void setMixedVerificationKeys(VerificationKeys verificationKeys) {
        this.verificationKeys = verificationKeys;
    }

    public VerificationKeys getMixedVerificationKeys() {
        return this.verificationKeys;
    }

    public void setLatelyRegisteredVoterCertificates(List<Certificate> listCertificate) {
        this.listCertificate = listCertificate;
    }

    public List<Certificate> getLatelyRegisteredVoterCertificates() {
        return this.listCertificate;
    }

    public void setVerificationKeysLatelyMixedBy() {

    }

    public List<MixedVerificationKey> getVerificationKeysLatelyMixedBy(String mixerId) {
        List<MixedVerificationKey> mixedVerificationKeys = new ArrayList<MixedVerificationKey>();

        for (int i = 0; i < mixers.length; i++) {
            if (mixers[i].equals(mixerId)) {
                mixedVerificationKeys.add(this.listMixedVerificationKey.get(i));
                break;
            }
        }

        return mixedVerificationKeys;
    }

    public void setLatelyMixedVerificationKeys(List<MixedVerificationKey> listMixedVerificationKey) {
        this.listMixedVerificationKey = listMixedVerificationKey;
    }

    public List<MixedVerificationKey> getLatelyMixedVerificationKeys() {
        return this.listMixedVerificationKey;
    }

    public void setBallots(Ballots ballots) {
        this.ballots = ballots;
    }

    public Ballots getBallots() {
        return this.ballots;
    }

    public Ballot getBallot(BigInteger verificationKey) {
        Ballot vkballot = null;
        for (Ballot ballot : getBallots().getBallot()) {
            if (ballot.getVerificationKey().equals(verificationKey)) {
                vkballot = ballot;
                break;
            }
        }

        return vkballot;
    }

    public void setEncryptedVotesMixedBy(MixedEncryptedVotes[] mixedEncryptedVotesList) {
        this.mixedEncryptedVotesList = mixedEncryptedVotesList;
    }

    public MixedEncryptedVotes getEncryptedVotesMixedBy(String mixerId) {
        MixedEncryptedVotes mixedEncryptedVotes = new MixedEncryptedVotes();
        for (int i = 0; i < mixers.length; i++) {
            if (mixers[i].equals(mixerId)) {
                mixedEncryptedVotes = this.mixedEncryptedVotesList[i];
            }
        }

        return mixedEncryptedVotes;
    }

    public void setMixedEncryptedVotes(MixedEncryptedVotes mixedEncryptedVotes) {
        this.mixedEncryptedVotes = mixedEncryptedVotes;
    }

    public MixedEncryptedVotes getMixedEncryptedVotes() {
        return this.mixedEncryptedVotes;
    }

    public void setEncryptedVotes(EncryptedVotes encryptedVotes) {
        this.encryptedVotes = encryptedVotes;
    }

    public EncryptedVotes getEncryptedVotes() {
        return this.encryptedVotes;
    }

    public void setPartiallyDecryptedVotes(PartiallyDecryptedVotes partiallyDecryptedVotes) {
        this.partiallyDecryptedVotes = partiallyDecryptedVotes;
    }

    public PartiallyDecryptedVotes getPartiallyDecryptedVotes(String tallierId) {
        PartiallyDecryptedVotes partiallyDecryptedVotes = new PartiallyDecryptedVotes();
        for (int i = 0; i < talliers.length; i++) {
            if (talliers[i].equals(tallierId)) {
                partiallyDecryptedVotes = this.partiallyDecryptedVotesList[i];
            }
        }

        return partiallyDecryptedVotes;
    }

    public void setPartiallyDecryptedVotesList(PartiallyDecryptedVotes[] partiallyDecryptedVotesList) {
        this.partiallyDecryptedVotesList = partiallyDecryptedVotesList;
    }

    public void setDecryptedVotes(DecryptedVotes decryptedVotes) {
        this.decryptedVotes = decryptedVotes;
    }

    public DecryptedVotes getDecryptedVotes() {
        return this.decryptedVotes;
    }

    public void setDecodedVotes(DecodedVotes decodedVotes) {
        this.decodedVotes = decodedVotes;
    }

    public DecodedVotes getDecodedVotes() {
        return this.decodedVotes;
    }

    public void addPoliticalList(PoliticalList politicalList) {
        this.politicalLists.add(politicalList);
    }

    public void addCandidate(Candidate candidate) {
        this.candidateList.add(candidate);
    }
}
