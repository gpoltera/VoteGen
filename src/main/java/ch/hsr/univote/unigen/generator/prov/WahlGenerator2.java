/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.generator.prov;

import ch.bfh.univote.common.Certificate;
import ch.bfh.univote.common.ElectionSystemInfo;
import ch.bfh.univote.common.Signature;
import ch.bfh.univote.common.SignatureParameters;
import java.math.BigInteger;

/**
 *
 * @author Gian
 */
public class WahlGenerator2 {
    public static SignatureParameters signatureParameters = new SignatureParameters();
    public static ElectionSystemInfo electionSystemInfo = new ElectionSystemInfo();
    
    public static void test() {
        Signature signature = new Signature();
        signature.setSignerId(null);
        signatureParameters.setPrime(new BigInteger("161931481198080639220214033595931441094586304918402813506510547237223787775475425991443924977419330663170224569788019900180050114468430413908687329871251101280878786588515668012772798298511621634145464600626619548823238185390034868354933050128115662663653841842699535282987363300852550784188180264807606304297"));
        signatureParameters.setGroupOrder(new BigInteger("65133683824381501983523684796057614145070427752690897588060462960319251776021"));
        signatureParameters.setGenerator(new BigInteger("109291242937709414881219423205417309207119127359359243049468707782004862682441897432780127734395596275377218236442035534825283725782836026439537687695084410797228793004739671835061419040912157583607422965551428749149162882960112513332411954585778903685207256083057895070357159920203407651236651002676481874709"));
        signatureParameters.setSignature(null);
        
        Certificate certificate = new Certificate();
        
        electionSystemInfo.setCertificateAuthority(certificate);
    }      
}
