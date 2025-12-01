package dev.inteleonyx.armandillo.api.luaj.ast;

import dev.inteleonyx.armandillo.api.luaj.LuaString;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

public class Str {
   private Str() {
   }

   public static LuaString quoteString(String var0) {
      String var1 = var0.substring(1, var0.length() - 1);
      byte[] var2 = unquote(var1);
      return LuaString.valueUsing(var2);
   }

   public static LuaString charString(String var0) {
      String var1 = var0.substring(1, var0.length() - 1);
      byte[] var2 = unquote(var1);
      return LuaString.valueUsing(var2);
   }

   public static LuaString longString(String var0) {
      int var1 = var0.indexOf(91, var0.indexOf(91) + 1) + 1;
      String var2 = var0.substring(var1, var0.length() - var1);
      byte[] var3 = iso88591bytes(var2);
      return LuaString.valueUsing(var3);
   }

   public static byte[] iso88591bytes(String var0) {
      try {
         return var0.getBytes("ISO8859-1");
      } catch (UnsupportedEncodingException var2) {
         throw new IllegalStateException("ISO8859-1 not supported");
      }
   }

   public static byte[] unquote(String var0) {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      char[] var2 = var0.toCharArray();
      int var3 = var2.length;

      for (int var4 = 0; var4 < var3; var4++) {
         if (var2[var4] == '\\' && var4 < var3) {
            var4++;
            switch (var2[var4]) {
               case '"':
                  var1.write(34);
                  break;
               case '#':
               case '$':
               case '%':
               case '&':
               case '(':
               case ')':
               case '*':
               case '+':
               case ',':
               case '-':
               case '.':
               case '/':
               case ':':
               case ';':
               case '<':
               case '=':
               case '>':
               case '?':
               case '@':
               case 'A':
               case 'B':
               case 'C':
               case 'D':
               case 'E':
               case 'F':
               case 'G':
               case 'H':
               case 'I':
               case 'J':
               case 'K':
               case 'L':
               case 'M':
               case 'N':
               case 'O':
               case 'P':
               case 'Q':
               case 'R':
               case 'S':
               case 'T':
               case 'U':
               case 'V':
               case 'W':
               case 'X':
               case 'Y':
               case 'Z':
               case '[':
               case ']':
               case '^':
               case '_':
               case '`':
               case 'c':
               case 'd':
               case 'e':
               case 'g':
               case 'h':
               case 'i':
               case 'j':
               case 'k':
               case 'l':
               case 'm':
               case 'o':
               case 'p':
               case 'q':
               case 's':
               case 'u':
               default:
                  var1.write((byte)var2[var4]);
                  break;
               case '\'':
                  var1.write(39);
                  break;
               case '0':
               case '1':
               case '2':
               case '3':
               case '4':
               case '5':
               case '6':
               case '7':
               case '8':
               case '9':
                  int var5 = var2[var4++] - '0';

                  for (int var6 = 0; var4 < var3 && var6 < 2 && var2[var4] >= '0' && var2[var4] <= '9'; var6++) {
                     var5 = var5 * 10 + (var2[var4] - '0');
                     var4++;
                  }

                  var1.write((byte)var5);
                  var4--;
                  break;
               case '\\':
                  var1.write(92);
                  break;
               case 'a':
                  var1.write(7);
                  break;
               case 'b':
                  var1.write(8);
                  break;
               case 'f':
                  var1.write(12);
                  break;
               case 'n':
                  var1.write(10);
                  break;
               case 'r':
                  var1.write(13);
                  break;
               case 't':
                  var1.write(9);
                  break;
               case 'v':
                  var1.write(11);
            }
         } else {
            var1.write((byte)var2[var4]);
         }
      }

      return var1.toByteArray();
   }
}
