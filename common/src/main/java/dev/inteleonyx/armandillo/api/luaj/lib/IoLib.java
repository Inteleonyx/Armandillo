package dev.inteleonyx.armandillo.api.luaj.lib;

import dev.inteleonyx.armandillo.api.luaj.*;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;

public abstract class IoLib extends TwoArgFunction {
   protected static final int FTYPE_STDIN = 0;
   protected static final int FTYPE_STDOUT = 1;
   protected static final int FTYPE_STDERR = 2;
   protected static final int FTYPE_NAMED = 3;
   private File infile = null;
   private File outfile = null;
   private File errfile = null;
   private static final LuaValue STDIN = valueOf("stdin");
   private static final LuaValue STDOUT = valueOf("stdout");
   private static final LuaValue STDERR = valueOf("stderr");
   private static final LuaValue FILE = valueOf("file");
   private static final LuaValue CLOSED_FILE = valueOf("closed file");
   private static final int IO_CLOSE = 0;
   private static final int IO_FLUSH = 1;
   private static final int IO_INPUT = 2;
   private static final int IO_LINES = 3;
   private static final int IO_OPEN = 4;
   private static final int IO_OUTPUT = 5;
   private static final int IO_POPEN = 6;
   private static final int IO_READ = 7;
   private static final int IO_TMPFILE = 8;
   private static final int IO_TYPE = 9;
   private static final int IO_WRITE = 10;
   private static final int FILE_CLOSE = 11;
   private static final int FILE_FLUSH = 12;
   private static final int FILE_LINES = 13;
   private static final int FILE_READ = 14;
   private static final int FILE_SEEK = 15;
   private static final int FILE_SETVBUF = 16;
   private static final int FILE_WRITE = 17;
   private static final int IO_INDEX = 18;
   private static final int LINES_ITER = 19;
   public static final String[] IO_NAMES = new String[]{"close", "flush", "input", "lines", "open", "output", "popen", "read", "tmpfile", "type", "write"};
   public static final String[] FILE_NAMES = new String[]{"close", "flush", "lines", "read", "seek", "setvbuf", "write"};
   LuaTable filemethods;
   protected Globals globals;

   protected abstract File wrapStdin() throws IOException;

   protected abstract File wrapStdout() throws IOException;

   protected abstract File wrapStderr() throws IOException;

   protected abstract File openFile(String var1, boolean var2, boolean var3, boolean var4, boolean var5) throws IOException;

   protected abstract File tmpFile() throws IOException;

   protected abstract File openProgram(String var1, String var2) throws IOException;

   public LuaValue call(LuaValue var1, LuaValue var2) {
      this.globals = var2.checkglobals();
      LuaTable var3 = new LuaTable();
      this.bind(var3, IoLib.IoLibV.class, IO_NAMES);

      this.filemethods = new LuaTable();
      this.bind(this.filemethods, IoLib.IoLibV.class, FILE_NAMES, 11);

      LuaTable var4 = new LuaTable();
      this.bind(var4, IoLib.IoLibV.class, new String[]{"__index"}, 18);
      var3.setmetatable(var4);
      this.setLibInstance(var3);
      this.setLibInstance(this.filemethods);
      this.setLibInstance(var4);
      var2.set("io", var3);
      if (!var2.get("package").isnil()) {
         var2.get("package").get("loaded").set("io", var3);
      }

      return var3;
   }

   private void setLibInstance(LuaTable var1) {
      LuaValue[] var2 = var1.keys();
      int var3 = 0;

      for (int var4 = var2.length; var3 < var4; var3++) {
         ((IoLibV)var1.get(var2[var3])).iolib = this;
      }
   }

   private File input() {
      return this.infile != null ? this.infile : (this.infile = this.ioopenfile(0, "-", "r"));
   }

   public Varargs _io_flush() throws IOException {
      checkopen(this.output());
      this.outfile.flush();
      return LuaValue.TRUE;
   }

