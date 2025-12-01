package dev.inteleonyx.armandillo.api.luaj.luajc;

import dev.inteleonyx.armandillo.api.luaj.LocVars;
import dev.inteleonyx.armandillo.api.luaj.Lua;
import dev.inteleonyx.armandillo.api.luaj.Prototype;
import dev.inteleonyx.armandillo.api.luaj.Upvaldesc;

public class JavaGen {
   public final String classname;
   public final byte[] bytecode;
   public final JavaGen[] inners;

   public JavaGen(Prototype var1, String var2, String var3, boolean var4) {
      this(new ProtoInfo(var1, var2), var2, var3, var4);
   }

   private JavaGen(ProtoInfo var1, String var2, String var3, boolean var4) {
      this.classname = var2;
      JavaBuilder var5 = new JavaBuilder(var1, var2, var3);
      this.scanInstructions(var1, var2, var5);

      for (int var6 = 0; var6 < var1.prototype.locvars.length; var6++) {
         LocVars var7 = var1.prototype.locvars[var6];
         var5.setVarStartEnd(var6, var7.startpc, var7.endpc, var7.varname.tojstring());
      }

      this.bytecode = var5.completeClass(var4);
      if (var1.subprotos != null) {
         int var8 = var1.subprotos.length;
         this.inners = new JavaGen[var8];

         for (int var9 = 0; var9 < var8; var9++) {
            this.inners[var9] = new JavaGen(var1.subprotos[var9], var1.subprotos[var9].name, var3, false);
         }
      } else {
         this.inners = null;
      }
   }

