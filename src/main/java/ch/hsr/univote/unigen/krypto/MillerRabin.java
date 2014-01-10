/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.krypto;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

/**
 *
 * @author Gian Poltéra
 */
public class MillerRabin {

    private static final BigInteger BIGINT0 = BigInteger.valueOf(0);
    private static final BigInteger BIGINT1 = BigInteger.valueOf(1);
    private static final BigInteger BIGINT2 = BigInteger.valueOf(2);
    private static final BigInteger BIGINT3 = BigInteger.valueOf(3);
    private static final BigInteger BIGINT5 = BigInteger.valueOf(5);
    private static final BigInteger BIGINT7 = BigInteger.valueOf(7);
    private static final BigInteger BIGINT11 = BigInteger.valueOf(11);
    private static final BigInteger BIGINT13 = BigInteger.valueOf(13);
    private static final BigInteger BIGINT17 = BigInteger.valueOf(17);
    private static final BigInteger BIGINT31 = BigInteger.valueOf(31);
    private static final BigInteger BIGINT61 = BigInteger.valueOf(61);  
    private static final BigInteger BIGINT73 = BigInteger.valueOf(73); 

    public static void main(String[] args) {
        int k = 0;
        int u = 0;
        int a = 0;
        int w = 100000000;
        int r = 100000000;
        int l = w - r;
        for (int i = l; i < w; i++) {
            double j = w - l;
            double z = ((i - l) / j * 100);
            int g = (int) z;
            k++;
            if (r / 10 == k) {
                System.out.println(g + "%");
                k = 0;
            }
            if (millerRabinTest(BigInteger.valueOf(i), 3)) {
                u++;
                if (!BigInteger.valueOf(i).isProbablePrime(2)) {
                    a++;
                }
            }
        }
        System.out.println("Gefundene Primzahlen: " + u);
        System.out.println("Falsche Zahlen gefunden: " + a);
    }

    public static boolean millerRabinTest(BigInteger n, int s) {
        if (n.equals(BIGINT2) || n.equals(BIGINT3) || n.equals(BIGINT5) || n.equals(BIGINT7)) {
            return true;
        }
        if (basicTests(n)) {
            ArrayList<BigInteger> listA = getA(n, s);
            int i = 0;
            for (int j = 0; j < listA.size(); j++) {
                if (isPrime(n, listA.get(j))) {
                    i++;
                } else {
                    return false;
                }
            }
            if (i == listA.size()) {
                //System.out.println("Unsicherheit " + 1 / Math.pow(4, i) * 100 + "%");
                return true;
            }
        }

        return false;
    }

    private static boolean basicTests(BigInteger n) {
        if (n.compareTo(BIGINT2) > 0) {
            if (!n.mod(BIGINT2).equals(BIGINT0)) {
                if (!n.mod(BIGINT3).equals(BIGINT0)) {
                    if (!n.mod(BIGINT5).equals(BIGINT0)) {
                        if (!n.mod(BIGINT7).equals(BIGINT0)) {
                            return true;
                        }
                    }
                }
            }

        }
        return false;
    }

    //n = natuerliche ungerade Zahl groesser 2 als Funktion einbinden
    private static boolean isPrime(BigInteger n, BigInteger a) {
        int j = 0;
        //n-1
        BigInteger n_1 = n.subtract(BIGINT1);

        while (n_1.divide(BIGINT2.pow(j)).mod(BIGINT2).compareTo(BIGINT0) == 0) {
            j++;
        }

        BigInteger d = n_1.divide(BIGINT2.pow(j));

        //System.out.println("j: " + j);
        //System.out.println("d: " + d);
        //System.out.println(n_1 + "=" + d + "*2^" + j);
        BigInteger y0;
        BigInteger y1 = a.modPow(d, n);
        //System.out.println(y1);

        if (y1.compareTo(BIGINT1) == 0) {
            return true;
        }
        if (y1.compareTo(n_1) == 0) {
            return true;
        }

        for (int i = 1; i < j; i++) {
            y0 = y1;
            y1 = y0.modPow(BIGINT2, n);
            //System.out.println(y1);
            //if (y1.compareTo(BigInteger.ONE) == 0 && y0.compareTo(BigInteger.ONE) != 0 && y0.compareTo(n_1) != 0) {
            if (y1.compareTo(n_1) == 0) {
                return true;
            }
        }
        return false;
    }

    // Choice the testing a's randomly from s or if is a small number with given a's -> (Pomerance, Selfridge, Wagstaff, Jaeschke)
    private static ArrayList<BigInteger> getA(BigInteger n, int s) {
        ArrayList<BigInteger> a = new ArrayList<>();

        if (n.compareTo(new BigInteger("1373653")) < 0) {
            a.add(BIGINT2);
            a.add(BIGINT3);
        } else if (n.compareTo(new BigInteger("9080191")) < 0) {
            a.add(BIGINT31);
            a.add(BIGINT73);
        } else if (n.compareTo(new BigInteger("4759123141")) < 0) {
            a.add(BIGINT2);
            a.add(BIGINT7);
            a.add(BIGINT61);
        } else if (n.compareTo(new BigInteger("2152302898747")) < 0) {
            a.add(BIGINT2);
            a.add(BIGINT3);
            a.add(BIGINT5);
            a.add(BIGINT7);
            a.add(BIGINT11);
        } else if (n.compareTo(new BigInteger("3474749660383")) < 0) {
            a.add(BIGINT2);
            a.add(BIGINT3);
            a.add(BIGINT5);
            a.add(BIGINT7);
            a.add(BIGINT11);
            a.add(BIGINT13);
        } else if (n.compareTo(new BigInteger("341550071728321")) < 0) {
            a.add(BIGINT2);
            a.add(BIGINT3);
            a.add(BIGINT5);
            a.add(BIGINT7);
            a.add(BIGINT11);
            a.add(BIGINT13);
            a.add(BIGINT17);
        } else {
            for (int i = 0; i < s; i++) {
                SecureRandom seed = new SecureRandom();
                BigInteger R;
                do {
                    R = new BigInteger(n.subtract(BIGINT2).bitLength(), seed).add(BIGINT1);
                } while (R.compareTo(n) > 0);
                a.add(R);
            }
        }
        return a;
    }
}
