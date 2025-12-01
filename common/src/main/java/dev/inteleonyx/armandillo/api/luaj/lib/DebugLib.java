package dev.inteleonyx.armandillo.api.luaj.lib;

import dev.inteleonyx.armandillo.api.luaj.*;

public class DebugLib extends TwoArgFunction {
   public static boolean CALLS;
   public static boolean TRACE;
   static final LuaString LUA;
   private static final LuaString QMARK;
   private static final LuaString CALL;
   private static final LuaString LINE;
   private static final LuaString COUNT;
   private static final LuaString RETURN;
   static final LuaString FUNC;
   static final LuaString ISTAILCALL;
   static final LuaString ISVARARG;
   static final LuaString NUPS;
   static final LuaString NPARAMS;
   static final LuaString NAME;
   static final LuaString NAMEWHAT;
   static final LuaString WHAT;
   static final LuaString SOURCE;
   static final LuaString SHORT_SRC;
   static final LuaString LINEDEFINED;
   static final LuaString LASTLINEDEFINED;
   static final LuaString CURRENTLINE;
   static final LuaString ACTIVELINES;
   Globals globals;

   public LuaValue call(LuaValue var1, LuaValue var2) {
      this.globals = var2.checkglobals();
      this.globals.debuglib = this;
      LuaTable var3 = new LuaTable();
      var3.set("debug", new debug());
      var3.set("gethook", new gethook());
      var3.set("getinfo", new getinfo());
      var3.set("getlocal", new getlocal());
      var3.set("getmetatable", new getmetatable());
      var3.set("getregistry", new getregistry());
      var3.set("getupvalue", new getupvalue());
      var3.set("getuservalue", new getuservalue());
      var3.set("sethook", new sethook());
      var3.set("setlocal", new setlocal());
      var3.set("setmetatable", new setmetatable());
      var3.set("setupvalue", new setupvalue());
      var3.set("setuservalue", new setuservalue());
      var3.set("traceback", new traceback());
      var3.set("upvalueid", new upvalueid());
      var3.set("upvaluejoin", new upvaluejoin());
      var2.set("debug", var3);
      if (!var2.get("package").isnil()) {
         var2.get("package").get("loaded").set("debug", var3);
      }

      return var3;
   }

   public void onCall(LuaFunction var1) {
      LuaThread.State var2 = this.globals.running.state;
      if (!var2.inhook) {
         this.callstack().onCall(var1);
         if (var2.hookcall) {
            this.callHook(var2, CALL, NIL);
         }
      }
   }

   public void onCall(LuaClosure var1, Varargs var2, LuaValue[] var3) {
      LuaThread.State var4 = this.globals.running.state;
      if (!var4.inhook) {
         this.callstack().onCall(var1, var2, var3);
         if (var4.hookcall) {
            this.callHook(var4, CALL, NIL);
         }
      }
   }

   public void onInstruction(int var1, Varargs var2, int var3) {
      LuaThread.State var4 = this.globals.running.state;
      if (!var4.inhook) {
         this.callstack().onInstruction(var1, var2, var3);
         if (var4.hookfunc != null) {
            if (var4.hookcount > 0 && ++var4.bytecodes % var4.hookcount == 0) {
               this.callHook(var4, COUNT, NIL);
            }

            if (var4.hookline) {
               int var5 = this.callstack().currentline();
               if (var5 != var4.lastline) {
                  var4.lastline = var5;
                  this.callHook(var4, LINE, LuaValue.valueOf(var5));
               }
            }
         }
      }
   }

   public void onReturn() {
      LuaThread.State var1 = this.globals.running.state;
      if (!var1.inhook) {
         this.callstack().onReturn();
         if (var1.hookrtrn) {
            this.callHook(var1, RETURN, NIL);
         }
      }
   }

   public String traceback(int var1) {
      return this.callstack().traceback(var1);
   }

   void callHook(LuaThread.State var1, LuaValue var2, LuaValue var3) {
      if (!var1.inhook && var1.hookfunc != null) {
         var1.inhook = true;

         try {
            var1.hookfunc.call(var2, var3);
         } catch (LuaError var9) {
            throw var9;
         } catch (RuntimeException var10) {
            throw new LuaError(var10);
         } finally {
            var1.inhook = false;
         }
      }
   }

