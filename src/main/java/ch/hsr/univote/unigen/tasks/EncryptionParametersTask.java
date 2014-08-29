/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.EncryptionParameters;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.ElGamal;
import ch.hsr.univote.unigen.krypto.RSASignatureGenerator;

/**
 *
 * @author Gian Poltéra
 */
public class EncryptionParametersTask {

    private ConfigHelper config;
    private ElectionBoard electionBoard;
    private KeyStore keyStore;

    public EncryptionParametersTask() {
        this.config = VoteGenerator.config;
        this.electionBoard = VoteGenerator.electionBoard;
        this.keyStore = VoteGenerator.keyStore;

        run();
    }

    //elgamal parameters
    private void run() {
        /*create EncryptionParameters*/
        EncryptionParameters encryptionParameters = createEncryptionParameters();

        /*sign by electionamanger*/
        encryptionParameters.setSignature(new RSASignatureGenerator().createSignature(encryptionParameters, keyStore.getEMSignatureKey()));

        /*submit to ElectionBoard*/
        electionBoard.setEncryptionParameters(encryptionParameters);
    }

    private EncryptionParameters createEncryptionParameters() {
        EncryptionParameters encryptionParameters = new ElGamal().getPublicParameters(config.getEncryptionKeyLength());
        encryptionParameters.setElectionId(config.getElectionId());

        return encryptionParameters;
    }
}
