/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.db;

import ch.bfh.univote.common.Ballots;
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
import ch.bfh.univote.common.EncryptionParameters;
import ch.bfh.univote.common.KnownElectionIds;
import ch.bfh.univote.common.SignatureParameters;
import ch.bfh.univote.common.VerificationKeys;
import ch.bfh.univote.common.VoterCertificates;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.common.BlindedGeneratorsList;
import ch.hsr.univote.unigen.common.EncryptionKeyShareList;
import ch.hsr.univote.unigen.common.LatelyCertificateList;
import ch.hsr.univote.unigen.common.LatelyMixedVerificationKeyList;
import ch.hsr.univote.unigen.common.LatelyMixedVerificationKeysList;
import ch.hsr.univote.unigen.common.MixedEncryptedVotesList;
import ch.hsr.univote.unigen.common.MixedVerificationKeysList;
import ch.hsr.univote.unigen.common.PartiallyDecryptedVotesList;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Gian Poltéra
 */
public class DBElectionBoardManager {
    private ConfigHelper config;
    
    Ballots ballots = new Ballots();
    BlindedGeneratorsList blindedGeneratorsList = new BlindedGeneratorsList();
    Certificate certificate = new Certificate();
    DecodedVotes decodedVotes = new DecodedVotes();
    DecryptedVotes decryptedVotes = new DecryptedVotes();
    ElectionData electionData = new ElectionData();
    ElectionDefinition electionDefinition = new ElectionDefinition();
    ElectionGenerator electionGenerator = new ElectionGenerator();
    ElectionOptions electionOptions = new ElectionOptions();
    ElectionSystemInfo electionSystemInfo = new ElectionSystemInfo();
    ElectoralRoll electoralRoll = new ElectoralRoll();
    EncryptedVotes encryptedVotes = new EncryptedVotes();
    EncryptionKey encryptionKey = new EncryptionKey();
    EncryptionKeyShareList encryptionKeyShareList = new EncryptionKeyShareList();
    EncryptionParameters encryptionParameters = new EncryptionParameters();
    KnownElectionIds knownElectionIds = new KnownElectionIds();
    LatelyCertificateList latelyCertificateList = new LatelyCertificateList();
    LatelyMixedVerificationKeyList latelyMixedVerificationKeyList = new LatelyMixedVerificationKeyList();
    LatelyMixedVerificationKeysList latelyMixedVerificationKeysList = new LatelyMixedVerificationKeysList();
    MixedEncryptedVotesList mixedEncryptedVotesList = new MixedEncryptedVotesList();
    MixedVerificationKeysList mixedVerificationKeysList = new MixedVerificationKeysList();
    PartiallyDecryptedVotesList partiallyDecryptedVotesList = new PartiallyDecryptedVotesList();
    SignatureParameters signatureParameters = new SignatureParameters();
    VerificationKeys verificationKeys = new VerificationKeys();
    VoterCertificates voterCertificates = new VoterCertificates();

    public DBElectionBoardManager() {
        this.config = new ConfigHelper();
    }

    public void saveElectionBoard(ElectionBoard electionBoard) {
        
        String electionId = electionBoard.getElectionData().getElectionId();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("_yyyy_MM_dd_HH_mm_ss");
        String new_date = sdf.format(date);
        String filename = electionId + new_date + ".db";
        
        ballots = electionBoard.getBallots();
        blindedGeneratorsList.setBlindedGeneratorList(electionBoard.getBlindedGeneratorList());
        certificate = electionBoard.getRootCertificate();
        decodedVotes = electionBoard.getDecodedVotes();
        decryptedVotes = electionBoard.getDecryptedVotes();
        electionData = electionBoard.getElectionData();
        electionDefinition = electionBoard.getElectionDefinition();
        electionGenerator = electionBoard.getElectionGenerator();
        electionOptions = electionBoard.getElectionOptions();
        electionSystemInfo = electionBoard.getElectionSystemInfo();
        electoralRoll = electionBoard.getElectoralRoll();
        encryptedVotes = electionBoard.getEncryptedVotes();
        encryptionKey = electionBoard.getEncryptionKey();
        encryptionKeyShareList.setEncryptionKeyShareList(electionBoard.getEncryptionKeyShareList());
        encryptionParameters = electionBoard.getEncryptionParameters();
        knownElectionIds = electionBoard.getKnownElectionIds();
        latelyCertificateList.setLatelyCertificateList(electionBoard.getLatelyRegisteredVoterCertificates());
        latelyMixedVerificationKeyList.setLatelyMixedVerificationKeyList(electionBoard.getLatelyMixedVerificationKeys());
        latelyMixedVerificationKeysList.setLatelyMixedVerificationKeysList(electionBoard.getVerificationKeysLatelyMixedBy());
        mixedEncryptedVotesList.setMixedEncryptedVotesList(electionBoard.getEncryptedVotesMixedBy());
        mixedVerificationKeysList.setListMixedVerificationKeys(electionBoard.getVerificationKeysMixedBy());
        partiallyDecryptedVotesList.setPartiallyDecryptedVotesList(electionBoard.getPartiallyDecryptedVotesList());
        signatureParameters = electionBoard.getSignatureParameters();
        verificationKeys = electionBoard.getMixedVerificationKeys();
        voterCertificates = electionBoard.getVoterCertificates();
        
        DB4O db = new DB4O(filename);
        
        db.storeDB(ballots);
        db.storeDB(blindedGeneratorsList);
        db.storeDB(certificate);
        db.storeDB(decodedVotes);
        db.storeDB(decryptedVotes);
        db.storeDB(electionData);
        db.storeDB(electionDefinition);
        db.storeDB(electionGenerator);
        db.storeDB(electionOptions);
        db.storeDB(electionSystemInfo);
        db.storeDB(electoralRoll);
        db.storeDB(encryptedVotes);
        db.storeDB(encryptionKey);
        db.storeDB(encryptionKeyShareList);
        db.storeDB(encryptionParameters);
        db.storeDB(knownElectionIds);
        db.storeDB(latelyCertificateList);
        db.storeDB(latelyMixedVerificationKeyList);
        db.storeDB(latelyMixedVerificationKeysList);
        db.storeDB(mixedEncryptedVotesList);
        db.storeDB(mixedVerificationKeysList);
        db.storeDB(partiallyDecryptedVotesList);
        db.storeDB(signatureParameters);
        db.storeDB(verificationKeys);
        db.storeDB(voterCertificates);
    }