   private void scanInstructions(ProtoInfo var1, String var2, JavaBuilder var3) {
      Prototype var4 = var1.prototype;
      int var5 = -1;

      for (int var6 = 0; var6 < var1.blocklist.length; var6++) {
         BasicBlock var7 = var1.blocklist[var6];

         for (int var8 = 0; var8 < var4.maxstacksize; var8++) {
            int var9 = var7.pc0;
            boolean var10 = var1.isUpvalueCreate(var9, var8);
            if (var10 && var1.vars[var8][var9].isPhiVar()) {
               var3.convertToUpvalue(var9, var8);
            }
         }

         for (int var24 = var7.pc0; var24 <= var7.pc1; var24++) {
            int var11;
            int var25 = var4.code[var24];
            var11 = var24 < var4.lineinfo.length ? var4.lineinfo[var24] : -1;
            int var12 = Lua.GET_OPCODE(var25);
            int var13 = Lua.GETARG_A(var25);
            int var14 = Lua.GETARG_B(var25);
            int var15 = Lua.GETARG_Bx(var25);
            int var16 = Lua.GETARG_sBx(var25);
            int var17 = Lua.GETARG_C(var25);
            label234:
            switch (var12) {
               case 0:
                  var3.loadLocal(var24, var14);
                  var3.storeLocal(var24, var13);
                  break;
               case 1:
                  var3.loadConstant(var4.k[var15]);
                  var3.storeLocal(var24, var13);
               case 2:
               default:
                  break;
               case 3:
                  var3.loadBoolean(var14 != 0);
                  var3.storeLocal(var24, var13);
                  if (var17 != 0) {
                     var3.addBranch(var24, 1, var24 + 2);
                  }
                  break;
               case 4:
                  var3.loadNil();

                  while (var14 >= 0) {
                     if (var14 > 0) {
                        var3.dup();
                     }

                     var3.storeLocal(var24, var13);
                     var13++;
                     var14--;
                  }
                  break;
               case 5:
                  var3.loadUpvalue(var14);
                  var3.storeLocal(var24, var13);
                  break;
               case 6:
                  var3.loadUpvalue(var14);
                  this.loadLocalOrConstant(var4, var3, var24, var17);
                  var3.getTable();
                  var3.storeLocal(var24, var13);
                  break;
               case 7:
                  var3.loadLocal(var24, var14);
                  this.loadLocalOrConstant(var4, var3, var24, var17);
                  var3.getTable();
                  var3.storeLocal(var24, var13);
                  break;
               case 8:
                  var3.loadUpvalue(var13);
                  this.loadLocalOrConstant(var4, var3, var24, var14);
                  this.loadLocalOrConstant(var4, var3, var24, var17);
                  var3.setTable();
                  break;
               case 9:
                  var3.storeUpvalue(var24, var14, var13);
                  break;
               case 10:
                  var3.loadLocal(var24, var13);
                  this.loadLocalOrConstant(var4, var3, var24, var14);
                  this.loadLocalOrConstant(var4, var3, var24, var17);
                  var3.setTable();
                  break;
               case 11:
                  var3.newTable(var14, var17);
                  var3.storeLocal(var24, var13);
                  break;
               case 12:
                  var3.loadLocal(var24, var14);
                  var3.dup();
                  var3.storeLocal(var24, var13 + 1);
                  this.loadLocalOrConstant(var4, var3, var24, var17);
                  var3.getTable();
                  var3.storeLocal(var24, var13);
                  break;
               case 13:
               case 14:
               case 15:
               case 16:
               case 17:
               case 18:
                  this.loadLocalOrConstant(var4, var3, var24, var14);
                  this.loadLocalOrConstant(var4, var3, var24, var17);
                  var3.binaryop(var12);
                  var3.storeLocal(var24, var13);
                  break;
               case 19:
               case 20:
               case 21:
                  var3.loadLocal(var24, var14);
                  var3.unaryop(var12);
                  var3.storeLocal(var24, var13);
                  break;
               case 22:
                  for (int var29 = var14; var29 <= var17; var29++) {
                     var3.loadLocal(var24, var29);
                  }

                  if (var17 > var14 + 1) {
                     var3.tobuffer();
                     int var30 = var17;

                     while (--var30 >= var14) {
                        var3.concatbuffer();
                     }

                     var3.tovalue();
                  } else {
                     var3.concatvalue();
                  }

                  var3.storeLocal(var24, var13);
                  break;
               case 23:
                  if (var13 > 0) {
                     for (int var28 = var13 - 1; var28 < var1.openups.length; var28++) {
                        var3.closeUpvalue(var24, var28);
                     }
                  }

                  var3.addBranch(var24, 1, var24 + 1 + var16);
                  break;
               case 24:
               case 25:
               case 26:
                  this.loadLocalOrConstant(var4, var3, var24, var14);
                  this.loadLocalOrConstant(var4, var3, var24, var17);
                  var3.compareop(var12);
                  var3.addBranch(var24, var13 != 0 ? 3 : 2, var24 + 2);
                  break;
               case 27:
                  var3.loadLocal(var24, var13);
                  var3.toBoolean();
                  var3.addBranch(var24, var17 != 0 ? 3 : 2, var24 + 2);
                  break;
               case 28:
                  var3.loadLocal(var24, var14);
                  var3.toBoolean();
                  var3.addBranch(var24, var17 != 0 ? 3 : 2, var24 + 2);
                  var3.loadLocal(var24, var14);
                  var3.storeLocal(var24, var13);
                  break;
               case 29:
                  var3.loadLocal(var24, var13);
                  int var27 = var14 - 1;
                  switch (var27) {
                     case -1:
                        this.loadVarargResults(var3, var24, var13 + 1, var5);
                        var27 = -1;
                        break;
                     case 0:
                     case 1:
                     case 2:
                     case 3:
                        for (int var33 = 1; var33 < var14; var33++) {
                           var3.loadLocal(var24, var13 + var33);
                        }
                        break;
                     default:
                        var3.newVarargs(var24, var13 + 1, var14 - 1);
                        var27 = -1;
                  }

                  boolean var34 = var27 < 0 || var17 < 1 || var17 > 2;
                  if (var34) {
                     var3.invoke(var27);
                  } else {
                     var3.call(var27);
                  }

                  switch (var17) {
                     case 0:
                        var5 = var13;
                        var3.storeVarresult();
                        break label234;
                     case 1:
                        var3.pop();
                        break label234;
                     case 2:
                        if (var34) {
                           var3.arg(1);
                        }

                        var3.storeLocal(var24, var13);
                        break label234;
                     default:
                        int var35 = 1;

                        while (true) {
                           if (var35 >= var17) {
                              break label234;
                           }

                           if (var35 + 1 < var17) {
                              var3.dup();
                           }

                           var3.arg(var35);
                           var3.storeLocal(var24, var13 + var35 - 1);
                           var35++;
                        }
                  }
               case 30:
                  var3.loadLocal(var24, var13);
                  switch (var14) {
                     case 0:
                        this.loadVarargResults(var3, var24, var13 + 1, var5);
                        break;
                     case 1:
                        var3.loadNone();
                        break;
                     case 2:
                        var3.loadLocal(var24, var13 + 1);
                        break;
                     default:
                        var3.newVarargs(var24, var13 + 1, var14 - 1);
                  }

                  var3.newTailcallVarargs();
                  var3.areturn();
                  break;
               case 31:
                  if (var17 == 1) {
                     var3.loadNone();
                  } else {
                     switch (var14) {
                        case 0:
                           this.loadVarargResults(var3, var24, var13, var5);
                           break;
                        case 1:
                           var3.loadNone();
                           break;
                        case 2:
                           var3.loadLocal(var24, var13);
                           break;
                        default:
                           var3.newVarargs(var24, var13, var14 - 1);
                     }
                  }

                  var3.areturn();
                  break;
               case 32:
                  var3.loadLocal(var24, var13);
                  var3.loadLocal(var24, var13 + 2);
                  var3.binaryop(13);
                  var3.dup();
                  var3.dup();
                  var3.storeLocal(var24, var13);
                  var3.storeLocal(var24, var13 + 3);
                  var3.loadLocal(var24, var13 + 1);
                  var3.loadLocal(var24, var13 + 2);
                  var3.testForLoop();
                  var3.addBranch(var24, 2, var24 + 1 + var16);
                  break;
               case 33:
                  var3.loadLocal(var24, var13);
                  var3.loadLocal(var24, var13 + 2);
                  var3.binaryop(14);
                  var3.storeLocal(var24, var13);
                  var3.addBranch(var24, 1, var24 + 1 + var16);
                  break;
               case 34:
                  var3.loadLocal(var24, var13);
                  var3.loadLocal(var24, var13 + 1);
                  var3.loadLocal(var24, var13 + 2);
                  var3.invoke(2);

                  for (int var26 = 1; var26 <= var17; var26++) {
                     if (var26 < var17) {
                        var3.dup();
                     }

                     var3.arg(var26);
                     var3.storeLocal(var24, var13 + 2 + var26);
                  }
                  break;
               case 35:
                  var3.loadLocal(var24, var13 + 1);
                  var3.dup();
                  var3.storeLocal(var24, var13);
                  var3.isNil();
                  var3.addBranch(var24, 3, var24 + 1 + var16);
                  break;
               case 36:
                  int var18 = (var17 - 1) * 50 + 1;
                  var3.loadLocal(var24, var13);
                  if (var14 == 0) {
                     int var32 = var5 - (var13 + 1);
                     if (var32 > 0) {
                        var3.setlistStack(var24, var13 + 1, var18, var32);
                        var18 += var32;
                     }

                     var3.setlistVarargs(var18, var5);
                  } else {
                     var3.setlistStack(var24, var13 + 1, var18, var14);
                     var3.pop();
                  }
                  break;
               case 37:
                  Prototype var31 = var4.p[var15];
                  int var20 = var31.upvalues.length;
                  String var21 = var1.subprotos[var15].name;
                  var3.closureCreate(var21);
                  if (var20 > 0) {
                     var3.dup();
                  }

                  var3.storeLocal(var24, var13);

                  for (int var22 = 0; var22 < var20; var22++) {
                     if (var22 + 1 < var20) {
                        var3.dup();
                     }

                     Upvaldesc var23 = var31.upvalues[var22];
                     if (var23.instack) {
                        var3.closureInitUpvalueFromLocal(var21, var22, var24, var23.idx);
                     } else {
                        var3.closureInitUpvalueFromUpvalue(var21, var22, var23.idx);
                     }
                  }
                  break;
               case 38:
                  if (var14 == 0) {
                     var3.loadVarargs();
                     var3.storeVarresult();
                     var5 = var13;
                  } else {
                     for (int var19 = 1; var19 < var14; var19++) {
                        var3.loadVarargs(var19);
                        var3.storeLocal(var24, var13);
                        var13++;
                     }
                  }
            }

            var3.onEndOfLuaInstruction(var24, var11);
         }
      }
   }

   private void loadVarargResults(JavaBuilder var1, int var2, int var3, int var4) {
      if (var4 <= var3) {
         var1.loadVarresult();
         var1.subargs(var3 + 1 - var4);
      } else if (var4 == var3) {
         var1.loadVarresult();
      } else {
         var1.newVarargsVarresult(var2, var3, var4 - var3);
      }
   }

   private void loadLocalOrConstant(Prototype var1, JavaBuilder var2, int var3, int var4) {
      if (var4 <= 255) {
         var2.loadLocal(var3, var4);
      } else {
         var2.loadConstant(var1.k[var4 & 0xFF]);
      }
   }
}
