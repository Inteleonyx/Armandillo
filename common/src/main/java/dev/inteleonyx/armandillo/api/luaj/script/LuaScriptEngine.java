package dev.inteleonyx.armandillo.api.luaj.script;

import dev.inteleonyx.armandillo.api.luaj.*;
import dev.inteleonyx.armandillo.api.luaj.lib.ThreeArgFunction;
import dev.inteleonyx.armandillo.api.luaj.lib.TwoArgFunction;
import dev.inteleonyx.armandillo.api.luaj.lib.jse.CoerceJavaToLua;

import javax.script.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

public class LuaScriptEngine extends AbstractScriptEngine implements ScriptEngine, Compilable {
   private static final String __ENGINE_VERSION__ = "Luaj-jse 3.0.2";
   private static final String __NAME__ = "Luaj";
   private static final String __SHORT_NAME__ = "Luaj";
   private static final String __LANGUAGE__ = "lua";
   private static final String __LANGUAGE_VERSION__ = "5.2";
   private static final String __ARGV__ = "arg";
   private static final String __FILENAME__ = "?";
   private static final ScriptEngineFactory myFactory = new LuaScriptEngineFactory();
   private LuajContext context = new LuajContext();

   public LuaScriptEngine() {
      this.context.setBindings(this.createBindings(), 100);
      this.setContext(this.context);
      this.put("javax.script.language_version", "5.2");
      this.put("javax.script.language", "lua");
      this.put("javax.script.engine", "Luaj");
      this.put("javax.script.engine_version", "Luaj-jse 3.0.2");
      this.put("javax.script.argv", "arg");
      this.put("javax.script.filename", "?");
      this.put("javax.script.name", "Luaj");
      this.put("THREADING", null);
   }

   @Override
   public CompiledScript compile(String var1) throws ScriptException {
      return this.compile(new StringReader(var1));
   }

   @Override
   public CompiledScript compile(Reader var1) throws ScriptException {
      try {
         Utf8Encoder var2 = new Utf8Encoder(var1);

         LuajCompiledScript var5;
         try {
            Globals var3 = this.context.globals;
            LuaFunction var4 = var3.load(var1, "script").checkfunction();
            var5 = new LuajCompiledScript(var4, var3);
         } catch (LuaError var10) {
            throw new ScriptException(var10.getMessage());
         } finally {
            var2.close();
         }

         return var5;
      } catch (Exception var12) {
         throw new ScriptException("eval threw " + var12.toString());
      }
   }

   @Override
   public Object eval(Reader var1, Bindings var2) throws ScriptException {
      return ((LuajCompiledScript)this.compile(var1)).eval(this.context.globals, var2);
   }

   @Override
   public Object eval(String var1, Bindings var2) throws ScriptException {
      return this.eval(new StringReader(var1), var2);
   }

   @Override
   protected ScriptContext getScriptContext(Bindings var1) {
      throw new IllegalStateException("LuajScriptEngine should not be allocating contexts.");
   }

   @Override
   public Bindings createBindings() {
      return new SimpleBindings();
   }

   @Override
   public Object eval(String var1, ScriptContext var2) throws ScriptException {
      return this.eval(new StringReader(var1), var2);
   }

   @Override
   public Object eval(Reader var1, ScriptContext var2) throws ScriptException {
      return this.compile(var1).eval(var2);
   }

   @Override
   public ScriptEngineFactory getFactory() {
      return myFactory;
   }

   private static LuaValue toLua(Object var0) {
      return var0 == null ? LuaValue.NIL : (var0 instanceof LuaValue ? (LuaValue)var0 : CoerceJavaToLua.coerce(var0));
   }

   private static Object toJava(LuaValue var0) {
      switch (var0.type()) {
         case 0:
            return null;
         case 1:
         case 2:
         case 5:
         case 6:
         default:
            return var0;
         case 3:
            return var0.isinttype() ? Integer.valueOf(var0.toint()) : Double.valueOf(var0.todouble());
         case 4:
            return var0.tojstring();
         case 7:
            return var0.checkuserdata(Object.class);
      }
   }

   private static Object toJava(Varargs var0) {
      int var1 = var0.narg();
      switch (var1) {
         case 0:
            return null;
         case 1:
            return toJava(var0.arg1());
         default:
            Object[] var2 = new Object[var1];

            for (int var3 = 0; var3 < var1; var3++) {
               var2[var3] = toJava(var0.arg(var3 + 1));
            }

            return var2;
      }
   }

   static class BindingsMetatable extends LuaTable {
      BindingsMetatable(final Bindings var1) {
         this.rawset(LuaValue.INDEX, new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue var1x, LuaValue var2) {
               return var2.isstring() ? LuaScriptEngine.toLua(var1.get(var2.tojstring())) : this.rawget(var2);
            }
         });
         this.rawset(LuaValue.NEWINDEX, new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue var1x, LuaValue var2, LuaValue var3) {
               if (var2.isstring()) {
                  String var4 = var2.tojstring();
                  Object var5 = LuaScriptEngine.toJava(var3);
                  if (var5 == null) {
                     var1.remove(var4);
                  } else {
                     var1.put(var4, var5);
                  }
               } else {
                  this.rawset(var2, var3);
               }

               return LuaValue.NONE;
            }
         });
      }
   }

   class LuajCompiledScript extends CompiledScript {
      final LuaFunction function;
      final Globals compiling_globals;

      LuajCompiledScript(LuaFunction var2, Globals var3) {
         this.function = var2;
         this.compiling_globals = var3;
      }

      @Override
      public ScriptEngine getEngine() {
         return LuaScriptEngine.this;
      }

      @Override
      public Object eval() throws ScriptException {
         return this.eval(LuaScriptEngine.this.getContext());
      }

      @Override
      public Object eval(Bindings var1) throws ScriptException {
         return this.eval(((LuajContext)LuaScriptEngine.this.getContext()).globals, var1);
      }

      @Override
      public Object eval(ScriptContext var1) throws ScriptException {
         return this.eval(((LuajContext)var1).globals, var1.getBindings(100));
      }

      Object eval(Globals var1, Bindings var2) throws ScriptException {
         var1.setmetatable(new BindingsMetatable(var2));
         LuaFunction var3 = this.function;
         if (var3.isclosure()) {
            var3 = new LuaClosure(var3.checkclosure().p, var1);
         } else {
            try {
               var3 = (LuaFunction)var3.getClass().newInstance();
            } catch (Exception var5) {
               throw new ScriptException(var5);
            }

            var3.initupvalue1(var1);
         }

         return LuaScriptEngine.toJava(var3.invoke(LuaValue.NONE));
      }
   }

   private final class Utf8Encoder extends InputStream {
      private final Reader r;
      private final int[] buf = new int[2];
      private int n;

      private Utf8Encoder(Reader var2) {
         this.r = var2;
      }

      @Override
      public int read() throws IOException {
         if (this.n > 0) {
            return this.buf[--this.n];
         } else {
            int var1 = this.r.read();
            if (var1 < 128) {
               return var1;
            } else {
               this.n = 0;
               if (var1 < 2048) {
                  this.buf[this.n++] = 128 | var1 & 63;
                  return 192 | var1 >> 6 & 31;
               } else {
                  this.buf[this.n++] = 128 | var1 & 63;
                  this.buf[this.n++] = 128 | var1 >> 6 & 63;
                  return 224 | var1 >> 12 & 15;
               }
            }
         }
      }
   }
}
