package dev.inteleonyx.armandillo.api.luaj.luajc;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class VarInfo {
   public static VarInfo INVALID = new VarInfo(-1, -1);
   public final int slot;
   public final int pc;
   public UpvalInfo upvalue;
   public boolean allocupvalue;
   public boolean isreferenced;

   public static VarInfo PARAM(int var0) {
      return new ParamVarInfo(var0, -1);
   }

   public static VarInfo NIL(int var0) {
      return new NilVarInfo(var0, -1);
   }

   public static VarInfo PHI(ProtoInfo var0, int var1, int var2) {
      return new PhiVarInfo(var0, var1, var2);
   }

   public VarInfo(int var1, int var2) {
      this.slot = var1;
      this.pc = var2;
   }

   public String toString() {
      return this.slot < 0 ? "x.x" : this.slot + "." + this.pc;
   }

   public VarInfo resolvePhiVariableValues() {
      return null;
   }

   protected void collectUniqueValues(Set var1, Set var2) {
      var2.add(this);
   }

   public boolean isPhiVar() {
      return false;
   }

   private static final class NilVarInfo extends VarInfo {
      private NilVarInfo(int var1, int var2) {
         super(var1, var2);
      }

      public String toString() {
         return "nil";
      }
   }

   private static final class ParamVarInfo extends VarInfo {
      private ParamVarInfo(int var1, int var2) {
         super(var1, var2);
      }

      public String toString() {
         return this.slot + ".p";
      }
   }

   private static final class PhiVarInfo extends VarInfo {
      private final ProtoInfo pi;
      VarInfo[] values;

      private PhiVarInfo(ProtoInfo var1, int var2, int var3) {
         super(var2, var3);
         this.pi = var1;
      }

      public boolean isPhiVar() {
         return true;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append(super.toString());
         var1.append("={");
         int var2 = 0;

         for (int var3 = this.values != null ? this.values.length : 0; var2 < var3; var2++) {
            if (var2 > 0) {
               var1.append(",");
            }

            var1.append(String.valueOf(this.values[var2]));
         }

         var1.append("}");
         return var1.toString();
      }

      public VarInfo resolvePhiVariableValues() {
         HashSet var1 = new HashSet();
         HashSet var2 = new HashSet();
         this.collectUniqueValues(var1, var2);
         if (var2.contains(INVALID)) {
            return INVALID;
         } else {
            int var3 = var2.size();
            Iterator var4 = var2.iterator();
            if (var3 == 1) {
               VarInfo var6 = (VarInfo)var4.next();
               var6.isreferenced = var6.isreferenced | this.isreferenced;
               return var6;
            } else {
               this.values = new VarInfo[var3];

               for (int var5 = 0; var5 < var3; var5++) {
                  this.values[var5] = (VarInfo)var4.next();
                  this.values[var5].isreferenced = this.values[var5].isreferenced | this.isreferenced;
               }

               return null;
            }
         }
      }

      protected void collectUniqueValues(Set var1, Set var2) {
         BasicBlock var3 = this.pi.blocks[this.pc];
         if (this.pc == 0) {
            var2.add(this.pi.params[this.slot]);
         }

         int var4 = 0;

         for (int var5 = var3.prev != null ? var3.prev.length : 0; var4 < var5; var4++) {
            BasicBlock var6 = var3.prev[var4];
            if (!var1.contains(var6)) {
               var1.add(var6);
               VarInfo var7 = this.pi.vars[this.slot][var6.pc1];
               if (var7 != null) {
                  var7.collectUniqueValues(var1, var2);
               }
            }
         }
      }
   }
}
