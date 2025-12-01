package dev.inteleonyx.armandillo.api.luaj;

public abstract class LuaFunction extends LuaValue {
   public static LuaValue s_metatable;

   public int type() {
      return 6;
   }

   public String typename() {
      return "function";
   }

   public boolean isfunction() {
      return true;
   }

   public LuaFunction checkfunction() {
      return this;
   }

   public LuaFunction optfunction(LuaFunction var1) {
      return this;
   }

   public LuaValue getmetatable() {
      return s_metatable;
   }

   public String tojstring() {
      return "function: " + this.classnamestub();
   }

   public LuaString strvalue() {
      return valueOf(this.tojstring());
   }

   public String classnamestub() {
      String var1 = this.getClass().getName();
      return var1.substring(Math.max(var1.lastIndexOf(46), var1.lastIndexOf(36)) + 1);
   }

   public String name() {
      return this.classnamestub();
   }
}
