package dev.inteleonyx.armandillo.api.luaj.lib;

import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.Varargs;

public abstract class ThreeArgFunction extends LibFunction {
   public abstract LuaValue call(LuaValue var1, LuaValue var2, LuaValue var3);

   public final LuaValue call() {
      return this.call(NIL, NIL, NIL);
   }

   public final LuaValue call(LuaValue var1) {
      return this.call(var1, NIL, NIL);
   }

   public LuaValue call(LuaValue var1, LuaValue var2) {
      return this.call(var1, var2, NIL);
   }

   public Varargs invoke(Varargs var1) {
      return this.call(var1.arg1(), var1.arg(2), var1.arg(3));
   }
}
