package dev.inteleonyx.armandillo.api.luaj.lib.jse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class JseProcess {
   final Process process;
   final Thread input;
   final Thread output;
   final Thread error;

   public JseProcess(String[] var1, InputStream var2, OutputStream var3, OutputStream var4) throws IOException {
      this(Runtime.getRuntime().exec(var1), var2, var3, var4);
   }

   public JseProcess(String var1, InputStream var2, OutputStream var3, OutputStream var4) throws IOException {
      this(Runtime.getRuntime().exec(var1), var2, var3, var4);
   }

   private JseProcess(Process var1, InputStream var2, OutputStream var3, OutputStream var4) {
      this.process = var1;
      this.input = var2 == null ? null : this.copyBytes(var2, var1.getOutputStream(), null, var1.getOutputStream());
      this.output = var3 == null ? null : this.copyBytes(var1.getInputStream(), var3, var1.getInputStream(), null);
      this.error = var4 == null ? null : this.copyBytes(var1.getErrorStream(), var4, var1.getErrorStream(), null);
   }

   public int exitValue() {
      return this.process.exitValue();
   }

   public int waitFor() throws InterruptedException {
      int var1 = this.process.waitFor();
      if (this.input != null) {
         this.input.join();
      }

      if (this.output != null) {
         this.output.join();
      }

      if (this.error != null) {
         this.error.join();
      }

      this.process.destroy();
      return var1;
   }

   private Thread copyBytes(InputStream var1, OutputStream var2, InputStream var3, OutputStream var4) {
      CopyThread var5 = new CopyThread(var2, var4, var3, var1);
      var5.start();
      return var5;
   }

   private static final class CopyThread extends Thread {
      private final OutputStream output;
      private final OutputStream ownedOutput;
      private final InputStream ownedInput;
      private final InputStream input;

      private CopyThread(OutputStream var1, OutputStream var2, InputStream var3, InputStream var4) {
         this.output = var1;
         this.ownedOutput = var2;
         this.ownedInput = var3;
         this.input = var4;
      }

      public void run() {
         try {
            byte[] var1 = new byte[1024];

            int var2;
            try {
               while ((var2 = this.input.read(var1)) >= 0) {
                  this.output.write(var1, 0, var2);
               }
            } finally {
               if (this.ownedInput != null) {
                  this.ownedInput.close();
               }

               if (this.ownedOutput != null) {
                  this.ownedOutput.close();
               }
            }
         } catch (IOException var7) {
            var7.printStackTrace();
         }
      }
   }
}
