package dev.inteleonyx.armandillo.api.luaj.lib.jse;

import dev.inteleonyx.armandillo.api.luaj.LuaError;
import dev.inteleonyx.armandillo.api.luaj.LuaTable;
import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.Varargs;
import dev.inteleonyx.armandillo.api.luaj.lib.VarArgFunction;

import java.lang.reflect.*;

public class LuajavaLib extends VarArgFunction {

   // Opcodes para controle interno
   static final int INIT = 0;
   static final int BINDCLASS = 1;
   static final int NEWINSTANCE = 2;
   static final int NEW = 3;
   static final int CREATEPROXY = 4;
   static final int LOADLIB = 5;

   static final String[] NAMES = new String[]{"bindClass", "newInstance", "new", "createProxy", "loadLib"};

   static final int METHOD_MODIFIERS_VARARGS = 128;

   @Override
   public Varargs invoke(Varargs args) {
      try {
         switch (this.opcode) {
            case INIT: {
               LuaValue env = args.arg(2);
               LuaTable lib = new LuaTable();
               this.bind(lib, this.getClass(), NAMES, 1);
               env.set("luajava", lib);

               // Registrar no package.loaded, se existir
               LuaValue packageTable = env.get("package");
               if (!packageTable.isnil()) {
                  packageTable.get("loaded").set("luajava", lib);
               }

               return lib;
            }
            case BINDCLASS: {
               String className = args.checkjstring(1);
               Class<?> clazz = this.classForName(className);
               return JavaClass.forClass(clazz);
            }
            case NEWINSTANCE:
            case NEW: {
               LuaValue val = args.checkvalue(1);
               Class<?> clazz;
               if (this.opcode == NEWINSTANCE) {
                  // Espera o nome da classe
                  clazz = this.classForName(val.tojstring());
               } else {
                  // Espera um userdata Class
                  clazz = (Class<?>) val.checkuserdata(Class.class);
               }
               Varargs ctorArgs = args.subargs(2);
               return JavaClass.forClass(clazz).getConstructor().invoke(ctorArgs);
            }
            case CREATEPROXY: {
               int n = args.narg() - 1;
               if (n <= 0) {
                  throw new LuaError("no interfaces");
               }
               LuaTable handlerTable = args.checktable(n + 1);
               Class<?>[] interfaces = new Class<?>[n];
               for (int i = 0; i < n; i++) {
                  interfaces[i] = this.classForName(args.checkjstring(i + 1));
               }
               ProxyInvocationHandler handler = new ProxyInvocationHandler(handlerTable);
               Object proxy = Proxy.newProxyInstance(this.getClass().getClassLoader(), interfaces, handler);
               return LuaValue.userdataOf(proxy);
            }
            case LOADLIB: {
               String className = args.checkjstring(1);
               String methodName = args.checkjstring(2);
               Class<?> clazz = this.classForName(className);
               Method method = clazz.getMethod(methodName);
               Object result = method.invoke(null);
               if (result instanceof LuaValue) {
                  return (LuaValue) result;
               }
               return NIL;
            }
            default:
               throw new LuaError("not yet supported: " + this);
         }
      } catch (LuaError e) {
         throw e;
      } catch (InvocationTargetException e) {
         throw new LuaError(e.getTargetException());
      } catch (Exception e) {
         throw new LuaError(e);
      }
   }

   protected Class<?> classForName(String className) throws ClassNotFoundException {
      return Class.forName(className, true, ClassLoader.getSystemClassLoader());
   }

   private static final class ProxyInvocationHandler implements InvocationHandler {
      private final LuaValue luaObject;

      private ProxyInvocationHandler(LuaValue luaObject) {
         this.luaObject = luaObject;
      }

      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
         String methodName = method.getName();
         LuaValue luaMethod = luaObject.get(methodName);
         if (luaMethod.isnil()) {
            return null; // Método não definido no Lua
         }

         boolean isVarArgs = (method.getModifiers() & METHOD_MODIFIERS_VARARGS) != 0;
         int argsLength = args != null ? args.length : 0;
         LuaValue[] luaArgs;

         if (isVarArgs) {
            // Último argumento é array varargs
            Object varArgArray = args[--argsLength];
            int varArgLen = Array.getLength(varArgArray);
            luaArgs = new LuaValue[argsLength + varArgLen];

            for (int i = 0; i < argsLength; i++) {
               luaArgs[i] = CoerceJavaToLua.coerce(args[i]);
            }
            for (int i = 0; i < varArgLen; i++) {
               luaArgs[argsLength + i] = CoerceJavaToLua.coerce(Array.get(varArgArray, i));
            }
         } else {
            luaArgs = new LuaValue[argsLength];
            for (int i = 0; i < argsLength; i++) {
               luaArgs[i] = CoerceJavaToLua.coerce(args[i]);
            }
         }

         LuaValue result = luaMethod.invoke(luaArgs).arg1();
         return CoerceLuaToJava.coerce(result, method.getReturnType());
      }
   }
}
