package dev.inteleonyx.armandillo.api.luaj.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class LuajClassLoader extends ClassLoader {
   static final String luajPackageRoot = "dev.inteleonyx.luaj.vm2.";
   static final String launcherInterfaceRoot = Launcher.class.getName();
   Map<String, Class<?>> classes = new HashMap<>();

   public static Launcher NewLauncher() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      return NewLauncher(DefaultLauncher.class);
   }

   public static Launcher NewLauncher(Class<? extends Launcher> var0) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      LuajClassLoader var1 = new LuajClassLoader();
      Object var2 = var1.loadAsUserClass(var0.getName()).newInstance();
      return (Launcher)var2;
   }

   public static boolean isUserClass(String var0) {
      return var0.startsWith("dev.inteleonyx.luaj.vm2.") && !var0.startsWith(launcherInterfaceRoot);
   }

   @Override
   public Class<?> loadClass(String var1) throws ClassNotFoundException {
      if (this.classes.containsKey(var1)) {
         return this.classes.get(var1);
      } else {
         return !isUserClass(var1) ? super.findSystemClass(var1) : this.loadAsUserClass(var1);
      }
   }

   private Class<?> loadAsUserClass(String var1) throws ClassNotFoundException {
      String var2 = var1.replace('.', '/').concat(".class");
      InputStream var3 = this.getResourceAsStream(var2);
      if (var3 != null) {
         try {
            ByteArrayOutputStream var4 = new ByteArrayOutputStream();
            byte[] var5 = new byte[1024];
            int var6 = 0;

            while ((var6 = var3.read(var5)) >= 0) {
               var4.write(var5, 0, var6);
            }

            byte[] var10 = var4.toByteArray();
            Class var7 = super.defineClass(var1, var10, 0, var10.length);
            this.classes.put(var1, var7);
            return var7;
         } catch (IOException var8) {
            throw new ClassNotFoundException("Read failed: " + var1 + ": " + var8);
         }
      } else {
         throw new ClassNotFoundException("Not found: " + var1);
      }
   }
}
