/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.common;

import ch.bfh.univote.common.BlindedGenerator;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Gian
 */
public class BlindedGeneratorsList {

    protected Map<String, BlindedGenerator> blindedGeneratorsList;

    public Map<String, BlindedGenerator> getBlindedGeneratorsList() {
        if (blindedGeneratorsList == null) {
            blindedGeneratorsList = new HashMap<String, BlindedGenerator>();
        }
        return this.blindedGeneratorsList;
    }

    public void setBlindedGeneratorList(Map<String, BlindedGenerator> blindedGeneratorsList) {
        this.blindedGeneratorsList = blindedGeneratorsList;
    }
}
