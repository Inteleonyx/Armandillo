package dev.inteleonyx.armandillo.api.luaj.compiler;

import dev.inteleonyx.armandillo.api.luaj.*;
import dev.inteleonyx.armandillo.api.luaj.lib.MathLib;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

public class LexState extends Constants {
   protected static final String RESERVED_LOCAL_VAR_FOR_CONTROL = "(for control)";
   protected static final String RESERVED_LOCAL_VAR_FOR_STATE = "(for state)";
   protected static final String RESERVED_LOCAL_VAR_FOR_GENERATOR = "(for generator)";
   protected static final String RESERVED_LOCAL_VAR_FOR_STEP = "(for step)";
   protected static final String RESERVED_LOCAL_VAR_FOR_LIMIT = "(for limit)";
   protected static final String RESERVED_LOCAL_VAR_FOR_INDEX = "(for index)";
   protected static final String[] RESERVED_LOCAL_VAR_KEYWORDS = new String[]{
      "(for control)", "(for generator)", "(for index)", "(for limit)", "(for state)", "(for step)"
   };
   private static final Hashtable RESERVED_LOCAL_VAR_KEYWORDS_TABLE = new Hashtable();
   private static final int EOZ = -1;
   private static final int MAX_INT = 2147483645;
   private static final int UCHAR_MAX = 255;
   private static final int LUAI_MAXCCALLS = 200;
   private static final int LUA_COMPAT_LSTR = 1;
   private static final boolean LUA_COMPAT_VARARG = true;
   static final int NO_JUMP = -1;
   static final int OPR_ADD = 0;
   static final int OPR_SUB = 1;
   static final int OPR_MUL = 2;
   static final int OPR_DIV = 3;
   static final int OPR_MOD = 4;
   static final int OPR_POW = 5;
   static final int OPR_CONCAT = 6;
   static final int OPR_NE = 7;
   static final int OPR_EQ = 8;
   static final int OPR_LT = 9;
   static final int OPR_LE = 10;
   static final int OPR_GT = 11;
   static final int OPR_GE = 12;
   static final int OPR_AND = 13;
   static final int OPR_OR = 14;
   static final int OPR_NOBINOPR = 15;
   static final int OPR_MINUS = 0;
   static final int OPR_NOT = 1;
   static final int OPR_LEN = 2;
   static final int OPR_NOUNOPR = 3;
   static final int VVOID = 0;
   static final int VNIL = 1;
   static final int VTRUE = 2;
   static final int VFALSE = 3;
   static final int VK = 4;
   static final int VKNUM = 5;
   static final int VNONRELOC = 6;
   static final int VLOCAL = 7;
   static final int VUPVAL = 8;
   static final int VINDEXED = 9;
   static final int VJMP = 10;
   static final int VRELOCABLE = 11;
   static final int VCALL = 12;
   static final int VVARARG = 13;
   int current;
   int linenumber;
   int lastline;
   final Token t = new Token();
   final Token lookahead = new Token();
   FuncState fs;
   LuaC.CompileState L;
   InputStream z;
   char[] buff;
   int nbuff;
   Dyndata dyd = new Dyndata();
   LuaString source;
   LuaString envn;
   byte decpoint;
   static final String[] luaX_tokens;
   static final int TK_AND = 257;
   static final int TK_BREAK = 258;
   static final int TK_DO = 259;
   static final int TK_ELSE = 260;
   static final int TK_ELSEIF = 261;
   static final int TK_END = 262;
   static final int TK_FALSE = 263;
   static final int TK_FOR = 264;
   static final int TK_FUNCTION = 265;
   static final int TK_GOTO = 266;
   static final int TK_IF = 267;
   static final int TK_IN = 268;
   static final int TK_LOCAL = 269;
   static final int TK_NIL = 270;
   static final int TK_NOT = 271;
   static final int TK_OR = 272;
   static final int TK_REPEAT = 273;
   static final int TK_RETURN = 274;
   static final int TK_THEN = 275;
   static final int TK_TRUE = 276;
   static final int TK_UNTIL = 277;
   static final int TK_WHILE = 278;
   static final int TK_CONCAT = 279;
   static final int TK_DOTS = 280;
   static final int TK_EQ = 281;
   static final int TK_GE = 282;
   static final int TK_LE = 283;
   static final int TK_NE = 284;
   static final int TK_DBCOLON = 285;
   static final int TK_EOS = 286;
   static final int TK_NUMBER = 287;
   static final int TK_NAME = 288;
   static final int TK_STRING = 289;
   static final int FIRST_RESERVED = 257;
   static final int NUM_RESERVED = 22;
   static final Hashtable RESERVED;
   static Priority[] priority;
   static final int UNARY_PRIORITY = 8;

   private static final String LUA_QS(String var0) {
      return "'" + var0 + "'";
   }

   private static final String LUA_QL(Object var0) {
      return LUA_QS(String.valueOf(var0));
   }

   public static boolean isReservedKeyword(String var0) {
      return RESERVED_LOCAL_VAR_KEYWORDS_TABLE.containsKey(var0);
   }

   private boolean isalnum(int var1) {
      return var1 >= 48 && var1 <= 57 || var1 >= 97 && var1 <= 122 || var1 >= 65 && var1 <= 90 || var1 == 95;
   }

   private boolean isalpha(int var1) {
      return var1 >= 97 && var1 <= 122 || var1 >= 65 && var1 <= 90;
   }

   private boolean isdigit(int var1) {
      return var1 >= 48 && var1 <= 57;
   }

   private boolean isxdigit(int var1) {
      return var1 >= 48 && var1 <= 57 || var1 >= 97 && var1 <= 102 || var1 >= 65 && var1 <= 70;
   }

   private boolean isspace(int var1) {
      return var1 <= 32;
   }

   public LexState(LuaC.CompileState var1, InputStream var2) {
      this.z = var2;
      this.buff = new char[32];
      this.L = var1;
   }

   void nextChar() {
      try {
         this.current = this.z.read();
      } catch (IOException var2) {
         var2.printStackTrace();
         this.current = -1;
      }
   }

   boolean currIsNewline() {
      return this.current == 10 || this.current == 13;
   }

   void save_and_next() {
      this.save(this.current);
      this.nextChar();
   }

   void save(int var1) {
      if (this.buff == null || this.nbuff + 1 > this.buff.length) {
         this.buff = realloc(this.buff, this.nbuff * 2 + 1);
      }

      this.buff[this.nbuff++] = (char)var1;
   }

   String token2str(int var1) {
      if (var1 < 257) {
         return iscntrl(var1) ? this.L.pushfstring("char(" + var1 + ")") : this.L.pushfstring(String.valueOf((char)var1));
      } else {
         return luaX_tokens[var1 - 257];
      }
   }

   private static boolean iscntrl(int var0) {
      return var0 < 32;
   }

