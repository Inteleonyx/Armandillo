package dev.inteleonyx.armandillo.api.luaj;

import dev.inteleonyx.armandillo.api.luaj.lib.MathLib;

import java.io.*;

public class LuaString extends LuaValue {
   public static LuaValue s_metatable;
   public final byte[] m_bytes;
   public final int m_offset;
   public final int m_length;
   private final int m_hashcode;
   static final int RECENT_STRINGS_CACHE_SIZE = 128;
   static final int RECENT_STRINGS_MAX_LENGTH = 32;

   public static LuaString valueOf(String var0) {
      char[] var1 = var0.toCharArray();
      byte[] var2 = new byte[lengthAsUtf8(var1)];
      encodeToUtf8(var1, var1.length, var2, 0);
      return valueUsing(var2, 0, var2.length);
   }

   public static LuaString valueOf(byte[] var0, int var1, int var2) {
      if (var2 > 32) {
         return valueFromCopy(var0, var1, var2);
      } else {
         int var3 = hashCode(var0, var1, var2);
         int var4 = var3 & 127;
         LuaString var5 = RecentShortStrings.recent_short_strings[var4];
         if (var5 != null && var5.m_hashcode == var3 && var5.byteseq(var0, var1, var2)) {
            return var5;
         } else {
            LuaString var6 = valueFromCopy(var0, var1, var2);
            RecentShortStrings.recent_short_strings[var4] = var6;
            return var6;
         }
      }
   }

   private static LuaString valueFromCopy(byte[] var0, int var1, int var2) {
      byte[] var3 = new byte[var2];
      System.arraycopy(var0, var1, var3, 0, var2);
      return new LuaString(var3, 0, var2);
   }

   public static LuaString valueUsing(byte[] var0, int var1, int var2) {
      if (var0.length > 32) {
         return new LuaString(var0, var1, var2);
      } else {
         int var3 = hashCode(var0, var1, var2);
         int var4 = var3 & 127;
         LuaString var5 = RecentShortStrings.recent_short_strings[var4];
         if (var5 != null && var5.m_hashcode == var3 && var5.byteseq(var0, var1, var2)) {
            return var5;
         } else {
            LuaString var6 = new LuaString(var0, var1, var2);
            RecentShortStrings.recent_short_strings[var4] = var6;
            return var6;
         }
      }
   }

   public static LuaString valueOf(char[] var0) {
      return valueOf(var0, 0, var0.length);
   }

   public static LuaString valueOf(char[] var0, int var1, int var2) {
      byte[] var3 = new byte[var2];

      for (int var4 = 0; var4 < var2; var4++) {
         var3[var4] = (byte)var0[var4 + var1];
      }

      return valueUsing(var3, 0, var2);
   }

   public static LuaString valueOf(byte[] var0) {
      return valueOf(var0, 0, var0.length);
   }

   public static LuaString valueUsing(byte[] var0) {
      return valueUsing(var0, 0, var0.length);
   }

   private LuaString(byte[] var1, int var2, int var3) {
      this.m_bytes = var1;
      this.m_offset = var2;
      this.m_length = var3;
      this.m_hashcode = hashCode(var1, var2, var3);
   }

   public boolean isstring() {
      return true;
   }

   public LuaValue getmetatable() {
      return s_metatable;
   }

   public int type() {
      return 4;
   }

   public String typename() {
      return "string";
   }

   public String tojstring() {
      return decodeAsUtf8(this.m_bytes, this.m_offset, this.m_length);
   }

   public LuaValue neg() {
      double var1 = this.scannumber();
      return (LuaValue)(Double.isNaN(var1) ? super.neg() : valueOf(-var1));
   }

   public LuaValue add(LuaValue var1) {
      double var2 = this.scannumber();
      return Double.isNaN(var2) ? this.arithmt(ADD, var1) : var1.add(var2);
   }

   public LuaValue add(double var1) {
      return valueOf(this.checkarith() + var1);
   }

   public LuaValue add(int var1) {
      return valueOf(this.checkarith() + var1);
   }

   public LuaValue sub(LuaValue var1) {
      double var2 = this.scannumber();
      return Double.isNaN(var2) ? this.arithmt(SUB, var1) : var1.subFrom(var2);
   }

   public LuaValue sub(double var1) {
      return valueOf(this.checkarith() - var1);
   }

   public LuaValue sub(int var1) {
      return valueOf(this.checkarith() - var1);
   }

   public LuaValue subFrom(double var1) {
      return valueOf(var1 - this.checkarith());
   }

