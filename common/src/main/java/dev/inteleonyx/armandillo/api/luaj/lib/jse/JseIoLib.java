package dev.inteleonyx.armandillo.api.luaj.lib.jse;

import dev.inteleonyx.armandillo.api.luaj.LuaError;
import dev.inteleonyx.armandillo.api.luaj.LuaString;
import dev.inteleonyx.armandillo.api.luaj.lib.IoLib;

import java.io.*;

public class JseIoLib extends IoLib {
   protected File wrapStdin() throws IOException {
      return new StdinFile();
   }

   protected File wrapStdout() throws IOException {
      return new StdoutFile(1);
   }

   protected File wrapStderr() throws IOException {
      return new StdoutFile(2);
   }

   protected File openFile(String var1, boolean var2, boolean var3, boolean var4, boolean var5) throws IOException {
      RandomAccessFile var6 = new RandomAccessFile(var1, var2 ? "r" : "rw");
      if (var3) {
         var6.seek(var6.length());
      } else if (!var2) {
         var6.setLength(0L);
      }

      return new FileImpl(var6);
   }

   protected File openProgram(String var1, String var2) throws IOException {
      Process var3 = Runtime.getRuntime().exec(var1);
      return "w".equals(var2) ? new FileImpl(var3.getOutputStream()) : new FileImpl(var3.getInputStream());
   }

   protected File tmpFile() throws IOException {
      java.io.File var1 = java.io.File.createTempFile(".luaj", "bin");
      var1.deleteOnExit();
      return new FileImpl(new RandomAccessFile(var1, "rw"));
   }

   private static void notimplemented() {
      throw new LuaError("not implemented");
   }

   private final class FileImpl extends File {
      private final RandomAccessFile file;
      private final InputStream is;
      private final OutputStream os;
      private boolean closed = false;
      private boolean nobuffer = false;

      private FileImpl(RandomAccessFile var2, InputStream var3, OutputStream var4) {
         this.file = var2;
         this.is = (InputStream)(var3 != null ? (var3.markSupported() ? var3 : new BufferedInputStream(var3)) : null);
         this.os = var4;
      }

      private FileImpl(RandomAccessFile var2) {
         this(var2, null, null);
      }

      private FileImpl(InputStream var2) {
         this(null, var2, null);
      }

      private FileImpl(OutputStream var2) {
         this(null, null, var2);
      }

      public String tojstring() {
         return "file (" + this.hashCode() + ")";
      }

      public boolean isstdfile() {
         return this.file == null;
      }

      public void close() throws IOException {
         this.closed = true;
         if (this.file != null) {
            this.file.close();
         }
      }

      public void flush() throws IOException {
         if (this.os != null) {
            this.os.flush();
         }
      }

      public void write(LuaString var1) throws IOException {
         if (this.os != null) {
            this.os.write(var1.m_bytes, var1.m_offset, var1.m_length);
         } else if (this.file != null) {
            this.file.write(var1.m_bytes, var1.m_offset, var1.m_length);
         } else {
            JseIoLib.notimplemented();
         }

         if (this.nobuffer) {
            this.flush();
         }
      }

      public boolean isclosed() {
         return this.closed;
      }

      public int seek(String var1, int var2) throws IOException {
         if (this.file != null) {
            if ("set".equals(var1)) {
               this.file.seek(var2);
            } else if ("end".equals(var1)) {
               this.file.seek(this.file.length() + var2);
            } else {
               this.file.seek(this.file.getFilePointer() + var2);
            }

            return (int)this.file.getFilePointer();
         } else {
            JseIoLib.notimplemented();
            return 0;
         }
      }

      public void setvbuf(String var1, int var2) {
         this.nobuffer = "no".equals(var1);
      }

      public int remaining() throws IOException {
         return this.file != null ? (int)(this.file.length() - this.file.getFilePointer()) : -1;
      }

      public int peek() throws IOException {
         if (this.is != null) {
            this.is.mark(1);
            int var4 = this.is.read();
            this.is.reset();
            return var4;
         } else if (this.file != null) {
            long var1 = this.file.getFilePointer();
            int var3 = this.file.read();
            this.file.seek(var1);
            return var3;
         } else {
            JseIoLib.notimplemented();
            return 0;
         }
      }

      public int read() throws IOException {
         if (this.is != null) {
            return this.is.read();
         } else if (this.file != null) {
            return this.file.read();
         } else {
            JseIoLib.notimplemented();
            return 0;
         }
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         if (this.file != null) {
            return this.file.read(var1, var2, var3);
         } else if (this.is != null) {
            return this.is.read(var1, var2, var3);
         } else {
            JseIoLib.notimplemented();
            return var3;
         }
      }
   }

   private final class StdinFile extends File {
      private StdinFile() {
      }

      public String tojstring() {
         return "file (" + this.hashCode() + ")";
      }

      public void write(LuaString var1) throws IOException {
      }

      public void flush() throws IOException {
      }

      public boolean isstdfile() {
         return true;
      }

      public void close() throws IOException {
      }

      public boolean isclosed() {
         return false;
      }

      public int seek(String var1, int var2) throws IOException {
         return 0;
      }

      public void setvbuf(String var1, int var2) {
      }

      public int remaining() throws IOException {
         return 0;
      }

      public int peek() throws IOException, EOFException {
         JseIoLib.this.globals.STDIN.mark(1);
         int var1 = JseIoLib.this.globals.STDIN.read();
         JseIoLib.this.globals.STDIN.reset();
         return var1;
      }

      public int read() throws IOException, EOFException {
         return JseIoLib.this.globals.STDIN.read();
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         return JseIoLib.this.globals.STDIN.read(var1, var2, var3);
      }
   }

   private final class StdoutFile extends File {
      private final int file_type;

      private StdoutFile(int var2) {
         this.file_type = var2;
      }

      public String tojstring() {
         return "file (" + this.hashCode() + ")";
      }

      private final PrintStream getPrintStream() {
         return this.file_type == 2 ? JseIoLib.this.globals.STDERR : JseIoLib.this.globals.STDOUT;
      }

      public void write(LuaString var1) throws IOException {
         this.getPrintStream().write(var1.m_bytes, var1.m_offset, var1.m_length);
      }

      public void flush() throws IOException {
         this.getPrintStream().flush();
      }

      public boolean isstdfile() {
         return true;
      }

      public void close() throws IOException {
      }

      public boolean isclosed() {
         return false;
      }

      public int seek(String var1, int var2) throws IOException {
         return 0;
      }

      public void setvbuf(String var1, int var2) {
      }

      public int remaining() throws IOException {
         return 0;
      }

      public int peek() throws IOException, EOFException {
         return 0;
      }

      public int read() throws IOException, EOFException {
         return 0;
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         return 0;
      }
   }
}