   String txtToken(int var1) {
      switch (var1) {
         case 287:
         case 288:
         case 289:
            return new String(this.buff, 0, this.nbuff);
         default:
            return this.token2str(var1);
      }
   }

   void lexerror(String var1, int var2) {
      String var3 = Lua.chunkid(this.source.tojstring());
      this.L.pushfstring(var3 + ":" + this.linenumber + ": " + var1);
      if (var2 != 0) {
         this.L.pushfstring("syntax error: " + var1 + " near " + this.txtToken(var2));
      }

      throw new LuaError(var3 + ":" + this.linenumber + ": " + var1);
   }

   void syntaxerror(String var1) {
      this.lexerror(var1, this.t.token);
   }

   LuaString newstring(String var1) {
      return this.L.newTString(var1);
   }

   LuaString newstring(char[] var1, int var2, int var3) {
      return this.L.newTString(new String(var1, var2, var3));
   }

   void inclinenumber() {
      int var1 = this.current;
      _assert(this.currIsNewline());
      this.nextChar();
      if (this.currIsNewline() && this.current != var1) {
         this.nextChar();
      }

      if (++this.linenumber >= 2147483645) {
         this.syntaxerror("chunk has too many lines");
      }
   }

   void setinput(LuaC.CompileState var1, int var2, InputStream var3, LuaString var4) {
      this.decpoint = 46;
      this.L = var1;
      this.lookahead.token = 286;
      this.z = var3;
      this.fs = null;
      this.linenumber = 1;
      this.lastline = 1;
      this.source = var4;
      this.envn = LuaValue.ENV;
      this.nbuff = 0;
      this.current = var2;
      this.skipShebang();
   }

   private void skipShebang() {
      if (this.current == 35) {
         while (!this.currIsNewline() && this.current != -1) {
            this.nextChar();
         }
      }
   }

   boolean check_next(String var1) {
      if (var1.indexOf(this.current) < 0) {
         return false;
      } else {
         this.save_and_next();
         return true;
      }
   }

   void buffreplace(char var1, char var2) {
      int var3 = this.nbuff;
      char[] var4 = this.buff;

      while (--var3 >= 0) {
         if (var4[var3] == var1) {
            var4[var3] = var2;
         }
      }
   }

   LuaValue strx2number(String var1, SemInfo var2) {
      char[] var3 = var1.toCharArray();
      int var4 = 0;

      while (var4 < var3.length && this.isspace(var3[var4])) {
         var4++;
      }

      double var5 = 1.0;
      if (var4 < var3.length && var3[var4] == '-') {
         var5 = -1.0;
         var4++;
      }

      if (var4 + 2 >= var3.length) {
         return LuaValue.ZERO;
      } else if (var3[var4++] != '0') {
         return LuaValue.ZERO;
      } else if (var3[var4] != 'x' && var3[var4] != 'X') {
         return LuaValue.ZERO;
      } else {
         var4++;
         double var7 = 0.0;
         int var9 = 0;

         while (var4 < var3.length && this.isxdigit(var3[var4])) {
            var7 = var7 * 16.0 + this.hexvalue(var3[var4++]);
         }

         if (var4 < var3.length && var3[var4] == '.') {
            var4++;

            while (var4 < var3.length && this.isxdigit(var3[var4])) {
               var7 = var7 * 16.0 + this.hexvalue(var3[var4++]);
               var9 -= 4;
            }
         }

         if (var4 < var3.length && (var3[var4] == 'p' || var3[var4] == 'P')) {
            var4++;
            int var10 = 0;
            boolean var11 = false;
            if (var4 < var3.length && var3[var4] == '-') {
               var11 = true;
               var4++;
            }

            while (var4 < var3.length && this.isdigit(var3[var4])) {
               var10 = var10 * 10 + var3[var4++] - 48;
            }

            if (var11) {
               var10 = -var10;
            }

            var9 += var10;
         }

         return LuaValue.valueOf(var5 * var7 * MathLib.dpow_d(2.0, var9));
      }
   }

   boolean str2d(String var1, SemInfo var2) {
      if (var1.indexOf(110) >= 0 || var1.indexOf(78) >= 0) {
         var2.r = LuaValue.ZERO;
      } else if (var1.indexOf(120) < 0 && var1.indexOf(88) < 0) {
         var2.r = LuaValue.valueOf(Double.parseDouble(var1.trim()));
      } else {
         var2.r = this.strx2number(var1, var2);
      }

      return true;
   }

   void read_numeral(SemInfo var1) {
      String var2 = "Ee";
      int var3 = this.current;
      _assert(this.isdigit(this.current));
      this.save_and_next();
      if (var3 == 48 && this.check_next("Xx")) {
         var2 = "Pp";
      }

      while (true) {
         if (this.check_next(var2)) {
            this.check_next("+-");
         }

         if (!this.isxdigit(this.current) && this.current != 46) {
            this.save(0);
            String var4 = new String(this.buff, 0, this.nbuff);
            this.str2d(var4, var1);
            return;
         }

         this.save_and_next();
      }
   }

   int skip_sep() {
      int var1 = 0;
      int var2 = this.current;
      _assert(var2 == 91 || var2 == 93);
      this.save_and_next();

      while (this.current == 61) {
         this.save_and_next();
         var1++;
      }

      return this.current == var2 ? var1 : -var1 - 1;
   }

   void read_long_string(SemInfo var1, int var2) {
      int var3 = 0;
      this.save_and_next();
      if (this.currIsNewline()) {
         this.inclinenumber();
      }

      boolean var4 = false;

      while (!var4) {
         switch (this.current) {
            case -1:
               this.lexerror(var1 != null ? "unfinished long string" : "unfinished long comment", 286);
               break;
            case 10:
            case 13:
               this.save(10);
               this.inclinenumber();
               if (var1 == null) {
                  this.nbuff = 0;
               }
               break;
            case 91:
               if (this.skip_sep() == var2) {
                  this.save_and_next();
                  var3++;
                  if (var2 == 0) {
                     this.lexerror("nesting of [[...]] is deprecated", 91);
                  }
               }
               break;
            case 93:
               if (this.skip_sep() == var2) {
                  this.save_and_next();
                  var4 = true;
               }
               break;
            default:
               if (var1 != null) {
                  this.save_and_next();
               } else {
                  this.nextChar();
               }
         }
      }

      if (var1 != null) {
         var1.ts = this.L.newTString(LuaString.valueOf(this.buff, 2 + var2, this.nbuff - 2 * (2 + var2)));
      }
   }

   int hexvalue(int var1) {
      return var1 <= 57 ? var1 - 48 : (var1 <= 70 ? var1 + 10 - 65 : var1 + 10 - 97);
   }

   int readhexaesc() {
      this.nextChar();
      int var1 = this.current;
      this.nextChar();
      int var2 = this.current;
      if (!this.isxdigit(var1) || !this.isxdigit(var2)) {
         this.lexerror("hexadecimal digit expected 'x" + (char)var1 + (char)var2, 289);
      }

      return (this.hexvalue(var1) << 4) + this.hexvalue(var2);
   }

