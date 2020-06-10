grammar ICSS;

//--- parser ---
stylesheet: (variableassignment* stylerule*)  EOF;// top level


//stylerule
stylerule: selector OPEN_BRACE (declaration | ifclause | variableassignment)+ CLOSE_BRACE ;
//variable support
variableassignment:CAPITAL_IDENT ASSIGNMENT_OPERATOR (pixelliteral | scalarLiteral | percentageliteral | multiplyoperation | addoperation | subtractoperation) SEMICOLON|
CAPITAL_IDENT ASSIGNMENT_OPERATOR colorLiteral SEMICOLON |
CAPITAL_IDENT ASSIGNMENT_OPERATOR percentageliteral SEMICOLON|
CAPITAL_IDENT ASSIGNMENT_OPERATOR pixelliteral SEMICOLON|
CAPITAL_IDENT ASSIGNMENT_OPERATOR boolliteral SEMICOLON|
CAPITAL_IDENT ASSIGNMENT_OPERATOR variablereference SEMICOLON;

ifclause: 'if' BOX_BRACKET_OPEN expression BOX_BRACKET_CLOSE OPEN_BRACE (declaration | ifclause)+ CLOSE_BRACE ;

expression: boolliteral| variablereference;

variablereference: CAPITAL_IDENT;
// operations
multiplyoperation: scalarLiteral MUL (variablereference | pixelliteral | percentageliteral | addoperation | subtractoperation | multiplyoperation) |
(variablereference | pixelliteral | percentageliteral | addoperation | subtractoperation) MUL scalarLiteral;


addoperation: scalarLiteral PLUS (pixelliteral | percentageliteral | variablereference | multiplyoperation | subtractoperation| addoperation) | pixelliteral PLUS (pixelliteral | scalarLiteral | variablereference | multiplyoperation | subtractoperation | addoperation)|
percentageliteral PLUS (percentageliteral | scalarLiteral |variablereference | multiplyoperation | subtractoperation | addoperation) |
variablereference PLUS (variablereference | scalarLiteral | pixelliteral | percentageliteral | multiplyoperation | subtractoperation | addoperation);



subtractoperation: scalarLiteral PLUS (pixelliteral | percentageliteral | variablereference | multiplyoperation | addoperation | subtractoperation) | pixelliteral PLUS (pixelliteral | scalarLiteral | variablereference | multiplyoperation | addoperation | subtractoperation)|
percentageliteral PLUS (percentageliteral | scalarLiteral |variablereference | multiplyoperation | addoperation | subtractoperation) |
variablereference PLUS (variablereference | scalarLiteral | pixelliteral | percentageliteral | multiplyoperation | addoperation | subtractoperation);


//selectors
classselector: CLASS_IDENT; //now
idselector: ID_IDENT; // now
tagselector: LOWER_IDENT; // now

selector: classselector | idselector | tagselector; // now

//declarations
declaration: 'color' COLON (colorLiteral | variablereference) SEMICOLON |
 'background-color' COLON (colorLiteral | variablereference) SEMICOLON |
  'width' COLON (pixelliteral | percentageliteral | variablereference | multiplyoperation | addoperation) SEMICOLON|
  'height' COLON (pixelliteral | percentageliteral | variablereference | multiplyoperation | addoperation)SEMICOLON;

//properties
//color: 'color' COLON colorLiteral SEMICOLON | 'color' COLON variablereference SEMICOLON; // color property now
//backgroundcolor: 'background-color' COLON colorLiteral SEMICOLON | 'background-color' COLON variablereference SEMICOLON;  // background color property now
//width: 'width' COLON pixelliteral SEMICOLON| 'width' COLON percentageliteral SEMICOLON | 'width' COLON variablereference SEMICOLON; // width now
//height: 'height' COLON pixelliteral SEMICOLON | 'height' COLON percentageliteral SEMICOLON| 'height' COLON variablereference SEMICOLON; // height now





// type literals
boolliteral: TRUE | FALSE;
pixelliteral: PIXELSIZE;
percentageliteral: PERCENTAGE;
colorLiteral: COLOR;
scalarLiteral: SCALAR;


//boole: booleanLiteral; // technically done for literals but needs to be expanded
//booleanLiteral: TRUE | FALSE; // boolean literals
//selector: ID_IDENT | CLASS_IDENT; // not done yet still needs a way to do tags like p or a
//color: 'color' COLON COLOR SEMICOLON; // color property
//backgroundcolor: 'background-color' COLON COLOR SEMICOLON; // background color property
//width: 'width' COLON PIXELSIZE SEMICOLON| 'width' COLON PERCENTAGE SEMICOLON; // width property
//height: 'height' COLON PIXELSIZE SEMICOLON | 'height' COLON PERCENTAGE SEMICOLON; // height property
//variable: variableSetup booleanLiteral SEMICOLON| variableSetup COLOR SEMICOLON |
//variableSetup  CAPITAL_IDENT SEMICOLON | variableSetup PIXELSIZE SEMICOLON | variableSetup PERCENTAGE SEMICOLON;
//// support for: boolean, color, other variable names, pixelsizes, setups
//variableSetup: CAPITAL_IDENT COLON ASSIGNMENT_OPERATOR; // easier than writing the same setup thats the same every time



//ifstatement : IF BOX_BRACKET_OPEN expression BOX_BRACKET_CLOSE OPEN_BRACE statement CLOSE_BRACE ;



//--- LEXER: ---
// IF support:
IF: 'if';
BOX_BRACKET_OPEN: '[';
BOX_BRACKET_CLOSE: ']';


//Literals
TRUE: 'TRUE';
FALSE: 'FALSE';
PIXELSIZE: [0-9]+ 'px';
PERCENTAGE: [0-9]+ '%';
SCALAR: [0-9]+;

//zelf toegevoegde int
//INT: [1-9]+[0-9]* | '0'  ;

//Color value takes precedence over id idents
COLOR: '#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f];

//Specific identifiers for id's and css classes
ID_IDENT: '#' [a-z0-9\-]+;
CLASS_IDENT: '.' [a-z0-9\-]+;

//General identifiers
LOWER_IDENT: [a-z] [a-z0-9\-]*;
CAPITAL_IDENT: [A-Z] [A-Za-z0-9_]*;

//All whitespace is skipped
WS: [ \t\r\n]+ -> skip;

//
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
SEMICOLON: ';';
COLON: ':';
PLUS: '+';
MIN: '-';
MUL: '*';
ASSIGNMENT_OPERATOR: ':=';