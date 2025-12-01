package dev.inteleonyx.armandillo.api.luaj.lib;

import dev.inteleonyx.armandillo.api.luaj.LuaDouble;
import dev.inteleonyx.armandillo.api.luaj.LuaTable;
import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.Varargs;

import java.util.Random;

public class MathLib extends TwoArgFunction {
   public static MathLib MATHLIB = null;

   public MathLib() {
      MATHLIB = this;
   }

   public LuaValue call(LuaValue var1, LuaValue var2) {
      LuaTable var3 = new LuaTable(0, 30);
      var3.set("abs", new abs());
      var3.set("ceil", new ceil());
      var3.set("cos", new cos());
      var3.set("deg", new deg());
      var3.set("exp", new exp(this));
      var3.set("floor", new floor());
      var3.set("fmod", new fmod());
      var3.set("frexp", new frexp());
      var3.set("huge", LuaDouble.POSINF);
      var3.set("ldexp", new ldexp());
      var3.set("max", new max());
      var3.set("min", new min());
      var3.set("modf", new modf());
      var3.set("pi", Math.PI);
      var3.set("pow", new pow());
      random var4;
      var3.set("random", var4 = new random());
      var3.set("randomseed", new randomseed(var4));
      var3.set("rad", new rad());
      var3.set("sin", new sin());
      var3.set("sqrt", new sqrt());
      var3.set("tan", new tan());
      var2.set("math", var3);
      if (!var2.get("package").isnil()) {
         var2.get("package").get("loaded").set("math", var3);
      }

      return var3;
   }

   public static LuaValue dpow(double var0, double var2) {
      return LuaDouble.valueOf(MATHLIB != null ? MATHLIB.dpow_lib(var0, var2) : dpow_default(var0, var2));
   }

   public static double dpow_d(double var0, double var2) {
      return MATHLIB != null ? MATHLIB.dpow_lib(var0, var2) : dpow_default(var0, var2);
   }

   public double dpow_lib(double var1, double var3) {
      return dpow_default(var1, var3);
   }

   protected static double dpow_default(double var0, double var2) {
      if (var2 < 0.0) {
         return 1.0 / dpow_default(var0, -var2);
      } else {
         double var4 = 1.0;
         int var6 = (int)var2;

         for (double var7 = var0; var6 > 0; var7 *= var7) {
            if ((var6 & 1) != 0) {
               var4 *= var7;
            }

            var6 >>= 1;
         }

         if ((var2 = var2 - var6) > 0.0) {
            for (int var10 = (int)(65536.0 * var2); (var10 & 65535) != 0; var10 <<= 1) {
               var0 = Math.sqrt(var0);
               if ((var10 & 32768) != 0) {
                  var4 *= var0;
               }
            }
         }

         return var4;
      }
   }

   protected abstract static class BinaryOp extends TwoArgFunction {
      public LuaValue call(LuaValue var1, LuaValue var2) {
         return valueOf(this.call(var1.checkdouble(), var2.checkdouble()));
      }

      protected abstract double call(double var1, double var3);
   }

   protected abstract static class UnaryOp extends OneArgFunction {
      public LuaValue call(LuaValue var1) {
         return valueOf(this.call(var1.checkdouble()));
      }

      protected abstract double call(double var1);
   }

   static final class abs extends UnaryOp {
      protected double call(double var1) {
         return Math.abs(var1);
      }
   }

   static final class ceil extends UnaryOp {
      protected double call(double var1) {
         return Math.ceil(var1);
      }
   }

   static final class cos extends UnaryOp {
      protected double call(double var1) {
         return Math.cos(var1);
      }
   }

   static final class deg extends UnaryOp {
      protected double call(double var1) {
         return Math.toDegrees(var1);
      }
   }

   static final class exp extends UnaryOp {
      final MathLib mathlib;

      exp(MathLib var1) {
         this.mathlib = var1;
      }