   public LuaValue mul(LuaValue var1) {
      double var2 = this.scannumber();
      return Double.isNaN(var2) ? this.arithmt(MUL, var1) : var1.mul(var2);
   }

   public LuaValue mul(double var1) {
      return valueOf(this.checkarith() * var1);
   }

   public LuaValue mul(int var1) {
      return valueOf(this.checkarith() * var1);
   }

   public LuaValue pow(LuaValue var1) {
      double var2 = this.scannumber();
      return Double.isNaN(var2) ? this.arithmt(POW, var1) : var1.powWith(var2);
   }

   public LuaValue pow(double var1) {
      return MathLib.dpow(this.checkarith(), var1);
   }

   public LuaValue pow(int var1) {
      return MathLib.dpow(this.checkarith(), var1);
   }

   public LuaValue powWith(double var1) {
      return MathLib.dpow(var1, this.checkarith());
   }

   public LuaValue powWith(int var1) {
      return MathLib.dpow(var1, this.checkarith());
   }

   public LuaValue div(LuaValue var1) {
      double var2 = this.scannumber();
      return Double.isNaN(var2) ? this.arithmt(DIV, var1) : var1.divInto(var2);
   }

   public LuaValue div(double var1) {
      return LuaDouble.ddiv(this.checkarith(), var1);
   }

   public LuaValue div(int var1) {
      return LuaDouble.ddiv(this.checkarith(), var1);
   }

   public LuaValue divInto(double var1) {
      return LuaDouble.ddiv(var1, this.checkarith());
   }

   public LuaValue mod(LuaValue var1) {
      double var2 = this.scannumber();
      return Double.isNaN(var2) ? this.arithmt(MOD, var1) : var1.modFrom(var2);
   }

   public LuaValue mod(double var1) {
      return LuaDouble.dmod(this.checkarith(), var1);
   }

   public LuaValue mod(int var1) {
      return LuaDouble.dmod(this.checkarith(), var1);
   }

   public LuaValue modFrom(double var1) {
      return LuaDouble.dmod(var1, this.checkarith());
   }

   public LuaValue lt(LuaValue var1) {
      return var1.strcmp(this) > 0 ? LuaValue.TRUE : FALSE;
   }

   public boolean lt_b(LuaValue var1) {
      return var1.strcmp(this) > 0;
   }

   public boolean lt_b(int var1) {
      this.typerror("attempt to compare string with number");
      return false;
   }

   public boolean lt_b(double var1) {
      this.typerror("attempt to compare string with number");
      return false;
   }

   public LuaValue lteq(LuaValue var1) {
      return var1.strcmp(this) >= 0 ? LuaValue.TRUE : FALSE;
   }

   public boolean lteq_b(LuaValue var1) {
      return var1.strcmp(this) >= 0;
   }

   public boolean lteq_b(int var1) {
      this.typerror("attempt to compare string with number");
      return false;
   }

   public boolean lteq_b(double var1) {
      this.typerror("attempt to compare string with number");
      return false;
   }

   public LuaValue gt(LuaValue var1) {
      return var1.strcmp(this) < 0 ? LuaValue.TRUE : FALSE;
   }

   public boolean gt_b(LuaValue var1) {
      return var1.strcmp(this) < 0;
   }

   public boolean gt_b(int var1) {
      this.typerror("attempt to compare string with number");
      return false;
   }

   public boolean gt_b(double var1) {
      this.typerror("attempt to compare string with number");
      return false;
   }

   public LuaValue gteq(LuaValue var1) {
      return var1.strcmp(this) <= 0 ? LuaValue.TRUE : FALSE;
   }

   public boolean gteq_b(LuaValue var1) {
      return var1.strcmp(this) <= 0;
   }

   public boolean gteq_b(int var1) {
      this.typerror("attempt to compare string with number");
      return false;
   }

   public boolean gteq_b(double var1) {
      this.typerror("attempt to compare string with number");
      return false;
   }

   public LuaValue concat(LuaValue var1) {
      return var1.concatTo(this);
   }

   public Buffer concat(Buffer var1) {
      return var1.concatTo(this);
   }

   public LuaValue concatTo(LuaNumber var1) {
      return this.concatTo(var1.strvalue());
   }

   public LuaValue concatTo(LuaString var1) {
      byte[] var2 = new byte[var1.m_length + this.m_length];
      System.arraycopy(var1.m_bytes, var1.m_offset, var2, 0, var1.m_length);
      System.arraycopy(this.m_bytes, this.m_offset, var2, var1.m_length, this.m_length);
      return valueUsing(var2, 0, var2.length);
   }

