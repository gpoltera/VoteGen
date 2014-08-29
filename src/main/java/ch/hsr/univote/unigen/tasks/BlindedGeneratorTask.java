/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.BlindedGenerator;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.NIZKP;
import ch.hsr.univote.unigen.krypto.RSASignatureGenerator;
import ch.hsr.univote.unigen.krypto.Schnorr;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.interfaces.DSAPrivateKey;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian Poltéra
 */
public class BlindedGeneratorTask {

    private ConfigHelper config;
    private ElectionBoard electionBoard;
    private KeyStore keyStore;

    private List<BlindedGenerator> blindedGeneratorsList;
    private BigInteger p, g_k;

    /*1.3.4 e) Constructing the Election Generator*/
    public BlindedGeneratorTask() {
        
        this.config = VoteGenerator.config;
        this.electionBoard = VoteGenerator.electionBoard;
        this.keyStore = VoteGenerator.keyStore;
        
        run();
    }

    private void run() {
        blindedGeneratorsList = new ArrayList<>();
        p = electionBoard.getSignatureParameters().getPrime();
        g_k = electionBoard.getSignatureParameters().getGenerator();
        
        /*for each mixer*/
        for (int k = 0; k < electionBoard.mixers.length; k++) {
            /*create BlindedGenerator*/
            BlindedGenerator blindedGenerator = createBlindedGenerator(k);            

            /*add to list*/
            blindedGeneratorsList.add(k, blindedGenerator);
        }
        /*submit to ElectionBoard*/
        electionBoard.setBlindedGeneratorList(blindedGeneratorsList);
    }

    private BlindedGenerator createBlindedGenerator(int k) {
        BlindedGenerator blindedGenerator = new BlindedGenerator();
        blindedGenerator.setElectionId(config.getElectionId());
        
        BigInteger previous_g_k = g_k;
        createMixerKeys(k);
        g_k = g_k.modPow(keyStore.getBlindedGeneratorKey(k).getX(), p);

        blindedGenerator.setGenerator(g_k);
        blindedGenerator.setProof(new NIZKP().getBlindedGeneratorProof(
                electionBoard.mixers[k],
                g_k,
                previous_g_k,
                keyStore.getBlindedGeneratorKey(k)));
        blindedGenerator.setSignature(new RSASignatureGenerator().createSignature(electionBoard.mixers[k], blindedGenerator, keyStore.getMixerSignatureKey(k)));
        
        return blindedGenerator;
    }
    
    private void createMixerKeys(int k) {
        KeyPair keyPair = new Schnorr().getKeyPair(electionBoard.getSignatureParameters());
        keyStore.setBlindedGeneratorKey(k, (DSAPrivateKey) keyPair.getPrivate());
    }
}
