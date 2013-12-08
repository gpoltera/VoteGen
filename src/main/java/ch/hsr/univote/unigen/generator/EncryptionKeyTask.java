/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.EncryptionKey;
import ch.bfh.univote.common.Signature;
import ch.hsr.univote.unigen.generator.prov.TimestampGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.ek;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.RSAGenerator;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;

/**
 *
 * @author Gian Poltéra
 */
public class EncryptionKeyTask {
    public static void run() throws Exception {
        EncryptionKey encryptionKey = new EncryptionKey();
        encryptionKey.setElectionId(ConfigHelper.getElectionId());
        encryptionKey.setKey(BigInteger.TEN);
        ek = encryptionKey;
        signEncryptioKey(encryptionKey);
    }
    
    private static void signEncryptioKey(EncryptionKey encryptionKey) throws Exception {
        RSAPrivateKey privateKey = RSAGenerator.getPrivateKey();
        Signature signature = SignatureGenerator.createSignature(encryptionKey, privateKey);
        signature.setSignerId(ConfigHelper.getAdministrationId());
        signature.setTimestamp(TimestampGenerator.generateTimestamp());
        ek.setSignature(signature);
    }
}
