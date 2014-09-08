/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.ElectionGenerator;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.crypto.RSASignatureGenerator;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class ElectionGeneratorTask {

    private ConfigHelper config;
    private ElectionBoard electionBoard;
    private KeyStore keyStore;

    public ElectionGeneratorTask() {
        this.config = VoteGenerator.config;
        this.electionBoard = VoteGenerator.electionBoard;
        this.keyStore = VoteGenerator.keyStore;
        
        run();
    }

    /*1.3.4 e) Constructing the Election Generator*/
    private void run() {
        /*create ElectionGenerator*/
        ElectionGenerator electionGenerator = createElectionGenerator();

        /*sign by ElectionManager*/
        electionGenerator.setSignature(new RSASignatureGenerator().createSignature(electionGenerator, keyStore.getEMSignatureKey()));

        /*submit to ElectionBoard*/
        electionBoard.setElectionGenerator(electionGenerator);
    }

    private ElectionGenerator createElectionGenerator() {
        ElectionGenerator electionGenerator = new ElectionGenerator();
        electionGenerator.setElectionId(config.getElectionId());        
        electionGenerator.setGenerator(electionBoard.getBlindedGenerator(electionBoard.mixers[electionBoard.mixers.length - 1]).getGenerator());

        return electionGenerator;
    }
}
