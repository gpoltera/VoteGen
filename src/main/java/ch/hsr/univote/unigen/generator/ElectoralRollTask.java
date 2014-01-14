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
package ch.hsr.univote.unigen.generator;

import ch.hsr.univote.unigen.generator.prov.WahlGenerator;
import ch.bfh.univote.common.ElectoralRoll;
import ch.hsr.univote.unigen.helper.ConfigHelper;
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
public class ElectoralRollTask extends WahlGenerator{

	public static void run() throws FileNotFoundException, FormatException, NoSuchAlgorithmException, Exception {
		List<String> voterIds = new ArrayList<String>();
                
                for (int i = 0; i < ConfigHelper.getVotersNumber(); i++) {
                    voterIds.add("voter" + i + 1);
                }

		ElectoralRoll roll = createRoll(voterIds);
                roll.setSignature(SignatureGenerator.createSignature(roll, electionAdministratorPrivateKey));
                er = roll;
	}

	private static ElectoralRoll createRoll(List<String> voterIds) throws NoSuchAlgorithmException {
            ElectoralRoll roll = new ElectoralRoll();
            roll.setElectionId(ConfigHelper.getElectionId());            
            MessageDigest messageDigest = MessageDigest.getInstance(ConfigHelper.getHashAlgorithm());
            for (String voterId : voterIds) {
                messageDigest.update(voterId.getBytes());
                byte[] digest = messageDigest.digest();
                roll.getVoterHash().add(new BigInteger(digest));
            }
            return roll;
	}
}