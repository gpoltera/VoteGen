/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.krypto;

import ch.bfh.univote.common.SignatureParameters;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class SchnorrVerificationKey extends Schnorr {

    private BigInteger p, q, g, vk;

    public SchnorrVerificationKey(SignatureParameters signatureParameters, BigInteger vk) {
        this.p = signatureParameters.getPrime();
        this.q = signatureParameters.getGroupOrder();
        this.g = signatureParameters.getGenerator();
        this.vk = vk;
    }

    public BigInteger getPrime() {
        return this.p;
    }

    public BigInteger getGroupOrder() {
        return this.q;
    }

    public BigInteger getGenerator() {
        return this.g;
    }

    public BigInteger getVerificationKey() {
        return this.vk;
    }
}
