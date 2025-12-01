package dev.inteleonyx.armandillo.api.luaj.lib.jse;

import dev.inteleonyx.armandillo.api.luaj.LuaError;
import dev.inteleonyx.armandillo.api.luaj.LuaFunction;
import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.Varargs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class JavaMethod extends JavaMember {
   static final Map methods = Collections.synchronizedMap(new HashMap());
   final Method method;

   static JavaMethod forMethod(Method var0) {
      JavaMethod var1 = (JavaMethod)methods.get(var0);
      if (var1 == null) {
         methods.put(var0, var1 = new JavaMethod(var0));
      }

      return var1;
   }

   static LuaFunction forMethods(JavaMethod[] var0) {
      return new Overload(var0);
   }

   private JavaMethod(Method var1) {
      super(var1.getParameterTypes(), var1.getModifiers());
      this.method = var1;

      try {
         if (!var1.isAccessible()) {
            var1.setAccessible(true);
         }
      } catch (SecurityException var3) {
      }
   }

   public LuaValue call() {
      return error("method cannot be called without instance");
   }

   public LuaValue call(LuaValue var1) {
      return this.invokeMethod(var1.checkuserdata(), LuaValue.NONE);
   }

   public LuaValue call(LuaValue var1, LuaValue var2) {
      return this.invokeMethod(var1.checkuserdata(), var2);
   }

   public LuaValue call(LuaValue var1, LuaValue var2, LuaValue var3) {
      return this.invokeMethod(var1.checkuserdata(), LuaValue.varargsOf(var2, var3));
   }

   public Varargs invoke(Varargs var1) {
      return this.invokeMethod(var1.checkuserdata(1), var1.subargs(2));
   }

   LuaValue invokeMethod(Object var1, Varargs var2) {
      Object[] var3 = this.convertArgs(var2);

      try {
         return CoerceJavaToLua.coerce(this.method.invoke(var1, var3));
      } catch (InvocationTargetException var5) {
         throw new LuaError(var5.getTargetException());
      } catch (Exception var6) {
         return LuaValue.error("coercion error " + var6);
      }
   }

   static class Overload extends LuaFunction {
      final JavaMethod[] methods;

      Overload(JavaMethod[] var1) {
         this.methods = var1;
      }

      public LuaValue call() {
         return error("method cannot be called without instance");
      }

      public LuaValue call(LuaValue var1) {
         return this.invokeBestMethod(var1.checkuserdata(), LuaValue.NONE);
      }

      public LuaValue call(LuaValue var1, LuaValue var2) {
         return this.invokeBestMethod(var1.checkuserdata(), var2);
      }

      public LuaValue call(LuaValue var1, LuaValue var2, LuaValue var3) {
         return this.invokeBestMethod(var1.checkuserdata(), LuaValue.varargsOf(var2, var3));
      }

      public Varargs invoke(Varargs var1) {
         return this.invokeBestMethod(var1.checkuserdata(1), var1.subargs(2));
      }

      private LuaValue invokeBestMethod(Object var1, Varargs var2) {
         JavaMethod var3 = null;
         int var4 = CoerceLuaToJava.SCORE_UNCOERCIBLE;

         for (int var5 = 0; var5 < this.methods.length; var5++) {
            int var6 = this.methods[var5].score(var2);
            if (var6 < var4) {
               var4 = var6;
               var3 = this.methods[var5];
               if (var6 == 0) {
                  break;
               }
            }
         }

         if (var3 == null) {
            LuaValue.error("no coercible public method");
         }

         return var3.invokeMethod(var1, var2);
      }
   }
}
