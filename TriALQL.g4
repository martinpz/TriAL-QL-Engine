grammar TriALQL;

@header {
	package parser;
}

parse : block+ EOF;

block : (selectBlock | selectRecursionBlock | selectJoinBlock | operatorBlock | storeBlock |  loadBlock | dropBlock | mergeBlock ) Semikolon;

selectBlock : Identifier Equals Select selection (With selectionProvenanceCreator)? 
From Identifier (Filter filterExpr)?;

selectRecursionBlock : Identifier Equals Select selectionRecursion (With selectionProvenanceAppender)? 
From Identifier On equationList (Filter filterExpr)?
Using recursionType (kleeneDepth)?;

selectJoinBlock : Identifier Equals Select selection (With selectionProvenanceAppender)? 
From Identifier Join Identifier (On equationList)? (Filter filterExpr)?;

operatorBlock : Identifier Equals Identifier (Union | Minus | Intersect)  Identifier (With Provenance)?;

mergeBlock : Identifier Merge Identifier (Merge Identifier)* ;

storeBlock : Store Provenance? Identifier As Identifier;

loadBlock : Load Cache? Identifier;

dropBlock : Drop Provenance? Identifier;

equationList : equation (Comma equation)*;

equation : term equationOperator term;

equationOperator : Equals | NEquals | GTEquals 
| LTEquals | GT | LT;

term : Pos | literal;

literal : Bool 
| Int 
| String;

selection : (Pos | Identifier)  Comma (Pos | Identifier)  Comma (Pos | Identifier) ;

selectionRecursion : Pos Comma Pos Comma Pos;

selectionProvenanceCreator : Pos (Comma Pos)*;

selectionProvenanceAppender : ('r1' Comma 'r2') | ('r2' Comma 'r1');

recursionType : Left | Right;

kleeneDepth
	: OBracket (PositiveInt | Star| Plus) (Comma (PositiveInt | Star))? CBracket;

filterExpr
	: filterExprAnd (Or filterExprAnd)*;

filterExprAnd
	: equation (And equation)*;

Select		: 'SELECT';
From		: 'FROM';
Join		: 'JOIN';
On		: 'ON';
Store		: 'STORE';
Provenance	: 'PROVENANCE';
As		: 'AS';
Using		: 'USING';
Left		: 'left';
Right		: 'right';
And		: 'AND';
Or		: 'OR';
Drop		: 'DROP';
Union		: 'UNION';
Minus       	: 'MINUS';
Intersect 	: 'INTERSECT';
Filter		: 'FILTER';
With            : 'WITH';
Load            : 'LOAD';
Cache          : 'CACHE';
Merge		: 'MERGE';
Star		: '*';
Plus		: '+';

Pos		: ('s' | 'p' | 'o') [1-2]; 

Equals		: '=';
NEquals		: '!=';
GTEquals	: '>=';
LTEquals	: '<=';
GT		: '>';
LT		: '<';
Apostrophe	: '\'';
Quote		: '"';
Semikolon	: ';';
OBracket	: '[';
CBracket	: ']';
Comma		: ',';

Bool : 'true' 
| 'false'; 

Identifier
	: ('a'..'z' | 'A'..'Z')('a'..'z' | 'A'..'Z' | '_' | '.' | Digit)*;

String : ('"' (~('"' | '\\') | '\\' .)* '"' 
| '\'' (~('\'' | '\\') | '\\' .)* '\''); 

PositiveInt : [1-9] Digit*; 

Int : PositiveInt 
| '0'; 

fragment Digit : [0-9];

Comment		: ('--' | '#') ~( '\r' | '\n' )* -> skip;

Whitespace	: [ \t\r\n] -> skip;
