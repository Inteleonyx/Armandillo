package dev.inteleonyx.armandillo.api.luaj.lib.jse;

import dev.inteleonyx.armandillo.api.luaj.LuaError;
import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.Varargs;
import dev.inteleonyx.armandillo.api.luaj.lib.VarArgFunction;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class JavaConstructor extends JavaMember {
   static final Map constructors = Collections.synchronizedMap(new HashMap());
   final Constructor constructor;

   static JavaConstructor forConstructor(Constructor var0) {
      JavaConstructor var1 = (JavaConstructor)constructors.get(var0);
      if (var1 == null) {
         constructors.put(var0, var1 = new JavaConstructor(var0));
      }

      return var1;
   }

   public static LuaValue forConstructors(JavaConstructor[] var0) {
      return new Overload(var0);
   }

   private JavaConstructor(Constructor var1) {
      super(var1.getParameterTypes(), var1.getModifiers());
      this.constructor = var1;
   }

   public Varargs invoke(Varargs var1) {
      Object[] var2 = this.convertArgs(var1);

      try {
         return CoerceJavaToLua.coerce(this.constructor.newInstance(var2));
      } catch (InvocationTargetException var4) {
         throw new LuaError(var4.getTargetException());
      } catch (Exception var5) {
         return LuaValue.error("coercion error " + var5);
      }
   }

   static class Overload extends VarArgFunction {
      final JavaConstructor[] constructors;

      public Overload(JavaConstructor[] var1) {
         this.constructors = var1;
      }

      public Varargs invoke(Varargs var1) {
         JavaConstructor var2 = null;
         int var3 = CoerceLuaToJava.SCORE_UNCOERCIBLE;

         for (int var4 = 0; var4 < this.constructors.length; var4++) {
            int var5 = this.constructors[var4].score(var1);
            if (var5 < var3) {
               var3 = var5;
               var2 = this.constructors[var4];
               if (var5 == 0) {
                  break;
               }
            }
         }

         if (var2 == null) {
            LuaValue.error("no coercible public method");
         }

         return var2.invoke(var1);
      }
   }
}
