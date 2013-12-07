/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.generator.prov;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Gian
 */
public class TallierGenerator {
    public static final String NAME = "Tallier";
    
    public static ArrayList<String> generateTalliers(int count) {
        ArrayList<String> tc = new ArrayList<>();
        
        for(int i = 1; i <= count; i++) {
            tc.add(NAME + i);
        }
        
        return tc;
    }
}