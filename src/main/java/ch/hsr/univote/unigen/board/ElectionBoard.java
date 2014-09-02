/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.board;

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
import ch.bfh.univote.common.EncryptedVotes;
import ch.bfh.univote.common.EncryptionKey;
import ch.bfh.univote.common.EncryptionKeyShare;
import ch.bfh.univote.common.EncryptionParameters;
import ch.bfh.univote.common.KnownElectionIds;
import ch.bfh.univote.common.MixedEncryptedVotes;
import ch.bfh.univote.common.MixedVerificationKey;
import ch.bfh.univote.common.MixedVerificationKeys;
import ch.bfh.univote.common.PartiallyDecryptedVotes;
import ch.bfh.univote.common.SignatureParameters;
import ch.bfh.univote.common.VerificationKeys;
import ch.bfh.univote.common.VoterCertificates;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Gian Poltéra
 */
public class ElectionBoard {

    private static ConfigHelper config;
    private String electionId;
    public static String[] mixers;
    public static String[] talliers;

    /*variables initialisation*/
    private static Ballots ballots;
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
    private static EncryptionKey encryptionKey = new EncryptionKey();
    private static KnownElectionIds knownElectionIds = new KnownElectionIds();
    private static Map<String, MixedVerificationKeys> listMixedVerificationKeys = new HashMap<>();
    private static List<MixedVerificationKey> listLatelyMixedVerificationKey = new ArrayList<>();
    private static Map<String, List> listLatelyMixedVerificationKeys = new HashMap<>();
    private static List<Certificate> listLatelyCertificate = new ArrayList<>();
    private static Map<String, EncryptionKeyShare> encryptionKeyShareList = new HashMap<>();
    private static Map<String, MixedEncryptedVotes> mixedEncryptedVotesList = new HashMap<>();
    private static Map<String, PartiallyDecryptedVotes> partiallyDecryptedVotesList = new HashMap<>();
    private static Map<String, BlindedGenerator> blindedGeneratorsList = new HashMap<>();
    private static SignatureParameters signatureParameters = new SignatureParameters();
    private static VoterCertificates voterCertificates = new VoterCertificates();
    private static VerificationKeys verificationKeys = new VerificationKeys();

    /*constructor*/
    public ElectionBoard() {
        this.config = VoteGenerator.config;
        electionId = config.getElectionId();
        talliers = config.getTallierIds();
        mixers = config.getMixerIds();
    }

    public void setSignatureParameters(SignatureParameters signatureParameters) {
        ElectionBoard.signatureParameters = signatureParameters;
    }

    public SignatureParameters getSignatureParameters() {
        return ElectionBoard.signatureParameters;
    }
    
    public void setRootCertificate(Certificate certificate) {
        ElectionBoard.certificate = certificate;
    }

    public Certificate getRootCertificate() {
        return ElectionBoard.certificate;
    }

    public void setKnownElectionIds(KnownElectionIds knownElectionIds) {
        ElectionBoard.knownElectionIds = knownElectionIds;
    }

    public KnownElectionIds getKnownElectionIds() {
        return ElectionBoard.knownElectionIds;
    }

    public void setElectionSystemInfo(ElectionSystemInfo electionSystemInfo) {
        ElectionBoard.electionSystemInfo = electionSystemInfo;
    }

    public ElectionSystemInfo getElectionSystemInfo() {
        return ElectionBoard.electionSystemInfo;
    }

    public void setElectionDefinition(ElectionDefinition electionDefinition) {
        ElectionBoard.electionDefinition = electionDefinition;
    }

    public ElectionDefinition getElectionDefinition() {
        return ElectionBoard.electionDefinition;
    }

    public void setEncryptionParameters(EncryptionParameters encryptionParameters) {
        ElectionBoard.encryptionParameters = encryptionParameters;
    }

    public EncryptionParameters getEncryptionParameters() {
        return ElectionBoard.encryptionParameters;
    }

    public void setEncryptionKeyShareList(Map<String, EncryptionKeyShare> encryptionKeyShareList) {
        ElectionBoard.encryptionKeyShareList = encryptionKeyShareList;
    }

    public EncryptionKeyShare getEncryptionKeyShare(String tallierId) {
        return ElectionBoard.encryptionKeyShareList.get(tallierId);
    }

    public void setEncryptionKey(EncryptionKey encryptionKey) {
        ElectionBoard.encryptionKey = encryptionKey;
    }

    public EncryptionKey getEncryptionKey() {
        return ElectionBoard.encryptionKey;
    }

    public void setBlindedGeneratorList(Map<String, BlindedGenerator> blindedGeneratorsList) {
        ElectionBoard.blindedGeneratorsList = blindedGeneratorsList;
    }