   CallStack callstack() {
      return this.callstack(this.globals.running);
   }

   CallStack callstack(LuaThread var1) {
      if (var1.callstack == null) {
         var1.callstack = new CallStack();
      }

      return (CallStack)var1.callstack;
   }

   static LuaString findupvalue(LuaClosure var0, int var1) {
      if (var0.upValues == null || var1 <= 0 || var1 > var0.upValues.length) {
         return null;
      } else {
         return var0.p.upvalues != null && var1 <= var0.p.upvalues.length ? var0.p.upvalues[var1 - 1].name : LuaString.valueOf("." + var1);
      }
   }

   static void lua_assert(boolean var0) {
      if (!var0) {
         throw new RuntimeException("lua_assert failed");
      }
   }

   static NameWhat getfuncname(CallFrame var0) {
      if (!var0.f.isclosure()) {
         return new NameWhat(var0.f.classnamestub(), "Java");
      } else {
         Prototype var1 = var0.f.checkclosure().p;
         int var2 = var0.pc;
         int var3 = var1.code[var2];
         LuaString var4;
         switch (Lua.GET_OPCODE(var3)) {
            case 6:
            case 7:
            case 12:
               var4 = LuaValue.INDEX;
               break;
            case 8:
            case 10:
               var4 = LuaValue.NEWINDEX;
               break;
            case 9:
            case 11:
            case 20:
            case 23:
            case 27:
            case 28:
            case 31:
            case 32:
            case 33:
            default:
               return null;
            case 13:
               var4 = LuaValue.ADD;
               break;
            case 14:
               var4 = LuaValue.SUB;
               break;
            case 15:
               var4 = LuaValue.MUL;
               break;
            case 16:
               var4 = LuaValue.DIV;
               break;
            case 17:
               var4 = LuaValue.MOD;
               break;
            case 18:
               var4 = LuaValue.POW;
               break;
            case 19:
               var4 = LuaValue.UNM;
               break;
            case 21:
               var4 = LuaValue.LEN;
               break;
            case 22:
               var4 = LuaValue.CONCAT;
               break;
            case 24:
               var4 = LuaValue.EQ;
               break;
            case 25:
               var4 = LuaValue.LT;
               break;
            case 26:
               var4 = LuaValue.LE;
               break;
            case 29:
            case 30:
               return getobjname(var1, var2, Lua.GETARG_A(var3));
            case 34:
               return new NameWhat("(for iterator)", "(for iterator");
         }

         return new NameWhat(var4.tojstring(), "metamethod");
      }
   }

   public static NameWhat getobjname(Prototype var0, int var1, int var2) {
      LuaString var4 = var0.getlocalname(var2 + 1, var1);
      if (var4 != null) {
         return new NameWhat(var4.tojstring(), "local");
      } else {
         int var3 = findsetreg(var0, var1, var2);
         if (var3 != -1) {
            int var5 = var0.code[var3];
            switch (Lua.GET_OPCODE(var5)) {
               case 0:
                  int var16 = Lua.GETARG_A(var5);
                  int var17 = Lua.GETARG_B(var5);
                  if (var17 < var16) {
                     return getobjname(var0, var3, var17);
                  }
                  break;
               case 1:
               case 2:
                  int var15 = Lua.GET_OPCODE(var5) == 1 ? Lua.GETARG_Bx(var5) : Lua.GETARG_Ax(var0.code[var3 + 1]);
                  if (var0.k[var15].isstring()) {
                     var4 = var0.k[var15].strvalue();
                     return new NameWhat(var4.tojstring(), "constant");
                  }
               case 3:
               case 4:
               case 8:
               case 9:
               case 10:
               case 11:
               default:
                  break;
               case 5:
                  int var14 = Lua.GETARG_B(var5);
                  var4 = var14 < var0.upvalues.length ? var0.upvalues[var14].name : QMARK;
                  return new NameWhat(var4.tojstring(), "upvalue");
               case 6:
               case 7:
                  int var13 = Lua.GETARG_C(var5);
                  int var7 = Lua.GETARG_B(var5);
                  LuaString var8 = Lua.GET_OPCODE(var5) == 7
                     ? var0.getlocalname(var7 + 1, var3)
                     : (var7 < var0.upvalues.length ? var0.upvalues[var7].name : QMARK);
                  var4 = kname(var0, var13);
                  return new NameWhat(var4.tojstring(), var8 != null && var8.eq_b(ENV) ? "global" : "field");
               case 12:
                  int var6 = Lua.GETARG_C(var5);
                  var4 = kname(var0, var6);
                  return new NameWhat(var4.tojstring(), "method");
            }
         }

         return null;
      }
   }

