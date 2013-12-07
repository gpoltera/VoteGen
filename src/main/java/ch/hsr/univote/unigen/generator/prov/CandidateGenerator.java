/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.generator.prov;

import ch.bfh.univote.common.Candidate;
import ch.bfh.univote.common.CandidateStatus;
import ch.bfh.univote.common.Sex;

/**
 *
 * @author Gian
 */
public class CandidateGenerator {
    
    public static void addCandidate() {
        Candidate candidate = new Candidate();
        candidate.setChoiceId(1);
        candidate.setCumulation(1);
        candidate.setFirstName("Peter");
        candidate.setLastName("Mueller");
        candidate.setListId(1);
        candidate.setNumber("1");
        candidate.setSemesterCount(1);
        candidate.setSex(Sex.M);
        candidate.setStatus(CandidateStatus.NEW);
        candidate.setYearOfBirth(1986);
    }
}
