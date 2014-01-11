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

import ch.bfh.univote.common.Candidate;
import ch.bfh.univote.common.Choice;
import ch.bfh.univote.common.DecodedVote;
import ch.bfh.univote.common.DecodedVoteEntry;
import ch.bfh.univote.common.DecodedVotes;
import ch.bfh.univote.common.ElectionData;
import ch.bfh.univote.common.PoliticalList;
import ch.hsr.univote.unigen.generator.prov.WahlGenerator;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User interface class to print the results of an election.
 *
 * @author Stephan Fischli &lt;stephan.fischli@bfh.ch&gt;
 */
public class ElectionResultsTask extends WahlGenerator {

    public static void run() {
        List<Choice> choices = getChoices();
        if (choices == null) {
            return;
        }
        List<Map<Choice, Integer>> choiceCounts = getChoiceCounts(choices);
        if (choiceCounts == null) {
            return;
        }
        Map<Choice, Integer> totalCounts = getTotalCounts(choiceCounts);
        writeResults(choices, choiceCounts, totalCounts);
    }

    private static List<Choice> getChoices() {
        ElectionData data = edat;
        List<Choice> choices = new ArrayList<Choice>();
        for (Choice choice : data.getChoice()) {
            if (choice instanceof PoliticalList) {
                choices.add(choice);
            }
        }
        for (Choice choice : data.getChoice()) {
            if (choice instanceof Candidate) {
                choices.add(choice);
            }
        }
        return choices;
    }

    private static List<Map<Choice, Integer>> getChoiceCounts(List<Choice> choices) {
        DecodedVotes votes = dov;
        List<Map<Choice, Integer>> choiceCounts = new ArrayList<Map<Choice, Integer>>();
        for (DecodedVote vote : votes.getDecodedVote()) {
            Map<Choice, Integer> counts = new HashMap<Choice, Integer>();
            for (DecodedVoteEntry entry : vote.getEntry()) {
                for (Choice choice : choices) {
                    if (entry.getChoiceId() == choice.getChoiceId()) {
                        counts.put(choice, entry.getCount());
                    }
                }
            }
            choiceCounts.add(counts);
        }
        return choiceCounts;

    }

    private static Map<Choice, Integer> getTotalCounts(List<Map<Choice, Integer>> choiceCounts) {
        Map<Choice, Integer> totalCounts = new HashMap<Choice, Integer>();
        for (Map<Choice, Integer> counts : choiceCounts) {
            for (Choice choice : counts.keySet()) {
                int count = counts.get(choice);
                if (totalCounts.containsKey(choice)) {
                    totalCounts.put(choice, totalCounts.get(choice) + count);
                } else {
                    totalCounts.put(choice, count);
                }
            }
        }
        return totalCounts;
    }

    private static void writeResults(List<Choice> choices, List<Map<Choice, Integer>> choiceCounts, Map<Choice, Integer> totalCounts) {
        try {
            // print choice numbers
            PrintWriter writer = new PrintWriter(new FileWriter(ConfigHelper.getElectionResultsPath()));
            String delimiter = ConfigHelper.getValueDelimiter();
            for (Choice choice : choices) {
                writer.print(delimiter);
                if (choice instanceof PoliticalList) {
                    writer.print("Liste " + ((PoliticalList) choice).getNumber());
                } else {
                    writer.print("Kandidat " + ((Candidate) choice).getNumber());
                }
            }
            writer.println();

            // print choice titles and names
            for (Choice choice : choices) {
                writer.print(delimiter);
                if (choice instanceof PoliticalList) {
                    writer.print(((PoliticalList) choice).getTitle().get(0).getText());
                } else {
                    writer.print(((Candidate) choice).getLastName() + " " + ((Candidate) choice).getFirstName());
                }
            }
            writer.println();

            // print choice counts
            int nr = 1;
            for (Map<Choice, Integer> counts : choiceCounts) {
                writer.write("Wahlzettel " + nr++);
                for (Choice choice : choices) {
                    int count = counts.containsKey(choice) ? counts.get(choice) : 0;
                    writer.print(delimiter + count);
                }
                writer.println();
            }

            // print total counts
            writer.print("Total");
            for (Choice choice : choices) {
                writer.print(delimiter + totalCounts.get(choice));
            }
            writer.println();
            writer.close();
            System.out.println("Die Wahlresultate wurden in die Datei " + ConfigHelper.getElectionResultsPath() + " geschrieben");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}