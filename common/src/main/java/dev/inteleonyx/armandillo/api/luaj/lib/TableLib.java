package dev.inteleonyx.armandillo.api.luaj.lib;

import dev.inteleonyx.armandillo.api.luaj.LuaTable;
import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.Varargs;

public class TableLib extends TwoArgFunction {
   public LuaValue call(LuaValue var1, LuaValue var2) {
      LuaTable var3 = new LuaTable();
      var3.set("concat", new concat());
      var3.set("insert", new insert());
      var3.set("pack", new pack());
      var3.set("remove", new remove());
      var3.set("sort", new sort());
      var3.set("unpack", new unpack());
      var2.set("table", var3);
      if (!var2.get("package").isnil()) {
         var2.get("package").get("loaded").set("table", var3);
      }

      return NIL;
   }

   static class TableLibFunction extends LibFunction {
      public LuaValue call() {
         return argerror(1, "table expected, got no value");
      }
   }

   static class concat extends TableLibFunction {
      public LuaValue call(LuaValue var1) {
         return var1.checktable().concat(EMPTYSTRING, 1, var1.length());
      }

      public LuaValue call(LuaValue var1, LuaValue var2) {
         return var1.checktable().concat(var2.checkstring(), 1, var1.length());
      }

      public LuaValue call(LuaValue var1, LuaValue var2, LuaValue var3) {
         return var1.checktable().concat(var2.checkstring(), var3.checkint(), var1.length());
      }

      public LuaValue call(LuaValue var1, LuaValue var2, LuaValue var3, LuaValue var4) {
         return var1.checktable().concat(var2.checkstring(), var3.checkint(), var4.checkint());
      }
   }

   static class insert extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         switch (var1.narg()) {
            case 0:
            case 1:
               return argerror(2, "value expected");
            case 2:
               LuaTable var2 = var1.arg1().checktable();
               var2.insert(var2.length() + 1, var1.arg(2));
               return NONE;
            default:
               var1.arg1().checktable().insert(var1.checkint(2), var1.arg(3));
               return NONE;
         }
      }
   }

   static class pack extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         LuaTable var2 = tableOf(var1, 1);
         var2.set("n", var1.narg());
         return var2;
      }
   }

   static class remove extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         return var1.arg1().checktable().remove(var1.optint(2, 0));
      }
   }

   static class sort extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         var1.arg1().checktable().sort((LuaValue)(var1.arg(2).isnil() ? NIL : var1.arg(2).checkfunction()));
         return NONE;
      }
   }

   static class unpack extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         LuaTable var2 = var1.checktable(1);
         switch (var1.narg()) {
            case 1:
               return var2.unpack();
            case 2:
               return var2.unpack(var1.checkint(2));
            default:
               return var2.unpack(var1.checkint(2), var1.checkint(3));
         }
      }
   }
}
