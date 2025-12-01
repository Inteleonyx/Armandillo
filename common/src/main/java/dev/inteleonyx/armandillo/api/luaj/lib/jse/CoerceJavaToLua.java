package dev.inteleonyx.armandillo.api.luaj.lib.jse;

import dev.inteleonyx.armandillo.api.luaj.LuaDouble;
import dev.inteleonyx.armandillo.api.luaj.LuaInteger;
import dev.inteleonyx.armandillo.api.luaj.LuaString;
import dev.inteleonyx.armandillo.api.luaj.LuaValue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CoerceJavaToLua {
   static final Map COERCIONS = Collections.synchronizedMap(new HashMap());
   static final Coercion instanceCoercion = new InstanceCoercion();
   static final Coercion arrayCoercion = new ArrayCoercion();
   static final Coercion luaCoercion = new LuaCoercion();

   public static LuaValue coerce(Object var0) {
      if (var0 == null) {
         return LuaValue.NIL;
      } else {
         Class var1 = var0.getClass();
         Coercion var2 = (Coercion)COERCIONS.get(var1);
         if (var2 == null) {
            var2 = var1.isArray() ? arrayCoercion : (var0 instanceof LuaValue ? luaCoercion : instanceCoercion);
            COERCIONS.put(var1, var2);
         }

         return var2.coerce(var0);
      }
   }

   static {
      BoolCoercion var0 = new BoolCoercion();
      IntCoercion var1 = new IntCoercion();
      CharCoercion var2 = new CharCoercion();
      DoubleCoercion var3 = new DoubleCoercion();
      StringCoercion var4 = new StringCoercion();
      BytesCoercion var5 = new BytesCoercion();
      ClassCoercion var6 = new ClassCoercion();
      COERCIONS.put(Boolean.class, var0);
      COERCIONS.put(Byte.class, var1);
      COERCIONS.put(Character.class, var2);
      COERCIONS.put(Short.class, var1);
      COERCIONS.put(Integer.class, var1);
      COERCIONS.put(Long.class, var3);
      COERCIONS.put(Float.class, var3);
      COERCIONS.put(Double.class, var3);
      COERCIONS.put(String.class, var4);
      COERCIONS.put(byte[].class, var5);
      COERCIONS.put(Class.class, var6);

   }

   private static final class ArrayCoercion implements Coercion {
      private ArrayCoercion() {
      }

      public LuaValue coerce(Object var1) {
         return new JavaArray(var1);
      }
   }

   private static final class BoolCoercion implements Coercion {
      private BoolCoercion() {
      }

      public LuaValue coerce(Object var1) {
         Boolean var2 = (Boolean)var1;
         return var2 ? LuaValue.TRUE : LuaValue.FALSE;
      }
   }

   private static final class BytesCoercion implements Coercion {
      private BytesCoercion() {
      }

      public LuaValue coerce(Object var1) {
         return LuaValue.valueOf((byte[])var1);
      }
   }

   private static final class CharCoercion implements Coercion {
      private CharCoercion() {
      }

      public LuaValue coerce(Object var1) {
         Character var2 = (Character)var1;
         return LuaInteger.valueOf(var2);
      }
   }

   private static final class ClassCoercion implements Coercion {
      private ClassCoercion() {
      }

      public LuaValue coerce(Object var1) {
         return JavaClass.forClass((Class)var1);
      }
   }

   interface Coercion {
      LuaValue coerce(Object var1);
   }

   private static final class DoubleCoercion implements Coercion {
      private DoubleCoercion() {
      }

      public LuaValue coerce(Object var1) {
         Number var2 = (Number)var1;
         return LuaDouble.valueOf(var2.doubleValue());
      }
   }

   private static final class InstanceCoercion implements Coercion {
      private InstanceCoercion() {
      }

      public LuaValue coerce(Object var1) {
         return new JavaInstance(var1);
      }
   }

   private static final class IntCoercion implements Coercion {
      private IntCoercion() {
      }

      public LuaValue coerce(Object var1) {
         Number var2 = (Number)var1;
         return LuaInteger.valueOf(var2.intValue());
      }
   }

   private static final class LuaCoercion implements Coercion {
      private LuaCoercion() {
      }

      public LuaValue coerce(Object var1) {
         return (LuaValue)var1;
      }
   }

   private static final class StringCoercion implements Coercion {
      private StringCoercion() {
      }

      public LuaValue coerce(Object var1) {
         return LuaString.valueOf(var1.toString());
      }
   }
}
