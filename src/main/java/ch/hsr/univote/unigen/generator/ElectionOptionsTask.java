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
import ch.bfh.univote.common.Candidate;
import ch.bfh.univote.common.CandidateStatus;
import ch.bfh.univote.common.ElectionOptions;
import ch.bfh.univote.common.ForallRule;
import ch.bfh.univote.common.LanguageCode;
import ch.bfh.univote.common.LocalizedText;
import ch.bfh.univote.common.PoliticalList;
import ch.bfh.univote.common.Sex;
import ch.bfh.univote.common.Signature;
import ch.bfh.univote.common.SummationRule;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.electionAdministratorPrivateKey;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.helper.FormatException;
import ch.hsr.univote.unigen.helper.XMLHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * User interface class to define election options.
 *
 * @author Stephan Fischli &lt;stephan.fischli@bfh.ch&gt;
 */
public class ElectionOptionsTask extends WahlGenerator {

    public static void run() throws FileNotFoundException, FormatException, Exception {
        List<CandidateList> lists = getCandidateLists();
        for (CandidateList list : lists) {
            if (!verifyList(list)) {
                return;
            }
        }
        boolean partyListSystem = ConfigHelper.getPartyListSystemIndicator();
        ElectionOptions options = createOptions(lists, partyListSystem);
        options.setSignature(SignatureGenerator.createSignature(options, electionAdministratorPrivateKey));
        eo = options;
    }

    private static List<CandidateList> getCandidateLists() throws FileNotFoundException, FormatException {
        File file = new File(ConfigHelper.getPoliticalListsFile());
        if (!file.exists() || !file.isFile()) {
            throw new FileNotFoundException("Die Datei " + file + " wurde nicht gefunden");
        }
        FileInputStream fis = null;
        List<CandidateList> lists = new ArrayList<CandidateList>();
        try {
            fis = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(fis);
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                CandidateList list = getCandidateList(workbook.getSheetAt(i));
                if (list != null) {
                    lists.add(list);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new FormatException("Die Kandidierendenliste hat ein ungueltiges Format", e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
            }
        }
        return lists;
    }

    private static CandidateList getCandidateList(Sheet sheet) {
        CandidateList list = new CandidateList();

        // parse header
        list.setNumber(sheet.getRow(2).getCell(2).getStringCellValue().trim());
        if (list.getNumber().isEmpty()) {
            return null;
        }
        list.getTitle().add(getLocalizedText(sheet, 3, 2));
        list.getPartyName().add(getLocalizedText(sheet, 4, 2));
        list.getPartyShortName().add(getLocalizedText(sheet, 5, 2));

        // parse candidates
        for (int i = 8; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                break;
            }
            Candidate candidate = getCandidate(row);
            if (candidate == null) {
                continue;
            }
            int index = -1;
            for (int j = 0; j < list.getCandidates().size(); j++) {
                Candidate c = list.getCandidates().get(j);
                if (c.getLastName().equals(candidate.getLastName())
                        && c.getFirstName().equals(candidate.getFirstName())) {
                    index = j;
                    break;
                }
            }
            if (index >= 0) {
                candidate = list.getCandidates().get(index);
                candidate.setCumulation(candidate.getCumulation() + 1);
            } else {
                candidate.setCumulation(1);
                list.getCandidates().add(candidate);
            }
        }
        return list;
    }

    private static Candidate getCandidate(Row row) {
        if (row.getCell(1) == null || row.getCell(1).getStringCellValue().trim().isEmpty()) {
            return null;
        }
        Candidate candidate = new Candidate();
        candidate.setLastName(row.getCell(1).getStringCellValue().trim());
        candidate.setFirstName(row.getCell(2).getStringCellValue().trim());
        candidate.setStatus(CandidateStatus.NEW);
        String status = row.getCell(3).getStringCellValue().trim();
        if (status.toLowerCase().equals("neu")) {
            candidate.setStatus(CandidateStatus.NEW);
        } else if (status.toLowerCase().equals("bisher")) {
            candidate.setStatus(CandidateStatus.PREVIOUS);
        } else {
            candidate.setStatus(CandidateStatus.UNDEF);
        }
        String sex = row.getCell(4).getStringCellValue().trim();
        if (sex.toLowerCase().equals("m")) {
            candidate.setSex(Sex.M);
        } else if (sex.toLowerCase().equals("w")) {
            candidate.setSex(Sex.F);
        } else {
            candidate.setSex(Sex.UNDEF);
        }
        candidate.setYearOfBirth((int) row.getCell(5).getNumericCellValue());
        candidate.getStudyBranch().add(getLocalizedText(row, 6));
        candidate.getStudyDegree().add(getLocalizedText(row, 7));
        candidate.setSemesterCount((int) row.getCell(8).getNumericCellValue());
        candidate.setCumulation(1);
        return candidate;
    }

