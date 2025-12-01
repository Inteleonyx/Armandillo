package dev.inteleonyx.armandillo.api.luaj.ast;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NameScope {
   private static final Set LUA_KEYWORDS = new HashSet();
   public final Map namedVariables = new HashMap();
   public final NameScope outerScope;
   public int functionNestingCount;

   public NameScope() {
      this.outerScope = null;
      this.functionNestingCount = 0;
   }

   public NameScope(NameScope var1) {
      this.outerScope = var1;
      this.functionNestingCount = var1 != null ? var1.functionNestingCount : 0;
   }

   public Variable find(String var1) throws IllegalArgumentException {
      this.validateIsNotKeyword(var1);

      for (NameScope var2 = this; var2 != null; var2 = var2.outerScope) {
         if (var2.namedVariables.containsKey(var1)) {
            return (Variable)var2.namedVariables.get(var1);
         }
      }

      Variable var3 = new Variable(var1);
      this.namedVariables.put(var1, var3);
      return var3;
   }

   public Variable define(String var1) throws IllegalStateException, IllegalArgumentException {
      this.validateIsNotKeyword(var1);
      Variable var2 = new Variable(var1, this);
      this.namedVariables.put(var1, var2);
      return var2;
   }

   private void validateIsNotKeyword(String var1) {
      if (LUA_KEYWORDS.contains(var1)) {
         throw new IllegalArgumentException("name is a keyword: '" + var1 + "'");
      }
   }

   static {
      String[] var0 = new String[]{
         "and",
         "break",
         "do",
         "else",
         "elseif",
         "end",
         "false",
         "for",
         "function",
         "if",
         "in",
         "local",
         "nil",
         "not",
         "or",
         "repeat",
         "return",
         "then",
         "true",
         "until",
         "while"
      };

      for (int var1 = 0; var1 < var0.length; var1++) {
         LUA_KEYWORDS.add(var0[var1]);
      }
   }
}
