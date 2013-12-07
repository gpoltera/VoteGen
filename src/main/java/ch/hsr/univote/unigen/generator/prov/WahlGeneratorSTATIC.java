/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator.prov;

import ch.bfh.univote.common.Ballot;
import ch.bfh.univote.common.Ballots;
import ch.bfh.univote.common.BallotsState;
import ch.bfh.univote.common.BlindedGenerator;
import ch.bfh.univote.common.Candidate;
import ch.bfh.univote.common.CandidateStatus;
import ch.bfh.univote.common.Certificate;
import ch.bfh.univote.common.Choice;
import ch.bfh.univote.common.DecodedVote;
import ch.bfh.univote.common.DecodedVoteEntry;
import ch.bfh.univote.common.DecodedVotes;
import ch.bfh.univote.common.DecryptedVotes;
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
import ch.bfh.univote.common.KnownElectionIds;
import ch.bfh.univote.common.LanguageCode;
import ch.bfh.univote.common.LocalizedText;
import ch.bfh.univote.common.MixedEncryptedVotes;
import ch.bfh.univote.common.MixedVerificationKey;
import ch.bfh.univote.common.MixedVerificationKeys;
import ch.bfh.univote.common.MixerInfo;
import ch.bfh.univote.common.NewCertificate;
import ch.bfh.univote.common.ObjectFactory;
import ch.bfh.univote.common.PartiallyDecryptedVotes;
import ch.bfh.univote.common.PoliticalList;
import ch.bfh.univote.common.Proof;
import ch.bfh.univote.common.Rule;
import ch.bfh.univote.common.Sex;
import ch.bfh.univote.common.Signature;
import ch.bfh.univote.common.SignatureParameters;
import ch.bfh.univote.common.VerificationKeys;
import ch.bfh.univote.common.VoterCertificates;
import ch.bfh.univote.common.VoterSignature;
import ch.hsr.univote.unigen.krypto.CertificateGenerator;
import ch.hsr.univote.unigen.krypto.CertificateHelper;
import ch.hsr.univote.unigen.krypto.PrimeGenerator;
import ch.hsr.univote.unigen.krypto.RSASignatur;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;

/**
 *
 * @author Gian Poltéra
 */

public class WahlGeneratorSTATIC {

    public static SignatureParameters sp = new SignatureParameters();
    public static Certificate cert = new Certificate();
    public static Certificate cert2 = new Certificate();
    public static ElectionSystemInfo esi = new ElectionSystemInfo();
    public static ElectionDefinition ed = new ElectionDefinition();
    public static EncryptionParameters ep = new EncryptionParameters();
    public static EncryptionKeyShare eks = new EncryptionKeyShare();
    public static KnownElectionIds kei = new KnownElectionIds();
    public static Ballots bts = new Ballots();
    public static Ballot bt = new Ballot();
    public static VoterCertificates vc = new VoterCertificates();
    public static Signature sg = new Signature();
    public static ElectionOptions eo = new ElectionOptions();
    public static ElectionGenerator eg = new ElectionGenerator();
    public static EncryptionKey ek = new EncryptionKey();
    public static BlindedGenerator bg = new BlindedGenerator();
    public static ElectionData edat = new ElectionData();
    public static ElectoralRoll er = new ElectoralRoll();
    public static MixedVerificationKeys mvk = new MixedVerificationKeys();
    public static VerificationKeys vk = new VerificationKeys();
    public static List<Certificate> lcert = new ArrayList<Certificate>();
    public static List<MixedVerificationKey> lmvk = new ArrayList<MixedVerificationKey>();
    public static MixedEncryptedVotes mev = new MixedEncryptedVotes();
    public static EncryptedVotes ev = new EncryptedVotes();
    public static PartiallyDecryptedVotes pdv = new PartiallyDecryptedVotes();
    public static DecryptedVotes dyv = new DecryptedVotes();
    public static DecodedVotes dov = new DecodedVotes();
    public static EncryptedVote ec = new EncryptedVote();
    