      protected double call(double var1) {
         return this.mathlib.dpow_lib(Math.E, var1);
      }
   }

   static final class floor extends UnaryOp {
      protected double call(double var1) {
         return Math.floor(var1);
      }
   }

   static final class fmod extends TwoArgFunction {
      public LuaValue call(LuaValue var1, LuaValue var2) {
         return var1.islong() && var2.islong() ? valueOf(var1.tolong() % var2.tolong()) : valueOf(var1.checkdouble() % var2.checkdouble());
      }
   }

   static class frexp extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         double var2 = var1.checkdouble(1);
         if (var2 == 0.0) {
            return varargsOf(ZERO, ZERO);
         } else {
            long var4 = Double.doubleToLongBits(var2);
            double var6 = ((var4 & 4503599627370495L) + 4503599627370496L) * (var4 >= 0L ? 1.110223E-16F : -1.110223E-16F);
            double var8 = ((int)(var4 >> 52) & 2047) - 1022;
            return varargsOf(valueOf(var6), valueOf(var8));
         }
      }
   }

   static final class ldexp extends BinaryOp {
      protected double call(double var1, double var3) {
         return var1 * Double.longBitsToDouble((long)var3 + 1023L << 52);
      }
   }

   static class max extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         LuaValue var2 = var1.checkvalue(1);
         int var3 = 2;

         for (int var4 = var1.narg(); var3 <= var4; var3++) {
            LuaValue var5 = var1.checkvalue(var3);
            if (var2.lt_b(var5)) {
               var2 = var5;
            }
         }

         return var2;
      }
   }

   static class min extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         LuaValue var2 = var1.checkvalue(1);
         int var3 = 2;

         for (int var4 = var1.narg(); var3 <= var4; var3++) {
            LuaValue var5 = var1.checkvalue(var3);
            if (var5.lt_b(var2)) {
               var2 = var5;
            }
         }

         return var2;
      }
   }

   static class modf extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         LuaValue var2 = var1.arg1();
         if (var2.islong()) {
            return varargsOf(var2, valueOf(0.0));
         } else {
            double var3 = var2.checkdouble();
            double var5 = var3 > 0.0 ? Math.floor(var3) : Math.ceil(var3);
            double var7 = var3 == var5 ? 0.0 : var3 - var5;
            return varargsOf(valueOf(var5), valueOf(var7));
         }
      }
   }

   static final class pow extends BinaryOp {
      protected double call(double var1, double var3) {
         return MathLib.dpow_default(var1, var3);
      }
   }

   static final class rad extends UnaryOp {
      protected double call(double var1) {
         return Math.toRadians(var1);
      }
   }

   static class random extends LibFunction {
      Random random = new Random();

      public LuaValue call() {
         return valueOf(this.random.nextDouble());
      }

      public LuaValue call(LuaValue var1) {
         int var2 = var1.checkint();
         if (var2 < 1) {
            argerror(1, "interval is empty");
         }

         return valueOf(1 + this.random.nextInt(var2));
      }

      public LuaValue call(LuaValue var1, LuaValue var2) {
         int var3 = var1.checkint();
         int var4 = var2.checkint();
         if (var4 < var3) {
            argerror(2, "interval is empty");
         }

         return valueOf(var3 + this.random.nextInt(var4 + 1 - var3));
      }
   }

   static class randomseed extends OneArgFunction {
      final random random;

      randomseed(random var1) {
         this.random = var1;
      }

      public LuaValue call(LuaValue var1) {
         long var2 = var1.checklong();
         this.random.random = new Random(var2);
         return NONE;
      }
   }

   static final class sin extends UnaryOp {
      protected double call(double var1) {
         return Math.sin(var1);
      }
   }

   static final class sqrt extends UnaryOp {
      protected double call(double var1) {
         return Math.sqrt(var1);
      }
   }

   static final class tan extends UnaryOp {
      protected double call(double var1) {
         return Math.tan(var1);
      }
   }
}
