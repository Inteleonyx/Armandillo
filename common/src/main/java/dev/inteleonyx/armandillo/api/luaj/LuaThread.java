package dev.inteleonyx.armandillo.api.luaj;

import java.lang.ref.WeakReference;

public class LuaThread extends LuaValue {
   public static LuaValue s_metatable;
   public static int coroutine_count = 0;
   public static long thread_orphan_check_interval = 5000L;
   public static final int STATUS_INITIAL = 0;
   public static final int STATUS_SUSPENDED = 1;
   public static final int STATUS_RUNNING = 2;
   public static final int STATUS_NORMAL = 3;
   public static final int STATUS_DEAD = 4;
   public static final String[] STATUS_NAMES = new String[]{"suspended", "suspended", "running", "normal", "dead"};
   public final State state;
   public static final int MAX_CALLSTACK = 256;
   public Object callstack;
   public final Globals globals;
   public LuaValue errorfunc;

   public LuaThread(Globals var1) {
      this.state = new State(var1, this, null);
      this.state.status = 2;
      this.globals = var1;
   }

   public LuaThread(Globals var1, LuaValue var2) {
      LuaValue.assert_(var2 != null, "function cannot be null");
      this.state = new State(var1, this, var2);
      this.globals = var1;
   }

   public int type() {
      return 8;
   }

   public String typename() {
      return "thread";
   }

   public boolean isthread() {
      return true;
   }

   public LuaThread optthread(LuaThread var1) {
      return this;
   }

   public LuaThread checkthread() {
      return this;
   }

   public LuaValue getmetatable() {
      return s_metatable;
   }

   public String getStatus() {
      return STATUS_NAMES[this.state.status];
   }

   public boolean isMainThread() {
      return this.state.function == null;
   }

   public Varargs resume(Varargs var1) {
      State var2 = this.state;
      return var2.status > 1
         ? LuaValue.varargsOf(LuaValue.FALSE, LuaValue.valueOf("cannot resume " + (var2.status == 4 ? "dead" : "non-suspended") + " coroutine"))
         : var2.lua_resume(this, var1);
   }

   public static class State implements Runnable {
      private final Globals globals;
      final WeakReference lua_thread;
      public final LuaValue function;
      Varargs args = LuaValue.NONE;
      Varargs result = LuaValue.NONE;
      String error = null;
      public LuaValue hookfunc;
      public boolean hookline;
      public boolean hookcall;
      public boolean hookrtrn;
      public int hookcount;
      public boolean inhook;
      public int lastline;
      public int bytecodes;
      public int status = 0;

      State(Globals var1, LuaThread var2, LuaValue var3) {
         this.globals = var1;
         this.lua_thread = new WeakReference<>(var2);
         this.function = var3;
      }

      public synchronized void run() {
         try {
            Varargs var1 = this.args;
            this.args = LuaValue.NONE;
            this.result = this.function.invoke(var1);
         } catch (Throwable var5) {
            this.error = var5.getMessage();
         } finally {
            this.status = 4;
            this.notify();
         }
      }

      public synchronized Varargs lua_resume(LuaThread var1, Varargs var2) {
         LuaThread var3 = this.globals.running;

         Varargs var4;
         try {
            this.globals.running = var1;
            this.args = var2;
            if (this.status == 0) {
               this.status = 2;
               new Thread(this, "Coroutine-" + ++LuaThread.coroutine_count).start();
            } else {
               this.notify();
            }

            if (var3 != null) {
               var3.state.status = 3;
            }

            this.status = 2;
            this.wait();
            var4 = this.error != null ? LuaValue.varargsOf(LuaValue.FALSE, LuaValue.valueOf(this.error)) : LuaValue.varargsOf(LuaValue.TRUE, this.result);
         } catch (InterruptedException var8) {
            throw new OrphanedThread();
         } finally {
            this.args = LuaValue.NONE;
            this.result = LuaValue.NONE;
            this.error = null;
            this.globals.running = var3;
            if (var3 != null) {
               this.globals.running.state.status = 2;
            }
         }

         return var4;
      }

      public synchronized Varargs lua_yield(Varargs var1) {
         Varargs var2;
         try {
            this.result = var1;
            this.status = 1;
            this.notify();

            do {
               this.wait(LuaThread.thread_orphan_check_interval);
               if (this.lua_thread.get() == null) {
                  this.status = 4;
                  throw new OrphanedThread();
               }
            } while (this.status == 1);

            var2 = this.args;
         } catch (InterruptedException var6) {
            this.status = 4;
            throw new OrphanedThread();
         } finally {
            this.args = LuaValue.NONE;
            this.result = LuaValue.NONE;
         }

         return var2;
      }
   }
}
