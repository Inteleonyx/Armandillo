package dev.inteleonyx.armandillo.api.luaj.lib.jse;

import dev.inteleonyx.armandillo.api.luaj.*;
import dev.inteleonyx.armandillo.api.luaj.compiler.LuaC;
import dev.inteleonyx.armandillo.api.luaj.lib.*;

public class JsePlatform {
   public static Globals standardGlobals() {
      Globals var0 = new Globals();
      var0.load(new JseBaseLib());
      var0.load(new PackageLib());
      var0.load(new Bit32Lib());
      var0.load(new TableLib());
      var0.load(new JseStringLib());
      var0.load(new CoroutineLib());
      var0.load(new JseMathLib());
      var0.load(new JseIoLib());
      var0.load(new JseOsLib());
      var0.load(new LuajavaLib());
      LoadState.install(var0);
      LuaC.install(var0);
      return var0;
   }

   public static Globals debugGlobals() {
      Globals var0 = standardGlobals();
      var0.load(new DebugLib());
      return var0;
   }

   public static Varargs luaMain(LuaValue var0, String[] var1) {
      Globals var2 = standardGlobals();
      int var3 = var1.length;
      LuaValue[] var4 = new LuaValue[var1.length];

      for (int var5 = 0; var5 < var3; var5++) {
         var4[var5] = LuaValue.valueOf(var1[var5]);
      }

      LuaTable var6 = LuaValue.listOf(var4);
      var6.set("n", var3);
      var2.set("arg", var6);
      var0.initupvalue1(var2);
      return var0.invoke(LuaValue.varargsOf(var4));
   }
}
