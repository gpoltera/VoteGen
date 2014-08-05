/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.board;

import ch.hsr.univote.unigen.db.DB4O;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 *
 * @author Gian
 */
public class KeyStore {
    /*Variable declaration*/

    private static ConfigHelper config;

    private static SimpleDateFormat sdf;
    private static Timestamp time;
    private static String filename;

    private static String[] mixers;
    private static String[] talliers;

    private static KeyPair caKeyPair;

    private static RSAPrivateKey certificateAuthorityPrivateKey;
    private static RSAPublicKey certificateAuthorityPublicKey;

    private static RSAPrivateKey electionManagerPrivateKey;
    private static RSAPublicKey electionManagerPublicKey;

    private static RSAPrivateKey electionAdministratorPrivateKey;
    private static RSAPublicKey electionAdministratorPublicKey;

    private static RSAPrivateKey[] mixersPrivateKey;
    private static RSAPublicKey[] mixersPublicKey;

    private static RSAPrivateKey[] talliersPrivateKey;
    private static RSAPublicKey[] talliersPublicKey;

    private static RSAPrivateKey[] votersPrivateKey;
    private static RSAPublicKey[] votersPublicKey;

    private static BigInteger[] talliersDecryptionKey;
    private static BigInteger[] talliersEncryptionKey;

    private static BigInteger[] mixersSignatureKey;
    private static BigInteger[] mixersVerificationKey;
    private static BigInteger[] mixersGenerator;

    private static BigInteger[] votersSignatureKey;
    private static BigInteger[] votersVerificationKey;

    /*constructor*/
    public KeyStore() {
        config = new ConfigHelper();
        sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        time = new Timestamp(System.currentTimeMillis());
        filename = sdf.format(time);
        mixers = config.getMixerIds();
        talliers = config.getTallierIds();
        mixersPrivateKey = new RSAPrivateKey[mixers.length];
        mixersPublicKey = new RSAPublicKey[mixers.length];
        talliersPrivateKey = new RSAPrivateKey[talliers.length];
        talliersPublicKey = new RSAPublicKey[talliers.length];
        votersPrivateKey = new RSAPrivateKey[config.getVotersNumber()];
        votersPublicKey = new RSAPublicKey[config.getVotersNumber()];
        talliersDecryptionKey = new BigInteger[talliers.length];
        talliersEncryptionKey = new BigInteger[talliers.length];
        mixersSignatureKey = new BigInteger[mixers.length];
        mixersVerificationKey = new BigInteger[mixers.length];
        mixersGenerator = new BigInteger[mixers.length];
        votersSignatureKey = new BigInteger[config.getVotersNumber()];
        votersVerificationKey = new BigInteger[config.getVotersNumber()];
    }

    /*accessors SET*/
    public void setCaKeyPair(KeyPair caKeyPair) {
        KeyStore.caKeyPair = caKeyPair;
    }

    public void setCertificateAuthorityPrivateKey(RSAPrivateKey certificateAuthorityPrivateKey) {
        KeyStore.certificateAuthorityPrivateKey = certificateAuthorityPrivateKey;
    }

    public void setCertificateAuthorityPublicKey(RSAPublicKey certificateAuthorityPublicKey) {
        KeyStore.certificateAuthorityPublicKey = certificateAuthorityPublicKey;
    }

    public void setElectionManagerPrivateKey(RSAPrivateKey electionManagerPrivateKey) {
        KeyStore.electionManagerPrivateKey = electionManagerPrivateKey;
    }

    public void setElectionManagerPublicKey(RSAPublicKey electionManagerPublicKey) {
        KeyStore.electionManagerPublicKey = electionManagerPublicKey;
    }

    public void setElectionAdministratorPrivateKey(RSAPrivateKey electionAdministratorPrivateKey) {
        KeyStore.electionAdministratorPrivateKey = electionAdministratorPrivateKey;
    }

    public void setElectionAdministratorPublicKey(RSAPublicKey electionAdministratorPublicKey) {
        KeyStore.electionAdministratorPublicKey = electionAdministratorPublicKey;
    }

    public void setMixerPrivateKey(int mixer, RSAPrivateKey mixerPrivateKey) {
        KeyStore.mixersPrivateKey[mixer] = mixerPrivateKey;
    }

    public void setMixerPublicKey(int mixer, RSAPublicKey mixerPublicKey) {
        KeyStore.mixersPublicKey[mixer] = mixerPublicKey;
    }

    public void setTallierPrivateKey(int tallier, RSAPrivateKey tallierPrivateKey) {
        KeyStore.talliersPrivateKey[tallier] = tallierPrivateKey;
    }