   void read_string(int var1, SemInfo var2) {
      this.save_and_next();

      while (this.current != var1) {
         switch (this.current) {
            case -1:
               this.lexerror("unfinished string", 286);
               break;
            case 10:
            case 13:
               this.lexerror("unfinished string", 289);
               break;
            case 92:
               this.nextChar();
               int var5;
               switch (this.current) {
                  case -1:
                     continue;
                  case 10:
                  case 13:
                     this.save(10);
                     this.inclinenumber();
                     continue;
                  case 97:
                     var5 = 7;
                     break;
                  case 98:
                     var5 = 8;
                     break;
                  case 102:
                     var5 = 12;
                     break;
                  case 110:
                     var5 = 10;
                     break;
                  case 114:
                     var5 = 13;
                     break;
                  case 116:
                     var5 = 9;
                     break;
                  case 118:
                     var5 = 11;
                     break;
                  case 120:
                     var5 = this.readhexaesc();
                     break;
                  case 122:
                     this.nextChar();

                     while (this.isspace(this.current)) {
                        if (this.currIsNewline()) {
                           this.inclinenumber();
                        } else {
                           this.nextChar();
                        }
                     }
                     continue;
                  default:
                     if (!this.isdigit(this.current)) {
                        this.save_and_next();
                        continue;
                     }

                     int var4 = 0;
                     var5 = 0;

                     do {
                        var5 = 10 * var5 + (this.current - 48);
                        this.nextChar();
                     } while (++var4 < 3 && this.isdigit(this.current));

                     if (var5 > 255) {
                        this.lexerror("escape sequence too large", 289);
                     }

                     this.save(var5);
                     continue;
               }

               this.save(var5);
               this.nextChar();
               break;
            default:
               this.save_and_next();
         }
      }

      this.save_and_next();
      var2.ts = this.L.newTString(LuaString.valueOf(this.buff, 1, this.nbuff - 2));
   }

   int llex(SemInfo var1) {
      this.nbuff = 0;

      while (true) {
         switch (this.current) {
            case -1:
               return 286;
            case 10:
            case 13:
               this.inclinenumber();
               break;
            case 34:
            case 39:
               this.read_string(this.current, var1);
               return 289;
            case 45:
               this.nextChar();
               if (this.current != 45) {
                  return 45;
               }

               this.nextChar();
               if (this.current == 91) {
                  int var5 = this.skip_sep();
                  this.nbuff = 0;
                  if (var5 >= 0) {
                     this.read_long_string(null, var5);
                     this.nbuff = 0;
                     break;
                  }
               }

               while (!this.currIsNewline() && this.current != -1) {
                  this.nextChar();
               }
               break;
            case 46:
               this.save_and_next();
               if (this.check_next(".")) {
                  if (this.check_next(".")) {
                     return 280;
                  }

                  return 279;
               }

               if (!this.isdigit(this.current)) {
                  return 46;
               }

               this.read_numeral(var1);
               return 287;
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
               this.read_numeral(var1);
               return 287;
            case 58:
               this.nextChar();
               if (this.current != 58) {
                  return 58;
               }

               this.nextChar();
               return 285;
            case 60:
               this.nextChar();
               if (this.current != 61) {
                  return 60;
               }

               this.nextChar();
               return 283;
            case 62:
               this.nextChar();
               if (this.current != 61) {
                  return 62;
               }

               this.nextChar();
               return 282;
            case 91:
               int var4 = this.skip_sep();
               if (var4 >= 0) {
                  this.read_long_string(var1, var4);
                  return 289;
               }

               if (var4 == -1) {
                  return 91;
               }

               this.lexerror("invalid long string delimiter", 289);
            case 61:
               this.nextChar();
               if (this.current != 61) {
                  return 61;
               }

               this.nextChar();
               return 281;
            case 126:
               this.nextChar();
               if (this.current != 61) {
                  return 126;
               }

               this.nextChar();
               return 284;
            default:
               if (!this.isspace(this.current)) {
                  if (this.isdigit(this.current)) {
                     this.read_numeral(var1);
                     return 287;
                  }

                  if (!this.isalpha(this.current) && this.current != 95) {
                     int var3 = this.current;
                     this.nextChar();
                     return var3;
                  }

                  do {
                     this.save_and_next();
                  } while (this.isalnum(this.current) || this.current == 95);

                  LuaString var2 = this.newstring(this.buff, 0, this.nbuff);
                  if (RESERVED.containsKey(var2)) {
                     return (Integer)RESERVED.get(var2);
                  }

                  var1.ts = var2;
                  return 288;
               }

               _assert(!this.currIsNewline());
               this.nextChar();
         }
      }
   }

   void next() {
      this.lastline = this.linenumber;
      if (this.lookahead.token != 286) {
         this.t.set(this.lookahead);
         this.lookahead.token = 286;
      } else {
         this.t.token = this.llex(this.t.seminfo);
      }
   }

   void lookahead() {
      _assert(this.lookahead.token == 286);
      this.lookahead.token = this.llex(this.lookahead.seminfo);
   }

   static final boolean vkisvar(int var0) {
      return 7 <= var0 && var0 <= 9;
   }

   static final boolean vkisinreg(int var0) {
      return var0 == 6 || var0 == 7;
   }

   boolean hasmultret(int var1) {
      return var1 == 12 || var1 == 13;
   }

   void anchor_token() {
      _assert(this.fs != null || this.t.token == 286);
      if (this.t.token == 288 || this.t.token == 289) {
         LuaString var1 = this.t.seminfo.ts;
         this.L.cachedLuaString(this.t.seminfo.ts);
      }
   }

   void semerror(String var1) {
      this.t.token = 0;
      this.syntaxerror(var1);
   }

   void error_expected(int var1) {
      this.syntaxerror(this.L.pushfstring(LUA_QS(this.token2str(var1)) + " expected"));
   }

   boolean testnext(int var1) {
      if (this.t.token == var1) {
         this.next();
         return true;
      } else {
         return false;
      }
   }

   void check(int var1) {
      if (this.t.token != var1) {
         this.error_expected(var1);
      }
   }

   void checknext(int var1) {
      this.check(var1);
      this.next();
   }

   void check_condition(boolean var1, String var2) {
      if (!var1) {
         this.syntaxerror(var2);
      }
   }

   void check_match(int var1, int var2, int var3) {
      if (!this.testnext(var1)) {
         if (var3 == this.linenumber) {
            this.error_expected(var1);
         } else {
            this.syntaxerror(
               this.L.pushfstring(LUA_QS(this.token2str(var1)) + " expected (to close " + LUA_QS(this.token2str(var2)) + " at line " + var3 + ")")
            );
         }
      }
   }

