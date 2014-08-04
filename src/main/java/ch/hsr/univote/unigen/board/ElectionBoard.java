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
    ConfigHelper config = new ConfigHelper();
    
    public final String[] mixers = config.getMixerIds();
    public final String[] talliers = config.getTallierIds();
    
    SignatureParameters signatureParameters = new SignatureParameters();
    Certificate certificate = new Certificate();
    KnownElectionIds knownElectionIds = new KnownElectionIds();
    ElectionSystemInfo electionSystemInfo = new ElectionSystemInfo();
    ElectionDefinition electionDefinition = new ElectionDefinition();
    EncryptionParameters encryptionParameters = new EncryptionParameters();
    EncryptionKeyShare encryptionKeyShare = new EncryptionKeyShare();
    EncryptionKeyShare[] encryptionKeyShareList = new EncryptionKeyShare[talliers.length];
    EncryptionKey encryptionKey = new EncryptionKey();
    BlindedGenerator blindedGenerator = new BlindedGenerator();
    BlindedGenerator[] blindedGeneratorsList = new BlindedGenerator[mixers.length];
    ElectionGenerator electionGenerator = new ElectionGenerator();
    ElectionOptions electionOptions = new ElectionOptions();
    ElectionData electionData = new ElectionData();
    ElectoralRoll electoralRoll = new ElectoralRoll();
    VoterCertificates voterCertificates = new VoterCertificates();
    VerificationKeys verificationKeys = new VerificationKeys();
    
    List<Certificate> listCertificate = new ArrayList<>();
    PartiallyDecryptedVotes[] partiallyDecryptedVotesList = new PartiallyDecryptedVotes[talliers.length];
    List<MixedVerificationKey> listMixedVerificationKey = new ArrayList<>();
    Ballots ballots = new Ballots();
    EncryptedVotes encryptedVotes = new EncryptedVotes();
    PartiallyDecryptedVotes partiallyDecryptedVotes = new PartiallyDecryptedVotes();
    DecryptedVotes decryptedVotes = new DecryptedVotes();
    DecodedVotes decodedVotes = new DecodedVotes();
    MixedEncryptedVotes[] mixedEncryptedVotesList = new MixedEncryptedVotes[mixers.length];
    EncryptedVote encryptedVote = new EncryptedVote();
    Signature signatures = new Signature();
    MixedEncryptedVotes mixedEncryptedVotes = new MixedEncryptedVotes();
    List<PoliticalList> politicalLists = new ArrayList<>();
    List<Candidate> candidateList = new ArrayList<>();
    
    public void setSignatureParameters(SignatureParameters signatureParameters) {
        this.signatureParameters = signatureParameters;
    }
    
    public SignatureParameters getSignatureParameters() {
        return this.signatureParameters;
    }
    
    public void setRootCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    public void setKnownElectionIds(KnownElectionIds knownElectionIds) {
        this.knownElectionIds = knownElectionIds;
    }
    
    public void setElectionSystemInfo(ElectionSystemInfo electionSystemInfo) {
        this.electionSystemInfo = electionSystemInfo;
    }
    
    public void setElectionDefinition(ElectionDefinition electionDefinition) {
        this.electionDefinition = electionDefinition;
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
    
    public void setElectoralRoll(ElectoralRoll electoralRoll) {
        this.electoralRoll = electoralRoll;
    }
    
    public void setVoterCertificates(VoterCertificates voterCertificates) {
        this.voterCertificates = voterCertificates;
    }
    
    public void setVerificationKeysMixedBy() {
        
    }
    
    public static List<MixedVerificationKeys> listMixedVerificationKeys = new ArrayList<>();
    
    
    public void setMixedVerificationKeys(VerificationKeys verificationKeys) {
        this.verificationKeys = verificationKeys;
    }
    
    public void setLatelyRegisteredVoterCertificates(List<Certificate> listCertificate) {
        this.listCertificate = listCertificate;
    }
    
    public void setVerificationKeysLatelyMixedBy() {
        
    }
    
    public void setLatelyMixedVerificationKeys(List<MixedVerificationKey> listMixedVerificationKey) {
        this.listMixedVerificationKey = listMixedVerificationKey;
    }
    
    public void setBallots(Ballots ballots) {
        this.ballots = ballots;
    }

    public Ballots getBallots() {
        return this.ballots;    
    }
    
    public void setEncryptedVotesMixedBy(MixedEncryptedVotes[] mixedEncryptedVotesList) {
        this.mixedEncryptedVotesList = mixedEncryptedVotesList;
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
    
    public void setPartiallyDecryptedVotesList(PartiallyDecryptedVotes[] partiallyDecryptedVotesList) {
        this.partiallyDecryptedVotesList = partiallyDecryptedVotesList;
    }
    
    public void setDecryptedVotes(DecryptedVotes decryptedVotes) {
        this.decryptedVotes = decryptedVotes;
    }
    
    public void setDecodedVotes(DecodedVotes decodedVotes) {
        this.decodedVotes = decodedVotes;
    }
}
