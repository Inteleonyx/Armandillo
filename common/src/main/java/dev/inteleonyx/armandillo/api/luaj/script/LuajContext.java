package dev.inteleonyx.armandillo.api.luaj.script;

import dev.inteleonyx.armandillo.api.luaj.Globals;
import dev.inteleonyx.armandillo.api.luaj.lib.jse.JsePlatform;
import dev.inteleonyx.armandillo.api.luaj.luajc.LuaJC;

import javax.script.ScriptContext;
import javax.script.SimpleScriptContext;
import java.io.*;

public class LuajContext extends SimpleScriptContext implements ScriptContext {
   public final Globals globals;
   private final InputStream stdin;
   private final PrintStream stdout;
   private final PrintStream stderr;

   public LuajContext() {
      this("true".equals(System.getProperty("org.luaj.debug")), "true".equals(System.getProperty("org.luaj.luajc")));
   }

   public LuajContext(boolean var1, boolean var2) {
      this.globals = var1 ? JsePlatform.debugGlobals() : JsePlatform.standardGlobals();
      if (var2) {
         LuaJC.install(this.globals);
      }

      this.stdin = this.globals.STDIN;
      this.stdout = this.globals.STDOUT;
      this.stderr = this.globals.STDERR;
   }

   @Override
   public void setErrorWriter(Writer var1) {
      this.globals.STDERR = var1 != null ? new PrintStream(new WriterOutputStream(var1)) : this.stderr;
   }

   @Override
   public void setReader(Reader var1) {
      this.globals.STDIN = (InputStream)(var1 != null ? new ReaderInputStream(var1) : this.stdin);
   }

   @Override
   public void setWriter(Writer var1) {
      this.globals.STDOUT = var1 != null ? new PrintStream(new WriterOutputStream(var1), true) : this.stdout;
   }

   static final class ReaderInputStream extends InputStream {
      final Reader r;

      ReaderInputStream(Reader var1) {
         this.r = var1;
      }

      @Override
      public int read() throws IOException {
         return this.r.read();
      }
   }

   static final class WriterOutputStream extends OutputStream {
      final Writer w;

      WriterOutputStream(Writer var1) {
         this.w = var1;
      }

      @Override
      public void write(int var1) throws IOException {
         this.w.write(new String(new byte[]{(byte)var1}));
      }

      @Override
      public void write(byte[] var1, int var2, int var3) throws IOException {
         this.w.write(new String(var1, var2, var3));
      }

      @Override
      public void write(byte[] var1) throws IOException {
         this.w.write(new String(var1));
      }

      @Override
      public void close() throws IOException {
         this.w.close();
      }

      @Override
      public void flush() throws IOException {
         this.w.flush();
      }
   }
}