   LuaString str_checkname() {
      this.check(288);
      LuaString var1 = this.t.seminfo.ts;
      this.next();
      return var1;
   }

   void codestring(expdesc var1, LuaString var2) {
      var1.init(4, this.fs.stringK(var2));
   }

   void checkname(expdesc var1) {
      this.codestring(var1, this.str_checkname());
   }

   int registerlocalvar(LuaString var1) {
      FuncState var2 = this.fs;
      Prototype var3 = var2.f;
      if (var3.locvars == null || var2.nlocvars + 1 > var3.locvars.length) {
         var3.locvars = realloc(var3.locvars, var2.nlocvars * 2 + 1);
      }

      var3.locvars[var2.nlocvars] = new LocVars(var1, 0, 0);
      return var2.nlocvars++;
   }

   void new_localvar(LuaString var1) {
      int var2 = this.registerlocalvar(var1);
      this.fs.checklimit(this.dyd.n_actvar + 1, 200, "local variables");
      if (this.dyd.actvar == null || this.dyd.n_actvar + 1 > this.dyd.actvar.length) {
         this.dyd.actvar = realloc(this.dyd.actvar, Math.max(1, this.dyd.n_actvar * 2));
      }

      this.dyd.actvar[this.dyd.n_actvar++] = new Vardesc(var2);
   }

   void new_localvarliteral(String var1) {
      LuaString var2 = this.newstring(var1);
      this.new_localvar(var2);
   }

   void adjustlocalvars(int var1) {
      FuncState var2 = this.fs;

      for (var2.nactvar = (short)(var2.nactvar + var1); var1 > 0; var1--) {
         var2.getlocvar(var2.nactvar - var1).startpc = var2.pc;
      }
   }

   void removevars(int var1) {
      FuncState var2 = this.fs;

      while (var2.nactvar > var1) {
         var2.getlocvar(--var2.nactvar).endpc = var2.pc;
      }
   }

   void singlevar(expdesc var1) {
      LuaString var2 = this.str_checkname();
      FuncState var3 = this.fs;
      if (FuncState.singlevaraux(var3, var2, var1, 1) == 0) {
         expdesc var4 = new expdesc();
         FuncState.singlevaraux(var3, this.envn, var1, 1);
         _assert(var1.k == 7 || var1.k == 8);
         this.codestring(var4, var2);
         var3.indexed(var1, var4);
      }
   }

   void adjust_assign(int var1, int var2, expdesc var3) {
      FuncState var4 = this.fs;
      int var5 = var1 - var2;
      if (this.hasmultret(var3.k)) {
         if (++var5 < 0) {
            var5 = 0;
         }

         var4.setreturns(var3, var5);
         if (var5 > 1) {
            var4.reserveregs(var5 - 1);
         }
      } else {
         if (var3.k != 0) {
            var4.exp2nextreg(var3);
         }

         if (var5 > 0) {
            short var6 = var4.freereg;
            var4.reserveregs(var5);
            var4.nil(var6, var5);
         }
      }
   }

   void enterlevel() {
      if (++this.L.nCcalls > 200) {
         this.lexerror("chunk has too many syntax levels", 0);
      }
   }

   void leavelevel() {
      this.L.nCcalls--;
   }

   void closegoto(int var1, Labeldesc var2) {
      FuncState var3 = this.fs;
      Labeldesc[] var4 = this.dyd.gt;
      Labeldesc var5 = var4[var1];
      _assert(var5.name.eq_b(var2.name));
      if (var5.nactvar < var2.nactvar) {
         LuaString var6 = var3.getlocvar(var5.nactvar).varname;
         String var7 = this.L.pushfstring("<goto " + var5.name + "> at line " + var5.line + " jumps into the scope of local '" + var6.tojstring() + "'");
         this.semerror(var7);
      }

      var3.patchlist(var5.pc, var2.pc);
      System.arraycopy(var4, var1 + 1, var4, var1, this.dyd.n_gt - var1 - 1);
      var4[--this.dyd.n_gt] = null;
   }

   boolean findlabel(int var1) {
      FuncState.BlockCnt var3 = this.fs.bl;
      Dyndata var4 = this.dyd;
      Labeldesc var5 = var4.gt[var1];

      for (int var2 = var3.firstlabel; var2 < var4.n_label; var2++) {
         Labeldesc var6 = var4.label[var2];
         if (var6.name.eq_b(var5.name)) {
            if (var5.nactvar > var6.nactvar && (var3.upval || var4.n_label > var3.firstlabel)) {
               this.fs.patchclose(var5.pc, var6.nactvar);
            }

            this.closegoto(var1, var6);
            return true;
         }
      }

      return false;
   }

   int newlabelentry(Labeldesc[] var1, int var2, LuaString var3, int var4, int var5) {
      var1[var2] = new Labeldesc(var3, var5, var4, this.fs.nactvar);
      return var2;
   }

   void findgotos(Labeldesc var1) {
      Labeldesc[] var2 = this.dyd.gt;
      int var3 = this.fs.bl.firstgoto;

      while (var3 < this.dyd.n_gt) {
         if (var2[var3].name.eq_b(var1.name)) {
            this.closegoto(var3, var1);
         } else {
            var3++;
         }
      }
   }

   void breaklabel() {
      LuaString var1 = LuaString.valueOf("break");
      int var2 = this.newlabelentry(this.dyd.label = grow(this.dyd.label, this.dyd.n_label + 1), this.dyd.n_label++, var1, 0, this.fs.pc);
      this.findgotos(this.dyd.label[var2]);
   }

   void undefgoto(Labeldesc var1) {
      String var2 = this.L
         .pushfstring(
            isReservedKeyword(var1.name.tojstring())
               ? "<" + var1.name + "> at line " + var1.line + " not inside a loop"
               : "no visible label '" + var1.name + "' for <goto> at line " + var1.line
         );
      this.semerror(var2);
   }

   Prototype addprototype() {
      Prototype var2 = this.fs.f;
      if (var2.p == null || this.fs.np >= var2.p.length) {
         var2.p = realloc(var2.p, Math.max(1, this.fs.np * 2));
      }

      Prototype var1;
      var2.p[this.fs.np++] = var1 = new Prototype();
      return var1;
   }

   void codeclosure(expdesc var1) {
      FuncState var2 = this.fs.prev;
      var1.init(11, var2.codeABx(37, 0, var2.np - 1));
      var2.exp2nextreg(var1);
   }

   void open_func(FuncState var1, FuncState.BlockCnt var2) {
      var1.prev = this.fs;
      var1.ls = this;
      this.fs = var1;
      var1.pc = 0;
      var1.lasttarget = -1;
      var1.jpc = new IntPtr(-1);
      var1.freereg = 0;
      var1.nk = 0;
      var1.np = 0;
      var1.nups = 0;
      var1.nlocvars = 0;
      var1.nactvar = 0;
      var1.firstlocal = this.dyd.n_actvar;
      var1.bl = null;
      var1.f.source = this.source;
      var1.f.maxstacksize = 2;
      var1.enterblock(var2, false);
   }

