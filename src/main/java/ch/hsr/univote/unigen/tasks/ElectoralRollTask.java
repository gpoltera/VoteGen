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
import ch.hsr.univote.unigen.db.DB4O;
import ch.hsr.univote.unigen.helper.FormatException;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
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
public class ElectoralRollTask extends VoteGenerator {

    public void run() throws FileNotFoundException, FormatException, NoSuchAlgorithmException, Exception {
        List<String> voterIds = new ArrayList<>();

        for (int i = 0; i < config.getVotersNumber(); i++) {
            voterIds.add("voter" + i + 1);
        }

        /*create ElectoralRoll*/
        ElectoralRoll electoralRoll = createRoll(voterIds);
        
        /*sign by ElectionAdministrator*/
        electoralRoll.setSignature(new SignatureGenerator().createSignature(electoralRoll, keyStore.electionAdministratorPrivateKey));
        
        /*submit to ElectionBoard*/
        electionBoard.setElectoralRoll(electoralRoll);

        /*save in db*/
        DB4O.storeDB(config.getElectionId(), electoralRoll);
    }

    private ElectoralRoll createRoll(List<String> voterIds) {
        try {
            ElectoralRoll electoralRoll = new ElectoralRoll();
            electoralRoll.setElectionId(config.getElectionId());
            MessageDigest messageDigest = MessageDigest.getInstance(config.getHashAlgorithm());
            for (String voterId : voterIds) {
                messageDigest.update(voterId.getBytes());
                byte[] digest = messageDigest.digest();
                electoralRoll.getVoterHash().add(new BigInteger(digest));
            }
            return electoralRoll;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
