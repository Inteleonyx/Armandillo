package dev.inteleonyx.armandillo.api.luaj.server;

import dev.inteleonyx.armandillo.api.luaj.Globals;
import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.Varargs;
import dev.inteleonyx.armandillo.api.luaj.lib.jse.CoerceJavaToLua;
import dev.inteleonyx.armandillo.api.luaj.lib.jse.JsePlatform;

import java.io.InputStream;
import java.io.Reader;

public class DefaultLauncher implements Launcher {
   protected Globals g = JsePlatform.standardGlobals();

   @Override
   public Object[] launch(String var1, Object[] var2) {
      return this.launchChunk(this.g.load(var1, "main"), var2);
   }

   @Override
   public Object[] launch(InputStream var1, Object[] var2) {
      return this.launchChunk(this.g.load(var1, "main", "bt", this.g), var2);
   }

   @Override
   public Object[] launch(Reader var1, Object[] var2) {
      return this.launchChunk(this.g.load(var1, "main"), var2);
   }

   private Object[] launchChunk(LuaValue var1, Object[] var2) {
      LuaValue[] var3 = new LuaValue[var2.length];

      for (int var4 = 0; var4 < var3.length; var4++) {
         var3[var4] = CoerceJavaToLua.coerce(var2[var4]);
      }

      Varargs var9 = var1.invoke(LuaValue.varargsOf(var3));
      int var5 = var9.narg();
      Object[] var6 = new Object[var5];

      for (int var7 = 0; var7 < var5; var7++) {
         LuaValue var8 = var9.arg(var7 + 1);
         switch (var8.type()) {
            case -2:
               var6[var7] = var8.toint();
               break;
            case -1:
            case 2:
            case 5:
            case 6:
            default:
               var6[var7] = var8;
               break;
            case 0:
               var6[var7] = null;
               break;
            case 1:
               var6[var7] = var8.toboolean();
               break;
            case 3:
               var6[var7] = var8.todouble();
               break;
            case 4:
               var6[var7] = var8.tojstring();
               break;
            case 7:
               var6[var7] = var8.touserdata();
         }
      }

      return var6;
   }
}
