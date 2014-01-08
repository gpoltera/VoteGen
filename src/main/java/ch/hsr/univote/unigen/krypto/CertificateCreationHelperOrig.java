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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.Calendar;
import java.util.Date;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.DigestInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.TBSCertificateStructure;
import org.bouncycastle.asn1.x509.Time;
import org.bouncycastle.asn1.x509.V3TBSCertificateGenerator;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.jce.PrincipalUtil;
import org.bouncycastle.jce.provider.X509CertificateObject;
import sun.security.provider.DSAPublicKeyImpl;

/**
 * Helper class allowing for creating X509 certificates for testing
 * purposes only. Not used in production code.
 *
 * @author Eric Dubuis &lt;eric.dubuis@bfh.ch&gt;
 */
public class CertificateCreationHelperOrig {

    /**
     * Test value for key store file name.
     */
    private static final String DEFAULT_KEYSTORE_NAME = "TestCA.jks";
    //private static final String DEFAULT_KEYSTORE_NAME = "test.jks";
    /**
     * Test value for certificate alias of certificate of registration.
     */
    private static final String DEFAULT_CA_ALIAS = "testca";
    /**
     * Test value for private key entry matching certificate of registration.
     */
    private static final String DEFAULT_CA_PK_PASSWORD = "testCA";
    /**
     * Test value for accessing key store.
     */
    private static final String DEFAULT_CA_PASSWORD = "testCA";

    /** Test value for Schnorr parameter p. */
    private static final BigInteger DEFAULT_P = new BigInteger("167");
    /** Test value for Schnorr parameter q. */
    private static final BigInteger DEFAULT_Q = new BigInteger("83");
    /** Test value for Schnorr parameter g. */
    private static final BigInteger DEFAULT_G = new BigInteger("11");

    /** Test value for RSA parameter n. */
    private static final BigInteger DEFAULT_N = new BigInteger("143");
    /** Test value for RSA parameter exp. */
    private static final BigInteger DEFAULT_EXP = new BigInteger("23");
    /** Test value for RSA parameter d. */
    private static final BigInteger DEFAULT_D = new BigInteger("47");

    /**
     * Default validity in terms of years of generated certificates.
     * For production, injected value must be used instead.
     */
    private static final Integer DEFAULT_VALIDITY_YEARS = 2;

    /**
     * The certificate for the registration, retrieved from the key store.
     */
    private X509Certificate caCert;
    /**
     * The in-memory representation of the private key of the registration.
     */
    private RSAPrivateCrtKeyParameters caPrivateKey;

    /**
     * Constructs and initializes a helper instance allowing to create
     * certificates.
     * @throws Exception if there is a problem during its initialization
     */
    public CertificateCreationHelperOrig() throws Exception {
        // Load CA keystore.
        KeyStore keystore = loadKeyStore();

        // Get CA certificate and remember it in instance field.
        caCert = getIssuerCertificate(keystore);

        // Get CA private key in order to sign certificate just created
        // and remeber it in instance field.
        caPrivateKey = getIssuerPrivateKey(keystore);
    }

    /**
     * Creates the certificate for the requestor.
     * @param voterId the identity of a (potential) voter, used to form the value for CN
     * @param organisation the name of the university or university of applied sciences, used to form the value O
     * @param studyBranch the identification of the study branch, used to form the value for OU
     * @param uid a unique identifier for the subject
     * @param publicKey the public key of the voter, also known as verification key
     * @return a certificate chain consisting of the certificate of the
     * requestor and the certificate of the registration
     */
	public X509Certificate createCertificate(
        String voterId,
        String organisation,
        String studyBranch,
        String uid,
        BigInteger publicKey)
        throws Exception
    {
		DSAPublicKey dpk = createDSAPublicKey(publicKey);

        Calendar expiry = getExpiryDate();

        X509Certificate clientCert =
            createClientCertificate(voterId, organisation, studyBranch, uid, caCert, caPrivateKey, dpk, expiry);

		return clientCert;
	}

