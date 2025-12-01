package dev.inteleonyx.armandillo.api.luaj.luajc;

import dev.inteleonyx.armandillo.api.luaj.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Hashtable;

public class ProtoInfo {
   public final String name;
   public final Prototype prototype;
   public final ProtoInfo[] subprotos;
   public final BasicBlock[] blocks;
   public final BasicBlock[] blocklist;
   public final VarInfo[] params;
   public final VarInfo[][] vars;
   public final UpvalInfo[] upvals;
   public final UpvalInfo[][] openups;

   public ProtoInfo(Prototype var1, String var2) {
      this(var1, var2, null);
   }

   private ProtoInfo(Prototype var1, String var2, UpvalInfo[] var3) {
      this.name = var2;
      this.prototype = var1;
      this.upvals = var3 != null ? var3 : new UpvalInfo[]{new UpvalInfo(this)};
      this.subprotos = var1.p != null && var1.p.length > 0 ? new ProtoInfo[var1.p.length] : null;
      this.blocks = BasicBlock.findBasicBlocks(var1);
      this.blocklist = BasicBlock.findLiveBlocks(this.blocks);
      this.params = new VarInfo[var1.maxstacksize];

      for (int var4 = 0; var4 < var1.maxstacksize; var4++) {
         VarInfo var5 = VarInfo.PARAM(var4);
         this.params[var4] = var5;
      }

      this.vars = this.findVariables();
      this.replaceTrivialPhiVariables();
      this.openups = new UpvalInfo[var1.maxstacksize][];
      this.findUpvalues();
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("proto '" + this.name + "'\n");
      int var2 = 0;

      for (int var3 = this.upvals != null ? this.upvals.length : 0; var2 < var3; var2++) {
         var1.append(" up[" + var2 + "]: " + this.upvals[var2] + "\n");
      }

      for (int var13 = 0; var13 < this.blocklist.length; var13++) {
         BasicBlock var15 = this.blocklist[var13];
         int var4 = var15.pc0;
         var1.append("  block " + var15.toString());
         this.appendOpenUps(var1, -1);

         for (int var5 = var4; var5 <= var15.pc1; var5++) {
            this.appendOpenUps(var1, var5);
            var1.append("     ");

            for (int var6 = 0; var6 < this.prototype.maxstacksize; var6++) {
               VarInfo var7 = this.vars[var6][var5];
               String var8 = var7 == null
                  ? ""
                  : (var7.upvalue != null ? (!var7.upvalue.rw ? "[C] " : (var7.allocupvalue && var7.pc == var5 ? "[*] " : "[]  ")) : "    ");
               String var9 = var7 == null ? "null   " : String.valueOf(var7);
               var1.append(var9 + var8);
            }

            var1.append("  ");
            ByteArrayOutputStream var17 = new ByteArrayOutputStream();
            PrintStream var18 = Print.ps;
            Print.ps = new PrintStream(var17);

            try {
               Print.printOpCode(this.prototype, var5);
            } finally {
               Print.ps.close();
               Print.ps = var18;
            }

            var1.append(var17.toString());
            var1.append("\n");
         }
      }

      var2 = 0;

      for (int var16 = this.subprotos != null ? this.subprotos.length : 0; var2 < var16; var2++) {
         var1.append(this.subprotos[var2].toString());
      }

      return var1.toString();
   }

   private void appendOpenUps(StringBuffer var1, int var2) {
      for (int var3 = 0; var3 < this.prototype.maxstacksize; var3++) {
         VarInfo var4 = var2 < 0 ? this.params[var3] : this.vars[var3][var2];
         if (var4 != null && var4.pc == var2 && var4.allocupvalue) {
            var1.append("    open: " + var4.upvalue + "\n");
         }
      }
   }

