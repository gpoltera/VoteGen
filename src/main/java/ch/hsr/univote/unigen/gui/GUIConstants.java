/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.gui;

import java.awt.Color;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Gian
 */
public class GUIConstants {
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("Bundle");
    
    
    
    
    
    
    
    
    
    
    
    public final static Color GREY = new Color(190, 190, 190);
    public final static Color DARK_GREY = new Color(160, 160, 160);
    public final static Color BLUE = new Color(110, 110, 254);
    public static Locale loc = new Locale("en", "EN");
    
    
    
    
    
    
    public final static String ABOUT_TEXT = "VoteVerifier 1.0\nProject independent verifier for UniVote."
            + "\nCopyright (c) 2013 Berner Fachhochschule, Switzerland."
            + "\nBern University of Applied Sciences, Engineering and Information Technology,"
            + " \nResearch Institute for Security in the Information Society, E-Voting Group,"
            + " \nBiel, Switzerland.";
    public final static String USAGE = ABOUT_TEXT
            + "\n\nUsage: uvv [OPTION] [FILE]\n"
            + "uvv verifies the results from an election held with the UniVote Wahlsystem.\n"
            + "\n"
            + "Examples:\n"
            + "  uvv -i /home/username/qrcode \t# Verify a single election receipt from a QR code.\n"
            + "  uvv -u vsbfh-2013   \t\t# Verify the results from the election with ID vsbfh-2013\n"
            + "\n"
            + "Commands:\n"
            + "\n"
            + "  -i, --individual [file]   \tVerify an election receipt.\n"
            + "  -u, --universal \"election id\"\tVerify an an entire election.\n";

    /**
     * Set the locale being used for the program to a new value.
     *
     * @param str the String representation of the newly desired locale. I.e.
     * "EN" or "FR".
     */
    public static void setLocale(String str, String str2) {
        loc = new Locale(str, str2);
    }

    /**
     * Get the locale that is currently being used for the program.
     *
     * @return Locale the locale that the program is currently set to use.
     */
    public static Locale getLocale() {
        return loc;
    }
}
