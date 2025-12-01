package dev.inteleonyx.armandillo.api.luaj;

import dev.inteleonyx.armandillo.api.luaj.lib.MathLib;

public class LuaInteger extends LuaNumber {
   private static final LuaInteger[] intValues = new LuaInteger[512];
   public final int v;

   public static LuaInteger valueOf(int var0) {
      return var0 <= 255 && var0 >= -256 ? intValues[var0 + 256] : new LuaInteger(var0);
   }

   public static LuaNumber valueOf(long var0) {
      int var2 = (int)var0;
      return (LuaNumber)(var0 == var2 ? (var2 <= 255 && var2 >= -256 ? intValues[var2 + 256] : new LuaInteger(var2)) : LuaDouble.valueOf(var0));
   }

   LuaInteger(int var1) {
      this.v = var1;
   }

   public boolean isint() {
      return true;
   }

   public boolean isinttype() {
      return true;
   }

   public boolean islong() {
      return true;
   }

   public byte tobyte() {
      return (byte)this.v;
   }

   public char tochar() {
      return (char)this.v;
   }

   public double todouble() {
      return this.v;
   }

   public float tofloat() {
      return this.v;
   }

   public int toint() {
      return this.v;
   }

   public long tolong() {
      return this.v;
   }

   public short toshort() {
      return (short)this.v;
   }

   public double optdouble(double var1) {
      return this.v;
   }

   public int optint(int var1) {
      return this.v;
   }

   public LuaInteger optinteger(LuaInteger var1) {
      return this;
   }

   public long optlong(long var1) {
      return this.v;
   }

   public String tojstring() {
      return Integer.toString(this.v);
   }

   public LuaString strvalue() {
      return LuaString.valueOf(Integer.toString(this.v));
   }

   public LuaString optstring(LuaString var1) {
      return LuaString.valueOf(Integer.toString(this.v));
   }

   public LuaValue tostring() {
      return LuaString.valueOf(Integer.toString(this.v));
   }

   public String optjstring(String var1) {
      return Integer.toString(this.v);
   }

   public LuaInteger checkinteger() {
      return this;
   }

   public boolean isstring() {
      return true;
   }

   public int hashCode() {
      return this.v;
   }

   public static int hashCode(int var0) {
      return var0;
   }

   public LuaValue neg() {
      return valueOf(-this.v);
   }

   public boolean equals(Object var1) {
      return var1 instanceof LuaInteger ? ((LuaInteger)var1).v == this.v : false;
   }

   public LuaValue eq(LuaValue var1) {
      return var1.raweq(this.v) ? TRUE : FALSE;
   }

   public boolean eq_b(LuaValue var1) {
      return var1.raweq(this.v);
   }

   public boolean raweq(LuaValue var1) {
      return var1.raweq(this.v);
   }

   public boolean raweq(double var1) {
      return this.v == var1;
   }

   public boolean raweq(int var1) {
      return this.v == var1;
   }

   public LuaValue add(LuaValue var1) {
      return var1.add(this.v);
   }

   public LuaValue add(double var1) {
      return LuaDouble.valueOf(var1 + this.v);
   }

   public LuaValue add(int var1) {
      return valueOf((long)var1 + this.v);
   }

   public LuaValue sub(LuaValue var1) {
      return var1.subFrom(this.v);
   }

   public LuaValue sub(double var1) {
      return LuaDouble.valueOf(this.v - var1);
   }

   public LuaValue sub(int var1) {
      return LuaDouble.valueOf(this.v - var1);
   }

   public LuaValue subFrom(double var1) {
      return LuaDouble.valueOf(var1 - this.v);
   }

   public LuaValue subFrom(int var1) {
      return valueOf((long)var1 - this.v);
   }

   public LuaValue mul(LuaValue var1) {
      return var1.mul(this.v);
   }

   public LuaValue mul(double var1) {
      return LuaDouble.valueOf(var1 * this.v);
   }

   public LuaValue mul(int var1) {
      return valueOf((long)var1 * this.v);
   }

   public LuaValue pow(LuaValue var1) {
      return var1.powWith(this.v);
   }

