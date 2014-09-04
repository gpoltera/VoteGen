/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.db;

import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 *
 * @author Gian Poltéra
 */
public class DBKeyStoreManager {
    
    private ConfigHelper config;
    private KeyStore keyStore;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
    private static Timestamp time = new Timestamp(System.currentTimeMillis());
    private static String filename = sdf.format(time);

    public DBKeyStoreManager() {
        this.config = VoteGenerator.config;
        this.keyStore = VoteGenerator.keyStore;
    }

    public void saveInDB(String electionId) {
//        DB4O.storeDB(keyStore.getCaKeyPair(), electionId, filename);
//        DB4O.storeDB(keyStore.getCASignatureKey(), electionId, filename);
//        DB4O.storeDB(keyStore.getCAVerificationKey(), electionId, filename);
//        DB4O.storeDB(keyStore.getEMSignatureKey(), electionId, filename);
//        DB4O.storeDB(keyStore.getEMVerificationKey(), electionId, filename);
//        DB4O.storeDB(keyStore.getEASignatureKey(), electionId, filename);
//        DB4O.storeDB(keyStore.getEAVerificationKey(), electionId, filename);
//        DB4O.storeDB(keyStore.getMixersSignatureKey(), electionId, filename);
//        DB4O.storeDB(keyStore.getMixersVerificationKey(), electionId, filename);
//        DB4O.storeDB(keyStore.getTalliersSignatureKey(), electionId, filename);
//        DB4O.storeDB(keyStore.getTalliersVerificationKey(), electionId, filename);
//        DB4O.storeDB(keyStore.getVotersSignatureKey(), electionId, filename);
//        DB4O.storeDB(keyStore.getVotersVerificationKey(), electionId, filename);
//        DB4O.storeDB(keyStore.getTalliersDecryptionKey(), electionId, filename);
//        DB4O.storeDB(keyStore.getTalliersEncryptionKey(), electionId, filename);
//        DB4O.storeDB(keyStore.getMixersSignatureKey(), electionId, filename);
//        DB4O.storeDB(keyStore.getMixersVerificationKey(), electionId, filename);
//        DB4O.storeDB(keyStore.getMixersGenerator(), electionId, filename);
//        DB4O.storeDB(keyStore.getVotersSignatureKey(), electionId, filename);
//        DB4O.storeDB(keyStore.getVotersVerificationKey(), electionId, filename);       
    }

    public void loadFromDB(String electionId) {
//        KeyPair caKeyPair = null;
//        keyStore.setCAKeyPair((KeyPair) DB4O.readDB(caKeyPair, electionId, filename));
    }
}