    /**
     * Creates the DSA public key of the requestor.
     * @param publicKey the value for the public key
     * @return the DSA object representing the DSA public key
     * @throws Exception if there is an error
     */
    private DSAPublicKey createDSAPublicKey(BigInteger publicKey) throws Exception {
		DSAPublicKey dpk;
		try {
			dpk = new DSAPublicKeyImpl(publicKey, getP(), getQ(), getQ());
		} catch (InvalidKeyException ex) {
			throw new Exception(ex);
		}
        return dpk;
    }

    /**
     * Returns the calendar representing the expiration date.
     * @return a calendar
     */
    private Calendar getExpiryDate() {
		Calendar expiry = Calendar.getInstance();
        expiry.add(Calendar.YEAR, getValidityYears());
        return expiry;
    }


    /**
     * Returns publicly known parameter prime p for Schnorr signatures.
     * @return a prime
     */
	private BigInteger getP() {
        return DEFAULT_P;
	}

    /**
     * Returns publicly known parameter prime q for Schnorr signatures.
     * @return a prime
     */
	private BigInteger getQ() {
        return DEFAULT_Q;
	}

    /**
     * Returns publicly known generator g for Schnorr signatures.
     * @return a generator
     */
    private BigInteger getG() {
        return DEFAULT_G;
	}

    /**
     * Returns the number of years the certificate will be valid for.
     * @return number of years
     */
	private Integer getValidityYears() {
        return DEFAULT_VALIDITY_YEARS;
	}

    /**
     * Effectively creates the requestor certificate.
     * @param voterId the identity of a (potential) voter, used to form the value for CN
     * @param organisation the name of the university or university of applied sciences, used to form the value O
     * @param studyBranch the identification of the study branch, used to form the value for OU
     * @param uid a unique identifier for the subject
     * @param caCert issuer certificate
     * @param caPrivateKey issuer private key used for signing
     * @param dpk the voter public key
     * @param expiry the expiry date
     * @return a certificate
     * @throws Exception
     */
    private X509Certificate createClientCertificate(
        String voterId,
        String organisation,
        String studyBranch,
        String uid,
        X509Certificate caCert,
        CipherParameters caPrivateKey,
        DSAPublicKey dpk,
        Calendar expiry)
        throws Exception
    {
        X509Certificate clientCert;
        try {
            X509Name x509Name = new X509Name(
                "CN=" + voterId +
                ", O=" + organisation +
                ", OU=" + studyBranch +
                ", UID=" + uid);

            V3TBSCertificateGenerator certGen = new V3TBSCertificateGenerator();
            certGen.setSerialNumber(new DERInteger(BigInteger.valueOf(System.currentTimeMillis())));
            certGen.setIssuer(PrincipalUtil.getSubjectX509Principal(caCert));
            certGen.setSubject(x509Name);
            DERObjectIdentifier sigOID = new DERObjectIdentifier("1.2.840.113549.1.1.5");
            AlgorithmIdentifier sigAlgId = new AlgorithmIdentifier(sigOID, new DERNull());
            certGen.setSignature(sigAlgId);
            certGen.setSubjectPublicKeyInfo(new SubjectPublicKeyInfo((ASN1Sequence) new ASN1InputStream(
                new ByteArrayInputStream(dpk.getEncoded())).readObject()));
            certGen.setStartDate(new Time(new Date(System.currentTimeMillis())));
            certGen.setEndDate(new Time(expiry.getTime()));
            TBSCertificateStructure tbsCert = certGen.generateTBSCertificate();

            //Sign certificate
            SHA1Digest digester = new SHA1Digest();
            AsymmetricBlockCipher rsa = new PKCS1Encoding(new RSAEngine());
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            DEROutputStream dOut = new DEROutputStream(bOut);
            dOut.writeObject(tbsCert);
            byte[] signature;
            byte[] certBlock = bOut.toByteArray();
            // first create digest
            digester.update(certBlock, 0, certBlock.length);
            byte[] hash = new byte[digester.getDigestSize()];
            digester.doFinal(hash, 0);
            // then sign it
            rsa.init(true, caPrivateKey);
            DigestInfo dInfo = new DigestInfo(new AlgorithmIdentifier(X509ObjectIdentifiers.id_SHA1, null), hash);
            byte[] digest = dInfo.getEncoded(ASN1Encodable.DER);
            signature = rsa.processBlock(digest, 0, digest.length);

            ASN1EncodableVector v = new ASN1EncodableVector();
            v.add(tbsCert);
            v.add(sigAlgId);
            v.add(new DERBitString(signature));

            // Create CRT data structure
            clientCert = new X509CertificateObject(new X509CertificateStructure(new DERSequence(v)));
            clientCert.verify(caCert.getPublicKey());
        } catch (IOException | InvalidCipherTextException | CertificateException | NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException | SignatureException e) {
            throw new Exception(e);
        }
        return clientCert;
    }

