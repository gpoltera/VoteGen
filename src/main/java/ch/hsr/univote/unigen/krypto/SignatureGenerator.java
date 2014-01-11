/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.krypto;

import ch.bfh.univote.common.Ballot;
import ch.bfh.univote.common.Ballots;
import ch.bfh.univote.common.BlindedGenerator;
import ch.bfh.univote.common.Candidate;
import ch.bfh.univote.common.Certificate;
import ch.bfh.univote.common.Choice;
import ch.bfh.univote.common.DecodedVote;
import ch.bfh.univote.common.DecodedVoteEntry;
import ch.bfh.univote.common.DecodedVotes;
import ch.bfh.univote.common.ElectionData;
import ch.bfh.univote.common.ElectionDefinition;
import ch.bfh.univote.common.ElectionGenerator;
import ch.bfh.univote.common.ElectionOptions;
import ch.bfh.univote.common.ElectionSystemInfo;
import ch.bfh.univote.common.ElectoralRoll;
import ch.bfh.univote.common.EncryptedVote;
import ch.bfh.univote.common.EncryptedVotes;
import ch.bfh.univote.common.EncryptionKey;
import ch.bfh.univote.common.EncryptionKeyShare;
import ch.bfh.univote.common.EncryptionParameters;
import ch.bfh.univote.common.ForallRule;
import ch.bfh.univote.common.MixedEncryptedVotes;
import ch.bfh.univote.common.MixedVerificationKey;
import ch.bfh.univote.common.MixedVerificationKeys;
import ch.bfh.univote.common.PartiallyDecryptedVotes;
import ch.bfh.univote.common.PoliticalList;
import ch.bfh.univote.common.Rule;
import ch.bfh.univote.common.Signature;
import ch.bfh.univote.common.SummationRule;
import ch.bfh.univote.common.VerificationKeys;
import ch.bfh.univote.common.VoterCertificates;
import ch.hsr.univote.unigen.common.StringConcatenator;
import ch.hsr.univote.unigen.generator.prov.TimestampGenerator;
import ch.hsr.univote.unigen.generator.prov.WahlGenerator;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

/**
 *
 * @author Gian & Copy From VoteVerifier
 */
public class SignatureGenerator {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final BigInteger[] old(BigInteger m, BigInteger p, BigInteger q, BigInteger g, BigInteger sk) throws NoSuchAlgorithmException {

        BigInteger r = PrimeGenerator.getPrime(1024).mod(q);
        byte[] gr = g.modPow(r, p).toByteArray();
        byte[] mgr = ByteUtils.concatenate(m.toByteArray(), gr);

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(mgr);

        BigInteger a = new BigInteger(md.digest());
        a = a.mod(q);

        BigInteger b = r.subtract(sk.multiply(a).mod(p));

        BigInteger[] S = new BigInteger[2];
        S[0] = b;
        S[1] = a;

        return S;
    }

    public static Signature createSignature(ElectionDefinition electionDefinition, RSAPrivateKey privateKey) throws Exception {
        Signature signature = new Signature();
        signature.setSignerId(ConfigHelper.getElectionAdministratorId());
        signature.setTimestamp(TimestampGenerator.generateTimestamp());

        //concatenate to (id|descr|keyLength|(t_1|...|t_n)|(m_1|...|m_n))|timestamp
        StringConcatenator sc = new StringConcatenator();
        sc.pushLeftDelim();

        sc.pushObjectDelimiter(electionDefinition.getElectionId(), StringConcatenator.INNER_DELIMITER);
        sc.pushObjectDelimiter(electionDefinition.getTitle(), StringConcatenator.INNER_DELIMITER);
        sc.pushObjectDelimiter(electionDefinition.getKeyLength(), StringConcatenator.INNER_DELIMITER);
        sc.pushList(electionDefinition.getTallierId(), true);
        sc.pushInnerDelim();
        sc.pushList(electionDefinition.getMixerId(), true);

        sc.pushRightDelim();
        sc.pushInnerDelim();
        sc.pushObject(signature.getTimestamp());

        String res = sc.pullAll();

        signature.setValue(RSASignatur.signRSA(res, privateKey));

        return signature;
    }

