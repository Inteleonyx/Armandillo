package dev.inteleonyx.armandillo.api.luaj.ast;

import java.util.List;

public abstract class Visitor {
   public void visit(Chunk var1) {
      var1.block.accept(this);
   }

   public void visit(Block var1) {
      this.visit(var1.scope);
      if (var1.stats != null) {
         int var2 = 0;

         for (int var3 = var1.stats.size(); var2 < var3; var2++) {
            ((Stat)var1.stats.get(var2)).accept(this);
         }
      }
   }

   public void visit(Stat.Assign var1) {
      this.visitVars(var1.vars);
      this.visitExps(var1.exps);
   }

   public void visit(Stat.Break var1) {
   }

   public void visit(Stat.FuncCallStat var1) {
      var1.funccall.accept(this);
   }

   public void visit(Stat.FuncDef var1) {
      var1.body.accept(this);
   }

   public void visit(Stat.GenericFor var1) {
      this.visit(var1.scope);
      this.visitNames(var1.names);
      this.visitExps(var1.exps);
      var1.block.accept(this);
   }

   public void visit(Stat.IfThenElse var1) {
      var1.ifexp.accept(this);
      var1.ifblock.accept(this);
      if (var1.elseifblocks != null) {
         int var2 = 0;

         for (int var3 = var1.elseifblocks.size(); var2 < var3; var2++) {
            ((Exp)var1.elseifexps.get(var2)).accept(this);
            ((Block)var1.elseifblocks.get(var2)).accept(this);
         }
      }

      if (var1.elseblock != null) {
         this.visit(var1.elseblock);
      }
   }

   public void visit(Stat.LocalAssign var1) {
      this.visitNames(var1.names);
      this.visitExps(var1.values);
   }

   public void visit(Stat.LocalFuncDef var1) {
      this.visit(var1.name);
      var1.body.accept(this);
   }

   public void visit(Stat.NumericFor var1) {
      this.visit(var1.scope);
      this.visit(var1.name);
      var1.initial.accept(this);
      var1.limit.accept(this);
      if (var1.step != null) {
         var1.step.accept(this);
      }

      var1.block.accept(this);
   }

   public void visit(Stat.RepeatUntil var1) {
      var1.block.accept(this);
      var1.exp.accept(this);
   }

   public void visit(Stat.Return var1) {
      this.visitExps(var1.values);
   }

   public void visit(Stat.WhileDo var1) {
      var1.exp.accept(this);
      var1.block.accept(this);
   }

   public void visit(FuncBody var1) {
      this.visit(var1.scope);
      var1.parlist.accept(this);
      var1.block.accept(this);
   }

   public void visit(FuncArgs var1) {
      this.visitExps(var1.exps);
   }

   public void visit(TableField var1) {
      if (var1.name != null) {
         this.visit(var1.name);
      }

      if (var1.index != null) {
         var1.index.accept(this);
      }

      var1.rhs.accept(this);
   }

   public void visit(Exp.AnonFuncDef var1) {
      var1.body.accept(this);
   }

   public void visit(Exp.BinopExp var1) {
      var1.lhs.accept(this);
      var1.rhs.accept(this);
   }

   public void visit(Exp.Constant var1) {
   }

   public void visit(Exp.FieldExp var1) {
      var1.lhs.accept(this);
      this.visit(var1.name);
   }

   public void visit(Exp.FuncCall var1) {
      var1.lhs.accept(this);
      var1.args.accept(this);
   }

   public void visit(Exp.IndexExp var1) {
      var1.lhs.accept(this);
      var1.exp.accept(this);
   }

   public void visit(Exp.MethodCall var1) {
      var1.lhs.accept(this);
      this.visit(var1.name);
      var1.args.accept(this);
   }

   public void visit(Exp.NameExp var1) {
      this.visit(var1.name);
   }

   public void visit(Exp.ParensExp var1) {
      var1.exp.accept(this);
   }

   public void visit(Exp.UnopExp var1) {
      var1.rhs.accept(this);
   }

   public void visit(Exp.VarargsExp var1) {
   }

   public void visit(ParList var1) {
      this.visitNames(var1.names);
   }

   public void visit(TableConstructor var1) {
      if (var1.fields != null) {
         int var2 = 0;

         for (int var3 = var1.fields.size(); var2 < var3; var2++) {
            ((TableField)var1.fields.get(var2)).accept(this);
         }
      }
   }

   public void visitVars(List var1) {
      if (var1 != null) {
         int var2 = 0;

         for (int var3 = var1.size(); var2 < var3; var2++) {
            ((Exp.VarExp)var1.get(var2)).accept(this);
         }
      }
   }

   public void visitExps(List var1) {
      if (var1 != null) {
         int var2 = 0;

         for (int var3 = var1.size(); var2 < var3; var2++) {
            ((Exp)var1.get(var2)).accept(this);
         }
      }
   }

   public void visitNames(List var1) {
      if (var1 != null) {
         int var2 = 0;

         for (int var3 = var1.size(); var2 < var3; var2++) {
            this.visit((Name)var1.get(var2));
         }
      }
   }

   public void visit(Name var1) {
   }

   public void visit(String var1) {
   }

   public void visit(NameScope var1) {
   }

   public void visit(Stat.Goto var1) {
   }

   public void visit(Stat.Label var1) {
   }
}
