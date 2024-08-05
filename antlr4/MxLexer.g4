lexer grammar MxLexer;

//NL: '\r'? '\n';
LineComm: '//' .*? (('\r'? '\n') | EOF) -> skip;
BlockComm: '/*' .*? '*/' -> skip;
WS: ('\r' | '\n' | ' ' | '\t') -> skip;

Void: 'void';
Bool: 'bool';
Int: 'int';
String: 'string';
New: 'new';
Class: 'class';
Null: 'null';
True: 'true';
False: 'false';
This: 'this';
If: 'if';
Else: 'else';
For: 'for';
While: 'while';
Break: 'break';
Continue: 'continue';
Return: 'return';

Add: '+';
Sub: '-';
Mul: '*';
Div: '/';
Mod: '%';

Gt: '>';
Lt: '<';
Ge: '>=';
Le: '<=';
Ne: '!=';
Eq: '==';

LogicAnd: '&&';
LogicOr: '||';
LogicNot: '!';

ShiftR: '>>';
ShiftL: '<<';
BitAnd: '&';
BitOr: '|';
BitXor: '^';
BitNot: '~';

Assign: '=';

SelfAdd: '++';
SelfSub: '--';

Component: '.';

LBracket: '[';
RBracket: ']';

LP: '(';
RP: ')';

QuesMark: '?';
Colon: ':';

Semicolon: ';';
Comma: ',';
LBrace: '{';
RBrace: '}';

FQuote: 'f"';

Quote: '"';

Dollar: '$';

Identifier: [A-Za-z][A-Za-z0-9_]*;

FormSpChar: ('\\n' | '\\\\' | '\\"' | '$$');

SpChar: ('\\n' | '\\\\' | '\\"');

StringConst: Quote (SpChar | .)*? Quote;

NumberConst: '0' | ([1-9][0-9]*);

LogicConst: True | False;