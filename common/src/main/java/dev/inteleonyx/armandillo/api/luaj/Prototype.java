package dev.inteleonyx.armandillo.api.luaj;

public class Prototype {
   public LuaValue[] k;
   public int[] code;
   public Prototype[] p;
   public int[] lineinfo;
   public LocVars[] locvars;
   public Upvaldesc[] upvalues;
   public LuaString source;
   public int linedefined;
   public int lastlinedefined;
   public int numparams;
   public int is_vararg;
   public int maxstacksize;
   private static final Upvaldesc[] NOUPVALUES = new Upvaldesc[0];
   private static final Prototype[] NOSUBPROTOS = new Prototype[0];

   public Prototype() {
      this.p = NOSUBPROTOS;
      this.upvalues = NOUPVALUES;
   }

   public Prototype(int var1) {
      this.p = NOSUBPROTOS;
      this.upvalues = new Upvaldesc[var1];
   }

   public String toString() {
      return this.source + ":" + this.linedefined + "-" + this.lastlinedefined;
   }

   public LuaString getlocalname(int var1, int var2) {
      for (int var3 = 0; var3 < this.locvars.length && this.locvars[var3].startpc <= var2; var3++) {
         if (var2 < this.locvars[var3].endpc) {
            if (--var1 == 0) {
               return this.locvars[var3].varname;
            }
         }
      }

      return null;
   }

   public String shortsource() {
      String var1 = this.source.tojstring();
      if (var1.startsWith("@") || var1.startsWith("=")) {
         var1 = var1.substring(1);
      } else if (var1.startsWith("\u001b")) {
         var1 = "binary string";
      }

      return var1;
   }
}
