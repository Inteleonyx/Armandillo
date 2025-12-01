package dev.inteleonyx.armandillo.api.luaj.lib.jse;

import dev.inteleonyx.armandillo.api.luaj.LuaError;
import dev.inteleonyx.armandillo.api.luaj.LuaUserdata;
import dev.inteleonyx.armandillo.api.luaj.LuaValue;

import java.lang.reflect.Field;

class JavaInstance extends LuaUserdata {
   JavaClass jclass;

   JavaInstance(Object var1) {
      super(var1);
   }

   public LuaValue get(LuaValue var1) {
      if (this.jclass == null) {
         this.jclass = JavaClass.forClass(this.m_instance.getClass());
      }

      Field var2 = this.jclass.getField(var1);
      if (var2 != null) {
         try {
            return CoerceJavaToLua.coerce(var2.get(this.m_instance));
         } catch (Exception var5) {
            throw new LuaError(var5);
         }
      } else {
         LuaValue var3 = this.jclass.getMethod(var1);
         if (var3 != null) {
            return var3;
         } else {
            Class var4 = this.jclass.getInnerClass(var1);
            return (LuaValue)(var4 != null ? JavaClass.forClass(var4) : super.get(var1));
         }
      }
   }

   public void set(LuaValue var1, LuaValue var2) {
      if (this.jclass == null) {
         this.jclass = JavaClass.forClass(this.m_instance.getClass());
      }

      Field var3 = this.jclass.getField(var1);
      if (var3 != null) {
         try {
            var3.set(this.m_instance, CoerceLuaToJava.coerce(var2, var3.getType()));
         } catch (Exception var5) {
            throw new LuaError(var5);
         }
      } else {
         super.set(var1, var2);
      }
   }
}
