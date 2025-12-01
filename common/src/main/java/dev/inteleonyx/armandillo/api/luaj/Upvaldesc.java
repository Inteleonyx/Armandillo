package dev.inteleonyx.armandillo.api.luaj;

public class Upvaldesc {
   public LuaString name;
   public final boolean instack;
   public final short idx;

   public Upvaldesc(LuaString var1, boolean var2, int var3) {
      this.name = var1;
      this.instack = var2;
      this.idx = (short)var3;
   }

   public String toString() {
      return this.idx + (this.instack ? " instack " : " closed ") + this.name;
   }
}
