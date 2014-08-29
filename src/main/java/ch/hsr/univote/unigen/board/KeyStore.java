/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.board;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian
 */
public class KeyStore {
    /*Variable declaration*/
    private static RSAPrivateKey caSignatureKey;
    private static RSAPublicKey caVerificationKey;
    private static RSAPrivateKey emSignatureKey;
    private static RSAPublicKey emVerificationKey;
    private static RSAPrivateKey eaSignatureKey;
    private static RSAPublicKey eaVerificationKey;
    private static List<RSAPrivateKey> mixersSignatureKey = new ArrayList<>();
    private static List<RSAPublicKey> mixersVerificationKey = new ArrayList<>();
    private static List<RSAPrivateKey> talliersSignatureKey = new ArrayList<>();
    private static List<RSAPublicKey> talliersVerificationKey = new ArrayList<>();
    private static List<DSAPrivateKey> votersSignatureKey = new ArrayList<>();
    private static List<DSAPublicKey> votersVerificationKey = new ArrayList<>();
    private static List<DSAPrivateKey> talliersDecryptionKey = new ArrayList<>();
    private static List<DSAPublicKey> talliersEncryptionKey = new ArrayList<>();
    private static List<DSAPrivateKey> blindedGeneratorKey = new ArrayList<>();

    /*accessors SET*/
    public void setCASignatureKey(RSAPrivateKey privateKey) {
        KeyStore.caSignatureKey = privateKey;
    }

    public void setCAVerificationKey(RSAPublicKey publicKey) {
        KeyStore.caVerificationKey = publicKey;
    }

    public void setEMSignatureKey(RSAPrivateKey privateKey) {
        KeyStore.emSignatureKey = privateKey;
    }

    public void setEMVerificationKey(RSAPublicKey publicKey) {
        KeyStore.emVerificationKey = publicKey;
    }

    public void setEASignatureKey(RSAPrivateKey privateKey) {
        KeyStore.eaSignatureKey = privateKey;
    }

    public void setEAVerificationKey(RSAPublicKey publicKey) {
        KeyStore.eaVerificationKey = publicKey;
    }

    public void setMixerSignatureKey(int mixer, RSAPrivateKey privateKey) {
        KeyStore.mixersSignatureKey.add(mixer, privateKey);
    }

    public void setMixerVerificationKey(int mixer, RSAPublicKey publicKey) {
        KeyStore.mixersVerificationKey.add(mixer, publicKey);
    }

    public void setTallierSignatureKey(int tallier, RSAPrivateKey privateKey) {
        KeyStore.talliersSignatureKey.add(tallier, privateKey);
    }

    public void setTallierVerificationKey(int tallier, RSAPublicKey publicKey) {
        KeyStore.talliersVerificationKey.add(tallier, publicKey);
    }

    public void setVoterSignatureKey(int voter, DSAPrivateKey privateKey) {
        KeyStore.votersSignatureKey.add(voter, privateKey);
    }
    
    public void setVoterVerificationKey(int voter, DSAPublicKey publicKey) {
        KeyStore.votersVerificationKey.add(voter, publicKey);
    }
    
    public void setTallierDecryptionKey(int tallier, DSAPrivateKey privateKey) {
        KeyStore.talliersDecryptionKey.add(tallier, privateKey);
    }
    
    public void setTallierEncryptionKey(int tallier, DSAPublicKey publicKey) {
        KeyStore.talliersEncryptionKey.add(tallier, publicKey);
    }
    
    public void setBlindedGeneratorKey(int mixer, DSAPrivateKey privateKey) {
        KeyStore.blindedGeneratorKey.add(mixer, privateKey);
    }
    

    /*accessors GET*/
    public RSAPrivateKey getCASignatureKey() {
        return caSignatureKey;
    }

    public RSAPublicKey getCAVerificationKey() {
        return caVerificationKey;
    }

    public RSAPrivateKey getEMSignatureKey() {
        return emSignatureKey;
    }

    public RSAPublicKey getEMVerificationKey() {
        return emVerificationKey;
    }

    public RSAPrivateKey getEASignatureKey() {
        return eaSignatureKey;
    }

    public RSAPublicKey getEAVerificationKey() {
        return eaVerificationKey;
    }

    public RSAPrivateKey getMixerSignatureKey(int mixer) {
        return mixersSignatureKey.get(mixer);
    }

    public List<RSAPrivateKey> getMixersSignatureKey() {
        return mixersSignatureKey;
    }

    public RSAPublicKey getMixerVerificationKey(int mixer) {
        return mixersVerificationKey.get(mixer);
    }

    public List<RSAPublicKey> getMixersVerificationKey() {
        return mixersVerificationKey;
    }

    public RSAPrivateKey getTallierSignatureKey(int tallier) {
        return talliersSignatureKey.get(tallier);
    }

    public List<RSAPrivateKey> getTalliersSignatureKey() {
        return talliersSignatureKey;
    }

    public RSAPublicKey getTallierVerificationKey(int tallier) {
        return talliersVerificationKey.get(tallier);
    }

    public List<RSAPublicKey> getTalliersVerificationKey() {
        return talliersVerificationKey;
    }

    public DSAPrivateKey getVoterSignatureKey(int voter) {
        return votersSignatureKey.get(voter);
    }

    public List<DSAPrivateKey> getVotersSignatureKey() {
        return votersSignatureKey;
    }

    public DSAPublicKey getVoterVerificationKey(int voter) {
        return votersVerificationKey.get(voter);
    }

    public List<DSAPublicKey> getVotersVerificationKey() {
        return votersVerificationKey;
    }
    
    public DSAPrivateKey getTallierDecryptionKey(int tallier) {
        return talliersDecryptionKey.get(tallier);
    }
    
     public List<DSAPrivateKey> getTalliersDecryptionKey() {
        return talliersDecryptionKey;
    }
    
    public DSAPublicKey getTallierEncryptionKey(int tallier) {
        return talliersEncryptionKey.get(tallier);
    }   
    public List<DSAPublicKey> getTalliersEncryptionKey() {
        return talliersEncryptionKey;
    }
    
    public DSAPrivateKey getBlindedGeneratorKey(int mixer) {
        return blindedGeneratorKey.get(mixer);
    }
}
