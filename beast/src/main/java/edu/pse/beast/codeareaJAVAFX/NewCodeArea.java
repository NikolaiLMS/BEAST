package edu.pse.beast.codeareaJAVAFX;

import java.util.Collection;
import java.util.Collections;
import java.util.function.IntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import javafx.scene.Node;

public class NewCodeArea extends CodeArea {

	private static final String[] KEYWORDS = new String[] { "auto", "break", "case", "const", "continue",
			"default", "do", "else", "const", "continue", "default", "do", "else", "enum", "extern",
			"for", "goto", "if", "return", "signed", "sizeof", "static", "struct" , "switch", "typedef", "union", "unsigned", "volatile", "while" };
	
	private static final String[] DATATYPES = new String[] { "char", "double", "enum", "float", "int", "long", "register",
			"void" };

	private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
	private static final String METHOD_PATTERN = "(\\s*)+[\\$_\\w\\<\\>\\[\\]]*\\s+[\\$_\\w]+\\([^\\)]*\\)?\\s*\\{";
	private static final String PAREN_PATTERN = "\\(|\\)";
	private static final String BRACE_PATTERN = "\\{|\\}";
	private static final String BRACKET_PATTERN = "\\[|\\]";
	private static final String SEMICOLON_PATTERN = "\\;";
	private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
	private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

	private static final Pattern PATTERN = Pattern.compile(
			"(?<KEYWORD>" + KEYWORD_PATTERN + ")" + "(?<METHOD>" + METHOD_PATTERN + ")" + "|(?<PAREN>" + PAREN_PATTERN + ")" + "|(?<BRACE>" + BRACE_PATTERN
					+ ")" + "|(?<BRACKET>" + BRACKET_PATTERN + ")" + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
					+ "|(?<STRING>" + STRING_PATTERN + ")" + "|(?<COMMENT>" + COMMENT_PATTERN + ")");

	public NewCodeArea() {

		String sampleCode = String.join("\n",
				new String[] { "package com.example;", "", "import java.util.*;", "",
						"public class Foo extends Bar implements Baz {", "", "    /*", "     * multi-line comment",
						"     */", "    public static void main(String[] args) {", "        // single-line comment",
						"        for(String arg: args) {", "            if(arg.length() != 0)",
						"                System.out.println(arg);", "            else",
						"                System.err.println(\"Warning: empty string as argument\");", "        }",
						"    }", "", "}" });

		String stylesheet = this.getClass().getResource("newCodeAreaStyle.css").toExternalForm();

		this.getStylesheets().add(stylesheet);
		
		IntFunction<Node> lineNumbers = LineNumberFactory.get(this);

		this.setParagraphGraphicFactory(lineNumbers);

		this.richChanges().filter(ch -> !ch.getInserted().equals(ch.getRemoved()))
				.subscribe(change -> {
					this.setStyleSpans(0, computeHighlighting(this.getText()));
				});
		this.replaceText(0, 0, sampleCode);

	}

	private static StyleSpans<Collection<String>> computeHighlighting(String text) {
		System.out.println("high");
		Matcher matcher = PATTERN.matcher(text);
		int lastKwEnd = 0;
		StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
		while (matcher.find()) {
			String styleClass = matcher.group("KEYWORD") != null ? "keyword"
					: matcher.group("METHOD") != null ? "method"
						: matcher.group("PAREN") != null ? "paren"
								: matcher.group("BRACE") != null ? "brace"
										: matcher.group("BRACKET") != null ? "bracket"
												: matcher.group("SEMICOLON") != null ? "semicolon"
														: matcher.group("STRING") != null ? "string"
																: matcher.group("COMMENT") != null ? "comment" : null;
			/* never happens */ assert styleClass != null;
			spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
			spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
			lastKwEnd = matcher.end();
		}
		spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
		return spansBuilder.create();
	}

}