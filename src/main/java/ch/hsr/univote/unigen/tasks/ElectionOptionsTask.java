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

import ch.bfh.univote.common.Candidate;
import ch.bfh.univote.common.CandidateStatus;
import ch.bfh.univote.common.ElectionOptions;
import ch.bfh.univote.common.ForallRule;
import ch.bfh.univote.common.LanguageCode;
import ch.bfh.univote.common.LocalizedText;
import ch.bfh.univote.common.PoliticalList;
import ch.bfh.univote.common.Sex;
import ch.bfh.univote.common.SummationRule;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.RSASignatureGenerator;
import java.util.ArrayList;
import java.util.List;

/**
 * User interface class to define election options.
 *
 * @author Stephan Fischli &lt;stephan.fischli@bfh.ch&gt;
 */
public class ElectionOptionsTask {

    private ConfigHelper config;
    private ElectionBoard electionBoard;
    private KeyStore keyStore;
    private int choiceId;

    public ElectionOptionsTask() {
        this.config = VoteGenerator.config;
        this.electionBoard = VoteGenerator.electionBoard;
        this.keyStore = VoteGenerator.keyStore;

        run();
    }

    private void run() {
        /*create ElectionOptions*/
        ElectionOptions electionOptions = createElectionOptions(getCandidateLists());

        /*sign by ElectionAdministrator*/
        electionOptions.setSignature(new RSASignatureGenerator().createSignature(electionOptions, keyStore.getEASignatureKey()));

        /*submit to ElectionBoard*/
        electionBoard.setElectionOptions(electionOptions);
    }

    private ElectionOptions createElectionOptions(List<CandidateList> lists) {
        ElectionOptions electionOptions = new ElectionOptions();
        electionOptions.setElectionId(config.getElectionId());

        SummationRule listSummationRule = new SummationRule();
        listSummationRule.setLowerBound(0);
        if (config.getPartyListSystemIndicator()) {
            listSummationRule.setUpperBound(1);
        } else {
            listSummationRule.setUpperBound(0);
        }

        ForallRule listForallRule = new ForallRule();
        listForallRule.setLowerBound(0);
        listForallRule.setUpperBound(1);

        SummationRule candidateSummationRule = new SummationRule();
        candidateSummationRule.setLowerBound(0);
        candidateSummationRule.setUpperBound(config.getMaxCandidates());

        ForallRule candidateForallRule = new ForallRule();
        candidateForallRule.setLowerBound(0);
        candidateForallRule.setUpperBound(config.getMaxCumulation());

        for (CandidateList candidateList : lists) {
            electionOptions.getChoice().add(candidateList);
            listSummationRule.getChoiceId().add(candidateList.getChoiceId());
            listForallRule.getChoiceId().add(candidateList.getChoiceId());
            for (Candidate candidate : candidateList.getCandidates()) {
                electionOptions.getChoice().add(candidate);
                candidateSummationRule.getChoiceId().add(candidate.getChoiceId());
                candidateForallRule.getChoiceId().add(candidate.getChoiceId());
            }
        }

        electionOptions.getRule().add(listSummationRule);
        electionOptions.getRule().add(listForallRule);
        electionOptions.getRule().add(candidateSummationRule);
        electionOptions.getRule().add(candidateForallRule);
        
        return electionOptions;
    }

    private List<CandidateList> getCandidateLists() {
        List<CandidateList> candidateLists = new ArrayList<>();

        choiceId = 0;
        for (int i = 1; i <= config.getListsNumber(); i++) {
            choiceId++;
            candidateLists.add(createCandidateList(i));
        }
        return candidateLists;
    }

    private CandidateList createCandidateList(int listId) {
        CandidateList candidateList = new CandidateList();

        LocalizedText title = createLocalizedText("List " + listId);
        LocalizedText partyname = createLocalizedText("partyname");
        LocalizedText partyshortname = createLocalizedText("partyshortname");
        
        candidateList.setChoiceId(choiceId);
        candidateList.setNumber(String.valueOf(listId));
        candidateList.getTitle().add(title);
        candidateList.getPartyName().add(partyname);
        candidateList.getPartyShortName().add(partyshortname);

        int listPlace = 0;
        for (int i = 1; i <= config.getCandidatesNumber(); i++) {
            if (Integer.parseInt(config.getCandidate(i)[5]) == listId) {
                choiceId++;
                listPlace++;
                candidateList.addCandidate(createCandidate(listId, i, listPlace));
            }
        }
        
        return candidateList;
    }

    private Candidate createCandidate(int listId, int candidateNumber, int listPlace) {
        Candidate candidate = new Candidate();

        String[] candidateItems = config.getCandidate(candidateNumber);

        String number = listId + "." + listPlace;

        String lastname = candidateItems[1];
        String firstname = candidateItems[2];
        CandidateStatus status = CandidateStatus.UNDEF;
        Sex sex = Sex.UNDEF;
        if (candidateItems[3].equals("Male") || candidateItems[2].equals("Männlich")) {
            sex = Sex.M;
        }
        if (candidateItems[3].equals("Female") || candidateItems[2].equals("Weiblich")) {
            sex = Sex.F;
        }
        int yearofbirth = Integer.parseInt(candidateItems[4]);
        LocalizedText studybranch = createLocalizedText("branch");
        LocalizedText studydegree = createLocalizedText("degree");
        int semestercount = 1;
        int cumulation = 1;

        candidate.setChoiceId(choiceId);
        candidate.setListId(listId);
        candidate.setNumber(number);
        candidate.setLastName(lastname);
        candidate.setFirstName(firstname);
        candidate.setStatus(status);
        candidate.setSex(sex);
        candidate.setYearOfBirth(yearofbirth);
        candidate.getStudyBranch().add(studybranch);
        candidate.getStudyDegree().add(studydegree);
        candidate.setSemesterCount(semestercount);
        candidate.setCumulation(cumulation);
        
        return candidate;
    }

    private LocalizedText createLocalizedText(String string) {
        LocalizedText localizedText = new LocalizedText();
        localizedText.setLanguage(LanguageCode.DE);
        localizedText.setText(string);

        return localizedText;
    }
}

class CandidateList extends PoliticalList {

    private List<Candidate> candidates;

    public CandidateList() {
        candidates = new ArrayList<>();
    }
    
    public void addCandidate(Candidate candidate) {
        candidates.add(candidate);
    }
    
    public List<Candidate> getCandidates() {   
        return candidates;
    }
    
    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }
}