   public Varargs _io_tmpfile() throws IOException {
      return this.tmpFile();
   }

   public Varargs _io_close(LuaValue var1) throws IOException {
      File var2 = var1.isnil() ? this.output() : checkfile(var1);
      checkopen(var2);
      return ioclose(var2);
   }

   public Varargs _io_input(LuaValue var1) {
      this.infile = var1.isnil() ? this.input() : (var1.isstring() ? this.ioopenfile(3, var1.checkjstring(), "r") : checkfile(var1));
      return this.infile;
   }

   public Varargs _io_output(LuaValue var1) {
      this.outfile = var1.isnil() ? this.output() : (var1.isstring() ? this.ioopenfile(3, var1.checkjstring(), "w") : checkfile(var1));
      return this.outfile;
   }

   public Varargs _io_type(LuaValue var1) {
      File var2 = optfile(var1);
      return var2 != null ? (var2.isclosed() ? CLOSED_FILE : FILE) : NIL;
   }

   public Varargs _io_popen(String var1, String var2) throws IOException {
      return this.openProgram(var1, var2);
   }

   public Varargs _io_open(String var1, String var2) throws IOException {
      return this.rawopenfile(3, var1, var2);
   }

   public Varargs _io_lines(String var1) {
      this.infile = var1 == null ? this.input() : this.ioopenfile(3, var1, "r");
      checkopen(this.infile);
      return this.lines(this.infile);
   }

   public Varargs _io_read(Varargs var1) throws IOException {
      checkopen(this.input());
      return this.ioread(this.infile, var1);
   }

   public Varargs _io_write(Varargs var1) throws IOException {
      checkopen(this.output());
      return iowrite(this.outfile, var1);
   }

   public Varargs _file_close(LuaValue var1) throws IOException {
      return ioclose(checkfile(var1));
   }

   public Varargs _file_flush(LuaValue var1) throws IOException {
      checkfile(var1).flush();
      return LuaValue.TRUE;
   }

   public Varargs _file_setvbuf(LuaValue var1, String var2, int var3) {
      checkfile(var1).setvbuf(var2, var3);
      return LuaValue.TRUE;
   }

   public Varargs _file_lines(LuaValue var1) {
      return this.lines(checkfile(var1));
   }

   public Varargs _file_read(LuaValue var1, Varargs var2) throws IOException {
      return this.ioread(checkfile(var1), var2);
   }

   public Varargs _file_seek(LuaValue var1, String var2, int var3) throws IOException {
      return valueOf(checkfile(var1).seek(var2, var3));
   }

   public Varargs _file_write(LuaValue var1, Varargs var2) throws IOException {
      return iowrite(checkfile(var1), var2);
   }

   public Varargs _io_index(LuaValue var1) {
      return (Varargs)(var1.equals(STDOUT) ? this.output() : (var1.equals(STDIN) ? this.input() : (var1.equals(STDERR) ? this.errput() : NIL)));
   }

   public Varargs _lines_iter(LuaValue var1) throws IOException {
      return freadline(checkfile(var1));
   }

   private File output() {
      return this.outfile != null ? this.outfile : (this.outfile = this.ioopenfile(1, "-", "w"));
   }

   private File errput() {
      return this.errfile != null ? this.errfile : (this.errfile = this.ioopenfile(2, "-", "w"));
   }

   private File ioopenfile(int var1, String var2, String var3) {
      try {
         return this.rawopenfile(var1, var2, var3);
      } catch (Exception var5) {
         error("io error: " + var5.getMessage());
         return null;
      }
   }

   private static Varargs ioclose(File var0) throws IOException {
      if (var0.isstdfile()) {
         return errorresult("cannot close standard file");
      } else {
         var0.close();
         return successresult();
      }
   }

   private static Varargs successresult() {
      return LuaValue.TRUE;
   }

   static Varargs errorresult(Exception var0) {
      String var1 = var0.getMessage();
      return errorresult("io error: " + (var1 != null ? var1 : var0.toString()));
   }