    /**
     * Loads the key store of the issuer.
     * @return a key store
     * @throws Exception if there is an error
     */
    private KeyStore loadKeyStore() throws Exception {
 		KeyStore keystore;
        InputStream in = null;
		try {
			keystore = KeyStore.getInstance(System.getProperty("javax.net.ssl.keyStoreType", "jks"));
            // Let's try to get the input stream for the key store. This depends
            // on how the bean is deployed. Thus, we try differnt was until we
            // give up.
            in = CertificateCreationHelperOrig.class.getResourceAsStream("/" + getKeyStoreName());
            if (in == null) {
                in = CertificateCreationHelperOrig.class.getResourceAsStream("/META-INF/" + getKeyStoreName());
            }
            if (in == null) {
                throw new Exception("Could not initialize key store");
            }
			keystore.load(in, getCaPassword().toCharArray());
		} catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException ex) {
			throw new Exception("Could not load key store", ex);
		} finally {
            if (in != null) {
                try { in.close(); } catch(Exception e) { /* intentionally left empty */ }
            }
        }
        return keystore;
    }

    /**
     * Returns the configured name of the key store to load.
     * @return a name
     */
    private String getKeyStoreName() {
        return DEFAULT_KEYSTORE_NAME;
	}

    /**
     * Returns the configured issuer password for the key store.
     * @return a name
     */
	private String getCaPassword() {
        return DEFAULT_CA_PASSWORD;
	}

    /**
     * Returns the certificate of the issuer.
     * @param keystore a key store
     * @return the issuer certificate
     * @throws Exception if there is an error
     */
    private X509Certificate getIssuerCertificate(KeyStore keystore) throws Exception {
        X509Certificate cert;
        try {
            cert = (X509Certificate) keystore.getCertificate(getCaAlias());
            if (cert == null) {
                throw new RuntimeException("No certificate found from keystore for alias: " + getCaAlias());
            }
            cert.verify(cert.getPublicKey());
        } catch (RuntimeException | CertificateException | NoSuchAlgorithmException
            | InvalidKeyException | NoSuchProviderException | SignatureException ex) {
			throw new Exception(ex);
        }
        return cert;
    }

    /**
     * Returns the configured certificate alias for the issuer certificate..
     * @return a name
     */
    private String getCaAlias() {
        return DEFAULT_CA_ALIAS;
	}

    /**
     * Returns the private key of the issuer.
     * @param keystore a key store
     * @return a private key
     * @throws Exception if there is an error
     */
    private RSAPrivateCrtKeyParameters getIssuerPrivateKey(KeyStore keystore) throws Exception {
        RSAPrivateCrtKeyParameters privateKey;
        try {
            Key key = keystore.getKey(getCaAlias(), getCaPKPassword().toCharArray());
            if (key == null) {
                throw new RuntimeException("No key found from keystore for name: " + getCaAlias());
            }
            RSAPrivateCrtKey privKey = (RSAPrivateCrtKey) key;
            privateKey =
                new RSAPrivateCrtKeyParameters(privKey.getModulus(), privKey.getPublicExponent(),
                    privKey.getPrivateExponent(), privKey.getPrimeP(), privKey.getPrimeQ(),
                privKey.getPrimeExponentP(), privKey.getPrimeExponentQ(), privKey.getCrtCoefficient());
        } catch (RuntimeException ex) {
			throw new Exception(ex);
        }
        return privateKey;
    }

    /**
     * Returns the configured issuer password for the private
     * key entry in the key store.
     * @return a name
     */
    private String getCaPKPassword() {
        return DEFAULT_CA_PK_PASSWORD;
	}
}