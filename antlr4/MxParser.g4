parser grammar MxParser;

options{
	tokenVocab = MxLexer;
}

prog: (def_func | def_class | def_var_stmt)* EOF;

basic_type: Int | String | Bool;

typename: basic_type | Identifier;

def_func:
	(returnType = typename | Void) funcId = Identifier LP (
		paras = para_list
	)? RP LBrace code = stmt* RBrace;

para_list: (typename Identifier (Comma typename Identifier)*);

def_class:
	Class className = Identifier LBrace (
		def_var_stmt
		| def_func
		| class_constructor
	)* RBrace;

class_constructor:
	className = Identifier LP RP LBrace code = stmt* RBrace;

const_val:
	array_const
	| StringConst
	| NumberConst
	| LogicConst
	| Null;

array_const: LBrace const_val (Comma const_val)* RBrace;

expr_list: expr (Comma expr)*;

expr:
	LP expr RP
	| expr op = (SelfAdd | SelfSub)
	| expr LP expr_list? RP
	| expr LBracket expr RBracket
	| expr op = Component Identifier
	| <assoc = right> op = (SelfAdd | SelfSub) expr
	| <assoc = right> op = (Add | Sub) expr
	| <assoc = right> op = (LogicNot | BitNot) expr
	| <assoc = right> op = New typename (LP RP)?
	| expr op = (Mul | Div | Mod) expr
	| expr op = (Add | Sub) expr
	| expr op = (ShiftL | ShiftR) expr
	| expr op = (Lt | Le | Gt | Ge) expr
	| expr op = (Eq | Ne) expr
	| expr op = BitAnd expr
	| expr op = BitXor expr
	| expr op = BitOr expr
	| expr op = LogicAnd expr
	| expr op = LogicOr expr
	| <assoc = right> expr op = QuesMark expr Colon expr
	| <assoc = right> expr op = Assign expr
	| NumberConst
	| StringConst
	| LogicConst
	| Null
	| This
	| Identifier;

stmt:
	LBrace stmt* RBrace
	| empty_stmt
	| def_var_stmt
	| if_stmt
	| while_stmt
	| for_stmt
	| return_stmt
	| break_stmt
	| continue_stmt
	| expr_stmt;

def_var_stmt:
	typename Identifier (Assign expr)? (
		Comma Identifier (Assign expr)?
	)* Semicolon;

if_stmt:
	If LP cond = expr RP thenStmt = stmt (Else elseStmt = stmt)?;

while_stmt: While LP cond = expr RP stmt;

for_stmt:
	For LP init = stmt (cond = expr_stmt | empty_stmt) step = expr? RP stmt;

return_stmt: Return val = expr Semicolon;

break_stmt: Break Semicolon;

continue_stmt: Continue Semicolon;

expr_stmt: expr Semicolon;

empty_stmt: Semicolon;