   void close_func() {
      FuncState var1 = this.fs;
      Prototype var2 = var1.f;
      var1.ret(0, 0);
      var1.leaveblock();
      var2.code = realloc(var2.code, var1.pc);
      var2.lineinfo = realloc(var2.lineinfo, var1.pc);
      var2.k = realloc(var2.k, var1.nk);
      var2.p = realloc(var2.p, var1.np);
      var2.locvars = realloc(var2.locvars, var1.nlocvars);
      var2.upvalues = realloc(var2.upvalues, var1.nups);
      _assert(var1.bl == null);
      this.fs = var1.prev;
   }

   void fieldsel(expdesc var1) {
      FuncState var2 = this.fs;
      expdesc var3 = new expdesc();
      var2.exp2anyregup(var1);
      this.next();
      this.checkname(var3);
      var2.indexed(var1, var3);
   }

   void yindex(expdesc var1) {
      this.next();
      this.expr(var1);
      this.fs.exp2val(var1);
      this.checknext(93);
   }

   void recfield(ConsControl var1) {
      FuncState var2 = this.fs;
      short var3 = this.fs.freereg;
      expdesc var4 = new expdesc();
      expdesc var5 = new expdesc();
      if (this.t.token == 288) {
         var2.checklimit(var1.nh, 2147483645, "items in a constructor");
         this.checkname(var4);
      } else {
         this.yindex(var4);
      }

      var1.nh++;
      this.checknext(61);
      int var6 = var2.exp2RK(var4);
      this.expr(var5);
      var2.codeABC(10, var1.t.u.info, var6, var2.exp2RK(var5));
      var2.freereg = (short)var3;
   }

   void listfield(ConsControl var1) {
      this.expr(var1.v);
      this.fs.checklimit(var1.na, 2147483645, "items in a constructor");
      var1.na++;
      var1.tostore++;
   }

   void constructor(expdesc var1) {
      FuncState var2 = this.fs;
      int var3 = this.linenumber;
      int var4 = var2.codeABC(11, 0, 0, 0);
      ConsControl var5 = new ConsControl();
      var5.na = var5.nh = var5.tostore = 0;
      var5.t = var1;
      var1.init(11, var4);
      var5.v.init(0, 0);
      var2.exp2nextreg(var1);
      this.checknext(123);

      do {
         _assert(var5.v.k == 0 || var5.tostore > 0);
         if (this.t.token == 125) {
            break;
         }

         var2.closelistfield(var5);
         switch (this.t.token) {
            case 91:
               this.recfield(var5);
               break;
            case 288:
               this.lookahead();
               if (this.lookahead.token != 61) {
                  this.listfield(var5);
               } else {
                  this.recfield(var5);
               }
               break;
            default:
               this.listfield(var5);
         }
      } while (this.testnext(44) || this.testnext(59));

      this.check_match(125, 123, var3);
      var2.lastlistfield(var5);
      InstructionPtr var6 = new InstructionPtr(var2.f.code, var4);
      SETARG_B(var6, luaO_int2fb(var5.na));
      SETARG_C(var6, luaO_int2fb(var5.nh));
   }

   static int luaO_int2fb(int var0) {
      int var1;
      for (var1 = 0; var0 >= 16; var1++) {
         var0 = var0 + 1 >> 1;
      }

      return var0 < 8 ? var0 : var1 + 1 << 3 | var0 - 8;
   }

   void parlist() {
      FuncState var1 = this.fs;
      Prototype var2 = var1.f;
      int var3 = 0;
      var2.is_vararg = 0;
      if (this.t.token != 41) {
         do {
            switch (this.t.token) {
               case 280:
                  this.next();
                  var2.is_vararg = 1;
                  break;
               case 288:
                  this.new_localvar(this.str_checkname());
                  var3++;
                  break;
               default:
                  this.syntaxerror("<name> or " + LUA_QL("...") + " expected");
            }
         } while (var2.is_vararg == 0 && this.testnext(44));
      }

      this.adjustlocalvars(var3);
      var2.numparams = var1.nactvar;
      var1.reserveregs(var1.nactvar);
   }

   void body(expdesc var1, boolean var2, int var3) {
      FuncState var4 = new FuncState();
      FuncState.BlockCnt var5 = new FuncState.BlockCnt();
      var4.f = this.addprototype();
      var4.f.linedefined = var3;
      this.open_func(var4, var5);
      this.checknext(40);
      if (var2) {
         this.new_localvarliteral("self");
         this.adjustlocalvars(1);
      }

      this.parlist();
      this.checknext(41);
      this.statlist();
      var4.f.lastlinedefined = this.linenumber;
      this.check_match(262, 265, var3);
      this.codeclosure(var1);
      this.close_func();
   }

   int explist(expdesc var1) {
      int var2 = 1;
      this.expr(var1);

      while (this.testnext(44)) {
         this.fs.exp2nextreg(var1);
         this.expr(var1);
         var2++;
      }

      return var2;
   }

   void funcargs(expdesc var1, int var2) {
      FuncState var3 = this.fs;
      expdesc var4 = new expdesc();
      switch (this.t.token) {
         case 40:
            this.next();
            if (this.t.token == 41) {
               var4.k = 0;
            } else {
               this.explist(var4);
               var3.setmultret(var4);
            }

            this.check_match(41, 40, var2);
            break;
         case 123:
            this.constructor(var4);
            break;
         case 289:
            this.codestring(var4, this.t.seminfo.ts);
            this.next();
            break;
         default:
            this.syntaxerror("function arguments expected");
            return;
      }

      _assert(var1.k == 6);
      int var5 = var1.u.info;
      int var6;
      if (this.hasmultret(var4.k)) {
         var6 = -1;
      } else {
         if (var4.k != 0) {
            var3.exp2nextreg(var4);
         }

         var6 = var3.freereg - (var5 + 1);
      }

      var1.init(12, var3.codeABC(29, var5, var6 + 1, 2));
      var3.fixline(var2);
      var3.freereg = (short)(var5 + 1);
   }

   void primaryexp(expdesc var1) {
      switch (this.t.token) {
         case 40:
            int var2 = this.linenumber;
            this.next();
            this.expr(var1);
            this.check_match(41, 40, var2);
            this.fs.dischargevars(var1);
            return;
         case 288:
            this.singlevar(var1);
            return;
         default:
            this.syntaxerror("unexpected symbol " + this.t.token + " (" + (char)this.t.token + ")");
      }
   }

