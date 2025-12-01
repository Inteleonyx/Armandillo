package dev.inteleonyx.armandillo.api.luaj;

public class LocVars {
   public LuaString varname;
   public int startpc;
   public int endpc;

   public LocVars(LuaString var1, int var2, int var3) {
      this.varname = var1;
      this.startpc = var2;
      this.endpc = var3;
   }

   public String tojstring() {
      return this.varname + " " + this.startpc + "-" + this.endpc;
   }
}
