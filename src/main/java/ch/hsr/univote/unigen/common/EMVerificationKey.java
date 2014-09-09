/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.common;

import java.security.interfaces.RSAPublicKey;

/**
 *
 * @author Gian
 */
public class EMVerificationKey {

    protected RSAPublicKey emVerificationKey;

    public RSAPublicKey getEMVerificationKey() {
        return this.emVerificationKey;
    }

    public void setEMVerificationKey(RSAPublicKey emVerificationKey) {
        this.emVerificationKey = emVerificationKey;
    }
}
