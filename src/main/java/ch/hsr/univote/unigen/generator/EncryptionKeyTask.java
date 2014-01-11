/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.EncryptionKey;
import ch.hsr.univote.unigen.generator.prov.WahlGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.ek;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.ep;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class EncryptionKeyTask extends WahlGenerator {
    public static void run() throws Exception {
        EncryptionKey encryptionKey = new EncryptionKey();
        encryptionKey.setElectionId(ConfigHelper.getElectionId());
        
        BigInteger a = new BigInteger("77");
        BigInteger A = ep.getGenerator().modPow(a, ep.getPrime());
        
        encryptionKey.setKey(A);
        ek = encryptionKey;
        ek.setSignature(SignatureGenerator.createSignature(encryptionKey, electionManagerPrivateKey));
    }
}
