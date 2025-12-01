package dev.inteleonyx.armandillo.api.luaj.lib;

import dev.inteleonyx.armandillo.api.luaj.LuaTable;
import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.Varargs;

public class Bit32Lib extends TwoArgFunction {
   public LuaValue call(LuaValue var1, LuaValue var2) {
      LuaTable var3 = new LuaTable();
      this.bind(
              var3,
              Bit32Lib.Bit32LibV.class,
              new String[]{"band", "bnot", "bor", "btest", "bxor", "extract", "replace"}
      );

      this.bind(
              var3,
              Bit32Lib.Bit32Lib2.class,
              new String[]{"arshift", "lrotate", "lshift", "rrotate", "rshift"}
      );
      var2.set("bit32", var3);
      if (!var2.get("package").isnil()) {
         var2.get("package").get("loaded").set("bit32", var3);
      }

      return var3;
   }

   static LuaValue arshift(int var0, int var1) {
      return var1 >= 0 ? bitsToValue(var0 >> var1) : bitsToValue(var0 << -var1);
   }

   static LuaValue rshift(int var0, int var1) {
      if (var1 >= 32 || var1 <= -32) {
         return ZERO;
      } else {
         return var1 >= 0 ? bitsToValue(var0 >>> var1) : bitsToValue(var0 << -var1);
      }
   }

   static LuaValue lshift(int var0, int var1) {
      if (var1 >= 32 || var1 <= -32) {
         return ZERO;
      } else {
         return var1 >= 0 ? bitsToValue(var0 << var1) : bitsToValue(var0 >>> -var1);
      }
   }

   static Varargs band(Varargs var0) {
      int var1 = -1;

      for (int var2 = 1; var2 <= var0.narg(); var2++) {
         var1 &= var0.checkint(var2);
      }

      return bitsToValue(var1);
   }

   static Varargs bnot(Varargs var0) {
      return bitsToValue(~var0.checkint(1));
   }

   static Varargs bor(Varargs var0) {
      int var1 = 0;

      for (int var2 = 1; var2 <= var0.narg(); var2++) {
         var1 |= var0.checkint(var2);
      }

      return bitsToValue(var1);
   }

   static Varargs btest(Varargs var0) {
      int var1 = -1;

      for (int var2 = 1; var2 <= var0.narg(); var2++) {
         var1 &= var0.checkint(var2);
      }

      return valueOf(var1 != 0);
   }

   static Varargs bxor(Varargs var0) {
      int var1 = 0;

      for (int var2 = 1; var2 <= var0.narg(); var2++) {
         var1 ^= var0.checkint(var2);
      }

      return bitsToValue(var1);
   }

   static LuaValue lrotate(int var0, int var1) {
      if (var1 < 0) {
         return rrotate(var0, -var1);
      } else {
         var1 &= 31;
         return bitsToValue(var0 << var1 | var0 >>> 32 - var1);
      }
   }

   static LuaValue rrotate(int var0, int var1) {
      if (var1 < 0) {
         return lrotate(var0, -var1);
      } else {
         var1 &= 31;
         return bitsToValue(var0 >>> var1 | var0 << 32 - var1);
      }
   }

   static LuaValue extract(int var0, int var1, int var2) {
      if (var1 < 0) {
         argerror(2, "field cannot be negative");
      }

      if (var2 < 0) {
         argerror(3, "width must be postive");
      }

      if (var1 + var2 > 32) {
         error("trying to access non-existent bits");
      }

      return bitsToValue(var0 >>> var1 & -1 >>> 32 - var2);
   }

   static LuaValue replace(int var0, int var1, int var2, int var3) {
      if (var2 < 0) {
         argerror(3, "field cannot be negative");
      }

      if (var3 < 0) {
         argerror(4, "width must be postive");
      }

      if (var2 + var3 > 32) {
         error("trying to access non-existent bits");
      }

      int var4 = -1 >>> 32 - var3 << var2;
      var0 = var0 & ~var4 | var1 << var2 & var4;
      return bitsToValue(var0);
   }

   private static LuaValue bitsToValue(int var0) {
      return (LuaValue)(var0 < 0 ? valueOf(var0 & 4294967295L) : valueOf(var0));
   }

   static final class Bit32Lib2 extends TwoArgFunction {
      public LuaValue call(LuaValue var1, LuaValue var2) {
         switch (this.opcode) {
            case 0:
               return Bit32Lib.arshift(var1.checkint(), var2.checkint());
            case 1:
               return Bit32Lib.lrotate(var1.checkint(), var2.checkint());
            case 2:
               return Bit32Lib.lshift(var1.checkint(), var2.checkint());
            case 3:
               return Bit32Lib.rrotate(var1.checkint(), var2.checkint());
            case 4:
               return Bit32Lib.rshift(var1.checkint(), var2.checkint());
            default:
               return NIL;
         }
      }
   }

   static final class Bit32LibV extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         switch (this.opcode) {
            case 0:
               return Bit32Lib.band(var1);
            case 1:
               return Bit32Lib.bnot(var1);
            case 2:
               return Bit32Lib.bor(var1);
            case 3:
               return Bit32Lib.btest(var1);
            case 4:
               return Bit32Lib.bxor(var1);
            case 5:
               return Bit32Lib.extract(var1.checkint(1), var1.checkint(2), var1.optint(3, 1));
            case 6:
               return Bit32Lib.replace(var1.checkint(1), var1.checkint(2), var1.checkint(3), var1.optint(4, 1));
            default:
               return NIL;
         }
      }
   }
}
