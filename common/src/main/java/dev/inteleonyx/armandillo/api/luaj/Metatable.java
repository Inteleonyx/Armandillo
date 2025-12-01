package dev.inteleonyx.armandillo.api.luaj;

interface Metatable {
   boolean useWeakKeys();

   boolean useWeakValues();

   LuaValue toLuaValue();

   LuaTable.Slot entry(LuaValue var1, LuaValue var2);

   LuaValue wrap(LuaValue var1);

   LuaValue arrayget(LuaValue[] var1, int var2);
}
