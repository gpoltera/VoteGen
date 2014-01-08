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
package ch.hsr.univote.unigen.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

/**
 * Configuration helper class.
 *
 * @author Stephan Fischli &lt;stephan.fischli@bfh.ch&gt;
 */
public class ConfigHelper {

    private static final String SYSTEM_CONFIG_FILE = "system.properties";
    private static final String ADMIN_CONFIG_FILE = "admin.properties";
    private static Properties properties;
    private static Scanner scanner = new Scanner(System.in);
    private static DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public static String getAdministrationId() {
        return getProperty("administrationId", "Identifikator der Wahladministration");
    }

    public static String getElectionId() {
        return getProperty("electionId", "Identifikator des Wahlereignis");
    }

    public static String getElectionTitle() {
        return getProperty("electionTitle", "Bezeichnung des Wahlereignis");
    }

    public static Date getVotingPhaseBegin() {
        return getDateProperty("votingPhaseBegin", "Beginn der Wahlphase");
    }

    public static Date getVotingPhaseEnd() {
        return getDateProperty("votingPhaseEnd", "Ende der Wahlphase");
    }

    public static String[] getMixerIds() {
        return getListProperty("mixerIds", "Identifikator der Mixer");
    }

    public static String[] getTallierIds() {
        return getListProperty("tallierIds", "Identifikator der Tallier");
    }

    public static int getEncryptionKeyLength() {
        return getIntProperty("encryptionKeyLength", "Laenge des Verschluesselungsschluessels");
    }

    public static String getElectionDefinitionPath() {
        return getProperty("j", "Pfad der signierten Wahldefinition");
    }

    public static String getPoliticalListsFile() {
        return getProperty("politicalListsFile", "Pfad der Kandidierendenlisten");
    }

    public static int getMaxCandidates() {
        return getIntProperty("maxCandidates", "Maximale Anzahl waehlbarer Kandidaten");
    }

    public static int getMaxCumulation() {
        return getIntProperty("maxCumulation", "Zulaessige Anzahl Kumulationen eines Kandidaten");
    }

    public static String getElectionOptionsPath() {
        return getProperty("electionOptionsPath", "Pfad der signierten Kandidierendenlisten");
    }

    public static String getElectoralRollFile() {
        return getProperty("electoralRollFile", "Pfad der Waehlendenliste");
    }

    public static String getHashAlgorithm() {
        return getProperty("hashAlgorithm", "Hash-Algorithmus fuer die Waehlendenliste");
    }

    public static String getElectoralRollPath() {
        return getProperty("electoralRollPath", "Pfad der signierten Waehlendenliste");
    }

    public static String getElectionResultsPath() {
        return getProperty("electionResultsPath", "Pfad der Wahlresultatliste");
    }

    public static String getValueDelimiter() {
        return getProperty("valueDelimiter", "Trennzeichen der Wahlresultatwerte");
    }

    public static String getKeystorePath() {
        return getProperty("keystorePath", "Pfad des Keystores mit dem Signaturschluessel");
    }

    public static String getKeystorePassword() {
        return getProperty("keystorePassword", "Passwort des Keystores mit dem Signaturschluessel");
    }

    public static String getSignatureKeyType() {
        return getProperty("signatureKeyType", "Typ des Signaturschluessels");
    }

    public static int getSignatureKeyLength() {
        return getIntProperty("signatureKeyLength", "Laenge des Signaturschluessels");
    }

    public static String getSignatureKeyAlias() {
        return getProperty("signatureKeyAlias", "Alias des Signaturschluessels");
    }

    public static String getSignatureKeyName() {
        return getProperty("signatureKeyName", "Name des Signaturschluessels");
    }

    public static int getSignatureKeyValidity() {
        return getIntProperty("signatureKeyValidity", "Gueltigkeitsdauer des Signaturschluessels");
    }

    public static String getSignatureAlgorithm() {
        return getProperty("signatureAlgorithm", "Signaturalgorithmus des Zertifikats zum Signaturschluessel");
    }

