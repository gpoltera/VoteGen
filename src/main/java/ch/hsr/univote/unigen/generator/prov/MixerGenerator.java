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
public class MixerGenerator {
    public static final String NAME = "Mixer";
    
    public static ArrayList<String> generateMixers(int count) {
        ArrayList<String> mc = new ArrayList<>();
        
        for(int i = 1; i <= count; i++) {
            mc.add(NAME + i);
        }
        
        return mc;
    }
}