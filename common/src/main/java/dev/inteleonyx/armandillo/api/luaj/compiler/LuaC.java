package dev.inteleonyx.armandillo.api.luaj.compiler;

import dev.inteleonyx.armandillo.api.luaj.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

public class LuaC extends Constants implements Globals.Compiler, Globals.Loader {
   public static final LuaC instance = new LuaC();

   public static void install(Globals var0) {
      var0.compiler = instance;
      var0.loader = instance;
   }

   protected LuaC() {
   }

   public Prototype compile(InputStream var1, String var2) throws IOException {
      return new CompileState().luaY_parser(var1, var2);
   }

   public LuaFunction load(Prototype var1, String var2, LuaValue var3) throws IOException {
      return new LuaClosure(var1, var3);
   }

   /** @deprecated */
   public LuaValue load(InputStream var1, String var2, Globals var3) throws IOException {
      return new LuaClosure(this.compile(var1, var2), var3);
   }

   static class CompileState {
      int nCcalls = 0;
      private Hashtable strings = new Hashtable();

      protected CompileState() {
      }

      Prototype luaY_parser(InputStream var1, String var2) throws IOException {
         LexState var3 = new LexState(this, var1);
         FuncState var4 = new FuncState();
         var3.fs = var4;
         var3.setinput(this, var1.read(), var1, LuaValue.valueOf(var2));
         var4.f = new Prototype();
         var4.f.source = LuaValue.valueOf(var2);
         var3.mainfunc(var4);
         LuaC._assert(var4.prev == null);
         LuaC._assert(var3.dyd == null || var3.dyd.n_actvar == 0 && var3.dyd.n_gt == 0 && var3.dyd.n_label == 0);
         return var4.f;
      }

      public LuaString newTString(String var1) {
         return this.cachedLuaString(LuaString.valueOf(var1));
      }

      public LuaString newTString(LuaString var1) {
         return this.cachedLuaString(var1);
      }

      public LuaString cachedLuaString(LuaString var1) {
         LuaString var2 = (LuaString)this.strings.get(var1);
         if (var2 != null) {
            return var2;
         } else {
            this.strings.put(var1, var1);
            return var1;
         }
      }

      public String pushfstring(String var1) {
         return var1;
      }
   }
}