   static LuaString kname(Prototype var0, int var1) {
      return Lua.ISK(var1) && var0.k[Lua.INDEXK(var1)].isstring() ? var0.k[Lua.INDEXK(var1)].strvalue() : QMARK;
   }

   static int findsetreg(Prototype var0, int var1, int var2) {
      int var4 = -1;

      for (int var3 = 0; var3 < var1; var3++) {
         int var5 = var0.code[var3];
         int var6 = Lua.GET_OPCODE(var5);
         int var7 = Lua.GETARG_A(var5);
         switch (var6) {
            case 4:
               int var10 = Lua.GETARG_B(var5);
               if (var7 <= var2 && var2 <= var7 + var10) {
                  var4 = var3;
               }
               break;
            case 23:
               int var8 = Lua.GETARG_sBx(var5);
               int var9 = var3 + 1 + var8;
               if (var3 < var9 && var9 <= var1) {
                  var3 += var8;
               }
               break;
            case 27:
               if (var2 == var7) {
                  var4 = var3;
               }
               break;
            case 29:
            case 30:
               if (var2 >= var7) {
                  var4 = var3;
               }
               break;
            case 34:
               if (var2 >= var7 + 2) {
                  var4 = var3;
               }
               break;
            case 36:
               if ((var5 >> 14 & 511) == 0) {
                  var3++;
               }
               break;
            default:
               if (Lua.testAMode(var6) && var2 == var7) {
                  var4 = var3;
               }
         }
      }

      return var4;
   }

   static {
      try {
         CALLS = null != System.getProperty("CALLS");
      } catch (Exception var2) {
      }

      try {
         TRACE = null != System.getProperty("TRACE");
      } catch (Exception var1) {
      }

      LUA = valueOf("Lua");
      QMARK = valueOf("?");
      CALL = valueOf("call");
      LINE = valueOf("line");
      COUNT = valueOf("count");
      RETURN = valueOf("return");
      FUNC = valueOf("func");
      ISTAILCALL = valueOf("istailcall");
      ISVARARG = valueOf("isvararg");
      NUPS = valueOf("nups");
      NPARAMS = valueOf("nparams");
      NAME = valueOf("name");
      NAMEWHAT = valueOf("namewhat");
      WHAT = valueOf("what");
      SOURCE = valueOf("source");
      SHORT_SRC = valueOf("short_src");
      LINEDEFINED = valueOf("linedefined");
      LASTLINEDEFINED = valueOf("lastlinedefined");
      CURRENTLINE = valueOf("currentline");
      ACTIVELINES = valueOf("activelines");
   }

   static class CallFrame {
      LuaFunction f;
      int pc;
      int top;
      Varargs v;
      LuaValue[] stack;
      CallFrame previous;

      void set(LuaClosure var1, Varargs var2, LuaValue[] var3) {
         this.f = var1;
         this.v = var2;
         this.stack = var3;
      }

      public String shortsource() {
         return this.f.isclosure() ? this.f.checkclosure().p.shortsource() : "[Java]";
      }

      void set(LuaFunction var1) {
         this.f = var1;
      }

      void reset() {
         this.f = null;
         this.v = null;
         this.stack = null;
      }

      void instr(int var1, Varargs var2, int var3) {
         this.pc = var1;
         this.v = var2;
         this.top = var3;
         if (DebugLib.TRACE) {
            Print.printState(this.f.checkclosure(), var1, this.stack, var3, var2);
         }
      }

