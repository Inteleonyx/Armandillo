package dev.inteleonyx.armandillo.api.luaj.ast;

import dev.inteleonyx.armandillo.api.luaj.LuaValue;

public abstract class Exp extends SyntaxElement {
   public abstract void accept(Visitor var1);

   public static Exp constant(LuaValue var0) {
      return new Constant(var0);
   }

   public static Exp numberconstant(String var0) {
      return new Constant(LuaValue.valueOf(var0).tonumber());
   }

   public static Exp varargs() {
      return new VarargsExp();
   }

   public static Exp tableconstructor(TableConstructor var0) {
      return var0;
   }

   public static Exp unaryexp(int var0, Exp var1) {
      if (var1 instanceof BinopExp) {
         BinopExp var2 = (BinopExp)var1;
         if (precedence(var0) > precedence(var2.op)) {
            return binaryexp(unaryexp(var0, var2.lhs), var2.op, var2.rhs);
         }
      }

      return new UnopExp(var0, var1);
   }

   public static Exp binaryexp(Exp var0, int var1, Exp var2) {
      if (var0 instanceof UnopExp) {
         UnopExp var3 = (UnopExp)var0;
         if (precedence(var1) > precedence(var3.op)) {
            return unaryexp(var3.op, binaryexp(var3.rhs, var1, var2));
         }
      }

      if (var0 instanceof BinopExp) {
         BinopExp var4 = (BinopExp)var0;
         if (precedence(var1) > precedence(var4.op) || precedence(var1) == precedence(var4.op) && isrightassoc(var1)) {
            return binaryexp(var4.lhs, var4.op, binaryexp(var4.rhs, var1, var2));
         }
      }

      if (var2 instanceof BinopExp) {
         BinopExp var5 = (BinopExp)var2;
         if (precedence(var1) > precedence(var5.op) || precedence(var1) == precedence(var5.op) && !isrightassoc(var1)) {
            return binaryexp(binaryexp(var0, var1, var5.lhs), var5.op, var5.rhs);
         }
      }

      return new BinopExp(var0, var1, var2);
   }

   static boolean isrightassoc(int var0) {
      switch (var0) {
         case 18:
         case 22:
            return true;
         default:
            return false;
      }
   }

   static int precedence(int var0) {
      switch (var0) {
         case 13:
         case 14:
            return 4;
         case 15:
         case 16:
         case 17:
            return 5;
         case 18:
            return 7;
         case 19:
         case 20:
         case 21:
            return 6;
         case 22:
            return 3;
         case 23:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 40:
         case 41:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         default:
            throw new IllegalStateException("precedence of bad op " + var0);
         case 24:
         case 25:
         case 26:
         case 61:
         case 62:
         case 63:
            return 2;
         case 59:
            return 0;
         case 60:
            return 1;
      }
   }

   public static Exp anonymousfunction(FuncBody var0) {
      return new AnonFuncDef(var0);
   }

   public static NameExp nameprefix(String var0) {
      return new NameExp(var0);
   }

   public static ParensExp parensprefix(Exp var0) {
      return new ParensExp(var0);
   }

   public static IndexExp indexop(PrimaryExp var0, Exp var1) {
      return new IndexExp(var0, var1);
   }

   public static FieldExp fieldop(PrimaryExp var0, String var1) {
      return new FieldExp(var0, var1);
   }

   public static FuncCall functionop(PrimaryExp var0, FuncArgs var1) {
      return new FuncCall(var0, var1);
   }

   public static MethodCall methodop(PrimaryExp var0, String var1, FuncArgs var2) {
      return new MethodCall(var0, var1, var2);
   }

   public boolean isvarexp() {
      return false;
   }

   public boolean isfunccall() {
      return false;
   }

   public boolean isvarargexp() {
      return false;
   }

   public static class AnonFuncDef extends Exp {
      public final FuncBody body;

      public AnonFuncDef(FuncBody var1) {
         this.body = var1;
      }

      public void accept(Visitor var1) {
         var1.visit(this);
      }
   }

   public static class BinopExp extends Exp {
      public final Exp lhs;
      public final Exp rhs;
      public final int op;

      public BinopExp(Exp var1, int var2, Exp var3) {
         this.lhs = var1;
         this.op = var2;
         this.rhs = var3;
      }

      public void accept(Visitor var1) {
         var1.visit(this);
      }
   }

   public static class Constant extends Exp {
      public final LuaValue value;

      public Constant(LuaValue var1) {
         this.value = var1;
      }

      public void accept(Visitor var1) {
         var1.visit(this);
      }
   }

   public static class FieldExp extends VarExp {
      public final PrimaryExp lhs;
      public final Name name;

      public FieldExp(PrimaryExp var1, String var2) {
         this.lhs = var1;
         this.name = new Name(var2);
      }

      public void accept(Visitor var1) {
         var1.visit(this);
      }
   }

   public static class FuncCall extends PrimaryExp {
      public final PrimaryExp lhs;
      public final FuncArgs args;

      public FuncCall(PrimaryExp var1, FuncArgs var2) {
         this.lhs = var1;
         this.args = var2;
      }

      public boolean isfunccall() {
         return true;
      }

      public void accept(Visitor var1) {
         var1.visit(this);
      }

      public boolean isvarargexp() {
         return true;
      }
   }

   public static class IndexExp extends VarExp {
      public final PrimaryExp lhs;
      public final Exp exp;

      public IndexExp(PrimaryExp var1, Exp var2) {
         this.lhs = var1;
         this.exp = var2;
      }

      public void accept(Visitor var1) {
         var1.visit(this);
      }
   }

   public static class MethodCall extends FuncCall {
      public final String name;

      public MethodCall(PrimaryExp var1, String var2, FuncArgs var3) {
         super(var1, var3);
         this.name = new String(var2);
      }

      public boolean isfunccall() {
         return true;
      }

      public void accept(Visitor var1) {
         var1.visit(this);
      }
   }

   public static class NameExp extends VarExp {
      public final Name name;

      public NameExp(String var1) {
         this.name = new Name(var1);
      }

      public void markHasAssignment() {
         this.name.variable.hasassignments = true;
      }

      public void accept(Visitor var1) {
         var1.visit(this);
      }
   }

   public static class ParensExp extends PrimaryExp {
      public final Exp exp;

      public ParensExp(Exp var1) {
         this.exp = var1;
      }

      public void accept(Visitor var1) {
         var1.visit(this);
      }
   }

   public abstract static class PrimaryExp extends Exp {
      public boolean isvarexp() {
         return false;
      }

      public boolean isfunccall() {
         return false;
      }
   }

   public static class UnopExp extends Exp {
      public final int op;
      public final Exp rhs;

      public UnopExp(int var1, Exp var2) {
         this.op = var1;
         this.rhs = var2;
      }

      public void accept(Visitor var1) {
         var1.visit(this);
      }
   }

   public abstract static class VarExp extends PrimaryExp {
      public boolean isvarexp() {
         return true;
      }

      public void markHasAssignment() {
      }
   }

   public static class VarargsExp extends Exp {
      public void accept(Visitor var1) {
         var1.visit(this);
      }

      public boolean isvarargexp() {
         return true;
      }
   }
}
