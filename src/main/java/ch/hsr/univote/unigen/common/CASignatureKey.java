/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.common;

import java.security.interfaces.RSAPrivateKey;

/**
 *
 * @author Gian
 */
public class CASignatureKey {

    protected RSAPrivateKey caSignatureKey;

    public RSAPrivateKey getCASignatureKey() {
        return this.caSignatureKey;
    }

    public void setCASignatureKey(RSAPrivateKey caSignatureKey) {
        this.caSignatureKey = caSignatureKey;
    }
}
