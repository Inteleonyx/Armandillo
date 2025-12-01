package dev.inteleonyx.armandillo.api.luaj.parser;

import dev.inteleonyx.armandillo.api.luaj.LuaString;
import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.ast.*;

import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LuaParser implements LuaParserConstants {
   public LuaParserTokenManager token_source;
   SimpleCharStream jj_input_stream;
   public Token token;
   public Token jj_nt;
   private int jj_ntk;
   private Token jj_scanpos;
   private Token jj_lastpos;
   private int jj_la;
   private int jj_gen;
   private final int[] jj_la1 = new int[34];
   private static int[] jj_la1_0;
   private static int[] jj_la1_1;
   private static int[] jj_la1_2;
   private final JJCalls[] jj_2_rtns = new JJCalls[7];
   private boolean jj_rescan = false;
   private int jj_gc = 0;
   private final LookaheadSuccess jj_ls = new LookaheadSuccess();
   private List<int[]> jj_expentries = new ArrayList();
   private int[] jj_expentry;
   private int jj_kind = -1;
   private int[] jj_lasttokens = new int[100];
   private int jj_endpos;

   public static void main(String[] var0) throws ParseException {
      LuaParser var1 = new LuaParser(System.in);
      var1.Chunk();
   }

   private static Exp.VarExp assertvarexp(Exp.PrimaryExp var0) throws ParseException {
      if (!var0.isvarexp()) {
         throw new ParseException("expected variable");
      } else {
         return (Exp.VarExp)var0;
      }
   }

   private static Exp.FuncCall assertfunccall(Exp.PrimaryExp var0) throws ParseException {
      if (!var0.isfunccall()) {
         throw new ParseException("expected function call");
      } else {
         return (Exp.FuncCall)var0;
      }
   }

   public SimpleCharStream getCharStream() {
      return this.jj_input_stream;
   }

   private long LineInfo() {
      return (long)this.jj_input_stream.getBeginLine() << 32 | this.jj_input_stream.getBeginColumn();
   }

   private void L(SyntaxElement var1, long var2) {
      var1.beginLine = (int)(var2 >> 32);
      var1.beginColumn = (short)var2;
      var1.endLine = this.token.endLine;
      var1.endColumn = (short)this.token.endColumn;
   }

   private void L(SyntaxElement var1, Token var2) {
      var1.beginLine = var2.beginLine;
      var1.beginColumn = (short)var2.beginColumn;
      var1.endLine = this.token.endLine;
      var1.endColumn = (short)this.token.endColumn;
   }

   public final Chunk Chunk() throws ParseException {
      long var3 = this.LineInfo();
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 69:
            this.jj_consume_token(69);
            this.token_source.SwitchTo(1);
            break;
         default:
            this.jj_la1[0] = this.jj_gen;
      }

      Block var1 = this.Block();
      this.jj_consume_token(0);
      Chunk var2 = new Chunk(var1);
      this.L(var2, var3);
      return var2;
   }

   public final Block Block() throws ParseException {
      Block var1 = new Block();
      long var3 = this.LineInfo();

      while (true) {
         switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 30:
            case 31:
            case 36:
            case 37:
            case 38:
            case 39:
            case 41:
            case 46:
            case 50:
            case 51:
            case 65:
            case 70:
            case 75:
               Stat var5 = this.Stat();
               var1.add(var5);
               break;
            case 32:
            case 33:
            case 34:
            case 35:
            case 40:
            case 42:
            case 43:
            case 44:
            case 45:
            case 47:
            case 48:
            case 49:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 66:
            case 67:
            case 68:
            case 69:
            case 71:
            case 72:
            case 73:
            case 74:
            default:
               this.jj_la1[1] = this.jj_gen;
               switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
                  case 45:
                     Stat var2 = this.ReturnStat();
                     var1.add(var2);
                     break;
                  default:
                     this.jj_la1[2] = this.jj_gen;
               }

               this.L(var1, var3);
               return var1;
         }
      }
   }

   public final Stat Stat() throws ParseException {
      Exp var5 = null;
      List var11 = null;
      long var12 = this.LineInfo();
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 30:
            this.jj_consume_token(30);
            Stat var31 = Stat.breakstat();
            this.L(var31, var12);
            return var31;
         case 31:
            this.jj_consume_token(31);
            Block var17 = this.Block();
            this.jj_consume_token(34);
            Stat var30 = Stat.block(var17);
            this.L(var30, var12);
            return var30;
         case 38:
            this.jj_consume_token(38);
            Token var34 = this.jj_consume_token(51);
            Stat var29 = Stat.gotostat(var34.image);
            this.L(var29, var12);
            return var29;
         case 39:
            Stat var28 = this.IfThenElse();
            this.L(var28, var12);
            return var28;
         case 46:
            this.jj_consume_token(46);
            Block var16 = this.Block();
            this.jj_consume_token(49);
            Exp var19 = this.Exp();
            Stat var27 = Stat.repeatuntil(var16, var19);
            this.L(var27, var12);
            return var27;
         case 50:
            this.jj_consume_token(50);
            Exp var18 = this.Exp();
            this.jj_consume_token(31);
            Block var15 = this.Block();
            this.jj_consume_token(34);
            Stat var26 = Stat.whiledo(var18, var15);
            this.L(var26, var12);
            return var26;
         case 65:
            Stat var25 = this.Label();
            this.L(var25, var12);
            return var25;
         case 70:
            this.jj_consume_token(70);
            return null;
         default:
            this.jj_la1[5] = this.jj_gen;
            if (this.jj_2_1(3)) {
               this.jj_consume_token(36);
               Token var33 = this.jj_consume_token(51);
               this.jj_consume_token(71);
               Exp var3 = this.Exp();
               this.jj_consume_token(72);
               Exp var4 = this.Exp();
               switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
                  case 72:
                     this.jj_consume_token(72);
                     var5 = this.Exp();
                     break;
                  default:
                     this.jj_la1[3] = this.jj_gen;
               }

               this.jj_consume_token(31);
               Block var14 = this.Block();
               this.jj_consume_token(34);
               Stat var24 = Stat.fornumeric(var33.image, var3, var4, var5, var14);
               this.L(var24, var12);
               return var24;
            } else {
               switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
                  case 36:
                     this.jj_consume_token(36);
                     List var35 = this.NameList();
                     this.jj_consume_token(40);
                     var11 = this.ExpList();
                     this.jj_consume_token(31);
                     Block var1 = this.Block();
                     this.jj_consume_token(34);
                     Stat var23 = Stat.forgeneric(var35, var11, var1);
                     this.L(var23, var12);
                     return var23;
                  case 37:
                     this.jj_consume_token(37);
                     FuncName var7 = this.FuncName();
                     FuncBody var32 = this.FuncBody();
                     Stat var22 = Stat.functiondef(var7, var32);
                     this.L(var22, var12);
                     return var22;
                  default:
                     this.jj_la1[6] = this.jj_gen;
                     if (this.jj_2_2(2)) {
                        this.jj_consume_token(41);
                        this.jj_consume_token(37);
                        Token var9 = this.jj_consume_token(51);
                        FuncBody var8 = this.FuncBody();
                        Stat var21 = Stat.localfunctiondef(var9.image, var8);
                        this.L(var21, var12);
                        return var21;
                     } else {
                        switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
                           case 41:
                              this.jj_consume_token(41);
                              List var10 = this.NameList();
                              switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
                                 case 71:
                                    this.jj_consume_token(71);
                                    var11 = this.ExpList();
                                    break;
                                 default:
                                    this.jj_la1[4] = this.jj_gen;
                              }

                              Stat var20 = Stat.localassignment(var10, var11);
                              this.L(var20, var12);
                              return var20;
                           case 51:
                           case 75:
                              Stat var6 = this.ExprStat();
                              this.L(var6, var12);
                              return var6;
                           default:
                              this.jj_la1[7] = this.jj_gen;
                              this.jj_consume_token(-1);
                              throw new ParseException();
                        }
                     }
               }
            }
      }
   }

   public final Stat IfThenElse() throws ParseException {
      Block var3 = null;
      ArrayList var6 = null;
      ArrayList var7 = null;
      this.jj_consume_token(39);
      Exp var4 = this.Exp();
      this.jj_consume_token(47);
      Block var1 = this.Block();

      while (true) {
         switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 33:
               this.jj_consume_token(33);
               Exp var5 = this.Exp();
               this.jj_consume_token(47);
               Block var2 = this.Block();
               if (var6 == null) {
                  var6 = new ArrayList();
               }

               if (var7 == null) {
                  var7 = new ArrayList();
               }

               var6.add(var5);
               var7.add(var2);
               break;
            default:
               this.jj_la1[8] = this.jj_gen;
               switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
                  case 32:
                     this.jj_consume_token(32);
                     var3 = this.Block();
                     break;
                  default:
                     this.jj_la1[9] = this.jj_gen;
               }

               this.jj_consume_token(34);
               return Stat.ifthenelse(var4, var1, var6, var7, var3);
         }
      }
   }

   public final Stat ReturnStat() throws ParseException {
      List var1 = null;
      long var3 = this.LineInfo();
      this.jj_consume_token(45);
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 23:
         case 24:
         case 25:
         case 26:
         case 27:
         case 35:
         case 37:
         case 42:
         case 43:
         case 48:
         case 51:
         case 52:
         case 61:
         case 62:
         case 69:
         case 75:
         case 79:
         case 80:
         case 83:
            var1 = this.ExpList();
            break;
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 36:
         case 38:
         case 39:
         case 40:
         case 41:
         case 44:
         case 45:
         case 46:
         case 47:
         case 49:
         case 50:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 76:
         case 77:
         case 78:
         case 81:
         case 82:
         default:
            this.jj_la1[10] = this.jj_gen;
      }

      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 70:
            this.jj_consume_token(70);
            break;
         default:
            this.jj_la1[11] = this.jj_gen;
      }

      Stat var2 = Stat.returnstat(var1);
      this.L(var2, var3);
      return var2;
   }

   public final Stat Label() throws ParseException {
      this.jj_consume_token(65);
      Token var1 = this.jj_consume_token(51);
      this.jj_consume_token(65);
      return Stat.labelstat(var1.image);
   }

   public final Stat ExprStat() throws ParseException {
      Stat var2 = null;
      long var3 = this.LineInfo();
      Exp.PrimaryExp var1 = this.PrimaryExp();
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 71:
         case 72:
            var2 = this.Assign(assertvarexp(var1));
            break;
         default:
            this.jj_la1[12] = this.jj_gen;
      }

      if (var2 == null) {
         var2 = Stat.functioncall(assertfunccall(var1));
      }

      this.L(var2, var3);
      return var2;
   }

   public final Stat Assign(Exp.VarExp var1) throws ParseException {
      ArrayList var2 = new ArrayList();
      var2.add(var1);
      long var6 = this.LineInfo();

      while (true) {
         switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 72:
               this.jj_consume_token(72);
               Exp.VarExp var3 = this.VarExp();
               var2.add(var3);
               break;
            default:
               this.jj_la1[13] = this.jj_gen;
               this.jj_consume_token(71);
               List var4 = this.ExpList();
               Stat var5 = Stat.assignment(var2, var4);
               this.L(var5, var6);
               return var5;
         }
      }
   }

   public final Exp.VarExp VarExp() throws ParseException {
      Exp.PrimaryExp var1 = this.PrimaryExp();
      return assertvarexp(var1);
   }

   public final FuncName FuncName() throws ParseException {
      Token var1 = this.jj_consume_token(51);
      FuncName var2 = new FuncName(var1.image);

      while (true) {
         switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 73:
               this.jj_consume_token(73);
               var1 = this.jj_consume_token(51);
               var2.adddot(var1.image);
               break;
            default:
               this.jj_la1[14] = this.jj_gen;
               switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
                  case 74:
                     this.jj_consume_token(74);
                     var1 = this.jj_consume_token(51);
                     var2.method = var1.image;
                     break;
                  default:
                     this.jj_la1[15] = this.jj_gen;
               }

               this.L(var2, var1);
               return var2;
         }
      }
   }

   public final Exp.PrimaryExp PrefixExp() throws ParseException {
      long var4 = this.LineInfo();
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 51:
            Token var1 = this.jj_consume_token(51);
            Exp.NameExp var6 = Exp.nameprefix(var1.image);
            this.L(var6, var4);
            return var6;
         case 75:
            this.jj_consume_token(75);
            Exp var2 = this.Exp();
            this.jj_consume_token(76);
            Exp.ParensExp var3 = Exp.parensprefix(var2);
            this.L(var3, var4);
            return var3;
         default:
            this.jj_la1[16] = this.jj_gen;
            this.jj_consume_token(-1);
            throw new ParseException();
      }
   }

   public final Exp.PrimaryExp PrimaryExp() throws ParseException {
      long var2 = this.LineInfo();
      Exp.PrimaryExp var1 = this.PrefixExp();

      while (this.jj_2_3(2)) {
         var1 = this.PostfixOp(var1);
      }

      this.L(var1, var2);
      return var1;
   }

   public final Exp.PrimaryExp PostfixOp(Exp.PrimaryExp var1) throws ParseException {
      long var6 = this.LineInfo();
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 23:
         case 24:
         case 25:
         case 26:
         case 27:
         case 61:
         case 62:
         case 75:
         case 80:
            FuncArgs var9 = this.FuncArgs();
            Exp.FuncCall var12 = Exp.functionop(var1, var9);
            this.L(var12, var6);
            return var12;
         case 73:
            this.jj_consume_token(73);
            Token var8 = this.jj_consume_token(51);
            Exp.FieldExp var11 = Exp.fieldop(var1, var8.image);
            this.L(var11, var6);
            return var11;
         case 74:
            this.jj_consume_token(74);
            Token var2 = this.jj_consume_token(51);
            FuncArgs var4 = this.FuncArgs();
            Exp.MethodCall var10 = Exp.methodop(var1, var2.image, var4);
            this.L(var10, var6);
            return var10;
         case 77:
            this.jj_consume_token(77);
            Exp var3 = this.Exp();
            this.jj_consume_token(78);
            Exp.IndexExp var5 = Exp.indexop(var1, var3);
            this.L(var5, var6);
            return var5;
         default:
            this.jj_la1[17] = this.jj_gen;
            this.jj_consume_token(-1);
            throw new ParseException();
      }
   }

   public final FuncArgs FuncArgs() throws ParseException {
      List var1 = null;
      long var5 = this.LineInfo();
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 23:
         case 24:
         case 25:
         case 26:
         case 27:
         case 61:
         case 62:
            LuaString var3 = this.Str();
            FuncArgs var8 = FuncArgs.string(var3);
            this.L(var8, var5);
            return var8;
         case 75:
            this.jj_consume_token(75);
            switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
               case 23:
               case 24:
               case 25:
               case 26:
               case 27:
               case 35:
               case 37:
               case 42:
               case 43:
               case 48:
               case 51:
               case 52:
               case 61:
               case 62:
               case 69:
               case 75:
               case 79:
               case 80:
               case 83:
                  var1 = this.ExpList();
                  break;
               case 28:
               case 29:
               case 30:
               case 31:
               case 32:
               case 33:
               case 34:
               case 36:
               case 38:
               case 39:
               case 40:
               case 41:
               case 44:
               case 45:
               case 46:
               case 47:
               case 49:
               case 50:
               case 53:
               case 54:
               case 55:
               case 56:
               case 57:
               case 58:
               case 59:
               case 60:
               case 63:
               case 64:
               case 65:
               case 66:
               case 67:
               case 68:
               case 70:
               case 71:
               case 72:
               case 73:
               case 74:
               case 76:
               case 77:
               case 78:
               case 81:
               case 82:
               default:
                  this.jj_la1[18] = this.jj_gen;
            }

            this.jj_consume_token(76);
            FuncArgs var7 = FuncArgs.explist(var1);
            this.L(var7, var5);
            return var7;
         case 80:
            TableConstructor var2 = this.TableConstructor();
            FuncArgs var4 = FuncArgs.tableconstructor(var2);
            this.L(var4, var5);
            return var4;
         default:
            this.jj_la1[19] = this.jj_gen;
            this.jj_consume_token(-1);
            throw new ParseException();
      }
   }

   public final List NameList() throws ParseException {
      ArrayList var1 = new ArrayList();
      Token var2 = this.jj_consume_token(51);
      var1.add(new Name(var2.image));

      while (this.jj_2_4(2)) {
         this.jj_consume_token(72);
         var2 = this.jj_consume_token(51);
         var1.add(new Name(var2.image));
      }

      return var1;
   }

   public final List ExpList() throws ParseException {
      ArrayList var1 = new ArrayList();
      Exp var2 = this.Exp();
      var1.add(var2);

      while (true) {
         switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 72:
               this.jj_consume_token(72);
               var2 = this.Exp();
               var1.add(var2);
               break;
            default:
               this.jj_la1[20] = this.jj_gen;
               return var1;
         }
      }
   }

   public final Exp SimpleExp() throws ParseException {
      long var6 = this.LineInfo();
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 23:
         case 24:
         case 25:
         case 26:
         case 27:
         case 61:
         case 62:
            LuaString var2 = this.Str();
            Exp var14 = Exp.constant(var2);
            this.L(var14, var6);
            return var14;
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 36:
         case 38:
         case 39:
         case 40:
         case 41:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 49:
         case 50:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 76:
         case 77:
         case 78:
         default:
            this.jj_la1[21] = this.jj_gen;
            this.jj_consume_token(-1);
            throw new ParseException();
         case 35:
            this.jj_consume_token(35);
            Exp var13 = Exp.constant(LuaValue.FALSE);
            this.L(var13, var6);
            return var13;
         case 37:
            FuncBody var5 = this.FunctionCall();
            Exp var12 = Exp.anonymousfunction(var5);
            this.L(var12, var6);
            return var12;
         case 42:
            this.jj_consume_token(42);
            Exp var11 = Exp.constant(LuaValue.NIL);
            this.L(var11, var6);
            return var11;
         case 48:
            this.jj_consume_token(48);
            Exp var10 = Exp.constant(LuaValue.TRUE);
            this.L(var10, var6);
            return var10;
         case 51:
         case 75:
            return this.PrimaryExp();
         case 52:
            Token var1 = this.jj_consume_token(52);
            Exp var9 = Exp.numberconstant(var1.image);
            this.L(var9, var6);
            return var9;
         case 79:
            this.jj_consume_token(79);
            Exp var8 = Exp.varargs();
            this.L(var8, var6);
            return var8;
         case 80:
            TableConstructor var4 = this.TableConstructor();
            Exp var3 = Exp.tableconstructor(var4);
            this.L(var3, var6);
            return var3;
      }
   }

   public final LuaString Str() throws ParseException {
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 23:
            this.jj_consume_token(23);
            return Str.longString(this.token.image);
         case 24:
            this.jj_consume_token(24);
            return Str.longString(this.token.image);
         case 25:
            this.jj_consume_token(25);
            return Str.longString(this.token.image);
         case 26:
            this.jj_consume_token(26);
            return Str.longString(this.token.image);
         case 27:
            this.jj_consume_token(27);
            return Str.longString(this.token.image);
         case 61:
            this.jj_consume_token(61);
            return Str.quoteString(this.token.image);
         case 62:
            this.jj_consume_token(62);
            return Str.charString(this.token.image);
         default:
            this.jj_la1[22] = this.jj_gen;
            this.jj_consume_token(-1);
            throw new ParseException();
      }
   }

   public final Exp Exp() throws ParseException {
      long var4 = this.LineInfo();
      Exp var1;
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 23:
         case 24:
         case 25:
         case 26:
         case 27:
         case 35:
         case 37:
         case 42:
         case 48:
         case 51:
         case 52:
         case 61:
         case 62:
         case 75:
         case 79:
         case 80:
            var1 = this.SimpleExp();
            break;
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 36:
         case 38:
         case 39:
         case 40:
         case 41:
         case 44:
         case 45:
         case 46:
         case 47:
         case 49:
         case 50:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 76:
         case 77:
         case 78:
         case 81:
         case 82:
         default:
            this.jj_la1[23] = this.jj_gen;
            this.jj_consume_token(-1);
            throw new ParseException();
         case 43:
         case 69:
         case 83:
            int var3 = this.Unop();
            Exp var2 = this.Exp();
            var1 = Exp.unaryexp(var3, var2);
      }

      while (this.jj_2_5(2)) {
         int var7 = this.Binop();
         Exp var6 = this.Exp();
         var1 = Exp.binaryexp(var1, var7, var6);
      }

      this.L(var1, var4);
      return var1;
   }

   public final FuncBody FunctionCall() throws ParseException {
      long var2 = this.LineInfo();
      this.jj_consume_token(37);
      FuncBody var1 = this.FuncBody();
      this.L(var1, var2);
      return var1;
   }

   public final FuncBody FuncBody() throws ParseException {
      ParList var1 = null;
      long var4 = this.LineInfo();
      this.jj_consume_token(75);
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 51:
         case 79:
            var1 = this.ParList();
            break;
         default:
            this.jj_la1[24] = this.jj_gen;
      }

      this.jj_consume_token(76);
      Block var2 = this.Block();
      this.jj_consume_token(34);
      FuncBody var3 = new FuncBody(var1, var2);
      this.L(var3, var4);
      return var3;
   }

   public final ParList ParList() throws ParseException {
      Object var1 = null;
      boolean var2 = false;
      long var4 = this.LineInfo();
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 51:
            var1 = this.NameList();
            switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
               case 72:
                  this.jj_consume_token(72);
                  this.jj_consume_token(79);
                  var2 = true;
                  break;
               default:
                  this.jj_la1[25] = this.jj_gen;
            }

            ParList var7 = new ParList((List)var1, var2);
            this.L(var7, var4);
            return var7;
         case 79:
            this.jj_consume_token(79);
            ParList var3 = new ParList(null, true);
            this.L(var3, var4);
            return var3;
         default:
            this.jj_la1[26] = this.jj_gen;
            this.jj_consume_token(-1);
            throw new ParseException();
      }
   }

   public final TableConstructor TableConstructor() throws ParseException {
      TableConstructor var1 = new TableConstructor();
      Object var2 = null;
      long var3 = this.LineInfo();
      this.jj_consume_token(80);
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 23:
         case 24:
         case 25:
         case 26:
         case 27:
         case 35:
         case 37:
         case 42:
         case 43:
         case 48:
         case 51:
         case 52:
         case 61:
         case 62:
         case 69:
         case 75:
         case 77:
         case 79:
         case 80:
         case 83:
            var2 = this.FieldList();
            var1.fields = (List)var2;
            break;
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 36:
         case 38:
         case 39:
         case 40:
         case 41:
         case 44:
         case 45:
         case 46:
         case 47:
         case 49:
         case 50:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 76:
         case 78:
         case 81:
         case 82:
         default:
            this.jj_la1[27] = this.jj_gen;
      }

      this.jj_consume_token(81);
      this.L(var1, var3);
      return var1;
   }

   public final List FieldList() throws ParseException {
      ArrayList var1 = new ArrayList();
      TableField var2 = this.Field();
      var1.add(var2);

      while (this.jj_2_6(2)) {
         this.FieldSep();
         var2 = this.Field();
         var1.add(var2);
      }

      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 70:
         case 72:
            this.FieldSep();
            break;
         default:
            this.jj_la1[28] = this.jj_gen;
      }

      return var1;
   }

   public final TableField Field() throws ParseException {
      long var5 = this.LineInfo();
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 77:
            this.jj_consume_token(77);
            Exp var2 = this.Exp();
            this.jj_consume_token(78);
            this.jj_consume_token(71);
            Exp var8 = this.Exp();
            TableField var10 = TableField.keyedField(var2, var8);
            this.L(var10, var5);
            return var10;
         default:
            this.jj_la1[29] = this.jj_gen;
            if (this.jj_2_7(2)) {
               Token var1 = this.jj_consume_token(51);
               this.jj_consume_token(71);
               Exp var7 = this.Exp();
               TableField var9 = TableField.namedField(var1.image, var7);
               this.L(var9, var5);
               return var9;
            } else {
               switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
                  case 23:
                  case 24:
                  case 25:
                  case 26:
                  case 27:
                  case 35:
                  case 37:
                  case 42:
                  case 43:
                  case 48:
                  case 51:
                  case 52:
                  case 61:
                  case 62:
                  case 69:
                  case 75:
                  case 79:
                  case 80:
                  case 83:
                     Exp var3 = this.Exp();
                     TableField var4 = TableField.listField(var3);
                     this.L(var4, var5);
                     return var4;
                  case 28:
                  case 29:
                  case 30:
                  case 31:
                  case 32:
                  case 33:
                  case 34:
                  case 36:
                  case 38:
                  case 39:
                  case 40:
                  case 41:
                  case 44:
                  case 45:
                  case 46:
                  case 47:
                  case 49:
                  case 50:
                  case 53:
                  case 54:
                  case 55:
                  case 56:
                  case 57:
                  case 58:
                  case 59:
                  case 60:
                  case 63:
                  case 64:
                  case 65:
                  case 66:
                  case 67:
                  case 68:
                  case 70:
                  case 71:
                  case 72:
                  case 73:
                  case 74:
                  case 76:
                  case 77:
                  case 78:
                  case 81:
                  case 82:
                  default:
                     this.jj_la1[30] = this.jj_gen;
                     this.jj_consume_token(-1);
                     throw new ParseException();
               }
            }
      }
   }

   public final void FieldSep() throws ParseException {
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 70:
            this.jj_consume_token(70);
            break;
         case 72:
            this.jj_consume_token(72);
            break;
         default:
            this.jj_la1[31] = this.jj_gen;
            this.jj_consume_token(-1);
            throw new ParseException();
      }
   }

   public final int Binop() throws ParseException {
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 29:
            this.jj_consume_token(29);
            return 60;
         case 44:
            this.jj_consume_token(44);
            return 59;
         case 82:
            this.jj_consume_token(82);
            return 13;
         case 83:
            this.jj_consume_token(83);
            return 14;
         case 84:
            this.jj_consume_token(84);
            return 15;
         case 85:
            this.jj_consume_token(85);
            return 16;
         case 86:
            this.jj_consume_token(86);
            return 18;
         case 87:
            this.jj_consume_token(87);
            return 17;
         case 88:
            this.jj_consume_token(88);
            return 22;
         case 89:
            this.jj_consume_token(89);
            return 25;
         case 90:
            this.jj_consume_token(90);
            return 26;
         case 91:
            this.jj_consume_token(91);
            return 63;
         case 92:
            this.jj_consume_token(92);
            return 62;
         case 93:
            this.jj_consume_token(93);
            return 24;
         case 94:
            this.jj_consume_token(94);
            return 61;
         default:
            this.jj_la1[32] = this.jj_gen;
            this.jj_consume_token(-1);
            throw new ParseException();
      }
   }

   public final int Unop() throws ParseException {
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 43:
            this.jj_consume_token(43);
            return 20;
         case 69:
            this.jj_consume_token(69);
            return 21;
         case 83:
            this.jj_consume_token(83);
            return 19;
         default:
            this.jj_la1[33] = this.jj_gen;
            this.jj_consume_token(-1);
            throw new ParseException();
      }
   }

   private boolean jj_2_1(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_1();
      } catch (LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(0, var1);
      }

      return var3;
   }

   private boolean jj_2_2(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_2();
      } catch (LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(1, var1);
      }

      return var3;
   }

   private boolean jj_2_3(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_3();
      } catch (LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(2, var1);
      }

      return var3;
   }

   private boolean jj_2_4(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_4();
      } catch (LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(3, var1);
      }

      return var3;
   }

   private boolean jj_2_5(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_5();
      } catch (LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(4, var1);
      }

      return var3;
   }

   private boolean jj_2_6(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_6();
      } catch (LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(5, var1);
      }

      return var3;
   }

   private boolean jj_2_7(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_7();
      } catch (LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(6, var1);
      }

      return var3;
   }

   private boolean jj_3R_43() {
      return this.jj_3R_58();
   }

   private boolean jj_3R_42() {
      return this.jj_3R_57();
   }

   private boolean jj_3R_41() {
      if (this.jj_scan_token(75)) {
         return true;
      } else {
         Token var1 = this.jj_scanpos;
         if (this.jj_3R_56()) {
            this.jj_scanpos = var1;
         }

         return this.jj_scan_token(76);
      }
   }

   private boolean jj_3R_38() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3R_41()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_42()) {
            this.jj_scanpos = var1;
            if (this.jj_3R_43()) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean jj_3_3() {
      return this.jj_3R_10();
   }

   private boolean jj_3R_18() {
      return this.jj_3R_38();
   }

   private boolean jj_3R_17() {
      return this.jj_scan_token(74) ? true : this.jj_scan_token(51);
   }

   private boolean jj_3R_16() {
      return this.jj_scan_token(77) ? true : this.jj_3R_12();
   }

   private boolean jj_3R_35() {
      return this.jj_3R_40();
   }

   private boolean jj_3R_15() {
      return this.jj_scan_token(73) ? true : this.jj_scan_token(51);
   }

   private boolean jj_3R_10() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3R_15()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_16()) {
            this.jj_scanpos = var1;
            if (this.jj_3R_17()) {
               this.jj_scanpos = var1;
               if (this.jj_3R_18()) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private boolean jj_3R_59() {
      return this.jj_scan_token(37);
   }

   private boolean jj_3_5() {
      return this.jj_3R_11() ? true : this.jj_3R_12();
   }

   private boolean jj_3R_60() {
      return this.jj_3R_70();
   }

   private boolean jj_3R_55() {
      return this.jj_scan_token(69);
   }

   private boolean jj_3R_54() {
      return this.jj_scan_token(43);
   }

   private boolean jj_3R_53() {
      return this.jj_scan_token(83);
   }

   private boolean jj_3R_40() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3R_53()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_54()) {
            this.jj_scanpos = var1;
            if (this.jj_3R_55()) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean jj_3R_34() {
      return this.jj_3R_39();
   }

   private boolean jj_3R_12() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3R_34()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_35()) {
            return true;
         }
      }

      return false;
   }

   private boolean jj_3R_73() {
      return this.jj_scan_token(75);
   }

   private boolean jj_3R_33() {
      return this.jj_scan_token(44);
   }

   private boolean jj_3R_72() {
      return this.jj_scan_token(51);
   }

   private boolean jj_3R_70() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3R_72()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_73()) {
            return true;
         }
      }

      return false;
   }

   private boolean jj_3_2() {
      return this.jj_scan_token(41) ? true : this.jj_scan_token(37);
   }

   private boolean jj_3R_32() {
      return this.jj_scan_token(29);
   }

   private boolean jj_3R_31() {
      return this.jj_scan_token(94);
   }

   private boolean jj_3_4() {
      return this.jj_scan_token(72) ? true : this.jj_scan_token(51);
   }

   private boolean jj_3R_30() {
      return this.jj_scan_token(93);
   }

   private boolean jj_3_1() {
      if (this.jj_scan_token(36)) {
         return true;
      } else {
         return this.jj_scan_token(51) ? true : this.jj_scan_token(71);
      }
   }

   private boolean jj_3R_29() {
      return this.jj_scan_token(92);
   }

   private boolean jj_3R_28() {
      return this.jj_scan_token(91);
   }

   private boolean jj_3R_69() {
      return this.jj_scan_token(27);
   }

   private boolean jj_3R_27() {
      return this.jj_scan_token(90);
   }

   private boolean jj_3R_68() {
      return this.jj_scan_token(26);
   }

   private boolean jj_3R_26() {
      return this.jj_scan_token(89);
   }

   private boolean jj_3R_67() {
      return this.jj_scan_token(25);
   }

   private boolean jj_3R_25() {
      return this.jj_scan_token(88);
   }

   private boolean jj_3R_66() {
      return this.jj_scan_token(24);
   }

   private boolean jj_3R_24() {
      return this.jj_scan_token(87);
   }

   private boolean jj_3R_65() {
      return this.jj_scan_token(23);
   }

   private boolean jj_3R_23() {
      return this.jj_scan_token(86);
   }

   private boolean jj_3R_64() {
      return this.jj_scan_token(62);
   }

   private boolean jj_3R_22() {
      return this.jj_scan_token(85);
   }

   private boolean jj_3R_63() {
      return this.jj_scan_token(61);
   }

   private boolean jj_3R_58() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3R_63()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_64()) {
            this.jj_scanpos = var1;
            if (this.jj_3R_65()) {
               this.jj_scanpos = var1;
               if (this.jj_3R_66()) {
                  this.jj_scanpos = var1;
                  if (this.jj_3R_67()) {
                     this.jj_scanpos = var1;
                     if (this.jj_3R_68()) {
                        this.jj_scanpos = var1;
                        if (this.jj_3R_69()) {
                           return true;
                        }
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   private boolean jj_3R_21() {
      return this.jj_scan_token(84);
   }

   private boolean jj_3R_20() {
      return this.jj_scan_token(83);
   }

   private boolean jj_3R_19() {
      return this.jj_scan_token(82);
   }

   private boolean jj_3R_11() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3R_19()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_20()) {
            this.jj_scanpos = var1;
            if (this.jj_3R_21()) {
               this.jj_scanpos = var1;
               if (this.jj_3R_22()) {
                  this.jj_scanpos = var1;
                  if (this.jj_3R_23()) {
                     this.jj_scanpos = var1;
                     if (this.jj_3R_24()) {
                        this.jj_scanpos = var1;
                        if (this.jj_3R_25()) {
                           this.jj_scanpos = var1;
                           if (this.jj_3R_26()) {
                              this.jj_scanpos = var1;
                              if (this.jj_3R_27()) {
                                 this.jj_scanpos = var1;
                                 if (this.jj_3R_28()) {
                                    this.jj_scanpos = var1;
                                    if (this.jj_3R_29()) {
                                       this.jj_scanpos = var1;
                                       if (this.jj_3R_30()) {
                                          this.jj_scanpos = var1;
                                          if (this.jj_3R_31()) {
                                             this.jj_scanpos = var1;
                                             if (this.jj_3R_32()) {
                                                this.jj_scanpos = var1;
                                                if (this.jj_3R_33()) {
                                                   return true;
                                                }
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   private boolean jj_3_6() {
      return this.jj_3R_13() ? true : this.jj_3R_14();
   }

   private boolean jj_3R_52() {
      return this.jj_3R_60();
   }

   private boolean jj_3R_51() {
      return this.jj_3R_59();
   }

   private boolean jj_3R_50() {
      return this.jj_3R_57();
   }

   private boolean jj_3R_13() {
      Token var1 = this.jj_scanpos;
      if (this.jj_scan_token(72)) {
         this.jj_scanpos = var1;
         if (this.jj_scan_token(70)) {
            return true;
         }
      }

      return false;
   }

   private boolean jj_3R_49() {
      return this.jj_scan_token(79);
   }

   private boolean jj_3R_48() {
      return this.jj_3R_58();
   }

   private boolean jj_3R_47() {
      return this.jj_scan_token(52);
   }

   private boolean jj_3R_46() {
      return this.jj_scan_token(35);
   }

   private boolean jj_3R_45() {
      return this.jj_scan_token(48);
   }

   private boolean jj_3R_44() {
      return this.jj_scan_token(42);
   }

   private boolean jj_3R_39() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3R_44()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_45()) {
            this.jj_scanpos = var1;
            if (this.jj_3R_46()) {
               this.jj_scanpos = var1;
               if (this.jj_3R_47()) {
                  this.jj_scanpos = var1;
                  if (this.jj_3R_48()) {
                     this.jj_scanpos = var1;
                     if (this.jj_3R_49()) {
                        this.jj_scanpos = var1;
                        if (this.jj_3R_50()) {
                           this.jj_scanpos = var1;
                           if (this.jj_3R_51()) {
                              this.jj_scanpos = var1;
                              if (this.jj_3R_52()) {
                                 return true;
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   private boolean jj_3R_37() {
      return this.jj_3R_12();
   }

   private boolean jj_3_7() {
      return this.jj_scan_token(51) ? true : this.jj_scan_token(71);
   }

   private boolean jj_3R_14() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3R_36()) {
         this.jj_scanpos = var1;
         if (this.jj_3_7()) {
            this.jj_scanpos = var1;
            if (this.jj_3R_37()) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean jj_3R_36() {
      return this.jj_scan_token(77);
   }

   private boolean jj_3R_71() {
      return this.jj_3R_14();
   }

   private boolean jj_3R_61() {
      return this.jj_3R_12();
   }

   private boolean jj_3R_62() {
      return this.jj_3R_71();
   }

   private boolean jj_3R_57() {
      if (this.jj_scan_token(80)) {
         return true;
      } else {
         Token var1 = this.jj_scanpos;
         if (this.jj_3R_62()) {
            this.jj_scanpos = var1;
         }

         return this.jj_scan_token(81);
      }
   }

   private boolean jj_3R_56() {
      return this.jj_3R_61();
   }

   private static void jj_la1_init_0() {
      jj_la1_0 = new int[]{
         0,
         -1073741824,
         0,
         0,
         0,
         -1073741824,
         0,
         0,
         0,
         0,
         260046848,
         0,
         0,
         0,
         0,
         0,
         0,
         260046848,
         260046848,
         260046848,
         0,
         260046848,
         260046848,
         260046848,
         0,
         0,
         0,
         260046848,
         0,
         0,
         260046848,
         0,
         536870912,
         0
      };
   }

   private static void jj_la1_init_1() {
      jj_la1_1 = new int[]{
         0,
         803568,
         8192,
         0,
         0,
         278720,
         48,
         524800,
         2,
         1,
         1612254248,
         0,
         0,
         0,
         0,
         0,
         524288,
         1610612736,
         1612254248,
         1610612736,
         0,
         1612252200,
         1610612736,
         1612254248,
         524288,
         0,
         524288,
         1612254248,
         0,
         0,
         1612254248,
         0,
         4096,
         2048
      };
   }

   private static void jj_la1_init_2() {
      jj_la1_2 = new int[]{
         32,
         2114,
         0,
         256,
         128,
         66,
         0,
         2048,
         0,
         0,
         624672,
         64,
         384,
         256,
         512,
         1024,
         2048,
         77312,
         624672,
         67584,
         256,
         100352,
         0,
         624672,
         32768,
         256,
         32768,
         632864,
         320,
         8192,
         624672,
         320,
         2147221504,
         524320
      };
   }

   public LuaParser(InputStream var1) {
      this(var1, null);
   }

   public LuaParser(InputStream var1, String var2) {
      try {
         this.jj_input_stream = new SimpleCharStream(var1, var2, 1, 1);
      } catch (UnsupportedEncodingException var4) {
         throw new RuntimeException(var4.getMessage());
      }

      this.token_source = new LuaParserTokenManager(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for (int var3 = 0; var3 < 34; var3++) {
         this.jj_la1[var3] = -1;
      }

      for (int var5 = 0; var5 < this.jj_2_rtns.length; var5++) {
         this.jj_2_rtns[var5] = new JJCalls();
      }
   }

   public void ReInit(InputStream var1) {
      this.ReInit(var1, null);
   }

   public void ReInit(InputStream var1, String var2) {
      try {
         this.jj_input_stream.ReInit(var1, var2, 1, 1);
      } catch (UnsupportedEncodingException var4) {
         throw new RuntimeException(var4.getMessage());
      }

      this.token_source.ReInit(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for (int var3 = 0; var3 < 34; var3++) {
         this.jj_la1[var3] = -1;
      }

      for (int var5 = 0; var5 < this.jj_2_rtns.length; var5++) {
         this.jj_2_rtns[var5] = new JJCalls();
      }
   }

   public LuaParser(Reader var1) {
      this.jj_input_stream = new SimpleCharStream(var1, 1, 1);
      this.token_source = new LuaParserTokenManager(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for (int var2 = 0; var2 < 34; var2++) {
         this.jj_la1[var2] = -1;
      }

      for (int var3 = 0; var3 < this.jj_2_rtns.length; var3++) {
         this.jj_2_rtns[var3] = new JJCalls();
      }
   }

   public void ReInit(Reader var1) {
      this.jj_input_stream.ReInit(var1, 1, 1);
      this.token_source.ReInit(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for (int var2 = 0; var2 < 34; var2++) {
         this.jj_la1[var2] = -1;
      }

      for (int var3 = 0; var3 < this.jj_2_rtns.length; var3++) {
         this.jj_2_rtns[var3] = new JJCalls();
      }
   }

   public LuaParser(LuaParserTokenManager var1) {
      this.token_source = var1;
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for (int var2 = 0; var2 < 34; var2++) {
         this.jj_la1[var2] = -1;
      }

      for (int var3 = 0; var3 < this.jj_2_rtns.length; var3++) {
         this.jj_2_rtns[var3] = new JJCalls();
      }
   }

   public void ReInit(LuaParserTokenManager var1) {
      this.token_source = var1;
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for (int var2 = 0; var2 < 34; var2++) {
         this.jj_la1[var2] = -1;
      }

      for (int var3 = 0; var3 < this.jj_2_rtns.length; var3++) {
         this.jj_2_rtns[var3] = new JJCalls();
      }
   }

   private Token jj_consume_token(int var1) throws ParseException {
      Token var2 = this.token;
      if (this.token.next != null) {
         this.token = this.token.next;
      } else {
         this.token = this.token.next = this.token_source.getNextToken();
      }

      this.jj_ntk = -1;
      if (this.token.kind != var1) {
         this.token = var2;
         this.jj_kind = var1;
         throw this.generateParseException();
      } else {
         this.jj_gen++;
         if (++this.jj_gc > 100) {
            this.jj_gc = 0;

            for (int var3 = 0; var3 < this.jj_2_rtns.length; var3++) {
               for (JJCalls var4 = this.jj_2_rtns[var3]; var4 != null; var4 = var4.next) {
                  if (var4.gen < this.jj_gen) {
                     var4.first = null;
                  }
               }
            }
         }

         return this.token;
      }
   }

   private boolean jj_scan_token(int var1) {
      if (this.jj_scanpos == this.jj_lastpos) {
         this.jj_la--;
         if (this.jj_scanpos.next == null) {
            this.jj_lastpos = this.jj_scanpos = this.jj_scanpos.next = this.token_source.getNextToken();
         } else {
            this.jj_lastpos = this.jj_scanpos = this.jj_scanpos.next;
         }
      } else {
         this.jj_scanpos = this.jj_scanpos.next;
      }

      if (this.jj_rescan) {
         int var2 = 0;

         Token var3;
         for (var3 = this.token; var3 != null && var3 != this.jj_scanpos; var3 = var3.next) {
            var2++;
         }

         if (var3 != null) {
            this.jj_add_error_token(var1, var2);
         }
      }

      if (this.jj_scanpos.kind != var1) {
         return true;
      } else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) {
         throw this.jj_ls;
      } else {
         return false;
      }
   }

   public final Token getNextToken() {
      if (this.token.next != null) {
         this.token = this.token.next;
      } else {
         this.token = this.token.next = this.token_source.getNextToken();
      }

      this.jj_ntk = -1;
      this.jj_gen++;
      return this.token;
   }

   public final Token getToken(int var1) {
      Token var2 = this.token;

      for (int var3 = 0; var3 < var1; var3++) {
         if (var2.next != null) {
            var2 = var2.next;
         } else {
            var2 = var2.next = this.token_source.getNextToken();
         }
      }

      return var2;
   }

   private int jj_ntk() {
      return (this.jj_nt = this.token.next) == null
         ? (this.jj_ntk = (this.token.next = this.token_source.getNextToken()).kind)
         : (this.jj_ntk = this.jj_nt.kind);
   }

   private void jj_add_error_token(int var1, int var2) {
      if (var2 < 100) {
         if (var2 == this.jj_endpos + 1) {
            this.jj_lasttokens[this.jj_endpos++] = var1;
         } else if (this.jj_endpos != 0) {
            this.jj_expentry = new int[this.jj_endpos];

            for (int var3 = 0; var3 < this.jj_endpos; var3++) {
               this.jj_expentry[var3] = this.jj_lasttokens[var3];
            }

            label41:
            for (int[] var4 : this.jj_expentries) {
               if (var4.length == this.jj_expentry.length) {
                  for (int var5 = 0; var5 < this.jj_expentry.length; var5++) {
                     if (var4[var5] != this.jj_expentry[var5]) {
                        continue label41;
                     }
                  }

                  this.jj_expentries.add(this.jj_expentry);
                  break;
               }
            }

            if (var2 != 0) {
               this.jj_lasttokens[(this.jj_endpos = var2) - 1] = var1;
            }
         }
      }
   }

   public ParseException generateParseException() {
      this.jj_expentries.clear();
      boolean[] var1 = new boolean[95];
      if (this.jj_kind >= 0) {
         var1[this.jj_kind] = true;
         this.jj_kind = -1;
      }

      for (int var2 = 0; var2 < 34; var2++) {
         if (this.jj_la1[var2] == this.jj_gen) {
            for (int var3 = 0; var3 < 32; var3++) {
               if ((jj_la1_0[var2] & 1 << var3) != 0) {
                  var1[var3] = true;
               }

               if ((jj_la1_1[var2] & 1 << var3) != 0) {
                  var1[32 + var3] = true;
               }

               if ((jj_la1_2[var2] & 1 << var3) != 0) {
                  var1[64 + var3] = true;
               }
            }
         }
      }

      for (int var4 = 0; var4 < 95; var4++) {
         if (var1[var4]) {
            this.jj_expentry = new int[1];
            this.jj_expentry[0] = var4;
            this.jj_expentries.add(this.jj_expentry);
         }
      }

      this.jj_endpos = 0;
      this.jj_rescan_token();
      this.jj_add_error_token(0, 0);
      int[][] var5 = new int[this.jj_expentries.size()][];

      for (int var6 = 0; var6 < this.jj_expentries.size(); var6++) {
         var5[var6] = (int[])this.jj_expentries.get(var6);
      }

      return new ParseException(this.token, var5, tokenImage);
   }

   public final void enable_tracing() {
   }

   public final void disable_tracing() {
   }

   private void jj_rescan_token() {
      this.jj_rescan = true;

      for (int var1 = 0; var1 < 7; var1++) {
         try {
            JJCalls var2 = this.jj_2_rtns[var1];

            while (true) {
               if (var2.gen > this.jj_gen) {
                  this.jj_la = var2.arg;
                  this.jj_lastpos = this.jj_scanpos = var2.first;
                  switch (var1) {
                     case 0:
                        this.jj_3_1();
                        break;
                     case 1:
                        this.jj_3_2();
                        break;
                     case 2:
                        this.jj_3_3();
                        break;
                     case 3:
                        this.jj_3_4();
                        break;
                     case 4:
                        this.jj_3_5();
                        break;
                     case 5:
                        this.jj_3_6();
                        break;
                     case 6:
                        this.jj_3_7();
                  }
               }

               var2 = var2.next;
               if (var2 == null) {
                  break;
               }
            }
         } catch (LookaheadSuccess var3) {
         }
      }

      this.jj_rescan = false;
   }

   private void jj_save(int var1, int var2) {
      JJCalls var3;
      for (var3 = this.jj_2_rtns[var1]; var3.gen > this.jj_gen; var3 = var3.next) {
         if (var3.next == null) {
            var3 = var3.next = new JJCalls();
            break;
         }
      }

      var3.gen = this.jj_gen + var2 - this.jj_la;
      var3.first = this.token;
      var3.arg = var2;
   }

   static {
      LuaValue.valueOf(true);
      jj_la1_init_0();
      jj_la1_init_1();
      jj_la1_init_2();
   }

   static final class JJCalls {
      int gen;
      Token first;
      int arg;
      JJCalls next;
   }

   private static final class LookaheadSuccess extends Error {
      private LookaheadSuccess() {
      }
   }
}