      Varargs getLocal(int var1) {
         LuaString var2 = this.getlocalname(var1);
         return (Varargs)(var1 >= 1 && var1 <= this.stack.length && this.stack[var1 - 1] != null
            ? LuaValue.varargsOf((LuaValue)(var2 == null ? LuaValue.NIL : var2), this.stack[var1 - 1])
            : LuaValue.NIL);
      }

      Varargs setLocal(int var1, LuaValue var2) {
         LuaString var3 = this.getlocalname(var1);
         if (var1 >= 1 && var1 <= this.stack.length && this.stack[var1 - 1] != null) {
            this.stack[var1 - 1] = var2;
            return (Varargs)(var3 == null ? LuaValue.NIL : var3);
         } else {
            return LuaValue.NIL;
         }
      }

      int currentline() {
         if (!this.f.isclosure()) {
            return -1;
         } else {
            int[] var1 = this.f.checkclosure().p.lineinfo;
            return var1 != null && this.pc >= 0 && this.pc < var1.length ? var1[this.pc] : -1;
         }
      }

      String sourceline() {
         return !this.f.isclosure() ? this.f.tojstring() : this.f.checkclosure().p.shortsource() + ":" + this.currentline();
      }

      int linedefined() {
         return this.f.isclosure() ? this.f.checkclosure().p.linedefined : -1;
      }

      LuaString getlocalname(int var1) {
         return !this.f.isclosure() ? null : this.f.checkclosure().p.getlocalname(var1, this.pc);
      }
   }

   public static class CallStack {
      static final CallFrame[] EMPTY = new CallFrame[0];
      CallFrame[] frame = EMPTY;
      int calls = 0;

      CallStack() {
      }

      synchronized int currentline() {
         return this.calls > 0 ? this.frame[this.calls - 1].currentline() : -1;
      }

      private synchronized CallFrame pushcall() {
         if (this.calls >= this.frame.length) {
            int var1 = Math.max(4, this.frame.length * 3 / 2);
            CallFrame[] var2 = new CallFrame[var1];
            System.arraycopy(this.frame, 0, var2, 0, this.frame.length);

            for (int var3 = this.frame.length; var3 < var1; var3++) {
               var2[var3] = new CallFrame();
            }

            this.frame = var2;

            for (int var4 = 1; var4 < var1; var4++) {
               var2[var4].previous = var2[var4 - 1];
            }
         }

         return this.frame[this.calls++];
      }

      final synchronized void onCall(LuaFunction var1) {
         this.pushcall().set(var1);
      }

      final synchronized void onCall(LuaClosure var1, Varargs var2, LuaValue[] var3) {
         this.pushcall().set(var1, var2, var3);
      }

      final synchronized void onReturn() {
         if (this.calls > 0) {
            this.frame[--this.calls].reset();
         }
      }

      final synchronized void onInstruction(int var1, Varargs var2, int var3) {
         if (this.calls > 0) {
            this.frame[this.calls - 1].instr(var1, var2, var3);
         }
      }

      synchronized String traceback(int var1) {
         StringBuffer var2 = new StringBuffer();
         var2.append("stack traceback:");

         CallFrame var3;
         while ((var3 = this.getCallFrame(var1++)) != null) {
            var2.append("\n\t");
            var2.append(var3.shortsource());
            var2.append(':');
            if (var3.currentline() > 0) {
               var2.append(var3.currentline() + ":");
            }

            var2.append(" in ");
            DebugInfo var4 = this.auxgetinfo("n", var3.f, var3);
            if (var3.linedefined() == 0) {
               var2.append("main chunk");
            } else if (var4.name != null) {
               var2.append("function '");
               var2.append(var4.name);
               var2.append('\'');
            } else {
               var2.append("function <");
               var2.append(var3.shortsource());
               var2.append(':');
               var2.append(var3.linedefined());
               var2.append('>');
            }
         }

         var2.append("\n\t[Java]: in ?");
         return var2.toString();
      }