    public static Signature createSignature(ElectionOptions electionOptions, RSAPrivateKey privateKey) throws Exception {
        Signature signature = new Signature();
        signature.setSignerId(ConfigHelper.getElectionAdministratorId());
        signature.setTimestamp(TimestampGenerator.generateTimestamp());

        //concatenate to (id|(c1|....|cn)|(r1|...|rn))|timestamp
        StringConcatenator sc = new StringConcatenator();
        sc.pushLeftDelim();
        sc.pushObjectDelimiter(electionOptions.getElectionId(), StringConcatenator.INNER_DELIMITER);
        sc.pushLeftDelim();

        //for each choice
        for (int i = 0; i < electionOptions.getChoice().size(); i++) {
            Choice choice = electionOptions.getChoice().get(i);

            if (i > 0) {
                sc.pushInnerDelim();
            }

            sc.pushLeftDelim();
            sc.pushObjectDelimiter(choice.getChoiceId(), StringConcatenator.INNER_DELIMITER);

            if (choice instanceof PoliticalList) {
                PoliticalList politicalList = (PoliticalList) choice;
                sc.pushObjectDelimiter(politicalList.getNumber(), StringConcatenator.INNER_DELIMITER);
                sc.pushLocalizedText(politicalList.getTitle());
                sc.pushInnerDelim();
                sc.pushLocalizedText(politicalList.getPartyName());
                sc.pushInnerDelim();
                sc.pushLocalizedText(politicalList.getPartyShortName());

            } else if (choice instanceof Candidate) {
                Candidate candidate = (Candidate) choice;

                sc.pushObjectDelimiter(candidate.getNumber(), StringConcatenator.INNER_DELIMITER);
                sc.pushObjectDelimiter(candidate.getLastName(), StringConcatenator.INNER_DELIMITER);
                sc.pushObjectDelimiter(candidate.getFirstName(), StringConcatenator.INNER_DELIMITER);
                sc.pushObjectDelimiter(candidate.getSex(), StringConcatenator.INNER_DELIMITER);
                sc.pushObjectDelimiter(candidate.getYearOfBirth(), StringConcatenator.INNER_DELIMITER);

                //getStudyBranc ((lan|text)|...|(lan|text))
                sc.pushLocalizedText(candidate.getStudyBranch());
                sc.pushInnerDelim();

                //getStudyDegree ((lan|text)|...|(lan|text))
                sc.pushLocalizedText(candidate.getStudyDegree());
                sc.pushInnerDelim();

                sc.pushObjectDelimiter(candidate.getSemesterCount(), StringConcatenator.INNER_DELIMITER);
                sc.pushObjectDelimiter(candidate.getStatus(), StringConcatenator.INNER_DELIMITER);
                sc.pushObjectDelimiter(candidate.getListId(), StringConcatenator.INNER_DELIMITER);
                sc.pushObject(candidate.getCumulation());
            }

            sc.pushRightDelim();
        }

        sc.pushRightDelim();
        sc.pushInnerDelim();
        sc.pushLeftDelim();

        //for each rule
        for (int k = 0; k < electionOptions.getRule().size(); k++) {
            if (k > 0) {
                sc.pushInnerDelim();
            }

            Rule rule = electionOptions.getRule().get(k);
            int lowerBound = 0, upperBound = 0;

            sc.pushLeftDelim();

            if (rule instanceof SummationRule) {
                sc.pushObjectDelimiter("summationRule", StringConcatenator.INNER_DELIMITER);
                lowerBound = ((SummationRule) rule).getLowerBound();
                upperBound = ((SummationRule) rule).getUpperBound();
            } else if (rule instanceof ForallRule) {
                sc.pushObjectDelimiter("forallRule", StringConcatenator.INNER_DELIMITER);
                lowerBound = ((ForallRule) rule).getLowerBound();
                upperBound = ((ForallRule) rule).getUpperBound();
            }
            //array for the choices
            sc.pushLeftDelim();
            for (int f = 0; f < rule.getChoiceId().size(); f++) {
                if (f > 0) {
                    sc.pushInnerDelim();
                }

                sc.pushObject(rule.getChoiceId().get(f));
            }
            sc.pushRightDelim();
            sc.pushInnerDelim();
            sc.pushObjectDelimiter(lowerBound, StringConcatenator.INNER_DELIMITER);
            sc.pushObject(upperBound);

            sc.pushRightDelim();
        }

        sc.pushRightDelim();
        sc.pushRightDelim();
        sc.pushInnerDelim();
        sc.pushObject(signature.getTimestamp());

        String res = sc.pullAll();

        signature.setValue(RSASignatur.signRSA(res, privateKey));

        return signature;
    }

