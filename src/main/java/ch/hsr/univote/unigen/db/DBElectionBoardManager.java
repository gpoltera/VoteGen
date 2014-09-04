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
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gian Poltéra
 */
public class DBElectionBoardManager {

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
    Map<String, MixedVerificationKeys> listMixedVerificationKeys = new ListMixedVerificationKeys<String, MixedVerificationKeys>();
    List<MixedVerificationKey> listLatelyMixedVerificationKey = new ListLatelyMixedVerificationKey<MixedVerificationKey>();
    Map<String, List> listLatelyMixedVerificationKeys = new ListLatelyMixedVerificationKeys<String, List>();
    List<Certificate> listLatelyCertificate = new ListLatelyCertificate<Certificate>();
    Map<String, EncryptionKeyShare> encryptionKeyShareList = new EncryptionKeyShareList<String, EncryptionKeyShare>();
    Map<String, MixedEncryptedVotes> mixedEncryptedVotesList = new MixedEncryptedVotesList<String, MixedEncryptedVotes>();
    Map<String, PartiallyDecryptedVotes> partiallyDecryptedVotesList = new PartiallyDecryptedVotesList<String, PartiallyDecryptedVotes>();
    Map<String, BlindedGenerator> blindedGeneratorsList = new BlindedGeneratorsList<String, BlindedGenerator>();
    SignatureParameters signatureParameters = new SignatureParameters();
    VoterCertificates voterCertificates = new VoterCertificates();
    VerificationKeys verificationKeys = new VerificationKeys();

    private ConfigHelper config;

    public DBElectionBoardManager() {
        this.config = new ConfigHelper();
    }

    public void saveElectionBoard(ElectionBoard electionBoard) {
        String electionId = electionBoard.getElectionData().getElectionId();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("_yyyy_mm_dd_HH_mm_ss");
        String new_date = sdf.format(date);
        String filename = electionId + new_date;

        ballots = electionBoard.getBallots();
        certificate = electionBoard.getRootCertificate();
        decodedVotes = electionBoard.getDecodedVotes();
        decryptedVotes = electionBoard.getDecryptedVotes();
        electionSystemInfo = electionBoard.getElectionSystemInfo();
        electionDefinition = electionBoard.getElectionDefinition();
        electionGenerator = electionBoard.getElectionGenerator();
        electionOptions = electionBoard.getElectionOptions();
        electionData = electionBoard.getElectionData();
        electoralRoll = electionBoard.getElectoralRoll();
        encryptedVotes = electionBoard.getEncryptedVotes();
        encryptionParameters = electionBoard.getEncryptionParameters();
        encryptionKey = electionBoard.getEncryptionKey();
        knownElectionIds = electionBoard.getKnownElectionIds();
        listMixedVerificationKeys = electionBoard.getVerificationKeysMixedBy();
        listLatelyMixedVerificationKey = electionBoard.getLatelyMixedVerificationKeys();
        listLatelyMixedVerificationKeys = electionBoard.getVerificationKeysLatelyMixedBy();
        listLatelyCertificate = electionBoard.getLatelyRegisteredVoterCertificates();
        encryptionKeyShareList = electionBoard.getEncryptionKeyShareList();
        mixedEncryptedVotesList = electionBoard.getEncryptedVotesMixedBy();
        partiallyDecryptedVotesList = electionBoard.getPartiallyDecryptedVotesList();
        blindedGeneratorsList = electionBoard.getBlindedGeneratorList();
        signatureParameters = electionBoard.getSignatureParameters();
        voterCertificates = electionBoard.getVoterCertificates();
        verificationKeys = electionBoard.getMixedVerificationKeys();

        DB4O.storeDB(ballots, filename);
        DB4O.storeDB(blindedGeneratorsList, filename);
        DB4O.storeDB(decodedVotes, filename);
        DB4O.storeDB(decryptedVotes, filename);
        DB4O.storeDB(electionData, filename);
        DB4O.storeDB(electionDefinition, filename);
        DB4O.storeDB(electionGenerator, filename);
        DB4O.storeDB(electionOptions, filename);
        DB4O.storeDB(electionSystemInfo, filename);
        DB4O.storeDB(electoralRoll, filename);
        DB4O.storeDB(encryptedVotes, filename);
        DB4O.storeDB(mixedEncryptedVotesList, filename);
        DB4O.storeDB(encryptionKey, filename);
        DB4O.storeDB(encryptionKeyShareList, filename);
        DB4O.storeDB(encryptionParameters, filename);
        DB4O.storeDB(knownElectionIds, filename);
        DB4O.storeDB(listLatelyMixedVerificationKey, filename);
        DB4O.storeDB(listLatelyCertificate, filename);
        DB4O.storeDB(verificationKeys, filename);
        DB4O.storeDB(partiallyDecryptedVotesList, filename);
        DB4O.storeDB(certificate, filename);
        DB4O.storeDB(signatureParameters, filename);
        DB4O.storeDB(listLatelyMixedVerificationKeys, filename);
        DB4O.storeDB(listMixedVerificationKeys, filename);
        DB4O.storeDB(voterCertificates, filename);
    }

