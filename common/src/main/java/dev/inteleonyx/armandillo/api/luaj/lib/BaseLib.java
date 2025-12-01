package dev.inteleonyx.armandillo.api.luaj.lib;

import dev.inteleonyx.armandillo.api.luaj.*;

import java.io.IOException;
import java.io.InputStream;

public class BaseLib extends TwoArgFunction implements ResourceFinder {
   Globals globals;

   public LuaValue call(LuaValue var1, LuaValue var2) {
      this.globals = var2.checkglobals();
      this.globals.finder = this;
      this.globals.baselib = this;
      var2.set("_G", var2);
      var2.set("_VERSION", "Luaj-jse 3.0.2");
      var2.set("assert", new _assert());
      var2.set("collectgarbage", new collectgarbage());
      var2.set("dofile", new dofile());
      var2.set("error", new error());
      var2.set("getmetatable", new getmetatable());
      var2.set("load", new load());
      var2.set("loadfile", new loadfile());
      var2.set("pcall", new pcall());
      var2.set("print", new print(this));
      var2.set("rawequal", new rawequal());
      var2.set("rawget", new rawget());
      var2.set("rawlen", new rawlen());
      var2.set("rawset", new rawset());
      var2.set("select", new select());
      var2.set("setmetatable", new setmetatable());
      var2.set("tonumber", new tonumber());
      var2.set("tostring", new tostring());
      var2.set("type", new type());
      var2.set("xpcall", new xpcall());
      next var3;
      var2.set("next", var3 = new next());
      var2.set("pairs", new pairs(var3));
      var2.set("ipairs", new ipairs());
      return var2;
   }

   public InputStream findResource(String var1) {
      return this.getClass().getResourceAsStream(var1.startsWith("/") ? var1 : "/" + var1);
   }

   public Varargs loadFile(String var1, String var2, LuaValue var3) {
      InputStream var4 = this.globals.finder.findResource(var1);
      if (var4 == null) {
         return varargsOf(NIL, valueOf("cannot open " + var1 + ": No such file or directory"));
      } else {
         Varargs var5;
         try {
            var5 = this.loadStream(var4, "@" + var1, var2, var3);
         } finally {
            try {
               var4.close();
            } catch (Exception var12) {
               var12.printStackTrace();
            }
         }

         return var5;
      }
   }

   public Varargs loadStream(InputStream var1, String var2, String var3, LuaValue var4) {
      try {
         return (Varargs)(var1 == null ? varargsOf(NIL, valueOf("not found: " + var2)) : this.globals.load(var1, var2, var3, var4));
      } catch (Exception var6) {
         return varargsOf(NIL, valueOf(var6.getMessage()));
      }
   }

   private static class StringInputStream extends InputStream {
      final LuaValue func;
      byte[] bytes;
      int offset;
      int remaining = 0;

      StringInputStream(LuaValue var1) {
         this.func = var1;
      }

      public int read() throws IOException {
         if (this.remaining <= 0) {
            LuaValue var1 = this.func.call();
            if (var1.isnil()) {
               return -1;
            }

            LuaString var2 = var1.strvalue();
            this.bytes = var2.m_bytes;
            this.offset = var2.m_offset;
            this.remaining = var2.m_length;
            if (this.remaining <= 0) {
               return -1;
            }
         }

         this.remaining--;
         return this.bytes[this.offset++];
      }
   }

   static final class _assert extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         if (!var1.arg1().toboolean()) {
            error(var1.narg() > 1 ? var1.optjstring(2, "assertion failed!") : "assertion failed!");
         }