    public static Signature createSignature(ElectoralRoll electoralRoll, RSAPrivateKey privateKey) throws Exception {
        Signature signature = new Signature();
        signature.setSignerId(ConfigHelper.getElectionAdministratorId());
        signature.setTimestamp(TimestampGenerator.generateTimestamp());

        //concatenate to (id|(h1|...|hn))|timestamp
        StringConcatenator sc = new StringConcatenator();
        sc.pushLeftDelim();

        sc.pushObjectDelimiter(electoralRoll.getElectionId(), StringConcatenator.INNER_DELIMITER);
        sc.pushList(electoralRoll.getVoterHash(), true);

        sc.pushRightDelim();
        sc.pushInnerDelim();
        sc.pushObject(signature.getTimestamp());

        String res = sc.pullAll();

        signature.setValue(RSASignatur.signRSA(res, privateKey));

        return signature;
    }

    public static Signature createSignature(DecodedVotes decodedVotes, RSAPrivateKey privateKey) throws Exception {
        Signature signature = new Signature();
        signature.setSignerId(ConfigHelper.getElectionManagerId());
        signature.setTimestamp(TimestampGenerator.generateTimestamp());

        //concatenate to (id|(v1|v2|....|vn))|timestamp
        StringConcatenator sc = new StringConcatenator();
        sc.pushLeftDelim();
        sc.pushObjectDelimiter(decodedVotes.getElectionId(), StringConcatenator.INNER_DELIMITER);

        //( ((cID|count)|(cID|count))|...|((cID|count)|(cID|count)) )
        sc.pushLeftDelim();
        for (int i = 0; i < decodedVotes.getDecodedVote().size(); i++) {
            if (i > 0) {
                sc.pushInnerDelim();
            }

            DecodedVote decVote = decodedVotes.getDecodedVote().get(i);

            sc.pushLeftDelim();
            //(cID|count)|(cID|count)|....|(cID|count)
            for (int j = 0; j < decodedVotes.getDecodedVote().get(i).getEntry().size(); j++) {
                DecodedVoteEntry dve = decVote.getEntry().get(j);

                if (j > 0) {
                    sc.pushInnerDelim();
                }

                sc.pushLeftDelim();
                sc.pushObject(dve.getChoiceId());
                sc.pushInnerDelim();
                sc.pushObject(dve.getCount());
                sc.pushRightDelim();
            }
            sc.pushRightDelim();
        }
        sc.pushRightDelim();

        sc.pushRightDelim();
        sc.pushInnerDelim();
        sc.pushObject(signature.getTimestamp());

        String res = sc.pullAll();

        signature.setValue(RSASignatur.signRSA(res, privateKey));

        return signature;
    }

    public static Signature createSignature(EncryptionParameters encryptionParameters, RSAPrivateKey privateKey) throws Exception {
        Signature signature = new Signature();
        signature.setSignerId(ConfigHelper.getElectionManagerId());
        signature.setTimestamp(TimestampGenerator.generateTimestamp());

        BigInteger p = encryptionParameters.getPrime();
        BigInteger q = encryptionParameters.getGroupOrder();
        BigInteger g = encryptionParameters.getGenerator();

        //concatenate to (id|P|Q|G)|timestamp
        StringConcatenator sc = new StringConcatenator();
        sc.pushLeftDelim();
        sc.pushObjectDelimiter(encryptionParameters.getElectionId(), StringConcatenator.INNER_DELIMITER);
        sc.pushObjectDelimiter(p, StringConcatenator.INNER_DELIMITER);
        sc.pushObjectDelimiter(q, StringConcatenator.INNER_DELIMITER);
        sc.pushObjectDelimiter(g, StringConcatenator.RIGHT_DELIMITER);
        sc.pushInnerDelim();
        sc.pushObject(signature.getTimestamp());

        String res = sc.pullAll();

        signature.setValue(RSASignatur.signRSA(res, privateKey));

        return signature;
    }

