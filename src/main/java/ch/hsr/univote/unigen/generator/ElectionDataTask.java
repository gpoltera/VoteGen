/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.EncryptionParameters;
import ch.bfh.univote.common.Signature;
import ch.hsr.univote.unigen.generator.prov.TimestampGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.ed;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.edat;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.electionManagerPrivateKey;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.ep;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.sg;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.PrimeGenerator;
import ch.hsr.univote.unigen.krypto.RSA;
import ch.hsr.univote.unigen.krypto.RSASignatur;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;

/**
 *
 * @author Gian
 */
public class ElectionDataTask {
    public static void run() throws Exception {
        edat.setElectionGenerator(PrimeGenerator.getPrime(ConfigHelper.getEncryptionKeyLength()));
        edat.setElectionId(ConfigHelper.getElectionId());
        edat.setEncryptionKey(PrimeGenerator.getPrime(ConfigHelper.getEncryptionKeyLength()));
        edat.setGenerator(PrimeGenerator.getPrime(ConfigHelper.getEncryptionKeyLength()));
        edat.setGroupOrder(PrimeGenerator.getPrime(ConfigHelper.getEncryptionKeyLength()));
        edat.setPrime(PrimeGenerator.getSafePrime(ConfigHelper.getEncryptionKeyLength()));
        edat.setTitle(ConfigHelper.getElectionId());
        //sign by electionamanger
        edat.setSignature(SignatureGenerator.createSignature(edat, electionManagerPrivateKey));
    }
}
