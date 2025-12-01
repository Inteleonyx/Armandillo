package dev.inteleonyx.armandillo.api.luaj.lib.jse;

import dev.inteleonyx.armandillo.api.luaj.lib.StringLib;

public class JseStringLib extends StringLib {
   protected String format(String var1, double var2) {
      String var4;
      try {
         var4 = String.format(var1, var2);
      } catch (Throwable var6) {
         var4 = super.format(var1, var2);
      }

      return var4;
   }
}