    public ElectionBoard loadElectionBoard(String filename) {
        ElectionBoard electionBoard = new ElectionBoard(config);
        
        DB4O db = new DB4O(filename);

        ballots = (Ballots) db.readDB(ballots);
        blindedGeneratorsList = (BlindedGeneratorsList) db.readDB(blindedGeneratorsList);
        certificate = (Certificate) db.readDB(certificate);
        decodedVotes = (DecodedVotes) db.readDB(decodedVotes);
        decryptedVotes = (DecryptedVotes) db.readDB(decryptedVotes);
        electionData = (ElectionData) db.readDB(electionData);
        electionDefinition = (ElectionDefinition) db.readDB(electionDefinition);
        electionGenerator = (ElectionGenerator) db.readDB(electionGenerator);
        electionOptions = (ElectionOptions) db.readDB(electionOptions);
        electionSystemInfo = (ElectionSystemInfo) db.readDB(electionSystemInfo);
        electoralRoll = (ElectoralRoll) db.readDB(electoralRoll);
        encryptedVotes = (EncryptedVotes) db.readDB(encryptedVotes);
        encryptionKey = (EncryptionKey) db.readDB(encryptionKey);
        encryptionKeyShareList = (EncryptionKeyShareList) db.readDB(encryptionKeyShareList);
        encryptionParameters = (EncryptionParameters) db.readDB(encryptionParameters);
        knownElectionIds = (KnownElectionIds) db.readDB(knownElectionIds);
        latelyCertificateList = (LatelyCertificateList) db.readDB(latelyCertificateList);
        latelyMixedVerificationKeyList = (LatelyMixedVerificationKeyList) db.readDB(latelyMixedVerificationKeyList);
        latelyMixedVerificationKeysList = (LatelyMixedVerificationKeysList) db.readDB(latelyMixedVerificationKeysList);
        mixedEncryptedVotesList = (MixedEncryptedVotesList) db.readDB(mixedEncryptedVotesList);
        mixedVerificationKeysList = (MixedVerificationKeysList) (db.readDB(mixedVerificationKeysList));
        partiallyDecryptedVotesList = (PartiallyDecryptedVotesList) db.readDB(partiallyDecryptedVotesList);
        signatureParameters = (SignatureParameters) db.readDB(signatureParameters);
        verificationKeys = (VerificationKeys) db.readDB(verificationKeys);
        voterCertificates = (VoterCertificates) db.readDB(voterCertificates);

        electionBoard.setBallots(ballots);
        electionBoard.setBlindedGeneratorList(blindedGeneratorsList.getBlindedGeneratorsList());
        electionBoard.setRootCertificate(certificate);
        electionBoard.setDecodedVotes(decodedVotes);
        electionBoard.setDecryptedVotes(decryptedVotes);
        electionBoard.setElectionData(electionData);
        electionBoard.setElectionDefinition(electionDefinition);
        electionBoard.setElectionGenerator(electionGenerator);
        electionBoard.setElectionOptions(electionOptions);
        electionBoard.setElectionSystemInfo(electionSystemInfo);
        electionBoard.setElectoralRoll(electoralRoll);
        electionBoard.setEncryptedVotes(encryptedVotes);
        electionBoard.setEncryptionKey(encryptionKey);
        electionBoard.setEncryptionKeyShareList(encryptionKeyShareList.getEncryptionKeyShareList());
        electionBoard.setEncryptionParameters(encryptionParameters);
        electionBoard.setKnownElectionIds(knownElectionIds);
        electionBoard.setLatelyRegisteredVoterCertificates(latelyCertificateList.getLatelyCertificateList());
        electionBoard.setLatelyMixedVerificationKeys(latelyMixedVerificationKeyList.getLatelyMixedVerificationKeyList());
        electionBoard.setVerificationKeysLatelyMixedBy(latelyMixedVerificationKeysList.getLatelyMixedVerificationKeysList());
        electionBoard.setEncryptedVotesMixedBy(mixedEncryptedVotesList.getMixedEncryptedVotesList());
        electionBoard.setVerificationKeysMixedBy(mixedVerificationKeysList.getListMixedVerificationKeys());
        electionBoard.setPartiallyDecryptedVotesList(partiallyDecryptedVotesList.getPartiallyDecryptedVotesList());
        electionBoard.setSignatureParameters(signatureParameters);
        electionBoard.setMixedVerificationKeys(verificationKeys);
        electionBoard.setVoterCertificates(voterCertificates);

        return electionBoard;
    }
}