    public static Signature createSignature(ElectionData electionData, RSAPrivateKey privateKey) throws Exception {
        Signature signature = new Signature();
        signature.setSignerId(ConfigHelper.getElectionManagerId());
        signature.setTimestamp(TimestampGenerator.generateTimestamp());

        //change this when it will be available - ToDo
        String eaIdentifier = WahlGenerator.eo.getSignature().getSignerId();

        //concatenate to (id|EA|descr|P|Q|G|y|g^|(c1|...|cn)|(r1|...|rn))|timestamp
        StringConcatenator sc = new StringConcatenator();
        sc.pushLeftDelim();
        sc.pushObjectDelimiter(electionData.getElectionId(), StringConcatenator.INNER_DELIMITER);
        sc.pushObjectDelimiter(eaIdentifier, StringConcatenator.INNER_DELIMITER);
        sc.pushObjectDelimiter(electionData.getTitle(), StringConcatenator.INNER_DELIMITER);
        sc.pushObjectDelimiter(electionData.getPrime(), StringConcatenator.INNER_DELIMITER);
        sc.pushObjectDelimiter(electionData.getGroupOrder(), StringConcatenator.INNER_DELIMITER);
        sc.pushObjectDelimiter(electionData.getGenerator(), StringConcatenator.INNER_DELIMITER);
        sc.pushObjectDelimiter(electionData.getEncryptionKey(), StringConcatenator.INNER_DELIMITER);
        sc.pushObjectDelimiter(electionData.getElectionGenerator(), StringConcatenator.INNER_DELIMITER);

        //choices and rules
        sc.pushLeftDelim();
        //for each choice
        for (int i = 0; i < electionData.getChoice().size(); i++) {
            Choice choice = electionData.getChoice().get(i);

            if (i > 0) {
                sc.pushInnerDelim();
            }

            sc.pushLeftDelim();
            sc.pushObjectDelimiter(choice.getChoiceId(), StringConcatenator.INNER_DELIMITER);

            if (choice instanceof PoliticalList) {
                PoliticalList pl = (PoliticalList) choice;

                sc.pushObjectDelimiter(pl.getNumber(), StringConcatenator.INNER_DELIMITER);

                sc.pushLocalizedText(pl.getTitle());

                sc.pushInnerDelim();
                sc.pushLocalizedText(pl.getPartyName());

                sc.pushInnerDelim();

                sc.pushLocalizedText(pl.getPartyShortName());

            } else if (choice instanceof Candidate) {
                Candidate candidate = (Candidate) choice;

                sc.pushObjectDelimiter(candidate.getNumber(), StringConcatenator.INNER_DELIMITER);
                sc.pushObjectDelimiter(candidate.getLastName(), StringConcatenator.INNER_DELIMITER);
                sc.pushObjectDelimiter(candidate.getFirstName(), StringConcatenator.INNER_DELIMITER);
                sc.pushObjectDelimiter(candidate.getSex(), StringConcatenator.INNER_DELIMITER);
                sc.pushObjectDelimiter(candidate.getYearOfBirth(), StringConcatenator.INNER_DELIMITER);

                //getStudyBranc ((lan|text)|...|(lan|text))
                sc.pushLocalizedText(candidate.getStudyBranch());

                sc.pushInnerDelim();

                //getStudyDegree ((lan|text)|...|(lan|text))
                sc.pushLocalizedText(candidate.getStudyDegree());

                sc.pushInnerDelim();

                sc.pushObjectDelimiter(candidate.getSemesterCount(), StringConcatenator.INNER_DELIMITER);
                sc.pushObjectDelimiter(candidate.getStatus(), StringConcatenator.INNER_DELIMITER);
                sc.pushObjectDelimiter(candidate.getListId(), StringConcatenator.INNER_DELIMITER);
                sc.pushObject(candidate.getCumulation());
            }

            sc.pushRightDelim();
        }

        sc.pushRightDelim();
        sc.pushInnerDelim();

        sc.pushLeftDelim();

        //for each rule
        for (int k = 0; k < electionData.getRule().size(); k++) {
            if (k > 0) {
                sc.pushInnerDelim();
            }

            Rule rule = electionData.getRule().get(k);
            int lowerBound = 0, upperBound = 0;

            sc.pushLeftDelim();

            if (rule instanceof SummationRule) {
                sc.pushObjectDelimiter("summationRule", StringConcatenator.INNER_DELIMITER);
                lowerBound = ((SummationRule) rule).getLowerBound();
                upperBound = ((SummationRule) rule).getUpperBound();
            } else if (rule instanceof ForallRule) {
                sc.pushObjectDelimiter("forallRule", StringConcatenator.INNER_DELIMITER);
                lowerBound = ((ForallRule) rule).getLowerBound();
                upperBound = ((ForallRule) rule).getUpperBound();
            }

            //array for the choices
            sc.pushLeftDelim();
            for (int f = 0; f < rule.getChoiceId().size(); f++) {
                if (f > 0) {
                    sc.pushInnerDelim();
                }

                sc.pushObject(rule.getChoiceId().get(f));
            }
            sc.pushRightDelim();
            sc.pushInnerDelim();
            sc.pushObjectDelimiter(lowerBound, StringConcatenator.INNER_DELIMITER);
            sc.pushObject(upperBound);

            sc.pushRightDelim();
        }

        sc.pushRightDelim();
        sc.pushRightDelim();
        sc.pushInnerDelim();
        sc.pushObject(signature.getTimestamp());

        String res = sc.pullAll();

        signature.setValue(RSASignatur.signRSA(res, privateKey));

        return signature;
    }