   void suffixedexp(expdesc var1) {
      int var2 = this.linenumber;
      this.primaryexp(var1);

      while (true) {
         switch (this.t.token) {
            case 40:
            case 123:
            case 289:
               this.fs.exp2nextreg(var1);
               this.funcargs(var1, var2);
               break;
            case 46:
               this.fieldsel(var1);
               break;
            case 58:
               expdesc var4 = new expdesc();
               this.next();
               this.checkname(var4);
               this.fs.self(var1, var4);
               this.funcargs(var1, var2);
               break;
            case 91:
               expdesc var3 = new expdesc();
               this.fs.exp2anyregup(var1);
               this.yindex(var3);
               this.fs.indexed(var1, var3);
               break;
            default:
               return;
         }
      }
   }

   void simpleexp(expdesc var1) {
      switch (this.t.token) {
         case 123:
            this.constructor(var1);
            return;
         case 263:
            var1.init(3, 0);
            break;
         case 265:
            this.next();
            this.body(var1, false, this.linenumber);
            return;
         case 270:
            var1.init(1, 0);
            break;
         case 276:
            var1.init(2, 0);
            break;
         case 280:
            FuncState var2 = this.fs;
            this.check_condition(var2.f.is_vararg != 0, "cannot use " + LUA_QL("...") + " outside a vararg function");
            var1.init(13, var2.codeABC(38, 0, 1, 0));
            break;
         case 287:
            var1.init(5, 0);
            var1.u.setNval(this.t.seminfo.r);
            break;
         case 289:
            this.codestring(var1, this.t.seminfo.ts);
            break;
         default:
            this.suffixedexp(var1);
            return;
      }

      this.next();
   }

   int getunopr(int var1) {
      switch (var1) {
         case 35:
            return 2;
         case 45:
            return 0;
         case 271:
            return 1;
         default:
            return 3;
      }
   }

   int getbinopr(int var1) {
      switch (var1) {
         case 37:
            return 4;
         case 42:
            return 2;
         case 43:
            return 0;
         case 45:
            return 1;
         case 47:
            return 3;
         case 60:
            return 9;
         case 62:
            return 11;
         case 94:
            return 5;
         case 257:
            return 13;
         case 272:
            return 14;
         case 279:
            return 6;
         case 281:
            return 8;
         case 282:
            return 12;
         case 283:
            return 10;
         case 284:
            return 7;
         default:
            return 15;
      }
   }

   int subexpr(expdesc var1, int var2) {
      this.enterlevel();
      int var4 = this.getunopr(this.t.token);
      if (var4 != 3) {
         int var5 = this.linenumber;
         this.next();
         this.subexpr(var1, 8);
         this.fs.prefix(var4, var1, var5);
      } else {
         this.simpleexp(var1);
      }

      int var3 = this.getbinopr(this.t.token);

      while (var3 != 15 && priority[var3].left > var2) {
         expdesc var8 = new expdesc();
         int var6 = this.linenumber;
         this.next();
         this.fs.infix(var3, var1);
         int var7 = this.subexpr(var8, priority[var3].right);
         this.fs.posfix(var3, var1, var8, var6);
         var3 = var7;
      }

      this.leavelevel();
      return var3;
   }

   void expr(expdesc var1) {
      this.subexpr(var1, 0);
   }

   boolean block_follow(boolean var1) {
      switch (this.t.token) {
         case 260:
         case 261:
         case 262:
         case 286:
            return true;
         case 277:
            return var1;
         default:
            return false;
      }
   }

   void block() {
      FuncState var1 = this.fs;
      FuncState.BlockCnt var2 = new FuncState.BlockCnt();
      var1.enterblock(var2, false);
      this.statlist();
      var1.leaveblock();
   }

   void check_conflict(LHS_assign var1, expdesc var2) {
      FuncState var3 = this.fs;
      short var4 = var3.freereg;
      boolean var5 = false;

      while (var1 != null) {
         if (var1.v.k == 9) {
            if (var1.v.u.ind_vt == var2.k && var1.v.u.ind_t == var2.u.info) {
               var5 = true;
               var1.v.u.ind_vt = 7;
               var1.v.u.ind_t = var4;
            }

            if (var2.k == 7 && var1.v.u.ind_idx == var2.u.info) {
               var5 = true;
               var1.v.u.ind_idx = var4;
            }
         }

         var1 = var1.prev;
      }

      if (var5) {
         int var6 = var2.k == 7 ? 0 : 5;
         var3.codeABC(var6, var4, var2.u.info, 0);
         var3.reserveregs(1);
      }
   }

   void assignment(LHS_assign var1, int var2) {
      expdesc var3 = new expdesc();
      this.check_condition(7 <= var1.v.k && var1.v.k <= 9, "syntax error");
      if (this.testnext(44)) {
         LHS_assign var4 = new LHS_assign();
         var4.prev = var1;
         this.suffixedexp(var4.v);
         if (var4.v.k != 9) {
            this.check_conflict(var1, var4.v);
         }

         this.assignment(var4, var2 + 1);
      } else {
         this.checknext(61);
         int var5 = this.explist(var3);
         if (var5 == var2) {
            this.fs.setoneret(var3);
            this.fs.storevar(var1.v, var3);
            return;
         }

         this.adjust_assign(var2, var5, var3);
         if (var5 > var2) {
            this.fs.freereg = (short)(this.fs.freereg - (var5 - var2));
         }
      }

      var3.init(6, this.fs.freereg - 1);
      this.fs.storevar(var1.v, var3);
   }

   int cond() {
      expdesc var1 = new expdesc();
      this.expr(var1);
      if (var1.k == 1) {
         var1.k = 3;
      }

      this.fs.goiftrue(var1);
      return var1.f.i;
   }

   void gotostat(int var1) {
      int var2 = this.linenumber;
      LuaString var3;
      if (this.testnext(266)) {
         var3 = this.str_checkname();
      } else {
         this.next();
         var3 = LuaString.valueOf("break");
      }

      int var4 = this.newlabelentry(this.dyd.gt = grow(this.dyd.gt, this.dyd.n_gt + 1), this.dyd.n_gt++, var3, var2, var1);
      this.findlabel(var4);
   }

   void skipnoopstat() {
      while (this.t.token == 59 || this.t.token == 285) {
         this.statement();
      }
   }

   void labelstat(LuaString var1, int var2) {
      this.fs.checkrepeated(this.dyd.label, this.dyd.n_label, var1);
      this.checknext(285);
      int var3 = this.newlabelentry(this.dyd.label = grow(this.dyd.label, this.dyd.n_label + 1), this.dyd.n_label++, var1, var2, this.fs.pc);
      this.skipnoopstat();
      if (this.block_follow(false)) {
         this.dyd.label[var3].nactvar = this.fs.bl.nactvar;
      }

      this.findgotos(this.dyd.label[var3]);
   }

   void whilestat(int var1) {
      FuncState var2 = this.fs;
      FuncState.BlockCnt var5 = new FuncState.BlockCnt();
      this.next();
      int var3 = var2.getlabel();
      int var4 = this.cond();
      var2.enterblock(var5, true);
      this.checknext(259);
      this.block();
      var2.patchlist(var2.jump(), var3);
      this.check_match(262, 278, var1);
      var2.leaveblock();
      var2.patchtohere(var4);
   }

