/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.krypto;

import ch.bfh.univote.common.Ballot;
import ch.hsr.univote.unigen.helper.StringConcatenator;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class SchnorrSignatureGenerator {

    public BigInteger[] createSignature(Ballot ballot, SchnorrSignatureKey schnorrSignatureKey) {
        //concatenate to ( id | (firstValue|secondValue) | ((t)|(s)) )
        StringConcatenator sc = new StringConcatenator();
        sc.pushLeftDelim();
        //election ID
        sc.pushObjectDelimiter(ballot.getElectionId(), StringConcatenator.INNER_DELIMITER);
        //encrypted vote
        sc.pushLeftDelim();
        sc.pushObjectDelimiter(ballot.getEncryptedVote().getFirstValue(), StringConcatenator.INNER_DELIMITER);
        sc.pushObject(ballot.getEncryptedVote().getSecondValue());
        sc.pushRightDelim();
        sc.pushInnerDelim();
        //proof
        sc.pushLeftDelim();
        sc.pushLeftDelim();
        sc.pushObject(ballot.getProof().getCommitment());
        sc.pushRightDelim();
        sc.pushInnerDelim();
        sc.pushLeftDelim();
        sc.pushObject(ballot.getProof().getResponse());
        sc.pushRightDelim();
        sc.pushRightDelim();
        //right parenthesis
        sc.pushRightDelim();

        String res = sc.pullAll();
        
        return new Schnorr().signSchnorr(res, schnorrSignatureKey);
    }
}