    public static Signature createSignature(Ballots bts, RSAPrivateKey privateKey) throws Exception {
        Signature signature = new Signature();
        signature.setSignerId(ConfigHelper.getElectionManagerId());
        signature.setTimestamp(TimestampGenerator.generateTimestamp());

        //concatenate to (id|ballotsstate|ballots)|timestamp
        StringConcatenator sc = new StringConcatenator();
        sc.pushLeftDelim();
        sc.pushObjectDelimiter(bts.getElectionId(), StringConcatenator.INNER_DELIMITER);

        //for each ballot
        sc.pushLeftDelim();
        for (int i = 0; i < bts.getBallot().size(); i++) {
            Ballot singleBallot = bts.getBallot().get(i);
            if (i > 0) {
                sc.pushInnerDelim();
            }

            sc.pushLeftDelim();
            sc.pushObjectDelimiter(singleBallot.getElectionId(), StringConcatenator.INNER_DELIMITER);

            //verification key
            sc.pushObjectDelimiter(singleBallot.getVerificationKey(), StringConcatenator.INNER_DELIMITER);

            //encrypted vote
            sc.pushLeftDelim();
            sc.pushObjectDelimiter(singleBallot.getEncryptedVote().getFirstValue(), StringConcatenator.INNER_DELIMITER);
            sc.pushObject(singleBallot.getEncryptedVote().getSecondValue());
            sc.pushRightDelim();

            sc.pushInnerDelim();

            //proof
            sc.pushLeftDelim();
            sc.pushList(singleBallot.getProof().getCommitment(), true);
            sc.pushInnerDelim();
            sc.pushList(singleBallot.getProof().getResponse(), true);
            sc.pushRightDelim();

            sc.pushInnerDelim();

            //voter signature
            sc.pushLeftDelim();
            sc.pushObjectDelimiter(singleBallot.getSignature().getFirstValue(), StringConcatenator.INNER_DELIMITER);
            sc.pushObject(singleBallot.getSignature().getSecondValue());
            sc.pushRightDelim();

            sc.pushRightDelim();
        }
        sc.pushRightDelim();

        sc.pushRightDelim();
        sc.pushInnerDelim();
        sc.pushObject(signature.getTimestamp());

        String res = sc.pullAll();

        signature.setValue(RSASignatur.signRSA(res, privateKey));

        return signature;
    }

    public static Signature createSignature(ElectionGenerator electionGenerator, RSAPrivateKey privateKey) throws Exception {
        Signature signature = new Signature();
        signature.setSignerId(ConfigHelper.getElectionManagerId());
        signature.setTimestamp(TimestampGenerator.generateTimestamp());

        //concatenate to (id|g^)|timestamp
        StringConcatenator sc = new StringConcatenator();
        sc.pushLeftDelim();
        sc.pushObjectDelimiter(electionGenerator.getElectionId(), StringConcatenator.INNER_DELIMITER);
        sc.pushObjectDelimiter(electionGenerator.getGenerator(), StringConcatenator.RIGHT_DELIMITER);
        sc.pushInnerDelim();
        sc.pushObject(signature.getTimestamp());

        String res = sc.pullAll();
        signature.setValue(RSASignatur.signRSA(res, privateKey));

        return signature;
    }

