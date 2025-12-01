package dev.inteleonyx.armandillo.api.luaj.ast;

import dev.inteleonyx.armandillo.api.luaj.LuaValue;

import java.util.List;

public class NameResolver extends Visitor {
   private NameScope scope = null;

   private void pushScope() {
      this.scope = new NameScope(this.scope);
   }

   private void popScope() {
      this.scope = this.scope.outerScope;
   }

   public void visit(NameScope var1) {
   }

   public void visit(Block var1) {
      this.pushScope();
      var1.scope = this.scope;
      super.visit(var1);
      this.popScope();
   }

   public void visit(FuncBody var1) {
      this.pushScope();
      this.scope.functionNestingCount++;
      var1.scope = this.scope;
      super.visit(var1);
      this.popScope();
   }

   public void visit(Stat.LocalFuncDef var1) {
      this.defineLocalVar(var1.name);
      super.visit(var1);
   }

   public void visit(Stat.NumericFor var1) {
      this.pushScope();
      var1.scope = this.scope;
      this.defineLocalVar(var1.name);
      super.visit(var1);
      this.popScope();
   }

   public void visit(Stat.GenericFor var1) {
      this.pushScope();
      var1.scope = this.scope;
      this.defineLocalVars(var1.names);
      super.visit(var1);
      this.popScope();
   }

   public void visit(Exp.NameExp var1) {
      var1.name.variable = this.resolveNameReference(var1.name);
      super.visit(var1);
   }

   public void visit(Stat.FuncDef var1) {
      var1.name.name.variable = this.resolveNameReference(var1.name.name);
      var1.name.name.variable.hasassignments = true;
      super.visit(var1);
   }

   public void visit(Stat.Assign var1) {
      super.visit(var1);
      int var2 = 0;

      for (int var3 = var1.vars.size(); var2 < var3; var2++) {
         Exp.VarExp var4 = (Exp.VarExp)var1.vars.get(var2);
         var4.markHasAssignment();
      }
   }

   public void visit(Stat.LocalAssign var1) {
      this.visitExps(var1.values);
      this.defineLocalVars(var1.names);
      int var2 = var1.names.size();
      int var3 = var1.values != null ? var1.values.size() : 0;
      boolean var4 = var3 > 0 && var3 < var2 && ((Exp)var1.values.get(var3 - 1)).isvarargexp();

      for (int var5 = 0; var5 < var2 && var5 < (var4 ? var3 - 1 : var3); var5++) {
         if (var1.values.get(var5) instanceof Exp.Constant) {
            ((Name)var1.names.get(var5)).variable.initialValue = ((Exp.Constant)var1.values.get(var5)).value;
         }
      }

      if (!var4) {
         for (int var6 = var3; var6 < var2; var6++) {
            ((Name)var1.names.get(var6)).variable.initialValue = LuaValue.NIL;
         }
      }
   }

   public void visit(ParList var1) {
      if (var1.names != null) {
         this.defineLocalVars(var1.names);
      }

      if (var1.isvararg) {
         this.scope.define("arg");
      }

      super.visit(var1);
   }

   protected void defineLocalVars(List var1) {
      int var2 = 0;

      for (int var3 = var1.size(); var2 < var3; var2++) {
         this.defineLocalVar((Name)var1.get(var2));
      }
   }

   protected void defineLocalVar(Name var1) {
      var1.variable = this.scope.define(var1.name);
   }

   protected Variable resolveNameReference(Name var1) {
      Variable var2 = this.scope.find(var1.name);
      if (var2.isLocal() && this.scope.functionNestingCount != var2.definingScope.functionNestingCount) {
         var2.isupvalue = true;
      }

      return var2;
   }
}
