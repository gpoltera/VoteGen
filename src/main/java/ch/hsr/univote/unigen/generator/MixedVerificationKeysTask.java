/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.MixedVerificationKey;
import ch.bfh.univote.common.MixedVerificationKeys;
import ch.bfh.univote.common.Proof;
import ch.bfh.univote.common.Signature;
import ch.bfh.univote.common.VerificationKeys;
import ch.hsr.univote.unigen.generator.prov.TimestampGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.mixers;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.mvk;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.vk;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.RSAGenerator;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;

/**
 *
 * @author Gian Poltéra
 */
public class MixedVerificationKeysTask {

    public static void run() throws Exception {

        vk.setElectionId(ConfigHelper.getElectionId());
        for (int i = 0; i < 100; i++) {
            vk.getKey().add(BigInteger.TEN);
        }

        Signature signature = new Signature();
        RSAPrivateKey privateKey = RSAGenerator.getPrivateKey();
        signature = SignatureGenerator.createSignature(vk, privateKey);
        signature.setSignerId(ConfigHelper.getAdministrationId());
        signature.setTimestamp(TimestampGenerator.generateTimestamp());
        vk.setSignature(signature);
    }
}
