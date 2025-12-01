package dev.inteleonyx.armandillo.api.luaj.script;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import java.util.Arrays;
import java.util.List;

public class LuaScriptEngineFactory implements ScriptEngineFactory {
   private static final String[] EXTENSIONS = new String[]{"lua", ".lua"};
   private static final String[] MIMETYPES = new String[]{"text/lua", "application/lua"};
   private static final String[] NAMES = new String[]{"lua", "luaj"};
   private List extensions = Arrays.asList(EXTENSIONS);
   private List mimeTypes = Arrays.asList(MIMETYPES);
   private List names = Arrays.asList(NAMES);

   @Override
   public String getEngineName() {
      return this.getScriptEngine().get("javax.script.engine").toString();
   }

   @Override
   public String getEngineVersion() {
      return this.getScriptEngine().get("javax.script.engine_version").toString();
   }

   @Override
   public List getExtensions() {
      return this.extensions;
   }

   @Override
   public List getMimeTypes() {
      return this.mimeTypes;
   }

   @Override
   public List getNames() {
      return this.names;
   }

   @Override
   public String getLanguageName() {
      return this.getScriptEngine().get("javax.script.language").toString();
   }

   @Override
   public String getLanguageVersion() {
      return this.getScriptEngine().get("javax.script.language_version").toString();
   }

   @Override
   public Object getParameter(String var1) {
      return this.getScriptEngine().get(var1).toString();
   }

   @Override
   public String getMethodCallSyntax(String var1, String var2, String... var3) {
      StringBuffer var4 = new StringBuffer();
      var4.append(var1 + ":" + var2 + "(");
      int var5 = var3.length;

      for (int var6 = 0; var6 < var5; var6++) {
         if (var6 > 0) {
            var4.append(',');
         }

         var4.append(var3[var6]);
      }

      var4.append(")");
      return var4.toString();
   }

   @Override
   public String getOutputStatement(String var1) {
      return "print(" + var1 + ")";
   }

   @Override
   public String getProgram(String... var1) {
      StringBuffer var2 = new StringBuffer();
      int var3 = var1.length;

      for (int var4 = 0; var4 < var3; var4++) {
         if (var4 > 0) {
            var2.append('\n');
         }

         var2.append(var1[var4]);
      }

      return var2.toString();
   }

   @Override
   public ScriptEngine getScriptEngine() {
      return new LuaScriptEngine();
   }
}
