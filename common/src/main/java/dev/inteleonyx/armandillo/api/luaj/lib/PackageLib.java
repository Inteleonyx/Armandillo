package dev.inteleonyx.armandillo.api.luaj.lib;

import dev.inteleonyx.armandillo.api.luaj.*;

import java.io.IOException;
import java.io.InputStream;

public class PackageLib extends TwoArgFunction {
   public static final String DEFAULT_LUA_PATH;
   static final LuaString _LOADED;
   private static final LuaString _LOADLIB;
   static final LuaString _PRELOAD;
   static final LuaString _PATH;
   static final LuaString _SEARCHPATH;
   static final LuaString _SEARCHERS;
   Globals globals;
   LuaTable package_;
   public preload_searcher preload_searcher;
   public lua_searcher lua_searcher;
   public java_searcher java_searcher;
   private static final LuaString _SENTINEL;
   private static final String FILE_SEP;

   public LuaValue call(LuaValue var1, LuaValue var2) {
      this.globals = var2.checkglobals();
      this.globals.set("require", new require());
      this.package_ = new LuaTable();
      this.package_.set(_LOADED, new LuaTable());
      this.package_.set(_PRELOAD, new LuaTable());
      this.package_.set(_PATH, LuaValue.valueOf(DEFAULT_LUA_PATH));
      this.package_.set(_LOADLIB, new loadlib());
      this.package_.set(_SEARCHPATH, new searchpath());
      LuaTable var3 = new LuaTable();
      var3.set(1, this.preload_searcher = new preload_searcher());
      var3.set(2, this.lua_searcher = new lua_searcher());
      var3.set(3, this.java_searcher = new java_searcher());
      this.package_.set(_SEARCHERS, var3);
      this.package_.get(_LOADED).set("package", this.package_);
      var2.set("package", this.package_);
      this.globals.package_ = this;
      return var2;
   }

   public void setIsLoaded(String var1, LuaTable var2) {
      this.package_.get(_LOADED).set(var1, var2);
   }

   public void setLuaPath(String var1) {
      this.package_.set(_PATH, LuaValue.valueOf(var1));
   }

   public String tojstring() {
      return "package";
   }

   public static final String toClassname(String var0) {
      int var1 = var0.length();
      int var2 = var1;
      if (var0.endsWith(".lua")) {
         var2 = var1 - 4;
      }

      for (int var3 = 0; var3 < var2; var3++) {
         char var4 = var0.charAt(var3);
         if (!isClassnamePart(var4) || var4 == '/' || var4 == '\\') {
            StringBuffer var5 = new StringBuffer(var2);

            for (int var6 = 0; var6 < var2; var6++) {
               var4 = var0.charAt(var6);
               var5.append((char)(isClassnamePart(var4) ? var4 : (var4 != '/' && var4 != '\\' ? '_' : '.')));
            }

            return var5.toString();
         }
      }

      return var1 == var2 ? var0 : var0.substring(0, var2);
   }

   private static final boolean isClassnamePart(char var0) {
      if ((var0 < 'a' || var0 > 'z') && (var0 < 'A' || var0 > 'Z') && (var0 < '0' || var0 > '9')) {
         switch (var0) {
            case '$':
            case '.':
            case '_':
               return true;
            default:
               return false;
         }
      } else {
         return true;
      }
   }

   static {
      String var0 = null;

      try {
         var0 = System.getProperty("luaj.package.path");
      } catch (Exception var2) {
         System.out.println(var2.toString());
      }

      if (var0 == null) {
         var0 = "?.lua";
      }

      DEFAULT_LUA_PATH = var0;
      _LOADED = valueOf("loaded");
      _LOADLIB = valueOf("loadlib");
      _PRELOAD = valueOf("preload");
      _PATH = valueOf("path");
      _SEARCHPATH = valueOf("searchpath");
      _SEARCHERS = valueOf("searchers");
      _SENTINEL = valueOf("\u0001");
      FILE_SEP = System.getProperty("file.separator");
   }

   public class java_searcher extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         String var2 = var1.checkjstring(1);
         String var3 = PackageLib.toClassname(var2);
         Class var4 = null;
         Object var5 = null;