      synchronized CallFrame getCallFrame(int var1) {
         return var1 >= 1 && var1 <= this.calls ? this.frame[this.calls - var1] : null;
      }

      synchronized CallFrame findCallFrame(LuaValue var1) {
         for (int var2 = 1; var2 <= this.calls; var2++) {
            if (this.frame[this.calls - var2].f == var1) {
               return this.frame[var2];
            }
         }

         return null;
      }

      synchronized DebugInfo auxgetinfo(String var1, LuaFunction var2, CallFrame var3) {
         DebugInfo var4 = new DebugInfo();
         int var5 = 0;

         for (int var6 = var1.length(); var5 < var6; var5++) {
            switch (var1.charAt(var5)) {
               case 'L':
               case 'f':
               default:
                  break;
               case 'S':
                  var4.funcinfo(var2);
                  break;
               case 'l':
                  var4.currentline = var3 != null && var3.f.isclosure() ? var3.currentline() : -1;
                  break;
               case 'n':
                  if (var3 != null && var3.previous != null && var3.previous.f.isclosure()) {
                     NameWhat var8 = DebugLib.getfuncname(var3.previous);
                     if (var8 != null) {
                        var4.name = var8.name;
                        var4.namewhat = var8.namewhat;
                     }
                  }

                  if (var4.namewhat == null) {
                     var4.namewhat = "";
                     var4.name = null;
                  }
                  break;
               case 't':
                  var4.istailcall = false;
                  break;
               case 'u':
                  if (var2 != null && var2.isclosure()) {
                     Prototype var7 = var2.checkclosure().p;
                     var4.nups = (short)var7.upvalues.length;
                     var4.nparams = (short)var7.numparams;
                     var4.isvararg = var7.is_vararg != 0;
                  } else {
                     var4.nups = 0;
                     var4.isvararg = true;
                     var4.nparams = 0;
                  }
            }
         }

         return var4;
      }
   }

   static class DebugInfo {
      String name;
      String namewhat;
      String what;
      String source;
      int currentline;
      int linedefined;
      int lastlinedefined;
      short nups;
      short nparams;
      boolean isvararg;
      boolean istailcall;
      String short_src;
      CallFrame cf;

      public void funcinfo(LuaFunction var1) {
         if (var1.isclosure()) {
            Prototype var2 = var1.checkclosure().p;
            this.source = var2.source != null ? var2.source.tojstring() : "=?";
            this.linedefined = var2.linedefined;
            this.lastlinedefined = var2.lastlinedefined;
            this.what = this.linedefined == 0 ? "main" : "Lua";
            this.short_src = var2.shortsource();
         } else {
            this.source = "=[Java]";
            this.linedefined = -1;
            this.lastlinedefined = -1;
            this.what = "Java";
            this.short_src = var1.name();
         }
      }
   }

   static class NameWhat {
      final String name;
      final String namewhat;

      NameWhat(String var1, String var2) {
         this.name = var1;
         this.namewhat = var2;
      }
   }

   static final class debug extends ZeroArgFunction {
      public LuaValue call() {
         return NONE;
      }
   }

