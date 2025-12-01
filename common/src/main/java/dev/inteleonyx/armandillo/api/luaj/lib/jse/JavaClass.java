package dev.inteleonyx.armandillo.api.luaj.lib.jse;

import dev.inteleonyx.armandillo.api.luaj.LuaValue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.Map.Entry;

class JavaClass extends JavaInstance implements CoerceJavaToLua.Coercion {
   // Usando Map com tipos genéricos
   static final Map<Class<?>, JavaClass> classes = Collections.synchronizedMap(new HashMap<>());
   static final LuaValue NEW = LuaValue.valueOf("new");

   Map<LuaValue, Field> fields;
   Map<LuaValue, LuaValue> methods;
   Map<LuaValue, Class<?>> innerclasses;

   static JavaClass forClass(Class<?> clazz) {
      JavaClass javaClass = classes.get(clazz);
      if (javaClass == null) {
         javaClass = new JavaClass(clazz);
         classes.put(clazz, javaClass);
      }
      return javaClass;
   }

   JavaClass(Class<?> clazz) {
      super(clazz);
      this.jclass = this;
   }

   @Override
   public LuaValue coerce(Object obj) {
      return this;
   }

   Field getField(LuaValue key) {
      if (this.fields == null) {
         Map<LuaValue, Field> fieldMap = new HashMap<>();
         Field[] fieldsArray = ((Class<?>) this.m_instance).getFields();

         for (Field field : fieldsArray) {
            if (Modifier.isPublic(field.getModifiers())) {
               fieldMap.put(LuaValue.valueOf(field.getName()), field);
               try {
                  if (!field.isAccessible()) {
                     field.setAccessible(true);
                  }
               } catch (SecurityException ignored) {
               }
            }
         }

         this.fields = fieldMap;
      }

      return this.fields.get(key);
   }

   LuaValue getMethod(LuaValue key) {
      if (this.methods == null) {
         Map<String, List<JavaMethod>> methodMap = new HashMap<>();
         Method[] methodsArray = ((Class<?>) this.m_instance).getMethods();

         for (Method method : methodsArray) {
            if (Modifier.isPublic(method.getModifiers())) {
               String name = method.getName();
               List<JavaMethod> list = methodMap.get(name);
               if (list == null) {
                  list = new ArrayList<>();
                  methodMap.put(name, list);
               }
               list.add(JavaMethod.forMethod(method));
            }
         }

         Map<LuaValue, LuaValue> luaMethodMap = new HashMap<>();
         Constructor<?>[] constructors = ((Class<?>) this.m_instance).getConstructors();
         List<JavaConstructor> constructorList = new ArrayList<>();

         for (Constructor<?> constructor : constructors) {
            if (Modifier.isPublic(constructor.getModifiers())) {
               constructorList.add(JavaConstructor.forConstructor(constructor));
            }
         }

         switch (constructorList.size()) {
            case 0:
               break;
            case 1:
               luaMethodMap.put(NEW, constructorList.get(0));
               break;
            default:
               luaMethodMap.put(NEW, JavaConstructor.forConstructors(constructorList.toArray(new JavaConstructor[0])));
               break;
         }

         for (Entry<String, List<JavaMethod>> entry : methodMap.entrySet()) {
            String name = entry.getKey();
            List<JavaMethod> methodsList = entry.getValue();
            LuaValue keyLua = LuaValue.valueOf(name);
            LuaValue valueLua = (methodsList.size() == 1)
                    ? methodsList.get(0)
                    : JavaMethod.forMethods(methodsList.toArray(new JavaMethod[0]));
            luaMethodMap.put(keyLua, valueLua);
         }

         this.methods = luaMethodMap;
      }

      return this.methods.get(key);
   }

   Class<?> getInnerClass(LuaValue key) {
      if (this.innerclasses == null) {
         Map<LuaValue, Class<?>> innerClassMap = new HashMap<>();
         Class<?>[] classesArray = ((Class<?>) this.m_instance).getClasses();

         for (Class<?> clazz : classesArray) {
            String fullName = clazz.getName();
            String simpleName = fullName.substring(Math.max(fullName.lastIndexOf('$'), fullName.lastIndexOf('.')) + 1);
            innerClassMap.put(LuaValue.valueOf(simpleName), clazz);
         }

         this.innerclasses = innerClassMap;
      }

      return this.innerclasses.get(key);
   }

   public LuaValue getConstructor() {
      return this.getMethod(NEW);
   }
}