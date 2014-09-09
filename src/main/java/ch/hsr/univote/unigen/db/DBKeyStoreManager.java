/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.db;

import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.common.BlindedGeneratorKey;
import ch.hsr.univote.unigen.common.CASignatureKey;
import ch.hsr.univote.unigen.common.CAVerificationKey;
import ch.hsr.univote.unigen.common.EASignatureKey;
import ch.hsr.univote.unigen.common.EAVerificationKey;
import ch.hsr.univote.unigen.common.EMSignatureKey;
import ch.hsr.univote.unigen.common.EMVerificationKey;
import ch.hsr.univote.unigen.common.LatelyVotersSignatureKey;
import ch.hsr.univote.unigen.common.LatelyVotersVerificationKey;
import ch.hsr.univote.unigen.common.MixersSignatureKey;
import ch.hsr.univote.unigen.common.MixersVerificationKey;
import ch.hsr.univote.unigen.common.RASignatureKey;
import ch.hsr.univote.unigen.common.RAVerificationKey;
import ch.hsr.univote.unigen.common.TalliersDecryptionKey;
import ch.hsr.univote.unigen.common.TalliersEncryptionKey;
import ch.hsr.univote.unigen.common.TalliersSignatureKey;
import ch.hsr.univote.unigen.common.TalliersVerificationKey;
import ch.hsr.univote.unigen.common.VotersSignatureKey;
import ch.hsr.univote.unigen.common.VotersVerificationKey;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Gian Poltéra
 */
public class DBKeyStoreManager {
    
    RASignatureKey raSignatureKey = new RASignatureKey();
    RAVerificationKey raVerificationKey = new RAVerificationKey();
    CASignatureKey caSignatureKey = new CASignatureKey();
    CAVerificationKey caVerificationKey = new CAVerificationKey();
    EMSignatureKey emSignatureKey = new EMSignatureKey();
    EMVerificationKey emVerificationKey = new EMVerificationKey();
    EASignatureKey eaSignatureKey = new EASignatureKey();
    EAVerificationKey eaVerificationKey = new EAVerificationKey();
    MixersSignatureKey mixersSignatureKey = new MixersSignatureKey();
    MixersVerificationKey mixersVerificationKey = new MixersVerificationKey();
    TalliersSignatureKey talliersSignatureKey = new TalliersSignatureKey();
    TalliersVerificationKey talliersVerificationKey = new TalliersVerificationKey();
    VotersSignatureKey votersSignatureKey = new VotersSignatureKey();
    VotersVerificationKey votersVerificationKey = new VotersVerificationKey();
    LatelyVotersSignatureKey latelyVotersSignatureKey = new LatelyVotersSignatureKey();
    LatelyVotersVerificationKey latelyVotersVerificationKey = new LatelyVotersVerificationKey();
    TalliersDecryptionKey talliersDecryptionKey = new TalliersDecryptionKey();
    TalliersEncryptionKey talliersEncryptionKey = new TalliersEncryptionKey();
    BlindedGeneratorKey blindedGeneratorKey = new BlindedGeneratorKey();
    
    public DBKeyStoreManager() {
    }
    
    public void saveKeyStore(String electionId, KeyStore keyStore) {
        
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("_yyyy_MM_dd_HH_mm_ss");
        String new_date = sdf.format(date);
        String filename = electionId + new_date + "_keystore" + ".db";
        
        raSignatureKey.setRASignatureKey(keyStore.getRASignatureKey());
        raVerificationKey.setRAVerificationKey(keyStore.getRAVerificationKey());
        caSignatureKey.setCASignatureKey(keyStore.getCASignatureKey());
        caVerificationKey.setCAVerificationKey(keyStore.getCAVerificationKey());
        emSignatureKey.setEMSignatureKey(keyStore.getEMSignatureKey());
        emVerificationKey.setEMVerificationKey(keyStore.getEMVerificationKey());
        eaSignatureKey.setEASignatureKey(keyStore.getEASignatureKey());
        eaVerificationKey.setEAVerificationKey(keyStore.getEAVerificationKey());
        mixersSignatureKey.setMixersSignatureKey(keyStore.getMixersSignatureKey());
        mixersVerificationKey.setMixersVerificationKey(keyStore.getMixersVerificationKey());
        talliersSignatureKey.setTalliersSignatureKey(keyStore.getTalliersSignatureKey());
        talliersVerificationKey.setTalliersVerificationKey(keyStore.getTalliersVerificationKey());
        votersSignatureKey.setVotersSignatureKey(keyStore.getVotersSignatureKey());
        votersVerificationKey.setVotersVerificationKey(keyStore.getVotersVerificationKey());
        latelyVotersSignatureKey.setLatelyVotersSignatureKey(keyStore.getLatelyVotersSignatureKey());
        latelyVotersVerificationKey.setLatelyVotersVerificationKey(keyStore.getLatelyVotersVerificationKey());
        talliersDecryptionKey.setTalliersDecryptionKey(keyStore.getTalliersDecryptionKey());
        talliersEncryptionKey.setTalliersEncryptionKey(keyStore.getTalliersEncryptionKey());
        blindedGeneratorKey.setBlindedGeneratorKey(keyStore.getBlindedGeneratorsKey());
        
        DB4O db = new DB4O(filename);
        
        db.storeDB(raSignatureKey);
        db.storeDB(raVerificationKey);
        db.storeDB(caSignatureKey);
        db.storeDB(caVerificationKey);
        db.storeDB(emSignatureKey);
        db.storeDB(emVerificationKey);
        db.storeDB(eaSignatureKey);
        db.storeDB(eaVerificationKey);
        db.storeDB(mixersSignatureKey);
        db.storeDB(mixersVerificationKey);
        db.storeDB(talliersSignatureKey);
        db.storeDB(talliersVerificationKey);
        db.storeDB(votersSignatureKey);
        db.storeDB(votersVerificationKey);
        db.storeDB(latelyVotersSignatureKey);
        db.storeDB(latelyVotersVerificationKey);
        db.storeDB(talliersDecryptionKey);
        db.storeDB(talliersEncryptionKey);
        db.storeDB(blindedGeneratorKey);
    }
}