   private VarInfo[][] findVariables() {
      int var1 = this.prototype.code.length;
      int var2 = this.prototype.maxstacksize;
      VarInfo[][] var3 = new VarInfo[var2][];

      for (int var4 = 0; var4 < var3.length; var4++) {
         var3[var4] = new VarInfo[var1];
      }

      for (int var16 = 0; var16 < this.blocklist.length; var16++) {
         BasicBlock var5 = this.blocklist[var16];
         int var6 = var5.prev != null ? var5.prev.length : 0;

         for (int var7 = 0; var7 < var2; var7++) {
            VarInfo var8 = null;
            if (var6 == 0) {
               var8 = this.params[var7];
            } else if (var6 == 1) {
               var8 = var3[var7][var5.prev[0].pc1];
            } else {
               for (int var9 = 0; var9 < var6; var9++) {
                  BasicBlock var10 = var5.prev[var9];
                  if (var3[var7][var10.pc1] == VarInfo.INVALID) {
                     var8 = VarInfo.INVALID;
                  }
               }
            }

            if (var8 == null) {
               var8 = VarInfo.PHI(this, var7, var5.pc0);
            }

            var3[var7][var5.pc0] = var8;
         }

         for (int var17 = var5.pc0; var17 <= var5.pc1; var17++) {
            if (var17 > var5.pc0) {
               propogateVars(var3, var17 - 1, var17);
            }

            int var11 = this.prototype.code[var17];
            int var12 = Lua.GET_OPCODE(var11);
            switch (var12) {
               case 0:
               case 19:
               case 20:
               case 21:
               case 28:
                  int var42 = Lua.GETARG_A(var11);
                  int var57 = Lua.GETARG_B(var11);
                  var3[var57][var17].isreferenced = true;
                  var3[var42][var17] = new VarInfo(var42, var17);
                  break;
               case 1:
               case 3:
               case 5:
               case 11:
                  int var41 = Lua.GETARG_A(var11);
                  var3[var41][var17] = new VarInfo(var41, var17);
                  break;
               case 2:
               default:
                  throw new IllegalStateException("unhandled opcode: " + var11);
               case 4:
                  int var40 = Lua.GETARG_A(var11);

                  for (int var56 = Lua.GETARG_B(var11); var56-- >= 0; var40++) {
                     var3[var40][var17] = new VarInfo(var40, var17);
                  }
                  break;
               case 6:
                  int var39 = Lua.GETARG_A(var11);
                  int var67 = Lua.GETARG_C(var11);
                  if (!Lua.ISK(var67)) {
                     var3[var67][var17].isreferenced = true;
                  }

                  var3[var39][var17] = new VarInfo(var39, var17);
                  break;
               case 7:
                  int var38 = Lua.GETARG_A(var11);
                  int var55 = Lua.GETARG_B(var11);
                  int var66 = Lua.GETARG_C(var11);
                  var3[var55][var17].isreferenced = true;
                  if (!Lua.ISK(var66)) {
                     var3[var66][var17].isreferenced = true;
                  }

                  var3[var38][var17] = new VarInfo(var38, var17);
                  break;
               case 8:
                  int var54 = Lua.GETARG_B(var11);
                  int var65 = Lua.GETARG_C(var11);
                  if (!Lua.ISK(var54)) {
                     var3[var54][var17].isreferenced = true;
                  }

                  if (!Lua.ISK(var65)) {
                     var3[var65][var17].isreferenced = true;
                  }
                  break;
               case 9:
               case 27:
                  int var37 = Lua.GETARG_A(var11);
                  var3[var37][var17].isreferenced = true;
                  break;
               case 10:
                  int var36 = Lua.GETARG_A(var11);
                  int var53 = Lua.GETARG_B(var11);
                  int var64 = Lua.GETARG_C(var11);
                  var3[var36][var17].isreferenced = true;
                  if (!Lua.ISK(var53)) {
                     var3[var53][var17].isreferenced = true;
                  }

                  if (!Lua.ISK(var64)) {
                     var3[var64][var17].isreferenced = true;
                  }
                  break;
               case 12:
                  int var35 = Lua.GETARG_A(var11);
                  int var52 = Lua.GETARG_B(var11);
                  int var63 = Lua.GETARG_C(var11);
                  var3[var52][var17].isreferenced = true;
                  if (!Lua.ISK(var63)) {
                     var3[var63][var17].isreferenced = true;
                  }

                  var3[var35][var17] = new VarInfo(var35, var17);
                  var3[var35 + 1][var17] = new VarInfo(var35 + 1, var17);
                  break;
               case 13:
               case 14:
               case 15:
               case 16:
               case 17:
               case 18:
                  int var34 = Lua.GETARG_A(var11);
                  int var51 = Lua.GETARG_B(var11);
                  int var62 = Lua.GETARG_C(var11);
                  if (!Lua.ISK(var51)) {
                     var3[var51][var17].isreferenced = true;
                  }

                  if (!Lua.ISK(var62)) {
                     var3[var62][var17].isreferenced = true;
                  }

                  var3[var34][var17] = new VarInfo(var34, var17);
                  break;
               case 22:
                  int var33 = Lua.GETARG_A(var11);
                  int var50 = Lua.GETARG_B(var11);

                  for (int var61 = Lua.GETARG_C(var11); var50 <= var61; var50++) {
                     var3[var50][var17].isreferenced = true;
                  }

                  var3[var33][var17] = new VarInfo(var33, var17);
                  break;
               case 23:
                  int var31 = Lua.GETARG_A(var11);
                  if (var31 > 0) {
                     var31--;

                     while (var31 < var2) {
                        var3[var31][var17] = VarInfo.INVALID;
                        var31++;
                     }
                  }
                  break;
               case 24:
               case 25:
               case 26:
                  int var49 = Lua.GETARG_B(var11);
                  int var60 = Lua.GETARG_C(var11);
                  if (!Lua.ISK(var49)) {
                     var3[var49][var17].isreferenced = true;
                  }

                  if (!Lua.ISK(var60)) {
                     var3[var60][var17].isreferenced = true;
                  }
                  break;
               case 29:
                  int var30 = Lua.GETARG_A(var11);
                  int var48 = Lua.GETARG_B(var11);
                  int var59 = Lua.GETARG_C(var11);
                  var3[var30][var17].isreferenced = true;
                  var3[var30][var17].isreferenced = true;

                  for (int var73 = 1; var73 <= var48 - 1; var73++) {
                     var3[var30 + var73][var17].isreferenced = true;
                  }

                  for (int var74 = 0; var74 <= var59 - 2; var30++) {
                     var3[var30][var17] = new VarInfo(var30, var17);
                     var74++;
                  }

                  while (var30 < var2) {
                     var3[var30][var17] = VarInfo.INVALID;
                     var30++;
                  }
                  break;
               case 30:
                  int var29 = Lua.GETARG_A(var11);
                  int var47 = Lua.GETARG_B(var11);
                  var3[var29][var17].isreferenced = true;

                  for (int var72 = 1; var72 <= var47 - 1; var72++) {
                     var3[var29 + var72][var17].isreferenced = true;
                  }
                  break;
               case 31:
                  int var28 = Lua.GETARG_A(var11);
                  int var46 = Lua.GETARG_B(var11);

                  for (int var71 = 0; var71 <= var46 - 2; var71++) {
                     var3[var28 + var71][var17].isreferenced = true;
                  }
                  break;
               case 32:
                  int var27 = Lua.GETARG_A(var11);
                  var3[var27][var17].isreferenced = true;
                  var3[var27 + 2][var17].isreferenced = true;
                  var3[var27][var17] = new VarInfo(var27, var17);
                  var3[var27][var17].isreferenced = true;
                  var3[var27 + 1][var17].isreferenced = true;
                  var3[var27 + 3][var17] = new VarInfo(var27 + 3, var17);
                  break;
               case 33:
                  int var26 = Lua.GETARG_A(var11);
                  var3[var26 + 2][var17].isreferenced = true;
                  var3[var26][var17] = new VarInfo(var26, var17);
                  break;
               case 34:
                  int var22 = Lua.GETARG_A(var11);
                  int var58 = Lua.GETARG_C(var11);
                  var3[var22++][var17].isreferenced = true;
                  var3[var22++][var17].isreferenced = true;
                  var3[var22++][var17].isreferenced = true;

                  for (int var70 = 0; var70 < var58; var22++) {
                     var3[var22][var17] = new VarInfo(var22, var17);
                     var70++;
                  }

                  while (var22 < var2) {
                     var3[var22][var17] = VarInfo.INVALID;
                     var22++;
                  }
                  break;
               case 35:
                  int var21 = Lua.GETARG_A(var11);
                  var3[var21 + 1][var17].isreferenced = true;
                  var3[var21][var17] = new VarInfo(var21, var17);
                  break;
               case 36:
                  int var20 = Lua.GETARG_A(var11);
                  int var45 = Lua.GETARG_B(var11);
                  var3[var20][var17].isreferenced = true;

                  for (int var69 = 1; var69 <= var45; var69++) {
                     var3[var20 + var69][var17].isreferenced = true;
                  }
                  break;
               case 37:
                  int var19 = Lua.GETARG_A(var11);
                  int var44 = Lua.GETARG_Bx(var11);
                  Upvaldesc[] var68 = this.prototype.p[var44].upvalues;
                  int var14 = 0;

                  for (int var15 = var68.length; var14 < var15; var14++) {
                     if (var68[var14].instack) {
                        var3[var68[var14].idx][var17].isreferenced = true;
                     }
                  }

                  var3[var19][var17] = new VarInfo(var19, var17);
                  break;
               case 38:
                  int var18 = Lua.GETARG_A(var11);
                  int var43 = Lua.GETARG_B(var11);

                  for (int var13 = 1; var13 < var43; var18++) {
                     var3[var18][var17] = new VarInfo(var18, var17);
                     var13++;
                  }

                  if (var43 == 0) {
                     while (var18 < var2) {
                        var3[var18][var17] = VarInfo.INVALID;
                        var18++;
                     }
                  }
            }
         }
      }

      return var3;
   }

