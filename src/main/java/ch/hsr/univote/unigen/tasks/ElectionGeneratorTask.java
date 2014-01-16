/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.tasks;

import ch.hsr.univote.unigen.board.ElectionBoard;
import static ch.hsr.univote.unigen.board.ElectionBoard.eg;
import static ch.hsr.univote.unigen.board.ElectionBoard.mixers;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.SchnorrSignature;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class ElectionGeneratorTask extends ElectionBoard {
     public static void run() throws Exception {
        eg.setElectionId(ConfigHelper.getElectionId());
        BigInteger g = signatureParameters.getGenerator();
        
        //Mixers
        for (int i = 0; i < mixers.length; i++) {
            BigInteger keyPair[] = SchnorrSignature.getKeyPair(
                    signatureParameters.getPrime(), 
                    signatureParameters.getGroupOrder(), 
                    g);
            mixersSignatureKey[i] = keyPair[0];
            mixersVerificationKey[i] = keyPair[1];

            g = g.modPow(mixersSignatureKey[i], signatureParameters.getPrime());
            
            mixersGenerator[i] = g;
        }
        
        eg.setGenerator(g);
        eg.setSignature(SignatureGenerator.createSignature(eg, electionManagerPrivateKey));
    }
}