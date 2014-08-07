/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.board;

import ch.hsr.univote.unigen.krypto.SchnorrSignatureKey;
import ch.hsr.univote.unigen.krypto.SchnorrVerificationKey;
import java.math.BigInteger;
import java.security.KeyPair;
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

    private static KeyPair caKeyPair;
    private static RSAPrivateKey certificateAuthorityPrivateKey;
    private static RSAPublicKey certificateAuthorityPublicKey;
    private static RSAPrivateKey electionManagerPrivateKey;
    private static RSAPublicKey electionManagerPublicKey;
    private static RSAPrivateKey electionAdministratorPrivateKey;
    private static RSAPublicKey electionAdministratorPublicKey;
    private static List<RSAPrivateKey> mixersPrivateKey = new ArrayList<>();
    private static List<RSAPublicKey> mixersPublicKey = new ArrayList<>();
    private static List<RSAPrivateKey> talliersPrivateKey = new ArrayList<>();
    private static List<RSAPublicKey> talliersPublicKey = new ArrayList<>();
    private static List<RSAPrivateKey> votersPrivateKey = new ArrayList<>();
    private static List<RSAPublicKey> votersPublicKey = new ArrayList<>();
    private static List<BigInteger> talliersDecryptionKey = new ArrayList<>();
    private static List<BigInteger> talliersEncryptionKey = new ArrayList<>();
    private static List<BigInteger> mixersSignatureKey = new ArrayList<>();
    private static List<BigInteger> mixersVerificationKey = new ArrayList<>();
    private static List<BigInteger> mixersGenerator = new ArrayList<>();
    private static List<SchnorrSignatureKey> votersSignatureKey = new ArrayList<>();
    private static List<SchnorrVerificationKey> votersVerificationKey = new ArrayList<>();

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
        KeyStore.mixersPrivateKey.add(mixer, mixerPrivateKey);
    }

    public void setMixerPublicKey(int mixer, RSAPublicKey mixerPublicKey) {
        KeyStore.mixersPublicKey.add(mixer, mixerPublicKey);
    }

    public void setTallierPrivateKey(int tallier, RSAPrivateKey tallierPrivateKey) {
        KeyStore.talliersPrivateKey.add(tallier, tallierPrivateKey);
    }

    public void setTallierPublicKey(int tallier, RSAPublicKey tallierPublicKey) {
        KeyStore.talliersPublicKey.add(tallier, tallierPublicKey);
    }

    public void setVoterPrivateKey(int voter, RSAPrivateKey voterPrivateKey) {
        KeyStore.votersPrivateKey.add(voter, voterPrivateKey);
    }

    public void setVoterPublicKey(int voter, RSAPublicKey voterPublicKey) {
        KeyStore.votersPublicKey.add(voter, voterPublicKey);
    }

    public void setTallierDecryptionKey(int tallier, BigInteger tallierDecryptionKey) {
        KeyStore.talliersDecryptionKey.add(tallier, tallierDecryptionKey);
    }

    public void setTallierEncryptionKey(int tallier, BigInteger tallierEncryptionKey) {
        KeyStore.talliersEncryptionKey.add(tallier, tallierEncryptionKey);
    }

    public void setMixerSignatureKey(int mixer, BigInteger mixerSignatureKey) {
        KeyStore.mixersSignatureKey.add(mixer, mixerSignatureKey);
    }

    public void setMixerVerificationKey(int mixer, BigInteger mixerVerificationKey) {
        KeyStore.mixersVerificationKey.add(mixer, mixerVerificationKey);
    }

    public void setMixerGenerator(int mixer, BigInteger mixerGenerator) {
        KeyStore.mixersGenerator.add(mixer, mixerGenerator);
    }

    public void setVoterSignatureKey(int voter, SchnorrSignatureKey voterSignatureKey) {
        KeyStore.votersSignatureKey.add(voter, voterSignatureKey);
    }

    public void setVoterVerificationKey(int voter, SchnorrVerificationKey voterVerificationKey) {
        KeyStore.votersVerificationKey.add(voter, voterVerificationKey);
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
        return mixersPrivateKey.get(mixer);
    }

    public List<RSAPrivateKey> getMixersPrivateKey() {
        return mixersPrivateKey;
    }

    public RSAPublicKey getMixerPublicKey(int mixer) {
        return mixersPublicKey.get(mixer);
    }

    public List<RSAPublicKey> getMixersPublicKey() {
        return mixersPublicKey;
    }

    public RSAPrivateKey getTallierPrivateKey(int tallier) {
        return talliersPrivateKey.get(tallier);
    }

    public List<RSAPrivateKey> getTalliersPrivateKey() {
        return talliersPrivateKey;
    }

    public RSAPublicKey getTallierPublicKey(int tallier) {
        return talliersPublicKey.get(tallier);
    }

    public List<RSAPublicKey> getTalliersPublicKey() {
        return talliersPublicKey;
    }

    public RSAPrivateKey getVoterPrivateKey(int voter) {
        return votersPrivateKey.get(voter);
    }

    public List<RSAPrivateKey> getVotersPrivateKey() {
        return votersPrivateKey;
    }

    public RSAPublicKey getVoterPublicKey(int voter) {
        return votersPublicKey.get(voter);
    }

    public List<RSAPublicKey> getVotersPublicKey() {
        return votersPublicKey;
    }

    public BigInteger getTallierDecryptionKey(int tallier) {
        return talliersDecryptionKey.get(tallier);
    }

    public List<BigInteger> getTalliersDecryptionKey() {
        return talliersDecryptionKey;
    }

    public BigInteger getTallierEncryptionKey(int tallier) {
        return talliersEncryptionKey.get(tallier);
    }

    public List<BigInteger> getTalliersEncryptionKey() {
        return talliersEncryptionKey;
    }

    public BigInteger getMixerSignatureKey(int mixer) {
        return mixersSignatureKey.get(mixer);
    }

    public List<BigInteger> getMixersSignatureKey() {
        return mixersSignatureKey;
    }

    public BigInteger getMixerVerificationKey(int mixer) {
        return mixersVerificationKey.get(mixer);
    }

    public List<BigInteger> getMixersVerificationKey() {
        return mixersVerificationKey;
    }

    public BigInteger getMixerGenerator(int mixer) {
        return mixersGenerator.get(mixer);
    }

    public List<BigInteger> getMixersGenerator() {
        return mixersGenerator;
    }

    public SchnorrSignatureKey getVoterSignatureKey(int voter) {
        return votersSignatureKey.get(voter);
    }

    public List<SchnorrSignatureKey> getVotersSignatureKey() {
        return votersSignatureKey;
    }

    public SchnorrVerificationKey getVoterVerificationKey(int voter) {
        return votersVerificationKey.get(voter);
    }

    public List<SchnorrVerificationKey> getVotersVerificationKey() {
        return votersVerificationKey;
    }
}
