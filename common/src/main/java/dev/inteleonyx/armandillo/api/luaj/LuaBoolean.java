package dev.inteleonyx.armandillo.api.luaj;

public final class LuaBoolean extends LuaValue {
   static final LuaBoolean _TRUE = new LuaBoolean(true);
   static final LuaBoolean _FALSE = new LuaBoolean(false);
   public static LuaValue s_metatable;
   public final boolean v;

   LuaBoolean(boolean var1) {
      this.v = var1;
   }

   public int type() {
      return 1;
   }

   public String typename() {
      return "boolean";
   }

   public boolean isboolean() {
      return true;
   }

   public LuaValue not() {
      return this.v ? FALSE : LuaValue.TRUE;
   }

   public boolean booleanValue() {
      return this.v;
   }

   public boolean toboolean() {
      return this.v;
   }

   public String tojstring() {
      return this.v ? "true" : "false";
   }

   public boolean optboolean(boolean var1) {
      return this.v;
   }

   public boolean checkboolean() {
      return this.v;
   }

   public LuaValue getmetatable() {
      return s_metatable;
   }
}
