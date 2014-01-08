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
import ch.bfh.univote.common.ElectionDefinition;
import ch.bfh.univote.common.Signature;
import ch.hsr.univote.unigen.generator.prov.TimestampGenerator;
import ch.hsr.univote.unigen.krypto.RSAGenerator;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.helper.KeystoreHelper;
import ch.hsr.univote.unigen.helper.XMLHelper;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.interfaces.RSAPrivateKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

/**
 * User interface class to define an election.
 *
 * @author Stephan Fischli &lt;stephan.fischli@bfh.ch&gt;
 */
public class ElectionDefinitionTask {

    public static void run() throws Exception {
        ElectionDefinition definition = createDefinition();
        if (!verifyDefinition(definition)) {
            return;
        }
        signDefinition(definition);
        writeDefinition(definition);
        submitDefinition(definition);
    }

    private static ElectionDefinition createDefinition() {
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            DatatypeFactory factory = DatatypeFactory.newInstance();
            ElectionDefinition definition = new ElectionDefinition();
            definition.setElectionId(ConfigHelper.getElectionId());
            definition.setTitle(ConfigHelper.getElectionTitle());
            calendar.setTime(ConfigHelper.getVotingPhaseBegin());
            definition.setVotingPhaseBegin(factory.newXMLGregorianCalendar(calendar).normalize());
            calendar.setTime(ConfigHelper.getVotingPhaseEnd());
            definition.setVotingPhaseEnd(factory.newXMLGregorianCalendar(calendar).normalize());
            definition.getMixerId().addAll(Arrays.asList(ConfigHelper.getMixerIds()));
            definition.getTallierId().addAll(Arrays.asList(ConfigHelper.getTallierIds()));
            definition.setKeyLength(ConfigHelper.getEncryptionKeyLength());
            return definition;
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean verifyDefinition(ElectionDefinition definition) {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        System.out.println("--------------------------------------------------");
        System.out.println("Wahlereignis:          " + definition.getElectionId());
        System.out.println("Bezeichnung:           " + definition.getTitle());
        Date votingPhaseBegin = definition.getVotingPhaseBegin().toGregorianCalendar().getTime();
        System.out.println("Beginn Wahlphase:      " + df.format(votingPhaseBegin));
        Date votingPhaseEnd = definition.getVotingPhaseEnd().toGregorianCalendar().getTime();
        System.out.println("Ende Wahlphase:        " + df.format(votingPhaseEnd));
        for (String id : definition.getMixerId()) {
            System.out.println("Identifikator Mixer:   " + id);
        }
        for (String id : definition.getTallierId()) {
            System.out.println("Identifikator Tallier: " + id);
        }
        System.out.println("Schluessellaenge:      " + definition.getKeyLength());
        System.out.println("--------------------------------------------------");
        Scanner scanner = new Scanner(System.in);
        Boolean result = null;
        while (result == null) {
            System.out.print("Ist diese Definition korrekt (ja/nein)? ");
            String answer = scanner.nextLine();
            if (answer.equals("ja")) {
                result = true;
            }
            if (answer.equals("nein")) {
                result = false;
            }
        }
        System.out.println();
        return result;
    }

    private static void signDefinition(ElectionDefinition definition) throws Exception {
        RSAPrivateKey privateKey = KeystoreHelper.getPrivateKey();
        Signature signature = SignatureGenerator.createSignature(definition, privateKey);
        signature.setTimestamp(TimestampGenerator.generateTimestamp());
        signature.setSignerId(ConfigHelper.getAdministrationId());
        definition.setSignature(signature);
    }

    private static void writeDefinition(ElectionDefinition definition) {
        try {
            PrintWriter writer = new PrintWriter(ConfigHelper.getElectionDefinitionPath(), ConfigHelper.getCharEncoding());
            writer.println(XMLHelper.serialize(definition));
            writer.close();
            System.out.println("Die Wahldefinition wurde in die Datei " + ConfigHelper.getElectionDefinitionPath() + " geschrieben");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void submitDefinition(ElectionDefinition definition) {
        WahlGenerator.addElectionDefinition(definition);
    }
}
