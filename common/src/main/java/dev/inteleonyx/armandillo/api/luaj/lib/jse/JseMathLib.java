package dev.inteleonyx.armandillo.api.luaj.lib.jse;

import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.lib.MathLib;
import dev.inteleonyx.armandillo.api.luaj.lib.TwoArgFunction;

public class JseMathLib extends MathLib {
   public LuaValue call(LuaValue var1, LuaValue var2) {
      super.call(var1, var2);
      LuaValue var3 = var2.get("math");
      var3.set("acos", new acos());
      var3.set("asin", new asin());
      atan2 var4 = new atan2();
      var3.set("atan", var4);
      var3.set("atan2", var4);
      var3.set("cosh", new cosh());
      var3.set("exp", new exp());
      var3.set("log", new log());
      var3.set("pow", new pow());
      var3.set("sinh", new sinh());
      var3.set("tanh", new tanh());
      return var3;
   }

   public double dpow_lib(double var1, double var3) {
      return Math.pow(var1, var3);
   }

   static final class acos extends UnaryOp {
      protected double call(double var1) {
         return Math.acos(var1);
      }
   }

   static final class asin extends UnaryOp {
      protected double call(double var1) {
         return Math.asin(var1);
      }
   }

   static final class atan2 extends TwoArgFunction {
      public LuaValue call(LuaValue var1, LuaValue var2) {
         return valueOf(Math.atan2(var1.checkdouble(), var2.optdouble(1.0)));
      }
   }

   static final class cosh extends UnaryOp {
      protected double call(double var1) {
         return Math.cosh(var1);
      }
   }

   static final class exp extends UnaryOp {
      protected double call(double var1) {
         return Math.exp(var1);
      }
   }

   static final class log extends TwoArgFunction {
      public LuaValue call(LuaValue var1, LuaValue var2) {
         double var3 = Math.log(var1.checkdouble());
         double var5 = var2.optdouble(Math.E);
         if (var5 != Math.E) {
            var3 /= Math.log(var5);
         }

         return valueOf(var3);
      }
   }

   static final class pow extends BinaryOp {
      protected double call(double var1, double var3) {
         return Math.pow(var1, var3);
      }
   }

   static final class sinh extends UnaryOp {
      protected double call(double var1) {
         return Math.sinh(var1);
      }
   }

   static final class tanh extends UnaryOp {
      protected double call(double var1) {
         return Math.tanh(var1);
      }
   }
}
