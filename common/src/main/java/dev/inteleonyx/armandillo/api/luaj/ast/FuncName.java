package dev.inteleonyx.armandillo.api.luaj.ast;

import java.util.ArrayList;
import java.util.List;

public class FuncName extends SyntaxElement {
   public final Name name;
   public List dots;
   public String method;

   public FuncName(String var1) {
      this.name = new Name(var1);
   }

   public void adddot(String var1) {
      if (this.dots == null) {
         this.dots = new ArrayList();
      }

      this.dots.add(var1);
   }
}
