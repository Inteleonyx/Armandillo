package dev.inteleonyx.armandillo.api.luaj.lib.jse;

import dev.inteleonyx.armandillo.api.luaj.Varargs;
import dev.inteleonyx.armandillo.api.luaj.lib.OsLib;

import java.io.File;
import java.io.IOException;

public class JseOsLib extends OsLib {
   public static final int EXEC_IOEXCEPTION = 1;
   public static final int EXEC_INTERRUPTED = -2;
   public static final int EXEC_ERROR = -3;

   protected String getenv(String var1) {
      String var2 = System.getenv(var1);
      return var2 != null ? var2 : System.getProperty(var1);
   }

   protected Varargs execute(String var1) {
      int var2;
      try {
         var2 = new JseProcess(var1, null, this.globals.STDOUT, this.globals.STDERR).waitFor();
      } catch (IOException var4) {
         var2 = 1;
      } catch (InterruptedException var5) {
         var2 = -2;
      } catch (Throwable var6) {
         var2 = -3;
      }

      return var2 == 0 ? varargsOf(TRUE, valueOf("exit"), ZERO) : varargsOf(NIL, valueOf("signal"), valueOf(var2));
   }

   protected void remove(String var1) throws IOException {
      File var2 = new File(var1);
      if (!var2.exists()) {
         throw new IOException("No such file or directory");
      } else if (!var2.delete()) {
         throw new IOException("Failed to delete");
      }
   }

   protected void rename(String var1, String var2) throws IOException {
      File var3 = new File(var1);
      if (!var3.exists()) {
         throw new IOException("No such file or directory");
      } else if (!var3.renameTo(new File(var2))) {
         throw new IOException("Failed to rename");
      }
   }

   protected String tmpname() {
      try {
         File var1 = File.createTempFile(".luaj", "tmp");
         return var1.getName();
      } catch (IOException var2) {
         return super.tmpname();
      }
   }
}
