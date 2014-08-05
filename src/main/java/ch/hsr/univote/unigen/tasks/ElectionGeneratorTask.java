/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.ElectionGenerator;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.db.DB4O;
import ch.hsr.univote.unigen.krypto.SchnorrSignature;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class ElectionGeneratorTask extends VoteGenerator {

    public void run() throws Exception {
        /*create ElectionGenerator*/
        ElectionGenerator electionGenerator = createElectionGenerator();
        
        /*sign by ElectionManager*/
        electionGenerator.setSignature(new SignatureGenerator().createSignature(electionGenerator, keyStore.electionManagerPrivateKey));

        /*submit to ElectionBoard*/
        electionBoard.setElectionGenerator(electionGenerator);
        
        /*save in db*/
        DB4O.storeDB(config.getElectionId(), electionGenerator);
    }

    private ElectionGenerator createElectionGenerator() {
        ElectionGenerator electionGenerator = new ElectionGenerator();
        electionGenerator.setElectionId(config.getElectionId());
        BigInteger g = electionBoard.getSignatureParameters().getGenerator();

        //Mixers
        for (int i = 0; i < electionBoard.mixers.length; i++) {
            BigInteger keyPair[] = SchnorrSignature.getKeyPair(
                    electionBoard.getSignatureParameters().getPrime(),
                    electionBoard.getSignatureParameters().getGroupOrder(),
                    g);
            keyStore.mixersSignatureKey[i] = keyPair[0];
            keyStore.mixersVerificationKey[i] = keyPair[1];

            g = g.modPow(keyStore.mixersSignatureKey[i], electionBoard.getSignatureParameters().getPrime());

            keyStore.mixersGenerator[i] = g;
        }

        electionGenerator.setGenerator(g);

        return electionGenerator;
    }
}
