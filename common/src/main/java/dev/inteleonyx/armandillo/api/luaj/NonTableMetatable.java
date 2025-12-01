package dev.inteleonyx.armandillo.api.luaj;

class NonTableMetatable implements Metatable {
   private final LuaValue value;

   public NonTableMetatable(LuaValue var1) {
      this.value = var1;
   }

   public boolean useWeakKeys() {
      return false;
   }

   public boolean useWeakValues() {
      return false;
   }

   public LuaValue toLuaValue() {
      return this.value;
   }

   public LuaTable.Slot entry(LuaValue var1, LuaValue var2) {
      return LuaTable.defaultEntry(var1, var2);
   }

   public LuaValue wrap(LuaValue var1) {
      return var1;
   }

   public LuaValue arrayget(LuaValue[] var1, int var2) {
      return var1[var2];
   }
}
