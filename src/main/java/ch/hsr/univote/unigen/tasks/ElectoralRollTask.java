 /*
 * Copyright (c) 2012 Berner Fachhochschule, Switzerland.
 * Bern University of Applied Sciences, Engineering and Information Technology,
 * Research Institute for Security in the Information Society, E-Voting Group,
 * Biel, Switzerland.
 *
 * Project UniVote.
 *
 * Distributable under GPL license.
 * See terms of license at gnu.org.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.ElectoralRoll;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.helper.FormatException;
import ch.hsr.univote.unigen.crypto.RSASignatureGenerator;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * User interface class to define the electoral roll.
 *
 * @author Stephan Fischli &lt;stephan.fischli@bfh.ch&gt;
 */
public class ElectoralRollTask {

    private ConfigHelper config;
    private ElectionBoard electionBoard;
    private KeyStore keyStore;

    public ElectoralRollTask() {
        this.config = VoteGenerator.config;
        this.electionBoard = VoteGenerator.electionBoard;
        this.keyStore = VoteGenerator.keyStore;

        run();
    }

    private void run() {
        List<String> voterIds = new ArrayList<>();

        for (int i = 0; i < config.getVotersNumber(); i++) {
            voterIds.add("voter" + i + 1);
        }

        /*create ElectoralRoll*/
        ElectoralRoll electoralRoll = createRoll(voterIds);

        /*sign by ElectionAdministrator*/
        electoralRoll.setSignature(new RSASignatureGenerator().createSignature(electoralRoll, keyStore.getEASignatureKey()));

        /*submit to ElectionBoard*/
        electionBoard.setElectoralRoll(electoralRoll);
    }

    private ElectoralRoll createRoll(List<String> voterIds) {
        ElectoralRoll electoralRoll = new ElectoralRoll();
        try {
            electoralRoll.setElectionId(config.getElectionId());
            MessageDigest messageDigest = MessageDigest.getInstance(config.getHashAlgorithm());
            for (String voterId : voterIds) {
                messageDigest.update(voterId.getBytes());
                byte[] digest = messageDigest.digest();
                electoralRoll.getVoterHash().add(new BigInteger(digest));
            }
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        
        return electoralRoll;
    }
}