    public static Signature createSignature(ElectionSystemInfo electionSystemInfo, RSAPrivateKey privateKey) throws Exception {
        Signature signature = new Signature();
        signature.setSignerId(ConfigHelper.getElectionManagerId());
        signature.setTimestamp(TimestampGenerator.generateTimestamp());

        //concatenate to (id|(Z_t1|....|Z_tn)|(Z_m1|...|Z_mn))|timestamp
        StringConcatenator sc = new StringConcatenator();
        sc.pushLeftDelim();
        sc.pushObjectDelimiter(electionSystemInfo.getElectionId(), StringConcatenator.INNER_DELIMITER);
        sc.pushList(electionSystemInfo.getTallier(), true);
        sc.pushInnerDelim();
        sc.pushList(electionSystemInfo.getMixer(), true);
        sc.pushInnerDelim();
        sc.pushRightDelim();
        sc.pushInnerDelim();
        //get the timestamp when we will know where it is
        //sc.pushObject(signature.getTimestamp());

        String res = sc.pullAll();

        signature.setValue(RSASignatur.signRSA(res, privateKey));

        return signature;
    }

    public static Signature createSignature(EncryptedVotes encryptedVotes, RSAPrivateKey privateKey) throws Exception {
        Signature signature = new Signature();
        signature.setSignerId(ConfigHelper.getElectionAdministratorId());
        signature.setTimestamp(TimestampGenerator.generateTimestamp());

        //concatenate to (id|((firstValue|secondValue)|.......|(nfirstValue|nSecondValue)))|timestamp
        StringConcatenator sc = new StringConcatenator();
        sc.pushLeftDelim();
        sc.pushObjectDelimiter(encryptedVotes.getElectionId(), StringConcatenator.INNER_DELIMITER);
        sc.pushLeftDelim();

        for (int i = 0; i < encryptedVotes.getVote().size(); i++) {
            EncryptedVote e = encryptedVotes.getVote().get(i);

            if (i > 0) {
                sc.pushInnerDelim();
            }

            sc.pushLeftDelim();
            sc.pushObject(e.getFirstValue());
            sc.pushInnerDelim();
            sc.pushObject(e.getSecondValue());
            sc.pushRightDelim();
        }

        sc.pushRightDelim();
        sc.pushRightDelim();
        sc.pushInnerDelim();
        sc.pushObject(signature.getTimestamp());

        String res = sc.pullAll();

        signature.setValue(RSASignatur.signRSA(res, privateKey));

        return signature;
    }

    public static Signature createSignature(EncryptionKey encryptionKey, RSAPrivateKey privateKey) throws Exception {
        Signature signature = new Signature();
        signature.setSignerId(ConfigHelper.getElectionManagerId());
        signature.setTimestamp(TimestampGenerator.generateTimestamp());

        //concatenate to (id|y)|timestamp
        StringConcatenator sc = new StringConcatenator();
        sc.pushLeftDelim();
        sc.pushObjectDelimiter(encryptionKey.getElectionId(), StringConcatenator.INNER_DELIMITER);
        sc.pushObjectDelimiter(encryptionKey.getKey(), StringConcatenator.RIGHT_DELIMITER);
        sc.pushInnerDelim();
        sc.pushObject(signature.getTimestamp());

        String res = sc.pullAll();

        signature.setValue(RSASignatur.signRSA(res, privateKey));

        return signature;
    }

    public static Signature createSignature(String mixerName, BlindedGenerator blindedGenerator, RSAPrivateKey privateKey) throws Exception {
        Signature signature = new Signature();
        signature.setSignerId(mixerName);
        signature.setTimestamp(TimestampGenerator.generateTimestamp());

        //concatenate to (id|g_k|proof)|timestamp
        StringConcatenator sc = new StringConcatenator();
        sc.pushLeftDelim();

        sc.pushObjectDelimiter(blindedGenerator.getElectionId(), StringConcatenator.INNER_DELIMITER);
        sc.pushObjectDelimiter(blindedGenerator.getGenerator(), StringConcatenator.INNER_DELIMITER);
        //proof ((t|...|tn)|(s|...|sn))
        sc.pushLeftDelim();
        sc.pushList(blindedGenerator.getProof().getCommitment(), true);
        sc.pushInnerDelim();
        sc.pushList(blindedGenerator.getProof().getResponse(), true);
        sc.pushRightDelim();
        //end proof
        sc.pushRightDelim();
        sc.pushInnerDelim();
        sc.pushObject(signature.getTimestamp());

        String res = sc.pullAll();

        signature.setValue(RSASignatur.signRSA(res, privateKey));

        return signature;
    }

