package dev.inteleonyx.armandillo.api.luaj.ast;

import dev.inteleonyx.armandillo.api.luaj.LuaString;

import java.util.ArrayList;
import java.util.List;

public class FuncArgs extends SyntaxElement {
   public final List exps;

   public static FuncArgs explist(List var0) {
      return new FuncArgs(var0);
   }

   public static FuncArgs tableconstructor(TableConstructor var0) {
      return new FuncArgs(var0);
   }

   public static FuncArgs string(LuaString var0) {
      return new FuncArgs(var0);
   }

   public FuncArgs(List var1) {
      this.exps = var1;
   }

   public FuncArgs(LuaString var1) {
      this.exps = new ArrayList();
      this.exps.add(Exp.constant(var1));
   }

   public FuncArgs(TableConstructor var1) {
      this.exps = new ArrayList();
      this.exps.add(var1);
   }

   public void accept(Visitor var1) {
      var1.visit(this);
   }
}
