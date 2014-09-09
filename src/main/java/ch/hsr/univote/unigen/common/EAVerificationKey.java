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
public class EAVerificationKey {

    protected RSAPublicKey eaVerificationKey;

    public RSAPublicKey getEAVerificationKey() {
        return this.eaVerificationKey;
    }

    public void setEAVerificationKey(RSAPublicKey eaVerificationKey) {
        this.eaVerificationKey = eaVerificationKey;
    }
}
