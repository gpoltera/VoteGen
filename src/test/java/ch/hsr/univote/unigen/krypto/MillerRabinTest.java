/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.krypto;

import ch.hsr.univote.unigen.crypto.MillerRabin;
import java.math.BigInteger;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author Gian Poltéra
 */
public class MillerRabinTest extends TestCase {

    public MillerRabinTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(MillerRabinTest.class);
    }

    public void testMillerRabin() {

        //primes between 1 and t
        int t = 10000000;
        int s = 10;
        int r1 = 664579;
        int r2 = 8;

        boolean result1 = test1(t, s, r1);
        boolean result2 = test2(t, s, r2);
        //boolean result3 = test3(t, s);
        //boolean result4 = test4(t, s, 7);

        //check the results
        if (result1 && result2) {
            assertTrue(true);
        } else {
            assertTrue("Miller-Rabin-Test not works well", false);
        }

    }

    private boolean test1(int t, int s, int r1) {
        //first test
        System.out.println("Testing the Miller-Rabin-Test for small values: ");
        int r = 0;
        long before1 = System.nanoTime();
        for (int i = 0; i < t; i++) {
            if (new MillerRabin().millerRabinTest(BigInteger.valueOf(i), s)) {
                r++;
            }
        }
        long after1 = System.nanoTime();
        long runningTimeMs1 = (after1 - before1) / 1000000;
        System.out.println("Primes found: " + r + ", in: " + runningTimeMs1 + "ms");

        if (r == r1) {
            return true;
        } else {
            return false;
        }
    }

    private boolean test2(int t, int s, int r2) {
        //second test       
        BigInteger startValue = BigInteger.TEN.pow(308);
        BigInteger endValue = startValue.add(BigInteger.valueOf(t / 1000));
        System.out.println("Testing the Miller-Rabin-Test for large values: ");
        int r = 0;
        long before2 = System.nanoTime();
        while (startValue.compareTo(endValue) < 0) {
            if (new MillerRabin().millerRabinTest(startValue, s)) {
                r++;
            }
            startValue = startValue.add(BigInteger.ONE);
        }
        long after2 = System.nanoTime();
        long runningTimeMs2 = (after2 - before2) / 1000000;
        System.out.println("Primes found: " + r + ", in: " + runningTimeMs2 + "ms");

        if (r == r2) {
            return true;
        } else {
            return false;
        }
    }

    private boolean test3(int t, int s) {
        //third test       
        BigInteger startValue = BigInteger.TEN.pow(616);
        BigInteger endValue = startValue.add(BigInteger.valueOf(t / 1000));
        System.out.println("Testing the Miller-Rabin-Test for large values: ");
        int r4 = 0;
        long before4 = System.nanoTime();
        while (startValue.compareTo(endValue) < 0) {
            if (new MillerRabin().millerRabinTest(startValue, s)) {
                r4++;
                System.out.println(startValue);
            }
            startValue = startValue.add(BigInteger.ONE);
        }
        long after4 = System.nanoTime();
        long runningTimeMs4 = (after4 - before4) / 1000000;
        System.out.println("Primes found: " + r4 + ", in: " + runningTimeMs4 + "ms");

        //third test with isProbablePrime
        startValue = BigInteger.TEN.pow(616);
        endValue = startValue.add(BigInteger.valueOf(t / 1000));

        System.out.println("Testing the BigInteger probableprime for large values: ");
        int r5 = 0;
        long before5 = System.nanoTime();
        while (startValue.compareTo(endValue) < 0) {
            if (startValue.isProbablePrime(s * 2)) {
                r5++;
            }
            startValue = startValue.add(BigInteger.ONE);
        }
        long after5 = System.nanoTime();
        long runningTimeMs5 = (after5 - before5) / 1000000;
        System.out.println("Primes found: " + r5 + ", in: " + runningTimeMs5 + "ms");

        if (r4 == r5) {
            return true;
        } else {
            return false;
        }
    }

    private boolean test4(int t, int s, int r) {
        //third test       
        BigInteger startValue = BigInteger.TEN.pow(616);
        BigInteger endValue = startValue.add(BigInteger.valueOf(t / 1000));

        //third test with isProbablePrime
        startValue = BigInteger.TEN.pow(616);
        endValue = startValue.add(BigInteger.valueOf(t / 1000));

        System.out.println("Testing the BigInteger nextprobableprime for large values: ");
        int r6 = 0;
        long before6 = System.nanoTime();
        for (int i = 0; i < r; i++) {
            startValue = startValue.nextProbablePrime();
            if (startValue.isProbablePrime(s * 2)) {
                r6++;
            }
        }
        long after6 = System.nanoTime();
        long runningTimeMs6 = (after6 - before6) / 1000000;
        System.out.println("Primes found: " + r6 + ", in: " + runningTimeMs6 + "ms");

        if (r == r6) {
            return true;
        } else {
            return false;
        }
    }
}
