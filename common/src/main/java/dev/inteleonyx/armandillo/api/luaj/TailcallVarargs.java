package dev.inteleonyx.armandillo.api.luaj;

public class TailcallVarargs extends Varargs {
   private LuaValue func;
   private Varargs args;
   private Varargs result;

   public TailcallVarargs(LuaValue var1, Varargs var2) {
      this.func = var1;
      this.args = var2;
   }

   public TailcallVarargs(LuaValue var1, LuaValue var2, Varargs var3) {
      this.func = var1.get(var2);
      this.args = LuaValue.varargsOf(var1, var3);
   }

   public boolean isTailcall() {
      return true;
   }

   public Varargs eval() {
      while (this.result == null) {
         Varargs var1 = this.func.onInvoke(this.args);
         if (var1.isTailcall()) {
            TailcallVarargs var2 = (TailcallVarargs)var1;
            this.func = var2.func;
            this.args = var2.args;
         } else {
            this.result = var1;
            this.func = null;
            this.args = null;
         }
      }

      return this.result;
   }

   public LuaValue arg(int var1) {
      if (this.result == null) {
         this.eval();
      }

      return this.result.arg(var1);
   }

   public LuaValue arg1() {
      if (this.result == null) {
         this.eval();
      }

      return this.result.arg1();
   }

   public int narg() {
      if (this.result == null) {
         this.eval();
      }

      return this.result.narg();
   }

   public Varargs subargs(int var1) {
      if (this.result == null) {
         this.eval();
      }

      return this.result.subargs(var1);
   }
}