         try {
            var4 = Class.forName(var3);
            var5 = (LuaValue)var4.newInstance();
            if (((LuaValue)var5).isfunction()) {
               ((LuaFunction)var5).initupvalue1(PackageLib.this.globals);
            }

            return varargsOf((LuaValue)var5, PackageLib.this.globals);
         } catch (ClassNotFoundException var7) {
            return valueOf("\n\tno class '" + var3 + "'");
         } catch (Exception var8) {
            return valueOf("\n\tjava load failed on '" + var3 + "', " + var8);
         }
      }
   }

   public static class loadlib extends VarArgFunction {
      public Varargs loadlib(Varargs var1) {
         var1.checkstring(1);
         return varargsOf(NIL, valueOf("dynamic libraries not enabled"), valueOf("absent"));
      }
   }

   public class lua_searcher extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         LuaString var2 = var1.checkstring(1);
         LuaValue var3 = PackageLib.this.package_.get(PackageLib._PATH);
         if (!var3.isstring()) {
            return valueOf("package.path is not a string");
         } else {
            Varargs var4 = PackageLib.this.package_.get(PackageLib._SEARCHPATH).invoke(varargsOf(var2, var3));
            if (!var4.isstring(1)) {
               return var4.arg(2).tostring();
            } else {
               LuaString var5 = var4.arg1().strvalue();
               LuaValue var6 = PackageLib.this.globals.loadfile(var5.tojstring());
               return var6.arg1().isfunction() ? LuaValue.varargsOf(var6.arg1(), var5) : varargsOf(NIL, valueOf("'" + var5 + "': " + var6.arg(2).tojstring()));
            }
         }
      }
   }

   public class preload_searcher extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         LuaString var2 = var1.checkstring(1);
         LuaValue var3 = PackageLib.this.package_.get(PackageLib._PRELOAD).get(var2);
         return (Varargs)(var3.isnil() ? valueOf("\n\tno field package.preload['" + var2 + "']") : var3);
      }
   }

   public class require extends OneArgFunction {
      public LuaValue call(LuaValue var1) {
         LuaString var2 = var1.checkstring();
         LuaValue var3 = PackageLib.this.package_.get(PackageLib._LOADED);
         LuaValue var4 = var3.get(var2);
         if (var4.toboolean()) {
            if (var4 == PackageLib._SENTINEL) {
               error("loop or previous error loading module '" + var2 + "'");
            }

            return var4;
         } else {
            LuaTable var5 = PackageLib.this.package_.get(PackageLib._SEARCHERS).checktable();
            StringBuffer var6 = new StringBuffer();
            Object var7 = null;
            int var8 = 1;

            while (true) {
               LuaValue var9 = var5.get(var8);
               if (var9.isnil()) {
                  error("module '" + var2 + "' not found: " + var2 + var6);
               }

               var7 = var9.invoke(var2);
               if (((Varargs)var7).isfunction(1)) {
                  var3.set(var2, PackageLib._SENTINEL);
                  var4 = ((Varargs)var7).arg1().call(var2, ((Varargs)var7).arg(2));
                  if (!var4.isnil()) {
                     var3.set(var2, var4);
                  } else if ((var4 = var3.get(var2)) == PackageLib._SENTINEL) {
                     var4 = LuaValue.TRUE;
                     var3.set(var2, LuaValue.TRUE);
                  }

                  return var4;
               }

               if (((Varargs)var7).isstring(1)) {
                  var6.append(((Varargs)var7).tojstring(1));
               }

               var8++;
            }
         }
      }
   }

   public class searchpath extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         String var2 = var1.checkjstring(1);
         String var3 = var1.checkjstring(2);
         String var4 = var1.optjstring(3, ".");
         String var5 = var1.optjstring(4, PackageLib.FILE_SEP);
         int var6 = -1;
         int var7 = var3.length();
         StringBuffer var8 = null;
         var2 = var2.replace(var4.charAt(0), var5.charAt(0));

         while (var6 < var7) {
            int var9 = var6 + 1;
            var6 = var3.indexOf(59, var9);
            if (var6 < 0) {
               var6 = var3.length();
            }

            String var10 = var3.substring(var9, var6);
            int var11 = var10.indexOf(63);
            String var12 = var10;
            if (var11 >= 0) {
               var12 = var10.substring(0, var11) + var2 + var10.substring(var11 + 1);
            }

            InputStream var13 = PackageLib.this.globals.finder.findResource(var12);
            if (var13 != null) {
               try {
                  var13.close();
               } catch (IOException var15) {
               }

               return valueOf(var12);
            }

            if (var8 == null) {
               var8 = new StringBuffer();
            }

            var8.append("\n\t" + var12);
         }

         return varargsOf(NIL, valueOf(var8.toString()));
      }
   }
}
