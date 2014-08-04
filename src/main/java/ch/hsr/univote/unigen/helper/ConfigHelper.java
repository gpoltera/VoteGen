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

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Configuration helper class.
 *
 * @author Stephan Fischli &lt;stephan.fischli@bfh.ch&gt;
 */
public class ConfigHelper {

    private final String SYSTEM_CONFIG_FILE = "SystemConfigFile";
    private final String CRYPTO_CONFIG_FILE = "CryptoConfigFile";
    private final String FAULT_CONFIG_FILE = "FaultConfigFile";
    private Properties properties;
    private Scanner scanner = new Scanner(System.in);
    private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public void saveProperties(String configname, Properties properties) {
        try {
            BufferedOutputStream streamout = new BufferedOutputStream(new FileOutputStream("properties/" + configname + ".properties"));
            properties.store(streamout, configname);
            streamout.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConfigHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getCertificateAuthorityId() {
        return getProperty("certificateAuthorityId", "Identifikator der ZertifikatAuthority");
    }

    public String getElectionManagerId() {
        return getProperty("electionManagerId", "Identifikator des Wahlmanagers");
    }

    public String getElectionAdministratorId() {
        return getProperty("electionAdministratorId", "Identifikator der Wahladministration");
    }

    public String getElectionId() {
        return getProperty("electionId", "Identifikator des Wahlereignis");
    }

    public String getElectionTitle() {
        return getProperty("electionTitle", "Bezeichnung des Wahlereignis");
    }

    public Date getVotingPhaseBegin() {
        return getDateProperty("votingPhaseBegin", "Beginn der Wahlphase");
    }

    public Date getVotingPhaseEnd() {
        return getDateProperty("votingPhaseEnd", "Ende der Wahlphase");
    }

    public String[] getMixerIds() {
        return getListProperty("mixerIds", "Identifikator der Mixer");
    }

    public String[] getTallierIds() {
        return getListProperty("tallierIds", "Identifikator der Tallier");
    }

    public int getEncryptionKeyLength() {
        return getIntProperty("encryptionKeyLength", "Laenge des Verschluesselungsschluessels");
    }

    public String getElectionDefinitionPath() {
        return getProperty("j", "Pfad der signierten Wahldefinition");
    }

    public String getPoliticalListsFile() {
        return getProperty("politicalListsFile", "Pfad der Kandidierendenlisten");
    }

    public int getMaxCandidates() {
        return getIntProperty("maxCandidates", "Maximale Anzahl waehlbarer Kandidaten");
    }

    public int getMaxCumulation() {
        return getIntProperty("maxCumulation", "Zulaessige Anzahl Kumulationen eines Kandidaten");
    }

    public String getElectionOptionsPath() {
        return getProperty("electionOptionsPath", "Pfad der signierten Kandidierendenlisten");
    }

    public String getElectoralRollFile() {
        return getProperty("electoralRollFile", "Pfad der Waehlendenliste");
    }

    public String getHashAlgorithm() {
        return getProperty("hashAlgorithm", "Hash-Algorithmus fuer die Waehlendenliste");
    }

    public String getElectoralRollPath() {
        return getProperty("electoralRollPath", "Pfad der signierten Waehlendenliste");
    }

    public String getElectionResultsPath() {
        return getProperty("electionResultsPath", "Pfad der Wahlresultatliste");
    }

    public String getValueDelimiter() {
        return getProperty("valueDelimiter", "Trennzeichen der Wahlresultatwerte");
    }

    public String getKeystorePath() {
        return getProperty("keystorePath", "Pfad des Keystores mit dem Signaturschluessel");
    }

    public String getKeystorePassword() {
        return getProperty("keystorePassword", "Passwort des Keystores mit dem Signaturschluessel");
    }

    public String getSignatureKeyType() {
        return getProperty("signatureKeyType", "Typ des Signaturschluessels");
    }

    public int getSignatureKeyLength() {
        return getIntProperty("signatureKeyLength", "Laenge des Signaturschluessels");
    }

    public String getSignatureKeyAlias() {
        return getProperty("signatureKeyAlias", "Alias des Signaturschluessels");
    }

    public String getSignatureKeyName() {
        return getProperty("signatureKeyName", "Name des Signaturschluessels");
    }

    public int getSignatureKeyValidity() {
        return getIntProperty("signatureKeyValidity", "Gueltigkeitsdauer des Signaturschluessels");
    }

    public String getSignatureAlgorithm() {
        return getProperty("signatureAlgorithm", "Signaturalgorithmus des Zertifikats zum Signaturschluessel");
    }

    public String getSignatureCertificatePath() {
        return getProperty("signatureCertificatePath", "Pfad des Zertifikats zum Signaturalgorithmus");
    }

    public String getAdminServiceAddress() {
        return getProperty("adminServiceAddress", "Webservice-Adresse der Wahladministration");
    }

    public String getBoardServiceAddress() {
        return getProperty("boardServiceAddress", "Webservice-Adresse des Wahlanschlagbretts");
    }

    public String getTruststorePath() {
        return getProperty("truststorePath", "Pfad des Truststores mit dem Webservice-Zertifikat");
    }

    public String getTruststorePassword() {
        return getProperty("truststorePassword", "Passwort des Truststores mit dem Webservice-Zertifikat");
    }

    public String getCharEncoding() {
        return getProperty("charEncoding", "Zeichencodierung");
    }

    public String getLogfilePath() {
        return getProperty("logfilePath", "Pfad des Logfiles");
    }

    public String getBallotsPath() {
        return getProperty("ballotsPath", "Pfad der Wahlzettel");
    }

    public String getDecodedVotesPath() {
        return getProperty("decodedVotesPath", "Pfad der entschluesselten Wahlzettel");
    }

    public String getDecryptedVotesPath() {
        return getProperty("decryptedVotesPath", "Pfad der entschluesselten Wahlzettel");
    }

    public String getElectionGeneratorPath() {
        return getProperty("electionGeneratorPath", "Pfad des ElectionGenerator");
    }

    public String getBlindedGeneratorPath() {
        return getProperty("blindedGeneratorPath", "Pfad des BlindedGenerator");
    }

    public String getEncryptionKeySharePath() {
        return getProperty("encryptionKeySharePath", "Pfad des EncryptionKeyShare");
    }

    public int getVotersNumber() {
        return getIntProperty("voters", "Anzahl der Waehlenden");
    }

    //Faults
    public boolean getFault(String fault) {
        return getBooleanProperty(fault, fault);
    }

    public boolean getPartyListSystemIndicator() {
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

    private String getProperty(String key, String label) {
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

    private int getIntProperty(String key, String label) {
        String value = getProperty(key, label + " (ganze Zahl)");
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ConfigException(label + ": " + value + " ist keine ganze Zahl");
        }
    }

    private boolean getBooleanProperty(String key, String label) {
        String value = getProperty(key, label + " (true/false)");
        try {
            return Boolean.parseBoolean(value);
        } catch (Exception e) {
            throw new ConfigException(label + ": " + value + " ist nicht true/false");
        }
    }

    private Date getDateProperty(String key, String label) {
        String value = getProperty(key, label + " (Format TT.MM.JJJJ hh:mm)");
        try {
            return dateFormat.parse(value);
        } catch (ParseException e) {
            throw new ConfigException(label + ": " + value + " ist kein gueltiges Datum");
        }
    }

    private String[] getListProperty(String key, String label) {
        String value = getProperty(key, label + " (Komma-separiert ohne Leerstellen)");
        if (value.matches("\\S+(,\\S+)*")) {
            return value.split(",");
        }
        throw new ConfigException(label + ": " + value + " ist keine gueltige Liste");
    }

    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            try {
                properties.load(new FileInputStream("properties/" + SYSTEM_CONFIG_FILE + ".properties"));
            } catch (IOException e) {
                throw new ConfigException("Die Konfigurationsdatei " + SYSTEM_CONFIG_FILE + ".properties konnte nicht gelesen werden", e);
            }
            try {
                properties.load(new FileInputStream("properties/" + CRYPTO_CONFIG_FILE + ".properties"));
            } catch (IOException e) {
                throw new ConfigException("Die Konfigurationsdatei " + CRYPTO_CONFIG_FILE + ".properties konnte nicht gelesen werden", e);
            }
            try {
                properties.load(new FileInputStream("properties/" + FAULT_CONFIG_FILE + ".properties"));
            } catch (IOException e) {
                throw new ConfigException("Die Konfigurationsdatei " + FAULT_CONFIG_FILE + ".properties konnte nicht gelesen werden", e);
            }
        }
        return properties;
    }
}