   private static void propogateVars(VarInfo[][] var0, int var1, int var2) {
      int var3 = 0;

      for (int var4 = var0.length; var3 < var4; var3++) {
         var0[var3][var2] = var0[var3][var1];
      }
   }

   private void replaceTrivialPhiVariables() {
      for (int var1 = 0; var1 < this.blocklist.length; var1++) {
         BasicBlock var2 = this.blocklist[var1];

         for (int var3 = 0; var3 < this.prototype.maxstacksize; var3++) {
            VarInfo var4 = this.vars[var3][var2.pc0];
            VarInfo var5 = var4.resolvePhiVariableValues();
            if (var5 != null) {
               this.substituteVariable(var3, var4, var5);
            }
         }
      }
   }

   private void substituteVariable(int var1, VarInfo var2, VarInfo var3) {
      int var4 = 0;

      for (int var5 = this.prototype.code.length; var4 < var5; var4++) {
         this.replaceAll(this.vars[var1], this.vars[var1].length, var2, var3);
      }
   }

   private void replaceAll(VarInfo[] var1, int var2, VarInfo var3, VarInfo var4) {
      for (int var5 = 0; var5 < var2; var5++) {
         if (var1[var5] == var3) {
            var1[var5] = var4;
         }
      }
   }

   private void findUpvalues() {
      int[] var1 = this.prototype.code;
      int var2 = var1.length;
      String[] var3 = this.findInnerprotoNames();

      for (int var4 = 0; var4 < var2; var4++) {
         if (Lua.GET_OPCODE(var1[var4]) == 37) {
            int var5 = Lua.GETARG_Bx(var1[var4]);
            Prototype var6 = this.prototype.p[var5];
            UpvalInfo[] var7 = new UpvalInfo[var6.upvalues.length];
            String var8 = this.name + "$" + var3[var5];

            for (int var9 = 0; var9 < var6.upvalues.length; var9++) {
               Upvaldesc var10 = var6.upvalues[var9];
               var7[var9] = var10.instack ? this.findOpenUp(var4, var10.idx) : this.upvals[var10.idx];
            }

            this.subprotos[var5] = new ProtoInfo(var6, var8, var7);
         }
      }

      for (int var11 = 0; var11 < var2; var11++) {
         if (Lua.GET_OPCODE(var1[var11]) == 9) {
            this.upvals[Lua.GETARG_B(var1[var11])].rw = true;
         }
      }
   }