   void repeatstat(int var1) {
      FuncState var3 = this.fs;
      int var4 = var3.getlabel();
      FuncState.BlockCnt var5 = new FuncState.BlockCnt();
      FuncState.BlockCnt var6 = new FuncState.BlockCnt();
      var3.enterblock(var5, true);
      var3.enterblock(var6, false);
      this.next();
      this.statlist();
      this.check_match(277, 273, var1);
      int var2 = this.cond();
      if (var6.upval) {
         var3.patchclose(var2, var6.nactvar);
      }

      var3.leaveblock();
      var3.patchlist(var2, var4);
      var3.leaveblock();
   }

   int exp1() {
      expdesc var1 = new expdesc();
      this.expr(var1);
      int var2 = var1.k;
      this.fs.exp2nextreg(var1);
      return var2;
   }

   void forbody(int var1, int var2, int var3, boolean var4) {
      FuncState.BlockCnt var5 = new FuncState.BlockCnt();
      FuncState var6 = this.fs;
      this.adjustlocalvars(3);
      this.checknext(259);
      int var7 = var4 ? var6.codeAsBx(33, var1, -1) : var6.jump();
      var6.enterblock(var5, false);
      this.adjustlocalvars(var3);
      var6.reserveregs(var3);
      this.block();
      var6.leaveblock();
      var6.patchtohere(var7);
      int var8;
      if (var4) {
         var8 = var6.codeAsBx(32, var1, -1);
      } else {
         var6.codeABC(34, var1, 0, var3);
         var6.fixline(var2);
         var8 = var6.codeAsBx(35, var1 + 2, -1);
      }

      var6.patchlist(var8, var7 + 1);
      var6.fixline(var2);
   }

   void fornum(LuaString var1, int var2) {
      FuncState var3 = this.fs;
      short var4 = var3.freereg;
      this.new_localvarliteral("(for index)");
      this.new_localvarliteral("(for limit)");
      this.new_localvarliteral("(for step)");
      this.new_localvar(var1);
      this.checknext(61);
      this.exp1();
      this.checknext(44);
      this.exp1();
      if (this.testnext(44)) {
         this.exp1();
      } else {
         var3.codeABx(1, var3.freereg, var3.numberK(LuaInteger.valueOf(1)));
         var3.reserveregs(1);
      }

      this.forbody(var4, var2, 1, true);
   }

   void forlist(LuaString var1) {
      FuncState var2 = this.fs;
      expdesc var3 = new expdesc();
      int var4 = 4;
      short var6 = var2.freereg;
      this.new_localvarliteral("(for generator)");
      this.new_localvarliteral("(for state)");
      this.new_localvarliteral("(for control)");
      this.new_localvar(var1);

      while (this.testnext(44)) {
         this.new_localvar(this.str_checkname());
         var4++;
      }

      this.checknext(268);
      int var5 = this.linenumber;
      this.adjust_assign(3, this.explist(var3), var3);
      var2.checkstack(3);
      this.forbody(var6, var5, var4 - 3, false);
   }

   void forstat(int var1) {
      FuncState var2 = this.fs;
      FuncState.BlockCnt var4 = new FuncState.BlockCnt();
      var2.enterblock(var4, true);
      this.next();
      LuaString var3 = this.str_checkname();
      switch (this.t.token) {
         case 44:
         case 268:
            this.forlist(var3);
            break;
         case 61:
            this.fornum(var3, var1);
            break;
         default:
            this.syntaxerror(LUA_QL("=") + " or " + LUA_QL("in") + " expected");
      }

      this.check_match(262, 264, var1);
      var2.leaveblock();
   }

   void test_then_block(IntPtr var1) {
      expdesc var2 = new expdesc();
      FuncState.BlockCnt var3 = new FuncState.BlockCnt();
      this.next();
      this.expr(var2);
      this.checknext(275);
      int var4;
      if (this.t.token != 266 && this.t.token != 258) {
         this.fs.goiftrue(var2);
         this.fs.enterblock(var3, false);
         var4 = var2.f.i;
      } else {
         this.fs.goiffalse(var2);
         this.fs.enterblock(var3, false);
         this.gotostat(var2.t.i);
         this.skipnoopstat();
         if (this.block_follow(false)) {
            this.fs.leaveblock();
            return;
         }

         var4 = this.fs.jump();
      }

      this.statlist();
      this.fs.leaveblock();
      if (this.t.token == 260 || this.t.token == 261) {
         this.fs.concat(var1, this.fs.jump());
      }

      this.fs.patchtohere(var4);
   }

   void ifstat(int var1) {
      IntPtr var2 = new IntPtr(-1);
      this.test_then_block(var2);

      while (this.t.token == 261) {
         this.test_then_block(var2);
      }

      if (this.testnext(260)) {
         this.block();
      }

      this.check_match(262, 267, var1);
      this.fs.patchtohere(var2.i);
   }

   void localfunc() {
      expdesc var1 = new expdesc();
      FuncState var2 = this.fs;
      this.new_localvar(this.str_checkname());
      this.adjustlocalvars(1);
      this.body(var1, false, this.linenumber);
      var2.getlocvar(var2.nactvar - 1).startpc = var2.pc;
   }

   void localstat() {
      int var1 = 0;
      expdesc var3 = new expdesc();

      do {
         this.new_localvar(this.str_checkname());
         var1++;
      } while (this.testnext(44));

      int var2;
      if (this.testnext(61)) {
         var2 = this.explist(var3);
      } else {
         var3.k = 0;
         var2 = 0;
      }

      this.adjust_assign(var1, var2, var3);
      this.adjustlocalvars(var1);
   }

   boolean funcname(expdesc var1) {
      boolean var2 = false;
      this.singlevar(var1);

      while (this.t.token == 46) {
         this.fieldsel(var1);
      }

      if (this.t.token == 58) {
         var2 = true;
         this.fieldsel(var1);
      }

      return var2;
   }

   void funcstat(int var1) {
      expdesc var3 = new expdesc();
      expdesc var4 = new expdesc();
      this.next();
      boolean var2 = this.funcname(var3);
      this.body(var4, var2, var1);
      this.fs.storevar(var3, var4);
      this.fs.fixline(var1);
   }

   void exprstat() {
      FuncState var1 = this.fs;
      LHS_assign var2 = new LHS_assign();
      this.suffixedexp(var2.v);
      if (this.t.token != 61 && this.t.token != 44) {
         this.check_condition(var2.v.k == 12, "syntax error");
         SETARG_C(var1.getcodePtr(var2.v), 1);
      } else {
         var2.prev = null;
         this.assignment(var2, 1);
      }
   }

