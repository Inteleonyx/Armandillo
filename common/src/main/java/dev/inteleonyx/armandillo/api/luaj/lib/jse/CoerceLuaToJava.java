package dev.inteleonyx.armandillo.api.luaj.lib.jse;

import dev.inteleonyx.armandillo.api.luaj.LuaString;
import dev.inteleonyx.armandillo.api.luaj.LuaValue;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CoerceLuaToJava {
   static int SCORE_NULL_VALUE = 16;
   static int SCORE_WRONG_TYPE = 256;
   static int SCORE_UNCOERCIBLE = 65536;
   static final Map COERCIONS = Collections.synchronizedMap(new HashMap());

   public static Object coerce(LuaValue var0, Class var1) {
      return getCoercion(var1).coerce(var0);
   }

   static final int inheritanceLevels(Class var0, Class var1) {
      if (var1 == null) {
         return SCORE_UNCOERCIBLE;
      } else if (var0 == var1) {
         return 0;
      } else {
         int var2 = Math.min(SCORE_UNCOERCIBLE, inheritanceLevels(var0, var1.getSuperclass()) + 1);
         Class[] var3 = var1.getInterfaces();

         for (int var4 = 0; var4 < var3.length; var4++) {
            var2 = Math.min(var2, inheritanceLevels(var0, var3[var4]) + 1);
         }

         return var2;
      }
   }

   static Coercion getCoercion(Class var0) {
      Coercion var1 = (Coercion)COERCIONS.get(var0);
      if (var1 != null) {
         return var1;
      } else {
         if (var0.isArray()) {
            Class var2 = var0.getComponentType();
            var1 = new ArrayCoercion(var0.getComponentType());
         } else {
            var1 = new ObjectCoercion(var0);
         }

         COERCIONS.put(var0, var1);
         return var1;
      }
   }

   static {
      BoolCoercion var0 = new BoolCoercion();
      NumericCoercion var1 = new NumericCoercion(0);
      NumericCoercion var2 = new NumericCoercion(1);
      NumericCoercion var3 = new NumericCoercion(2);
      NumericCoercion var4 = new NumericCoercion(3);
      NumericCoercion var5 = new NumericCoercion(4);
      NumericCoercion var6 = new NumericCoercion(5);
      NumericCoercion var7 = new NumericCoercion(6);
      StringCoercion var8 = new StringCoercion(0);
      StringCoercion var9 = new StringCoercion(1);
      COERCIONS.put(boolean.class, var0);
      COERCIONS.put(Boolean.class, var0);

      COERCIONS.put(byte.class, var1);
      COERCIONS.put(Byte.class, var1);

      COERCIONS.put(char.class, var2);
      COERCIONS.put(Character.class, var2);

      COERCIONS.put(short.class, var3);
      COERCIONS.put(Short.class, var3);

      COERCIONS.put(int.class, var4);
      COERCIONS.put(Integer.class, var4);

      COERCIONS.put(long.class, var5);
      COERCIONS.put(Long.class, var5);

      COERCIONS.put(float.class, var6);
      COERCIONS.put(Float.class, var6);

      COERCIONS.put(double.class, var7);
      COERCIONS.put(Double.class, var7);

      COERCIONS.put(String.class, var8);

      COERCIONS.put(byte[].class, var9);
   }

   static final class ArrayCoercion implements Coercion {
      final Class componentType;
      final Coercion componentCoercion;

      public ArrayCoercion(Class var1) {
         this.componentType = var1;
         this.componentCoercion = CoerceLuaToJava.getCoercion(var1);
      }

      public String toString() {
         return "ArrayCoercion(" + this.componentType.getName() + ")";
      }

      public int score(LuaValue var1) {
         switch (var1.type()) {
            case 0:
               return CoerceLuaToJava.SCORE_NULL_VALUE;
            case 5:
               return var1.length() == 0 ? 0 : this.componentCoercion.score(var1.get(1));
            case 7:
               return CoerceLuaToJava.inheritanceLevels(this.componentType, var1.touserdata().getClass().getComponentType());
            default:
               return CoerceLuaToJava.SCORE_UNCOERCIBLE;
         }
      }

      public Object coerce(LuaValue var1) {
         switch (var1.type()) {
            case 0:
               return null;
            case 5:
               int var2 = var1.length();
               Object var3 = Array.newInstance(this.componentType, var2);

               for (int var4 = 0; var4 < var2; var4++) {
                  Array.set(var3, var4, this.componentCoercion.coerce(var1.get(var4 + 1)));
               }

               return var3;
            case 7:
               return var1.touserdata();
            default:
               return null;
         }
      }
   }

   static final class BoolCoercion implements Coercion {
      public String toString() {
         return "BoolCoercion()";
      }

      public int score(LuaValue var1) {
         switch (var1.type()) {
            case 1:
               return 0;
            default:
               return 1;
         }
      }

      public Object coerce(LuaValue var1) {
         return var1.toboolean() ? Boolean.TRUE : Boolean.FALSE;
      }
   }

   interface Coercion {
      int score(LuaValue var1);

      Object coerce(LuaValue var1);
   }

   static final class NumericCoercion implements Coercion {
      static final int TARGET_TYPE_BYTE = 0;
      static final int TARGET_TYPE_CHAR = 1;
      static final int TARGET_TYPE_SHORT = 2;
      static final int TARGET_TYPE_INT = 3;
      static final int TARGET_TYPE_LONG = 4;
      static final int TARGET_TYPE_FLOAT = 5;
      static final int TARGET_TYPE_DOUBLE = 6;
      static final String[] TYPE_NAMES = new String[]{"byte", "char", "short", "int", "long", "float", "double"};
      final int targetType;

      public String toString() {
         return "NumericCoercion(" + TYPE_NAMES[this.targetType] + ")";
      }

      NumericCoercion(int var1) {
         this.targetType = var1;
      }

      public int score(LuaValue var1) {
         byte var2 = 0;
         if (var1.type() == 4) {
            var1 = var1.tonumber();
            if (var1.isnil()) {
               return CoerceLuaToJava.SCORE_UNCOERCIBLE;
            }

            var2 = 4;
         }

         if (var1.isint()) {
            switch (this.targetType) {
               case 0:
                  int var10 = var1.toint();
                  return var2 + (var10 == (byte)var10 ? 0 : CoerceLuaToJava.SCORE_WRONG_TYPE);
               case 1:
                  int var9 = var1.toint();
                  return var2 + (var9 == (byte)var9 ? 1 : (var9 == (char)var9 ? 0 : CoerceLuaToJava.SCORE_WRONG_TYPE));
               case 2:
                  int var8 = var1.toint();
                  return var2 + (var8 == (byte)var8 ? 1 : (var8 == (short)var8 ? 0 : CoerceLuaToJava.SCORE_WRONG_TYPE));
               case 3:
                  int var7 = var1.toint();
                  return var2 + (var7 == (byte)var7 ? 2 : (var7 != (char)var7 && var7 != (short)var7 ? 0 : 1));
               case 4:
                  return var2 + 1;
               case 5:
                  return var2 + 1;
               case 6:
                  return var2 + 2;
               default:
                  return CoerceLuaToJava.SCORE_WRONG_TYPE;
            }
         } else if (var1.isnumber()) {
            switch (this.targetType) {
               case 0:
                  return CoerceLuaToJava.SCORE_WRONG_TYPE;
               case 1:
                  return CoerceLuaToJava.SCORE_WRONG_TYPE;
               case 2:
                  return CoerceLuaToJava.SCORE_WRONG_TYPE;
               case 3:
                  return CoerceLuaToJava.SCORE_WRONG_TYPE;
               case 4:
                  double var6 = var1.todouble();
                  return var2 + (var6 == (long)var6 ? 0 : CoerceLuaToJava.SCORE_WRONG_TYPE);
               case 5:
                  double var5 = var1.todouble();
                  return var2 + (var5 == (float)var5 ? 0 : CoerceLuaToJava.SCORE_WRONG_TYPE);
               case 6:
                  double var3 = var1.todouble();
                  return var2 + (var3 != (long)var3 && var3 != (float)var3 ? 0 : 1);
               default:
                  return CoerceLuaToJava.SCORE_WRONG_TYPE;
            }
         } else {
            return CoerceLuaToJava.SCORE_UNCOERCIBLE;
         }
      }

      public Object coerce(LuaValue var1) {
         switch (this.targetType) {
            case 0:
               return Byte.valueOf((byte)var1.toint());
            case 1:
               return Character.valueOf((char)var1.toint());
            case 2:
               return Short.valueOf((short)var1.toint());
            case 3:
               return Integer.valueOf(var1.toint());
            case 4:
               return Long.valueOf((long)var1.todouble());
            case 5:
               return Float.valueOf((float)var1.todouble());
            case 6:
               return Double.valueOf(var1.todouble());
            default:
               return null;
         }
      }
   }

   static final class ObjectCoercion implements Coercion {
      final Class targetType;

      ObjectCoercion(Class var1) {
         this.targetType = var1;
      }

      public String toString() {
         return "ObjectCoercion(" + this.targetType.getName() + ")";
      }

      public int score(LuaValue var1) {
         switch (var1.type()) {
            case 0: // NIL
               return CoerceLuaToJava.SCORE_NULL_VALUE;
            case 1: // BOOLEAN
               return CoerceLuaToJava.inheritanceLevels(this.targetType, Boolean.class);
            case 2:
            case 5:
            case 6:
            default:
               return CoerceLuaToJava.inheritanceLevels(this.targetType, var1.getClass());
            case 3: // NUMBER
               return CoerceLuaToJava.inheritanceLevels(
                       this.targetType,
                       var1.isint() ? Integer.class : Double.class
               );
            case 4: // STRING
               return CoerceLuaToJava.inheritanceLevels(this.targetType, String.class);
            case 7: // USERDATA
               return CoerceLuaToJava.inheritanceLevels(this.targetType, var1.touserdata().getClass());
         }
      }

      public Object coerce(LuaValue var1) {
         switch (var1.type()) {
            case 0:
               return null;
            case 1:
               return var1.toboolean() ? Boolean.TRUE : Boolean.FALSE;
            case 2:
            case 5:
            case 6:
            default:
               return var1;
            case 3:
               return var1.isint() ? Integer.valueOf(var1.toint()) : Double.valueOf(var1.todouble());
            case 4:
               return var1.tojstring();
            case 7:
               return var1.optuserdata(this.targetType, null);
         }
      }
   }

   static final class StringCoercion implements Coercion {
      public static final int TARGET_TYPE_STRING = 0;
      public static final int TARGET_TYPE_BYTES = 1;
      final int targetType;

      public StringCoercion(int var1) {
         this.targetType = var1;
      }

      public String toString() {
         return "StringCoercion(" + (this.targetType == 0 ? "String" : "byte[]") + ")";
      }

      public int score(LuaValue var1) {
         switch (var1.type()) {
            case 0:
               return CoerceLuaToJava.SCORE_NULL_VALUE;
            case 4:
               return var1.checkstring().isValidUtf8() ? (this.targetType == 0 ? 0 : 1) : (this.targetType == 1 ? 0 : CoerceLuaToJava.SCORE_WRONG_TYPE);
            default:
               return this.targetType == 0 ? CoerceLuaToJava.SCORE_WRONG_TYPE : CoerceLuaToJava.SCORE_UNCOERCIBLE;
         }
      }

      public Object coerce(LuaValue var1) {
         if (var1.isnil()) {
            return null;
         } else if (this.targetType == 0) {
            return var1.tojstring();
         } else {
            LuaString var2 = var1.checkstring();
            byte[] var3 = new byte[var2.m_length];
            var2.copyInto(0, var3, 0, var3.length);
            return var3;
         }
      }
   }
}
