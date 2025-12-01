package dev.inteleonyx.armandillo.api.luaj.ast;

import java.util.List;

public abstract class Stat extends SyntaxElement {
   public abstract void accept(Visitor var1);

   public static Stat block(Block var0) {
      return var0;
   }

   public static Stat whiledo(Exp var0, Block var1) {
      return new WhileDo(var0, var1);
   }

   public static Stat repeatuntil(Block var0, Exp var1) {
      return new RepeatUntil(var0, var1);
   }

   public static Stat breakstat() {
      return new Break();
   }

   public static Stat returnstat(List var0) {
      return new Return(var0);
   }

   public static Stat assignment(List var0, List var1) {
      return new Assign(var0, var1);
   }

   public static Stat functioncall(Exp.FuncCall var0) {
      return new FuncCallStat(var0);
   }

   public static Stat localfunctiondef(String var0, FuncBody var1) {
      return new LocalFuncDef(var0, var1);
   }

   public static Stat fornumeric(String var0, Exp var1, Exp var2, Exp var3, Block var4) {
      return new NumericFor(var0, var1, var2, var3, var4);
   }

   public static Stat functiondef(FuncName var0, FuncBody var1) {
      return new FuncDef(var0, var1);
   }

   public static Stat forgeneric(List var0, List var1, Block var2) {
      return new GenericFor(var0, var1, var2);
   }

   public static Stat localassignment(List var0, List var1) {
      return new LocalAssign(var0, var1);
   }

   public static Stat ifthenelse(Exp var0, Block var1, List var2, List var3, Block var4) {
      return new IfThenElse(var0, var1, var2, var3, var4);
   }

   public static Stat gotostat(String var0) {
      return new Goto(var0);
   }

   public static Stat labelstat(String var0) {
      return new Label(var0);
   }

   public static class Assign extends Stat {
      public final List vars;
      public final List exps;

      public Assign(List var1, List var2) {
         this.vars = var1;
         this.exps = var2;
      }

      public void accept(Visitor var1) {
         var1.visit(this);
      }
   }

   public static class Break extends Stat {
      public void accept(Visitor var1) {
         var1.visit(this);
      }
   }

   public static class FuncCallStat extends Stat {
      public final Exp.FuncCall funccall;

      public FuncCallStat(Exp.FuncCall var1) {
         this.funccall = var1;
      }

      public void accept(Visitor var1) {
         var1.visit(this);
      }
   }

   public static class FuncDef extends Stat {
      public final FuncName name;
      public final FuncBody body;

      public FuncDef(FuncName var1, FuncBody var2) {
         this.name = var1;
         this.body = var2;
      }

      public void accept(Visitor var1) {
         var1.visit(this);
      }
   }

   public static class GenericFor extends Stat {
      public List names;
      public List exps;
      public Block block;
      public NameScope scope;

      public GenericFor(List var1, List var2, Block var3) {
         this.names = var1;
         this.exps = var2;
         this.block = var3;
      }

      public void accept(Visitor var1) {
         var1.visit(this);
      }
   }

   public static class Goto extends Stat {
      public final String name;

      public Goto(String var1) {
         this.name = var1;
      }

      public void accept(Visitor var1) {
         var1.visit(this);
      }
   }

   public static class IfThenElse extends Stat {
      public final Exp ifexp;
      public final Block ifblock;
      public final List elseifexps;
      public final List elseifblocks;
      public final Block elseblock;

      public IfThenElse(Exp var1, Block var2, List var3, List var4, Block var5) {
         this.ifexp = var1;
         this.ifblock = var2;
         this.elseifexps = var3;
         this.elseifblocks = var4;
         this.elseblock = var5;
      }

      public void accept(Visitor var1) {
         var1.visit(this);
      }
   }

   public static class Label extends Stat {
      public final String name;

      public Label(String var1) {
         this.name = var1;
      }

      public void accept(Visitor var1) {
         var1.visit(this);
      }
   }

   public static class LocalAssign extends Stat {
      public final List names;
      public final List values;

      public LocalAssign(List var1, List var2) {
         this.names = var1;
         this.values = var2;
      }

      public void accept(Visitor var1) {
         var1.visit(this);
      }
   }

   public static class LocalFuncDef extends Stat {
      public final Name name;
      public final FuncBody body;

      public LocalFuncDef(String var1, FuncBody var2) {
         this.name = new Name(var1);
         this.body = var2;
      }

      public void accept(Visitor var1) {
         var1.visit(this);
      }
   }

   public static class NumericFor extends Stat {
      public final Name name;
      public final Exp initial;
      public final Exp limit;
      public final Exp step;
      public final Block block;
      public NameScope scope;

      public NumericFor(String var1, Exp var2, Exp var3, Exp var4, Block var5) {
         this.name = new Name(var1);
         this.initial = var2;
         this.limit = var3;
         this.step = var4;
         this.block = var5;
      }

      public void accept(Visitor var1) {
         var1.visit(this);
      }
   }

   public static class RepeatUntil extends Stat {
      public final Block block;
      public final Exp exp;

      public RepeatUntil(Block var1, Exp var2) {
         this.block = var1;
         this.exp = var2;
      }

      public void accept(Visitor var1) {
         var1.visit(this);
      }
   }

   public static class Return extends Stat {
      public final List values;

      public Return(List var1) {
         this.values = var1;
      }

      public void accept(Visitor var1) {
         var1.visit(this);
      }

      public int nreturns() {
         int var1 = this.values != null ? this.values.size() : 0;
         if (var1 > 0 && ((Exp)this.values.get(var1 - 1)).isvarargexp()) {
            var1 = -1;
         }

         return var1;
      }
   }

   public static class WhileDo extends Stat {
      public final Exp exp;
      public final Block block;

      public WhileDo(Exp var1, Block var2) {
         this.exp = var1;
         this.block = var2;
      }

      public void accept(Visitor var1) {
         var1.visit(this);
      }
   }
}
