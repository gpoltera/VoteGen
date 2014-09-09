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
public class CAVerificationKey {

    protected RSAPublicKey caVerificationKey;

    public RSAPublicKey getCAVerificationKey() {
        return this.caVerificationKey;
    }

    public void setCAVerificationKey(RSAPublicKey caVerificationKey) {
        this.caVerificationKey = caVerificationKey;
    }
}
