/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.db;

import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.security.KeyPair;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 *
 * @author Gian Poltéra
 */
public class DBKeyStoreManager extends VoteGenerator {

    private static ConfigHelper config = new ConfigHelper();

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
    private static Timestamp time = new Timestamp(System.currentTimeMillis());
    private static String filename = sdf.format(time);

    public void saveInDB(String electionId) {
        DB4O.storeDB(keyStore.getCaKeyPair(), electionId, filename);
        DB4O.storeDB(keyStore.getCertificateAuthorityPrivateKey(), electionId, filename);
        DB4O.storeDB(keyStore.getCertificateAuthorityPublicKey(), electionId, filename);
        DB4O.storeDB(keyStore.getElectionManagerPrivateKey(), electionId, filename);
        DB4O.storeDB(keyStore.getElectionManagerPublicKey(), electionId, filename);
        DB4O.storeDB(keyStore.getElectionAdministratorPrivateKey(), electionId, filename);
        DB4O.storeDB(keyStore.getElectionAdministratorPublicKey(), electionId, filename);
        DB4O.storeDB(keyStore.getMixersPrivateKey(), electionId, filename);
        DB4O.storeDB(keyStore.getMixersPublicKey(), electionId, filename);
        DB4O.storeDB(keyStore.getTalliersPrivateKey(), electionId, filename);
        DB4O.storeDB(keyStore.getTalliersPublicKey(), electionId, filename);
        DB4O.storeDB(keyStore.getVotersPrivateKey(), electionId, filename);
        DB4O.storeDB(keyStore.getVotersPublicKey(), electionId, filename);
        DB4O.storeDB(keyStore.getTalliersDecryptionKey(), electionId, filename);
        DB4O.storeDB(keyStore.getTalliersEncryptionKey(), electionId, filename);
        DB4O.storeDB(keyStore.getMixersSignatureKey(), electionId, filename);
        DB4O.storeDB(keyStore.getMixersVerificationKey(), electionId, filename);
        DB4O.storeDB(keyStore.getMixersGenerator(), electionId, filename);
        DB4O.storeDB(keyStore.getVotersSignatureKey(), electionId, filename);
        DB4O.storeDB(keyStore.getVotersVerificationKey(), electionId, filename);       
    }

    public void loadFromDB(String electionId) {
        KeyPair caKeyPair = null;
        keyStore.setCaKeyPair((KeyPair) DB4O.readDB(caKeyPair, electionId, filename));
    }
}
