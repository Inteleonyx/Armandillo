package dev.inteleonyx.armandillo.api.luaj;

public class LuaClosure extends LuaFunction {
   private static final UpValue[] NOUPVALUES = new UpValue[0];
   public final Prototype p;
   public UpValue[] upValues;
   final Globals globals;

   public LuaClosure(Prototype var1, LuaValue var2) {
      this.p = var1;
      this.initupvalue1(var2);
      this.globals = var2 instanceof Globals ? (Globals)var2 : null;
   }

   public void initupvalue1(LuaValue var1) {
      if (this.p.upvalues != null && this.p.upvalues.length != 0) {
         this.upValues = new UpValue[this.p.upvalues.length];
         this.upValues[0] = new UpValue(new LuaValue[]{var1}, 0);
      } else {
         this.upValues = NOUPVALUES;
      }
   }

   public boolean isclosure() {
      return true;
   }

   public LuaClosure optclosure(LuaClosure var1) {
      return this;
   }

   public LuaClosure checkclosure() {
      return this;
   }

   public String tojstring() {
      return "function: " + this.p.toString();
   }

   private LuaValue[] getNewStack() {
      int var1 = this.p.maxstacksize;
      LuaValue[] var2 = new LuaValue[var1];
      System.arraycopy(NILS, 0, var2, 0, var1);
      return var2;
   }

   public final LuaValue call() {
      LuaValue[] var1 = this.getNewStack();
      return this.execute(var1, NONE).arg1();
   }

   public final LuaValue call(LuaValue var1) {
      LuaValue[] var2 = this.getNewStack();
      switch (this.p.numparams) {
         case 0:
            return this.execute(var2, var1).arg1();
         default:
            var2[0] = var1;
            return this.execute(var2, NONE).arg1();
      }
   }

   public final LuaValue call(LuaValue var1, LuaValue var2) {
      LuaValue[] var3 = this.getNewStack();
      switch (this.p.numparams) {
         case 0:
            return this.execute(var3, (Varargs)(this.p.is_vararg != 0 ? varargsOf(var1, var2) : NONE)).arg1();
         case 1:
            var3[0] = var1;
            return this.execute(var3, var2).arg1();
         default:
            var3[0] = var1;
            var3[1] = var2;
            return this.execute(var3, NONE).arg1();
      }
   }

   public final LuaValue call(LuaValue var1, LuaValue var2, LuaValue var3) {
      LuaValue[] var4 = this.getNewStack();
      switch (this.p.numparams) {
         case 0:
            return this.execute(var4, (Varargs)(this.p.is_vararg != 0 ? varargsOf(var1, var2, var3) : NONE)).arg1();
         case 1:
            var4[0] = var1;
            return this.execute(var4, (Varargs)(this.p.is_vararg != 0 ? varargsOf(var2, var3) : NONE)).arg1();
         case 2:
            var4[0] = var1;
            var4[1] = var2;
            return this.execute(var4, var3).arg1();
         default:
            var4[0] = var1;
            var4[1] = var2;
            var4[2] = var3;
            return this.execute(var4, NONE).arg1();
      }
   }

   public final Varargs invoke(Varargs var1) {
      return this.onInvoke(var1).eval();
   }

   public final Varargs onInvoke(Varargs var1) {
      LuaValue[] var2 = this.getNewStack();

      for (int var3 = 0; var3 < this.p.numparams; var3++) {
         var2[var3] = var1.arg(var3 + 1);
      }

      return this.execute(var2, (Varargs)(this.p.is_vararg != 0 ? var1.subargs(this.p.numparams + 1) : NONE));
   }

   // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   protected Varargs execute(LuaValue[] var1, Varargs var2) {
      int var7 = 0;
      int var8 = 0;
      Varargs var10 = NONE;
      int[] var11 = this.p.code;
      LuaValue[] var12 = this.p.k;
      UpValue[] var13 = this.p.p.length > 0 ? new UpValue[var1.length] : null;
      if (this.globals != null && this.globals.debuglib != null) {
         this.globals.debuglib.onCall(this, var2, var1);
      }

