package dev.inteleonyx.armandillo.api.luaj.lib.jse;

import dev.inteleonyx.armandillo.api.luaj.Varargs;
import dev.inteleonyx.armandillo.api.luaj.lib.VarArgFunction;

abstract class JavaMember extends VarArgFunction {
   static final int METHOD_MODIFIERS_VARARGS = 128;
   final CoerceLuaToJava.Coercion[] fixedargs;
   final CoerceLuaToJava.Coercion varargs;

   protected JavaMember(Class[] var1, int var2) {
      boolean var3 = (var2 & 128) != 0;
      this.fixedargs = new CoerceLuaToJava.Coercion[var3 ? var1.length - 1 : var1.length];

      for (int var4 = 0; var4 < this.fixedargs.length; var4++) {
         this.fixedargs[var4] = CoerceLuaToJava.getCoercion(var1[var4]);
      }

      this.varargs = var3 ? CoerceLuaToJava.getCoercion(var1[var1.length - 1]) : null;
   }

   int score(Varargs var1) {
      int var2 = var1.narg();
      int var3 = var2 > this.fixedargs.length ? CoerceLuaToJava.SCORE_WRONG_TYPE * (var2 - this.fixedargs.length) : 0;

      for (int var4 = 0; var4 < this.fixedargs.length; var4++) {
         var3 += this.fixedargs[var4].score(var1.arg(var4 + 1));
      }

      if (this.varargs != null) {
         for (int var5 = this.fixedargs.length; var5 < var2; var5++) {
            var3 += this.varargs.score(var1.arg(var5 + 1));
         }
      }

      return var3;
   }

   protected Object[] convertArgs(Varargs var1) {
      Object[] var2;
      if (this.varargs == null) {
         var2 = new Object[this.fixedargs.length];

         for (int var3 = 0; var3 < var2.length; var3++) {
            var2[var3] = this.fixedargs[var3].coerce(var1.arg(var3 + 1));
         }
      } else {
         int var5 = Math.max(this.fixedargs.length, var1.narg());
         var2 = new Object[var5];

         for (int var4 = 0; var4 < this.fixedargs.length; var4++) {
            var2[var4] = this.fixedargs[var4].coerce(var1.arg(var4 + 1));
         }

         for (int var6 = this.fixedargs.length; var6 < var5; var6++) {
            var2[var6] = this.varargs.coerce(var1.arg(var6 + 1));
         }
      }

      return var2;
   }
}
