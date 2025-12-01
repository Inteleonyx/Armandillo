package dev.inteleonyx.armandillo.api.luaj;

public abstract class LuaNumber extends LuaValue {
   public static LuaValue s_metatable;

   public int type() {
      return 3;
   }

   public String typename() {
      return "number";
   }

   public LuaNumber checknumber() {
      return this;
   }

   public LuaNumber checknumber(String var1) {
      return this;
   }

   public LuaNumber optnumber(LuaNumber var1) {
      return this;
   }

   public LuaValue tonumber() {
      return this;
   }

   public boolean isnumber() {
      return true;
   }

   public boolean isstring() {
      return true;
   }

   public LuaValue getmetatable() {
      return s_metatable;
   }

   public LuaValue concat(LuaValue var1) {
      return var1.concatTo(this);
   }

   public Buffer concat(Buffer var1) {
      return var1.concatTo(this);
   }

   public LuaValue concatTo(LuaNumber var1) {
      return this.strvalue().concatTo(var1.strvalue());
   }

   public LuaValue concatTo(LuaString var1) {
      return this.strvalue().concatTo(var1);
   }
}