         return var1;
      }
   }

   static final class collectgarbage extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         String var2 = var1.optjstring(1, "collect");
         if ("collect".equals(var2)) {
            System.gc();
            return ZERO;
         } else if ("count".equals(var2)) {
            Runtime var3 = Runtime.getRuntime();
            long var4 = var3.totalMemory() - var3.freeMemory();
            return varargsOf(valueOf(var4 / 1024.0), valueOf(var4 % 1024L));
         } else if ("step".equals(var2)) {
            System.gc();
            return LuaValue.TRUE;
         } else {
            this.argerror("gc op");
            return NIL;
         }
      }
   }

   final class dofile extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         var1.argcheck(var1.isstring(1) || var1.isnil(1), 1, "filename must be string or nil");
         String var2 = var1.isstring(1) ? var1.tojstring(1) : null;
         Varargs var3 = var2 == null
            ? BaseLib.this.loadStream(BaseLib.this.globals.STDIN, "=stdin", "bt", BaseLib.this.globals)
            : BaseLib.this.loadFile(var1.checkjstring(1), "bt", BaseLib.this.globals);
         return (Varargs)(var3.isnil(1) ? error(var3.tojstring(2)) : var3.arg1().invoke());
      }
   }

   static final class error extends TwoArgFunction {
      public LuaValue call(LuaValue var1, LuaValue var2) {
         throw var1.isnil() ? new LuaError(null, var2.optint(1)) : (var1.isstring() ? new LuaError(var1.tojstring(), var2.optint(1)) : new LuaError(var1));
      }
   }

   static final class getmetatable extends LibFunction {
      public LuaValue call() {
         return argerror(1, "value");
      }

      public LuaValue call(LuaValue var1) {
         LuaValue var2 = var1.getmetatable();
         return var2 != null ? var2.rawget(METATABLE).optvalue(var2) : NIL;
      }
   }

   static final class inext extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         return var1.checktable(1).inext(var1.arg(2));
      }
   }

   static final class ipairs extends VarArgFunction {
      inext inext = new inext();

      public Varargs invoke(Varargs var1) {
         return varargsOf(this.inext, var1.checktable(1), ZERO);
      }
   }

   final class load extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         LuaValue var2 = var1.arg1();
         var1.argcheck(var2.isstring() || var2.isfunction(), 1, "ld must be string or function");
         String var3 = var1.optjstring(2, var2.isstring() ? var2.tojstring() : "=(load)");
         String var4 = var1.optjstring(3, "bt");
         LuaValue var5 = var1.optvalue(4, BaseLib.this.globals);
         return BaseLib.this.loadStream(
            (InputStream)(var2.isstring() ? var2.strvalue().toInputStream() : new StringInputStream(var2.checkfunction())), var3, var4, var5
         );
      }
   }

   final class loadfile extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         var1.argcheck(var1.isstring(1) || var1.isnil(1), 1, "filename must be string or nil");
         String var2 = var1.isstring(1) ? var1.tojstring(1) : null;
         String var3 = var1.optjstring(2, "bt");
         LuaValue var4 = var1.optvalue(3, BaseLib.this.globals);
         return var2 == null ? BaseLib.this.loadStream(BaseLib.this.globals.STDIN, "=stdin", var3, var4) : BaseLib.this.loadFile(var2, var3, var4);
      }
   }

   static final class next extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         return var1.checktable(1).next(var1.arg(2));
      }
   }

   static final class pairs extends VarArgFunction {
      final next next;

      pairs(next var1) {
         this.next = var1;
      }

      public Varargs invoke(Varargs var1) {
         return varargsOf(this.next, var1.checktable(1), NIL);
      }
   }

   final class pcall extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         LuaValue var2 = var1.checkvalue(1);
         if (BaseLib.this.globals != null && BaseLib.this.globals.debuglib != null) {
            BaseLib.this.globals.debuglib.onCall(this);
         }

         Varargs var5;
         try {
            return varargsOf(TRUE, var2.invoke(var1.subargs(2)));
         } catch (LuaError var10) {
            LuaValue var13 = var10.getMessageObject();
            return varargsOf(FALSE, var13 != null ? var13 : NIL);
         } catch (Exception var11) {
            String var4 = var11.getMessage();
            var5 = varargsOf(FALSE, valueOf(var4 != null ? var4 : var11.toString()));
         } finally {
            if (BaseLib.this.globals != null && BaseLib.this.globals.debuglib != null) {
               BaseLib.this.globals.debuglib.onReturn();
            }
         }

         return var5;
      }
   }

   final class print extends VarArgFunction {
      final BaseLib baselib;

      print(BaseLib var2) {
         this.baselib = var2;
      }

      public Varargs invoke(Varargs var1) {
         LuaValue var2 = BaseLib.this.globals.get("tostring");
         int var3 = 1;

         for (int var4 = var1.narg(); var3 <= var4; var3++) {
            if (var3 > 1) {
               BaseLib.this.globals.STDOUT.print(" \t");
            }

            LuaString var5 = var2.call(var1.arg(var3)).strvalue();
            BaseLib.this.globals.STDOUT.print(var5.tojstring());
         }

         BaseLib.this.globals.STDOUT.println();
         return NONE;
      }
   }

   static final class rawequal extends LibFunction {
      public LuaValue call() {
         return argerror(1, "value");
      }

      public LuaValue call(LuaValue var1) {
         return argerror(2, "value");
      }

      public LuaValue call(LuaValue var1, LuaValue var2) {
         return valueOf(var1.raweq(var2));
      }
   }

   static final class rawget extends LibFunction {
      public LuaValue call() {
         return argerror(1, "value");
      }

      public LuaValue call(LuaValue var1) {
         return argerror(2, "value");
      }

      public LuaValue call(LuaValue var1, LuaValue var2) {
         return var1.checktable().rawget(var2);
      }
   }

   static final class rawlen extends LibFunction {
      public LuaValue call(LuaValue var1) {
         return valueOf(var1.rawlen());
      }
   }

   static final class rawset extends LibFunction {
      public LuaValue call(LuaValue var1) {
         return argerror(2, "value");
      }

      public LuaValue call(LuaValue var1, LuaValue var2) {
         return argerror(3, "value");
      }

      public LuaValue call(LuaValue var1, LuaValue var2, LuaValue var3) {
         LuaTable var4 = var1.checktable();
         if (!var2.isvalidkey()) {
            argerror(2, "value");
         }

         var4.rawset(var2, var3);
         return var4;
      }
   }

   static final class select extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         int var2 = var1.narg() - 1;
         if (var1.arg1().equals(valueOf("#"))) {
            return valueOf(var2);
         } else {
            int var3 = var1.checkint(1);
            if (var3 == 0 || var3 < -var2) {
               argerror(1, "index out of range");
            }

            return var1.subargs(var3 < 0 ? var2 + var3 + 2 : var3 + 1);
         }
      }
   }

   static final class setmetatable extends LibFunction {
      public LuaValue call(LuaValue var1) {
         return argerror(2, "value");
      }

      public LuaValue call(LuaValue var1, LuaValue var2) {
         LuaValue var3 = var1.checktable().getmetatable();
         if (var3 != null && !var3.rawget(METATABLE).isnil()) {
            error("cannot change a protected metatable");
         }

         return var1.setmetatable(var2.isnil() ? null : var2.checktable());
      }
   }

   static final class tonumber extends LibFunction {
      public LuaValue call(LuaValue var1) {
         return var1.tonumber();
      }

      public LuaValue call(LuaValue var1, LuaValue var2) {
         if (var2.isnil()) {
            return var1.tonumber();
         } else {
            int var3 = var2.checkint();
            if (var3 < 2 || var3 > 36) {
               argerror(2, "base out of range");
            }

            return var1.checkstring().tonumber(var3);
         }
      }
   }

   static final class tostring extends LibFunction {
      public LuaValue call(LuaValue var1) {
         LuaValue var2 = var1.metatag(TOSTRING);
         if (!var2.isnil()) {
            return var2.call(var1);
         } else {
            LuaValue var3 = var1.tostring();
            return (LuaValue)(!var3.isnil() ? var3 : valueOf(var1.tojstring()));
         }
      }
   }

   static final class type extends LibFunction {
      public LuaValue call(LuaValue var1) {
         return valueOf(var1.typename());
      }
   }

   final class xpcall extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         LuaThread var2 = BaseLib.this.globals.running;
         LuaValue var3 = var2.errorfunc;
         var2.errorfunc = var1.checkvalue(2);

         Varargs var6;
         try {
            if (BaseLib.this.globals != null && BaseLib.this.globals.debuglib != null) {
               BaseLib.this.globals.debuglib.onCall(this);
            }

            try {
               return varargsOf(TRUE, var1.arg1().invoke(var1.subargs(3)));
            } catch (LuaError var17) {
               LuaValue var21 = var17.getMessageObject();
               return varargsOf(FALSE, var21 != null ? var21 : NIL);
            } catch (Exception var18) {
               String var5 = var18.getMessage();
               var6 = varargsOf(FALSE, valueOf(var5 != null ? var5 : var18.toString()));
            } finally {
               if (BaseLib.this.globals != null && BaseLib.this.globals.debuglib != null) {
                  BaseLib.this.globals.debuglib.onReturn();
               }
            }
         } finally {
            var2.errorfunc = var3;
         }

         return var6;
      }
   }
}