   public LuaValue pow(double var1) {
      return MathLib.dpow(this.v, var1);
   }

   public LuaValue pow(int var1) {
      return MathLib.dpow(this.v, var1);
   }

   public LuaValue powWith(double var1) {
      return MathLib.dpow(var1, this.v);
   }

   public LuaValue powWith(int var1) {
      return MathLib.dpow(var1, this.v);
   }

   public LuaValue div(LuaValue var1) {
      return var1.divInto(this.v);
   }

   public LuaValue div(double var1) {
      return LuaDouble.ddiv(this.v, var1);
   }

   public LuaValue div(int var1) {
      return LuaDouble.ddiv(this.v, var1);
   }

   public LuaValue divInto(double var1) {
      return LuaDouble.ddiv(var1, this.v);
   }

   public LuaValue mod(LuaValue var1) {
      return var1.modFrom(this.v);
   }

   public LuaValue mod(double var1) {
      return LuaDouble.dmod(this.v, var1);
   }

   public LuaValue mod(int var1) {
      return LuaDouble.dmod(this.v, var1);
   }

   public LuaValue modFrom(double var1) {
      return LuaDouble.dmod(var1, this.v);
   }

   public LuaValue lt(LuaValue var1) {
      return var1.gt_b(this.v) ? TRUE : FALSE;
   }

   public LuaValue lt(double var1) {
      return this.v < var1 ? TRUE : FALSE;
   }

   public LuaValue lt(int var1) {
      return this.v < var1 ? TRUE : FALSE;
   }

   public boolean lt_b(LuaValue var1) {
      return var1.gt_b(this.v);
   }

   public boolean lt_b(int var1) {
      return this.v < var1;
   }

   public boolean lt_b(double var1) {
      return this.v < var1;
   }

   public LuaValue lteq(LuaValue var1) {
      return var1.gteq_b(this.v) ? TRUE : FALSE;
   }

   public LuaValue lteq(double var1) {
      return this.v <= var1 ? TRUE : FALSE;
   }

   public LuaValue lteq(int var1) {
      return this.v <= var1 ? TRUE : FALSE;
   }

   public boolean lteq_b(LuaValue var1) {
      return var1.gteq_b(this.v);
   }

   public boolean lteq_b(int var1) {
      return this.v <= var1;
   }

   public boolean lteq_b(double var1) {
      return this.v <= var1;
   }

   public LuaValue gt(LuaValue var1) {
      return var1.lt_b(this.v) ? TRUE : FALSE;
   }

   public LuaValue gt(double var1) {
      return this.v > var1 ? TRUE : FALSE;
   }

   public LuaValue gt(int var1) {
      return this.v > var1 ? TRUE : FALSE;
   }

   public boolean gt_b(LuaValue var1) {
      return var1.lt_b(this.v);
   }

   public boolean gt_b(int var1) {
      return this.v > var1;
   }

   public boolean gt_b(double var1) {
      return this.v > var1;
   }

   public LuaValue gteq(LuaValue var1) {
      return var1.lteq_b(this.v) ? TRUE : FALSE;
   }

   public LuaValue gteq(double var1) {
      return this.v >= var1 ? TRUE : FALSE;
   }

   public LuaValue gteq(int var1) {
      return this.v >= var1 ? TRUE : FALSE;
   }

   public boolean gteq_b(LuaValue var1) {
      return var1.lteq_b(this.v);
   }

   public boolean gteq_b(int var1) {
      return this.v >= var1;
   }

   public boolean gteq_b(double var1) {
      return this.v >= var1;
   }

   public int strcmp(LuaString var1) {
      this.typerror("attempt to compare number with string");
      return 0;
   }

   public int checkint() {
      return this.v;
   }

   public long checklong() {
      return this.v;
   }

   public double checkdouble() {
      return this.v;
   }

   public String checkjstring() {
      return String.valueOf(this.v);
   }

   public LuaString checkstring() {
      return valueOf(String.valueOf(this.v));
   }

   static {
      for (int var0 = 0; var0 < 512; var0++) {
         intValues[var0] = new LuaInteger(var0 - 256);
      }
   }
}
