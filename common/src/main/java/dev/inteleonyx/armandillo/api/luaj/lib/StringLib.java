package dev.inteleonyx.armandillo.api.luaj.lib;

import dev.inteleonyx.armandillo.api.luaj.*;
import dev.inteleonyx.armandillo.api.luaj.compiler.DumpState;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class StringLib extends TwoArgFunction {
   private static final String FLAGS = "-+ #0";
   private static final int L_ESC = 37;
   private static final LuaString SPECIALS = valueOf("^$*+?.([%-");
   private static final int MAX_CAPTURES = 32;
   private static final int CAP_UNFINISHED = -1;
   private static final int CAP_POSITION = -2;
   private static final byte MASK_ALPHA = 1;
   private static final byte MASK_LOWERCASE = 2;
   private static final byte MASK_UPPERCASE = 4;
   private static final byte MASK_DIGIT = 8;
   private static final byte MASK_PUNCT = 16;
   private static final byte MASK_SPACE = 32;
   private static final byte MASK_CONTROL = 64;
   private static final byte MASK_HEXDIGIT = -128;
   static final byte[] CHAR_TABLE = new byte[256];

   public LuaValue call(LuaValue var1, LuaValue var2) {
      LuaTable var3 = new LuaTable();
      var3.set("byte", new byte_());
      var3.set("char", new char_());
      var3.set("dump", new dump());
      var3.set("find", new find());
      var3.set("format", new format());
      var3.set("gmatch", new gmatch());
      var3.set("gsub", new gsub());
      var3.set("len", new len());
      var3.set("lower", new lower());
      var3.set("match", new match());
      var3.set("rep", new rep());
      var3.set("reverse", new reverse());
      var3.set("sub", new sub());
      var3.set("upper", new upper());
      var2.set("string", var3);
      if (!var2.get("package").isnil()) {
         var2.get("package").get("loaded").set("string", var3);
      }

      if (LuaString.s_metatable == null) {
         LuaString.s_metatable = LuaValue.tableOf(new LuaValue[]{INDEX, var3});
      }

      return var3;
   }

   static void addquoted(Buffer var0, LuaString var1) {
      var0.append((byte)34);
      int var3 = 0;

      for (int var4 = var1.length(); var3 < var4; var3++) {
         int var2;
         switch (var2 = var1.luaByte(var3)) {
            case 10:
            case 34:
            case 92:
               var0.append((byte)92);
               var0.append((byte)var2);
               break;
            default:
               if (var2 > 31 && var2 != 127) {
                  var0.append((byte)var2);
               } else {
                  var0.append((byte)92);
                  if (var3 + 1 != var4 && var1.luaByte(var3 + 1) >= 48 && var1.luaByte(var3 + 1) <= 57) {
                     var0.append((byte)48);
                     var0.append((byte)((char)(48 + var2 / 10)));
                     var0.append((byte)((char)(48 + var2 % 10)));
                  } else {
                     var0.append(Integer.toString(var2));
                  }
               }
         }
      }

      var0.append((byte)34);
   }

   protected String format(String var1, double var2) {
      return String.valueOf(var2);
   }

   static Varargs str_find_aux(Varargs var0, boolean var1) {
      LuaString var2 = var0.checkstring(1);
      LuaString var3 = var0.checkstring(2);
      int var4 = var0.optint(3, 1);
      if (var4 > 0) {
         var4 = Math.min(var4 - 1, var2.length());
      } else if (var4 < 0) {
         var4 = Math.max(0, var2.length() + var4);
      }

      boolean var5 = var1 && (var0.arg(4).toboolean() || var3.indexOfAny(SPECIALS) == -1);
      if (var5) {
         int var6 = var2.indexOf(var3, var4);
         if (var6 != -1) {
            return varargsOf(valueOf(var6 + 1), valueOf(var6 + var3.length()));
         }
      } else {
         MatchState var11 = new MatchState(var0, var2, var3);
         boolean var7 = false;
         byte var8 = 0;
         if (var3.length() > 0 && var3.luaByte(0) == 94) {
            var7 = true;
            var8 = 1;
         }

         int var9 = var4;

         do {
            var11.reset();
            int var10;
            if ((var10 = var11.match(var9, var8)) != -1) {
               if (var1) {
                  return varargsOf(valueOf(var9 + 1), valueOf(var10), var11.push_captures(false, var9, var10));
               }

               return var11.push_captures(true, var9, var10);
            }
         } while (var9++ < var2.length() && !var7);
      }

      return NIL;
   }

   static int posrelat(int var0, int var1) {
      return var0 >= 0 ? var0 : var1 + var0 + 1;
   }

   static {
      for (int var0 = 0; var0 < 256; var0++) {
         char var1 = (char)var0;
         CHAR_TABLE[var0] = (byte)(
            (Character.isDigit(var1) ? 8 : 0)
               | (Character.isLowerCase(var1) ? 2 : 0)
               | (Character.isUpperCase(var1) ? 4 : 0)
               | (var1 >= ' ' && var1 != 127 ? 0 : 64)
         );
         if (var1 >= 'a' && var1 <= 'f' || var1 >= 'A' && var1 <= 'F' || var1 >= '0' && var1 <= '9') {
            CHAR_TABLE[var0] = (byte)(CHAR_TABLE[var0] | -128);
         }

         if (var1 >= '!' && var1 <= '/' || var1 >= ':' && var1 <= '@') {
            CHAR_TABLE[var0] = (byte)(CHAR_TABLE[var0] | 16);
         }

         if ((CHAR_TABLE[var0] & 6) != 0) {
            CHAR_TABLE[var0] = (byte)(CHAR_TABLE[var0] | 1);
         }
      }

      CHAR_TABLE[32] = 32;
      CHAR_TABLE[13] = (byte)(CHAR_TABLE[13] | 32);
      CHAR_TABLE[10] = (byte)(CHAR_TABLE[10] | 32);
      CHAR_TABLE[9] = (byte)(CHAR_TABLE[9] | 32);
      CHAR_TABLE[12] = (byte)(CHAR_TABLE[12] | 32);
      CHAR_TABLE[12] = (byte)(CHAR_TABLE[12] | 32);
   }

   class FormatDesc {
      private boolean leftAdjust;
      private boolean zeroPad;
      private boolean explicitPlus;
      private boolean space;
      private boolean alternateForm;
      private static final int MAX_FLAGS = 5;
      private int width;
      int precision;
      public final int conversion;
      public final int length;
      public final String src;

      public FormatDesc(Varargs var2, LuaString var3, int var4) {
         int var5 = var4;
         int var6 = var3.length();
         int var7 = 0;
         boolean var8 = true;

         while (var8) {
            switch (var7 = var5 < var6 ? var3.luaByte(var5++) : 0) {
               case 32:
                  this.space = true;
                  break;
               case 35:
                  this.alternateForm = true;
                  break;
               case 43:
                  this.explicitPlus = true;
                  break;
               case 45:
                  this.leftAdjust = true;
                  break;
               case 48:
                  this.zeroPad = true;
                  break;
               default:
                  var8 = false;
            }
         }

         if (var5 - var4 > 5) {
            LuaValue.error("invalid format (repeated flags)");
         }

         this.width = -1;
         if (Character.isDigit((char)var7)) {
            this.width = var7 - 48;
            var7 = var5 < var6 ? var3.luaByte(var5++) : 0;
            if (Character.isDigit((char)var7)) {
               this.width = this.width * 10 + (var7 - 48);
               var7 = var5 < var6 ? var3.luaByte(var5++) : 0;
            }
         }

         this.precision = -1;
         if (var7 == 46) {
            var7 = var5 < var6 ? var3.luaByte(var5++) : 0;
            if (Character.isDigit((char)var7)) {
               this.precision = var7 - 48;
               var7 = var5 < var6 ? var3.luaByte(var5++) : 0;
               if (Character.isDigit((char)var7)) {
                  this.precision = this.precision * 10 + (var7 - 48);
                  var7 = var5 < var6 ? var3.luaByte(var5++) : 0;
               }
            }
         }

         if (Character.isDigit((char)var7)) {
            LuaValue.error("invalid format (width or precision too long)");
         }

         this.zeroPad = this.zeroPad & !this.leftAdjust;
         this.conversion = var7;
         this.length = var5 - var4;
         this.src = var3.substring(var4 - 1, var5).tojstring();
      }

      public void format(Buffer var1, byte var2) {
         var1.append(var2);
      }

      public void format(Buffer var1, long var2) {
         String var4;
         if (var2 == 0L && this.precision == 0) {
            var4 = "";
         } else {
            byte var5;
            switch (this.conversion) {
               case 88:
               case 120:
                  var5 = 16;
                  break;
               case 111:
                  var5 = 8;
                  break;
               default:
                  var5 = 10;
            }

            var4 = Long.toString(var2, var5);
            if (this.conversion == 88) {
               var4 = var4.toUpperCase();
            }
         }

         int var9 = var4.length();
         int var6 = var9;
         if (var2 < 0L) {
            var6 = var9 - 1;
         } else if (this.explicitPlus || this.space) {
            var9++;
         }

         int var7;
         if (this.precision > var6) {
            var7 = this.precision - var6;
         } else if (this.precision == -1 && this.zeroPad && this.width > var9) {
            var7 = this.width - var9;
         } else {
            var7 = 0;
         }

         var9 += var7;
         int var8 = this.width > var9 ? this.width - var9 : 0;
         if (!this.leftAdjust) {
            this.pad(var1, ' ', var8);
         }

         if (var2 < 0L) {
            if (var7 > 0) {
               var1.append((byte)45);
               var4 = var4.substring(1);
            }
         } else if (this.explicitPlus) {
            var1.append((byte)43);
         } else if (this.space) {
            var1.append((byte)32);
         }

         if (var7 > 0) {
            this.pad(var1, '0', var7);
         }

         var1.append(var4);
         if (this.leftAdjust) {
            this.pad(var1, ' ', var8);
         }
      }

      public void format(Buffer var1, double var2) {
         var1.append(StringLib.this.format(this.src, var2));
      }

      public void format(Buffer var1, LuaString var2) {
         int var3 = var2.indexOf((byte)0, 0);
         if (var3 != -1) {
            var2 = var2.substring(0, var3);
         }

         var1.append(var2);
      }

      public final void pad(Buffer var1, char var2, int var3) {
         byte var4 = (byte)var2;

         while (var3-- > 0) {
            var1.append(var4);
         }
      }
   }

   static class GMatchAux extends VarArgFunction {
      private final int srclen;
      private final MatchState ms;
      private int soffset;

      public GMatchAux(Varargs var1, LuaString var2, LuaString var3) {
         this.srclen = var2.length();
         this.ms = new MatchState(var1, var2, var3);
         this.soffset = 0;
      }

      public Varargs invoke(Varargs var1) {
         while (this.soffset <= this.srclen) {
            this.ms.reset();
            int var2 = this.ms.match(this.soffset, 0);
            if (var2 >= 0) {
               int var3 = this.soffset;
               this.soffset = var2;
               if (var3 == var2) {
                  this.soffset++;
               }

               return this.ms.push_captures(true, var3, var2);
            }

            this.soffset++;
         }

         return NIL;
      }
   }

   static class MatchState {
      final LuaString s;
      final LuaString p;
      final Varargs args;
      int level;
      int[] cinit;
      int[] clen;

      MatchState(Varargs var1, LuaString var2, LuaString var3) {
         this.s = var2;
         this.p = var3;
         this.args = var1;
         this.level = 0;
         this.cinit = new int[32];
         this.clen = new int[32];
      }

      void reset() {
         this.level = 0;
      }

      private void add_s(Buffer var1, LuaString var2, int var3, int var4) {
         int var5 = var2.length();

         for (int var6 = 0; var6 < var5; var6++) {
            byte var7 = (byte)var2.luaByte(var6);
            if (var7 != 37) {
               var1.append(var7);
            } else {
               var6++;
               var7 = (byte)(var6 < var5 ? var2.luaByte(var6) : 0);
               if (!Character.isDigit((char)var7)) {
                  if (var7 != 37) {
                     LuaValue.error(
                        "invalid use of '%' in replacement string: after '%' must be '0'-'9' or '%', but found "
                           + (var6 < var5 ? "symbol '" + (char)var7 + "' with code " + var7 + " at pos " + (var6 + 1) : "end of string")
                     );
                  }

                  var1.append(var7);
               } else if (var7 == 48) {
                  var1.append(this.s.substring(var3, var4));
               } else {
                  var1.append(this.push_onecapture(var7 - 49, var3, var4).strvalue());
               }
            }
         }
      }

      public void add_value(Buffer var1, int var2, int var3, LuaValue var4) {
         Object var5;
         switch (var4.type()) {
            case 3:
            case 4:
               this.add_s(var1, var4.strvalue(), var2, var3);
               return;
            case 5:
               var5 = var4.get(this.push_onecapture(0, var2, var3));
               break;
            case 6:
               var5 = var4.invoke(this.push_captures(true, var2, var3)).arg1();
               break;
            default:
               LuaValue.error("bad argument: string/function/table expected");
               return;
         }

         if (!((LuaValue)var5).toboolean()) {
            var5 = this.s.substring(var2, var3);
         } else if (!((LuaValue)var5).isstring()) {
            LuaValue.error("invalid replacement value (a " + ((LuaValue)var5).typename() + ")");
         }

         var1.append(((LuaValue)var5).strvalue());
      }

      Varargs push_captures(boolean var1, int var2, int var3) {
         int var4 = this.level == 0 && var1 ? 1 : this.level;
         switch (var4) {
            case 0:
               return LuaValue.NONE;
            case 1:
               return this.push_onecapture(0, var2, var3);
            default:
               LuaValue[] var5 = new LuaValue[var4];

               for (int var6 = 0; var6 < var4; var6++) {
                  var5[var6] = this.push_onecapture(var6, var2, var3);
               }

               return LuaValue.varargsOf(var5);
         }
      }

      private LuaValue push_onecapture(int var1, int var2, int var3) {
         if (var1 >= this.level) {
            return (LuaValue)(var1 == 0 ? this.s.substring(var2, var3) : LuaValue.error("invalid capture index"));
         } else {
            int var4 = this.clen[var1];
            if (var4 == -1) {
               return LuaValue.error("unfinished capture");
            } else if (var4 == -2) {
               return LuaValue.valueOf(this.cinit[var1] + 1);
            } else {
               int var5 = this.cinit[var1];
               return this.s.substring(var5, var5 + var4);
            }
         }
      }

      private int check_capture(int var1) {
         var1 -= 49;
         if (var1 < 0 || var1 >= this.level || this.clen[var1] == -1) {
            LuaValue.error("invalid capture index");
         }

         return var1;
      }

      private int capture_to_close() {
         int var1 = this.level;
         var1--;

         while (var1 >= 0) {
            if (this.clen[var1] == -1) {
               return var1;
            }

            var1--;
         }

         LuaValue.error("invalid pattern capture");
         return 0;
      }

      int classend(int var1) {
         switch (this.p.luaByte(var1++)) {
            case 37:
               if (var1 == this.p.length()) {
                  LuaValue.error("malformed pattern (ends with '%')");
               }

               return var1 + 1;
            case 91:
               if (var1 != this.p.length() && this.p.luaByte(var1) == 94) {
                  var1++;
               }

               do {
                  if (var1 == this.p.length()) {
                     LuaValue.error("malformed pattern (missing ']')");
                  }

                  if (this.p.luaByte(var1++) == 37 && var1 < this.p.length()) {
                     var1++;
                  }
               } while (var1 == this.p.length() || this.p.luaByte(var1) != 93);

               return var1 + 1;
            default:
               return var1;
         }
      }

      static boolean match_class(int var0, int var1) {
         char var2 = Character.toLowerCase((char)var1);
         byte var3 = StringLib.CHAR_TABLE[var0];
         boolean var4;
         switch (var2) {
            case 'a':
               var4 = (var3 & 1) != 0;
               break;
            case 'b':
            case 'e':
            case 'f':
            case 'g':
            case 'h':
            case 'i':
            case 'j':
            case 'k':
            case 'm':
            case 'n':
            case 'o':
            case 'q':
            case 'r':
            case 't':
            case 'v':
            case 'y':
            default:
               return var1 == var0;
            case 'c':
               var4 = (var3 & 64) != 0;
               break;
            case 'd':
               var4 = (var3 & 8) != 0;
               break;
            case 'l':
               var4 = (var3 & 2) != 0;
               break;
            case 'p':
               var4 = (var3 & 16) != 0;
               break;
            case 's':
               var4 = (var3 & 32) != 0;
               break;
            case 'u':
               var4 = (var3 & 4) != 0;
               break;
            case 'w':
               var4 = (var3 & 9) != 0;
               break;
            case 'x':
               var4 = (var3 & -128) != 0;
               break;
            case 'z':
               var4 = var0 == 0;
         }

         return var2 == var1 ? var4 : !var4;
      }

      boolean matchbracketclass(int var1, int var2, int var3) {
         boolean var4 = true;
         if (this.p.luaByte(var2 + 1) == 94) {
            var4 = false;
            var2++;
         }

         while (++var2 < var3) {
            if (this.p.luaByte(var2) == 37) {
               if (match_class(var1, this.p.luaByte(++var2))) {
                  return var4;
               }
            } else if (this.p.luaByte(var2 + 1) == 45 && var2 + 2 < var3) {
               var2 += 2;
               if (this.p.luaByte(var2 - 2) <= var1 && var1 <= this.p.luaByte(var2)) {
                  return var4;
               }
            } else if (this.p.luaByte(var2) == var1) {
               return var4;
            }
         }

         return !var4;
      }

      boolean singlematch(int var1, int var2, int var3) {
         switch (this.p.luaByte(var2)) {
            case 37:
               return match_class(var1, this.p.luaByte(var2 + 1));
            case 46:
               return true;
            case 91:
               return this.matchbracketclass(var1, var2, var3 - 1);
            default:
               return this.p.luaByte(var2) == var1;
         }
      }

      int match(int var1, int var2) {
         while (var2 != this.p.length()) {
            switch (this.p.luaByte(var2)) {
               case 37:
                  if (var2 + 1 == this.p.length()) {
                     LuaValue.error("malformed pattern (ends with '%')");
                  }

                  switch (this.p.luaByte(var2 + 1)) {
                     case 98:
                        var1 = this.matchbalance(var1, var2 + 2);
                        if (var1 == -1) {
                           return -1;
                        }

                        var2 += 4;
                        continue;
                     case 102:
                        var2 += 2;
                        if (var2 == this.p.length() || this.p.luaByte(var2) != 91) {
                           LuaValue.error("Missing '[' after '%f' in pattern");
                        }

                        int var11 = this.classend(var2);
                        int var12 = var1 == 0 ? 0 : this.s.luaByte(var1 - 1);
                        int var13 = var1 == this.s.length() ? 0 : this.s.luaByte(var1);
                        if (!this.matchbracketclass(var12, var2, var11 - 1) && this.matchbracketclass(var13, var2, var11 - 1)) {
                           var2 = var11;
                           continue;
                        }

                        return -1;
                     default:
                        int var3 = this.p.luaByte(var2 + 1);
                        if (Character.isDigit((char)var3)) {
                           var1 = this.match_capture(var1, var3);
                           if (var1 == -1) {
                              return -1;
                           }

                           return this.match(var1, var2 + 2);
                        }
                  }
               case 36:
                  if (var2 + 1 == this.p.length()) {
                     return var1 == this.s.length() ? var1 : -1;
                  }
               case 38:
               case 39:
               default:
                  break;
               case 40:
                  var2++;
                  if (var2 < this.p.length() && this.p.luaByte(var2) == 41) {
                     return this.start_capture(var1, var2 + 1, -2);
                  }

                  return this.start_capture(var1, var2, -1);
               case 41:
                  return this.end_capture(var1, var2 + 1);
            }

            int var10 = this.classend(var2);
            boolean var4 = var1 < this.s.length() && this.singlematch(this.s.luaByte(var1), var2, var10);
            int var5 = var10 < this.p.length() ? this.p.luaByte(var10) : 0;
            switch (var5) {
               case 42:
                  return this.max_expand(var1, var2, var10);
               case 43:
                  return var4 ? this.max_expand(var1 + 1, var2, var10) : -1;
               case 45:
                  return this.min_expand(var1, var2, var10);
               case 63:
                  int var6;
                  if (var4 && (var6 = this.match(var1 + 1, var10 + 1)) != -1) {
                     return var6;
                  }

                  var2 = var10 + 1;
                  break;
               default:
                  if (!var4) {
                     return -1;
                  }

                  var1++;
                  var2 = var10;
            }
         }

         return var1;
      }

      int max_expand(int var1, int var2, int var3) {
         int var4 = 0;

         while (var1 + var4 < this.s.length() && this.singlematch(this.s.luaByte(var1 + var4), var2, var3)) {
            var4++;
         }

         while (var4 >= 0) {
            int var5 = this.match(var1 + var4, var3 + 1);
            if (var5 != -1) {
               return var5;
            }

            var4--;
         }

         return -1;
      }

      int min_expand(int var1, int var2, int var3) {
         while (true) {
            int var4 = this.match(var1, var3 + 1);
            if (var4 != -1) {
               return var4;
            }

            if (var1 >= this.s.length() || !this.singlematch(this.s.luaByte(var1), var2, var3)) {
               return -1;
            }

            var1++;
         }
      }

      int start_capture(int var1, int var2, int var3) {
         int var5 = this.level;
         if (var5 >= 32) {
            LuaValue.error("too many captures");
         }

         this.cinit[var5] = var1;
         this.clen[var5] = var3;
         this.level = var5 + 1;
         int var4;
         if ((var4 = this.match(var1, var2)) == -1) {
            this.level--;
         }

         return var4;
      }

      int end_capture(int var1, int var2) {
         int var3 = this.capture_to_close();
         this.clen[var3] = var1 - this.cinit[var3];
         int var4;
         if ((var4 = this.match(var1, var2)) == -1) {
            this.clen[var3] = -1;
         }

         return var4;
      }

      int match_capture(int var1, int var2) {
         var2 = this.check_capture(var2);
         int var3 = this.clen[var2];
         return this.s.length() - var1 >= var3 && LuaString.equals(this.s, this.cinit[var2], this.s, var1, var3) ? var1 + var3 : -1;
      }

      int matchbalance(int var1, int var2) {
         int var3 = this.p.length();
         if (var2 == var3 || var2 + 1 == var3) {
            LuaValue.error("unbalanced pattern");
         }

         int var4 = this.s.length();
         if (var1 >= var4) {
            return -1;
         } else {
            int var5 = this.p.luaByte(var2);
            if (this.s.luaByte(var1) != var5) {
               return -1;
            } else {
               int var6 = this.p.luaByte(var2 + 1);
               int var7 = 1;

               while (++var1 < var4) {
                  if (this.s.luaByte(var1) == var6) {
                     if (--var7 == 0) {
                        return var1 + 1;
                     }
                  } else if (this.s.luaByte(var1) == var5) {
                     var7++;
                  }
               }

               return -1;
            }
         }
      }
   }

   static final class byte_ extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         LuaString var2 = var1.checkstring(1);
         int var3 = var2.m_length;
         int var4 = StringLib.posrelat(var1.optint(2, 1), var3);
         int var5 = StringLib.posrelat(var1.optint(3, var4), var3);
         if (var4 <= 0) {
            var4 = 1;
         }

         if (var5 > var3) {
            var5 = var3;
         }

         if (var4 > var5) {
            return NONE;
         } else {
            int var6 = var5 - var4 + 1;
            if (var4 + var6 <= var5) {
               error("string slice too long");
            }

            LuaValue[] var8 = new LuaValue[var6];

            for (int var7 = 0; var7 < var6; var7++) {
               var8[var7] = valueOf(var2.luaByte(var4 + var7 - 1));
            }

            return varargsOf(var8);
         }
      }
   }

   static final class char_ extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         int var2 = var1.narg();
         byte[] var3 = new byte[var2];
         int var4 = 0;

         for (int var5 = 1; var4 < var2; var5++) {
            int var6 = var1.checkint(var5);
            if (var6 < 0 || var6 >= 256) {
               argerror(var5, "invalid value for string.char [0; 255]: " + var6);
            }

            var3[var4] = (byte)var6;
            var4++;
         }

         return LuaString.valueUsing(var3);
      }
   }

   static final class dump extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         LuaFunction var2 = var1.checkfunction(1);
         ByteArrayOutputStream var3 = new ByteArrayOutputStream();

         try {
            DumpState.dump(((LuaClosure)var2).p, var3, var1.optboolean(2, true));
            return LuaString.valueUsing(var3.toByteArray());
         } catch (IOException var5) {
            return error(var5.getMessage());
         }
      }
   }

   static final class find extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         return StringLib.str_find_aux(var1, true);
      }
   }

   final class format extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         LuaString var2 = var1.checkstring(1);
         int var3 = var2.length();
         Buffer var4 = new Buffer(var3);
         int var5 = 1;
         int var7 = 0;

         while (var7 < var3) {
            int var6;
            switch (var6 = var2.luaByte(var7++)) {
               case 10:
                  var4.append("\n");
                  break;
               case 37:
                  if (var7 < var3) {
                     if (var2.luaByte(var7) == 37) {
                        var7++;
                        var4.append((byte)37);
                     } else {
                        var5++;
                        FormatDesc var8 = StringLib.this.new FormatDesc(var1, var2, var7);
                        var7 += var8.length;
                        switch (var8.conversion) {
                           case 69:
                           case 71:
                           case 101:
                           case 102:
                           case 103:
                              var8.format(var4, var1.checkdouble(var5));
                              break;
                           case 70:
                           case 72:
                           case 73:
                           case 74:
                           case 75:
                           case 76:
                           case 77:
                           case 78:
                           case 79:
                           case 80:
                           case 81:
                           case 82:
                           case 83:
                           case 84:
                           case 85:
                           case 86:
                           case 87:
                           case 89:
                           case 90:
                           case 91:
                           case 92:
                           case 93:
                           case 94:
                           case 95:
                           case 96:
                           case 97:
                           case 98:
                           case 104:
                           case 106:
                           case 107:
                           case 108:
                           case 109:
                           case 110:
                           case 112:
                           case 114:
                           case 116:
                           case 118:
                           case 119:
                           default:
                              error("invalid option '%" + (char)var8.conversion + "' to 'format'");
                              break;
                           case 88:
                           case 111:
                           case 117:
                           case 120:
                              var8.format(var4, var1.checklong(var5));
                              break;
                           case 99:
                              var8.format(var4, (byte)var1.checkint(var5));
                              break;
                           case 100:
                           case 105:
                              var8.format(var4, var1.checklong(var5));
                              break;
                           case 113:
                              StringLib.addquoted(var4, var1.checkstring(var5));
                              break;
                           case 115:
                              LuaString var9 = var1.checkstring(var5);
                              if (var8.precision == -1 && var9.length() >= 100) {
                                 var4.append(var9);
                              } else {
                                 var8.format(var4, var9);
                              }
                        }
                     }
                  }
                  break;
               default:
                  var4.append((byte)var6);
            }
         }

         return var4.tostring();
      }
   }

   static final class gmatch extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         LuaString var2 = var1.checkstring(1);
         LuaString var3 = var1.checkstring(2);
         return new GMatchAux(var1, var2, var3);
      }
   }

   static final class gsub extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         LuaString var2 = var1.checkstring(1);
         int var3 = var2.length();
         LuaString var4 = var1.checkstring(2);
         LuaValue var5 = var1.arg(3);
         int var6 = var1.optint(4, var3 + 1);
         boolean var7 = var4.length() > 0 && var4.charAt(0) == 94;
         Buffer var8 = new Buffer(var3);
         MatchState var9 = new MatchState(var1, var2, var4);
         int var10 = 0;
         int var11 = 0;

         while (var11 < var6) {
            var9.reset();
            int var12 = var9.match(var10, var7 ? 1 : 0);
            if (var12 != -1) {
               var11++;
               var9.add_value(var8, var10, var12, var5);
            }

            if (var12 != -1 && var12 > var10) {
               var10 = var12;
            } else {
               if (var10 >= var3) {
                  break;
               }

               var8.append((byte)var2.luaByte(var10++));
            }

            if (var7) {
               break;
            }
         }

         var8.append(var2.substring(var10, var3));
         return varargsOf(var8.tostring(), valueOf(var11));
      }
   }

   static final class len extends OneArgFunction {
      public LuaValue call(LuaValue var1) {
         return var1.checkstring().len();
      }
   }

   static final class lower extends OneArgFunction {
      public LuaValue call(LuaValue var1) {
         return valueOf(var1.checkjstring().toLowerCase());
      }
   }

   static final class match extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         return StringLib.str_find_aux(var1, false);
      }
   }

   static final class rep extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         LuaString var2 = var1.checkstring(1);
         int var3 = var1.checkint(2);
         byte[] var4 = new byte[var2.length() * var3];
         int var5 = var2.length();

         for (int var6 = 0; var6 < var4.length; var6 += var5) {
            var2.copyInto(0, var4, var6, var5);
         }

         return LuaString.valueUsing(var4);
      }
   }

   static final class reverse extends OneArgFunction {
      public LuaValue call(LuaValue var1) {
         LuaString var2 = var1.checkstring();
         int var3 = var2.length();
         byte[] var4 = new byte[var3];
         int var5 = 0;

         for (int var6 = var3 - 1; var5 < var3; var6--) {
            var4[var6] = (byte)var2.luaByte(var5);
            var5++;
         }

         return LuaString.valueUsing(var4);
      }
   }

   static final class sub extends VarArgFunction {
      public Varargs invoke(Varargs var1) {
         LuaString var2 = var1.checkstring(1);
         int var3 = var2.length();
         int var4 = StringLib.posrelat(var1.checkint(2), var3);
         int var5 = StringLib.posrelat(var1.optint(3, -1), var3);
         if (var4 < 1) {
            var4 = 1;
         }

         if (var5 > var3) {
            var5 = var3;
         }

         return var4 <= var5 ? var2.substring(var4 - 1, var5) : EMPTYSTRING;
      }
   }

   static final class upper extends OneArgFunction {
      public LuaValue call(LuaValue var1) {
         return valueOf(var1.checkjstring().toUpperCase());
      }
   }
}
