/*
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland.
 * Bern University of Applied Sciences, Engineering and Information Technology,
 * Research Institute for Security in the Information Society, E-Voting Group,
 * Biel, Switzerland.
 *
 * Project UniVote.
 *
 * Distributable under GPL license.
 * See terms of license at gnu.org.
 */
package ch.hsr.univote.unigen.krypto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import org.bouncycastle.openssl.PEMWriter;

/**
 * Helper to convert X509 certificates to base 64 encoded strings, JAXB structures,
 * and vice versa.
 *
 * @author Eric Dubuis &lt,eric.dubuis@bfh.ch&gt;
 */
public class CertificateHelperOrig {
    /**
     * Not used.
     */
    private CertificateHelperOrig() {
    }

    /**
     * Converts a X509Certificate instance into a Base64 encoded string (PEM format).
     * @param cert a certificate
     * @return a string (PEM format)
     * @throws IOException if the conversion fails
     */
    public static String x509ToBase64PEMString(X509Certificate cert) throws IOException {
        // Convert certificate to PEM format.
        StringWriter sw = new StringWriter();
        try (PEMWriter pw = new PEMWriter(sw)) {
            pw.writeObject(cert);
            pw.flush();
            pw.close();
        }
        return sw.toString();
    }

    /**
     * Converts a string of a Base64 encoded certificate (PEM format) into a byte array.
     * @param str a string representing a Base 64 encoded X509 certificate (PEM format)
     * @return a byte array
     */
    public static byte[] base64PEMStringToByteArray(String str) {
        return str.getBytes();
    }

    /**
     * Converts a byte array to a X509Certificate instance.
     * @param bytes the byte array
     * @return a X509Certificate instance
     * @throws CertificateException if the conversion fails
     */
    public static X509Certificate fromByteArrayToX509Certificate(byte[] bytes) throws CertificateException {
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        InputStream in = new ByteArrayInputStream(bytes);
        X509Certificate cert = (X509Certificate) certFactory.generateCertificate(in);
        return cert;
    }

    /**
     * Converts the given data into a JAXP Certificate instance.
     * @param canonicalName the name (CN) of the certificate holder
     * @param organization the organization (O)
     * @param organizationUnit the organization unit (OU)
     * @param uid the unique identifier (UID)
     * @param issuer the name of the issuer
     * @param serialNumber the serial number of the certificate
     * @param validFrom the revoked-from date
     * @param validUntil the revoked-until date
     * @param fpSha1 the SHA-1 fingerprint
     * @param pem the certificate as a PEM structure
     * @param revoked a flag indicating whether the certificate is revoked or not
     * @return a JAXB Certificate instance
     */
   // public static Certificate certToJAXB(String canonicalName, String organization,
    //    String organizationUnit, String uid, String issuer, BigInteger serialNumber,
     //   Date validFrom, Date validUntil, String fpSha1, String pem, boolean revoked) {
      //  Certificate cert = new Certificate(
       //     canonicalName,
        //    organization,
         //   organizationUnit,
          //  uid,
          //  issuer,
          //  serialNumber,
          //  validFrom,
          //  validUntil,
          //  fpSha1,
          //  pem,
          //  revoked);
        //return cert;
    //}
}

