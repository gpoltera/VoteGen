package ch.hsr.univote.unigen;

import ch.bfh.univote.common.ElectionSystemInfo;
import java.math.BigInteger;

/**
 * Test
 *
 */
public class App
{
    public static void main( String[] args )
    {
        ElectionSystemInfo esi = new ElectionSystemInfo();
        esi.setElectionId("some-election-2013");
      
        System.out.println(esi.getElectionId());
        System.out.println("Wahlausgabe:");
        System.out.println("p is not prime");
        System.out.println("Schnorrsignatur");
    }
}