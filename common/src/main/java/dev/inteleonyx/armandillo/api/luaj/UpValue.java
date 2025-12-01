package dev.inteleonyx.armandillo.api.luaj;

public final class UpValue {
   LuaValue[] array;
   int index;

   public UpValue(LuaValue[] var1, int var2) {
      this.array = var1;
      this.index = var2;
   }

   public String toString() {
      return this.index + "/" + this.array.length + " " + this.array[this.index];
   }

   public String tojstring() {
      return this.array[this.index].tojstring();
   }

   public final LuaValue getValue() {
      return this.array[this.index];
   }

   public final void setValue(LuaValue var1) {
      this.array[this.index] = var1;
   }

   public final void close() {
      LuaValue[] var1 = this.array;
      this.array = new LuaValue[]{var1[this.index]};
      var1[this.index] = null;
      this.index = 0;
   }
}