    public void setTallierPublicKey(int tallier, RSAPublicKey tallierPublicKey) {
        KeyStore.talliersPublicKey[tallier] = tallierPublicKey;
    }

    public void setVoterPrivateKey(int voter, RSAPrivateKey voterPrivateKey) {
        KeyStore.votersPrivateKey[voter] = voterPrivateKey;
    }

    public void setVoterPublicKey(int voter, RSAPublicKey voterPublicKey) {
        KeyStore.votersPublicKey[voter] = voterPublicKey;
    }

    public void setTallierDecryptionKey(int tallier, BigInteger tallierDecryptionKey) {
        KeyStore.talliersDecryptionKey[tallier] = tallierDecryptionKey;
    }

    public void setTallierEncryptionKey(int tallier, BigInteger tallierEncryptionKey) {
        KeyStore.talliersEncryptionKey[tallier] = tallierEncryptionKey;
    }

    public void setMixerSignatureKey(int mixer, BigInteger mixerSignatureKey) {
        KeyStore.mixersSignatureKey[mixer] = mixerSignatureKey;
    }

    public void setMixerVerificationKey(int mixer, BigInteger mixerVerificationKey) {
        KeyStore.mixersVerificationKey[mixer] = mixerVerificationKey;
    }

    public void setMixerGenerator(int mixer, BigInteger mixerGenerator) {
        KeyStore.mixersGenerator[mixer] = mixerGenerator;
    }

    public void setVoterSignatureKey(int voter, BigInteger voterSignatureKey) {
        KeyStore.votersSignatureKey[voter] = voterSignatureKey;
    }

    public void setVoterVerificationKey(int voter, BigInteger voterVerificationKey) {
        KeyStore.votersVerificationKey[voter] = voterVerificationKey;
    }

    /*accessors GET*/
    public KeyPair getCaKeyPair() {
        return caKeyPair;
    }

    public RSAPrivateKey getCertificateAuthorityPrivateKey() {
        return certificateAuthorityPrivateKey;
    }

    public RSAPublicKey getCertificateAuthorityPublicKey() {
        return certificateAuthorityPublicKey;
    }

    public RSAPrivateKey getElectionManagerPrivateKey() {
        return electionManagerPrivateKey;
    }

    public RSAPublicKey getElectionManagerPublicKey() {
        return electionManagerPublicKey;
    }

    public RSAPrivateKey getElectionAdministratorPrivateKey() {
        return electionAdministratorPrivateKey;
    }

    public RSAPublicKey getElectionAdministratorPublicKey() {
        return electionAdministratorPublicKey;
    }

    public RSAPrivateKey getMixerPrivateKey(int mixer) {
        return mixersPrivateKey[mixer];
    }

    public RSAPublicKey getMixerPublicKey(int mixer) {
        return mixersPublicKey[mixer];
    }

    public RSAPrivateKey getTallierPrivateKey(int tallier) {
        return talliersPrivateKey[tallier];
    }

    public RSAPublicKey getTallierPublicKey(int tallier) {
        return talliersPublicKey[tallier];
    }

    public RSAPrivateKey getVoterPrivateKey(int voter) {
        return votersPrivateKey[voter];
    }

    public RSAPublicKey getVoterPublicKey(int voter) {
        return votersPublicKey[voter];
    }

    public BigInteger getTallierDecryptionKey(int tallier) {
        return talliersDecryptionKey[tallier];
    }

    public BigInteger getTallierEncryptionKey(int tallier) {
        return talliersEncryptionKey[tallier];
    }

    public BigInteger getMixerSignatureKey(int mixer) {
        return mixersSignatureKey[mixer];
    }

    public BigInteger getMixerVerificationKey(int mixer) {
        return mixersVerificationKey[mixer];
    }

    public BigInteger getMixerGenerator(int mixer) {
        return mixersGenerator[mixer];
    }

    public BigInteger getVoterSignatureKey(int voter) {
        return votersSignatureKey[voter];
    }
    
    public BigInteger getVoterVerificationKey(int voter) {
        return votersVerificationKey[voter];
    }
    
    public BigInteger[] getVotersVerificationKey() {
        return votersVerificationKey;
    }


    /*accessors DB*/
    public void storeInDB(String electionId) {
        DB4O.storeDB(caKeyPair, electionId, filename);
        DB4O.storeDB(certificateAuthorityPrivateKey, electionId, filename);
    }

    public void readFromDB(String electionId) {
        caKeyPair = (KeyPair) DB4O.readDB(caKeyPair, electionId, filename);
        certificateAuthorityPrivateKey = (RSAPrivateKey) DB4O.readDB(certificateAuthorityPrivateKey, electionId, filename);
    }
}
