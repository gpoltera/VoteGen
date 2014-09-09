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
public class RAVerificationKey {

    protected RSAPublicKey raVerificationKey;

    public RSAPublicKey getRAVerificationKey() {
        return this.raVerificationKey;
    }

    public void setRAVerificationKey(RSAPublicKey raVerificationKey) {
        this.raVerificationKey = raVerificationKey;
    }
}
