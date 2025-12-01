package dev.inteleonyx.armandillo.api.luaj.lib;

import dev.inteleonyx.armandillo.api.luaj.*;

public class CoroutineLib extends TwoArgFunction {
   static int coroutine_count = 0;
   Globals globals;

   public LuaValue call(LuaValue var1, LuaValue var2) {
      this.globals = var2.checkglobals();
      LuaTable var3 = new LuaTable();
      var3.set("create", new create());
      var3.set("resume", new resume());
      var3.set("running", new running());
      var3.set("status", new status());
      var3.set("yield", new YieldFunction());
      var3.set("wrap", new wrap());
      var2.set("coroutine", var3);
      if (!var2.get("package").isnil()) {
         var2.get("package").get("loaded").set("coroutine", var3);
      }

      return var3;
   }

   final class create extends LibFunction {
      public LuaValue call(LuaValue var1) {
         return new LuaThread(CoroutineLib.this.globals, var1.checkfunction());
      }
   }

   static final class resume extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         LuaThread var2 = var1.checkthread(1);
         return var2.resume(var1.subargs(2));
      }
   }

   final class running extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         LuaThread var2 = CoroutineLib.this.globals.running;
         return varargsOf(var2, valueOf(var2.isMainThread()));
      }
   }

   static final class status extends LibFunction {
      public LuaValue call(LuaValue var1) {
         LuaThread var2 = var1.checkthread();
         return valueOf(var2.getStatus());
      }
   }

   final class wrap extends LibFunction {
      public LuaValue call(LuaValue var1) {
         LuaFunction var2 = var1.checkfunction();
         LuaThread var3 = new LuaThread(CoroutineLib.this.globals, var2);
         return new wrapper(var3);
      }
   }

   static final class wrapper extends VarArgFunction {
      final LuaThread luathread;

      wrapper(LuaThread var1) {
         this.luathread = var1;
      }

      public Varargs invoke(Varargs var1) {
         Varargs var2 = this.luathread.resume(var1);
         return (Varargs)(var2.arg1().toboolean() ? var2.subargs(2) : error(var2.arg(2).tojstring()));
      }
   }

   final class YieldFunction extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         return CoroutineLib.this.globals.yield(var1);
      }
   }
}
