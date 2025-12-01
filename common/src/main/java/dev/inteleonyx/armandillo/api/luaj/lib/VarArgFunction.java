package dev.inteleonyx.armandillo.api.luaj.lib;

import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.Varargs;

public abstract class VarArgFunction extends LibFunction {
   public LuaValue call() {
      return this.invoke(NONE).arg1();
   }

   public LuaValue call(LuaValue var1) {
      return this.invoke(var1).arg1();
   }

   public LuaValue call(LuaValue var1, LuaValue var2) {
      return this.invoke(varargsOf(var1, var2)).arg1();
   }

   public LuaValue call(LuaValue var1, LuaValue var2, LuaValue var3) {
      return this.invoke(varargsOf(var1, var2, var3)).arg1();
   }

   public Varargs invoke(Varargs var1) {
      return this.onInvoke(var1).eval();
   }

   public Varargs onInvoke(Varargs var1) {
      return this.invoke(var1);
   }
}
