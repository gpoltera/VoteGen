/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.ElectionGenerator;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.krypto.RSASignatureGenerator;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class ElectionGeneratorTask extends VoteGenerator {

    /*1.3.4 e) Constructing the Election Generator*/
    public void run() throws Exception {
        /*create ElectionGenerator*/
        ElectionGenerator electionGenerator = createElectionGenerator();
        
        /*sign by ElectionManager*/
        electionGenerator.setSignature(new RSASignatureGenerator().createSignature(electionGenerator, keyStore.getElectionManagerPrivateKey()));

        /*submit to ElectionBoard*/
        electionBoard.setElectionGenerator(electionGenerator);
    }

    private ElectionGenerator createElectionGenerator() {
        ElectionGenerator electionGenerator = new ElectionGenerator();
        electionGenerator.setElectionId(config.getElectionId());
        BigInteger p = electionBoard.getSignatureParameters().getPrime();
        BigInteger g = electionBoard.getSignatureParameters().getGenerator();

        //for each mixer
        for (int k = 0; k < electionBoard.mixers.length; k++) {
            BigInteger signatureKey = keyStore.getMixerSignatureKey(k);
            g = g.modPow(signatureKey, p);
            System.out.println("Mixer " + k + " Generator: " + g);
            keyStore.setMixerGenerator(k, g);
        }

        electionGenerator.setGenerator(g);

        return electionGenerator;
    }
}