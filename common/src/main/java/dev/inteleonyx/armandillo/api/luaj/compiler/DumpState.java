package dev.inteleonyx.armandillo.api.luaj.compiler;

import dev.inteleonyx.armandillo.api.luaj.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DumpState {
   public static boolean ALLOW_INTEGER_CASTING = false;
   public static final int NUMBER_FORMAT_FLOATS_OR_DOUBLES = 0;
   public static final int NUMBER_FORMAT_INTS_ONLY = 1;
   public static final int NUMBER_FORMAT_NUM_PATCH_INT32 = 4;
   public static final int NUMBER_FORMAT_DEFAULT = 0;
   private boolean IS_LITTLE_ENDIAN = false;
   private int NUMBER_FORMAT = 0;
   private int SIZEOF_LUA_NUMBER = 8;
   private static final int SIZEOF_INT = 4;
   private static final int SIZEOF_SIZET = 4;
   private static final int SIZEOF_INSTRUCTION = 4;
   DataOutputStream writer;
   boolean strip;
   int status;

   public DumpState(OutputStream var1, boolean var2) {
      this.writer = new DataOutputStream(var1);
      this.strip = var2;
      this.status = 0;
   }

   void dumpBlock(byte[] var1, int var2) throws IOException {
      this.writer.write(var1, 0, var2);
   }

   void dumpChar(int var1) throws IOException {
      this.writer.write(var1);
   }

   void dumpInt(int var1) throws IOException {
      if (this.IS_LITTLE_ENDIAN) {
         this.writer.writeByte(var1 & 0xFF);
         this.writer.writeByte(var1 >> 8 & 0xFF);
         this.writer.writeByte(var1 >> 16 & 0xFF);
         this.writer.writeByte(var1 >> 24 & 0xFF);
      } else {
         this.writer.writeInt(var1);
      }
   }

   void dumpString(LuaString var1) throws IOException {
      int var2 = var1.len().toint();
      this.dumpInt(var2 + 1);
      var1.write(this.writer, 0, var2);
      this.writer.write(0);
   }

   void dumpDouble(double var1) throws IOException {
      long var3 = Double.doubleToLongBits(var1);
      if (this.IS_LITTLE_ENDIAN) {
         this.dumpInt((int)var3);
         this.dumpInt((int)(var3 >> 32));
      } else {
         this.writer.writeLong(var3);
      }
   }

   void dumpCode(Prototype var1) throws IOException {
      int[] var2 = var1.code;
      int var3 = var2.length;
      this.dumpInt(var3);

      for (int var4 = 0; var4 < var3; var4++) {
         this.dumpInt(var2[var4]);
      }
   }

   void dumpConstants(Prototype var1) throws IOException {
      LuaValue[] var2 = var1.k;
      int var4 = var2.length;
      this.dumpInt(var4);

      for (int var3 = 0; var3 < var4; var3++) {
         LuaValue var5 = var2[var3];
         switch (var5.type()) {
            case 0:
               this.writer.write(0);
               break;
            case 1:
               this.writer.write(1);
               this.dumpChar(var5.toboolean() ? 1 : 0);
               break;
            case 2:
            default:
               throw new IllegalArgumentException("bad type for " + var5);
            case 3:
               switch (this.NUMBER_FORMAT) {
                  case 0:
                     this.writer.write(3);
                     this.dumpDouble(var5.todouble());
                     continue;
                  case 1:
                     if (!ALLOW_INTEGER_CASTING && !var5.isint()) {
                        throw new IllegalArgumentException("not an integer: " + var5);
                     }

                     this.writer.write(3);
                     this.dumpInt(var5.toint());
                     continue;
                  case 2:
                  case 3:
                  default:
                     throw new IllegalArgumentException("number format not supported: " + this.NUMBER_FORMAT);
                  case 4:
                     if (var5.isint()) {
                        this.writer.write(-2);
                        this.dumpInt(var5.toint());
                     } else {
                        this.writer.write(3);
                        this.dumpDouble(var5.todouble());
                     }
                     continue;
               }
            case 4:
               this.writer.write(4);
               this.dumpString((LuaString)var5);
         }
      }

      var4 = var1.p.length;
      this.dumpInt(var4);

      for (int var6 = 0; var6 < var4; var6++) {
         this.dumpFunction(var1.p[var6]);
      }
   }

   void dumpUpvalues(Prototype var1) throws IOException {
      int var2 = var1.upvalues.length;
      this.dumpInt(var2);

      for (int var3 = 0; var3 < var2; var3++) {
         this.writer.writeByte(var1.upvalues[var3].instack ? 1 : 0);
         this.writer.writeByte(var1.upvalues[var3].idx);
      }
   }

   void dumpDebug(Prototype var1) throws IOException {
      if (this.strip) {
         this.dumpInt(0);
      } else {
         this.dumpString(var1.source);
      }

      int var3 = this.strip ? 0 : var1.lineinfo.length;
      this.dumpInt(var3);

      for (int var2 = 0; var2 < var3; var2++) {
         this.dumpInt(var1.lineinfo[var2]);
      }

      var3 = this.strip ? 0 : var1.locvars.length;
      this.dumpInt(var3);

      for (int var5 = 0; var5 < var3; var5++) {
         LocVars var4 = var1.locvars[var5];
         this.dumpString(var4.varname);
         this.dumpInt(var4.startpc);
         this.dumpInt(var4.endpc);
      }

      var3 = this.strip ? 0 : var1.upvalues.length;
      this.dumpInt(var3);

      for (int var6 = 0; var6 < var3; var6++) {
         this.dumpString(var1.upvalues[var6].name);
      }
   }

   void dumpFunction(Prototype var1) throws IOException {
      this.dumpInt(var1.linedefined);
      this.dumpInt(var1.lastlinedefined);
      this.dumpChar(var1.numparams);
      this.dumpChar(var1.is_vararg);
      this.dumpChar(var1.maxstacksize);
      this.dumpCode(var1);
      this.dumpConstants(var1);
      this.dumpUpvalues(var1);
      this.dumpDebug(var1);
   }

   void dumpHeader() throws IOException {
      this.writer.write(LoadState.LUA_SIGNATURE);
      this.writer.write(82);
      this.writer.write(0);
      this.writer.write(this.IS_LITTLE_ENDIAN ? 1 : 0);
      this.writer.write(4);
      this.writer.write(4);
      this.writer.write(4);
      this.writer.write(this.SIZEOF_LUA_NUMBER);
      this.writer.write(this.NUMBER_FORMAT);
      this.writer.write(LoadState.LUAC_TAIL);
   }

   public static int dump(Prototype var0, OutputStream var1, boolean var2) throws IOException {
      DumpState var3 = new DumpState(var1, var2);
      var3.dumpHeader();
      var3.dumpFunction(var0);
      return var3.status;
   }

   public static int dump(Prototype var0, OutputStream var1, boolean var2, int var3, boolean var4) throws IOException {
      switch (var3) {
         case 0:
         case 1:
         case 4:
            DumpState var5 = new DumpState(var1, var2);
            var5.IS_LITTLE_ENDIAN = var4;
            var5.NUMBER_FORMAT = var3;
            var5.SIZEOF_LUA_NUMBER = var3 == 1 ? 4 : 8;
            var5.dumpHeader();
            var5.dumpFunction(var0);
            return var5.status;
         case 2:
         case 3:
         default:
            throw new IllegalArgumentException("number format not supported: " + var3);
      }
   }
}
