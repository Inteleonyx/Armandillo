package dev.inteleonyx.armandillo.api.luaj.compiler;

import dev.inteleonyx.armandillo.api.luaj.*;

public class Constants extends Lua {
   public static final int MAXSTACK = 250;
   static final int LUAI_MAXUPVAL = 255;
   static final int LUAI_MAXVARS = 200;
   static final int NO_REG = 255;
   static final int iABC = 0;
   static final int iABx = 1;
   static final int iAsBx = 2;
   static final int OpArgN = 0;
   static final int OpArgU = 1;
   static final int OpArgR = 2;
   static final int OpArgK = 3;

   protected static void _assert(boolean var0) {
      if (!var0) {
         throw new LuaError("compiler assert failed");
      }
   }

   static void SET_OPCODE(InstructionPtr var0, int var1) {
      var0.set(var0.get() & -64 | var1 << 0 & 63);
   }

   static void SETARG_A(int[] var0, int var1, int var2) {
      var0[var1] = var0[var1] & -16321 | var2 << 6 & 16320;
   }

   static void SETARG_A(InstructionPtr var0, int var1) {
      var0.set(var0.get() & -16321 | var1 << 6 & 16320);
   }

   static void SETARG_B(InstructionPtr var0, int var1) {
      var0.set(var0.get() & 8388607 | var1 << 23 & -8388608);
   }

   static void SETARG_C(InstructionPtr var0, int var1) {
      var0.set(var0.get() & -8372225 | var1 << 14 & 8372224);
   }

   static void SETARG_Bx(InstructionPtr var0, int var1) {
      var0.set(var0.get() & 16383 | var1 << 14 & -16384);
   }

   static void SETARG_sBx(InstructionPtr var0, int var1) {
      SETARG_Bx(var0, var1 + 131071);
   }

   static int CREATE_ABC(int var0, int var1, int var2, int var3) {
      return var0 << 0 & 63 | var1 << 6 & 16320 | var2 << 23 & -8388608 | var3 << 14 & 8372224;
   }

   static int CREATE_ABx(int var0, int var1, int var2) {
      return var0 << 0 & 63 | var1 << 6 & 16320 | var2 << 14 & -16384;
   }

   static LuaValue[] realloc(LuaValue[] var0, int var1) {
      LuaValue[] var2 = new LuaValue[var1];
      if (var0 != null) {
         System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      }

      return var2;
   }

   static Prototype[] realloc(Prototype[] var0, int var1) {
      Prototype[] var2 = new Prototype[var1];
      if (var0 != null) {
         System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      }

      return var2;
   }

   static LuaString[] realloc(LuaString[] var0, int var1) {
      LuaString[] var2 = new LuaString[var1];
      if (var0 != null) {
         System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      }

      return var2;
   }

   static LocVars[] realloc(LocVars[] var0, int var1) {
      LocVars[] var2 = new LocVars[var1];
      if (var0 != null) {
         System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      }

      return var2;
   }

   static Upvaldesc[] realloc(Upvaldesc[] var0, int var1) {
      Upvaldesc[] var2 = new Upvaldesc[var1];
      if (var0 != null) {
         System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      }

      return var2;
   }

   static LexState.Vardesc[] realloc(LexState.Vardesc[] var0, int var1) {
      LexState.Vardesc[] var2 = new LexState.Vardesc[var1];
      if (var0 != null) {
         System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      }

      return var2;
   }

   static LexState.Labeldesc[] grow(LexState.Labeldesc[] var0, int var1) {
      return var0 == null ? new LexState.Labeldesc[2] : (var0.length < var1 ? realloc(var0, var0.length * 2) : var0);
   }

   static LexState.Labeldesc[] realloc(LexState.Labeldesc[] var0, int var1) {
      LexState.Labeldesc[] var2 = new LexState.Labeldesc[var1];
      if (var0 != null) {
         System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      }

      return var2;
   }

   static int[] realloc(int[] var0, int var1) {
      int[] var2 = new int[var1];
      if (var0 != null) {
         System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      }

      return var2;
   }

   static byte[] realloc(byte[] var0, int var1) {
      byte[] var2 = new byte[var1];
      if (var0 != null) {
         System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      }

      return var2;
   }

   static char[] realloc(char[] var0, int var1) {
      char[] var2 = new char[var1];
      if (var0 != null) {
         System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      }

      return var2;
   }

   protected Constants() {
   }
}
