/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.Signature;
import ch.hsr.univote.unigen.generator.prov.TimestampGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.ed;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.edat;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.sg;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.PrimeGenerator;
import ch.hsr.univote.unigen.krypto.RSAGenerator;
import ch.hsr.univote.unigen.krypto.RSASignatur;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;

/**
 *
 * @author Gian
 */
public class ElectionDataTask {
    public static void run() throws Exception {
        edat.setElectionGenerator(new BigInteger("2323"));
        edat.setElectionId(ConfigHelper.getElectionId());
        edat.setEncryptionKey(new BigInteger("23243"));
        edat.setGenerator(new BigInteger("4656"));
        edat.setGroupOrder(new BigInteger("4567"));
        edat.setPrime(PrimeGenerator.getPrime(1024));
        RSAPrivateKey privateKey = RSAGenerator.getPrivateKey();
        Signature signature = SignatureGenerator.createSignature(edat, privateKey);
        signature.setSignerId(ConfigHelper.getAdministrationId());
        signature.setTimestamp(TimestampGenerator.generateTimestamp());
        edat.setSignature(signature);
        edat.setTitle(ConfigHelper.getElectionId());  
    }
}