   public int strcmp(LuaValue var1) {
      return -var1.strcmp(this);
   }

   public int strcmp(LuaString var1) {
      int var2 = 0;

      for (int var3 = 0; var2 < this.m_length && var3 < var1.m_length; var3++) {
         if (this.m_bytes[this.m_offset + var2] != var1.m_bytes[var1.m_offset + var3]) {
            return this.m_bytes[this.m_offset + var2] - var1.m_bytes[var1.m_offset + var3];
         }

         var2++;
      }

      return this.m_length - var1.m_length;
   }

   private double checkarith() {
      double var1 = this.scannumber();
      if (Double.isNaN(var1)) {
         this.aritherror();
      }

      return var1;
   }

   public int checkint() {
      return (int)((long)this.checkdouble());
   }

   public LuaInteger checkinteger() {
      return valueOf(this.checkint());
   }

   public long checklong() {
      return (long)this.checkdouble();
   }

   public double checkdouble() {
      double var1 = this.scannumber();
      if (Double.isNaN(var1)) {
         this.argerror("number");
      }

      return var1;
   }

   public LuaNumber checknumber() {
      return valueOf(this.checkdouble());
   }

   public LuaNumber checknumber(String var1) {
      double var2 = this.scannumber();
      if (Double.isNaN(var2)) {
         error(var1);
      }

      return valueOf(var2);
   }

   public boolean isnumber() {
      double var1 = this.scannumber();
      return !Double.isNaN(var1);
   }

   public boolean isint() {
      double var1 = this.scannumber();
      if (Double.isNaN(var1)) {
         return false;
      } else {
         int var3 = (int)var1;
         return var3 == var1;
      }
   }

   public boolean islong() {
      double var1 = this.scannumber();
      if (Double.isNaN(var1)) {
         return false;
      } else {
         long var3 = (long)var1;
         return var3 == var1;
      }
   }

   public byte tobyte() {
      return (byte)this.toint();
   }

   public char tochar() {
      return (char)this.toint();
   }

   public double todouble() {
      double var1 = this.scannumber();
      return Double.isNaN(var1) ? 0.0 : var1;
   }

   public float tofloat() {
      return (float)this.todouble();
   }

   public int toint() {
      return (int)this.tolong();
   }

   public long tolong() {
      return (long)this.todouble();
   }

   public short toshort() {
      return (short)this.toint();
   }

   public double optdouble(double var1) {
      return this.checkdouble();
   }

   public int optint(int var1) {
      return this.checkint();
   }

   public LuaInteger optinteger(LuaInteger var1) {
      return this.checkinteger();
   }

   public long optlong(long var1) {
      return this.checklong();
   }

   public LuaNumber optnumber(LuaNumber var1) {
      return this.checknumber();
   }

   public LuaString optstring(LuaString var1) {
      return this;
   }

   public LuaValue tostring() {
      return this;
   }

   public String optjstring(String var1) {
      return this.tojstring();
   }

   public LuaString strvalue() {
      return this;
   }

   public LuaString substring(int var1, int var2) {
      int var3 = this.m_offset + var1;
      int var4 = var2 - var1;
      return var4 >= this.m_length / 2 ? valueUsing(this.m_bytes, var3, var4) : valueOf(this.m_bytes, var3, var4);
   }

   public int hashCode() {
      return this.m_hashcode;
   }

   public static int hashCode(byte[] var0, int var1, int var2) {
      int var3 = var2;
      int var4 = (var2 >> 5) + 1;

      for (int var5 = var2; var5 >= var4; var5 -= var4) {
         var3 ^= (var3 << 5) + (var3 >> 2) + (var0[var1 + var5 - 1] & 255);
      }

      return var3;
   }

   public boolean equals(Object var1) {
      return var1 instanceof LuaString ? this.raweq((LuaString)var1) : false;
   }

   public LuaValue eq(LuaValue var1) {
      return var1.raweq(this) ? TRUE : FALSE;
   }

   public boolean eq_b(LuaValue var1) {
      return var1.raweq(this);
   }

   public boolean raweq(LuaValue var1) {
      return var1.raweq(this);
   }

