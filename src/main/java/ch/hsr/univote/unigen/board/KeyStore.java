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
    ConfigHelper config = new ConfigHelper();
    
    public final String[] mixers = config.getMixerIds();
    public final String[] talliers = config.getTallierIds();
    
    public KeyPair caKeyPair;
    public RSAPrivateKey certificateAuthorityPrivateKey;
    public RSAPublicKey certificateAuthorityPublicKey;

    public RSAPrivateKey electionManagerPrivateKey;
    public RSAPublicKey electionManagerPublicKey;

    public RSAPrivateKey electionAdministratorPrivateKey;
    public RSAPublicKey electionAdministratorPublicKey;

    public RSAPrivateKey[] mixersPrivateKey = new RSAPrivateKey[mixers.length];
    public RSAPublicKey[] mixersPublicKey = new RSAPublicKey[mixers.length];

    public RSAPrivateKey[] talliersPrivateKey = new RSAPrivateKey[talliers.length];
    public RSAPublicKey[] talliersPublicKey = new RSAPublicKey[talliers.length];

    public RSAPrivateKey[] votersPrivateKey = new RSAPrivateKey[config.getVotersNumber()];
    public RSAPublicKey[] votersPublicKey = new RSAPublicKey[config.getVotersNumber()];
    
    public BigInteger[] talliersDecryptionKey = new BigInteger[talliers.length];
    public BigInteger[] talliersEncryptionKey = new BigInteger[talliers.length];

    public BigInteger[] mixersSignatureKey = new BigInteger[mixers.length];
    public BigInteger[] mixersVerificationKey = new BigInteger[mixers.length];
    public BigInteger[] mixersGenerator = new BigInteger[mixers.length];
    
    public BigInteger[] votersSignatureKey = new BigInteger[config.getVotersNumber()];
    public BigInteger[] votersVerificationKey = new BigInteger[config.getVotersNumber()];
}
