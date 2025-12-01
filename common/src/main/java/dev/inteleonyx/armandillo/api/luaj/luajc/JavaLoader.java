package dev.inteleonyx.armandillo.api.luaj.luajc;

import dev.inteleonyx.armandillo.api.luaj.LuaFunction;
import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.Prototype;

import java.util.HashMap;
import java.util.Map;

public class JavaLoader extends ClassLoader {
   private Map unloaded = new HashMap();

   public LuaFunction load(Prototype var1, String var2, String var3, LuaValue var4) {
      JavaGen var5 = new JavaGen(var1, var2, var3, false);
      return this.load(var5, var4);
   }

   public LuaFunction load(JavaGen var1, LuaValue var2) {
      this.include(var1);
      return this.load(var1.classname, var2);
   }

   public LuaFunction load(String var1, LuaValue var2) {
      try {
         Class var3 = this.loadClass(var1);
         LuaFunction var4 = (LuaFunction)var3.newInstance();
         var4.initupvalue1(var2);
         return var4;
      } catch (Exception var5) {
         var5.printStackTrace();
         throw new IllegalStateException("bad class gen: " + var5);
      }
   }

   public void include(JavaGen var1) {
      this.unloaded.put(var1.classname, var1.bytecode);
      int var2 = 0;

      for (int var3 = var1.inners != null ? var1.inners.length : 0; var2 < var3; var2++) {
         this.include(var1.inners[var2]);
      }
   }

   public Class findClass(String var1) throws ClassNotFoundException {
      byte[] var2 = (byte[])this.unloaded.get(var1);
      return var2 != null ? this.defineClass(var1, var2, 0, var2.length) : super.findClass(var1);
   }
}
