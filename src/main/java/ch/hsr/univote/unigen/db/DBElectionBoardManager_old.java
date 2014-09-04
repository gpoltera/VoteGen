/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.db;

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
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Gian Poltéra
 */
public class DBElectionBoardManager_old {
    private ConfigHelper config;
    
    public DBElectionBoardManager_old() {
        this.config = new ConfigHelper();
    }
    
    public void saveElectionBoard(ElectionBoard electionBoard) {
        String electionId = electionBoard.getElectionData().getElectionId();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("_yyyy_mm_dd_HH_mm_ss");
        String new_date = sdf.format(date);
        String filename = electionId + new_date;
        
        DB4O.storeDB(electionBoard.getBallots(), filename);
        DB4O.storeDB(electionBoard.getBlindedGeneratorList(), filename);
        DB4O.storeDB(electionBoard.getDecodedVotes(), filename);
        DB4O.storeDB(electionBoard.getDecryptedVotes(), filename);
        DB4O.storeDB(electionBoard.getElectionData(), filename);
        DB4O.storeDB(electionBoard.getElectionDefinition(), filename);
        DB4O.storeDB(electionBoard.getElectionGenerator(), filename);
        DB4O.storeDB(electionBoard.getElectionOptions(), filename);
        DB4O.storeDB(electionBoard.getElectionSystemInfo(), filename);
        DB4O.storeDB(electionBoard.getElectoralRoll(), filename);
        DB4O.storeDB(electionBoard.getEncryptedVotes(), filename);
        DB4O.storeDB(electionBoard.getEncryptedVotesMixedBy(), filename);
        DB4O.storeDB(electionBoard.getEncryptionKey(), filename);
        DB4O.storeDB(electionBoard.getEncryptionKeyShareList(), filename);
        DB4O.storeDB(electionBoard.getEncryptionParameters(), filename);
        DB4O.storeDB(electionBoard.getKnownElectionIds(), filename);
        DB4O.storeDB(electionBoard.getLatelyMixedVerificationKeys(), filename);
        DB4O.storeDB(electionBoard.getLatelyRegisteredVoterCertificates(), filename);
        DB4O.storeDB(electionBoard.getMixedVerificationKeys(), filename);
        DB4O.storeDB(electionBoard.getPartiallyDecryptedVotesList(), filename);
        DB4O.storeDB(electionBoard.getRootCertificate(), filename);
        DB4O.storeDB(electionBoard.getSignatureParameters(), filename);
        DB4O.storeDB(electionBoard.getVerificationKeysLatelyMixedBy(), filename);
        DB4O.storeDB(electionBoard.getVerificationKeysMixedBy(), filename);
        DB4O.storeDB(electionBoard.getVoterCertificates(), filename);
    }

