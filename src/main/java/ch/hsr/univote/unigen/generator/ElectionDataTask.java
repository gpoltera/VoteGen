/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.generator;

import ch.hsr.univote.unigen.generator.prov.WahlGenerator;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.PrimeGenerator;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;

/**
 *
 * @author Gian
 */
public class ElectionDataTask extends WahlGenerator {
    
    public static void run() throws Exception {
        edat.setElectionGenerator(eg.getGenerator());
        edat.setElectionId(ConfigHelper.getElectionId());
        edat.setEncryptionKey(ek.getKey());
        edat.setGenerator(encryptionParameters.getGenerator());
        edat.setGroupOrder(encryptionParameters.getGroupOrder());
        edat.setPrime(encryptionParameters.getPrime());
        edat.setTitle(ConfigHelper.getElectionId());
        edat.getChoice().addAll(eo.getChoice());
        edat.getRule().addAll(eo.getRule());

        //sign by electionamanger
        edat.setSignature(SignatureGenerator.createSignature(edat, electionManagerPrivateKey));
    }
}
