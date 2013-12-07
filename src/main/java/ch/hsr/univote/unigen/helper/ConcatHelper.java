/*
 * Copyright (c) 2012 Berner Fachhochschule, Switzerland.
 * Bern University of Applied Sciences, Engineering and Information Technology,
 * Research Institute for Security in the Information Society, E-Voting Group,
 * Biel, Switzerland.
 *
 * Project UniVote.
 *
 * Distributable under GPL license.
 * See terms of license at gnu.org.
 */
package ch.bfh.univote.administration.helper;

import ch.bfh.unicrypt.concat.classes.ConcatSchemeClass;
import ch.bfh.unicrypt.concat.interfaces.ConcatScheme;
import ch.bfh.unicrypt.math.element.interfaces.AtomicElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.element.interfaces.TupleElement;
import ch.bfh.unicrypt.math.function.classes.ConcatenateFunctionClass;
import ch.bfh.unicrypt.math.group.classes.ProductGroupClass;
import ch.bfh.unicrypt.math.java2unicrypt.classes.ExternalDataMapperClass;
import ch.bfh.univote.common.Candidate;
import ch.bfh.univote.common.Choice;
import ch.bfh.univote.common.ElectionDefinition;
import ch.bfh.univote.common.ElectionOptions;
import ch.bfh.univote.common.ElectoralRoll;
import ch.bfh.univote.common.ForallRule;
import ch.bfh.univote.common.LocalizedText;
import ch.bfh.univote.common.PoliticalList;
import ch.bfh.univote.common.Rule;
import ch.bfh.univote.common.SummationRule;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Helper class to concatenate data.
 *
 * @author Stephan Fischli &lt;stephan.fischli@bfh.ch&gt;
 */
public final class ConcatHelper {

	public static final ExternalDataMapperClass mapper = new ExternalDataMapperClass();
	public static final ConcatScheme concatScheme = new ConcatSchemeClass(
			ConcatenateFunctionClass.ConcatParameter.PipeBrackets, mapper);

	public static TupleElement createElement(ElectionDefinition electionDefinition) {
		List<Element> encoded = new ArrayList<Element>();
		encoded.add(mapper.getEncodedElement(electionDefinition.getElectionId()));
		encoded.add(mapper.getEncodedElement(electionDefinition.getTitle()));
		encoded.add(mapper.getEncodedElement(electionDefinition.getKeyLength()));
		encoded.add(createElement(electionDefinition.getMixerId()));
		encoded.add(createElement(electionDefinition.getTallierId()));
		encoded.add(mapper.getEncodedElement(electionDefinition.getVotingPhaseBegin().toXMLFormat()));
		encoded.add(mapper.getEncodedElement(electionDefinition.getVotingPhaseEnd().toXMLFormat()));
		return ProductGroupClass.createTupleElement(encoded);
	}

	public static TupleElement createElement(ElectionOptions electionOptions) {
		List<Element> encoded = new ArrayList<Element>();
		encoded.add(mapper.getEncodedElement(electionOptions.getElectionId()));
		List<Element> choices = new ArrayList<Element>();
		for (Choice choice : electionOptions.getChoice()) {
			if (choice instanceof Candidate) {
				choices.add(createElement((Candidate) choice));
			} else if (choice instanceof PoliticalList) {
				choices.add(createElement((PoliticalList) choice));
			} else {
				throw new RuntimeException("Unknown choice type: " + choice.getClass().getName());
			}
		}
		encoded.add(ProductGroupClass.createTupleElement(choices));
		List<Element> rules = new ArrayList<Element>();
		for (Rule rule : electionOptions.getRule()) {
			if (rule instanceof SummationRule) {
				rules.add(createElement((SummationRule) rule));
			} else if (rule instanceof ForallRule) {
				rules.add(createElement((ForallRule) rule));
			} else {
				throw new IllegalArgumentException("Unknown rule type: " + rule.getClass().getName());
			}
		}
		encoded.add(ProductGroupClass.createTupleElement(rules));
		return ProductGroupClass.createTupleElement(encoded);
	}