   void retstat() {
      FuncState var1 = this.fs;
      expdesc var2 = new expdesc();
      int var3;
      int var4;
      if (!this.block_follow(true) && this.t.token != 59) {
         var4 = this.explist(var2);
         if (this.hasmultret(var2.k)) {
            var1.setmultret(var2);
            if (var2.k == 12 && var4 == 1) {
               SET_OPCODE(var1.getcodePtr(var2), 30);
               _assert(Lua.GETARG_A(var1.getcode(var2)) == var1.nactvar);
            }

            var3 = var1.nactvar;
            var4 = -1;
         } else if (var4 == 1) {
            var3 = var1.exp2anyreg(var2);
         } else {
            var1.exp2nextreg(var2);
            var3 = var1.nactvar;
            _assert(var4 == var1.freereg - var3);
         }
      } else {
         var4 = 0;
         var3 = 0;
      }

      var1.ret(var3, var4);
      this.testnext(59);
   }

   void statement() {
      int var1 = this.linenumber;
      this.enterlevel();
      switch (this.t.token) {
         case 59:
            this.next();
            break;
         case 258:
         case 266:
            this.gotostat(this.fs.jump());
            break;
         case 259:
            this.next();
            this.block();
            this.check_match(262, 259, var1);
            break;
         case 264:
            this.forstat(var1);
            break;
         case 265:
            this.funcstat(var1);
            break;
         case 267:
            this.ifstat(var1);
            break;
         case 269:
            this.next();
            if (this.testnext(265)) {
               this.localfunc();
            } else {
               this.localstat();
            }
            break;
         case 273:
            this.repeatstat(var1);
            break;
         case 274:
            this.next();
            this.retstat();
            break;
         case 278:
            this.whilestat(var1);
            break;
         case 285:
            this.next();
            this.labelstat(this.str_checkname(), var1);
            break;
         default:
            this.exprstat();
      }

      _assert(this.fs.f.maxstacksize >= this.fs.freereg && this.fs.freereg >= this.fs.nactvar);
      this.fs.freereg = this.fs.nactvar;
      this.leavelevel();
   }

   void statlist() {
      while (!this.block_follow(true)) {
         if (this.t.token == 274) {
            this.statement();
            return;
         }

         this.statement();
      }
   }

   public void mainfunc(FuncState var1) {
      FuncState.BlockCnt var2 = new FuncState.BlockCnt();
      this.open_func(var1, var2);
      this.fs.f.is_vararg = 1;
      expdesc var3 = new expdesc();
      var3.init(7, 0);
      this.fs.newupvalue(this.envn, var3);
      this.next();
      this.statlist();
      this.check(286);
      this.close_func();
   }

   static {
      for (int var0 = 0; var0 < RESERVED_LOCAL_VAR_KEYWORDS.length; var0++) {
         RESERVED_LOCAL_VAR_KEYWORDS_TABLE.put(RESERVED_LOCAL_VAR_KEYWORDS[var0], Boolean.TRUE);
      }

      luaX_tokens = new String[]{
         "and",
         "break",
         "do",
         "else",
         "elseif",
         "end",
         "false",
         "for",
         "function",
         "goto",
         "if",
         "in",
         "local",
         "nil",
         "not",
         "or",
         "repeat",
         "return",
         "then",
         "true",
         "until",
         "while",
         "..",
         "...",
         "==",
         ">=",
         "<=",
         "~=",
         "::",
         "<eos>",
         "<number>",
         "<name>",
         "<string>",
         "<eof>"
      };
      RESERVED = new Hashtable();

      for (int var2 = 0; var2 < 22; var2++) {
         LuaString var1 = LuaValue.valueOf(luaX_tokens[var2]);
         RESERVED.put(var1, Integer.valueOf(257 + var2));
      }

      priority = new Priority[]{
         new Priority(6, 6),
         new Priority(6, 6),
         new Priority(7, 7),
         new Priority(7, 7),
         new Priority(7, 7),
         new Priority(10, 9),
         new Priority(5, 4),
         new Priority(3, 3),
         new Priority(3, 3),
         new Priority(3, 3),
         new Priority(3, 3),
         new Priority(3, 3),
         new Priority(3, 3),
         new Priority(2, 2),
         new Priority(1, 1)
      };
   }

   static class ConsControl {
      expdesc v = new expdesc();
      expdesc t;
      int nh;
      int na;
      int tostore;
   }

   static class Dyndata {
      Vardesc[] actvar;
      int n_actvar = 0;
      Labeldesc[] gt;
      int n_gt = 0;
      Labeldesc[] label;
      int n_label = 0;
   }

   static class LHS_assign {
      LHS_assign prev;
      expdesc v = new expdesc();
   }

   static class Labeldesc {
      LuaString name;
      int pc;
      int line;
      short nactvar;

      public Labeldesc(LuaString var1, int var2, int var3, short var4) {
         this.name = var1;
         this.pc = var2;
         this.line = var3;
         this.nactvar = var4;
      }
   }

   static class Priority {
      final byte left;
      final byte right;

      public Priority(int var1, int var2) {
         this.left = (byte)var1;
         this.right = (byte)var2;
      }
   }

   private static class SemInfo {
      LuaValue r;
      LuaString ts;

      private SemInfo() {
      }
   }

   private static class Token {
      int token;
      final SemInfo seminfo = new SemInfo();

      private Token() {
      }

      public void set(Token var1) {
         this.token = var1.token;
         this.seminfo.r = var1.seminfo.r;
         this.seminfo.ts = var1.seminfo.ts;
      }
   }

   static class Vardesc {
      final short idx;

      Vardesc(int var1) {
         this.idx = (short)var1;
      }
   }

   static class expdesc {
      int k;
      final U u = new U();
      final IntPtr t = new IntPtr();
      final IntPtr f = new IntPtr();

      void init(int var1, int var2) {
         this.f.i = -1;
         this.t.i = -1;
         this.k = var1;
         this.u.info = var2;
      }

      boolean hasjumps() {
         return this.t.i != this.f.i;
      }

      boolean isnumeral() {
         return this.k == 5 && this.t.i == -1 && this.f.i == -1;
      }

      public void setvalue(expdesc var1) {
         this.f.i = var1.f.i;
         this.k = var1.k;
         this.t.i = var1.t.i;
         this.u._nval = var1.u._nval;
         this.u.ind_idx = var1.u.ind_idx;
         this.u.ind_t = var1.u.ind_t;
         this.u.ind_vt = var1.u.ind_vt;
         this.u.info = var1.u.info;
      }

      static class U {
         short ind_idx;
         short ind_t;
         short ind_vt;
         private LuaValue _nval;
         int info;

         public void setNval(LuaValue var1) {
            this._nval = var1;
         }

         public LuaValue nval() {
            return (LuaValue)(this._nval == null ? LuaInteger.valueOf(this.info) : this._nval);
         }
      }
   }
}
