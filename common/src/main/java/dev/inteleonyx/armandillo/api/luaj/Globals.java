package dev.inteleonyx.armandillo.api.luaj;

import dev.inteleonyx.armandillo.api.luaj.lib.BaseLib;
import dev.inteleonyx.armandillo.api.luaj.lib.DebugLib;
import dev.inteleonyx.armandillo.api.luaj.lib.PackageLib;
import dev.inteleonyx.armandillo.api.luaj.lib.ResourceFinder;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;

public class Globals extends LuaTable {
   public InputStream STDIN = null;
   public PrintStream STDOUT = System.out;
   public PrintStream STDERR = System.err;
   public ResourceFinder finder;
   public LuaThread running = new LuaThread(this);
   public BaseLib baselib;
   public PackageLib package_;
   public DebugLib debuglib;
   public Loader loader;
   public Compiler compiler;
   public Undumper undumper;

   public Globals checkglobals() {
      return this;
   }

   public LuaValue loadfile(String var1) {
      try {
         return this.load(this.finder.findResource(var1), "@" + var1, "bt", this);
      } catch (Exception var3) {
         return error("load " + var1 + ": " + var3);
      }
   }

   public LuaValue load(String var1, String var2) {
      return this.load(new StrReader(var1), var2);
   }

   public LuaValue load(String var1) {
      return this.load(new StrReader(var1), var1);
   }

   public LuaValue load(String var1, String var2, LuaTable var3) {
      return this.load(new StrReader(var1), var2, var3);
   }

   public LuaValue load(Reader var1, String var2) {
      return this.load(new UTF8Stream(var1), var2, "t", this);
   }

   public LuaValue load(Reader var1, String var2, LuaTable var3) {
      return this.load(new UTF8Stream(var1), var2, "t", var3);
   }

   public LuaValue load(InputStream var1, String var2, String var3, LuaValue var4) {
      try {
         Prototype var5 = this.loadPrototype(var1, var2, var3);
         return this.loader.load(var5, var2, var4);
      } catch (LuaError var6) {
         throw var6;
      } catch (Exception var7) {
         return error("load " + var2 + ": " + var7);
      }
   }

   public Prototype loadPrototype(InputStream var1, String var2, String var3) throws IOException {
      if (var3.indexOf(98) >= 0) {
         if (this.undumper == null) {
            error("No undumper.");
         }

         if (!var1.markSupported()) {
            var1 = new BufferedStream((InputStream)var1);
         }

         var1.mark(4);
         Prototype var4 = this.undumper.undump((InputStream)var1, var2);
         if (var4 != null) {
            return var4;
         }

         var1.reset();
      }

      if (var3.indexOf(116) >= 0) {
         return this.compilePrototype((InputStream)var1, var2);
      } else {
         error("Failed to load prototype " + var2 + " using mode '" + var3 + "'");
         return null;
      }
   }

   public Prototype compilePrototype(Reader var1, String var2) throws IOException {
      return this.compilePrototype(new UTF8Stream(var1), var2);
   }

   public Prototype compilePrototype(InputStream var1, String var2) throws IOException {
      if (this.compiler == null) {
         error("No compiler.");
      }

      return this.compiler.compile(var1, var2);
   }

   public Varargs yield(Varargs var1) {
      if (this.running != null && !this.running.isMainThread()) {
         LuaThread.State var2 = this.running.state;
         return var2.lua_yield(var1);
      } else {
         throw new LuaError("cannot yield main thread");
      }
   }

   abstract static class AbstractBufferedStream extends InputStream {
      protected byte[] b;
      protected int i = 0;
      protected int j = 0;

      protected AbstractBufferedStream(int var1) {
         this.b = new byte[var1];
      }

      protected abstract int avail() throws IOException;

      public int read() throws IOException {
         int var1 = this.avail();
         return var1 <= 0 ? -1 : 0xFF & this.b[this.i++];
      }

      public int read(byte[] var1) throws IOException {
         return this.read(var1, 0, var1.length);
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         int var4 = this.avail();
         if (var4 <= 0) {
            return -1;
         } else {
            int var5 = Math.min(var4, var3);
            System.arraycopy(this.b, this.i, var1, var2, var5);
            this.i += var5;
            return var5;
         }
      }

      public long skip(long var1) throws IOException {
         long var3 = Math.min(var1, (long)(this.j - this.i));
         this.i = (int)(this.i + var3);
         return var3;
      }

      public int available() throws IOException {
         return this.j - this.i;
      }
   }

   static class BufferedStream extends AbstractBufferedStream {
      private final InputStream s;

      public BufferedStream(InputStream var1) {
         this(128, var1);
      }

      BufferedStream(int var1, InputStream var2) {
         super(var1);
         this.s = var2;
      }

      protected int avail() throws IOException {
         if (this.i < this.j) {
            return this.j - this.i;
         } else {
            if (this.j >= this.b.length) {
               this.i = this.j = 0;
            }

            int var1 = this.s.read(this.b, this.j, this.b.length - this.j);
            if (var1 < 0) {
               return -1;
            } else {
               if (var1 == 0) {
                  int var2 = this.s.read();
                  if (var2 < 0) {
                     return -1;
                  }

                  this.b[this.j] = (byte)var2;
                  var1 = 1;
               }

               this.j += var1;
               return var1;
            }
         }
      }

      public void close() throws IOException {
         this.s.close();
      }

      public synchronized void mark(int var1) {
         if (this.i > 0 || var1 > this.b.length) {
            byte[] var2 = var1 > this.b.length ? new byte[var1] : this.b;
            System.arraycopy(this.b, this.i, var2, 0, this.j - this.i);
            this.j = this.j - this.i;
            this.i = 0;
            this.b = var2;
         }
      }

      public boolean markSupported() {
         return true;
      }

      public synchronized void reset() throws IOException {
         this.i = 0;
      }
   }

   public interface Compiler {
      Prototype compile(InputStream var1, String var2) throws IOException;
   }

   public interface Loader {
      LuaFunction load(Prototype var1, String var2, LuaValue var3) throws IOException;
   }

   static class StrReader extends Reader {
      final String s;
      int i = 0;
      final int n;

      StrReader(String var1) {
         this.s = var1;
         this.n = var1.length();
      }

      public void close() throws IOException {
         this.i = this.n;
      }

      public int read() throws IOException {
         return this.i < this.n ? this.s.charAt(this.i++) : -1;
      }

      public int read(char[] var1, int var2, int var3) throws IOException {
         int var4 = 0;

         while (var4 < var3 && this.i < this.n) {
            var1[var2 + var4] = this.s.charAt(this.i);
            var4++;
            this.i++;
         }

         return var4 <= 0 && var3 != 0 ? -1 : var4;
      }
   }

   static class UTF8Stream extends AbstractBufferedStream {
      private final char[] c = new char[32];
      private final Reader r;

      UTF8Stream(Reader var1) {
         super(96);
         this.r = var1;
      }

      protected int avail() throws IOException {
         if (this.i < this.j) {
            return this.j - this.i;
         } else {
            int var1 = this.r.read(this.c);
            if (var1 < 0) {
               return -1;
            } else {
               if (var1 == 0) {
                  int var2 = this.r.read();
                  if (var2 < 0) {
                     return -1;
                  }

                  this.c[0] = (char)var2;
                  var1 = 1;
               }

               this.j = LuaString.encodeToUtf8(this.c, var1, this.b, this.i = 0);
               return this.j;
            }
         }
      }

      public void close() throws IOException {
         this.r.close();
      }
   }

   public interface Undumper {
      Prototype undump(InputStream var1, String var2) throws IOException;
   }
}
