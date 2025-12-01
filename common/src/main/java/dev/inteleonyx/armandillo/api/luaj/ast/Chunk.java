package dev.inteleonyx.armandillo.api.luaj.ast;

public class Chunk extends SyntaxElement {
   public final Block block;

   public Chunk(Block var1) {
      this.block = var1;
   }

   public void accept(Visitor var1) {
      var1.visit(this);
   }
}