   private static Varargs errorresult(String var0) {
      return varargsOf(NIL, valueOf(var0));
   }

   private Varargs lines(File var1) {
      try {
         return new IoLibV(var1, "lnext", 19, this);
      } catch (Exception var3) {
         return error("lines: " + var3);
      }
   }

   private static Varargs iowrite(File var0, Varargs var1) throws IOException {
      int var2 = 1;

      for (int var3 = var1.narg(); var2 <= var3; var2++) {
         var0.write(var1.checkstring(var2));
      }

      return var0;
   }

   private Varargs ioread(File var1, Varargs var2) throws IOException {
      int var4 = var2.narg();
      LuaValue[] var5 = new LuaValue[var4];
      int var3 = 0;

      while (var3 < var4) {
         LuaValue var7;
         LuaValue var6;
         label32:
         switch ((var6 = var2.arg(var3 + 1)).type()) {
            case 3:
               var7 = freadbytes(var1, var6.toint());
               break;
            case 4:
               LuaString var8 = var6.checkstring();
               if (var8.m_length == 2 && var8.m_bytes[var8.m_offset] == 42) {
                  switch (var8.m_bytes[var8.m_offset + 1]) {
                     case 97:
                        var7 = freadall(var1);
                        break label32;
                     case 108:
                        var7 = freadline(var1);
                        break label32;
                     case 110:
                        var7 = freadnumber(var1);
                        break label32;
                  }
               }

               return argerror(var3 + 1, "(invalid format)");
            default:
               return argerror(var3 + 1, "(invalid format)");
         }

         if ((var5[var3++] = var7).isnil()) {
            break;
         }
      }

      return (Varargs)(var3 == 0 ? NIL : varargsOf(var5, 0, var3));
   }

   private static File checkfile(LuaValue var0) {
      File var1 = optfile(var0);
      if (var1 == null) {
         argerror(1, "file");
      }

      checkopen(var1);
      return var1;
   }

   private static File optfile(LuaValue var0) {
      return var0 instanceof File ? (File)var0 : null;
   }

   private static File checkopen(File var0) {
      if (var0.isclosed()) {
         error("attempt to use a closed file");
      }

      return var0;
   }

   private File rawopenfile(int var1, String var2, String var3) throws IOException {
      switch (var1) {
         case 0:
            return this.wrapStdin();
         case 1:
            return this.wrapStdout();
         case 2:
            return this.wrapStderr();
         default:
            boolean var4 = var3.startsWith("r");
            boolean var5 = var3.startsWith("a");
            boolean var6 = var3.indexOf(43) > 0;
            boolean var7 = var3.endsWith("b");
            return this.openFile(var2, var4, var5, var6, var7);
      }
   }

   public static LuaValue freadbytes(File var0, int var1) throws IOException {
      byte[] var2 = new byte[var1];
      int var3;
      return (LuaValue)((var3 = var0.read(var2, 0, var2.length)) < 0 ? NIL : LuaString.valueUsing(var2, 0, var3));
   }

   public static LuaValue freaduntil(File var0, boolean var1) throws IOException {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();

      int var3;
      try {
         if (var1) {
            while ((var3 = var0.read()) > 0) {
               switch (var3) {
                  case 10:
                     return (LuaValue)(var3 < 0 && var2.size() == 0 ? NIL : LuaString.valueUsing(var2.toByteArray()));
                  case 13:
                     break;
                  default:
                     var2.write(var3);
               }
            }
         } else {
            while ((var3 = var0.read()) > 0) {
               var2.write(var3);
            }
         }
      } catch (EOFException var5) {
         var3 = -1;
      }

      return (LuaValue)(var3 < 0 && var2.size() == 0 ? NIL : LuaString.valueUsing(var2.toByteArray()));
   }

   public static LuaValue freadline(File var0) throws IOException {
      return freaduntil(var0, true);
   }