   public boolean raweq(LuaString var1) {
      if (this == var1) {
         return true;
      } else if (var1.m_length != this.m_length) {
         return false;
      } else if (var1.m_bytes == this.m_bytes && var1.m_offset == this.m_offset) {
         return true;
      } else if (var1.hashCode() != this.hashCode()) {
         return false;
      } else {
         for (int var2 = 0; var2 < this.m_length; var2++) {
            if (var1.m_bytes[var1.m_offset + var2] != this.m_bytes[this.m_offset + var2]) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean equals(LuaString var0, int var1, LuaString var2, int var3, int var4) {
      return equals(var0.m_bytes, var0.m_offset + var1, var2.m_bytes, var2.m_offset + var3, var4);
   }

   private boolean byteseq(byte[] var1, int var2, int var3) {
      return this.m_length == var3 && equals(this.m_bytes, this.m_offset, var1, var2, var3);
   }

   public static boolean equals(byte[] var0, int var1, byte[] var2, int var3, int var4) {
      if (var0.length >= var1 + var4 && var2.length >= var3 + var4) {
         while (--var4 >= 0) {
            if (var0[var1++] != var2[var3++]) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public void write(DataOutputStream var1, int var2, int var3) throws IOException {
      var1.write(this.m_bytes, this.m_offset + var2, var3);
   }

   public LuaValue len() {
      return LuaInteger.valueOf(this.m_length);
   }

   public int length() {
      return this.m_length;
   }

   public int rawlen() {
      return this.m_length;
   }

   public int luaByte(int var1) {
      return this.m_bytes[this.m_offset + var1] & 0xFF;
   }

   public int charAt(int var1) {
      if (var1 >= 0 && var1 < this.m_length) {
         return this.luaByte(var1);
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public String checkjstring() {
      return this.tojstring();
   }

   public LuaString checkstring() {
      return this;
   }

   public InputStream toInputStream() {
      return new ByteArrayInputStream(this.m_bytes, this.m_offset, this.m_length);
   }

   public void copyInto(int var1, byte[] var2, int var3, int var4) {
      System.arraycopy(this.m_bytes, this.m_offset + var1, var2, var3, var4);
   }

   public int indexOfAny(LuaString var1) {
      int var2 = this.m_offset + this.m_length;
      int var3 = var1.m_offset + var1.m_length;

      for (int var4 = this.m_offset; var4 < var2; var4++) {
         for (int var5 = var1.m_offset; var5 < var3; var5++) {
            if (this.m_bytes[var4] == var1.m_bytes[var5]) {
               return var4 - this.m_offset;
            }
         }
      }

      return -1;
   }

   public int indexOf(byte var1, int var2) {
      for (int var3 = var2; var3 < this.m_length; var3++) {
         if (this.m_bytes[this.m_offset + var3] == var1) {
            return var3;
         }
      }

      return -1;
   }

   public int indexOf(LuaString var1, int var2) {
      int var3 = var1.length();
      int var4 = this.m_length - var3;

      for (int var5 = var2; var5 <= var4; var5++) {
         if (equals(this.m_bytes, this.m_offset + var5, var1.m_bytes, var1.m_offset, var3)) {
            return var5;
         }
      }

      return -1;
   }

   public int lastIndexOf(LuaString var1) {
      int var2 = var1.length();
      int var3 = this.m_length - var2;

      for (int var4 = var3; var4 >= 0; var4--) {
         if (equals(this.m_bytes, this.m_offset + var4, var1.m_bytes, var1.m_offset, var2)) {
            return var4;
         }
      }

      return -1;
   }

   public static String decodeAsUtf8(byte[] var0, int var1, int var2) {
      int var3 = var1;
      int var4 = var1 + var2;

      int var5;
      for (var5 = 0; var3 < var4; var5++) {
         switch (224 & var0[var3++]) {
            case 224:
               var3++;
            case 192:
               var3++;
               break;
         }
      }

      char[] var7 = new char[var5];
      var3 = var1;
      var4 = var1 + var2;
      var5 = 0;

      while (var3 < var4) {
         byte var6;
         var7[var5++] = (char)(
            (var6 = var0[var3++]) < 0 && var3 < var4
               ? (var6 >= -32 && var3 + 1 < var4 ? (var6 & 15) << 12 | (var0[var3++] & 63) << 6 | var0[var3++] & 63 : (var6 & 63) << 6 | var0[var3++] & 63)
               : var6
         );
      }

      return new String(var7);
   }

   public static int lengthAsUtf8(char[] var0) {
      int var2;
      int var1 = var2 = var0.length;

      while (--var1 >= 0) {
         char var3;
         if ((var3 = var0[var1]) >= 128) {
            var2 += var3 >= 2048 ? 2 : 1;
         }
      }

      return var2;
   }

   public static int encodeToUtf8(char[] var0, int var1, byte[] var2, int var3) {
      int var5 = var3;

      for (int var6 = 0; var6 < var1; var6++) {
         char var4;
         if ((var4 = var0[var6]) < 128) {
            var2[var5++] = (byte)var4;
         } else if (var4 < 2048) {
            var2[var5++] = (byte)(192 | var4 >> 6 & 31);
            var2[var5++] = (byte)(128 | var4 & '?');
         } else {
            var2[var5++] = (byte)(224 | var4 >> '\f' & 15);
            var2[var5++] = (byte)(128 | var4 >> 6 & 63);
            var2[var5++] = (byte)(128 | var4 & '?');
         }
      }

      return var5 - var3;
   }

   public boolean isValidUtf8() {
      int var1 = this.m_offset;
      int var2 = this.m_offset + this.m_length;

      while (var1 < var2) {
         byte var3 = this.m_bytes[var1++];
         if (var3 < 0
            && ((var3 & 224) != 192 || var1 >= var2 || (this.m_bytes[var1++] & 192) != 128)
            && ((var3 & 240) != 224 || var1 + 1 >= var2 || (this.m_bytes[var1++] & 192) != 128 || (this.m_bytes[var1++] & 192) != 128)) {
            return false;
         }
      }

      return true;
   }

   public LuaValue tonumber() {
      double var1 = this.scannumber();
      return (LuaValue)(Double.isNaN(var1) ? NIL : valueOf(var1));
   }

   public LuaValue tonumber(int var1) {
      double var2 = this.scannumber(var1);
      return (LuaValue)(Double.isNaN(var2) ? NIL : valueOf(var2));
   }

   public double scannumber() {
      int var1 = this.m_offset;
      int var2 = this.m_offset + this.m_length;

      while (var1 < var2 && this.m_bytes[var1] == 32) {
         var1++;
      }

      while (var1 < var2 && this.m_bytes[var2 - 1] == 32) {
         var2--;
      }

      if (var1 >= var2) {
         return Double.NaN;
      } else if (this.m_bytes[var1] != 48 || var1 + 1 >= var2 || this.m_bytes[var1 + 1] != 120 && this.m_bytes[var1 + 1] != 88) {
         double var3 = this.scanlong(10, var1, var2);
         return Double.isNaN(var3) ? this.scandouble(var1, var2) : var3;
      } else {
         return this.scanlong(16, var1 + 2, var2);
      }
   }

   public double scannumber(int var1) {
      if (var1 >= 2 && var1 <= 36) {
         int var2 = this.m_offset;
         int var3 = this.m_offset + this.m_length;

         while (var2 < var3 && this.m_bytes[var2] == 32) {
            var2++;
         }

         while (var2 < var3 && this.m_bytes[var3 - 1] == 32) {
            var3--;
         }

         return var2 >= var3 ? Double.NaN : this.scanlong(var1, var2, var3);
      } else {
         return Double.NaN;
      }
   }

   private double scanlong(int var1, int var2, int var3) {
      long var4 = 0L;
      boolean var6 = this.m_bytes[var2] == 45;

      for (int var7 = var6 ? var2 + 1 : var2; var7 < var3; var7++) {
         int var8 = this.m_bytes[var7]
            - (var1 > 10 && (this.m_bytes[var7] < 48 || this.m_bytes[var7] > 57) ? (this.m_bytes[var7] >= 65 && this.m_bytes[var7] <= 90 ? 55 : 87) : 48);
         if (var8 < 0 || var8 >= var1) {
            return Double.NaN;
         }

         var4 = var4 * var1 + var8;
         if (var4 < 0L) {
            return Double.NaN;
         }
      }

      return var6 ? -var4 : var4;
   }

   private double scandouble(int var1, int var2) {
      if (var2 > var1 + 64) {
         var2 = var1 + 64;
      }

      for (int var3 = var1; var3 < var2; var3++) {
         switch (this.m_bytes[var3]) {
            case 43:
            case 45:
            case 46:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 69:
            case 101:
            case 44:
            case 47:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 100:
            default:
               return Double.NaN;
         }
      }

      char[] var6 = new char[var2 - var1];

      for (int var4 = var1; var4 < var2; var4++) {
         var6[var4 - var1] = (char)this.m_bytes[var4];
      }

      try {
         return Double.parseDouble(new String(var6));
      } catch (Exception var5) {
         return Double.NaN;
      }
   }

   public void printToStream(PrintStream var1) {
      int var2 = 0;

      for (int var3 = this.m_length; var2 < var3; var2++) {
         byte var4 = this.m_bytes[this.m_offset + var2];
         var1.print((char)var4);
      }
   }

   private static final class RecentShortStrings {
      private static final LuaString[] recent_short_strings = new LuaString[128];
   }
}
