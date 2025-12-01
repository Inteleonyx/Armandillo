package dev.inteleonyx.armandillo.api.luaj.ast;

import java.util.ArrayList;
import java.util.List;

public class Block extends Stat {
   public List stats = new ArrayList();
   public NameScope scope;

   public void add(Stat var1) {
      if (var1 != null) {
         this.stats.add(var1);
      }
   }

   public void accept(Visitor var1) {
      var1.visit(this);
   }
}
