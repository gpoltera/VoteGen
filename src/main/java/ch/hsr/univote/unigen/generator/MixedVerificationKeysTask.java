/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.Signature;
import ch.hsr.univote.unigen.generator.prov.WahlGenerator;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class MixedVerificationKeysTask extends WahlGenerator {

    public static void run() throws Exception {

        vk.setElectionId(ConfigHelper.getElectionId());
        for (int i = 0; i < ConfigHelper.getVotersNumber(); i++) {
            vk.getKey().add(BigInteger.TEN);
        }
        vk.setSignature(SignatureGenerator.createSignature(vk, electionManagerPrivateKey));
    }
}
