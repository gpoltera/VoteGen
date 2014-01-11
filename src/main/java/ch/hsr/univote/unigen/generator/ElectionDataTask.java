/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.Choice;
import ch.bfh.univote.common.Rule;
import ch.hsr.univote.unigen.generator.prov.WahlGenerator;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.PrimeGenerator;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian
 */
public class ElectionDataTask extends WahlGenerator {
    
    public static void run() throws Exception {
        edat.setElectionGenerator(PrimeGenerator.getPrime(ConfigHelper.getEncryptionKeyLength()));
        edat.setElectionId(ConfigHelper.getElectionId());
        edat.setEncryptionKey(PrimeGenerator.getPrime(ConfigHelper.getEncryptionKeyLength()));
        edat.setGenerator(PrimeGenerator.getPrime(ConfigHelper.getEncryptionKeyLength()));
        edat.setGroupOrder(PrimeGenerator.getPrime(ConfigHelper.getEncryptionKeyLength()));
        edat.setPrime(PrimeGenerator.getSafePrime(ConfigHelper.getEncryptionKeyLength()));
        edat.setTitle(ConfigHelper.getElectionId());
        edat.getChoice().addAll(eo.getChoice());
        edat.getRule().addAll(eo.getRule());

        //sign by electionamanger
        edat.setSignature(SignatureGenerator.createSignature(edat, electionManagerPrivateKey));
    }
}
