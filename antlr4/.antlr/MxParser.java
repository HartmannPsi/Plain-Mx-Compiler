// Generated from /home/hartmann/MxCompiler/antlr4/MxParser.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class MxParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		LineComm=1, BlockComm=2, WS=3, Void=4, Bool=5, Int=6, String=7, New=8, 
		Class=9, Null=10, True=11, False=12, This=13, If=14, Else=15, For=16, 
		While=17, Break=18, Continue=19, Return=20, Add=21, Sub=22, Mul=23, Div=24, 
		Mod=25, Gt=26, Lt=27, Ge=28, Le=29, Ne=30, Eq=31, LogicAnd=32, LogicOr=33, 
		LogicNot=34, ShiftR=35, ShiftL=36, BitAnd=37, BitOr=38, BitXor=39, BitNot=40, 
		Assign=41, SelfAdd=42, SelfSub=43, Component=44, LBracket=45, RBracket=46, 
		LP=47, RP=48, QuesMark=49, Colon=50, Semicolon=51, Comma=52, LBrace=53, 
		RBrace=54, FQuote=55, Quote=56, Dollar=57, Identifier=58, StringConst=59, 
		FStringL=60, FStringM=61, FStringR=62, FStringN=63, NumberConst=64;
	public static final int
		RULE_prog = 0, RULE_basic_type = 1, RULE_typename = 2, RULE_array_init_tp = 3, 
		RULE_def_func = 4, RULE_para_list = 5, RULE_def_class = 6, RULE_class_constructor = 7, 
		RULE_array = 8, RULE_expr_list = 9, RULE_expr = 10, RULE_form_string = 11, 
		RULE_stmt = 12, RULE_def_var_stmt = 13, RULE_if_stmt = 14, RULE_while_stmt = 15, 
		RULE_for_stmt = 16, RULE_return_stmt = 17, RULE_break_stmt = 18, RULE_continue_stmt = 19, 
		RULE_expr_stmt = 20, RULE_empty_stmt = 21;
	private static String[] makeRuleNames() {
		return new String[] {
			"prog", "basic_type", "typename", "array_init_tp", "def_func", "para_list", 
			"def_class", "class_constructor", "array", "expr_list", "expr", "form_string", 
			"stmt", "def_var_stmt", "if_stmt", "while_stmt", "for_stmt", "return_stmt", 
			"break_stmt", "continue_stmt", "expr_stmt", "empty_stmt"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, "'void'", "'bool'", "'int'", "'string'", "'new'", 
			"'class'", "'null'", "'true'", "'false'", "'this'", "'if'", "'else'", 
			"'for'", "'while'", "'break'", "'continue'", "'return'", "'+'", "'-'", 
			"'*'", "'/'", "'%'", "'>'", "'<'", "'>='", "'<='", "'!='", "'=='", "'&&'", 
			"'||'", "'!'", "'>>'", "'<<'", "'&'", "'|'", "'^'", "'~'", "'='", "'++'", 
			"'--'", "'.'", "'['", "']'", "'('", "')'", "'?'", "':'", "';'", "','", 
			"'{'", "'}'", "'f\"'", "'\"'", "'$'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "LineComm", "BlockComm", "WS", "Void", "Bool", "Int", "String", 
			"New", "Class", "Null", "True", "False", "This", "If", "Else", "For", 
			"While", "Break", "Continue", "Return", "Add", "Sub", "Mul", "Div", "Mod", 
			"Gt", "Lt", "Ge", "Le", "Ne", "Eq", "LogicAnd", "LogicOr", "LogicNot", 
			"ShiftR", "ShiftL", "BitAnd", "BitOr", "BitXor", "BitNot", "Assign", 
			"SelfAdd", "SelfSub", "Component", "LBracket", "RBracket", "LP", "RP", 
			"QuesMark", "Colon", "Semicolon", "Comma", "LBrace", "RBrace", "FQuote", 
			"Quote", "Dollar", "Identifier", "StringConst", "FStringL", "FStringM", 
			"FStringR", "FStringN", "NumberConst"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
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

	@Override
	public String getGrammarFileName() { return "MxParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MxParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(MxParser.EOF, 0); }
		public List<Def_funcContext> def_func() {
			return getRuleContexts(Def_funcContext.class);
		}
		public Def_funcContext def_func(int i) {
			return getRuleContext(Def_funcContext.class,i);
		}
		public List<Def_classContext> def_class() {
			return getRuleContexts(Def_classContext.class);
		}
		public Def_classContext def_class(int i) {
			return getRuleContext(Def_classContext.class,i);
		}
		public List<Def_var_stmtContext> def_var_stmt() {
			return getRuleContexts(Def_var_stmtContext.class);
		}
		public Def_var_stmtContext def_var_stmt(int i) {
			return getRuleContext(Def_var_stmtContext.class,i);
		}
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prog);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(49);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 288230376151712496L) != 0)) {
				{
				setState(47);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
				case 1:
					{
					setState(44);
					def_func();
					}
					break;
				case 2:
					{
					setState(45);
					def_class();
					}
					break;
				case 3:
					{
					setState(46);
					def_var_stmt();
					}
					break;
				}
				}
				setState(51);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(52);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Basic_typeContext extends ParserRuleContext {
		public TerminalNode Int() { return getToken(MxParser.Int, 0); }
		public TerminalNode String() { return getToken(MxParser.String, 0); }
		public TerminalNode Bool() { return getToken(MxParser.Bool, 0); }
		public Basic_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_basic_type; }
	}

	public final Basic_typeContext basic_type() throws RecognitionException {
		Basic_typeContext _localctx = new Basic_typeContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_basic_type);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(54);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 224L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypenameContext extends ParserRuleContext {
		public Basic_typeContext basic_type() {
			return getRuleContext(Basic_typeContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public List<TerminalNode> LBracket() { return getTokens(MxParser.LBracket); }
		public TerminalNode LBracket(int i) {
			return getToken(MxParser.LBracket, i);
		}
		public List<TerminalNode> RBracket() { return getTokens(MxParser.RBracket); }
		public TerminalNode RBracket(int i) {
			return getToken(MxParser.RBracket, i);
		}
		public TypenameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typename; }
	}

	public final TypenameContext typename() throws RecognitionException {
		TypenameContext _localctx = new TypenameContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_typename);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(58);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Bool:
			case Int:
			case String:
				{
				setState(56);
				basic_type();
				}
				break;
			case Identifier:
				{
				setState(57);
				match(Identifier);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(64);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(60);
					match(LBracket);
					setState(61);
					match(RBracket);
					}
					} 
				}
				setState(66);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Array_init_tpContext extends ParserRuleContext {
		public Basic_typeContext basic_type() {
			return getRuleContext(Basic_typeContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public List<TerminalNode> LBracket() { return getTokens(MxParser.LBracket); }
		public TerminalNode LBracket(int i) {
			return getToken(MxParser.LBracket, i);
		}
		public List<TerminalNode> RBracket() { return getTokens(MxParser.RBracket); }
		public TerminalNode RBracket(int i) {
			return getToken(MxParser.RBracket, i);
		}
		public List<TerminalNode> NumberConst() { return getTokens(MxParser.NumberConst); }
		public TerminalNode NumberConst(int i) {
			return getToken(MxParser.NumberConst, i);
		}
		public Array_init_tpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array_init_tp; }
	}

	public final Array_init_tpContext array_init_tp() throws RecognitionException {
		Array_init_tpContext _localctx = new Array_init_tpContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_array_init_tp);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(69);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Bool:
			case Int:
			case String:
				{
				setState(67);
				basic_type();
				}
				break;
			case Identifier:
				{
				setState(68);
				match(Identifier);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(76);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(71);
					match(LBracket);
					setState(72);
					match(NumberConst);
					setState(73);
					match(RBracket);
					}
					} 
				}
				setState(78);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			{
			setState(79);
			match(LBracket);
			setState(81);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NumberConst) {
				{
				setState(80);
				match(NumberConst);
				}
			}

			setState(83);
			match(RBracket);
			}
			setState(89);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(85);
					match(LBracket);
					setState(86);
					match(RBracket);
					}
					} 
				}
				setState(91);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Def_funcContext extends ParserRuleContext {
		public TypenameContext returnType;
		public Token funcName;
		public Para_listContext paras;
		public StmtContext code;
		public TerminalNode LP() { return getToken(MxParser.LP, 0); }
		public TerminalNode RP() { return getToken(MxParser.RP, 0); }
		public TerminalNode LBrace() { return getToken(MxParser.LBrace, 0); }
		public TerminalNode RBrace() { return getToken(MxParser.RBrace, 0); }
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public TerminalNode Void() { return getToken(MxParser.Void, 0); }
		public TypenameContext typename() {
			return getRuleContext(TypenameContext.class,0);
		}
		public Para_listContext para_list() {
			return getRuleContext(Para_listContext.class,0);
		}
		public List<StmtContext> stmt() {
			return getRuleContexts(StmtContext.class);
		}
		public StmtContext stmt(int i) {
			return getRuleContext(StmtContext.class,i);
		}
		public Def_funcContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_def_func; }
	}

	public final Def_funcContext def_func() throws RecognitionException {
		Def_funcContext _localctx = new Def_funcContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_def_func);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(94);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Bool:
			case Int:
			case String:
			case Identifier:
				{
				setState(92);
				((Def_funcContext)_localctx).returnType = typename();
				}
				break;
			case Void:
				{
				setState(93);
				match(Void);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(96);
			((Def_funcContext)_localctx).funcName = match(Identifier);
			setState(97);
			match(LP);
			setState(99);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 288230376151711968L) != 0)) {
				{
				setState(98);
				((Def_funcContext)_localctx).paras = para_list();
				}
			}

			setState(101);
			match(RP);
			setState(102);
			match(LBrace);
			setState(106);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 5)) & ~0x3f) == 0 && ((1L << (_la - 5)) & 928098212219452399L) != 0)) {
				{
				{
				setState(103);
				((Def_funcContext)_localctx).code = stmt();
				}
				}
				setState(108);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(109);
			match(RBrace);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Para_listContext extends ParserRuleContext {
		public List<TypenameContext> typename() {
			return getRuleContexts(TypenameContext.class);
		}
		public TypenameContext typename(int i) {
			return getRuleContext(TypenameContext.class,i);
		}
		public List<TerminalNode> Identifier() { return getTokens(MxParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(MxParser.Identifier, i);
		}
		public List<TerminalNode> Comma() { return getTokens(MxParser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(MxParser.Comma, i);
		}
		public Para_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_para_list; }
	}

	public final Para_listContext para_list() throws RecognitionException {
		Para_listContext _localctx = new Para_listContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_para_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(111);
			typename();
			setState(112);
			match(Identifier);
			setState(119);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(113);
				match(Comma);
				setState(114);
				typename();
				setState(115);
				match(Identifier);
				}
				}
				setState(121);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Def_classContext extends ParserRuleContext {
		public Token className;
		public TerminalNode Class() { return getToken(MxParser.Class, 0); }
		public TerminalNode LBrace() { return getToken(MxParser.LBrace, 0); }
		public TerminalNode RBrace() { return getToken(MxParser.RBrace, 0); }
		public TerminalNode Semicolon() { return getToken(MxParser.Semicolon, 0); }
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public List<Def_var_stmtContext> def_var_stmt() {
			return getRuleContexts(Def_var_stmtContext.class);
		}
		public Def_var_stmtContext def_var_stmt(int i) {
			return getRuleContext(Def_var_stmtContext.class,i);
		}
		public List<Def_funcContext> def_func() {
			return getRuleContexts(Def_funcContext.class);
		}
		public Def_funcContext def_func(int i) {
			return getRuleContext(Def_funcContext.class,i);
		}
		public List<Class_constructorContext> class_constructor() {
			return getRuleContexts(Class_constructorContext.class);
		}
		public Class_constructorContext class_constructor(int i) {
			return getRuleContext(Class_constructorContext.class,i);
		}
		public Def_classContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_def_class; }
	}

	public final Def_classContext def_class() throws RecognitionException {
		Def_classContext _localctx = new Def_classContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_def_class);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(122);
			match(Class);
			setState(123);
			((Def_classContext)_localctx).className = match(Identifier);
			setState(124);
			match(LBrace);
			setState(130);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 288230376151711984L) != 0)) {
				{
				setState(128);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
				case 1:
					{
					setState(125);
					def_var_stmt();
					}
					break;
				case 2:
					{
					setState(126);
					def_func();
					}
					break;
				case 3:
					{
					setState(127);
					class_constructor();
					}
					break;
				}
				}
				setState(132);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(133);
			match(RBrace);
			setState(134);
			match(Semicolon);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Class_constructorContext extends ParserRuleContext {
		public Token className;
		public StmtContext code;
		public TerminalNode LP() { return getToken(MxParser.LP, 0); }
		public TerminalNode RP() { return getToken(MxParser.RP, 0); }
		public TerminalNode LBrace() { return getToken(MxParser.LBrace, 0); }
		public TerminalNode RBrace() { return getToken(MxParser.RBrace, 0); }
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public List<StmtContext> stmt() {
			return getRuleContexts(StmtContext.class);
		}
		public StmtContext stmt(int i) {
			return getRuleContext(StmtContext.class,i);
		}
		public Class_constructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_class_constructor; }
	}

	public final Class_constructorContext class_constructor() throws RecognitionException {
		Class_constructorContext _localctx = new Class_constructorContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_class_constructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(136);
			((Class_constructorContext)_localctx).className = match(Identifier);
			setState(137);
			match(LP);
			setState(138);
			match(RP);
			setState(139);
			match(LBrace);
			setState(143);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 5)) & ~0x3f) == 0 && ((1L << (_la - 5)) & 928098212219452399L) != 0)) {
				{
				{
				setState(140);
				((Class_constructorContext)_localctx).code = stmt();
				}
				}
				setState(145);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(146);
			match(RBrace);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayContext extends ParserRuleContext {
		public TerminalNode LBrace() { return getToken(MxParser.LBrace, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode RBrace() { return getToken(MxParser.RBrace, 0); }
		public List<TerminalNode> Comma() { return getTokens(MxParser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(MxParser.Comma, i);
		}
		public ArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array; }
	}

	public final ArrayContext array() throws RecognitionException {
		ArrayContext _localctx = new ArrayContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_array);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(148);
			match(LBrace);
			setState(149);
			expr(0);
			setState(154);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(150);
				match(Comma);
				setState(151);
				expr(0);
				}
				}
				setState(156);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(157);
			match(RBrace);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Expr_listContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> Comma() { return getTokens(MxParser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(MxParser.Comma, i);
		}
		public Expr_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr_list; }
	}

	public final Expr_listContext expr_list() throws RecognitionException {
		Expr_listContext _localctx = new Expr_listContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_expr_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(159);
			expr(0);
			setState(164);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(160);
				match(Comma);
				setState(161);
				expr(0);
				}
				}
				setState(166);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ArrContext extends ExprContext {
		public ArrayContext array() {
			return getRuleContext(ArrayContext.class,0);
		}
		public ArrContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NullContext extends ExprContext {
		public TerminalNode Null() { return getToken(MxParser.Null, 0); }
		public NullContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BOrContext extends ExprContext {
		public ExprContext lhs;
		public Token op;
		public ExprContext rhs;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode BitOr() { return getToken(MxParser.BitOr, 0); }
		public BOrContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class EqOpsContext extends ExprContext {
		public ExprContext lhs;
		public Token op;
		public ExprContext rhs;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode Eq() { return getToken(MxParser.Eq, 0); }
		public TerminalNode Ne() { return getToken(MxParser.Ne, 0); }
		public EqOpsContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class LAndContext extends ExprContext {
		public ExprContext lhs;
		public Token op;
		public ExprContext rhs;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode LogicAnd() { return getToken(MxParser.LogicAnd, 0); }
		public LAndContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TrueContext extends ExprContext {
		public TerminalNode True() { return getToken(MxParser.True, 0); }
		public TrueContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StringContext extends ExprContext {
		public TerminalNode StringConst() { return getToken(MxParser.StringConst, 0); }
		public StringContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FalseContext extends ExprContext {
		public TerminalNode False() { return getToken(MxParser.False, 0); }
		public FalseContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class LOrContext extends ExprContext {
		public ExprContext lhs;
		public Token op;
		public ExprContext rhs;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode LogicOr() { return getToken(MxParser.LogicOr, 0); }
		public LOrContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ArrayAccessContext extends ExprContext {
		public ExprContext arrayName;
		public ExprContext serial;
		public TerminalNode LBracket() { return getToken(MxParser.LBracket, 0); }
		public TerminalNode RBracket() { return getToken(MxParser.RBracket, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ArrayAccessContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NotOpsContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode LogicNot() { return getToken(MxParser.LogicNot, 0); }
		public TerminalNode BitNot() { return getToken(MxParser.BitNot, 0); }
		public NotOpsContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class RSelfOpsContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode SelfAdd() { return getToken(MxParser.SelfAdd, 0); }
		public TerminalNode SelfSub() { return getToken(MxParser.SelfSub, 0); }
		public RSelfOpsContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ArrayInitContext extends ExprContext {
		public Token op;
		public Array_init_tpContext type;
		public TerminalNode New() { return getToken(MxParser.New, 0); }
		public Array_init_tpContext array_init_tp() {
			return getRuleContext(Array_init_tpContext.class,0);
		}
		public ArrayContext array() {
			return getRuleContext(ArrayContext.class,0);
		}
		public ArrayInitContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BuiltInPtrContext extends ExprContext {
		public TerminalNode This() { return getToken(MxParser.This, 0); }
		public BuiltInPtrContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FStringContext extends ExprContext {
		public Form_stringContext form_string() {
			return getRuleContext(Form_stringContext.class,0);
		}
		public FStringContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class CompOpsContext extends ExprContext {
		public ExprContext lhs;
		public Token op;
		public ExprContext rhs;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode Lt() { return getToken(MxParser.Lt, 0); }
		public TerminalNode Le() { return getToken(MxParser.Le, 0); }
		public TerminalNode Gt() { return getToken(MxParser.Gt, 0); }
		public TerminalNode Ge() { return getToken(MxParser.Ge, 0); }
		public CompOpsContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class LSelfOpsContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode SelfAdd() { return getToken(MxParser.SelfAdd, 0); }
		public TerminalNode SelfSub() { return getToken(MxParser.SelfSub, 0); }
		public LSelfOpsContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SignOpsContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode Add() { return getToken(MxParser.Add, 0); }
		public TerminalNode Sub() { return getToken(MxParser.Sub, 0); }
		public SignOpsContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TernaryContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode Colon() { return getToken(MxParser.Colon, 0); }
		public TerminalNode QuesMark() { return getToken(MxParser.QuesMark, 0); }
		public TernaryContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MulOpsContext extends ExprContext {
		public ExprContext lhs;
		public Token op;
		public ExprContext rhs;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode Mul() { return getToken(MxParser.Mul, 0); }
		public TerminalNode Div() { return getToken(MxParser.Div, 0); }
		public TerminalNode Mod() { return getToken(MxParser.Mod, 0); }
		public MulOpsContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BXorContext extends ExprContext {
		public ExprContext lhs;
		public Token op;
		public ExprContext rhs;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode BitXor() { return getToken(MxParser.BitXor, 0); }
		public BXorContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NumContext extends ExprContext {
		public TerminalNode NumberConst() { return getToken(MxParser.NumberConst, 0); }
		public NumContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BAndContext extends ExprContext {
		public ExprContext lhs;
		public Token op;
		public ExprContext rhs;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode BitAnd() { return getToken(MxParser.BitAnd, 0); }
		public BAndContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AddOpsContext extends ExprContext {
		public ExprContext lhs;
		public Token op;
		public ExprContext rhs;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode Add() { return getToken(MxParser.Add, 0); }
		public TerminalNode Sub() { return getToken(MxParser.Sub, 0); }
		public AddOpsContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ObjectInitContext extends ExprContext {
		public Token op;
		public TypenameContext type;
		public TerminalNode New() { return getToken(MxParser.New, 0); }
		public TypenameContext typename() {
			return getRuleContext(TypenameContext.class,0);
		}
		public TerminalNode LP() { return getToken(MxParser.LP, 0); }
		public TerminalNode RP() { return getToken(MxParser.RP, 0); }
		public ObjectInitContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FuncCallContext extends ExprContext {
		public ExprContext funcName;
		public TerminalNode LP() { return getToken(MxParser.LP, 0); }
		public TerminalNode RP() { return getToken(MxParser.RP, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public Expr_listContext expr_list() {
			return getRuleContext(Expr_listContext.class,0);
		}
		public FuncCallContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MemberAccessContext extends ExprContext {
		public ExprContext object;
		public Token op;
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode Component() { return getToken(MxParser.Component, 0); }
		public MemberAccessContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ShiftOpsContext extends ExprContext {
		public ExprContext lhs;
		public Token op;
		public ExprContext rhs;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode ShiftL() { return getToken(MxParser.ShiftL, 0); }
		public TerminalNode ShiftR() { return getToken(MxParser.ShiftR, 0); }
		public ShiftOpsContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AssignContext extends ExprContext {
		public ExprContext lhs;
		public Token op;
		public ExprContext rhs;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode Assign() { return getToken(MxParser.Assign, 0); }
		public AssignContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IdContext extends ExprContext {
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public IdContext(ExprContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ParenExprContext extends ExprContext {
		public TerminalNode LP() { return getToken(MxParser.LP, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RP() { return getToken(MxParser.RP, 0); }
		public ParenExprContext(ExprContext ctx) { copyFrom(ctx); }
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 20;
		enterRecursionRule(_localctx, 20, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(198);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				{
				_localctx = new ParenExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(168);
				match(LP);
				setState(169);
				expr(0);
				setState(170);
				match(RP);
				}
				break;
			case 2:
				{
				_localctx = new LSelfOpsContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(172);
				((LSelfOpsContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==SelfAdd || _la==SelfSub) ) {
					((LSelfOpsContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(173);
				expr(26);
				}
				break;
			case 3:
				{
				_localctx = new SignOpsContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(174);
				((SignOpsContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==Add || _la==Sub) ) {
					((SignOpsContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(175);
				expr(25);
				}
				break;
			case 4:
				{
				_localctx = new NotOpsContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(176);
				((NotOpsContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==LogicNot || _la==BitNot) ) {
					((NotOpsContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(177);
				expr(24);
				}
				break;
			case 5:
				{
				_localctx = new ObjectInitContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(178);
				((ObjectInitContext)_localctx).op = match(New);
				setState(179);
				((ObjectInitContext)_localctx).type = typename();
				setState(182);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
				case 1:
					{
					setState(180);
					match(LP);
					setState(181);
					match(RP);
					}
					break;
				}
				}
				break;
			case 6:
				{
				_localctx = new ArrayInitContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(184);
				((ArrayInitContext)_localctx).op = match(New);
				setState(185);
				((ArrayInitContext)_localctx).type = array_init_tp();
				setState(187);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
				case 1:
					{
					setState(186);
					array();
					}
					break;
				}
				}
				break;
			case 7:
				{
				_localctx = new NumContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(189);
				match(NumberConst);
				}
				break;
			case 8:
				{
				_localctx = new StringContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(190);
				match(StringConst);
				}
				break;
			case 9:
				{
				_localctx = new FStringContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(191);
				form_string();
				}
				break;
			case 10:
				{
				_localctx = new TrueContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(192);
				match(True);
				}
				break;
			case 11:
				{
				_localctx = new FalseContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(193);
				match(False);
				}
				break;
			case 12:
				{
				_localctx = new NullContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(194);
				match(Null);
				}
				break;
			case 13:
				{
				_localctx = new BuiltInPtrContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(195);
				match(This);
				}
				break;
			case 14:
				{
				_localctx = new IdContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(196);
				match(Identifier);
				}
				break;
			case 15:
				{
				_localctx = new ArrContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(197);
				array();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(257);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(255);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
					case 1:
						{
						_localctx = new MulOpsContext(new ExprContext(_parentctx, _parentState));
						((MulOpsContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(200);
						if (!(precpred(_ctx, 21))) throw new FailedPredicateException(this, "precpred(_ctx, 21)");
						setState(201);
						((MulOpsContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 58720256L) != 0)) ) {
							((MulOpsContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(202);
						((MulOpsContext)_localctx).rhs = expr(22);
						}
						break;
					case 2:
						{
						_localctx = new AddOpsContext(new ExprContext(_parentctx, _parentState));
						((AddOpsContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(203);
						if (!(precpred(_ctx, 20))) throw new FailedPredicateException(this, "precpred(_ctx, 20)");
						setState(204);
						((AddOpsContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==Add || _la==Sub) ) {
							((AddOpsContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(205);
						((AddOpsContext)_localctx).rhs = expr(21);
						}
						break;
					case 3:
						{
						_localctx = new ShiftOpsContext(new ExprContext(_parentctx, _parentState));
						((ShiftOpsContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(206);
						if (!(precpred(_ctx, 19))) throw new FailedPredicateException(this, "precpred(_ctx, 19)");
						setState(207);
						((ShiftOpsContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==ShiftR || _la==ShiftL) ) {
							((ShiftOpsContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(208);
						((ShiftOpsContext)_localctx).rhs = expr(20);
						}
						break;
					case 4:
						{
						_localctx = new CompOpsContext(new ExprContext(_parentctx, _parentState));
						((CompOpsContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(209);
						if (!(precpred(_ctx, 18))) throw new FailedPredicateException(this, "precpred(_ctx, 18)");
						setState(210);
						((CompOpsContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 1006632960L) != 0)) ) {
							((CompOpsContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(211);
						((CompOpsContext)_localctx).rhs = expr(19);
						}
						break;
					case 5:
						{
						_localctx = new EqOpsContext(new ExprContext(_parentctx, _parentState));
						((EqOpsContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(212);
						if (!(precpred(_ctx, 17))) throw new FailedPredicateException(this, "precpred(_ctx, 17)");
						setState(213);
						((EqOpsContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==Ne || _la==Eq) ) {
							((EqOpsContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(214);
						((EqOpsContext)_localctx).rhs = expr(18);
						}
						break;
					case 6:
						{
						_localctx = new BAndContext(new ExprContext(_parentctx, _parentState));
						((BAndContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(215);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(216);
						((BAndContext)_localctx).op = match(BitAnd);
						setState(217);
						((BAndContext)_localctx).rhs = expr(17);
						}
						break;
					case 7:
						{
						_localctx = new BXorContext(new ExprContext(_parentctx, _parentState));
						((BXorContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(218);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(219);
						((BXorContext)_localctx).op = match(BitXor);
						setState(220);
						((BXorContext)_localctx).rhs = expr(16);
						}
						break;
					case 8:
						{
						_localctx = new BOrContext(new ExprContext(_parentctx, _parentState));
						((BOrContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(221);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(222);
						((BOrContext)_localctx).op = match(BitOr);
						setState(223);
						((BOrContext)_localctx).rhs = expr(15);
						}
						break;
					case 9:
						{
						_localctx = new LAndContext(new ExprContext(_parentctx, _parentState));
						((LAndContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(224);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(225);
						((LAndContext)_localctx).op = match(LogicAnd);
						setState(226);
						((LAndContext)_localctx).rhs = expr(14);
						}
						break;
					case 10:
						{
						_localctx = new LOrContext(new ExprContext(_parentctx, _parentState));
						((LOrContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(227);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(228);
						((LOrContext)_localctx).op = match(LogicOr);
						setState(229);
						((LOrContext)_localctx).rhs = expr(13);
						}
						break;
					case 11:
						{
						_localctx = new TernaryContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(230);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(231);
						((TernaryContext)_localctx).op = match(QuesMark);
						setState(232);
						expr(0);
						setState(233);
						match(Colon);
						setState(234);
						expr(11);
						}
						break;
					case 12:
						{
						_localctx = new AssignContext(new ExprContext(_parentctx, _parentState));
						((AssignContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(236);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(237);
						((AssignContext)_localctx).op = match(Assign);
						setState(238);
						((AssignContext)_localctx).rhs = expr(10);
						}
						break;
					case 13:
						{
						_localctx = new RSelfOpsContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(239);
						if (!(precpred(_ctx, 30))) throw new FailedPredicateException(this, "precpred(_ctx, 30)");
						setState(240);
						((RSelfOpsContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==SelfAdd || _la==SelfSub) ) {
							((RSelfOpsContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						break;
					case 14:
						{
						_localctx = new FuncCallContext(new ExprContext(_parentctx, _parentState));
						((FuncCallContext)_localctx).funcName = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(241);
						if (!(precpred(_ctx, 29))) throw new FailedPredicateException(this, "precpred(_ctx, 29)");
						setState(242);
						match(LP);
						setState(244);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (((((_la - 8)) & ~0x3f) == 0 && ((1L << (_la - 8)) & 116003480434401341L) != 0)) {
							{
							setState(243);
							expr_list();
							}
						}

						setState(246);
						match(RP);
						}
						break;
					case 15:
						{
						_localctx = new ArrayAccessContext(new ExprContext(_parentctx, _parentState));
						((ArrayAccessContext)_localctx).arrayName = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(247);
						if (!(precpred(_ctx, 28))) throw new FailedPredicateException(this, "precpred(_ctx, 28)");
						setState(248);
						match(LBracket);
						setState(249);
						((ArrayAccessContext)_localctx).serial = expr(0);
						setState(250);
						match(RBracket);
						}
						break;
					case 16:
						{
						_localctx = new MemberAccessContext(new ExprContext(_parentctx, _parentState));
						((MemberAccessContext)_localctx).object = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(252);
						if (!(precpred(_ctx, 27))) throw new FailedPredicateException(this, "precpred(_ctx, 27)");
						setState(253);
						((MemberAccessContext)_localctx).op = match(Component);
						setState(254);
						match(Identifier);
						}
						break;
					}
					} 
				}
				setState(259);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Form_stringContext extends ParserRuleContext {
		public TerminalNode FStringL() { return getToken(MxParser.FStringL, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode FStringR() { return getToken(MxParser.FStringR, 0); }
		public List<TerminalNode> FStringM() { return getTokens(MxParser.FStringM); }
		public TerminalNode FStringM(int i) {
			return getToken(MxParser.FStringM, i);
		}
		public TerminalNode FStringN() { return getToken(MxParser.FStringN, 0); }
		public Form_stringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_form_string; }
	}

	public final Form_stringContext form_string() throws RecognitionException {
		Form_stringContext _localctx = new Form_stringContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_form_string);
		int _la;
		try {
			setState(272);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FStringL:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(260);
				match(FStringL);
				setState(261);
				expr(0);
				setState(266);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==FStringM) {
					{
					{
					setState(262);
					match(FStringM);
					setState(263);
					expr(0);
					}
					}
					setState(268);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(269);
				match(FStringR);
				}
				}
				break;
			case FStringN:
				enterOuterAlt(_localctx, 2);
				{
				setState(271);
				match(FStringN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StmtContext extends ParserRuleContext {
		public TerminalNode LBrace() { return getToken(MxParser.LBrace, 0); }
		public TerminalNode RBrace() { return getToken(MxParser.RBrace, 0); }
		public List<StmtContext> stmt() {
			return getRuleContexts(StmtContext.class);
		}
		public StmtContext stmt(int i) {
			return getRuleContext(StmtContext.class,i);
		}
		public Empty_stmtContext empty_stmt() {
			return getRuleContext(Empty_stmtContext.class,0);
		}
		public Def_var_stmtContext def_var_stmt() {
			return getRuleContext(Def_var_stmtContext.class,0);
		}
		public If_stmtContext if_stmt() {
			return getRuleContext(If_stmtContext.class,0);
		}
		public While_stmtContext while_stmt() {
			return getRuleContext(While_stmtContext.class,0);
		}
		public For_stmtContext for_stmt() {
			return getRuleContext(For_stmtContext.class,0);
		}
		public Return_stmtContext return_stmt() {
			return getRuleContext(Return_stmtContext.class,0);
		}
		public Break_stmtContext break_stmt() {
			return getRuleContext(Break_stmtContext.class,0);
		}
		public Continue_stmtContext continue_stmt() {
			return getRuleContext(Continue_stmtContext.class,0);
		}
		public Expr_stmtContext expr_stmt() {
			return getRuleContext(Expr_stmtContext.class,0);
		}
		public StmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stmt; }
	}

	public final StmtContext stmt() throws RecognitionException {
		StmtContext _localctx = new StmtContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_stmt);
		int _la;
		try {
			setState(291);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(274);
				match(LBrace);
				setState(278);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la - 5)) & ~0x3f) == 0 && ((1L << (_la - 5)) & 928098212219452399L) != 0)) {
					{
					{
					setState(275);
					stmt();
					}
					}
					setState(280);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(281);
				match(RBrace);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(282);
				empty_stmt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(283);
				def_var_stmt();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(284);
				if_stmt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(285);
				while_stmt();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(286);
				for_stmt();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(287);
				return_stmt();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(288);
				break_stmt();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(289);
				continue_stmt();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(290);
				expr_stmt();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Def_var_stmtContext extends ParserRuleContext {
		public TypenameContext typename() {
			return getRuleContext(TypenameContext.class,0);
		}
		public List<TerminalNode> Identifier() { return getTokens(MxParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(MxParser.Identifier, i);
		}
		public TerminalNode Semicolon() { return getToken(MxParser.Semicolon, 0); }
		public List<TerminalNode> Assign() { return getTokens(MxParser.Assign); }
		public TerminalNode Assign(int i) {
			return getToken(MxParser.Assign, i);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> Comma() { return getTokens(MxParser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(MxParser.Comma, i);
		}
		public Def_var_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_def_var_stmt; }
	}

	public final Def_var_stmtContext def_var_stmt() throws RecognitionException {
		Def_var_stmtContext _localctx = new Def_var_stmtContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_def_var_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(293);
			typename();
			setState(294);
			match(Identifier);
			setState(297);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Assign) {
				{
				setState(295);
				match(Assign);
				setState(296);
				expr(0);
				}
			}

			setState(307);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(299);
				match(Comma);
				setState(300);
				match(Identifier);
				setState(303);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Assign) {
					{
					setState(301);
					match(Assign);
					setState(302);
					expr(0);
					}
				}

				}
				}
				setState(309);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(310);
			match(Semicolon);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class If_stmtContext extends ParserRuleContext {
		public ExprContext cond;
		public StmtContext thenStmt;
		public StmtContext elseStmt;
		public TerminalNode If() { return getToken(MxParser.If, 0); }
		public TerminalNode LP() { return getToken(MxParser.LP, 0); }
		public TerminalNode RP() { return getToken(MxParser.RP, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public List<StmtContext> stmt() {
			return getRuleContexts(StmtContext.class);
		}
		public StmtContext stmt(int i) {
			return getRuleContext(StmtContext.class,i);
		}
		public TerminalNode Else() { return getToken(MxParser.Else, 0); }
		public If_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_if_stmt; }
	}

	public final If_stmtContext if_stmt() throws RecognitionException {
		If_stmtContext _localctx = new If_stmtContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_if_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(312);
			match(If);
			setState(313);
			match(LP);
			setState(314);
			((If_stmtContext)_localctx).cond = expr(0);
			setState(315);
			match(RP);
			setState(316);
			((If_stmtContext)_localctx).thenStmt = stmt();
			setState(319);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				{
				setState(317);
				match(Else);
				setState(318);
				((If_stmtContext)_localctx).elseStmt = stmt();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class While_stmtContext extends ParserRuleContext {
		public ExprContext cond;
		public TerminalNode While() { return getToken(MxParser.While, 0); }
		public TerminalNode LP() { return getToken(MxParser.LP, 0); }
		public TerminalNode RP() { return getToken(MxParser.RP, 0); }
		public StmtContext stmt() {
			return getRuleContext(StmtContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public While_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_while_stmt; }
	}

	public final While_stmtContext while_stmt() throws RecognitionException {
		While_stmtContext _localctx = new While_stmtContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_while_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(321);
			match(While);
			setState(322);
			match(LP);
			setState(323);
			((While_stmtContext)_localctx).cond = expr(0);
			setState(324);
			match(RP);
			setState(325);
			stmt();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class For_stmtContext extends ParserRuleContext {
		public StmtContext init;
		public ExprContext cond;
		public ExprContext step;
		public TerminalNode For() { return getToken(MxParser.For, 0); }
		public TerminalNode LP() { return getToken(MxParser.LP, 0); }
		public TerminalNode Semicolon() { return getToken(MxParser.Semicolon, 0); }
		public TerminalNode RP() { return getToken(MxParser.RP, 0); }
		public List<StmtContext> stmt() {
			return getRuleContexts(StmtContext.class);
		}
		public StmtContext stmt(int i) {
			return getRuleContext(StmtContext.class,i);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public For_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_for_stmt; }
	}

	public final For_stmtContext for_stmt() throws RecognitionException {
		For_stmtContext _localctx = new For_stmtContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_for_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(327);
			match(For);
			setState(328);
			match(LP);
			setState(329);
			((For_stmtContext)_localctx).init = stmt();
			setState(331);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 8)) & ~0x3f) == 0 && ((1L << (_la - 8)) & 116003480434401341L) != 0)) {
				{
				setState(330);
				((For_stmtContext)_localctx).cond = expr(0);
				}
			}

			setState(333);
			match(Semicolon);
			setState(335);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 8)) & ~0x3f) == 0 && ((1L << (_la - 8)) & 116003480434401341L) != 0)) {
				{
				setState(334);
				((For_stmtContext)_localctx).step = expr(0);
				}
			}

			setState(337);
			match(RP);
			setState(338);
			stmt();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Return_stmtContext extends ParserRuleContext {
		public ExprContext val;
		public TerminalNode Return() { return getToken(MxParser.Return, 0); }
		public TerminalNode Semicolon() { return getToken(MxParser.Semicolon, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public Return_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_return_stmt; }
	}

	public final Return_stmtContext return_stmt() throws RecognitionException {
		Return_stmtContext _localctx = new Return_stmtContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_return_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(340);
			match(Return);
			setState(341);
			((Return_stmtContext)_localctx).val = expr(0);
			setState(342);
			match(Semicolon);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Break_stmtContext extends ParserRuleContext {
		public TerminalNode Break() { return getToken(MxParser.Break, 0); }
		public TerminalNode Semicolon() { return getToken(MxParser.Semicolon, 0); }
		public Break_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_break_stmt; }
	}

	public final Break_stmtContext break_stmt() throws RecognitionException {
		Break_stmtContext _localctx = new Break_stmtContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_break_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(344);
			match(Break);
			setState(345);
			match(Semicolon);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Continue_stmtContext extends ParserRuleContext {
		public TerminalNode Continue() { return getToken(MxParser.Continue, 0); }
		public TerminalNode Semicolon() { return getToken(MxParser.Semicolon, 0); }
		public Continue_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_continue_stmt; }
	}

	public final Continue_stmtContext continue_stmt() throws RecognitionException {
		Continue_stmtContext _localctx = new Continue_stmtContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_continue_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(347);
			match(Continue);
			setState(348);
			match(Semicolon);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Expr_stmtContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode Semicolon() { return getToken(MxParser.Semicolon, 0); }
		public Expr_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr_stmt; }
	}

	public final Expr_stmtContext expr_stmt() throws RecognitionException {
		Expr_stmtContext _localctx = new Expr_stmtContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_expr_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(350);
			expr(0);
			setState(351);
			match(Semicolon);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Empty_stmtContext extends ParserRuleContext {
		public TerminalNode Semicolon() { return getToken(MxParser.Semicolon, 0); }
		public Empty_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_empty_stmt; }
	}

	public final Empty_stmtContext empty_stmt() throws RecognitionException {
		Empty_stmtContext _localctx = new Empty_stmtContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_empty_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(353);
			match(Semicolon);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 10:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 21);
		case 1:
			return precpred(_ctx, 20);
		case 2:
			return precpred(_ctx, 19);
		case 3:
			return precpred(_ctx, 18);
		case 4:
			return precpred(_ctx, 17);
		case 5:
			return precpred(_ctx, 16);
		case 6:
			return precpred(_ctx, 15);
		case 7:
			return precpred(_ctx, 14);
		case 8:
			return precpred(_ctx, 13);
		case 9:
			return precpred(_ctx, 12);
		case 10:
			return precpred(_ctx, 11);
		case 11:
			return precpred(_ctx, 10);
		case 12:
			return precpred(_ctx, 30);
		case 13:
			return precpred(_ctx, 29);
		case 14:
			return precpred(_ctx, 28);
		case 15:
			return precpred(_ctx, 27);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001@\u0164\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0005\u00000\b\u0000\n\u0000\f\u0000"+
		"3\t\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002"+
		"\u0001\u0002\u0003\u0002;\b\u0002\u0001\u0002\u0001\u0002\u0005\u0002"+
		"?\b\u0002\n\u0002\f\u0002B\t\u0002\u0001\u0003\u0001\u0003\u0003\u0003"+
		"F\b\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0005\u0003K\b\u0003\n\u0003"+
		"\f\u0003N\t\u0003\u0001\u0003\u0001\u0003\u0003\u0003R\b\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0005\u0003X\b\u0003\n\u0003\f\u0003"+
		"[\t\u0003\u0001\u0004\u0001\u0004\u0003\u0004_\b\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0003\u0004d\b\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0005\u0004i\b\u0004\n\u0004\f\u0004l\t\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0005\u0005v\b\u0005\n\u0005\f\u0005y\t\u0005\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0005\u0006\u0081"+
		"\b\u0006\n\u0006\f\u0006\u0084\t\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0005\u0007"+
		"\u008e\b\u0007\n\u0007\f\u0007\u0091\t\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\b\u0001\b\u0001\b\u0001\b\u0005\b\u0099\b\b\n\b\f\b\u009c\t\b\u0001\b"+
		"\u0001\b\u0001\t\u0001\t\u0001\t\u0005\t\u00a3\b\t\n\t\f\t\u00a6\t\t\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0003\n\u00b7\b\n\u0001\n\u0001"+
		"\n\u0001\n\u0003\n\u00bc\b\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0003\n\u00c7\b\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0003\n\u00f5\b\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0005\n\u0100\b\n\n"+
		"\n\f\n\u0103\t\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0005"+
		"\u000b\u0109\b\u000b\n\u000b\f\u000b\u010c\t\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0003\u000b\u0111\b\u000b\u0001\f\u0001\f\u0005\f\u0115\b"+
		"\f\n\f\f\f\u0118\t\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0003\f\u0124\b\f\u0001\r\u0001\r\u0001\r\u0001"+
		"\r\u0003\r\u012a\b\r\u0001\r\u0001\r\u0001\r\u0001\r\u0003\r\u0130\b\r"+
		"\u0005\r\u0132\b\r\n\r\f\r\u0135\t\r\u0001\r\u0001\r\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0003"+
		"\u000e\u0140\b\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001"+
		"\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0003"+
		"\u0010\u014c\b\u0010\u0001\u0010\u0001\u0010\u0003\u0010\u0150\b\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0000\u0001\u0014\u0016\u0000\u0002\u0004\u0006\b\n\f\u000e"+
		"\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*\u0000\b\u0001"+
		"\u0000\u0005\u0007\u0001\u0000*+\u0001\u0000\u0015\u0016\u0002\u0000\""+
		"\"((\u0001\u0000\u0017\u0019\u0001\u0000#$\u0001\u0000\u001a\u001d\u0001"+
		"\u0000\u001e\u001f\u0193\u00001\u0001\u0000\u0000\u0000\u00026\u0001\u0000"+
		"\u0000\u0000\u0004:\u0001\u0000\u0000\u0000\u0006E\u0001\u0000\u0000\u0000"+
		"\b^\u0001\u0000\u0000\u0000\no\u0001\u0000\u0000\u0000\fz\u0001\u0000"+
		"\u0000\u0000\u000e\u0088\u0001\u0000\u0000\u0000\u0010\u0094\u0001\u0000"+
		"\u0000\u0000\u0012\u009f\u0001\u0000\u0000\u0000\u0014\u00c6\u0001\u0000"+
		"\u0000\u0000\u0016\u0110\u0001\u0000\u0000\u0000\u0018\u0123\u0001\u0000"+
		"\u0000\u0000\u001a\u0125\u0001\u0000\u0000\u0000\u001c\u0138\u0001\u0000"+
		"\u0000\u0000\u001e\u0141\u0001\u0000\u0000\u0000 \u0147\u0001\u0000\u0000"+
		"\u0000\"\u0154\u0001\u0000\u0000\u0000$\u0158\u0001\u0000\u0000\u0000"+
		"&\u015b\u0001\u0000\u0000\u0000(\u015e\u0001\u0000\u0000\u0000*\u0161"+
		"\u0001\u0000\u0000\u0000,0\u0003\b\u0004\u0000-0\u0003\f\u0006\u0000."+
		"0\u0003\u001a\r\u0000/,\u0001\u0000\u0000\u0000/-\u0001\u0000\u0000\u0000"+
		"/.\u0001\u0000\u0000\u000003\u0001\u0000\u0000\u00001/\u0001\u0000\u0000"+
		"\u000012\u0001\u0000\u0000\u000024\u0001\u0000\u0000\u000031\u0001\u0000"+
		"\u0000\u000045\u0005\u0000\u0000\u00015\u0001\u0001\u0000\u0000\u0000"+
		"67\u0007\u0000\u0000\u00007\u0003\u0001\u0000\u0000\u00008;\u0003\u0002"+
		"\u0001\u00009;\u0005:\u0000\u0000:8\u0001\u0000\u0000\u0000:9\u0001\u0000"+
		"\u0000\u0000;@\u0001\u0000\u0000\u0000<=\u0005-\u0000\u0000=?\u0005.\u0000"+
		"\u0000><\u0001\u0000\u0000\u0000?B\u0001\u0000\u0000\u0000@>\u0001\u0000"+
		"\u0000\u0000@A\u0001\u0000\u0000\u0000A\u0005\u0001\u0000\u0000\u0000"+
		"B@\u0001\u0000\u0000\u0000CF\u0003\u0002\u0001\u0000DF\u0005:\u0000\u0000"+
		"EC\u0001\u0000\u0000\u0000ED\u0001\u0000\u0000\u0000FL\u0001\u0000\u0000"+
		"\u0000GH\u0005-\u0000\u0000HI\u0005@\u0000\u0000IK\u0005.\u0000\u0000"+
		"JG\u0001\u0000\u0000\u0000KN\u0001\u0000\u0000\u0000LJ\u0001\u0000\u0000"+
		"\u0000LM\u0001\u0000\u0000\u0000MO\u0001\u0000\u0000\u0000NL\u0001\u0000"+
		"\u0000\u0000OQ\u0005-\u0000\u0000PR\u0005@\u0000\u0000QP\u0001\u0000\u0000"+
		"\u0000QR\u0001\u0000\u0000\u0000RS\u0001\u0000\u0000\u0000ST\u0005.\u0000"+
		"\u0000TY\u0001\u0000\u0000\u0000UV\u0005-\u0000\u0000VX\u0005.\u0000\u0000"+
		"WU\u0001\u0000\u0000\u0000X[\u0001\u0000\u0000\u0000YW\u0001\u0000\u0000"+
		"\u0000YZ\u0001\u0000\u0000\u0000Z\u0007\u0001\u0000\u0000\u0000[Y\u0001"+
		"\u0000\u0000\u0000\\_\u0003\u0004\u0002\u0000]_\u0005\u0004\u0000\u0000"+
		"^\\\u0001\u0000\u0000\u0000^]\u0001\u0000\u0000\u0000_`\u0001\u0000\u0000"+
		"\u0000`a\u0005:\u0000\u0000ac\u0005/\u0000\u0000bd\u0003\n\u0005\u0000"+
		"cb\u0001\u0000\u0000\u0000cd\u0001\u0000\u0000\u0000de\u0001\u0000\u0000"+
		"\u0000ef\u00050\u0000\u0000fj\u00055\u0000\u0000gi\u0003\u0018\f\u0000"+
		"hg\u0001\u0000\u0000\u0000il\u0001\u0000\u0000\u0000jh\u0001\u0000\u0000"+
		"\u0000jk\u0001\u0000\u0000\u0000km\u0001\u0000\u0000\u0000lj\u0001\u0000"+
		"\u0000\u0000mn\u00056\u0000\u0000n\t\u0001\u0000\u0000\u0000op\u0003\u0004"+
		"\u0002\u0000pw\u0005:\u0000\u0000qr\u00054\u0000\u0000rs\u0003\u0004\u0002"+
		"\u0000st\u0005:\u0000\u0000tv\u0001\u0000\u0000\u0000uq\u0001\u0000\u0000"+
		"\u0000vy\u0001\u0000\u0000\u0000wu\u0001\u0000\u0000\u0000wx\u0001\u0000"+
		"\u0000\u0000x\u000b\u0001\u0000\u0000\u0000yw\u0001\u0000\u0000\u0000"+
		"z{\u0005\t\u0000\u0000{|\u0005:\u0000\u0000|\u0082\u00055\u0000\u0000"+
		"}\u0081\u0003\u001a\r\u0000~\u0081\u0003\b\u0004\u0000\u007f\u0081\u0003"+
		"\u000e\u0007\u0000\u0080}\u0001\u0000\u0000\u0000\u0080~\u0001\u0000\u0000"+
		"\u0000\u0080\u007f\u0001\u0000\u0000\u0000\u0081\u0084\u0001\u0000\u0000"+
		"\u0000\u0082\u0080\u0001\u0000\u0000\u0000\u0082\u0083\u0001\u0000\u0000"+
		"\u0000\u0083\u0085\u0001\u0000\u0000\u0000\u0084\u0082\u0001\u0000\u0000"+
		"\u0000\u0085\u0086\u00056\u0000\u0000\u0086\u0087\u00053\u0000\u0000\u0087"+
		"\r\u0001\u0000\u0000\u0000\u0088\u0089\u0005:\u0000\u0000\u0089\u008a"+
		"\u0005/\u0000\u0000\u008a\u008b\u00050\u0000\u0000\u008b\u008f\u00055"+
		"\u0000\u0000\u008c\u008e\u0003\u0018\f\u0000\u008d\u008c\u0001\u0000\u0000"+
		"\u0000\u008e\u0091\u0001\u0000\u0000\u0000\u008f\u008d\u0001\u0000\u0000"+
		"\u0000\u008f\u0090\u0001\u0000\u0000\u0000\u0090\u0092\u0001\u0000\u0000"+
		"\u0000\u0091\u008f\u0001\u0000\u0000\u0000\u0092\u0093\u00056\u0000\u0000"+
		"\u0093\u000f\u0001\u0000\u0000\u0000\u0094\u0095\u00055\u0000\u0000\u0095"+
		"\u009a\u0003\u0014\n\u0000\u0096\u0097\u00054\u0000\u0000\u0097\u0099"+
		"\u0003\u0014\n\u0000\u0098\u0096\u0001\u0000\u0000\u0000\u0099\u009c\u0001"+
		"\u0000\u0000\u0000\u009a\u0098\u0001\u0000\u0000\u0000\u009a\u009b\u0001"+
		"\u0000\u0000\u0000\u009b\u009d\u0001\u0000\u0000\u0000\u009c\u009a\u0001"+
		"\u0000\u0000\u0000\u009d\u009e\u00056\u0000\u0000\u009e\u0011\u0001\u0000"+
		"\u0000\u0000\u009f\u00a4\u0003\u0014\n\u0000\u00a0\u00a1\u00054\u0000"+
		"\u0000\u00a1\u00a3\u0003\u0014\n\u0000\u00a2\u00a0\u0001\u0000\u0000\u0000"+
		"\u00a3\u00a6\u0001\u0000\u0000\u0000\u00a4\u00a2\u0001\u0000\u0000\u0000"+
		"\u00a4\u00a5\u0001\u0000\u0000\u0000\u00a5\u0013\u0001\u0000\u0000\u0000"+
		"\u00a6\u00a4\u0001\u0000\u0000\u0000\u00a7\u00a8\u0006\n\uffff\uffff\u0000"+
		"\u00a8\u00a9\u0005/\u0000\u0000\u00a9\u00aa\u0003\u0014\n\u0000\u00aa"+
		"\u00ab\u00050\u0000\u0000\u00ab\u00c7\u0001\u0000\u0000\u0000\u00ac\u00ad"+
		"\u0007\u0001\u0000\u0000\u00ad\u00c7\u0003\u0014\n\u001a\u00ae\u00af\u0007"+
		"\u0002\u0000\u0000\u00af\u00c7\u0003\u0014\n\u0019\u00b0\u00b1\u0007\u0003"+
		"\u0000\u0000\u00b1\u00c7\u0003\u0014\n\u0018\u00b2\u00b3\u0005\b\u0000"+
		"\u0000\u00b3\u00b6\u0003\u0004\u0002\u0000\u00b4\u00b5\u0005/\u0000\u0000"+
		"\u00b5\u00b7\u00050\u0000\u0000\u00b6\u00b4\u0001\u0000\u0000\u0000\u00b6"+
		"\u00b7\u0001\u0000\u0000\u0000\u00b7\u00c7\u0001\u0000\u0000\u0000\u00b8"+
		"\u00b9\u0005\b\u0000\u0000\u00b9\u00bb\u0003\u0006\u0003\u0000\u00ba\u00bc"+
		"\u0003\u0010\b\u0000\u00bb\u00ba\u0001\u0000\u0000\u0000\u00bb\u00bc\u0001"+
		"\u0000\u0000\u0000\u00bc\u00c7\u0001\u0000\u0000\u0000\u00bd\u00c7\u0005"+
		"@\u0000\u0000\u00be\u00c7\u0005;\u0000\u0000\u00bf\u00c7\u0003\u0016\u000b"+
		"\u0000\u00c0\u00c7\u0005\u000b\u0000\u0000\u00c1\u00c7\u0005\f\u0000\u0000"+
		"\u00c2\u00c7\u0005\n\u0000\u0000\u00c3\u00c7\u0005\r\u0000\u0000\u00c4"+
		"\u00c7\u0005:\u0000\u0000\u00c5\u00c7\u0003\u0010\b\u0000\u00c6\u00a7"+
		"\u0001\u0000\u0000\u0000\u00c6\u00ac\u0001\u0000\u0000\u0000\u00c6\u00ae"+
		"\u0001\u0000\u0000\u0000\u00c6\u00b0\u0001\u0000\u0000\u0000\u00c6\u00b2"+
		"\u0001\u0000\u0000\u0000\u00c6\u00b8\u0001\u0000\u0000\u0000\u00c6\u00bd"+
		"\u0001\u0000\u0000\u0000\u00c6\u00be\u0001\u0000\u0000\u0000\u00c6\u00bf"+
		"\u0001\u0000\u0000\u0000\u00c6\u00c0\u0001\u0000\u0000\u0000\u00c6\u00c1"+
		"\u0001\u0000\u0000\u0000\u00c6\u00c2\u0001\u0000\u0000\u0000\u00c6\u00c3"+
		"\u0001\u0000\u0000\u0000\u00c6\u00c4\u0001\u0000\u0000\u0000\u00c6\u00c5"+
		"\u0001\u0000\u0000\u0000\u00c7\u0101\u0001\u0000\u0000\u0000\u00c8\u00c9"+
		"\n\u0015\u0000\u0000\u00c9\u00ca\u0007\u0004\u0000\u0000\u00ca\u0100\u0003"+
		"\u0014\n\u0016\u00cb\u00cc\n\u0014\u0000\u0000\u00cc\u00cd\u0007\u0002"+
		"\u0000\u0000\u00cd\u0100\u0003\u0014\n\u0015\u00ce\u00cf\n\u0013\u0000"+
		"\u0000\u00cf\u00d0\u0007\u0005\u0000\u0000\u00d0\u0100\u0003\u0014\n\u0014"+
		"\u00d1\u00d2\n\u0012\u0000\u0000\u00d2\u00d3\u0007\u0006\u0000\u0000\u00d3"+
		"\u0100\u0003\u0014\n\u0013\u00d4\u00d5\n\u0011\u0000\u0000\u00d5\u00d6"+
		"\u0007\u0007\u0000\u0000\u00d6\u0100\u0003\u0014\n\u0012\u00d7\u00d8\n"+
		"\u0010\u0000\u0000\u00d8\u00d9\u0005%\u0000\u0000\u00d9\u0100\u0003\u0014"+
		"\n\u0011\u00da\u00db\n\u000f\u0000\u0000\u00db\u00dc\u0005\'\u0000\u0000"+
		"\u00dc\u0100\u0003\u0014\n\u0010\u00dd\u00de\n\u000e\u0000\u0000\u00de"+
		"\u00df\u0005&\u0000\u0000\u00df\u0100\u0003\u0014\n\u000f\u00e0\u00e1"+
		"\n\r\u0000\u0000\u00e1\u00e2\u0005 \u0000\u0000\u00e2\u0100\u0003\u0014"+
		"\n\u000e\u00e3\u00e4\n\f\u0000\u0000\u00e4\u00e5\u0005!\u0000\u0000\u00e5"+
		"\u0100\u0003\u0014\n\r\u00e6\u00e7\n\u000b\u0000\u0000\u00e7\u00e8\u0005"+
		"1\u0000\u0000\u00e8\u00e9\u0003\u0014\n\u0000\u00e9\u00ea\u00052\u0000"+
		"\u0000\u00ea\u00eb\u0003\u0014\n\u000b\u00eb\u0100\u0001\u0000\u0000\u0000"+
		"\u00ec\u00ed\n\n\u0000\u0000\u00ed\u00ee\u0005)\u0000\u0000\u00ee\u0100"+
		"\u0003\u0014\n\n\u00ef\u00f0\n\u001e\u0000\u0000\u00f0\u0100\u0007\u0001"+
		"\u0000\u0000\u00f1\u00f2\n\u001d\u0000\u0000\u00f2\u00f4\u0005/\u0000"+
		"\u0000\u00f3\u00f5\u0003\u0012\t\u0000\u00f4\u00f3\u0001\u0000\u0000\u0000"+
		"\u00f4\u00f5\u0001\u0000\u0000\u0000\u00f5\u00f6\u0001\u0000\u0000\u0000"+
		"\u00f6\u0100\u00050\u0000\u0000\u00f7\u00f8\n\u001c\u0000\u0000\u00f8"+
		"\u00f9\u0005-\u0000\u0000\u00f9\u00fa\u0003\u0014\n\u0000\u00fa\u00fb"+
		"\u0005.\u0000\u0000\u00fb\u0100\u0001\u0000\u0000\u0000\u00fc\u00fd\n"+
		"\u001b\u0000\u0000\u00fd\u00fe\u0005,\u0000\u0000\u00fe\u0100\u0005:\u0000"+
		"\u0000\u00ff\u00c8\u0001\u0000\u0000\u0000\u00ff\u00cb\u0001\u0000\u0000"+
		"\u0000\u00ff\u00ce\u0001\u0000\u0000\u0000\u00ff\u00d1\u0001\u0000\u0000"+
		"\u0000\u00ff\u00d4\u0001\u0000\u0000\u0000\u00ff\u00d7\u0001\u0000\u0000"+
		"\u0000\u00ff\u00da\u0001\u0000\u0000\u0000\u00ff\u00dd\u0001\u0000\u0000"+
		"\u0000\u00ff\u00e0\u0001\u0000\u0000\u0000\u00ff\u00e3\u0001\u0000\u0000"+
		"\u0000\u00ff\u00e6\u0001\u0000\u0000\u0000\u00ff\u00ec\u0001\u0000\u0000"+
		"\u0000\u00ff\u00ef\u0001\u0000\u0000\u0000\u00ff\u00f1\u0001\u0000\u0000"+
		"\u0000\u00ff\u00f7\u0001\u0000\u0000\u0000\u00ff\u00fc\u0001\u0000\u0000"+
		"\u0000\u0100\u0103\u0001\u0000\u0000\u0000\u0101\u00ff\u0001\u0000\u0000"+
		"\u0000\u0101\u0102\u0001\u0000\u0000\u0000\u0102\u0015\u0001\u0000\u0000"+
		"\u0000\u0103\u0101\u0001\u0000\u0000\u0000\u0104\u0105\u0005<\u0000\u0000"+
		"\u0105\u010a\u0003\u0014\n\u0000\u0106\u0107\u0005=\u0000\u0000\u0107"+
		"\u0109\u0003\u0014\n\u0000\u0108\u0106\u0001\u0000\u0000\u0000\u0109\u010c"+
		"\u0001\u0000\u0000\u0000\u010a\u0108\u0001\u0000\u0000\u0000\u010a\u010b"+
		"\u0001\u0000\u0000\u0000\u010b\u010d\u0001\u0000\u0000\u0000\u010c\u010a"+
		"\u0001\u0000\u0000\u0000\u010d\u010e\u0005>\u0000\u0000\u010e\u0111\u0001"+
		"\u0000\u0000\u0000\u010f\u0111\u0005?\u0000\u0000\u0110\u0104\u0001\u0000"+
		"\u0000\u0000\u0110\u010f\u0001\u0000\u0000\u0000\u0111\u0017\u0001\u0000"+
		"\u0000\u0000\u0112\u0116\u00055\u0000\u0000\u0113\u0115\u0003\u0018\f"+
		"\u0000\u0114\u0113\u0001\u0000\u0000\u0000\u0115\u0118\u0001\u0000\u0000"+
		"\u0000\u0116\u0114\u0001\u0000\u0000\u0000\u0116\u0117\u0001\u0000\u0000"+
		"\u0000\u0117\u0119\u0001\u0000\u0000\u0000\u0118\u0116\u0001\u0000\u0000"+
		"\u0000\u0119\u0124\u00056\u0000\u0000\u011a\u0124\u0003*\u0015\u0000\u011b"+
		"\u0124\u0003\u001a\r\u0000\u011c\u0124\u0003\u001c\u000e\u0000\u011d\u0124"+
		"\u0003\u001e\u000f\u0000\u011e\u0124\u0003 \u0010\u0000\u011f\u0124\u0003"+
		"\"\u0011\u0000\u0120\u0124\u0003$\u0012\u0000\u0121\u0124\u0003&\u0013"+
		"\u0000\u0122\u0124\u0003(\u0014\u0000\u0123\u0112\u0001\u0000\u0000\u0000"+
		"\u0123\u011a\u0001\u0000\u0000\u0000\u0123\u011b\u0001\u0000\u0000\u0000"+
		"\u0123\u011c\u0001\u0000\u0000\u0000\u0123\u011d\u0001\u0000\u0000\u0000"+
		"\u0123\u011e\u0001\u0000\u0000\u0000\u0123\u011f\u0001\u0000\u0000\u0000"+
		"\u0123\u0120\u0001\u0000\u0000\u0000\u0123\u0121\u0001\u0000\u0000\u0000"+
		"\u0123\u0122\u0001\u0000\u0000\u0000\u0124\u0019\u0001\u0000\u0000\u0000"+
		"\u0125\u0126\u0003\u0004\u0002\u0000\u0126\u0129\u0005:\u0000\u0000\u0127"+
		"\u0128\u0005)\u0000\u0000\u0128\u012a\u0003\u0014\n\u0000\u0129\u0127"+
		"\u0001\u0000\u0000\u0000\u0129\u012a\u0001\u0000\u0000\u0000\u012a\u0133"+
		"\u0001\u0000\u0000\u0000\u012b\u012c\u00054\u0000\u0000\u012c\u012f\u0005"+
		":\u0000\u0000\u012d\u012e\u0005)\u0000\u0000\u012e\u0130\u0003\u0014\n"+
		"\u0000\u012f\u012d\u0001\u0000\u0000\u0000\u012f\u0130\u0001\u0000\u0000"+
		"\u0000\u0130\u0132\u0001\u0000\u0000\u0000\u0131\u012b\u0001\u0000\u0000"+
		"\u0000\u0132\u0135\u0001\u0000\u0000\u0000\u0133\u0131\u0001\u0000\u0000"+
		"\u0000\u0133\u0134\u0001\u0000\u0000\u0000\u0134\u0136\u0001\u0000\u0000"+
		"\u0000\u0135\u0133\u0001\u0000\u0000\u0000\u0136\u0137\u00053\u0000\u0000"+
		"\u0137\u001b\u0001\u0000\u0000\u0000\u0138\u0139\u0005\u000e\u0000\u0000"+
		"\u0139\u013a\u0005/\u0000\u0000\u013a\u013b\u0003\u0014\n\u0000\u013b"+
		"\u013c\u00050\u0000\u0000\u013c\u013f\u0003\u0018\f\u0000\u013d\u013e"+
		"\u0005\u000f\u0000\u0000\u013e\u0140\u0003\u0018\f\u0000\u013f\u013d\u0001"+
		"\u0000\u0000\u0000\u013f\u0140\u0001\u0000\u0000\u0000\u0140\u001d\u0001"+
		"\u0000\u0000\u0000\u0141\u0142\u0005\u0011\u0000\u0000\u0142\u0143\u0005"+
		"/\u0000\u0000\u0143\u0144\u0003\u0014\n\u0000\u0144\u0145\u00050\u0000"+
		"\u0000\u0145\u0146\u0003\u0018\f\u0000\u0146\u001f\u0001\u0000\u0000\u0000"+
		"\u0147\u0148\u0005\u0010\u0000\u0000\u0148\u0149\u0005/\u0000\u0000\u0149"+
		"\u014b\u0003\u0018\f\u0000\u014a\u014c\u0003\u0014\n\u0000\u014b\u014a"+
		"\u0001\u0000\u0000\u0000\u014b\u014c\u0001\u0000\u0000\u0000\u014c\u014d"+
		"\u0001\u0000\u0000\u0000\u014d\u014f\u00053\u0000\u0000\u014e\u0150\u0003"+
		"\u0014\n\u0000\u014f\u014e\u0001\u0000\u0000\u0000\u014f\u0150\u0001\u0000"+
		"\u0000\u0000\u0150\u0151\u0001\u0000\u0000\u0000\u0151\u0152\u00050\u0000"+
		"\u0000\u0152\u0153\u0003\u0018\f\u0000\u0153!\u0001\u0000\u0000\u0000"+
		"\u0154\u0155\u0005\u0014\u0000\u0000\u0155\u0156\u0003\u0014\n\u0000\u0156"+
		"\u0157\u00053\u0000\u0000\u0157#\u0001\u0000\u0000\u0000\u0158\u0159\u0005"+
		"\u0012\u0000\u0000\u0159\u015a\u00053\u0000\u0000\u015a%\u0001\u0000\u0000"+
		"\u0000\u015b\u015c\u0005\u0013\u0000\u0000\u015c\u015d\u00053\u0000\u0000"+
		"\u015d\'\u0001\u0000\u0000\u0000\u015e\u015f\u0003\u0014\n\u0000\u015f"+
		"\u0160\u00053\u0000\u0000\u0160)\u0001\u0000\u0000\u0000\u0161\u0162\u0005"+
		"3\u0000\u0000\u0162+\u0001\u0000\u0000\u0000!/1:@ELQY^cjw\u0080\u0082"+
		"\u008f\u009a\u00a4\u00b6\u00bb\u00c6\u00f4\u00ff\u0101\u010a\u0110\u0116"+
		"\u0123\u0129\u012f\u0133\u013f\u014b\u014f";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}