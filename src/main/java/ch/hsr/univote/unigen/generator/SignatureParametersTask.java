/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.Signature;
import ch.hsr.univote.unigen.generator.prov.TimestampGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.sp;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.RSAGenerator;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;

/**
 *
 * @author Gian Poltéra
 */
public class SignatureParametersTask {

    public static void run() throws Exception {
        sp.setPrime(BigInteger.TEN);
        sp.setGroupOrder(BigInteger.TEN);
        sp.setGenerator(BigInteger.TEN);
        Signature signature = new Signature();
        RSAPrivateKey privateKey = RSAGenerator.getPrivateKey();
        signature = SignatureGenerator.createSignature(sp, privateKey);
        signature.setSignerId(ConfigHelper.getAdministrationId());
        signature.setTimestamp(TimestampGenerator.generateTimestamp());
        sp.setSignature(signature);
    }
}
