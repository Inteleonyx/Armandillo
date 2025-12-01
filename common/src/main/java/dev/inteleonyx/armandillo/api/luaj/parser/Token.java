package dev.inteleonyx.armandillo.api.luaj.parser;

import java.io.Serializable;

public class Token implements Serializable {
   private static final long serialVersionUID = 1L;
   public int kind;
   public int beginLine;
   public int beginColumn;
   public int endLine;
   public int endColumn;
   public String image;
   public Token next;
   public Token specialToken;

   public Object getValue() {
      return null;
   }

   public Token() {
   }

   public Token(int var1) {
      this(var1, null);
   }

   public Token(int var1, String var2) {
      this.kind = var1;
      this.image = var2;
   }

   public String toString() {
      return this.image;
   }

   public static Token newToken(int var0, String var1) {
      switch (var0) {
         default:
            return new Token(var0, var1);
      }
   }

   public static Token newToken(int var0) {
      return newToken(var0, null);
   }
}
