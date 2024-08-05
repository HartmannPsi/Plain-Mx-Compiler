parser grammar MxParser;

options{
	tokenVocab = MxLexer;
}

prog: (def_func | def_class | def_var_stmt)* EOF;

basic_type: Int | String | Bool;

typename: (basic_type | Identifier) (LBracket RBracket)*;

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

const_val:
	array
	| StringConst
	| NumberConst
	| True
	| False
	| Null;

array: LBrace expr (Comma expr)* RBrace;

expr_list: expr (Comma expr)*;

expr:
	LP expr RP
	| expr op = (SelfAdd | SelfSub)
	| funcName = expr LP expr_list? RP
	| arrayName = expr LBracket serial = expr RBracket
	| object = expr op = Component Identifier
	| <assoc = right> op = (SelfAdd | SelfSub) expr
	| <assoc = right> op = (Add | Sub) expr
	| <assoc = right> op = (LogicNot | BitNot) expr
	| <assoc = right> op = New type = typename (LP RP)?
	| <assoc = right> op = New type = typename array?
	| lhs = expr op = (Mul | Div | Mod) rhs = expr
	| lhs = expr op = (Add | Sub) rhs = expr
	| lhs = expr op = (ShiftL | ShiftR) rhs = expr
	| lhs = expr op = (Lt | Le | Gt | Ge) rhs = expr
	| lhs = expr op = (Eq | Ne) rhs = expr
	| lhs = expr op = BitAnd rhs = expr
	| lhs = expr op = BitXor rhs = expr
	| lhs = expr op = BitOr rhs = expr
	| lhs = expr op = LogicAnd rhs = expr
	| lhs = expr op = LogicOr rhs = expr
	| <assoc = right> expr op = QuesMark expr Colon expr
	| <assoc = right> lhs = expr op = Assign rhs = expr
	| NumberConst
	| StringConst
	| form_string
	| True
	| False
	| Null
	| This
	| Identifier
	| array;

form_string: (FStringL expr (FStringM expr)* FStringR)
	| FStringN;

stmt:
	(LBrace stmt* RBrace)
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
	For LP init = stmt cond = expr? Semicolon step = expr? RP stmt;

return_stmt: Return val = expr Semicolon;

break_stmt: Break Semicolon;

continue_stmt: Continue Semicolon;

expr_stmt: expr Semicolon;

empty_stmt: Semicolon;