/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.board;

import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 *
 * @author Gian
 */
public class KeyStore {
    public static final String[] mixers = ConfigHelper.getMixerIds();
    public static final String[] talliers = ConfigHelper.getTallierIds();
    
    public static KeyPair caKeyPair;
    public static RSAPrivateKey certificateAuthorityPrivateKey;
    public static RSAPublicKey certificateAuthorityPublicKey;

    public static RSAPrivateKey electionManagerPrivateKey;
    public static RSAPublicKey electionManagerPublicKey;

    public static RSAPrivateKey electionAdministratorPrivateKey;
    public static RSAPublicKey electionAdministratorPublicKey;

    public static RSAPrivateKey[] mixersPrivateKey = new RSAPrivateKey[mixers.length];
    public static RSAPublicKey[] mixersPublicKey = new RSAPublicKey[mixers.length];

    public static RSAPrivateKey[] talliersPrivateKey = new RSAPrivateKey[talliers.length];
    public static RSAPublicKey[] talliersPublicKey = new RSAPublicKey[talliers.length];

    public static RSAPrivateKey[] votersPrivateKey = new RSAPrivateKey[ConfigHelper.getVotersNumber()];
    public static RSAPublicKey[] votersPublicKey = new RSAPublicKey[ConfigHelper.getVotersNumber()];
    
    public static BigInteger[] talliersDecryptionKey = new BigInteger[talliers.length];
    public static BigInteger[] talliersEncryptionKey = new BigInteger[talliers.length];

    public static BigInteger[] mixersSignatureKey = new BigInteger[mixers.length];
    public static BigInteger[] mixersVerificationKey = new BigInteger[mixers.length];
    public static BigInteger[] mixersGenerator = new BigInteger[mixers.length];
    
    public static BigInteger[] votersSignatureKey = new BigInteger[ConfigHelper.getVotersNumber()];
    public static BigInteger[] votersVerificationKey = new BigInteger[ConfigHelper.getVotersNumber()];
}