	public static TupleElement createElement(ElectoralRoll electoralRoll) {
		List<Element> encoded = new ArrayList<Element>();
		encoded.add(mapper.getEncodedElement(electoralRoll.getElectionId()));
		encoded.add(createElement(electoralRoll.getVoterHash()));
		return ProductGroupClass.createTupleElement(encoded);
	}

	public static AtomicElement concat(Element element, XMLGregorianCalendar calendar) {
		return concatScheme.concat(element, mapper.getEncodedElement(calendar.toXMLFormat()));
	}

	private static Element createElement(Candidate candidate) {
		List<Element> encoded = new ArrayList<Element>();
		encoded.add(mapper.getEncodedElement(candidate.getChoiceId()));
		encoded.add(mapper.getEncodedElement(candidate.getNumber()));
		encoded.add(mapper.getEncodedElement(candidate.getLastName()));
		encoded.add(mapper.getEncodedElement(candidate.getFirstName()));
		encoded.add(mapper.getEncodedElement(candidate.getSex().value()));
		encoded.add(mapper.getEncodedElement(candidate.getYearOfBirth()));
		encoded.add(createElement(candidate.getStudyBranch()));
		encoded.add(createElement(candidate.getStudyDegree()));
		encoded.add(mapper.getEncodedElement(candidate.getSemesterCount()));
		encoded.add(mapper.getEncodedElement(candidate.getStatus().value()));
		encoded.add(mapper.getEncodedElement(candidate.getListId()));
		encoded.add(mapper.getEncodedElement(candidate.getCumulation()));
		return ProductGroupClass.createTupleElement(encoded);
	}

	private static Element createElement(PoliticalList list) {
		List<Element> encoded = new ArrayList<Element>();
		encoded.add(mapper.getEncodedElement(list.getChoiceId()));
		encoded.add(mapper.getEncodedElement(list.getNumber()));
		encoded.add(createElement(list.getTitle()));
		encoded.add(createElement(list.getPartyName()));
		encoded.add(createElement(list.getPartyShortName()));
		return ProductGroupClass.createTupleElement(encoded);
	}

	private static Element createElement(ForallRule rule) {
		List<Element> encoded = new ArrayList<Element>();
		encoded.add(mapper.getEncodedElement("forallRule"));
		encoded.add(createElement(rule.getChoiceId()));
		encoded.add(mapper.getEncodedElement(rule.getLowerBound()));
		encoded.add(mapper.getEncodedElement(rule.getUpperBound()));
		return ProductGroupClass.createTupleElement(encoded);
	}

	private static Element createElement(SummationRule rule) {
		List<Element> encoded = new ArrayList<Element>();
		encoded.add(mapper.getEncodedElement("summationRule"));
		encoded.add(createElement(rule.getChoiceId()));
		encoded.add(mapper.getEncodedElement(rule.getLowerBound()));
		encoded.add(mapper.getEncodedElement(rule.getUpperBound()));
		return ProductGroupClass.createTupleElement(encoded);
	}

	private static <T> Element createElement(List<T> list) {
		List<Element> encoded = new ArrayList<Element>();
		for (T elem : list) {
			if (elem instanceof LocalizedText) {
				LocalizedText text = (LocalizedText) elem;
				encoded.add(mapper.getEncodedElement(text.getLanguage().value(), text.getText()));
			} else if (elem instanceof String) {
				encoded.add(mapper.getEncodedElement((String) elem));
			} else if (elem instanceof Integer) {
				encoded.add(mapper.getEncodedElement((Integer) elem));
			} else if (elem instanceof BigInteger) {
				encoded.add(mapper.getEncodedElement((BigInteger) elem));
			} else if (elem instanceof byte[]) {
				encoded.add(mapper.getEncodedElement((byte[]) elem));
			} else {
				throw new IllegalArgumentException("Unknown element type");
			}
		}
		return ProductGroupClass.createTupleElement(encoded);
	}
}