    private static LocalizedText getLocalizedText(Sheet sheet, int row, int col) {
        return getLocalizedText(sheet.getRow(row), col);
    }

    private static LocalizedText getLocalizedText(Row row, int col) {
        String text = row.getCell(col).getStringCellValue().trim();
        LocalizedText localizedText = new LocalizedText();
        localizedText.setLanguage(LanguageCode.DE);
        localizedText.setText(text.isEmpty() ? "-" : text);
        return localizedText;
    }

    private static boolean verifyList(CandidateList list) {
        System.out.println("--------------------------------------------------");
        System.out.println("Listennummer:  " + list.getNumber());
        System.out.println("Bezeichnung:   " + list.getTitle().get(0).getText());
        System.out.println("Partei:        " + list.getPartyName().get(0).getText());
        System.out.println("Parteikuerzel: " + list.getPartyShortName().get(0).getText());
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.println("(Kumulierung) Name, Vorname, Status, Geschlecht, Jahrgang, Studienrichtung, Abschluss, Semesterzahl");
        System.out.println("----------------------------------------------------------------------------------------------------");
        for (Candidate candidate : list.getCandidates()) {
            System.out.println(
                    "(" + candidate.getCumulation() + ") "
                    + candidate.getLastName() + ", "
                    + candidate.getFirstName() + ", "
                    + (candidate.getStatus() == CandidateStatus.NEW ? "neu"
                    : (candidate.getStatus() == CandidateStatus.PREVIOUS ? "bisher" : "")) + ", "
                    + (candidate.getSex() == Sex.M ? "m"
                    : (candidate.getSex() == Sex.F ? "w" : "")) + ", "
                    + candidate.getYearOfBirth() + ", "
                    + candidate.getStudyBranch().get(0).getText() + ", "
                    + candidate.getStudyDegree().get(0).getText() + ", "
                    + candidate.getSemesterCount());
        }
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.println("Total: " + list.getCandidates().size());
        Scanner scanner = new Scanner(System.in);
        Boolean result = null;
        result = true;
        while (result == null) {
            System.out.print("Ist diese Kandidierendenliste korrekt (ja/nein)? ");
            String answer = scanner.nextLine();
            if (answer.equals("ja")) {
                result = true;
            } else if (answer.equals("nein")) {
                result = false;
            }
        }
        System.out.println();
        return result;
    }

    private static ElectionOptions createOptions(List<CandidateList> lists, boolean partyListSystem) {
        ElectionOptions options = new ElectionOptions();
        options.setElectionId(ConfigHelper.getElectionId());
        SummationRule listSummationRule = new SummationRule();
        listSummationRule.setLowerBound(0);
        if (partyListSystem) {
            listSummationRule.setUpperBound(1);
        } else {
            listSummationRule.setUpperBound(0);
        }
        ForallRule listForallRule = new ForallRule();
        listForallRule.setLowerBound(0);
        listForallRule.setUpperBound(1);
        SummationRule candidateSummationRule = new SummationRule();
        candidateSummationRule.setLowerBound(0);
        candidateSummationRule.setUpperBound(ConfigHelper.getMaxCandidates());
        ForallRule candidateForallRule = new ForallRule();
        candidateForallRule.setLowerBound(0);
        candidateForallRule.setUpperBound(ConfigHelper.getMaxCumulation());
        int choiceId = 1;
        for (CandidateList list : lists) {
            int listId = choiceId++;
            list.setChoiceId(listId);
            options.getChoice().add(list);
            listSummationRule.getChoiceId().add(listId);
            listForallRule.getChoiceId().add(listId);
            int count = 1;
            for (Candidate candidate : list.getCandidates()) {
                String number = list.getNumber() + "." + count++;
                candidate.setNumber(number);
                int candidateId = choiceId++;
                candidate.setChoiceId(candidateId);
                candidate.setListId(listId);
                options.getChoice().add(candidate);
                candidateSummationRule.getChoiceId().add(candidateId);
                candidateForallRule.getChoiceId().add(candidateId);
            }
        }
        options.getRule().add(listSummationRule);
        options.getRule().add(listForallRule);
        options.getRule().add(candidateSummationRule);
        options.getRule().add(candidateForallRule);
        return options;
    }
}

class CandidateList extends PoliticalList {

    private List<Candidate> candidates;

    public List<Candidate> getCandidates() {
        if (candidates == null) {
            candidates = new ArrayList<Candidate>();
        }
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }
}