    public ElectionBoard loadElectionBoard(String filename) {
        ElectionBoard electionBoard = new ElectionBoard(config);

        ballots = (Ballots) DB4O.readDB(ballots, filename);
        certificate = (Certificate) DB4O.readDB(certificate, filename);
        decodedVotes = (DecodedVotes) DB4O.readDB(decodedVotes, filename);
        decryptedVotes = (DecryptedVotes) DB4O.readDB(decryptedVotes, filename);
        electionSystemInfo = (ElectionSystemInfo) DB4O.readDB(electionSystemInfo, filename);
        electionDefinition = (ElectionDefinition) DB4O.readDB(electionDefinition, filename);
        electionGenerator = (ElectionGenerator) DB4O.readDB(electionGenerator, filename);
        electionOptions = (ElectionOptions) DB4O.readDB(electionOptions, filename);
        electionData = (ElectionData) DB4O.readDB(electionData, filename);
        electoralRoll = (ElectoralRoll) DB4O.readDB(electoralRoll, filename);
        encryptedVotes = (EncryptedVotes) DB4O.readDB(encryptedVotes, filename);
        encryptionParameters = (EncryptionParameters) DB4O.readDB(encryptionParameters, filename);
        encryptionKey = (EncryptionKey) DB4O.readDB(encryptionKey, filename);
        knownElectionIds = (KnownElectionIds) DB4O.readDB(knownElectionIds, filename);
        listMixedVerificationKeys = (HashMap) getMap((DB4O.readDB(listMixedVerificationKeys, filename)));
        listLatelyMixedVerificationKey = (ArrayList) getList(DB4O.readDB(listLatelyMixedVerificationKey, filename));
        listLatelyMixedVerificationKeys = (HashMap) getMap(DB4O.readDB(listLatelyMixedVerificationKeys, filename));
        listLatelyCertificate = (ArrayList) getList(DB4O.readDB(listLatelyCertificate, filename));
        encryptionKeyShareList = getMapEKS(DB4O.readDB(encryptionKeyShareList, filename));
        mixedEncryptedVotesList = (HashMap) getMap(DB4O.readDB(mixedEncryptedVotesList, filename));
        partiallyDecryptedVotesList = (HashMap) getMap(DB4O.readDB(partiallyDecryptedVotesList, filename));
        blindedGeneratorsList = (HashMap) getMap(DB4O.readDB(blindedGeneratorsList, filename));
        signatureParameters = (SignatureParameters) DB4O.readDB(signatureParameters, filename);
        voterCertificates = (VoterCertificates) DB4O.readDB(voterCertificates, filename);
        verificationKeys = (VerificationKeys) DB4O.readDB(verificationKeys, filename);

        electionBoard.setBallots(ballots);
        electionBoard.setBlindedGeneratorList(blindedGeneratorsList);
        electionBoard.setDecodedVotes(decodedVotes);
        electionBoard.setDecryptedVotes(decryptedVotes);
        electionBoard.setElectionData(electionData);
        electionBoard.setElectionDefinition(electionDefinition);
        electionBoard.setElectionGenerator(electionGenerator);
        electionBoard.setElectionOptions(electionOptions);
        electionBoard.setElectionSystemInfo(electionSystemInfo);
        electionBoard.setElectoralRoll(electoralRoll);
        electionBoard.setEncryptedVotes(encryptedVotes);
        electionBoard.setEncryptedVotesMixedBy(mixedEncryptedVotesList);
        electionBoard.setEncryptionKey(encryptionKey);
        electionBoard.setEncryptionKeyShareList(encryptionKeyShareList);
        electionBoard.setEncryptionParameters(encryptionParameters);
        electionBoard.setKnownElectionIds(knownElectionIds);
        electionBoard.setLatelyMixedVerificationKeys(listLatelyMixedVerificationKey);
        electionBoard.setLatelyRegisteredVoterCertificates(listLatelyCertificate);
        electionBoard.setMixedVerificationKeys(verificationKeys);
        electionBoard.setPartiallyDecryptedVotesList(partiallyDecryptedVotesList);
        electionBoard.setRootCertificate(certificate);
        electionBoard.setSignatureParameters(signatureParameters);
        electionBoard.setVerificationKeysLatelyMixedBy(listLatelyMixedVerificationKeys);
        electionBoard.setVerificationKeysMixedBy(listMixedVerificationKeys);
        electionBoard.setVoterCertificates(voterCertificates);

        return electionBoard;
    }

    public Map<String, EncryptionKeyShare> getMapEKS(Object o) {
        Map<String, EncryptionKeyShare> result = new EncryptionKeyShareList<String, EncryptionKeyShare>();
        Field[] declaredFields = o.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            try {
                result.put(field.getName(), (EncryptionKeyShare) field.get(o));
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(DBElectionBoardManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return result;
    }

    public Map<String, Object> getMap(Object o) {
        Map<String, Object> result = new HashMap<>();
        Field[] declaredFields = o.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            try {
                result.put(field.getName(), field.get(o));
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(DBElectionBoardManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public List<Object> getList(Object o) {
        List<Object> result = new ArrayList<>();
        Field[] declaredFields = o.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            try {
                result.add(field.get(o));
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(DBElectionBoardManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
}

class ListMixedVerificationKeys<K, V> extends HashMap {
}

class ListLatelyMixedVerificationKey<E> extends ArrayList {
}

class ListLatelyMixedVerificationKeys<K, V> extends HashMap {
}

class ListLatelyCertificate<E> extends ArrayList {
}

class EncryptionKeyShareList<K, V> extends HashMap {
}

class MixedEncryptedVotesList<K, V> extends HashMap {
}

class PartiallyDecryptedVotesList<K, V> extends HashMap {
}

class BlindedGeneratorsList<K, V> extends HashMap {
}