   private UpvalInfo findOpenUp(int var1, int var2) {
      if (this.openups[var2] == null) {
         this.openups[var2] = new UpvalInfo[this.prototype.code.length];
      }

      if (this.openups[var2][var1] != null) {
         return this.openups[var2][var1];
      } else {
         UpvalInfo var3 = new UpvalInfo(this, var1, var2);
         int var4 = 0;

         for (int var5 = this.prototype.code.length; var4 < var5; var4++) {
            if (this.vars[var2][var4] != null && this.vars[var2][var4].upvalue == var3) {
               this.openups[var2][var4] = var3;
            }
         }

         return var3;
      }
   }

   public boolean isUpvalueAssign(int var1, int var2) {
      VarInfo var3 = var1 < 0 ? this.params[var2] : this.vars[var2][var1];
      return var3 != null && var3.upvalue != null && var3.upvalue.rw;
   }

   public boolean isUpvalueCreate(int var1, int var2) {
      VarInfo var3 = var1 < 0 ? this.params[var2] : this.vars[var2][var1];
      return var3 != null && var3.upvalue != null && var3.upvalue.rw && var3.allocupvalue && var1 == var3.pc;
   }

   public boolean isUpvalueRefer(int var1, int var2) {
      if (var1 > 0 && this.vars[var2][var1] != null && this.vars[var2][var1].pc == var1 && this.vars[var2][var1 - 1] != null) {
         var1--;
      }

      VarInfo var3 = var1 < 0 ? this.params[var2] : this.vars[var2][var1];
      return var3 != null && var3.upvalue != null && var3.upvalue.rw;
   }

