/*
* Copyright (C) 2017 University of Freiburg.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
* The TriAL-QL Engine is a research project at the department of 
* computer science, University of Freiburg. 
*
* More information on the project:
* http://dbis.informatik.uni-freiburg.de/forschung/projekte/DiPoS/
* zablocki@informatik.uni-freiburg.de
*/

package parser;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

/**
 * Lexer: Auto generated from ANTLR and the E-TriAL-QL grammar.
 */
@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast" })
public class TriALQLLexer extends Lexer {
	static {
		RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION);
	}

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
	public static final int T__0 = 1, T__1 = 2, Select = 3, From = 4, Join = 5, On = 6, Store = 7, Provenance = 8,
			As = 9, Using = 10, Left = 11, Right = 12, And = 13, Or = 14, Drop = 15, Union = 16, Minus = 17,
			Intersect = 18, Filter = 19, With = 20, Load = 21, Cache = 22, Merge = 23, Star = 24, Plus = 25, Pos = 26,
			Equals = 27, NEquals = 28, GTEquals = 29, LTEquals = 30, GT = 31, LT = 32, Apostrophe = 33, Quote = 34,
			Semikolon = 35, OBracket = 36, CBracket = 37, Comma = 38, Bool = 39, Identifier = 40, String = 41,
			PositiveInt = 42, Int = 43, Comment = 44, Whitespace = 45;
	public static String[] modeNames = { "DEFAULT_MODE" };

	public static final String[] ruleNames = { "T__0", "T__1", "Select", "From", "Join", "On", "Store", "Provenance",
			"As", "Using", "Left", "Right", "And", "Or", "Drop", "Union", "Minus", "Intersect", "Filter", "With",
			"Load", "Cache", "Merge", "Star", "Plus", "Pos", "Equals", "NEquals", "GTEquals", "LTEquals", "GT", "LT",
			"Apostrophe", "Quote", "Semikolon", "OBracket", "CBracket", "Comma", "Bool", "Identifier", "String",
			"PositiveInt", "Int", "Digit", "Comment", "Whitespace" };

	private static final String[] _LITERAL_NAMES = { null, "'r1'", "'r2'", "'SELECT'", "'FROM'", "'JOIN'", "'ON'",
			"'STORE'", "'PROVENANCE'", "'AS'", "'USING'", "'left'", "'right'", "'AND'", "'OR'", "'DROP'", "'UNION'",
			"'MINUS'", "'INTERSECT'", "'FILTER'", "'WITH'", "'LOAD'", "'CACHE'", "'MERGE'", "'*'", "'+'", null, "'='",
			"'!='", "'>='", "'<='", "'>'", "'<'", "'''", "'\"'", "';'", "'['", "']'", "','" };
	private static final String[] _SYMBOLIC_NAMES = { null, null, null, "Select", "From", "Join", "On", "Store",
			"Provenance", "As", "Using", "Left", "Right", "And", "Or", "Drop", "Union", "Minus", "Intersect", "Filter",
			"With", "Load", "Cache", "Merge", "Star", "Plus", "Pos", "Equals", "NEquals", "GTEquals", "LTEquals", "GT",
			"LT", "Apostrophe", "Quote", "Semikolon", "OBracket", "CBracket", "Comma", "Bool", "Identifier", "String",
			"PositiveInt", "Int", "Comment", "Whitespace" };
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	public TriALQLLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
	}

	@Override
	public String getGrammarFileName() {
		return "TriALQL.g4";
	}

	@Override
	public String[] getRuleNames() {
		return ruleNames;
	}

	@Override
	public String getSerializedATN() {
		return _serializedATN;
	}

	@Override
	public String[] getModeNames() {
		return modeNames;
	}

	@Override
	public ATN getATN() {
		return _ATN;
	}

	public static final String _serializedATN = "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2/\u0146\b\1\4\2\t"
			+ "\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"
			+ "\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"
			+ "\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"
			+ "\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"
			+ "\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"
			+ ",\t,\4-\t-\4.\t.\4/\t/\3\2\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4"
			+ "\3\4\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\b\3"
			+ "\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\13"
			+ "\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r"
			+ "\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\21\3\21"
			+ "\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23"
			+ "\3\23\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\25"
			+ "\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27"
			+ "\3\27\3\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3\33\3\33"
			+ "\3\34\3\34\3\35\3\35\3\35\3\36\3\36\3\36\3\37\3\37\3\37\3 \3 \3!\3!\3"
			+ "\"\3\"\3#\3#\3$\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3(\3(\3(\3(\3(\3(\3(\5(\u0109"
			+ "\n(\3)\3)\3)\7)\u010e\n)\f)\16)\u0111\13)\3*\3*\3*\3*\7*\u0117\n*\f*\16"
			+ "*\u011a\13*\3*\3*\3*\3*\3*\7*\u0121\n*\f*\16*\u0124\13*\3*\5*\u0127\n"
			+ "*\3+\3+\7+\u012b\n+\f+\16+\u012e\13+\3,\3,\5,\u0132\n,\3-\3-\3.\3.\3."
			+ "\5.\u0139\n.\3.\7.\u013c\n.\f.\16.\u013f\13.\3.\3.\3/\3/\3/\3/\2\2\60"
			+ "\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20"
			+ "\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37"
			+ "= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y\2[.]/\3\2\f\4\2qruu\3\2\63\64\4\2C\\c"
			+ "|\6\2\60\60C\\aac|\4\2$$^^\4\2))^^\3\2\63;\3\2\62;\4\2\f\f\17\17\5\2\13"
			+ "\f\17\17\"\"\u0150\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13"
			+ "\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2"
			+ "\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2"
			+ "!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3"
			+ "\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2"
			+ "\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E"
			+ "\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2"
			+ "\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\3_\3\2\2\2"
			+ "\5b\3\2\2\2\7e\3\2\2\2\tl\3\2\2\2\13q\3\2\2\2\rv\3\2\2\2\17y\3\2\2\2\21"
			+ "\177\3\2\2\2\23\u008a\3\2\2\2\25\u008d\3\2\2\2\27\u0093\3\2\2\2\31\u0098"
			+ "\3\2\2\2\33\u009e\3\2\2\2\35\u00a2\3\2\2\2\37\u00a5\3\2\2\2!\u00aa\3\2"
			+ "\2\2#\u00b0\3\2\2\2%\u00b6\3\2\2\2\'\u00c0\3\2\2\2)\u00c7\3\2\2\2+\u00cc"
			+ "\3\2\2\2-\u00d1\3\2\2\2/\u00d7\3\2\2\2\61\u00dd\3\2\2\2\63\u00df\3\2\2"
			+ "\2\65\u00e1\3\2\2\2\67\u00e4\3\2\2\29\u00e6\3\2\2\2;\u00e9\3\2\2\2=\u00ec"
			+ "\3\2\2\2?\u00ef\3\2\2\2A\u00f1\3\2\2\2C\u00f3\3\2\2\2E\u00f5\3\2\2\2G"
			+ "\u00f7\3\2\2\2I\u00f9\3\2\2\2K\u00fb\3\2\2\2M\u00fd\3\2\2\2O\u0108\3\2"
			+ "\2\2Q\u010a\3\2\2\2S\u0126\3\2\2\2U\u0128\3\2\2\2W\u0131\3\2\2\2Y\u0133"
			+ "\3\2\2\2[\u0138\3\2\2\2]\u0142\3\2\2\2_`\7t\2\2`a\7\63\2\2a\4\3\2\2\2"
			+ "bc\7t\2\2cd\7\64\2\2d\6\3\2\2\2ef\7U\2\2fg\7G\2\2gh\7N\2\2hi\7G\2\2ij"
			+ "\7E\2\2jk\7V\2\2k\b\3\2\2\2lm\7H\2\2mn\7T\2\2no\7Q\2\2op\7O\2\2p\n\3\2"
			+ "\2\2qr\7L\2\2rs\7Q\2\2st\7K\2\2tu\7P\2\2u\f\3\2\2\2vw\7Q\2\2wx\7P\2\2"
			+ "x\16\3\2\2\2yz\7U\2\2z{\7V\2\2{|\7Q\2\2|}\7T\2\2}~\7G\2\2~\20\3\2\2\2"
			+ "\177\u0080\7R\2\2\u0080\u0081\7T\2\2\u0081\u0082\7Q\2\2\u0082\u0083\7"
			+ "X\2\2\u0083\u0084\7G\2\2\u0084\u0085\7P\2\2\u0085\u0086\7C\2\2\u0086\u0087"
			+ "\7P\2\2\u0087\u0088\7E\2\2\u0088\u0089\7G\2\2\u0089\22\3\2\2\2\u008a\u008b"
			+ "\7C\2\2\u008b\u008c\7U\2\2\u008c\24\3\2\2\2\u008d\u008e\7W\2\2\u008e\u008f"
			+ "\7U\2\2\u008f\u0090\7K\2\2\u0090\u0091\7P\2\2\u0091\u0092\7I\2\2\u0092"
			+ "\26\3\2\2\2\u0093\u0094\7n\2\2\u0094\u0095\7g\2\2\u0095\u0096\7h\2\2\u0096"
			+ "\u0097\7v\2\2\u0097\30\3\2\2\2\u0098\u0099\7t\2\2\u0099\u009a\7k\2\2\u009a"
			+ "\u009b\7i\2\2\u009b\u009c\7j\2\2\u009c\u009d\7v\2\2\u009d\32\3\2\2\2\u009e"
			+ "\u009f\7C\2\2\u009f\u00a0\7P\2\2\u00a0\u00a1\7F\2\2\u00a1\34\3\2\2\2\u00a2"
			+ "\u00a3\7Q\2\2\u00a3\u00a4\7T\2\2\u00a4\36\3\2\2\2\u00a5\u00a6\7F\2\2\u00a6"
			+ "\u00a7\7T\2\2\u00a7\u00a8\7Q\2\2\u00a8\u00a9\7R\2\2\u00a9 \3\2\2\2\u00aa"
			+ "\u00ab\7W\2\2\u00ab\u00ac\7P\2\2\u00ac\u00ad\7K\2\2\u00ad\u00ae\7Q\2\2"
			+ "\u00ae\u00af\7P\2\2\u00af\"\3\2\2\2\u00b0\u00b1\7O\2\2\u00b1\u00b2\7K"
			+ "\2\2\u00b2\u00b3\7P\2\2\u00b3\u00b4\7W\2\2\u00b4\u00b5\7U\2\2\u00b5$\3"
			+ "\2\2\2\u00b6\u00b7\7K\2\2\u00b7\u00b8\7P\2\2\u00b8\u00b9\7V\2\2\u00b9"
			+ "\u00ba\7G\2\2\u00ba\u00bb\7T\2\2\u00bb\u00bc\7U\2\2\u00bc\u00bd\7G\2\2"
			+ "\u00bd\u00be\7E\2\2\u00be\u00bf\7V\2\2\u00bf&\3\2\2\2\u00c0\u00c1\7H\2"
			+ "\2\u00c1\u00c2\7K\2\2\u00c2\u00c3\7N\2\2\u00c3\u00c4\7V\2\2\u00c4\u00c5"
			+ "\7G\2\2\u00c5\u00c6\7T\2\2\u00c6(\3\2\2\2\u00c7\u00c8\7Y\2\2\u00c8\u00c9"
			+ "\7K\2\2\u00c9\u00ca\7V\2\2\u00ca\u00cb\7J\2\2\u00cb*\3\2\2\2\u00cc\u00cd"
			+ "\7N\2\2\u00cd\u00ce\7Q\2\2\u00ce\u00cf\7C\2\2\u00cf\u00d0\7F\2\2\u00d0"
			+ ",\3\2\2\2\u00d1\u00d2\7E\2\2\u00d2\u00d3\7C\2\2\u00d3\u00d4\7E\2\2\u00d4"
			+ "\u00d5\7J\2\2\u00d5\u00d6\7G\2\2\u00d6.\3\2\2\2\u00d7\u00d8\7O\2\2\u00d8"
			+ "\u00d9\7G\2\2\u00d9\u00da\7T\2\2\u00da\u00db\7I\2\2\u00db\u00dc\7G\2\2"
			+ "\u00dc\60\3\2\2\2\u00dd\u00de\7,\2\2\u00de\62\3\2\2\2\u00df\u00e0\7-\2"
			+ "\2\u00e0\64\3\2\2\2\u00e1\u00e2\t\2\2\2\u00e2\u00e3\t\3\2\2\u00e3\66\3"
			+ "\2\2\2\u00e4\u00e5\7?\2\2\u00e58\3\2\2\2\u00e6\u00e7\7#\2\2\u00e7\u00e8"
			+ "\7?\2\2\u00e8:\3\2\2\2\u00e9\u00ea\7@\2\2\u00ea\u00eb\7?\2\2\u00eb<\3"
			+ "\2\2\2\u00ec\u00ed\7>\2\2\u00ed\u00ee\7?\2\2\u00ee>\3\2\2\2\u00ef\u00f0"
			+ "\7@\2\2\u00f0@\3\2\2\2\u00f1\u00f2\7>\2\2\u00f2B\3\2\2\2\u00f3\u00f4\7"
			+ ")\2\2\u00f4D\3\2\2\2\u00f5\u00f6\7$\2\2\u00f6F\3\2\2\2\u00f7\u00f8\7="
			+ "\2\2\u00f8H\3\2\2\2\u00f9\u00fa\7]\2\2\u00faJ\3\2\2\2\u00fb\u00fc\7_\2"
			+ "\2\u00fcL\3\2\2\2\u00fd\u00fe\7.\2\2\u00feN\3\2\2\2\u00ff\u0100\7v\2\2"
			+ "\u0100\u0101\7t\2\2\u0101\u0102\7w\2\2\u0102\u0109\7g\2\2\u0103\u0104"
			+ "\7h\2\2\u0104\u0105\7c\2\2\u0105\u0106\7n\2\2\u0106\u0107\7u\2\2\u0107"
			+ "\u0109\7g\2\2\u0108\u00ff\3\2\2\2\u0108\u0103\3\2\2\2\u0109P\3\2\2\2\u010a"
			+ "\u010f\t\4\2\2\u010b\u010e\t\5\2\2\u010c\u010e\5Y-\2\u010d\u010b\3\2\2"
			+ "\2\u010d\u010c\3\2\2\2\u010e\u0111\3\2\2\2\u010f\u010d\3\2\2\2\u010f\u0110"
			+ "\3\2\2\2\u0110R\3\2\2\2\u0111\u010f\3\2\2\2\u0112\u0118\7$\2\2\u0113\u0117"
			+ "\n\6\2\2\u0114\u0115\7^\2\2\u0115\u0117\13\2\2\2\u0116\u0113\3\2\2\2\u0116"
			+ "\u0114\3\2\2\2\u0117\u011a\3\2\2\2\u0118\u0116\3\2\2\2\u0118\u0119\3\2"
			+ "\2\2\u0119\u011b\3\2\2\2\u011a\u0118\3\2\2\2\u011b\u0127\7$\2\2\u011c"
			+ "\u0122\7)\2\2\u011d\u0121\n\7\2\2\u011e\u011f\7^\2\2\u011f\u0121\13\2"
			+ "\2\2\u0120\u011d\3\2\2\2\u0120\u011e\3\2\2\2\u0121\u0124\3\2\2\2\u0122"
			+ "\u0120\3\2\2\2\u0122\u0123\3\2\2\2\u0123\u0125\3\2\2\2\u0124\u0122\3\2"
			+ "\2\2\u0125\u0127\7)\2\2\u0126\u0112\3\2\2\2\u0126\u011c\3\2\2\2\u0127"
			+ "T\3\2\2\2\u0128\u012c\t\b\2\2\u0129\u012b\5Y-\2\u012a\u0129\3\2\2\2\u012b"
			+ "\u012e\3\2\2\2\u012c\u012a\3\2\2\2\u012c\u012d\3\2\2\2\u012dV\3\2\2\2"
			+ "\u012e\u012c\3\2\2\2\u012f\u0132\5U+\2\u0130\u0132\7\62\2\2\u0131\u012f"
			+ "\3\2\2\2\u0131\u0130\3\2\2\2\u0132X\3\2\2\2\u0133\u0134\t\t\2\2\u0134"
			+ "Z\3\2\2\2\u0135\u0136\7/\2\2\u0136\u0139\7/\2\2\u0137\u0139\7%\2\2\u0138"
			+ "\u0135\3\2\2\2\u0138\u0137\3\2\2\2\u0139\u013d\3\2\2\2\u013a\u013c\n\n"
			+ "\2\2\u013b\u013a\3\2\2\2\u013c\u013f\3\2\2\2\u013d\u013b\3\2\2\2\u013d"
			+ "\u013e\3\2\2\2\u013e\u0140\3\2\2\2\u013f\u013d\3\2\2\2\u0140\u0141\b."
			+ "\2\2\u0141\\\3\2\2\2\u0142\u0143\t\13\2\2\u0143\u0144\3\2\2\2\u0144\u0145"
			+ "\b/\2\2\u0145^\3\2\2\2\17\2\u0108\u010d\u010f\u0116\u0118\u0120\u0122"
			+ "\u0126\u012c\u0131\u0138\u013d\3\b\2\2";
	public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}