    public static Signature createSignature(VerificationKeys verificationKeys, RSAPrivateKey privateKey) throws Exception {
        Signature signature = new Signature();
        signature.setSignerId(ConfigHelper.getElectionManagerId());
        signature.setTimestamp(TimestampGenerator.generateTimestamp());

        //concatenate to (id|(vk1|...|vkn))|timestamp
        StringConcatenator sc = new StringConcatenator();
        sc.pushLeftDelim();
        sc.pushObjectDelimiter(verificationKeys.getElectionId(), StringConcatenator.INNER_DELIMITER);
        sc.pushList(verificationKeys.getKey(), true);
        sc.pushRightDelim();
        sc.pushInnerDelim();
        sc.pushObject(signature.getTimestamp());

        String res = sc.pullAll();

        signature.setValue(RSASignatur.signRSA(res, privateKey));

        return signature;
    }

    public static Signature createSignature(String mixerName, MixedVerificationKeys mixedVerificationKeys, RSAPrivateKey privateKey) throws Exception {
        Signature signature = new Signature();
        signature.setSignerId(mixerName);
        signature.setTimestamp(TimestampGenerator.generateTimestamp());

        //concatenate to (id|(vk1|...|vkn)|(()|()))|timestamp - WARNING the proof is empty.
        StringConcatenator sc = new StringConcatenator();
        sc.pushLeftDelim();

        sc.pushObjectDelimiter(mixedVerificationKeys.getElectionId(), StringConcatenator.INNER_DELIMITER);
        sc.pushList(mixedVerificationKeys.getKey(), true);
        sc.pushInnerDelim();

        //empty proof
        sc.pushLeftDelim();
        sc.pushLeftDelim();
        sc.pushRightDelim();
        sc.pushInnerDelim();
        sc.pushLeftDelim();
        sc.pushRightDelim();
        sc.pushRightDelim();
        //end empty proof

        sc.pushRightDelim();
        sc.pushInnerDelim();
        sc.pushObject(signature.getTimestamp());

        String res = sc.pullAll();

        signature.setValue(RSASignatur.signRSA(res, privateKey));

        return signature;
    }

    public static Signature createSignature(String tallierName, PartiallyDecryptedVotes partiallyDecryptedVotes, RSAPrivateKey privateKey) throws Exception {
        Signature signature = new Signature();
        signature.setSignerId(tallierName);
        signature.setTimestamp(TimestampGenerator.generateTimestamp());

        //concatenate to (id|(a_1|a_2|....|a_n)|proof)|timestamp
        StringConcatenator sc = new StringConcatenator();
        sc.pushLeftDelim();
        sc.pushObjectDelimiter(partiallyDecryptedVotes.getElectionId(), StringConcatenator.INNER_DELIMITER);
        sc.pushList(partiallyDecryptedVotes.getVote(), true);
        sc.pushInnerDelim();

        //push the proof
        sc.pushLeftDelim();
        sc.pushList(partiallyDecryptedVotes.getProof().getCommitment(), true);
        sc.pushInnerDelim();
        sc.pushList(partiallyDecryptedVotes.getProof().getResponse(), true);
        sc.pushRightDelim();
        //end proof

        sc.pushRightDelim();
        sc.pushInnerDelim();
        sc.pushObject(signature.getTimestamp());

        String res = sc.pullAll();

        signature.setValue(RSASignatur.signRSA(res, privateKey));

        return signature;
    }

    public static Signature createSignature(VoterCertificates voterCertificates, RSAPrivateKey privateKey) throws Exception {
        Signature signature = new Signature();
        signature.setSignerId(ConfigHelper.getElectionManagerId());
        signature.setTimestamp(TimestampGenerator.generateTimestamp());

        StringConcatenator sc = new StringConcatenator();
        sc.pushLeftDelim();
        sc.pushObjectDelimiter(voterCertificates.getElectionId(), StringConcatenator.INNER_DELIMITER);

        //for all certs
        sc.pushLeftDelim();
        for (int k = 0; k < voterCertificates.getCertificate().size(); k++) {
            if (k > 0) {
                sc.pushInnerDelim();
            }

            Certificate certificate = voterCertificates.getCertificate().get(k);
            sc.pushObject(new BigInteger(certificate.getValue()));
        }
        sc.pushRightDelim();
        sc.pushRightDelim();
        sc.pushInnerDelim();
        sc.pushObject(signature.getTimestamp());

        String res = sc.pullAll();

        signature.setValue(RSASignatur.signRSA(res, privateKey));
        return signature;
    }

