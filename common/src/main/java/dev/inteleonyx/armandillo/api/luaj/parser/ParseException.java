package dev.inteleonyx.armandillo.api.luaj.parser;

public class ParseException extends Exception {
   private static final long serialVersionUID = 1L;
   public Token currentToken;
   public int[][] expectedTokenSequences;
   public String[] tokenImage;
   protected String eol = System.getProperty("line.separator", "\n");

   public ParseException(Token var1, int[][] var2, String[] var3) {
      super(initialise(var1, var2, var3));
      this.currentToken = var1;
      this.expectedTokenSequences = var2;
      this.tokenImage = var3;
   }

   public ParseException() {
   }

   public ParseException(String var1) {
      super(var1);
   }

   private static String initialise(Token var0, int[][] var1, String[] var2) {
      String var3 = System.getProperty("line.separator", "\n");
      StringBuffer var4 = new StringBuffer();
      int var5 = 0;

      for (int var6 = 0; var6 < var1.length; var6++) {
         if (var5 < var1[var6].length) {
            var5 = var1[var6].length;
         }

         for (int var7 = 0; var7 < var1[var6].length; var7++) {
            var4.append(var2[var1[var6][var7]]).append(' ');
         }

         if (var1[var6][var1[var6].length - 1] != 0) {
            var4.append("...");
         }

         var4.append(var3).append("    ");
      }

      String var9 = "Encountered \"";
      Token var16 = var0.next;

      for (int var8 = 0; var8 < var5; var8++) {
         if (var8 != 0) {
            var9 = var9 + " ";
         }

         if (var16.kind == 0) {
            var9 = var9 + var2[0];
            break;
         }

         var9 = var9 + " " + var2[var16.kind];
         var9 = var9 + " \"";
         var9 = var9 + add_escapes(var16.image);
         var9 = var9 + " \"";
         var16 = var16.next;
      }

      var9 = var9 + "\" at line " + var0.next.beginLine + ", column " + var0.next.beginColumn;
      var9 = var9 + "." + var3;
      if (var1.length == 1) {
         var9 = var9 + "Was expecting:" + var3 + "    ";
      } else {
         var9 = var9 + "Was expecting one of:" + var3 + "    ";
      }

      return var9 + var4.toString();
   }

   static String add_escapes(String var0) {
      StringBuffer var1 = new StringBuffer();

      for (int var3 = 0; var3 < var0.length(); var3++) {
         switch (var0.charAt(var3)) {
            case '\u0000':
               break;
            case '\b':
               var1.append("\\b");
               break;
            case '\t':
               var1.append("\\t");
               break;
            case '\n':
               var1.append("\\n");
               break;
            case '\f':
               var1.append("\\f");
               break;
            case '\r':
               var1.append("\\r");
               break;
            case '"':
               var1.append("\\\"");
               break;
            case '\'':
               var1.append("\\'");
               break;
            case '\\':
               var1.append("\\\\");
               break;
            default:
               char var2;
               if ((var2 = var0.charAt(var3)) >= ' ' && var2 <= '~') {
                  var1.append(var2);
               } else {
                  String var4 = "0000" + Integer.toString(var2, 16);
                  var1.append("\\u" + var4.substring(var4.length() - 4, var4.length()));
               }
         }
      }

      return var1.toString();
   }
}