    public BlindedGenerator getBlindedGenerator(String mixerId) {
        return  ElectionBoard.blindedGeneratorsList.get(mixerId);
    }

    public void setElectionGenerator(ElectionGenerator electionGenerator) {
        ElectionBoard.electionGenerator = electionGenerator;
    }

    public ElectionGenerator getElectionGenerator() {
        return ElectionBoard.electionGenerator;
    }

    public void setElectionOptions(ElectionOptions electionOptions) {
        ElectionBoard.electionOptions = electionOptions;
    }

    public ElectionOptions getElectionOptions() {
        return ElectionBoard.electionOptions;
    }

    public void setElectionData(ElectionData electionData) {
        ElectionBoard.electionData = electionData;
    }

    public ElectionData getElectionData() {
        return ElectionBoard.electionData;
    }

    public void setElectoralRoll(ElectoralRoll electoralRoll) {
        ElectionBoard.electoralRoll = electoralRoll;
    }

    public ElectoralRoll getElectoralRoll() {
        return ElectionBoard.electoralRoll;
    }

    public void setVoterCertificates(VoterCertificates voterCertificates) {
        ElectionBoard.voterCertificates = voterCertificates;
    }

    public VoterCertificates getVoterCertificates() {
        return ElectionBoard.voterCertificates;
    }

    public void setVerificationKeysMixedBy(Map<String, MixedVerificationKeys> listMixedVerificationKeys) {
        ElectionBoard.listMixedVerificationKeys = listMixedVerificationKeys;
    }

    public MixedVerificationKeys getVerificationKeysMixedBy(String mixerId) {
        return ElectionBoard.listMixedVerificationKeys.get(mixerId);
    }

    public void setMixedVerificationKeys(VerificationKeys verificationKeys) {
        ElectionBoard.verificationKeys = verificationKeys;
    }

    public VerificationKeys getMixedVerificationKeys() {
        return ElectionBoard.verificationKeys;
    }

    public void setLatelyRegisteredVoterCertificates(List<Certificate> listCertificate) {
        ElectionBoard.listLatelyCertificate = listCertificate;
    }

    public List<Certificate> getLatelyRegisteredVoterCertificates() {
        return ElectionBoard.listLatelyCertificate;
    }

    public void setVerificationKeysLatelyMixedBy(Map<String, List> listMixedVerificationKeys) {
        ElectionBoard.listLatelyMixedVerificationKeys = listMixedVerificationKeys;
    }

    public List<MixedVerificationKey> getVerificationKeysLatelyMixedBy(String mixerId) {
        return ElectionBoard.listLatelyMixedVerificationKeys.get(mixerId);
    }

    public void setLatelyMixedVerificationKeys(List<MixedVerificationKey> mixedVerificationKeys) {
        ElectionBoard.listLatelyMixedVerificationKey = mixedVerificationKeys;
    }
        
    public List<MixedVerificationKey> getLatelyMixedVerificationKeys() {
        return ElectionBoard.listLatelyMixedVerificationKey;
    }

    public void setBallots(Ballots ballots) {
        ElectionBoard.ballots = ballots;
    }

    public Ballots getBallots() {
        return ElectionBoard.ballots;
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

    public void setEncryptedVotesMixedBy(Map<String, MixedEncryptedVotes> mixedEncryptedVotesList) {
        ElectionBoard.mixedEncryptedVotesList = mixedEncryptedVotesList;
    }

    public MixedEncryptedVotes getEncryptedVotesMixedBy(String mixerId) {
        return ElectionBoard.mixedEncryptedVotesList.get(mixerId);
    }

    public void setEncryptedVotes(EncryptedVotes encryptedVotes) {
        ElectionBoard.encryptedVotes = encryptedVotes;
    }

    public EncryptedVotes getEncryptedVotes() {
        return ElectionBoard.encryptedVotes;
    }

    public void setPartiallyDecryptedVotesList(Map<String, PartiallyDecryptedVotes> partiallyDecryptedVotesList) {
        ElectionBoard.partiallyDecryptedVotesList = partiallyDecryptedVotesList;
    }

    public PartiallyDecryptedVotes getPartiallyDecryptedVotes(String tallierId) {
        return ElectionBoard.partiallyDecryptedVotesList.get(tallierId);
    }

    public void setDecryptedVotes(DecryptedVotes decryptedVotes) {
        ElectionBoard.decryptedVotes = decryptedVotes;
    }

    public DecryptedVotes getDecryptedVotes() {
        return ElectionBoard.decryptedVotes;
    }

    public void setDecodedVotes(DecodedVotes decodedVotes) {
        ElectionBoard.decodedVotes = decodedVotes;
    }

    public DecodedVotes getDecodedVotes() {
        return ElectionBoard.decodedVotes;
    }
}
