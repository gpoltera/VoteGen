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
public class RASignatureKey {
    
    protected RSAPrivateKey raSignatureKey;

    public RSAPrivateKey getRASignatureKey() {
        return this.raSignatureKey;
    }

    public void setRASignatureKey(RSAPrivateKey raSignatureKey) {
        this.raSignatureKey = raSignatureKey;
    }
}
