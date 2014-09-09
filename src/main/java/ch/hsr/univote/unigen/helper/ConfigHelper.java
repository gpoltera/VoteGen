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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * Configuration helper class.
 *
 * @author Stephan Fischli &lt;stephan.fischli@bfh.ch&gt;
 */
public class ConfigHelper {

    private Scanner scanner = new Scanner(System.in);
    private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private Properties properties;
    private FileHandler fileHandler;

    public ConfigHelper() {
        fileHandler = new FileHandler();
        properties = fileHandler.getConfigProperties();
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
        int mixerNumber = getMixersNumber();
        String[] mixerIds = new String[mixerNumber];

        for (int i = 0; i < mixerNumber; i++) {
            String mixerId = "Mixer" + (i + 1) + "Id";
            mixerIds[i] = getProperty(mixerId);
        }
        return mixerIds;
    }

    public String[] getTallierIds() {
        int tallierNumber = getTalliersNumber();
        String[] tallierIds = new String[tallierNumber];

        for (int i = 0; i < tallierNumber; i++) {
            String tallierId = "Tallier" + (i + 1) + "Id";
            tallierIds[i] = getProperty(tallierId);
        }
        return tallierIds;
    }

    public String[] getCandidate(int i) {
        return getListProperty("candidate" + i, "Kandidat defininieren (Nummer, Name, Vorname, Geschlecht, Alter, Liste");
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
        return getIntProperty("Voters", "Anzahl der Waehlenden");
    }

    public int getLatelyVotersNumber() {
        return getIntProperty("LatelyVoters", "Anzahl der Waehlenden die sich erst waehrend der Wahl registrieren");
    }

    public int getCandidatesNumber() {
        return getIntProperty("candidates", "Anzahl der Kandidaten");
    }

    public int getListsNumber() {
        return getIntProperty("lists", "Anzahl Listen");
    }

    public int getMixersNumber() {
        return getIntProperty("Mixers", "Anzahl der Mixer");
    }

    public int getTalliersNumber() {
        return getIntProperty("Talliers", "Anzahl der Tallier");
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
        String value = properties.getProperty(key);
        while (value == null || value.isEmpty()) {
            value = JOptionPane.showInputDialog(label);
            if (!value.isEmpty()) {
                properties.put(key, value);
            }
        }
        return value;
    }

    public String getProperty(String key) {
        String value = properties.getProperty(key);

        return value;
    }

    public boolean existProperty(String key) {
        boolean result;
        String value = properties.getProperty(key);
        if (value == null || value.isEmpty()) {
            result = false;
        } else {
            result = true;
        }
        return result;
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

    public void setProperty(String key, String value) {
        properties.put(key, value);
    }
    
    public void saveProperties() {
        fileHandler.saveConfigProperties(properties);
    }
}