   public static LuaValue freadall(File var0) throws IOException {
      int var1 = var0.remaining();
      return var1 >= 0 ? freadbytes(var0, var1) : freaduntil(var0, false);
   }

   public static LuaValue freadnumber(File var0) throws IOException {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      freadchars(var0, " \t\r\n", null);
      freadchars(var0, "-+", var1);
      freadchars(var0, "0123456789", var1);
      freadchars(var0, ".", var1);
      freadchars(var0, "0123456789", var1);
      String var2 = var1.toString();
      return (LuaValue)(var2.length() > 0 ? valueOf(Double.parseDouble(var2)) : NIL);
   }

   private static void freadchars(File var0, String var1, ByteArrayOutputStream var2) throws IOException {
      while (true) {
         int var3 = var0.peek();
         if (var1.indexOf(var3) < 0) {
            return;
         }

         var0.read();
         if (var2 != null) {
            var2.write(var3);
         }
      }
   }

   protected abstract class File extends LuaValue {
      public abstract void write(LuaString var1) throws IOException;

      public abstract void flush() throws IOException;

      public abstract boolean isstdfile();

      public abstract void close() throws IOException;

      public abstract boolean isclosed();

      public abstract int seek(String var1, int var2) throws IOException;

      public abstract void setvbuf(String var1, int var2);

      public abstract int remaining() throws IOException;

      public abstract int peek() throws IOException, EOFException;

      public abstract int read() throws IOException, EOFException;

      public abstract int read(byte[] var1, int var2, int var3) throws IOException;

      public LuaValue get(LuaValue var1) {
         return IoLib.this.filemethods.get(var1);
      }

      public int type() {
         return 7;
      }

      public String typename() {
         return "userdata";
      }

      public String tojstring() {
         return "file: " + Integer.toHexString(this.hashCode());
      }
   }

   static final class IoLibV extends VarArgFunction {
      private File f;
      public IoLib iolib;

      public IoLibV() {
      }

      public IoLibV(File var1, String var2, int var3, IoLib var4) {
         this.f = var1;
         this.name = var2;
         this.opcode = var3;
         this.iolib = var4;
      }

      public Varargs invoke(Varargs var1) {
         try {
            switch (this.opcode) {
               case 0:
                  return this.iolib._io_close(var1.arg1());
               case 1:
                  return this.iolib._io_flush();
               case 2:
                  return this.iolib._io_input(var1.arg1());
               case 3:
                  return this.iolib._io_lines(var1.isvalue(1) ? var1.checkjstring(1) : null);
               case 4:
                  return this.iolib._io_open(var1.checkjstring(1), var1.optjstring(2, "r"));
               case 5:
                  return this.iolib._io_output(var1.arg1());
               case 6:
                  return this.iolib._io_popen(var1.checkjstring(1), var1.optjstring(2, "r"));
               case 7:
                  return this.iolib._io_read(var1);
               case 8:
                  return this.iolib._io_tmpfile();
               case 9:
                  return this.iolib._io_type(var1.arg1());
               case 10:
                  return this.iolib._io_write(var1);
               case 11:
                  return this.iolib._file_close(var1.arg1());
               case 12:
                  return this.iolib._file_flush(var1.arg1());
               case 13:
                  return this.iolib._file_lines(var1.arg1());
               case 14:
                  return this.iolib._file_read(var1.arg1(), var1.subargs(2));
               case 15:
                  return this.iolib._file_seek(var1.arg1(), var1.optjstring(2, "cur"), var1.optint(3, 0));
               case 16:
                  return this.iolib._file_setvbuf(var1.arg1(), var1.checkjstring(2), var1.optint(3, 1024));
               case 17:
                  return this.iolib._file_write(var1.arg1(), var1.subargs(2));
               case 18:
                  return this.iolib._io_index(var1.arg(2));
               case 19:
                  return this.iolib._lines_iter(this.f);
            }
         } catch (IOException var3) {
            return IoLib.errorresult(var3);
         }

         return NONE;
      }
   }
}
