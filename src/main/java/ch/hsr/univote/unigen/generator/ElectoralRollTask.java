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
import ch.bfh.univote.common.Signature;
import ch.hsr.univote.unigen.generator.prov.TimestampGenerator;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.helper.FormatException;
import ch.hsr.univote.unigen.helper.XMLHelper;
import ch.hsr.univote.unigen.krypto.RSAGenerator;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * User interface class to define the electoral roll.
 *
 * @author Stephan Fischli &lt;stephan.fischli@bfh.ch&gt;
 */
public class ElectoralRollTask {

	public static void run() throws FileNotFoundException, FormatException, NoSuchAlgorithmException, Exception {
		List<String> voterIds = getVoters();
		if (!verifyVoters(voterIds)) {
			return;
		}
		ElectoralRoll roll = createRoll(voterIds);
		signRoll(roll);
		writeRoll(roll);
		submitRoll(roll);
	}

	private static List<String> getVoters() throws FileNotFoundException, FormatException {
		File file = new File(ConfigHelper.getElectoralRollFile());
		if (!file.exists() || !file.isFile()) {
			throw new FileNotFoundException("Die Datei " + file + " wurde nicht gefunden");
		}
		List<String> voterIds = new ArrayList<String>();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			Scanner scanner = new Scanner(fis);
			while (scanner.hasNextLine()) {
				String voterId = scanner.nextLine().trim();
				if (voterIds.contains(voterId)) {
					throw new FormatException("Die Waehlendenliste enthaelt den Identifikator " + voterId + " mehrfach");
				}
				voterIds.add(voterId);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
			}
		}
		return voterIds;
	}

	private static boolean verifyVoters(List<String> voterIds) {
		System.out.println();
		System.out.println("--------------------------------------------------");
		for (String voter : voterIds) {
			System.out.println(voter);
		}
		System.out.println("--------------------------------------------------");
		System.out.println("Total: " + voterIds.size());
		Scanner scanner = new Scanner(System.in);
		Boolean result = null;
		while (result == null) {
			System.out.print("Ist diese Waehlendenliste korrekt (ja/nein)? ");
			String answer = scanner.nextLine();
			if (answer.equals("ja")) {
				result = true;
			} else if (answer.equals("nein")) {
				result = false;
			}
		}
		System.out.println();
		return true;
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

	private static void signRoll(ElectoralRoll roll) throws Exception {
                RSAPrivateKey privateKey = RSAGenerator.getPrivateKey();
		// RSAPrivateKey privateKey = KeystoreHelper.getPrivateKey(); -> Original Zeile
		Signature signature = SignatureGenerator.createSignature(roll, privateKey);
		signature.setSignerId(ConfigHelper.getAdministrationId());
                signature.setTimestamp(TimestampGenerator.generateTimestamp());
		roll.setSignature(signature);
	}

	private static void writeRoll(ElectoralRoll roll) throws FileNotFoundException, UnsupportedEncodingException {
            PrintWriter writer = new PrintWriter(ConfigHelper.getElectoralRollPath(), ConfigHelper.getCharEncoding());
            writer.println(XMLHelper.serialize(roll));
            writer.close();
            System.out.println("Die Waehlendenliste wurde in die Datei " + ConfigHelper.getElectoralRollPath() + " geschrieben");
	}

	private static void submitRoll(ElectoralRoll roll) {
            WahlGenerator.addElectionRoll(roll);
	}
}
