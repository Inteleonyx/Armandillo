package dev.inteleonyx.armandillo.api.luaj.lib;

import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.Varargs;

public abstract class ZeroArgFunction extends LibFunction {
   public abstract LuaValue call();

   public LuaValue call(LuaValue var1) {
      return this.call();
   }

   public LuaValue call(LuaValue var1, LuaValue var2) {
      return this.call();
   }

   public LuaValue call(LuaValue var1, LuaValue var2, LuaValue var3) {
      return this.call();
   }

   public Varargs invoke(Varargs var1) {
      return this.call();
   }
}
