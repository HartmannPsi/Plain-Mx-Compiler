parser grammar MxParser;

//TODO: 数组初始化语句修改

options{
	tokenVocab = MxLexer;
}

prog: (def_func | def_class | def_var_stmt)* EOF;

basic_type: Int | String | Bool;

typename: (basic_type | Identifier) (LBracket RBracket)*;

array_init_tp: (basic_type | Identifier) (
		LBracket expr? RBracket
	)+;

def_func:
	(returnType = typename | Void) funcName = Identifier LP (
		paras = para_list
	)? RP LBrace code = stmt* RBrace;

para_list: (typename Identifier (Comma typename Identifier)*);

def_class:
	Class className = Identifier LBrace (
		def_var_stmt
		| def_func
		| class_constructor
	)* RBrace Semicolon;

class_constructor:
	className = Identifier LP RP LBrace code = stmt* RBrace;

array: LBrace (expr (Comma expr)*)? RBrace;

expr_list: expr (Comma expr)*;

expr:
	LP expr RP												# ParenExpr
	| expr op = (SelfAdd | SelfSub)							# RSelfOps
	| funcName = expr LP expr_list? RP						# FuncCall
	| arrayName = expr LBracket serial = expr RBracket		# ArrayAccess
	| object = expr op = Component Identifier				# MemberAccess
	| <assoc = right> op = (SelfAdd | SelfSub) expr			# LSelfOps
	| <assoc = right> op = (Add | Sub) expr					# SignOps
	| <assoc = right> op = (LogicNot | BitNot) expr			# NotOps
	| <assoc = right> op = New type = array_init_tp array?	# ArrayInit
	| <assoc = right> op = New type = typename (LP RP)?		# ObjectInit
	| lhs = expr op = (Mul | Div | Mod) rhs = expr			# MulOps
	| lhs = expr op = (Add | Sub) rhs = expr				# AddOps
	| lhs = expr op = (ShiftL | ShiftR) rhs = expr			# ShiftOps
	| lhs = expr op = (Lt | Le | Gt | Ge) rhs = expr		# CompOps
	| lhs = expr op = (Eq | Ne) rhs = expr					# EqOps
	| lhs = expr op = BitAnd rhs = expr						# BAnd
	| lhs = expr op = BitXor rhs = expr						# BXor
	| lhs = expr op = BitOr rhs = expr						# BOr
	| lhs = expr op = LogicAnd rhs = expr					# LAnd
	| lhs = expr op = LogicOr rhs = expr					# LOr
	| <assoc = right> expr op = QuesMark expr Colon expr	# Ternary
	| <assoc = right> lhs = expr op = Assign rhs = expr		# Assign
	| NumberConst											# Num
	| StringConst											# String
	| form_string											# FString
	| True													# True
	| False													# False
	| Null													# Null
	| This													# BuiltInPtr
	| Identifier											# Id
	| array													# Arr;

form_string: (FStringL expr (FStringM expr)* FStringR)
	| FStringN;

stmt:
	(LBrace stmt* RBrace)	# NewStmtScope
	| empty_stmt			# OtherStmt
	| def_var_stmt			# OtherStmt
	| if_stmt				# OtherStmt
	| while_stmt			# OtherStmt
	| for_stmt				# OtherStmt
	| return_stmt			# OtherStmt
	| break_stmt			# OtherStmt
	| continue_stmt			# OtherStmt
	| expr_stmt				# OtherStmt;

var_def: Identifier (Assign expr)?;

def_var_stmt: typename var_def (Comma var_def)* Semicolon;

if_stmt: If LP cond = expr RP (stmt) (Else (stmt))?;

while_stmt:
	While LP cond = expr RP ((LBrace stmt* RBrace) | stmt);

for_stmt:
	For LP init = stmt cond = expr? Semicolon step = expr? RP (
		( LBrace stmt* RBrace)
		| stmt
	);

return_stmt: Return val = expr? Semicolon;

break_stmt: Break Semicolon;

continue_stmt: Continue Semicolon;

expr_stmt: expr Semicolon;

empty_stmt: Semicolon;