    public static void addElectionId(String electionId) throws DatatypeConfigurationException, FileNotFoundException, FileNotFoundException, CertificateException, Exception {
        

        
        sg.setSignerId("electionmanager");
        sg.setValue(new BigInteger("49808603764619692678519637681037419089801926169578084316514931428096008442861576140198870404390375902395003604992838425680726410238888641155614175713597215450635928253311928454417345484387865476886719383127751707951994907912583299908353352002373847599087868798405581021640997487357009216507332296576608724325"));
        sg.setTimestamp(TimestampGenerator.generateTimestamp());

        cert.setValue(CertificateHelper.base64PEMStringToByteArray(CertificateGenerator.main(null)));
        
        kei.getElectionId().add(electionId);
        
        ep.setElectionId(electionId);
        ep.setGenerator(new BigInteger("109291242937709414881219423205417309207119127359359243049468707782004862682441897432780127734395596275377218236442035534825283725782836026439537687695084410797228793004739671835061419040912157583607422965551428749149162882960112513332411954585778903685207256083057895070357159920203407651236651002676481874709"));
        ep.setGroupOrder(new BigInteger("65133683824381501983523684796057614145070427752690897588060462960319251776021"));
        ep.setPrime(new BigInteger("161931481198080639220214033595931441094586304918402813506510547237223787775475425991443924977419330663170224569788019900180050114468430413908687329871251101280878786588515668012772798298511621634145464600626619548823238185390034868354933050128115662663653841842699535282987363300852550784188180264807606304297"));
        ep.setSignature(sg);
        
        MixerInfo mi = new MixerInfo();
        mi.setCertificate(cert);
        mi.setEndpointUrl(electionId);
        mi.setIdentifier(electionId);
        mi.setSignature(sg);
        
        NewCertificate nc = new NewCertificate();
        nc.setElectionId(electionId);
        nc.setKey(new BigInteger("1024"));
        nc.setSignature(sg);
        
        ed.setElectionId(electionId);
        ed.setKeyLength(256);
        ed.setTitle(electionId);
        ed.setVotingPhaseBegin(TimestampGenerator.generateTimestamp());
        ed.setVotingPhaseEnd(TimestampGenerator.generateTimestamp());
        ed.getMixerId().addAll(MixerGenerator.generateMixers(10));
        ed.getTallierId().addAll(TallierGenerator.generateTalliers(10));
        ed.setSignature(RSASignatur.createSignature(ed));
        
        esi.setElectionId(electionId);
        esi.setCertificateAuthority(cert);
        esi.setElectionAdministration(cert);
        esi.setElectionManager(cert);
        esi.setSignature(RSASignatur.createSignature(ed));
        esi.getMixer().add(cert);
        esi.getTallier().add(cert);
        
        ec.setFirstValue(new BigInteger ("70704691770484788690714322617450920308445476854899536590803945068498160985209626633048425882290920095666058938286331514764808568259086126645597349237912987283476854491616966485012781429820054886368801419311649478707304478216517693337520602702778433083989065606057549625925936848752984036736676531131368011385"));
        ec.setSecondValue(new BigInteger ("122181888589865715269751030902285609884184801851247258101141396133355466299519608935670443846681174946410944847301784935722390042407588379730126276952611447848597187467826554759786759339980931917153838743140601445983871113334892104443104643789852382074116091216648628877812439640150660576396342325170247156806"));
        
        VoterSignature vs = new VoterSignature();
        vs.setFirstValue(new BigInteger("232234234234234234234324"));
        vs.setSecondValue(new BigInteger("43245454545545445532234"));
        
        Proof proof = new Proof();
        proof.getCommitment().add(new BigInteger("150343273013956950096587520643430064628880106142074020456319011098765487479623594894074898920310660551569568367876815380102141916320790467084676598694567712922730679800096403736580672168288765006061984400662051622032818670240061050669976777709885028502236772046526075629475890598922266090991003285933492868352"));
        proof.getResponse().add(new BigInteger("78585941875708237817629315327380917427004969785835675525984521535273212763728794737784357378573410253660195578273537809623026936114073293558271876170972472948105627491339264851170952863184351246922900400200169610944594040062243402311519118746038920602441288799306713624007746686847790983625996361846015392086"));
        proof.getCommitment().set(0, BigInteger.TEN);
        proof.getResponse().set(0, BigInteger.TEN);
        
        bt.setElectionId(electionId);
        bt.setEncryptedVote(ec);
        bt.setProof(proof);
        bt.setSignature(vs);
        bt.setVerificationKey(new BigInteger("101905244440216074538805999004329016420625572973059875300124629445480799897244312484160619745940044068334351597082601626627689116612763592715510701592578510934262413998368223212119676981642495885762705771355192457679782193188477952567351454405570759206716742452684923519572379243834326357319090964514453326429"));
        
        bts.getBallot().add(bt);
        bts.setElectionId(electionId);
        bts.setBallotsState(BallotsState.VALID);
        bts.setSignature(sg);
       
        sp.setGenerator(new BigInteger("12323"));
        sp.setGroupOrder(new BigInteger("125555323"));
        sp.setPrime(PrimeGenerator.getPrime(1024));
        sp.setSignature(RSASignatur.createSignature(ed));
        
        vc.setElectionId(electionId);
        vc.setSignature(sg);
        vc.getCertificate().add(cert);
        
        Candidate candidate = new Candidate();
        candidate.setChoiceId(1);
        candidate.setCumulation(1);
        candidate.setFirstName("Peter");
        candidate.setLastName("Mueller");
        candidate.setListId(1);
        candidate.setNumber("1");
        candidate.setSemesterCount(1);
        candidate.setSex(Sex.M);
        candidate.setStatus(CandidateStatus.NEW);
        candidate.setYearOfBirth(1986);
        
        LocalizedText lt = new LocalizedText();
        lt.setLanguage(LanguageCode.DE);
        lt.setText("Informatik");
        
        candidate.getStudyBranch().add(lt);
        
        lt.setText("Bachelor");
        candidate.getStudyDegree().add(lt);
        
        lt.setText("TR");        
        
        PoliticalList pl = new PoliticalList();
        pl.setChoiceId(1);
        pl.setNumber("1");
        pl.getPartyName().add(lt);
        pl.getPartyShortName().add(lt);
        pl.getTitle().add(lt);
        
        ForallRule fr = new ForallRule();
        fr.setLowerBound(1);
        fr.setUpperBound(2);
        fr.getChoiceId().add(candidate.getChoiceId());        
        
        //Choice choice = Choice.class.newInstance().setChoiceId(1);
        
        //choice.setChoiceId(1);
        
        //Rule rule;
        //rule = new Rule() {};
        //rule.getChoiceId().add(candidate.getChoiceId());
        
        eo.setElectionId(electionId);
        eo.setSignature(sg);
        //eo.getChoice().add(choice);
        //eo.getRule().add(rule);
        
        eg.setElectionId(electionId);
        eg.setGenerator(new BigInteger("23"));
        eg.setSignature(sg);
        
        ek.setElectionId(electionId);
        ek.setKey(new BigInteger("234"));
        ek.setSignature(sg);
        
        bg.setElectionId(electionId);
        bg.setGenerator(new BigInteger("59"));
        bg.setProof(proof);
        bg.setSignature(sg);
        
        edat.setElectionGenerator(new BigInteger("2323"));
        edat.setElectionId(electionId);
        edat.setEncryptionKey(new BigInteger("23243"));
        edat.setGenerator(new BigInteger("4656"));
        edat.setGroupOrder(new BigInteger("4567"));
        edat.setPrime(PrimeGenerator.getPrime(1024));
        
        edat.setSignature(sg);
        edat.setTitle(electionId);
        //edat.getChoice().add(choice);
        //edat.getRule().add(rule);
        
        er.setElectionId("Giani");
        er.setSignature(sg);
        er.getVoterHash().add(new BigInteger("03498"));
        
        mvk.setElectionId(electionId);
        mvk.setProof(proof);
        mvk.setSignature(sg);
        mvk.getKey().add(new BigInteger("234324"));
        
        vk.setElectionId(electionId);
        vk.setSignature(sg);
        vk.getKey().add(new BigInteger("323424"));
        
        lcert.add(cert);
        
        MixedVerificationKey mvkk = new MixedVerificationKey();
        mvkk.setElectionId(electionId);
        mvkk.setKey(new BigInteger("29938"));
        mvkk.setProof(proof);
        mvkk.setSignature(sg);

        lmvk.add(mvkk);
        
        mev.setElectionId(electionId);
        mev.setProof(proof);
        mev.setSignature(sg);
        mev.getVote().add(ec);
        mev.getVote().set(0, ec);
        
        ev.setElectionId(electionId);
        ev.setSignature(sg);
        ev.getVote().add(ec);
        ev.getVote().set(0, ec);
        
        pdv.setElectionId(electionId);
        pdv.setProof(proof);
        pdv.setSignature(sg);
        pdv.getVote().add(new BigInteger("1"));
        pdv.getVote().set(0, BigInteger.ONE);
        
        dyv.setElectionId(electionId);
        dyv.setSignature(sg);
        dyv.getVote().add(new BigInteger("1"));
        dyv.getVote().set(0, BigInteger.ONE);
        
        DecodedVote dv = new DecodedVote();
        DecodedVoteEntry dve = new DecodedVoteEntry();
        dve.setChoiceId(1);
        dve.setCount(1);
        
        dv.getEntry().add(dve);
        
        dov.getDecodedVote().add(dv); 
        dov.setElectionId(electionId);
        dov.setSignature(RSASignatur.createSignature(ed));
         
        
        ObjectFactory of = new ObjectFactory();
        of.createPoliticalList().setChoiceId(1);
        of.createCandidate().setFirstName("Gian");
        of.createForallRule().setLowerBound(1);
        of.createForallRule().setUpperBound(2);
        of.createBallot().setElectionId(electionId);
        of.createBallot().setEncryptedVote(ec);
        of.createBallot().setProof(proof);
        of.createBallot().setSignature(vs);
        of.createBallot().setVerificationKey(BigInteger.TEN);
        of.createBallots().setBallotsState(BallotsState.VALID);
        of.createBallots().setElectionId(electionId);
        of.createBallots().setSignature(sg);
    }
}