   final class gethook extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         LuaThread var2 = var1.narg() > 0 ? var1.checkthread(1) : DebugLib.this.globals.running;
         LuaThread.State var3 = var2.state;
         return varargsOf(
            var3.hookfunc != null ? var3.hookfunc : NIL,
            valueOf((var3.hookcall ? "c" : "") + (var3.hookline ? "l" : "") + (var3.hookrtrn ? "r" : "")),
            valueOf(var3.hookcount)
         );
      }
   }

   final class getinfo extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         int var2 = 1;
         LuaThread var3 = var1.isthread(var2) ? var1.checkthread(var2++) : DebugLib.this.globals.running;
         Object var4 = var1.arg(var2++);
         String var5 = var1.optjstring(var2++, "flnStu");
         CallStack var6 = DebugLib.this.callstack(var3);
         CallFrame var7;
         if (((LuaValue)var4).isnumber()) {
            var7 = var6.getCallFrame(((LuaValue)var4).toint());
            if (var7 == null) {
               return NONE;
            }

            var4 = var7.f;
         } else {
            if (!((LuaValue)var4).isfunction()) {
               return argerror(var2 - 2, "function or level");
            }

            var7 = var6.findCallFrame((LuaValue)var4);
         }

         DebugInfo var8 = var6.auxgetinfo(var5, (LuaFunction)var4, var7);
         LuaTable var9 = new LuaTable();
         if (var5.indexOf(83) >= 0) {
            var9.set(DebugLib.WHAT, DebugLib.LUA);
            var9.set(DebugLib.SOURCE, valueOf(var8.source));
            var9.set(DebugLib.SHORT_SRC, valueOf(var8.short_src));
            var9.set(DebugLib.LINEDEFINED, valueOf(var8.linedefined));
            var9.set(DebugLib.LASTLINEDEFINED, valueOf(var8.lastlinedefined));
         }

         if (var5.indexOf(108) >= 0) {
            var9.set(DebugLib.CURRENTLINE, valueOf(var8.currentline));
         }

         if (var5.indexOf(117) >= 0) {
            var9.set(DebugLib.NUPS, valueOf(var8.nups));
            var9.set(DebugLib.NPARAMS, valueOf(var8.nparams));
            var9.set(DebugLib.ISVARARG, var8.isvararg ? ONE : ZERO);
         }

         if (var5.indexOf(110) >= 0) {
            var9.set(DebugLib.NAME, LuaValue.valueOf(var8.name != null ? var8.name : "?"));
            var9.set(DebugLib.NAMEWHAT, LuaValue.valueOf(var8.namewhat));
         }

         if (var5.indexOf(116) >= 0) {
            var9.set(DebugLib.ISTAILCALL, ZERO);
         }

         if (var5.indexOf(76) >= 0) {
            LuaTable var10 = new LuaTable();
            var9.set(DebugLib.ACTIVELINES, var10);

            CallFrame var11;
            for (int var12 = 1; (var11 = var6.getCallFrame(var12)) != null; var12++) {
               if (var11.f == var4) {
                  var10.insert(-1, valueOf(var11.currentline()));
               }
            }
         }

         if (var5.indexOf(102) >= 0 && var4 != null) {
            var9.set(DebugLib.FUNC, (LuaValue)var4);
         }

         return var9;
      }
   }

   final class getlocal extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         int var2 = 1;
         LuaThread var3 = var1.isthread(var2) ? var1.checkthread(var2++) : DebugLib.this.globals.running;
         int var4 = var1.checkint(var2++);
         int var5 = var1.checkint(var2++);
         CallFrame var6 = DebugLib.this.callstack(var3).getCallFrame(var4);
         return (Varargs)(var6 != null ? var6.getLocal(var5) : NONE);
      }
   }

   static final class getmetatable extends LibFunction {
      public LuaValue call(LuaValue var1) {
         LuaValue var2 = var1.getmetatable();
         return var2 != null ? var2 : NIL;
      }
   }

   final class getregistry extends ZeroArgFunction {
      public LuaValue call() {
         return DebugLib.this.globals;
      }
   }

   static final class getupvalue extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         LuaFunction var2 = var1.checkfunction(1);
         int var3 = var1.checkint(2);
         if (var2 instanceof LuaClosure) {
            LuaClosure var4 = (LuaClosure)var2;
            LuaString var5 = DebugLib.findupvalue(var4, var3);
            if (var5 != null) {
               return varargsOf(var5, var4.upValues[var3 - 1].getValue());
            }
         }

         return NIL;
      }
   }

   static final class getuservalue extends LibFunction {
      public LuaValue call(LuaValue var1) {
         return var1.isuserdata() ? var1 : NIL;
      }
   }

   final class sethook extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         int var2 = 1;
         LuaThread var3 = var1.isthread(var2) ? var1.checkthread(var2++) : DebugLib.this.globals.running;
         LuaFunction var4 = var1.optfunction(var2++, null);
         String var5 = var1.optjstring(var2++, "");
         int var6 = var1.optint(var2++, 0);
         boolean var7 = false;
         boolean var8 = false;
         boolean var9 = false;

         for (int var10 = 0; var10 < var5.length(); var10++) {
            switch (var5.charAt(var10)) {
               case 'c':
                  var7 = true;
                  break;
               case 'l':
                  var8 = true;
                  break;
               case 'r':
                  var9 = true;
            }
         }

         LuaThread.State var14 = var3.state;
         var14.hookfunc = var4;
         var14.hookcall = var7;
         var14.hookline = var8;
         var14.hookcount = var6;
         var14.hookrtrn = var9;
         return NONE;
      }
   }

   final class setlocal extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         int var2 = 1;
         LuaThread var3 = var1.isthread(var2) ? var1.checkthread(var2++) : DebugLib.this.globals.running;
         int var4 = var1.checkint(var2++);
         int var5 = var1.checkint(var2++);
         LuaValue var6 = var1.arg(var2++);
         CallFrame var7 = DebugLib.this.callstack(var3).getCallFrame(var4);
         return (Varargs)(var7 != null ? var7.setLocal(var5, var6) : NONE);
      }
   }

   static final class setmetatable extends TwoArgFunction {
      public LuaValue call(LuaValue var1, LuaValue var2) {
         LuaTable var3 = var2.opttable(null);
         switch (var1.type()) {
            case 0:
               LuaNil.s_metatable = var3;
               break;
            case 1:
               LuaBoolean.s_metatable = var3;
               break;
            case 2:
            case 5:
            case 7:
            default:
               var1.setmetatable(var3);
               break;
            case 3:
               LuaNumber.s_metatable = var3;
               break;
            case 4:
               LuaString.s_metatable = var3;
               break;
            case 6:
               LuaFunction.s_metatable = var3;
               break;
            case 8:
               LuaThread.s_metatable = var3;
         }

         return var1;
      }
   }

   static final class setupvalue extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         LuaFunction var2 = var1.checkfunction(1);
         int var3 = var1.checkint(2);
         LuaValue var4 = var1.arg(3);
         if (var2 instanceof LuaClosure) {
            LuaClosure var5 = (LuaClosure)var2;
            LuaString var6 = DebugLib.findupvalue(var5, var3);
            if (var6 != null) {
               var5.upValues[var3 - 1].setValue(var4);
               return var6;
            }
         }

         return NIL;
      }
   }

   static final class setuservalue extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         Object var2 = var1.checkuserdata(1);
         LuaValue var3 = var1.checkvalue(2);
         LuaUserdata var4 = (LuaUserdata)var1.arg1();
         var4.m_instance = var3.checkuserdata();
         var4.m_metatable = var3.getmetatable();
         return NONE;
      }
   }

   final class traceback extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         int var2 = 1;
         LuaThread var3 = var1.isthread(var2) ? var1.checkthread(var2++) : DebugLib.this.globals.running;
         String var4 = var1.optjstring(var2++, null);
         int var5 = var1.optint(var2++, 1);
         String var6 = DebugLib.this.callstack(var3).traceback(var5);
         return valueOf(var4 != null ? var4 + "\n" + var6 : var6);
      }
   }

   static final class upvalueid extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         LuaFunction var2 = var1.checkfunction(1);
         int var3 = var1.checkint(2);
         if (var2 instanceof LuaClosure) {
            LuaClosure var4 = (LuaClosure)var2;
            if (var4.upValues != null && var3 > 0 && var3 <= var4.upValues.length) {
               return valueOf(var4.upValues[var3 - 1].hashCode());
            }
         }

         return NIL;
      }
   }

   static final class upvaluejoin extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         LuaClosure var2 = var1.checkclosure(1);
         int var3 = var1.checkint(2);
         LuaClosure var4 = var1.checkclosure(3);
         int var5 = var1.checkint(4);
         if (var3 < 1 || var3 > var2.upValues.length) {
            this.argerror("index out of range");
         }

         if (var5 < 1 || var5 > var4.upValues.length) {
            this.argerror("index out of range");
         }

         var2.upValues[var3 - 1] = var4.upValues[var5 - 1];
         return NONE;
      }
   }
}