    public static Signature createSignature(String tallierName, EncryptionKeyShare encryptionKeyShare, RSAPrivateKey privateKey) throws Exception {
        Signature signature = new Signature();
        signature.setSignerId(tallierName);
        signature.setTimestamp(TimestampGenerator.generateTimestamp());

        //concatenate to (id|y_j|(t|s))|timestamp
        StringConcatenator sc = new StringConcatenator();
        sc.pushLeftDelim();

        sc.pushObjectDelimiter(encryptionKeyShare.getElectionId(), StringConcatenator.INNER_DELIMITER);
        sc.pushObjectDelimiter(encryptionKeyShare.getKey(), StringConcatenator.INNER_DELIMITER);
        //proof ((t|...|tn)|(s|...|sn))
        sc.pushLeftDelim();
        sc.pushList(encryptionKeyShare.getProof().getCommitment(), true);
        sc.pushInnerDelim();
        sc.pushList(encryptionKeyShare.getProof().getResponse(), true);
        sc.pushRightDelim();
        //end proof

        sc.pushRightDelim();
        sc.pushInnerDelim();
        sc.pushObject(signature.getTimestamp());

        String res = sc.pullAll();
        signature.setValue(RSASignatur.signRSA(res, privateKey));

        return signature;
    }

    public static Signature createSignature(String mixerName, MixedEncryptedVotes mixedEncryptedVotes, RSAPrivateKey privateKey) throws Exception {
        Signature signature = new Signature();
        signature.setSignerId(mixerName);
        signature.setTimestamp(TimestampGenerator.generateTimestamp());

        //concatenate to (id|((firstValue|secondValue)|.......|(nfirstValue|nSecondValue))(()|()))|timestamp - WARNING :the proof is not implemented
        StringConcatenator sc = new StringConcatenator();
        sc.pushLeftDelim();
        sc.pushObjectDelimiter(mixedEncryptedVotes.getElectionId(), StringConcatenator.INNER_DELIMITER);
        sc.pushLeftDelim();
        //for each vote
        for (int i = 0; i < mixedEncryptedVotes.getVote().size(); i++) {
            EncryptedVote e = mixedEncryptedVotes.getVote().get(i);

            if (i > 0) {
                sc.pushInnerDelim();
            }

            sc.pushLeftDelim();
            sc.pushObject(e.getFirstValue());
            sc.pushInnerDelim();
            sc.pushObject(e.getSecondValue());
            sc.pushRightDelim();
        }
        sc.pushRightDelim();
        //Empty proof
        sc.pushInnerDelim();
        sc.pushLeftDelim();
        sc.pushLeftDelim();
        sc.pushRightDelim();
        sc.pushInnerDelim();
        sc.pushLeftDelim();
        sc.pushRightDelim();
        sc.pushRightDelim();

        sc.pushRightDelim();
        sc.pushInnerDelim();
        sc.pushObject(signature.getTimestamp());

        String res = sc.pullAll();
        signature.setValue(RSASignatur.signRSA(res, privateKey));

        return signature;
    }

    public static Signature createSignature(String mixerName, MixedVerificationKey mixedVerificationKey, RSAPrivateKey privateKey) throws Exception {
        Signature signature = new Signature();
        signature.setSignerId(mixerName);
        signature.setTimestamp(TimestampGenerator.generateTimestamp());

        //concatenate to (id|vk|(()|()))|timestamp - WARNING: The proof is not yet implemented.
        StringConcatenator sc = new StringConcatenator();
        sc.pushLeftDelim();

        sc.pushObjectDelimiter(mixedVerificationKey.getElectionId(), StringConcatenator.INNER_DELIMITER);
        sc.pushObjectDelimiter(mixedVerificationKey.getKey(), StringConcatenator.INNER_DELIMITER);

        //empty proof
        sc.pushLeftDelim();
        sc.pushList(mixedVerificationKey.getProof().getCommitment(), true);
        sc.pushInnerDelim();
        sc.pushList(mixedVerificationKey.getProof().getResponse(), true);
        sc.pushRightDelim();
        //end empty proof

        sc.pushRightDelim();
        sc.pushInnerDelim();
        sc.pushObject(signature.getTimestamp());

        String res = sc.pullAll();

        signature.setValue(RSASignatur.signRSA(res, privateKey));

        return signature;
    }
}
