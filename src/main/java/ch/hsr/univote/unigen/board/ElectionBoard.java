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
import ch.hsr.univote.unigen.VoteGenerator;
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

    private static ConfigHelper config;
    private String electionId;
    public static String[] mixers;
    public static String[] talliers;

    /*variables initialisation*/
    private static Ballots ballots;
    private static BlindedGenerator blindedGenerator = new BlindedGenerator();
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
    private static EncryptionKey encryptionKey = new EncryptionKey();
    private static EncryptedVote encryptedVote = new EncryptedVote();
    private static KnownElectionIds knownElectionIds = new KnownElectionIds();
    private static List<Certificate> listCertificate = new ArrayList<>();
    private static List<PoliticalList> politicalLists = new ArrayList<>();
    private static List<Candidate> candidateList = new ArrayList<>();
    private static List<MixedVerificationKey> listMixedVerificationKey = new ArrayList<>();
    private static List<MixedVerificationKeys> listMixedVerificationKeys = new ArrayList<>();
    private static List<MixedVerificationKey> listLatelyMixedVerificationKey = new ArrayList<>();
    private static List<MixedVerificationKeys> listLatelyMixedVerificationKeys = new ArrayList<>();
    private static List<Certificate> listLatelyCertificate = new ArrayList<>();
    private static List<EncryptionKeyShare> encryptionKeyShareList = new ArrayList<>();
    private static List<MixedEncryptedVotes> mixedEncryptedVotesList = new ArrayList<>();
    private static List<PartiallyDecryptedVotes> partiallyDecryptedVotesList = new ArrayList<>();
    private static List<BlindedGenerator> blindedGeneratorsList = new ArrayList<>();
    private static MixedEncryptedVotes mixedEncryptedVotes = new MixedEncryptedVotes();
    private static PartiallyDecryptedVotes partiallyDecryptedVotes = new PartiallyDecryptedVotes();
    private static Signature signatures = new Signature();
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

    //OK
    public void setSignatureParameters(SignatureParameters signatureParameters) {
        ElectionBoard.signatureParameters = signatureParameters;
    }

    //OK
    public SignatureParameters getSignatureParameters() {
        return ElectionBoard.signatureParameters;
    }

    //Wird momentan nicht gesetzt!!!
    public void setRootCertificate(Certificate certificate) {
        ElectionBoard.certificate = certificate;
    }

    public Certificate getRootCertificate() {
        return ElectionBoard.certificate;
    }

    //OK
    public void setKnownElectionIds(KnownElectionIds knownElectionIds) {
        ElectionBoard.knownElectionIds = knownElectionIds;
    }

    public KnownElectionIds getKnownElectionIds() {
        return ElectionBoard.knownElectionIds;
    }

    //OK
    public void setElectionSystemInfo(ElectionSystemInfo electionSystemInfo) {
        ElectionBoard.electionSystemInfo = electionSystemInfo;
    }

    public ElectionSystemInfo getElectionSystemInfo() {
        return ElectionBoard.electionSystemInfo;
    }

    //OK
    public void setElectionDefinition(ElectionDefinition electionDefinition) {
        ElectionBoard.electionDefinition = electionDefinition;
    }

    public ElectionDefinition getElectionDefinition() {
        return ElectionBoard.electionDefinition;
    }

    //OK
    public void setEncryptionParameters(EncryptionParameters encryptionParameters) {
        ElectionBoard.encryptionParameters = encryptionParameters;
    }

    public EncryptionParameters getEncryptionParameters() {
        return ElectionBoard.encryptionParameters;
    }

    //OK evtl noch anpassen
    public void setEncryptionKeyShareList(List<EncryptionKeyShare> encryptionKeyShareList) {
        ElectionBoard.encryptionKeyShareList = encryptionKeyShareList;
    }

    public EncryptionKeyShare getEncryptionKeyShare(String tallierId) {
        EncryptionKeyShare encryptionKeyShare = new EncryptionKeyShare();
        for (int i = 0; i < talliers.length; i++) {
            if (talliers[i].equals(tallierId)) {
                encryptionKeyShare = this.encryptionKeyShareList.get(i);
            }
        }

        return encryptionKeyShare;
    }

    //OK
    public void setEncryptionKey(EncryptionKey encryptionKey) {
        ElectionBoard.encryptionKey = encryptionKey;
    }

    public EncryptionKey getEncryptionKey() {
        return ElectionBoard.encryptionKey;
    }

    //OK evtl noch anpassen
    public void setBlindedGeneratorList(List<BlindedGenerator> blindedGeneratorsList) {
        ElectionBoard.blindedGeneratorsList = blindedGeneratorsList;
    }

    public BlindedGenerator getBlindedGenerator(String mixerId) {
        BlindedGenerator blindedGenerator = new BlindedGenerator();
        for (int i = 0; i < mixers.length; i++) {
            if (mixers[i].equals(mixerId)) {
                blindedGenerator = this.blindedGeneratorsList.get(i);
            }
        }

        return blindedGenerator;
    }

    //OK
    public void setElectionGenerator(ElectionGenerator electionGenerator) {
        ElectionBoard.electionGenerator = electionGenerator;
    }

    public ElectionGenerator getElectionGenerator() {
        return ElectionBoard.electionGenerator;
    }

    //OK
    public void setElectionOptions(ElectionOptions electionOptions) {
        ElectionBoard.electionOptions = electionOptions;
    }

    public ElectionOptions getElectionOptions() {
        return ElectionBoard.electionOptions;
    }

    //OK
    public void setElectionData(ElectionData electionData) {
        ElectionBoard.electionData = electionData;
    }

    public ElectionData getElectionData() {
        return ElectionBoard.electionData;
    }

    //OK
    public void setElectoralRoll(ElectoralRoll electoralRoll) {
        ElectionBoard.electoralRoll = electoralRoll;
    }

    public ElectoralRoll getElectoralRoll() {
        return ElectionBoard.electoralRoll;
    }

    //OK
    public void setVoterCertificates(VoterCertificates voterCertificates) {
        ElectionBoard.voterCertificates = voterCertificates;
    }

    public VoterCertificates getVoterCertificates() {
        return ElectionBoard.voterCertificates;
    }

    //OK evtl. noch anpassen -> Noch verwechslungen drin
    public void setVerificationKeysMixedBy(List<MixedVerificationKeys> listMixedVerificationKeys) {
        ElectionBoard.listMixedVerificationKeys = listMixedVerificationKeys;
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

    // OK
    public void setMixedVerificationKeys(VerificationKeys verificationKeys) {
        ElectionBoard.verificationKeys = verificationKeys;
    }

    public VerificationKeys getMixedVerificationKeys() {
        return ElectionBoard.verificationKeys;
    }

    //OK
    public void setLatelyRegisteredVoterCertificates(List<Certificate> listCertificate) {
        ElectionBoard.listLatelyCertificate = listCertificate;
    }

    public List<Certificate> getLatelyRegisteredVoterCertificates() {
        return ElectionBoard.listLatelyCertificate;
    }

    public void setVerificationKeysLatelyMixedBy(List<MixedVerificationKeys> listMixedVerificationKeys) {
        ElectionBoard.listLatelyMixedVerificationKeys = listMixedVerificationKeys;
    }

    public List<MixedVerificationKey> getVerificationKeysLatelyMixedBy(String mixerId) {
        List<MixedVerificationKey> mixedVerificationKeys = new ArrayList<MixedVerificationKey>();

        for (int i = 0; i < mixers.length; i++) {
            if (mixers[i].equals(mixerId)) {
                mixedVerificationKeys.add(this.listLatelyMixedVerificationKey.get(i));
                break;
            }
        }

        return mixedVerificationKeys;
    }

    // OK
    public void setLatelyMixedVerificationKeys(List<MixedVerificationKey> mixedVerificationKeys) {
        ElectionBoard.listLatelyMixedVerificationKey = mixedVerificationKeys;
    }
        
    public List<MixedVerificationKey> getLatelyMixedVerificationKeys() {
        return ElectionBoard.listLatelyMixedVerificationKey;
    }



    //OK
    public void setBallots(Ballots ballots) {
        ElectionBoard.ballots = ballots;
    }

    public Ballots getBallots() {
        return ElectionBoard.ballots;
    }

    //OK
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

    //OK
    public void setEncryptedVotesMixedBy(List<MixedEncryptedVotes> mixedEncryptedVotesList) {
        ElectionBoard.mixedEncryptedVotesList = mixedEncryptedVotesList;
    }

    public MixedEncryptedVotes getEncryptedVotesMixedBy(String mixerId) {
        MixedEncryptedVotes mixedEncryptedVotes = new MixedEncryptedVotes();
        for (int i = 0; i < mixers.length; i++) {
            if (mixers[i].equals(mixerId)) {
                mixedEncryptedVotes = ElectionBoard.mixedEncryptedVotesList.get(i);
            }
        }

        return mixedEncryptedVotes;
    }

    // OK
    public void setEncryptedVotes(EncryptedVotes encryptedVotes) {
        ElectionBoard.encryptedVotes = encryptedVotes;
    }

    public EncryptedVotes getEncryptedVotes() {
        return ElectionBoard.encryptedVotes;
    }

    // OK
    public void setPartiallyDecryptedVotesList(List<PartiallyDecryptedVotes> partiallyDecryptedVotesList) {
        ElectionBoard.partiallyDecryptedVotesList = partiallyDecryptedVotesList;
    }

    public PartiallyDecryptedVotes getPartiallyDecryptedVotes(String tallierId) {
        PartiallyDecryptedVotes partiallyDecryptedVotes = new PartiallyDecryptedVotes();
        for (int i = 0; i < talliers.length; i++) {
            if (talliers[i].equals(tallierId)) {
                partiallyDecryptedVotes = ElectionBoard.partiallyDecryptedVotesList.get(i);
            }
        }

        return partiallyDecryptedVotes;
    }

    // OK
    public void setDecryptedVotes(DecryptedVotes decryptedVotes) {
        ElectionBoard.decryptedVotes = decryptedVotes;
    }

    public DecryptedVotes getDecryptedVotes() {
        return ElectionBoard.decryptedVotes;
    }

    // OK
    public void setDecodedVotes(DecodedVotes decodedVotes) {
        ElectionBoard.decodedVotes = decodedVotes;
    }

    public DecodedVotes getDecodedVotes() {
        return ElectionBoard.decodedVotes;
    }

    public void addPoliticalList(PoliticalList politicalList) {
        ElectionBoard.politicalLists.add(politicalList);
    }

    public void addCandidate(Candidate candidate) {
        ElectionBoard.candidateList.add(candidate);
    }
}