      while (true) {
         boolean var24 = false /* VF: Semaphore variable */;

         Varargs var75;
         label975: {
            label976: {
               label977: {
                  label978: {
                     label979: {
                        label980: {
                           label981: {
                              label982: {
                                 try {
                                    label1040: {
                                       var24 = true;
                                       if (this.globals != null && this.globals.debuglib != null) {
                                          this.globals.debuglib.onInstruction(var7, var10, var8);
                                       }

                                       int var3 = var11[var7];
                                       int var4 = var3 >> 6 & 0xFF;
                                       label946:
                                       switch (var3 & 63) {
                                          case 0:
                                             var1[var4] = var1[var3 >>> 23];
                                             break;
                                          case 1:
                                             var1[var4] = var12[var3 >>> 14];
                                             break;
                                          case 2:
                                          default:
                                             throw new IllegalArgumentException("Illegal opcode: " + (var3 & 63));
                                          case 3:
                                             var1[var4] = var3 >>> 23 != 0 ? LuaValue.TRUE : LuaValue.FALSE;
                                             if ((var3 & 8372224) != 0) {
                                                var7++;
                                             }
                                             break;
                                          case 4:
                                             int var47 = var3 >>> 23;

                                             while (var47-- >= 0) {
                                                var1[var4++] = LuaValue.NIL;
                                             }
                                             break;
                                          case 5:
                                             var1[var4] = this.upValues[var3 >>> 23].getValue();
                                             break;
                                          case 6:
                                             int var64;
                                             var1[var4] = this.upValues[var3 >>> 23]
                                                .getValue()
                                                .get((var64 = var3 >> 14 & 511) > 255 ? var12[var64 & 0xFF] : var1[var64]);
                                             break;
                                          case 7:
                                             int var63;
                                             var1[var4] = var1[var3 >>> 23].get((var63 = var3 >> 14 & 511) > 255 ? var12[var63 & 0xFF] : var1[var63]);
                                             break;
                                          case 8:
                                             int var46;
                                             int var62;
                                             this.upValues[var4]
                                                .getValue()
                                                .set(
                                                   (var46 = var3 >>> 23) > 255 ? var12[var46 & 0xFF] : var1[var46],
                                                   (var62 = var3 >> 14 & 511) > 255 ? var12[var62 & 0xFF] : var1[var62]
                                                );
                                             break;
                                          case 9:
                                             this.upValues[var3 >>> 23].setValue(var1[var4]);
                                             break;
                                          case 10:
                                             int var45;
                                             int var61;
                                             var1[var4]
                                                .set(
                                                   (var45 = var3 >>> 23) > 255 ? var12[var45 & 0xFF] : var1[var45],
                                                   (var61 = var3 >> 14 & 511) > 255 ? var12[var61 & 0xFF] : var1[var61]
                                                );
                                             break;
                                          case 11:
                                             var1[var4] = new LuaTable(var3 >>> 23, var3 >> 14 & 511);
                                             break;
                                          case 12:
                                             LuaValue var66;
                                             var1[var4 + 1] = var66 = var1[var3 >>> 23];
                                             int var60;
                                             var1[var4] = var66.get((var60 = var3 >> 14 & 511) > 255 ? var12[var60 & 0xFF] : var1[var60]);
                                             break;
                                          case 13:
                                             int var44;
                                             int var59;
                                             var1[var4] = ((var44 = var3 >>> 23) > 255 ? var12[var44 & 0xFF] : var1[var44])
                                                .add((var59 = var3 >> 14 & 511) > 255 ? var12[var59 & 0xFF] : var1[var59]);
                                             break;
                                          case 14:
                                             int var43;
                                             int var58;
                                             var1[var4] = ((var43 = var3 >>> 23) > 255 ? var12[var43 & 0xFF] : var1[var43])
                                                .sub((var58 = var3 >> 14 & 511) > 255 ? var12[var58 & 0xFF] : var1[var58]);
                                             break;
                                          case 15:
                                             int var42;
                                             int var57;
                                             var1[var4] = ((var42 = var3 >>> 23) > 255 ? var12[var42 & 0xFF] : var1[var42])
                                                .mul((var57 = var3 >> 14 & 511) > 255 ? var12[var57 & 0xFF] : var1[var57]);
                                             break;
                                          case 16:
                                             int var41;
                                             int var56;
                                             var1[var4] = ((var41 = var3 >>> 23) > 255 ? var12[var41 & 0xFF] : var1[var41])
                                                .div((var56 = var3 >> 14 & 511) > 255 ? var12[var56 & 0xFF] : var1[var56]);
                                             break;
                                          case 17:
                                             int var40;
                                             int var55;
                                             var1[var4] = ((var40 = var3 >>> 23) > 255 ? var12[var40 & 0xFF] : var1[var40])
                                                .mod((var55 = var3 >> 14 & 511) > 255 ? var12[var55 & 0xFF] : var1[var55]);
                                             break;
                                          case 18:
                                             int var39;
                                             int var54;
                                             var1[var4] = ((var39 = var3 >>> 23) > 255 ? var12[var39 & 0xFF] : var1[var39])
                                                .pow((var54 = var3 >> 14 & 511) > 255 ? var12[var54 & 0xFF] : var1[var54]);
                                             break;
                                          case 19:
                                             var1[var4] = var1[var3 >>> 23].neg();
                                             break;
                                          case 20:
                                             var1[var4] = var1[var3 >>> 23].not();
                                             break;
                                          case 21:
                                             var1[var4] = var1[var3 >>> 23].len();
                                             break;
                                          case 22:
                                             int var38 = var3 >>> 23;
                                             int var53 = var3 >> 14 & 511;
                                             if (var53 > var38 + 1) {
                                                Buffer var83 = var1[var53].buffer();

                                                while (--var53 >= var38) {
                                                   var83.concatTo(var1[var53]);
                                                }

                                                var1[var4] = var83.value();
                                             } else {
                                                var1[var4] = var1[var53 - 1].concat(var1[var53]);
                                             }
                                             break;
                                          case 23:
                                             var7 += (var3 >>> 14) - 131071;
                                             if (var4 > 0) {
                                                var4--;
                                                int var37 = var13.length;

                                                while (--var37 >= 0) {
                                                   if (var13[var37] != null && var13[var37].index >= var4) {
                                                      var13[var37].close();
                                                      var13[var37] = null;
                                                   }
                                                }
                                             }
                                             break;
                                          case 24:
                                             int var36;
                                             int var52;
                                             if (((var36 = var3 >>> 23) > 255 ? var12[var36 & 0xFF] : var1[var36])
                                                   .eq_b((var52 = var3 >> 14 & 511) > 255 ? var12[var52 & 0xFF] : var1[var52])
                                                != (var4 != 0)) {
                                                var7++;
                                             }
                                             break;
                                          case 25:
                                             int var35;
                                             int var51;
                                             if (((var35 = var3 >>> 23) > 255 ? var12[var35 & 0xFF] : var1[var35])
                                                   .lt_b((var51 = var3 >> 14 & 511) > 255 ? var12[var51 & 0xFF] : var1[var51])
                                                != (var4 != 0)) {
                                                var7++;
                                             }
                                             break;
                                          case 26:
                                             int var34;
                                             int var50;
                                             if (((var34 = var3 >>> 23) > 255 ? var12[var34 & 0xFF] : var1[var34])
                                                   .lteq_b((var50 = var3 >> 14 & 511) > 255 ? var12[var50 & 0xFF] : var1[var50])
                                                != (var4 != 0)) {
                                                var7++;
                                             }
                                             break;
                                          case 27:
                                             if (var1[var4].toboolean() != ((var3 & 8372224) != 0)) {
                                                var7++;
                                             }
                                             break;
                                          case 28:
                                             LuaValue var65;
                                             if ((var65 = var1[var3 >>> 23]).toboolean() != ((var3 & 8372224) != 0)) {
                                                var7++;
                                             } else {
                                                var1[var4] = var65;
                                             }
                                             break;
                                          case 29:
                                             switch (var3 & -16384) {
                                                case 8388608:
                                                   var10 = var1[var4].invoke(NONE);
                                                   var8 = var4 + var10.narg();
                                                   break label946;
                                                case 8404992:
                                                   var1[var4].call();
                                                   break label946;
                                                case 8421376:
                                                   var1[var4] = var1[var4].call();
                                                   break label946;
                                                case 16777216:
                                                   var10 = var1[var4].invoke(var1[var4 + 1]);
                                                   var8 = var4 + var10.narg();
                                                   break label946;
                                                case 16793600:
                                                   var1[var4].call(var1[var4 + 1]);
                                                   break label946;
                                                case 16809984:
                                                   var1[var4] = var1[var4].call(var1[var4 + 1]);
                                                   break label946;
                                                case 25182208:
                                                   var1[var4].call(var1[var4 + 1], var1[var4 + 2]);
                                                   break label946;
                                                case 25198592:
                                                   var1[var4] = var1[var4].call(var1[var4 + 1], var1[var4 + 2]);
                                                   break label946;
                                                case 33570816:
                                                   var1[var4].call(var1[var4 + 1], var1[var4 + 2], var1[var4 + 3]);
                                                   break label946;
                                                case 33587200:
                                                   var1[var4] = var1[var4].call(var1[var4 + 1], var1[var4 + 2], var1[var4 + 3]);
                                                   break label946;
                                                default:
                                                   int var33 = var3 >>> 23;
                                                   int var49 = var3 >> 14 & 511;
                                                   var10 = var1[var4]
                                                      .invoke(
                                                         var33 > 0
                                                            ? varargsOf(var1, var4 + 1, var33 - 1)
                                                            : varargsOf(var1, var4 + 1, var8 - var10.narg() - (var4 + 1), var10)
                                                      );
                                                   if (var49 > 0) {
                                                      var10.copyto(var1, var4, var49 - 1);
                                                      var10 = NONE;
                                                   } else {
                                                      var8 = var4 + var10.narg();
                                                      var10 = var10.dealias();
                                                   }
                                                   break label946;
                                             }
                                          case 30:
                                             switch (var3 & -8388608) {
                                                case 8388608:
                                                   var75 = new TailcallVarargs(var1[var4], NONE);
                                                   var24 = false;
                                                   break label982;
                                                case 16777216:
                                                   var75 = new TailcallVarargs(var1[var4], var1[var4 + 1]);
                                                   var24 = false;
                                                   break label981;
                                                case 25165824:
                                                   var75 = new TailcallVarargs(var1[var4], varargsOf(var1[var4 + 1], var1[var4 + 2]));
                                                   var24 = false;
                                                   break label980;
                                                case 33554432:
                                                   var75 = new TailcallVarargs(var1[var4], varargsOf(var1[var4 + 1], var1[var4 + 2], var1[var4 + 3]));
                                                   var24 = false;
                                                   break label979;
                                             }

                                             int var32 = var3 >>> 23;
                                             var10 = var32 > 0
                                                ? varargsOf(var1, var4 + 1, var32 - 1)
                                                : varargsOf(var1, var4 + 1, var8 - var10.narg() - (var4 + 1), var10);
                                             var75 = new TailcallVarargs(var1[var4], var10);
                                             var24 = false;
                                             break label1040;
                                          case 31:
                                             int var31 = var3 >>> 23;
                                             switch (var31) {
                                                case 0:
                                                   var75 = varargsOf(var1, var4, var8 - var10.narg() - var4, var10);
                                                   var24 = false;
                                                   break label977;
                                                case 1:
                                                   var75 = NONE;
                                                   var24 = false;
                                                   break label976;
                                                case 2:
                                                   var75 = var1[var4];
                                                   var24 = false;
                                                   break label975;
                                                default:
                                                   var75 = varargsOf(var1, var4, var31 - 1);
                                                   var24 = false;
                                                   break label978;
                                             }
                                          case 32:
                                             LuaValue var73 = var1[var4 + 1];
                                             LuaValue var88 = var1[var4 + 2];
                                             LuaValue var100 = var88.add(var1[var4]);
                                             if (var88.gt_b(0) ? var100.lteq_b(var73) : var100.gteq_b(var73)) {
                                                var1[var4] = var100;
                                                var1[var4 + 3] = var100;
                                                var7 += (var3 >>> 14) - 131071;
                                             }
                                             break;
                                          case 33:
                                             LuaNumber var72 = var1[var4].checknumber("'for' initial value must be a number");
                                             LuaNumber var87 = var1[var4 + 1].checknumber("'for' limit must be a number");
                                             LuaNumber var99 = var1[var4 + 2].checknumber("'for' step must be a number");
                                             var1[var4] = var72.sub(var99);
                                             var1[var4 + 1] = var87;
                                             var1[var4 + 2] = var99;
                                             var7 += (var3 >>> 14) - 131071;
                                             break;
                                          case 34:
                                             var10 = var1[var4].invoke(varargsOf(var1[var4 + 1], var1[var4 + 2]));
                                             int var48 = var3 >> 14 & 511;

                                             while (--var48 >= 0) {
                                                var1[var4 + 3 + var48] = var10.arg(var48 + 1);
                                             }

                                             var10 = NONE;
                                             break;
                                          case 35:
                                             if (!var1[var4 + 1].isnil()) {
                                                var1[var4] = var1[var4 + 1];
                                                var7 += (var3 >>> 14) - 131071;
                                             }
                                             break;
                                          case 36:
                                             int var6;
                                             if ((var6 = var3 >> 14 & 511) == 0) {
                                                var6 = var11[++var7];
                                             }

                                             int var71 = (var6 - 1) * 50;
                                             LuaValue var9 = var1[var4];
                                             int var29;
                                             if ((var29 = var3 >>> 23) == 0) {
                                                var29 = var8 - var4 - 1;
                                                int var85 = var29 - var10.narg();

                                                int var98;
                                                for (var98 = 1; var98 <= var85; var98++) {
                                                   var9.set(var71 + var98, var1[var4 + var98]);
                                                }

                                                while (var98 <= var29) {
                                                   var9.set(var71 + var98, var10.arg(var98 - var85));
                                                   var98++;
                                                }
                                             } else {
                                                var9.presize(var71 + var29);

                                                for (int var86 = 1; var86 <= var29; var86++) {
                                                   var9.set(var71 + var86, var1[var4 + var86]);
                                                }
                                             }
                                             break;
                                          case 37:
                                             Prototype var70 = this.p.p[var3 >>> 14];
                                             LuaClosure var84 = new LuaClosure(var70, this.globals);
                                             Upvaldesc[] var16 = var70.upvalues;
                                             int var17 = 0;

                                             for (int var18 = var16.length; var17 < var18; var17++) {
                                                if (var16[var17].instack) {
                                                   var84.upValues[var17] = this.findupval(var1, var16[var17].idx, var13);
                                                } else {
                                                   var84.upValues[var17] = this.upValues[var16[var17].idx];
                                                }
                                             }

                                             var1[var4] = var84;
                                             break;
                                          case 38:
                                             int var5 = var3 >>> 23;
                                             if (var5 == 0) {
                                                var8 = var4 + var2.narg();
                                                var10 = var2;
                                             } else {
                                                for (int var14 = 1; var14 < var5; var14++) {
                                                   var1[var4 + var14 - 1] = var2.arg(var14);
                                                }
                                             }
                                             break;
                                          case 39:
                                             throw new IllegalArgumentException("Uexecutable opcode: OP_EXTRAARG");
                                       }

                                       var7++;
                                       continue;
                                    }
                                 } catch (LuaError var25) {
                                    if (var25.traceback == null) {
                                       this.processErrorHooks(var25, this.p, var7);
                                    }

                                    throw var25;
                                 } catch (Exception var26) {
                                    LuaError var15 = new LuaError(var26);
                                    this.processErrorHooks(var15, this.p, var7);
                                    throw var15;
                                 } finally {
                                    if (var24) {
                                       if (var13 != null) {
                                          int var20 = var13.length;

                                          while (--var20 >= 0) {
                                             if (var13[var20] != null) {
                                                var13[var20].close();
                                             }
                                          }
                                       }

                                       if (this.globals != null && this.globals.debuglib != null) {
                                          this.globals.debuglib.onReturn();
                                       }
                                    }
                                 }

                                 if (var13 != null) {
                                    int var93 = var13.length;

                                    while (--var93 >= 0) {
                                       if (var13[var93] != null) {
                                          var13[var93].close();
                                       }
                                    }
                                 }

                                 if (this.globals != null && this.globals.debuglib != null) {
                                    this.globals.debuglib.onReturn();
                                 }

                                 return var75;
                              }

                              if (var13 != null) {
                                 int var97 = var13.length;

                                 while (--var97 >= 0) {
                                    if (var13[var97] != null) {
                                       var13[var97].close();
                                    }
                                 }
                              }

                              if (this.globals != null && this.globals.debuglib != null) {
                                 this.globals.debuglib.onReturn();
                              }

                              return var75;
                           }

                           if (var13 != null) {
                              int var96 = var13.length;

                              while (--var96 >= 0) {
                                 if (var13[var96] != null) {
                                    var13[var96].close();
                                 }
                              }
                           }

                           if (this.globals != null && this.globals.debuglib != null) {
                              this.globals.debuglib.onReturn();
                           }

                           return var75;
                        }

                        if (var13 != null) {
                           int var95 = var13.length;

                           while (--var95 >= 0) {
                              if (var13[var95] != null) {
                                 var13[var95].close();
                              }
                           }
                        }

                        if (this.globals != null && this.globals.debuglib != null) {
                           this.globals.debuglib.onReturn();
                        }

                        return var75;
                     }

                     if (var13 != null) {
                        int var94 = var13.length;

                        while (--var94 >= 0) {
                           if (var13[var94] != null) {
                              var13[var94].close();
                           }
                        }
                     }

                     if (this.globals != null && this.globals.debuglib != null) {
                        this.globals.debuglib.onReturn();
                     }

                     return var75;
                  }

                  if (var13 != null) {
                     int var89 = var13.length;

                     while (--var89 >= 0) {
                        if (var13[var89] != null) {
                           var13[var89].close();
                        }
                     }
                  }

                  if (this.globals != null && this.globals.debuglib != null) {
                     this.globals.debuglib.onReturn();
                  }

                  return var75;
               }

               if (var13 != null) {
                  int var92 = var13.length;

                  while (--var92 >= 0) {
                     if (var13[var92] != null) {
                        var13[var92].close();
                     }
                  }
               }

               if (this.globals != null && this.globals.debuglib != null) {
                  this.globals.debuglib.onReturn();
               }

               return var75;
            }

            if (var13 != null) {
               int var91 = var13.length;

               while (--var91 >= 0) {
                  if (var13[var91] != null) {
                     var13[var91].close();
                  }
               }
            }

            if (this.globals != null && this.globals.debuglib != null) {
               this.globals.debuglib.onReturn();
            }

            return var75;
         }

         if (var13 != null) {
            int var90 = var13.length;

            while (--var90 >= 0) {
               if (var13[var90] != null) {
                  var13[var90].close();
               }
            }
         }

         if (this.globals != null && this.globals.debuglib != null) {
            this.globals.debuglib.onReturn();
         }

         return var75;
      }
   }

   String errorHook(String var1, int var2) {
      if (this.globals == null) {
         return var1;
      } else {
         LuaThread var3 = this.globals.running;
         if (var3.errorfunc == null) {
            return this.globals.debuglib != null ? var1 + "\n" + this.globals.debuglib.traceback(var2) : var1;
         } else {
            LuaValue var4 = var3.errorfunc;
            var3.errorfunc = null;

            String var6;
            try {
               return var4.call(LuaValue.valueOf(var1)).tojstring();
            } catch (Throwable var10) {
               var6 = "error in error handling";
            } finally {
               var3.errorfunc = var4;
            }

            return var6;
         }
      }
   }

   private void processErrorHooks(LuaError var1, Prototype var2, int var3) {
      var1.fileline = (var2.source != null ? var2.source.tojstring() : "?")
         + ":"
         + (var2.lineinfo != null && var3 >= 0 && var3 < var2.lineinfo.length ? String.valueOf(var2.lineinfo[var3]) : "?");
      var1.traceback = this.errorHook(var1.getMessage(), var1.level);
   }

   private UpValue findupval(LuaValue[] var1, short var2, UpValue[] var3) {
      int var4 = var3.length;

      for (int var5 = 0; var5 < var4; var5++) {
         if (var3[var5] != null && var3[var5].index == var2) {
            return var3[var5];
         }
      }

      for (int var6 = 0; var6 < var4; var6++) {
         if (var3[var6] == null) {
            return var3[var6] = new UpValue(var1, var2);
         }
      }

      error("No space for upvalue");
      return null;
   }

   protected LuaValue getUpvalue(int var1) {
      return this.upValues[var1].getValue();
   }

   protected void setUpvalue(int var1, LuaValue var2) {
      this.upValues[var1].setValue(var2);
   }

   public String name() {
      return "<" + this.p.shortsource() + ":" + this.p.linedefined + ">";
   }
}
