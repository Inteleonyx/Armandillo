package dev.inteleonyx.armandillo.api.luaj.luajc;

import dev.inteleonyx.armandillo.api.luaj.Globals;
import dev.inteleonyx.armandillo.api.luaj.LuaFunction;
import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.Prototype;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Hashtable;

public class LuaJC implements Globals.Loader {
   public static final LuaJC instance = new LuaJC();

   public static final void install(Globals var0) {
      var0.loader = instance;
   }

   protected LuaJC() {
   }

   public Hashtable compileAll(InputStream var1, String var2, String var3, Globals var4, boolean var5) throws IOException {
      String var6 = toStandardJavaClassName(var2);
      Prototype var7 = var4.loadPrototype(var1, var6, "bt");
      return this.compileProtoAndSubProtos(var7, var6, var3, var5);
   }

   public Hashtable compileAll(Reader var1, String var2, String var3, Globals var4, boolean var5) throws IOException {
      String var6 = toStandardJavaClassName(var2);
      Prototype var7 = var4.compilePrototype(var1, var6);
      return this.compileProtoAndSubProtos(var7, var6, var3, var5);
   }

   private Hashtable compileProtoAndSubProtos(Prototype var1, String var2, String var3, boolean var4) throws IOException {
      String var5 = toStandardLuaFileName(var3);
      Hashtable var6 = new Hashtable();
      JavaGen var7 = new JavaGen(var1, var2, var5, var4);
      this.insert(var6, var7);
      return var6;
   }

   private void insert(Hashtable var1, JavaGen var2) {
      var1.put(var2.classname, var2.bytecode);
      int var3 = 0;

      for (int var4 = var2.inners != null ? var2.inners.length : 0; var3 < var4; var3++) {
         this.insert(var1, var2.inners[var3]);
      }
   }

   public LuaFunction load(Prototype var1, String var2, LuaValue var3) throws IOException {
      String var4 = toStandardLuaFileName(var2);
      String var5 = toStandardJavaClassName(var4);
      JavaLoader var6 = new JavaLoader();
      return var6.load(var1, var5, var4, var3);
   }

   private static String toStandardJavaClassName(String var0) {
      String var1 = toStub(var0);
      StringBuffer var2 = new StringBuffer();
      int var3 = 0;

      for (int var4 = var1.length(); var3 < var4; var3++) {
         char var5 = var1.charAt(var3);
         var2.append((var3 != 0 || !Character.isJavaIdentifierStart(var5)) && (var3 <= 0 || !Character.isJavaIdentifierPart(var5)) ? '_' : var5);
      }

      return var2.toString();
   }

   private static String toStandardLuaFileName(String var0) {
      String var1 = toStub(var0);
      String var2 = var1.replace('.', '/') + ".lua";
      return var2.startsWith("@") ? var2.substring(1) : var2;
   }

   private static String toStub(String var0) {
      return var0.endsWith(".lua") ? var0.substring(0, var0.length() - 4) : var0;
   }
}