   public boolean isInitialValueUsed(int var1) {
      VarInfo var2 = this.params[var1];
      return var2.isreferenced;
   }

   public boolean isReadWriteUpvalue(UpvalInfo var1) {
      return var1.rw;
   }

   private String[] findInnerprotoNames() {
      if (this.prototype.p.length <= 0) {
         return null;
      } else {
         String[] var1 = new String[this.prototype.p.length];
         Hashtable var2 = new Hashtable();
         int[] var3 = this.prototype.code;
         int var4 = var3.length;

         for (int var5 = 0; var5 < var4; var5++) {
            if (Lua.GET_OPCODE(var3[var5]) == 37) {
               int var6 = Lua.GETARG_Bx(var3[var5]);
               String var7 = null;
               int var8 = var3[var5 + 1];
               switch (Lua.GET_OPCODE(var8)) {
                  case 8:
                  case 10:
                     int var13 = Lua.GETARG_B(var8);
                     if (Lua.ISK(var13)) {
                        var7 = this.prototype.k[var13 & 0xFF].tojstring();
                     }
                     break;
                  case 9:
                     int var12 = Lua.GETARG_B(var8);
                     LuaString var15 = this.prototype.upvalues[var12].name;
                     if (var15 != null) {
                        var7 = var15.tojstring();
                     }
                     break;
                  default:
                     int var9 = Lua.GETARG_A(var3[var5]);
                     LuaString var10 = this.prototype.getlocalname(var9 + 1, var5 + 1);
                     if (var10 != null) {
                        var7 = var10.tojstring();
                     }
               }

               var7 = var7 != null ? toJavaClassPart(var7) : String.valueOf(var6);
               if (var2.containsKey(var7)) {
                  String var14 = var7;
                  int var16 = 1;

                  do {
                     var7 = var14 + '$' + var16++;
                  } while (var2.containsKey(var7));
               }

               var2.put(var7, Boolean.TRUE);
               var1[var6] = var7;
            }
         }

         return var1;
      }
   }

   private static String toJavaClassPart(String var0) {
      int var1 = var0.length();
      StringBuffer var2 = new StringBuffer(var1);

      for (int var3 = 0; var3 < var1; var3++) {
         var2.append(Character.isJavaIdentifierPart(var0.charAt(var3)) ? var0.charAt(var3) : '_');
      }

      return var2.toString();
   }
}
