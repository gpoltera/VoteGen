/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.gui;

import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.util.Locale;

/**
 *
 * @author Gian
 */
public class StartVoteGenerator {
    /**
     *
     * @param args To tell the command line verifier what to do and where to get
     * data. Usage: uvv [OPTION] [FILE] uvv -i /home/username/qrcode \t# Verify
     * a single election receipt from a QR code. uvv -u vsbfh-2013 \t\t# Verify
     * the results from the election with ID vsbfh-2013 * Commands:\n"
     *
     * -i, --individual [file] \tVerify an election receipt. -u, --universal
     * \"election id\"\tVerify an an entire election.
     */
    public static void main(String[] args) {
        StartVoteGenerator runner = new StartVoteGenerator(args);
    }

    /**
     * Begin a new verification.
     *
     * @param args
     */
    public StartVoteGenerator(String[] args) {
            Locale locale = new Locale("en", "EN");
            Locale.setDefault(locale);
            MainGUI gui = new MainGUI();
    }
}