    public ElectionBoard loadElectionBoard(String filename) {
        ElectionBoard electionBoard = new ElectionBoard(config);
        
        Ballots ballots = new Ballots();
        Certificate certificate = new Certificate();
        DecodedVotes decodedVotes = new DecodedVotes();
        DecryptedVotes decryptedVotes = new DecryptedVotes();
        ElectionSystemInfo electionSystemInfo = new ElectionSystemInfo();
        ElectionDefinition electionDefinition = new ElectionDefinition();
        ElectionGenerator electionGenerator = new ElectionGenerator();
        ElectionOptions electionOptions = new ElectionOptions();
        ElectionData electionData = new ElectionData();
        ElectoralRoll electoralRoll = new ElectoralRoll();
        EncryptedVotes encryptedVotes = new EncryptedVotes();
        EncryptionParameters encryptionParameters = new EncryptionParameters();
        EncryptionKey encryptionKey = new EncryptionKey();
        KnownElectionIds knownElectionIds = new KnownElectionIds();
        Map<String, MixedVerificationKeys> listMixedVerificationKeys = new HashMap<>();
        List<MixedVerificationKey> listLatelyMixedVerificationKey = new ArrayList<>();
        Map<String, List> listLatelyMixedVerificationKeys = new HashMap<>();
        List<Certificate> listLatelyCertificate = new ArrayList<>();
        Map<String, EncryptionKeyShare> encryptionKeyShareList = new HashMap<>();
        Map<String, MixedEncryptedVotes> mixedEncryptedVotesList = new HashMap<>();
        Map<String, PartiallyDecryptedVotes> partiallyDecryptedVotesList = new HashMap<>();
        Map<String, BlindedGenerator> blindedGeneratorsList = new HashMap<>();
        SignatureParameters signatureParameters = new SignatureParameters();
        VoterCertificates voterCertificates = new VoterCertificates();
        VerificationKeys verificationKeys = new VerificationKeys();

        electionBoard.setBallots((Ballots) DB4O.readDB(ballots, filename));
        electionBoard.setBlindedGeneratorList((Map<String, BlindedGenerator>) DB4O.readDB(blindedGeneratorsList, filename));
        electionBoard.setDecodedVotes((DecodedVotes) DB4O.readDB(decodedVotes, filename));
        electionBoard.setDecryptedVotes((DecryptedVotes) DB4O.readDB(decryptedVotes, filename));
        electionBoard.setElectionData((ElectionData) DB4O.readDB(electionData, filename));
        electionBoard.setElectionDefinition((ElectionDefinition) DB4O.readDB(electionDefinition, filename));
        electionBoard.setElectionGenerator((ElectionGenerator) DB4O.readDB(electionGenerator, filename));
        electionBoard.setElectionOptions((ElectionOptions) DB4O.readDB(electionOptions, filename));
        electionBoard.setElectionSystemInfo((ElectionSystemInfo) DB4O.readDB(electionSystemInfo, filename));
        electionBoard.setElectoralRoll((ElectoralRoll) DB4O.readDB(electoralRoll, filename));
        electionBoard.setEncryptedVotes((EncryptedVotes) DB4O.readDB(encryptedVotes, filename));
        electionBoard.setEncryptedVotesMixedBy((Map<String, MixedEncryptedVotes>) DB4O.readDB(mixedEncryptedVotesList, filename)); //NOK
        electionBoard.setEncryptionKey((EncryptionKey) DB4O.readDB(encryptionKey, filename));
        electionBoard.setEncryptionKeyShareList((Map<String, EncryptionKeyShare>) DB4O.readDB(encryptionKeyShareList, filename));
        electionBoard.setEncryptionParameters((EncryptionParameters) DB4O.readDB(encryptionParameters, filename));
        electionBoard.setKnownElectionIds((KnownElectionIds) DB4O.readDB(knownElectionIds, filename));
        electionBoard.setLatelyMixedVerificationKeys((List<MixedVerificationKey>) DB4O.readDB(listLatelyMixedVerificationKey, filename)); //NOK
        electionBoard.setLatelyRegisteredVoterCertificates((List<Certificate>) DB4O.readDB(listLatelyCertificate, filename));
        electionBoard.setMixedVerificationKeys((VerificationKeys) DB4O.readDB(verificationKeys, filename));
        electionBoard.setPartiallyDecryptedVotesList((Map<String, PartiallyDecryptedVotes>) DB4O.readDB(partiallyDecryptedVotesList, filename));
        electionBoard.setRootCertificate((Certificate) DB4O.readDB(certificate, filename));
        electionBoard.setSignatureParameters((SignatureParameters) DB4O.readDB(signatureParameters, filename));
        electionBoard.setVerificationKeysLatelyMixedBy((Map<String, List>) DB4O.readDB(listLatelyMixedVerificationKeys, filename));
        electionBoard.setVerificationKeysMixedBy((Map<String, MixedVerificationKeys>) DB4O.readDB(listMixedVerificationKeys, filename));
        electionBoard.setVoterCertificates((VoterCertificates) DB4O.readDB(voterCertificates, filename));

        Object test = (Object) listMixedVerificationKeys;
        Map<String, MixedVerificationKeys> test2 = (Map<String, MixedVerificationKeys>) test;
        
        return electionBoard;
    }
}
