package dev.inteleonyx.armandillo.api.luaj.lib;

import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.Varargs;

public abstract class OneArgFunction extends LibFunction {
   public abstract LuaValue call(LuaValue var1);

   public final LuaValue call() {
      return this.call(NIL);
   }

   public final LuaValue call(LuaValue var1, LuaValue var2) {
      return this.call(var1);
   }

   public LuaValue call(LuaValue var1, LuaValue var2, LuaValue var3) {
      return this.call(var1);
   }

   public Varargs invoke(Varargs var1) {
      return this.call(var1.arg1());
   }
}
