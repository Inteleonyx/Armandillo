package dev.inteleonyx.armandillo.api.luaj.luajc;

import dev.inteleonyx.armandillo.api.luaj.Lua;

public class UpvalInfo {
   ProtoInfo pi;
   int slot;
   int nvars;
   VarInfo[] var;
   boolean rw;

   public UpvalInfo(ProtoInfo var1) {
      this.pi = var1;
      this.slot = 0;
      this.nvars = 1;
      this.var = new VarInfo[]{VarInfo.PARAM(0)};
      this.rw = false;
   }

   public UpvalInfo(ProtoInfo var1, int var2, int var3) {
      this.pi = var1;
      this.slot = var3;
      this.nvars = 0;
      this.var = null;
      this.includeVarAndPosteriorVars(var1.vars[var3][var2]);

      for (int var4 = 0; var4 < this.nvars; var4++) {
         this.var[var4].allocupvalue = this.testIsAllocUpvalue(this.var[var4]);
      }

      this.rw = this.nvars > 1;
   }

   private boolean includeVarAndPosteriorVars(VarInfo var1) {
      if (var1 == null || var1 == VarInfo.INVALID) {
         return false;
      } else if (var1.upvalue == this) {
         return true;
      } else {
         var1.upvalue = this;
         this.appendVar(var1);
         if (this.isLoopVariable(var1)) {
            return false;
         } else {
            boolean var2 = this.includePosteriorVarsCheckLoops(var1);
            if (var2) {
               this.includePriorVarsIgnoreLoops(var1);
            }

            return var2;
         }
      }
   }

   private boolean isLoopVariable(VarInfo var1) {
      if (var1.pc >= 0) {
         switch (Lua.GET_OPCODE(this.pi.prototype.code[var1.pc])) {
            case 32:
            case 35:
               return true;
         }
      }

      return false;
   }

   private boolean includePosteriorVarsCheckLoops(VarInfo var1) {
      boolean var2 = false;
      int var3 = 0;

      for (int var4 = this.pi.blocklist.length; var3 < var4; var3++) {
         BasicBlock var5 = this.pi.blocklist[var3];
         VarInfo var6 = this.pi.vars[this.slot][var5.pc1];
         if (var6 == var1) {
            int var11 = 0;

            for (int var8 = var5.next != null ? var5.next.length : 0; var11 < var8; var11++) {
               BasicBlock var9 = var5.next[var11];
               VarInfo var10 = this.pi.vars[this.slot][var9.pc0];
               if (var10 != var1) {
                  var2 |= this.includeVarAndPosteriorVars(var10);
                  if (var10.isPhiVar()) {
                     this.includePriorVarsIgnoreLoops(var10);
                  }
               }
            }
         } else {
            for (int var7 = var5.pc1 - 1; var7 >= var5.pc0; var7--) {
               if (this.pi.vars[this.slot][var7] == var1) {
                  var2 |= this.includeVarAndPosteriorVars(this.pi.vars[this.slot][var7 + 1]);
                  break;
               }
            }
         }
      }

      return var2;
   }

   private void includePriorVarsIgnoreLoops(VarInfo var1) {
      int var2 = 0;

      for (int var3 = this.pi.blocklist.length; var2 < var3; var2++) {
         BasicBlock var4 = this.pi.blocklist[var2];
         VarInfo var5 = this.pi.vars[this.slot][var4.pc0];
         if (var5 == var1) {
            int var10 = 0;

            for (int var7 = var4.prev != null ? var4.prev.length : 0; var10 < var7; var10++) {
               BasicBlock var8 = var4.prev[var10];
               VarInfo var9 = this.pi.vars[this.slot][var8.pc1];
               if (var9 != var1) {
                  this.includeVarAndPosteriorVars(var9);
               }
            }
         } else {
            for (int var6 = var4.pc0 + 1; var6 <= var4.pc1; var6++) {
               if (this.pi.vars[this.slot][var6] == var1) {
                  this.includeVarAndPosteriorVars(this.pi.vars[this.slot][var6 - 1]);
                  break;
               }
            }
         }
      }
   }

   private void appendVar(VarInfo var1) {
      if (this.nvars == 0) {
         this.var = new VarInfo[1];
      } else if (this.nvars + 1 >= this.var.length) {
         VarInfo[] var2 = this.var;
         this.var = new VarInfo[this.nvars * 2 + 1];
         System.arraycopy(var2, 0, this.var, 0, this.nvars);
      }

      this.var[this.nvars++] = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.pi.name);

      for (int var2 = 0; var2 < this.nvars; var2++) {
         var1.append(var2 > 0 ? "," : " ");
         var1.append(String.valueOf(this.var[var2]));
      }

      if (this.rw) {
         var1.append("(rw)");
      }

      return var1.toString();
   }

   private boolean testIsAllocUpvalue(VarInfo var1) {
      if (var1.pc < 0) {
         return true;
      } else {
         BasicBlock var2 = this.pi.blocks[var1.pc];
         if (var1.pc > var2.pc0) {
            return this.pi.vars[this.slot][var1.pc - 1].upvalue != this;
         } else {
            if (var2.prev == null) {
               var1 = this.pi.params[this.slot];
               if (var1 != null && var1.upvalue != this) {
                  return true;
               }
            } else {
               int var3 = 0;

               for (int var4 = var2.prev.length; var3 < var4; var3++) {
                  var1 = this.pi.vars[this.slot][var2.prev[var3].pc1];
                  if (var1 != null && var1.upvalue != this) {
                     return true;
                  }
               }
            }

            return false;
         }
      }
   }
}
