package dev.inteleonyx.armandillo.api.luaj.lib.jse;

import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.lib.BaseLib;

import java.io.*;

public class JseBaseLib extends BaseLib {
   public LuaValue call(LuaValue var1, LuaValue var2) {
      super.call(var1, var2);
      var2.checkglobals().STDIN = System.in;
      return var2;
   }

   public InputStream findResource(String var1) {
      File var2 = new File(var1);
      if (!var2.exists()) {
         return super.findResource(var1);
      } else {
         try {
            return new BufferedInputStream(new FileInputStream(var2));
         } catch (IOException var4) {
            return null;
         }
      }
   }
}
