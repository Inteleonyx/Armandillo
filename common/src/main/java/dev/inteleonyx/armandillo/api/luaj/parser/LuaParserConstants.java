package dev.inteleonyx.armandillo.api.luaj.parser;

public interface LuaParserConstants {
   int EOF = 0;
   int COMMENT = 17;
   int LONGCOMMENT0 = 18;
   int LONGCOMMENT1 = 19;
   int LONGCOMMENT2 = 20;
   int LONGCOMMENT3 = 21;
   int LONGCOMMENTN = 22;
   int LONGSTRING0 = 23;
   int LONGSTRING1 = 24;
   int LONGSTRING2 = 25;
   int LONGSTRING3 = 26;
   int LONGSTRINGN = 27;
   int AND = 29;
   int BREAK = 30;
   int DO = 31;
   int ELSE = 32;
   int ELSEIF = 33;
   int END = 34;
   int FALSE = 35;
   int FOR = 36;
   int FUNCTION = 37;
   int GOTO = 38;
   int IF = 39;
   int IN = 40;
   int LOCAL = 41;
   int NIL = 42;
   int NOT = 43;
   int OR = 44;
   int RETURN = 45;
   int REPEAT = 46;
   int THEN = 47;
   int TRUE = 48;
   int UNTIL = 49;
   int WHILE = 50;
   int NAME = 51;
   int NUMBER = 52;
   int FLOAT = 53;
   int FNUM = 54;
   int DIGIT = 55;
   int EXP = 56;
   int HEX = 57;
   int HEXNUM = 58;
   int HEXDIGIT = 59;
   int HEXEXP = 60;
   int STRING = 61;
   int CHARSTRING = 62;
   int QUOTED = 63;
   int DECIMAL = 64;
   int DBCOLON = 65;
   int UNICODE = 66;
   int CHAR = 67;
   int LF = 68;
   int DEFAULT = 0;
   int IN_COMMENT = 1;
   int IN_LC0 = 2;
   int IN_LC1 = 3;
   int IN_LC2 = 4;
   int IN_LC3 = 5;
   int IN_LCN = 6;
   int IN_LS0 = 7;
   int IN_LS1 = 8;
   int IN_LS2 = 9;
   int IN_LS3 = 10;
   int IN_LSN = 11;
   String[] tokenImage = new String[]{
      "<EOF>",
      "\" \"",
      "\"\\t\"",
      "\"\\n\"",
      "\"\\r\"",
      "\"\\f\"",
      "\"--[[\"",
      "\"--[=[\"",
      "\"--[==[\"",
      "\"--[===[\"",
      "<token of kind 10>",
      "\"[[\"",
      "\"[=[\"",
      "\"[==[\"",
      "\"[===[\"",
      "<token of kind 15>",
      "\"--\"",
      "<COMMENT>",
      "\"]]\"",
      "\"]=]\"",
      "\"]==]\"",
      "\"]===]\"",
      "<LONGCOMMENTN>",
      "\"]]\"",
      "\"]=]\"",
      "\"]==]\"",
      "\"]===]\"",
      "<LONGSTRINGN>",
      "<token of kind 28>",
      "\"and\"",
      "\"break\"",
      "\"do\"",
      "\"else\"",
      "\"elseif\"",
      "\"end\"",
      "\"false\"",
      "\"for\"",
      "\"function\"",
      "\"goto\"",
      "\"if\"",
      "\"in\"",
      "\"local\"",
      "\"nil\"",
      "\"not\"",
      "\"or\"",
      "\"return\"",
      "\"repeat\"",
      "\"then\"",
      "\"true\"",
      "\"until\"",
      "\"while\"",
      "<NAME>",
      "<NUMBER>",
      "<FLOAT>",
      "<FNUM>",
      "<DIGIT>",
      "<EXP>",
      "<HEX>",
      "<HEXNUM>",
      "<HEXDIGIT>",
      "<HEXEXP>",
      "<STRING>",
      "<CHARSTRING>",
      "<QUOTED>",
      "<DECIMAL>",
      "\"::\"",
      "<UNICODE>",
      "<CHAR>",
      "<LF>",
      "\"#\"",
      "\";\"",
      "\"=\"",
      "\",\"",
      "\".\"",
      "\":\"",
      "\"(\"",
      "\")\"",
      "\"[\"",
      "\"]\"",
      "\"...\"",
      "\"{\"",
      "\"}\"",
      "\"+\"",
      "\"-\"",
      "\"*\"",
      "\"/\"",
      "\"^\"",
      "\"%\"",
      "\"..\"",
      "\"<\"",
      "\"<=\"",
      "\">\"",
      "\">=\"",
      "\"==\"",
      "\"~=\""
   };
}