    public static String getSignatureCertificatePath() {
        return getProperty("signatureCertificatePath", "Pfad des Zertifikats zum Signaturalgorithmus");
    }

    public static String getAdminServiceAddress() {
        return getProperty("adminServiceAddress", "Webservice-Adresse der Wahladministration");
    }

    public static String getBoardServiceAddress() {
        return getProperty("boardServiceAddress", "Webservice-Adresse des Wahlanschlagbretts");
    }

    public static String getTruststorePath() {
        return getProperty("truststorePath", "Pfad des Truststores mit dem Webservice-Zertifikat");
    }

    public static String getTruststorePassword() {
        return getProperty("truststorePassword", "Passwort des Truststores mit dem Webservice-Zertifikat");
    }

    public static String getCharEncoding() {
        return getProperty("charEncoding", "Zeichencodierung");
    }

    public static String getLogfilePath() {
        return getProperty("logfilePath", "Pfad des Logfiles");
    }

    public static String getBallotsPath() {
        return getProperty("ballotsPath", "Pfad der Wahlzettel");
    }

    public static String getDecodedVotesPath() {
        return getProperty("decodedVotesPath", "Pfad der entschluesselten Wahlzettel");
    }

    public static String getDecryptedVotesPath() {
        return getProperty("decryptedVotesPath", "Pfad der entschluesselten Wahlzettel");
    }

    public static String getElectionGeneratorPath() {
        return getProperty("electionGeneratorPath", "Pfad des ElectionGenerator");
    }

    public static String getBlindedGeneratorPath() {
        return getProperty("blindedGeneratorPath", "Pfad des BlindedGenerator");
    }

    public static String getEncryptionKeySharePath() {
        return getProperty("encryptionKeySharePath", "Pfad des EncryptionKeyShare");
    }

    public static int getVotersNumber() {
        return getIntProperty("voters", "Anzahl der Waehlenden");
    }

    public static boolean getPartyListSystemIndicator() {
        String response = getProperty("partyListSystem", "Listenwahl");
        boolean rval = false;
        if (response.toLowerCase().startsWith("true")
                || response.toLowerCase().startsWith("yes")
                || response.toLowerCase().startsWith("ja")
                || response.toLowerCase().startsWith("oui")) {
            rval = true;
        }
        return rval;
    }

    private static String getProperty(String key, String label) {
        String value = getProperties().getProperty(key);
        while (value == null || value.isEmpty()) {
            System.out.print(label + ": ");
            value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                properties.put(key, value);
            }
        }
        return value;
    }

    private static int getIntProperty(String key, String label) {
        String value = getProperty(key, label + " (ganze Zahl)");
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ConfigException(label + ": " + value + " ist keine ganze Zahl");
        }
    }

    private static Date getDateProperty(String key, String label) {
        String value = getProperty(key, label + " (Format TT.MM.JJJJ hh:mm)");
        try {
            return dateFormat.parse(value);
        } catch (ParseException e) {
            throw new ConfigException(label + ": " + value + " ist kein gueltiges Datum");
        }
    }

    private static String[] getListProperty(String key, String label) {
        String value = getProperty(key, label + " (Komma-separiert ohne Leerstellen)");
        if (value.matches("\\S+(,\\S+)*")) {
            return value.split(",");
        }
        throw new ConfigException(label + ": " + value + " ist keine gueltige Liste");
    }

    private static Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            try {
                properties.load(new FileInputStream(SYSTEM_CONFIG_FILE));
            } catch (Exception e) {
                throw new ConfigException("Die Konfigurationsdatei " + SYSTEM_CONFIG_FILE + " konnte nicht gelesen werden", e);
            }
            try {
                properties.load(new FileInputStream(ADMIN_CONFIG_FILE));
            } catch (IOException e) {
                throw new ConfigException("Die Konfigurationsdatei " + ADMIN_CONFIG_FILE + " konnte nicht gelesen werden", e);
            }
        }
        return properties;
    }
}
