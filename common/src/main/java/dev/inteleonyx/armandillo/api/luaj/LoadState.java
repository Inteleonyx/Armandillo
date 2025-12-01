package dev.inteleonyx.armandillo.api.luaj;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LoadState {
   public static final Globals.Undumper instance = new GlobalsUndumper();
   public static final int NUMBER_FORMAT_FLOATS_OR_DOUBLES = 0;
   public static final int NUMBER_FORMAT_INTS_ONLY = 1;
   public static final int NUMBER_FORMAT_NUM_PATCH_INT32 = 4;
   public static final int LUA_TINT = -2;
   public static final int LUA_TNONE = -1;
   public static final int LUA_TNIL = 0;
   public static final int LUA_TBOOLEAN = 1;
   public static final int LUA_TLIGHTUSERDATA = 2;
   public static final int LUA_TNUMBER = 3;
   public static final int LUA_TSTRING = 4;
   public static final int LUA_TTABLE = 5;
   public static final int LUA_TFUNCTION = 6;
   public static final int LUA_TUSERDATA = 7;
   public static final int LUA_TTHREAD = 8;
   public static final int LUA_TVALUE = 9;
   public static String encoding = null;
   public static final byte[] LUA_SIGNATURE = new byte[]{27, 76, 117, 97};
   public static final byte[] LUAC_TAIL = new byte[]{25, -109, 13, 10, 26, 10};
   public static final String SOURCE_BINARY_STRING = "binary string";
   public static final int LUAC_VERSION = 82;
   public static final int LUAC_FORMAT = 0;
   public static final int LUAC_HEADERSIZE = 12;
   private int luacVersion;
   private int luacFormat;
   private boolean luacLittleEndian;
   private int luacSizeofInt;
   private int luacSizeofSizeT;
   private int luacSizeofInstruction;
   private int luacSizeofLuaNumber;
   private int luacNumberFormat;
   public final DataInputStream is;
   String name;
   private static final LuaValue[] NOVALUES = new LuaValue[0];
   private static final Prototype[] NOPROTOS = new Prototype[0];
   private static final LocVars[] NOLOCVARS = new LocVars[0];
   private static final LuaString[] NOSTRVALUES = new LuaString[0];
   private static final Upvaldesc[] NOUPVALDESCS = new Upvaldesc[0];
   private static final int[] NOINTS = new int[0];
   private byte[] buf = new byte[512];

   public static void install(Globals var0) {
      var0.undumper = instance;
   }

   int loadInt() throws IOException {
      this.is.readFully(this.buf, 0, 4);
      return this.luacLittleEndian
         ? this.buf[3] << 24 | (0xFF & this.buf[2]) << 16 | (0xFF & this.buf[1]) << 8 | 0xFF & this.buf[0]
         : this.buf[0] << 24 | (0xFF & this.buf[1]) << 16 | (0xFF & this.buf[2]) << 8 | 0xFF & this.buf[3];
   }

   int[] loadIntArray() throws IOException {
      int var1 = this.loadInt();
      if (var1 == 0) {
         return NOINTS;
      } else {
         int var2 = var1 << 2;
         if (this.buf.length < var2) {
            this.buf = new byte[var2];
         }

         this.is.readFully(this.buf, 0, var2);
         int[] var3 = new int[var1];
         int var4 = 0;

         for (byte var5 = 0; var4 < var1; var5 += 4) {
            var3[var4] = this.luacLittleEndian
               ? this.buf[var5 + 3] << 24 | (255 & this.buf[var5 + 2]) << 16 | (255 & this.buf[var5 + 1]) << 8 | 255 & this.buf[var5 + 0]
               : this.buf[var5 + 0] << 24 | (255 & this.buf[var5 + 1]) << 16 | (255 & this.buf[var5 + 2]) << 8 | 255 & this.buf[var5 + 3];
            var4++;
         }

         return var3;
      }
   }

   long loadInt64() throws IOException {
      int var1;
      int var2;
      if (this.luacLittleEndian) {
         var1 = this.loadInt();
         var2 = this.loadInt();
      } else {
         var2 = this.loadInt();
         var1 = this.loadInt();
      }

      return (long)var2 << 32 | var1 & 4294967295L;
   }

   LuaString loadString() throws IOException {
      int var1 = this.luacSizeofSizeT == 8 ? (int)this.loadInt64() : this.loadInt();
      if (var1 == 0) {
         return null;
      } else {
         byte[] var2 = new byte[var1];
         this.is.readFully(var2, 0, var1);
         return LuaString.valueUsing(var2, 0, var2.length - 1);
      }
   }

   public static LuaValue longBitsToLuaNumber(long var0) {
      if ((var0 & Long.MAX_VALUE) == 0L) {
         return LuaValue.ZERO;
      } else {
         int var2 = (int)(var0 >> 52 & 2047L) - 1023;
         if (var2 >= 0 && var2 < 31) {
            long var3 = var0 & 4503599627370495L;
            int var5 = 52 - var2;
            long var6 = (1L << var5) - 1L;
            if ((var3 & var6) == 0L) {
               int var8 = (int)(var3 >> var5) | 1 << var2;
               return LuaInteger.valueOf(var0 >> 63 != 0L ? -var8 : var8);
            }
         }

         return LuaValue.valueOf(Double.longBitsToDouble(var0));
      }
   }

   LuaValue loadNumber() throws IOException {
      return (LuaValue)(this.luacNumberFormat == 1 ? LuaInteger.valueOf(this.loadInt()) : longBitsToLuaNumber(this.loadInt64()));
   }

   void loadConstants(Prototype var1) throws IOException {
      int var2 = this.loadInt();
      LuaValue[] var3 = var2 > 0 ? new LuaValue[var2] : NOVALUES;

      for (int var4 = 0; var4 < var2; var4++) {
         switch (this.is.readByte()) {
            case -2:
               var3[var4] = LuaInteger.valueOf(this.loadInt());
               break;
            case -1:
            case 2:
            default:
               throw new IllegalStateException("bad constant");
            case 0:
               var3[var4] = LuaValue.NIL;
               break;
            case 1:
               var3[var4] = 0 != this.is.readUnsignedByte() ? LuaValue.TRUE : LuaValue.FALSE;
               break;
            case 3:
               var3[var4] = this.loadNumber();
               break;
            case 4:
               var3[var4] = this.loadString();
         }
      }

      var1.k = var3;
      var2 = this.loadInt();
      Prototype[] var7 = var2 > 0 ? new Prototype[var2] : NOPROTOS;

      for (int var5 = 0; var5 < var2; var5++) {
         var7[var5] = this.loadFunction(var1.source);
      }

      var1.p = var7;
   }

   void loadUpvalues(Prototype var1) throws IOException {
      int var2 = this.loadInt();
      var1.upvalues = var2 > 0 ? new Upvaldesc[var2] : NOUPVALDESCS;

      for (int var3 = 0; var3 < var2; var3++) {
         boolean var4 = this.is.readByte() != 0;
         int var5 = this.is.readByte() & 255;
         var1.upvalues[var3] = new Upvaldesc(null, var4, var5);
      }
   }

   void loadDebug(Prototype var1) throws IOException {
      var1.source = this.loadString();
      var1.lineinfo = this.loadIntArray();
      int var2 = this.loadInt();
      var1.locvars = var2 > 0 ? new LocVars[var2] : NOLOCVARS;

      for (int var3 = 0; var3 < var2; var3++) {
         LuaString var4 = this.loadString();
         int var5 = this.loadInt();
         int var6 = this.loadInt();
         var1.locvars[var3] = new LocVars(var4, var5, var6);
      }

      var2 = this.loadInt();

      for (int var8 = 0; var8 < var2; var8++) {
         var1.upvalues[var8].name = this.loadString();
      }
   }

   public Prototype loadFunction(LuaString var1) throws IOException {
      Prototype var2 = new Prototype();
      var2.linedefined = this.loadInt();
      var2.lastlinedefined = this.loadInt();
      var2.numparams = this.is.readUnsignedByte();
      var2.is_vararg = this.is.readUnsignedByte();
      var2.maxstacksize = this.is.readUnsignedByte();
      var2.code = this.loadIntArray();
      this.loadConstants(var2);
      this.loadUpvalues(var2);
      this.loadDebug(var2);
      return var2;
   }

   public void loadHeader() throws IOException {
      this.luacVersion = this.is.readByte();
      this.luacFormat = this.is.readByte();
      this.luacLittleEndian = 0 != this.is.readByte();
      this.luacSizeofInt = this.is.readByte();
      this.luacSizeofSizeT = this.is.readByte();
      this.luacSizeofInstruction = this.is.readByte();
      this.luacSizeofLuaNumber = this.is.readByte();
      this.luacNumberFormat = this.is.readByte();

      for (int var1 = 0; var1 < LUAC_TAIL.length; var1++) {
         if (this.is.readByte() != LUAC_TAIL[var1]) {
            throw new LuaError("Unexpeted byte in luac tail of header, index=" + var1);
         }
      }
   }

   public static Prototype undump(InputStream var0, String var1) throws IOException {
      if (var0.read() == LUA_SIGNATURE[0] && var0.read() == LUA_SIGNATURE[1] && var0.read() == LUA_SIGNATURE[2] && var0.read() == LUA_SIGNATURE[3]) {
         String var2 = getSourceName(var1);
         LoadState var3 = new LoadState(var0, var2);
         var3.loadHeader();
         switch (var3.luacNumberFormat) {
            case 0:
            case 1:
            case 4:
               return var3.loadFunction(LuaString.valueOf(var2));
            case 2:
            case 3:
            default:
               throw new LuaError("unsupported int size");
         }
      } else {
         return null;
      }
   }

   public static String getSourceName(String var0) {
      String var1 = var0;
      if (var0.startsWith("@") || var0.startsWith("=")) {
         var1 = var0.substring(1);
      } else if (var0.startsWith("\u001b")) {
         var1 = "binary string";
      }

      return var1;
   }

   private LoadState(InputStream var1, String var2) {
      this.name = var2;
      this.is = new DataInputStream(var1);
   }

   private static final class GlobalsUndumper implements Globals.Undumper {
      private GlobalsUndumper() {
      }

      public Prototype undump(InputStream var1, String var2) throws IOException {
         return LoadState.undump(var1, var2);
      }
   }
}
