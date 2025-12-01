package dev.inteleonyx.armandillo.api.luaj.parser;

import java.io.IOException;
import java.io.PrintStream;

public class LuaParserTokenManager implements LuaParserConstants {
   public PrintStream debugStream = System.out;
   static final long[] jjbitVec0 = new long[]{-2L, -1L, -1L, -1L};
   static final long[] jjbitVec2 = new long[]{0L, 0L, -1L, -1L};
   static final int[] jjnextStates = new int[]{
      62,
      63,
      65,
      32,
      49,
      50,
      51,
      36,
      37,
      38,
      26,
      27,
      29,
      22,
      36,
      37,
      38,
      46,
      36,
      47,
      37,
      38,
      49,
      50,
      51,
      59,
      49,
      60,
      50,
      51,
      20,
      25,
      23,
      24,
      33,
      34,
      39,
      40,
      45,
      52,
      53,
      58,
      0,
      1,
      3
   };
   public static final String[] jjstrLiteralImages = new String[]{
      "",
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      "and",
      "break",
      "do",
      "else",
      "elseif",
      "end",
      "false",
      "for",
      "function",
      "goto",
      "if",
      "in",
      "local",
      "nil",
      "not",
      "or",
      "return",
      "repeat",
      "then",
      "true",
      "until",
      "while",
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      "::",
      null,
      null,
      null,
      "#",
      ";",
      "=",
      ",",
      ".",
      ":",
      "(",
      ")",
      "[",
      "]",
      "...",
      "{",
      "}",
      "+",
      "-",
      "*",
      "/",
      "^",
      "%",
      "..",
      "<",
      "<=",
      ">",
      ">=",
      "==",
      "~="
   };
   public static final String[] lexStateNames = new String[]{
      "DEFAULT", "IN_COMMENT", "IN_LC0", "IN_LC1", "IN_LC2", "IN_LC3", "IN_LCN", "IN_LS0", "IN_LS1", "IN_LS2", "IN_LS3", "IN_LSN"
   };
   public static final int[] jjnewLexState = new int[]{
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      2,
      3,
      4,
      5,
      6,
      7,
      8,
      9,
      10,
      11,
      1,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1
   };
   static final long[] jjtoToken = new long[]{6926536226618998785L, 2147483618L};
   static final long[] jjtoSkip = new long[]{8257598L, 0L};
   static final long[] jjtoSpecial = new long[]{8257536L, 0L};
   static final long[] jjtoMore = new long[]{268566464L, 0L};
   protected SimpleCharStream input_stream;
   private final int[] jjrounds = new int[66];
   private final int[] jjstateSet = new int[132];
   private final StringBuffer jjimage = new StringBuffer();
   private StringBuffer image = this.jjimage;
   private int jjimageLen;
   private int lengthOfMatch;
   protected char curChar;
   int curLexState = 0;
   int defaultLexState = 0;
   int jjnewStateCnt;
   int jjround;
   int jjmatchedPos;
   int jjmatchedKind;

   public void setDebugStream(PrintStream var1) {
      this.debugStream = var1;
   }

   private int jjStopAtPos(int var1, int var2) {
      this.jjmatchedKind = var2;
      this.jjmatchedPos = var1;
      return var1 + 1;
   }

   private int jjMoveStringLiteralDfa0_2() {
      switch (this.curChar) {
         case ']':
            return this.jjMoveStringLiteralDfa1_2(262144L);
         default:
            return 1;
      }
   }

   private int jjMoveStringLiteralDfa1_2(long var1) {
      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var4) {
         return 1;
      }

      switch (this.curChar) {
         case ']':
            if ((var1 & 262144L) != 0L) {
               return this.jjStopAtPos(1, 18);
            }

            return 2;
         default:
            return 2;
      }
   }

   private int jjMoveStringLiteralDfa0_11() {
      return this.jjMoveNfa_11(6, 0);
   }

   private int jjMoveNfa_11(int var1, int var2) {
      int var3 = 0;
      this.jjnewStateCnt = 7;
      int var4 = 1;
      this.jjstateSet[0] = var1;
      int var5 = Integer.MAX_VALUE;

      while (true) {
         if (++this.jjround == Integer.MAX_VALUE) {
            this.ReInitRounds();
         }

         if (this.curChar < '@') {
            long var15 = 1L << this.curChar;

            do {
               var4--;
               switch (this.jjstateSet[var4]) {
                  case 0:
                  case 1:
                     if (this.curChar == '=') {
                        this.jjCheckNAddTwoStates(1, 2);
                     }
                  case 2:
                  default:
                     break;
                  case 3:
                     if (this.curChar == '=') {
                        this.jjstateSet[this.jjnewStateCnt++] = 0;
                     }
                     break;
                  case 4:
                     if (this.curChar == '=') {
                        this.jjstateSet[this.jjnewStateCnt++] = 3;
                     }
                     break;
                  case 5:
                     if (this.curChar == '=') {
                        this.jjstateSet[this.jjnewStateCnt++] = 4;
                     }
               }
            } while (var4 != var3);
         } else if (this.curChar < 128) {
            long var14 = 1L << (this.curChar & '?');

            do {
               var4--;
               switch (this.jjstateSet[var4]) {
                  case 2:
                     if (this.curChar == ']' && var5 > 27) {
                        var5 = 27;
                     }
                     break;
                  case 6:
                     if (this.curChar == ']') {
                        this.jjstateSet[this.jjnewStateCnt++] = 5;
                     }
               }
            } while (var4 != var3);
         } else {
            int var6 = this.curChar >> '\b';
            int var7 = var6 >> 6;
            long var8 = 1L << (var6 & 63);
            int var10 = (this.curChar & 255) >> 6;
            long var11 = 1L << (this.curChar & '?');

            do {
               var4--;
               switch (this.jjstateSet[var4]) {
               }
            } while (var4 != var3);
         }

         if (var5 != Integer.MAX_VALUE) {
            this.jjmatchedKind = var5;
            this.jjmatchedPos = var2;
            var5 = Integer.MAX_VALUE;
         }

         var2++;
         if ((var4 = this.jjnewStateCnt) == (var3 = 7 - (this.jjnewStateCnt = var3))) {
            return var2;
         }

         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var13) {
            return var2;
         }
      }
   }

   private int jjMoveStringLiteralDfa0_10() {
      switch (this.curChar) {
         case ']':
            return this.jjMoveStringLiteralDfa1_10(67108864L);
         default:
            return 1;
      }
   }

   private int jjMoveStringLiteralDfa1_10(long var1) {
      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var4) {
         return 1;
      }

      switch (this.curChar) {
         case '=':
            return this.jjMoveStringLiteralDfa2_10(var1, 67108864L);
         default:
            return 2;
      }
   }

   private int jjMoveStringLiteralDfa2_10(long var1, long var3) {
      if ((var3 = var3 & var1) == 0L) {
         return 2;
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            return 2;
         }

         switch (this.curChar) {
            case '=':
               return this.jjMoveStringLiteralDfa3_10(var3, 67108864L);
            default:
               return 3;
         }
      }
   }

   private int jjMoveStringLiteralDfa3_10(long var1, long var3) {
      if ((var3 = var3 & var1) == 0L) {
         return 3;
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            return 3;
         }

         switch (this.curChar) {
            case '=':
               return this.jjMoveStringLiteralDfa4_10(var3, 67108864L);
            default:
               return 4;
         }
      }
   }

   private int jjMoveStringLiteralDfa4_10(long var1, long var3) {
      if ((var3 = var3 & var1) == 0L) {
         return 4;
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            return 4;
         }

         switch (this.curChar) {
            case ']':
               if ((var3 & 67108864L) != 0L) {
                  return this.jjStopAtPos(4, 26);
               }

               return 5;
            default:
               return 5;
         }
      }
   }

   private int jjMoveStringLiteralDfa0_9() {
      switch (this.curChar) {
         case ']':
            return this.jjMoveStringLiteralDfa1_9(33554432L);
         default:
            return 1;
      }
   }

   private int jjMoveStringLiteralDfa1_9(long var1) {
      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var4) {
         return 1;
      }

      switch (this.curChar) {
         case '=':
            return this.jjMoveStringLiteralDfa2_9(var1, 33554432L);
         default:
            return 2;
      }
   }

   private int jjMoveStringLiteralDfa2_9(long var1, long var3) {
      if ((var3 = var3 & var1) == 0L) {
         return 2;
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            return 2;
         }

         switch (this.curChar) {
            case '=':
               return this.jjMoveStringLiteralDfa3_9(var3, 33554432L);
            default:
               return 3;
         }
      }
   }

   private int jjMoveStringLiteralDfa3_9(long var1, long var3) {
      if ((var3 = var3 & var1) == 0L) {
         return 3;
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            return 3;
         }

         switch (this.curChar) {
            case ']':
               if ((var3 & 33554432L) != 0L) {
                  return this.jjStopAtPos(3, 25);
               }

               return 4;
            default:
               return 4;
         }
      }
   }

   private int jjMoveStringLiteralDfa0_8() {
      switch (this.curChar) {
         case ']':
            return this.jjMoveStringLiteralDfa1_8(16777216L);
         default:
            return 1;
      }
   }

   private int jjMoveStringLiteralDfa1_8(long var1) {
      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var4) {
         return 1;
      }

      switch (this.curChar) {
         case '=':
            return this.jjMoveStringLiteralDfa2_8(var1, 16777216L);
         default:
            return 2;
      }
   }

   private int jjMoveStringLiteralDfa2_8(long var1, long var3) {
      if ((var3 = var3 & var1) == 0L) {
         return 2;
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            return 2;
         }

         switch (this.curChar) {
            case ']':
               if ((var3 & 16777216L) != 0L) {
                  return this.jjStopAtPos(2, 24);
               }

               return 3;
            default:
               return 3;
         }
      }
   }

   private int jjMoveStringLiteralDfa0_7() {
      switch (this.curChar) {
         case ']':
            return this.jjMoveStringLiteralDfa1_7(8388608L);
         default:
            return 1;
      }
   }

   private int jjMoveStringLiteralDfa1_7(long var1) {
      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var4) {
         return 1;
      }

      switch (this.curChar) {
         case ']':
            if ((var1 & 8388608L) != 0L) {
               return this.jjStopAtPos(1, 23);
            }

            return 2;
         default:
            return 2;
      }
   }

   private final int jjStopStringLiteralDfa_0(int var1, long var2, long var4) {
      switch (var1) {
         case 0:
            if ((var2 & 30720L) == 0L && (var4 & 8192L) == 0L) {
               if ((var4 & 16810496L) != 0L) {
                  return 31;
               }

               if ((var2 & 2251799276814336L) != 0L) {
                  this.jjmatchedKind = 51;
                  return 17;
               }

               if ((var2 & 66496L) == 0L && (var4 & 524288L) == 0L) {
                  return -1;
               }

               return 7;
            }

            return 14;
         case 1:
            if ((var2 & 66496L) != 0L) {
               return 6;
            } else if ((var2 & 28672L) != 0L) {
               return 13;
            } else if ((var2 & 19243600969728L) != 0L) {
               return 17;
            } else {
               if ((var2 & 2232555675844608L) != 0L) {
                  if (this.jjmatchedPos != 1) {
                     this.jjmatchedKind = 51;
                     this.jjmatchedPos = 1;
                  }

                  return 17;
               }

               return -1;
            }
         case 2:
            if ((var2 & 2219275100094464L) != 0L) {
               this.jjmatchedKind = 51;
               this.jjmatchedPos = 2;
               return 17;
            } else if ((var2 & 24576L) != 0L) {
               return 12;
            } else if ((var2 & 960L) != 0L) {
               return 5;
            } else {
               if ((var2 & 13280575750144L) != 0L) {
                  return 17;
               }

               return -1;
            }
         case 3:
            if ((var2 & 896L) != 0L) {
               return 4;
            } else if ((var2 & 1796774872219648L) != 0L) {
               if (this.jjmatchedPos != 3) {
                  this.jjmatchedKind = 51;
                  this.jjmatchedPos = 3;
               }

               return 17;
            } else if ((var2 & 422500227874816L) != 0L) {
               return 17;
            } else {
               if ((var2 & 16384L) != 0L) {
                  return 9;
               }

               return -1;
            }
         case 4:
            if ((var2 & 105699145154560L) != 0L) {
               this.jjmatchedKind = 51;
               this.jjmatchedPos = 4;
               return 17;
            } else if ((var2 & 768L) != 0L) {
               return 3;
            } else {
               if ((var2 & 1691084316999680L) != 0L) {
                  return 17;
               }

               return -1;
            }
         case 5:
            if ((var2 & 512L) != 0L) {
               return 0;
            } else if ((var2 & 105561706201088L) != 0L) {
               return 17;
            } else {
               if ((var2 & 137438953472L) != 0L) {
                  this.jjmatchedKind = 51;
                  this.jjmatchedPos = 5;
                  return 17;
               }

               return -1;
            }
         case 6:
            if ((var2 & 137438953472L) != 0L) {
               this.jjmatchedKind = 51;
               this.jjmatchedPos = 6;
               return 17;
            }

            return -1;
         default:
            return -1;
      }
   }

   private final int jjStartNfa_0(int var1, long var2, long var4) {
      return this.jjMoveNfa_0(this.jjStopStringLiteralDfa_0(var1, var2, var4), var1 + 1);
   }

   private int jjMoveStringLiteralDfa0_0() {
      switch (this.curChar) {
         case '#':
            return this.jjStopAtPos(0, 69);
         case '$':
         case '&':
         case '\'':
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
         case '\\':
         case '_':
         case '`':
         case 'c':
         case 'h':
         case 'j':
         case 'k':
         case 'm':
         case 'p':
         case 'q':
         case 's':
         case 'v':
         case 'x':
         case 'y':
         case 'z':
         case '|':
         default:
            return this.jjMoveNfa_0(8, 0);
         case '%':
            return this.jjStopAtPos(0, 87);
         case '(':
            return this.jjStopAtPos(0, 75);
         case ')':
            return this.jjStopAtPos(0, 76);
         case '*':
            return this.jjStopAtPos(0, 84);
         case '+':
            return this.jjStopAtPos(0, 82);
         case ',':
            return this.jjStopAtPos(0, 72);
         case '-':
            this.jjmatchedKind = 83;
            return this.jjMoveStringLiteralDfa1_0(66496L, 0L);
         case '.':
            this.jjmatchedKind = 73;
            return this.jjMoveStringLiteralDfa1_0(0L, 16809984L);
         case '/':
            return this.jjStopAtPos(0, 85);
         case ':':
            this.jjmatchedKind = 74;
            return this.jjMoveStringLiteralDfa1_0(0L, 2L);
         case ';':
            return this.jjStopAtPos(0, 70);
         case '<':
            this.jjmatchedKind = 89;
            return this.jjMoveStringLiteralDfa1_0(0L, 67108864L);
         case '=':
            this.jjmatchedKind = 71;
            return this.jjMoveStringLiteralDfa1_0(0L, 536870912L);
         case '>':
            this.jjmatchedKind = 91;
            return this.jjMoveStringLiteralDfa1_0(0L, 268435456L);
         case '[':
            this.jjmatchedKind = 77;
            return this.jjMoveStringLiteralDfa1_0(30720L, 0L);
         case ']':
            return this.jjStopAtPos(0, 78);
         case '^':
            return this.jjStopAtPos(0, 86);
         case 'a':
            return this.jjMoveStringLiteralDfa1_0(536870912L, 0L);
         case 'b':
            return this.jjMoveStringLiteralDfa1_0(1073741824L, 0L);
         case 'd':
            return this.jjMoveStringLiteralDfa1_0(2147483648L, 0L);
         case 'e':
            return this.jjMoveStringLiteralDfa1_0(30064771072L, 0L);
         case 'f':
            return this.jjMoveStringLiteralDfa1_0(240518168576L, 0L);
         case 'g':
            return this.jjMoveStringLiteralDfa1_0(274877906944L, 0L);
         case 'i':
            return this.jjMoveStringLiteralDfa1_0(1649267441664L, 0L);
         case 'l':
            return this.jjMoveStringLiteralDfa1_0(2199023255552L, 0L);
         case 'n':
            return this.jjMoveStringLiteralDfa1_0(13194139533312L, 0L);
         case 'o':
            return this.jjMoveStringLiteralDfa1_0(17592186044416L, 0L);
         case 'r':
            return this.jjMoveStringLiteralDfa1_0(105553116266496L, 0L);
         case 't':
            return this.jjMoveStringLiteralDfa1_0(422212465065984L, 0L);
         case 'u':
            return this.jjMoveStringLiteralDfa1_0(562949953421312L, 0L);
         case 'w':
            return this.jjMoveStringLiteralDfa1_0(1125899906842624L, 0L);
         case '{':
            return this.jjStopAtPos(0, 80);
         case '}':
            return this.jjStopAtPos(0, 81);
         case '~':
            return this.jjMoveStringLiteralDfa1_0(0L, 1073741824L);
      }
   }

   private int jjMoveStringLiteralDfa1_0(long var1, long var3) {
      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var6) {
         this.jjStopStringLiteralDfa_0(0, var1, var3);
         return 1;
      }

      switch (this.curChar) {
         case '-':
            if ((var1 & 65536L) != 0L) {
               this.jjmatchedKind = 16;
               this.jjmatchedPos = 1;
            }

            return this.jjMoveStringLiteralDfa2_0(var1, 960L, var3, 0L);
         case '.':
            if ((var3 & 16777216L) != 0L) {
               this.jjmatchedKind = 88;
               this.jjmatchedPos = 1;
            }

            return this.jjMoveStringLiteralDfa2_0(var1, 0L, var3, 32768L);
         case ':':
            if ((var3 & 2L) != 0L) {
               return this.jjStopAtPos(1, 65);
            }
            break;
         case '=':
            if ((var3 & 67108864L) != 0L) {
               return this.jjStopAtPos(1, 90);
            }

            if ((var3 & 268435456L) != 0L) {
               return this.jjStopAtPos(1, 92);
            }

            if ((var3 & 536870912L) != 0L) {
               return this.jjStopAtPos(1, 93);
            }

            if ((var3 & 1073741824L) != 0L) {
               return this.jjStopAtPos(1, 94);
            }

            return this.jjMoveStringLiteralDfa2_0(var1, 28672L, var3, 0L);
         case '[':
            if ((var1 & 2048L) != 0L) {
               return this.jjStopAtPos(1, 11);
            }
            break;
         case 'a':
            return this.jjMoveStringLiteralDfa2_0(var1, 34359738368L, var3, 0L);
         case 'e':
            return this.jjMoveStringLiteralDfa2_0(var1, 105553116266496L, var3, 0L);
         case 'f':
            if ((var1 & 549755813888L) != 0L) {
               return this.jjStartNfaWithStates_0(1, 39, 17);
            }
            break;
         case 'h':
            return this.jjMoveStringLiteralDfa2_0(var1, 1266637395197952L, var3, 0L);
         case 'i':
            return this.jjMoveStringLiteralDfa2_0(var1, 4398046511104L, var3, 0L);
         case 'l':
            return this.jjMoveStringLiteralDfa2_0(var1, 12884901888L, var3, 0L);
         case 'n':
            if ((var1 & 1099511627776L) != 0L) {
               return this.jjStartNfaWithStates_0(1, 40, 17);
            }

            return this.jjMoveStringLiteralDfa2_0(var1, 562967670161408L, var3, 0L);
         case 'o':
            if ((var1 & 2147483648L) != 0L) {
               return this.jjStartNfaWithStates_0(1, 31, 17);
            }

            return this.jjMoveStringLiteralDfa2_0(var1, 11338713661440L, var3, 0L);
         case 'r':
            if ((var1 & 17592186044416L) != 0L) {
               return this.jjStartNfaWithStates_0(1, 44, 17);
            }

            return this.jjMoveStringLiteralDfa2_0(var1, 281476050452480L, var3, 0L);
         case 'u':
            return this.jjMoveStringLiteralDfa2_0(var1, 137438953472L, var3, 0L);
      }

      return this.jjStartNfa_0(0, var1, var3);
   }

   private int jjMoveStringLiteralDfa2_0(long var1, long var3, long var5, long var7) {
      if (((var3 = var3 & var1) | (var7 = var7 & var5)) == 0L) {
         return this.jjStartNfa_0(0, var1, var5);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var10) {
            this.jjStopStringLiteralDfa_0(1, var3, var7);
            return 2;
         }

         switch (this.curChar) {
            case '.':
               if ((var7 & 32768L) != 0L) {
                  return this.jjStopAtPos(2, 79);
               }
               break;
            case '=':
               return this.jjMoveStringLiteralDfa3_0(var3, 24576L, var7, 0L);
            case '[':
               if ((var3 & 4096L) != 0L) {
                  return this.jjStopAtPos(2, 12);
               }

               return this.jjMoveStringLiteralDfa3_0(var3, 960L, var7, 0L);
            case 'c':
               return this.jjMoveStringLiteralDfa3_0(var3, 2199023255552L, var7, 0L);
            case 'd':
               if ((var3 & 536870912L) != 0L) {
                  return this.jjStartNfaWithStates_0(2, 29, 17);
               }

               if ((var3 & 17179869184L) != 0L) {
                  return this.jjStartNfaWithStates_0(2, 34, 17);
               }
               break;
            case 'e':
               return this.jjMoveStringLiteralDfa3_0(var3, 140738562097152L, var7, 0L);
            case 'i':
               return this.jjMoveStringLiteralDfa3_0(var3, 1125899906842624L, var7, 0L);
            case 'l':
               if ((var3 & 4398046511104L) != 0L) {
                  return this.jjStartNfaWithStates_0(2, 42, 17);
               }

               return this.jjMoveStringLiteralDfa3_0(var3, 34359738368L, var7, 0L);
            case 'n':
               return this.jjMoveStringLiteralDfa3_0(var3, 137438953472L, var7, 0L);
            case 'p':
               return this.jjMoveStringLiteralDfa3_0(var3, 70368744177664L, var7, 0L);
            case 'r':
               if ((var3 & 68719476736L) != 0L) {
                  return this.jjStartNfaWithStates_0(2, 36, 17);
               }
               break;
            case 's':
               return this.jjMoveStringLiteralDfa3_0(var3, 12884901888L, var7, 0L);
            case 't':
               if ((var3 & 8796093022208L) != 0L) {
                  return this.jjStartNfaWithStates_0(2, 43, 17);
               }

               return this.jjMoveStringLiteralDfa3_0(var3, 598409203417088L, var7, 0L);
            case 'u':
               return this.jjMoveStringLiteralDfa3_0(var3, 281474976710656L, var7, 0L);
         }

         return this.jjStartNfa_0(1, var3, var7);
      }
   }

   private int jjMoveStringLiteralDfa3_0(long var1, long var3, long var5, long var7) {
      if (((var3 = var3 & var1) | (var7 = var7 & var5)) == 0L) {
         return this.jjStartNfa_0(1, var1, var5);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var10) {
            this.jjStopStringLiteralDfa_0(2, var3, 0L);
            return 3;
         }

         switch (this.curChar) {
            case '=':
               return this.jjMoveStringLiteralDfa4_0(var3, 17280L);
            case '[':
               if ((var3 & 64L) != 0L) {
                  return this.jjStopAtPos(3, 6);
               }

               if ((var3 & 8192L) != 0L) {
                  return this.jjStopAtPos(3, 13);
               }
               break;
            case 'a':
               return this.jjMoveStringLiteralDfa4_0(var3, 2200096997376L);
            case 'c':
               return this.jjMoveStringLiteralDfa4_0(var3, 137438953472L);
            case 'e':
               if ((var3 & 4294967296L) != 0L) {
                  this.jjmatchedKind = 32;
                  this.jjmatchedPos = 3;
               } else if ((var3 & 281474976710656L) != 0L) {
                  return this.jjStartNfaWithStates_0(3, 48, 17);
               }

               return this.jjMoveStringLiteralDfa4_0(var3, 70377334112256L);
            case 'i':
               return this.jjMoveStringLiteralDfa4_0(var3, 562949953421312L);
            case 'l':
               return this.jjMoveStringLiteralDfa4_0(var3, 1125899906842624L);
            case 'n':
               if ((var3 & 140737488355328L) != 0L) {
                  return this.jjStartNfaWithStates_0(3, 47, 17);
               }
               break;
            case 'o':
               if ((var3 & 274877906944L) != 0L) {
                  return this.jjStartNfaWithStates_0(3, 38, 17);
               }
               break;
            case 's':
               return this.jjMoveStringLiteralDfa4_0(var3, 34359738368L);
            case 'u':
               return this.jjMoveStringLiteralDfa4_0(var3, 35184372088832L);
         }

         return this.jjStartNfa_0(2, var3, 0L);
      }
   }

   private int jjMoveStringLiteralDfa4_0(long var1, long var3) {
      if ((var3 = var3 & var1) == 0L) {
         return this.jjStartNfa_0(2, var1, 0L);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_0(3, var3, 0L);
            return 4;
         }

         switch (this.curChar) {
            case '=':
               return this.jjMoveStringLiteralDfa5_0(var3, 768L);
            case '[':
               if ((var3 & 128L) != 0L) {
                  return this.jjStopAtPos(4, 7);
               }

               if ((var3 & 16384L) != 0L) {
                  return this.jjStopAtPos(4, 14);
               }
               break;
            case 'a':
               return this.jjMoveStringLiteralDfa5_0(var3, 70368744177664L);
            case 'e':
               if ((var3 & 34359738368L) != 0L) {
                  return this.jjStartNfaWithStates_0(4, 35, 17);
               }

               if ((var3 & 1125899906842624L) != 0L) {
                  return this.jjStartNfaWithStates_0(4, 50, 17);
               }
               break;
            case 'i':
               return this.jjMoveStringLiteralDfa5_0(var3, 8589934592L);
            case 'k':
               if ((var3 & 1073741824L) != 0L) {
                  return this.jjStartNfaWithStates_0(4, 30, 17);
               }
               break;
            case 'l':
               if ((var3 & 2199023255552L) != 0L) {
                  return this.jjStartNfaWithStates_0(4, 41, 17);
               }

               if ((var3 & 562949953421312L) != 0L) {
                  return this.jjStartNfaWithStates_0(4, 49, 17);
               }
               break;
            case 'r':
               return this.jjMoveStringLiteralDfa5_0(var3, 35184372088832L);
            case 't':
               return this.jjMoveStringLiteralDfa5_0(var3, 137438953472L);
         }

         return this.jjStartNfa_0(3, var3, 0L);
      }
   }

   private int jjMoveStringLiteralDfa5_0(long var1, long var3) {
      if ((var3 = var3 & var1) == 0L) {
         return this.jjStartNfa_0(3, var1, 0L);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_0(4, var3, 0L);
            return 5;
         }

         switch (this.curChar) {
            case '=':
               return this.jjMoveStringLiteralDfa6_0(var3, 512L);
            case '[':
               if ((var3 & 256L) != 0L) {
                  return this.jjStopAtPos(5, 8);
               }
               break;
            case 'f':
               if ((var3 & 8589934592L) != 0L) {
                  return this.jjStartNfaWithStates_0(5, 33, 17);
               }
               break;
            case 'i':
               return this.jjMoveStringLiteralDfa6_0(var3, 137438953472L);
            case 'n':
               if ((var3 & 35184372088832L) != 0L) {
                  return this.jjStartNfaWithStates_0(5, 45, 17);
               }
               break;
            case 't':
               if ((var3 & 70368744177664L) != 0L) {
                  return this.jjStartNfaWithStates_0(5, 46, 17);
               }
         }

         return this.jjStartNfa_0(4, var3, 0L);
      }
   }

   private int jjMoveStringLiteralDfa6_0(long var1, long var3) {
      if ((var3 = var3 & var1) == 0L) {
         return this.jjStartNfa_0(4, var1, 0L);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_0(5, var3, 0L);
            return 6;
         }

         switch (this.curChar) {
            case '[':
               if ((var3 & 512L) != 0L) {
                  return this.jjStopAtPos(6, 9);
               }
            default:
               return this.jjStartNfa_0(5, var3, 0L);
            case 'o':
               return this.jjMoveStringLiteralDfa7_0(var3, 137438953472L);
         }
      }
   }

   private int jjMoveStringLiteralDfa7_0(long var1, long var3) {
      if ((var3 = var3 & var1) == 0L) {
         return this.jjStartNfa_0(5, var1, 0L);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_0(6, var3, 0L);
            return 7;
         }

         switch (this.curChar) {
            case 'n':
               if ((var3 & 137438953472L) != 0L) {
                  return this.jjStartNfaWithStates_0(7, 37, 17);
               }
            default:
               return this.jjStartNfa_0(6, var3, 0L);
         }
      }
   }

   private int jjStartNfaWithStates_0(int var1, int var2, int var3) {
      this.jjmatchedKind = var2;
      this.jjmatchedPos = var1;

      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var5) {
         return var1 + 1;
      }

      return this.jjMoveNfa_0(var3, var1 + 1);
   }

   private int jjMoveNfa_0(int var1, int var2) {
      int var3 = 0;
      this.jjnewStateCnt = 66;
      int var4 = 1;
      this.jjstateSet[0] = var1;
      int var5 = Integer.MAX_VALUE;

      while (true) {
         if (++this.jjround == Integer.MAX_VALUE) {
            this.ReInitRounds();
         }

         if (this.curChar < '@') {
            long var15 = 1L << this.curChar;

            do {
               var4--;
               switch (this.jjstateSet[var4]) {
                  case 0:
                  case 1:
                     if (this.curChar == '=') {
                        this.jjCheckNAddTwoStates(1, 2);
                     }
                  case 2:
                  case 6:
                  case 11:
                  case 15:
                  case 16:
                  case 19:
                  case 22:
                  case 32:
                  case 38:
                  case 40:
                  case 51:
                  case 53:
                  default:
                     break;
                  case 3:
                     if (this.curChar == '=') {
                        this.jjstateSet[this.jjnewStateCnt++] = 0;
                     }
                     break;
                  case 4:
                     if (this.curChar == '=') {
                        this.jjstateSet[this.jjnewStateCnt++] = 3;
                     }
                     break;
                  case 5:
                     if (this.curChar == '=') {
                        this.jjstateSet[this.jjnewStateCnt++] = 4;
                     }
                     break;
                  case 7:
                     if (this.curChar == '-') {
                        this.jjstateSet[this.jjnewStateCnt++] = 6;
                     }
                     break;
                  case 8:
                     if ((287948901175001088L & var15) != 0L) {
                        if (var5 > 52) {
                           var5 = 52;
                        }

                        this.jjCheckNAddStates(0, 3);
                     } else if (this.curChar == '\'') {
                        this.jjCheckNAddStates(4, 6);
                     } else if (this.curChar == '"') {
                        this.jjCheckNAddStates(7, 9);
                     } else if (this.curChar == '.') {
                        this.jjCheckNAdd(31);
                     } else if (this.curChar == '-') {
                        this.jjstateSet[this.jjnewStateCnt++] = 7;
                     }

                     if (this.curChar == '0') {
                        this.jjstateSet[this.jjnewStateCnt++] = 19;
                     }
                     break;
                  case 9:
                  case 10:
                     if (this.curChar == '=') {
                        this.jjCheckNAddTwoStates(10, 11);
                     }
                     break;
                  case 12:
                     if (this.curChar == '=') {
                        this.jjstateSet[this.jjnewStateCnt++] = 9;
                     }
                     break;
                  case 13:
                     if (this.curChar == '=') {
                        this.jjstateSet[this.jjnewStateCnt++] = 12;
                     }
                     break;
                  case 14:
                     if (this.curChar == '=') {
                        this.jjstateSet[this.jjnewStateCnt++] = 13;
                     }
                     break;
                  case 17:
                     if ((287948901175001088L & var15) != 0L) {
                        if (var5 > 51) {
                           var5 = 51;
                        }

                        this.jjstateSet[this.jjnewStateCnt++] = 17;
                     }
                     break;
                  case 18:
                     if (this.curChar == '0') {
                        this.jjstateSet[this.jjnewStateCnt++] = 19;
                     }
                     break;
                  case 20:
                     if (this.curChar == '.') {
                        this.jjCheckNAdd(21);
                     }
                     break;
                  case 21:
                     if ((287948901175001088L & var15) != 0L) {
                        if (var5 > 52) {
                           var5 = 52;
                        }

                        this.jjCheckNAddTwoStates(21, 22);
                     }
                     break;
                  case 23:
                     if ((43980465111040L & var15) != 0L) {
                        this.jjCheckNAdd(24);
                     }
                     break;
                  case 24:
                     if ((287948901175001088L & var15) != 0L) {
                        if (var5 > 52) {
                           var5 = 52;
                        }

                        this.jjCheckNAdd(24);
                     }
                     break;
                  case 25:
                     if ((287948901175001088L & var15) != 0L) {
                        if (var5 > 52) {
                           var5 = 52;
                        }

                        this.jjCheckNAddStates(10, 13);
                     }
                     break;
                  case 26:
                     if ((287948901175001088L & var15) != 0L) {
                        this.jjCheckNAddTwoStates(26, 27);
                     }
                     break;
                  case 27:
                     if (this.curChar == '.') {
                        if (var5 > 52) {
                           var5 = 52;
                        }

                        this.jjCheckNAddTwoStates(28, 22);
                     }
                     break;
                  case 28:
                     if ((287948901175001088L & var15) != 0L) {
                        if (var5 > 52) {
                           var5 = 52;
                        }

                        this.jjCheckNAddTwoStates(28, 22);
                     }
                     break;
                  case 29:
                     if ((287948901175001088L & var15) != 0L) {
                        if (var5 > 52) {
                           var5 = 52;
                        }

                        this.jjCheckNAddTwoStates(29, 22);
                     }
                     break;
                  case 30:
                     if (this.curChar == '.') {
                        this.jjCheckNAdd(31);
                     }
                     break;
                  case 31:
                     if ((287948901175001088L & var15) != 0L) {
                        if (var5 > 52) {
                           var5 = 52;
                        }

                        this.jjCheckNAddTwoStates(31, 32);
                     }
                     break;
                  case 33:
                     if ((43980465111040L & var15) != 0L) {
                        this.jjCheckNAdd(34);
                     }
                     break;
                  case 34:
                     if ((287948901175001088L & var15) != 0L) {
                        if (var5 > 52) {
                           var5 = 52;
                        }

                        this.jjCheckNAdd(34);
                     }
                     break;
                  case 35:
                     if (this.curChar == '"') {
                        this.jjCheckNAddStates(7, 9);
                     }
                     break;
                  case 36:
                     if ((-17179869185L & var15) != 0L) {
                        this.jjCheckNAddStates(7, 9);
                     }
                     break;
                  case 37:
                     if (this.curChar == '"' && var5 > 61) {
                        var5 = 61;
                     }
                     break;
                  case 39:
                     this.jjCheckNAddStates(7, 9);
                     break;
                  case 41:
                     if ((287948901175001088L & var15) != 0L) {
                        this.jjstateSet[this.jjnewStateCnt++] = 42;
                     }
                     break;
                  case 42:
                     if ((287948901175001088L & var15) != 0L) {
                        this.jjstateSet[this.jjnewStateCnt++] = 43;
                     }
                     break;
                  case 43:
                     if ((287948901175001088L & var15) != 0L) {
                        this.jjstateSet[this.jjnewStateCnt++] = 44;
                     }
                     break;
                  case 44:
                  case 47:
                     if ((287948901175001088L & var15) != 0L) {
                        this.jjCheckNAddStates(7, 9);
                     }
                     break;
                  case 45:
                     if ((287948901175001088L & var15) != 0L) {
                        this.jjCheckNAddStates(14, 17);
                     }
                     break;
                  case 46:
                     if ((287948901175001088L & var15) != 0L) {
                        this.jjCheckNAddStates(18, 21);
                     }
                     break;
                  case 48:
                     if (this.curChar == '\'') {
                        this.jjCheckNAddStates(4, 6);
                     }
                     break;
                  case 49:
                     if ((-549755813889L & var15) != 0L) {
                        this.jjCheckNAddStates(4, 6);
                     }
                     break;
                  case 50:
                     if (this.curChar == '\'' && var5 > 62) {
                        var5 = 62;
                     }
                     break;
                  case 52:
                     this.jjCheckNAddStates(4, 6);
                     break;
                  case 54:
                     if ((287948901175001088L & var15) != 0L) {
                        this.jjstateSet[this.jjnewStateCnt++] = 55;
                     }
                     break;
                  case 55:
                     if ((287948901175001088L & var15) != 0L) {
                        this.jjstateSet[this.jjnewStateCnt++] = 56;
                     }
                     break;
                  case 56:
                     if ((287948901175001088L & var15) != 0L) {
                        this.jjstateSet[this.jjnewStateCnt++] = 57;
                     }
                     break;
                  case 57:
                  case 60:
                     if ((287948901175001088L & var15) != 0L) {
                        this.jjCheckNAddStates(4, 6);
                     }
                     break;
                  case 58:
                     if ((287948901175001088L & var15) != 0L) {
                        this.jjCheckNAddStates(22, 25);
                     }
                     break;
                  case 59:
                     if ((287948901175001088L & var15) != 0L) {
                        this.jjCheckNAddStates(26, 29);
                     }
                     break;
                  case 61:
                     if ((287948901175001088L & var15) != 0L) {
                        if (var5 > 52) {
                           var5 = 52;
                        }

                        this.jjCheckNAddStates(0, 3);
                     }
                     break;
                  case 62:
                     if ((287948901175001088L & var15) != 0L) {
                        this.jjCheckNAddTwoStates(62, 63);
                     }
                     break;
                  case 63:
                     if (this.curChar == '.') {
                        if (var5 > 52) {
                           var5 = 52;
                        }

                        this.jjCheckNAddTwoStates(64, 32);
                     }
                     break;
                  case 64:
                     if ((287948901175001088L & var15) != 0L) {
                        if (var5 > 52) {
                           var5 = 52;
                        }

                        this.jjCheckNAddTwoStates(64, 32);
                     }
                     break;
                  case 65:
                     if ((287948901175001088L & var15) != 0L) {
                        if (var5 > 52) {
                           var5 = 52;
                        }

                        this.jjCheckNAddTwoStates(65, 32);
                     }
               }
            } while (var4 != var3);
         } else if (this.curChar < 128) {
            long var14 = 1L << (this.curChar & '?');

            do {
               var4--;
               switch (this.jjstateSet[var4]) {
                  case 2:
                     if (this.curChar == '[' && var5 > 10) {
                        var5 = 10;
                     }
                  case 3:
                  case 4:
                  case 5:
                  case 7:
                  case 9:
                  case 10:
                  case 12:
                  case 13:
                  case 14:
                  case 18:
                  case 20:
                  case 23:
                  case 24:
                  case 27:
                  case 30:
                  case 31:
                  case 33:
                  case 34:
                  case 35:
                  case 37:
                  case 45:
                  case 46:
                  case 47:
                  case 48:
                  case 50:
                  default:
                     break;
                  case 6:
                     if (this.curChar == '[') {
                        this.jjstateSet[this.jjnewStateCnt++] = 5;
                     }
                     break;
                  case 8:
                     if ((576460745995190270L & var14) != 0L) {
                        if (var5 > 51) {
                           var5 = 51;
                        }

                        this.jjCheckNAdd(17);
                     } else if (this.curChar == '[') {
                        this.jjstateSet[this.jjnewStateCnt++] = 14;
                     }
                     break;
                  case 11:
                     if (this.curChar == '[' && var5 > 15) {
                        var5 = 15;
                     }
                     break;
                  case 15:
                     if (this.curChar == '[') {
                        this.jjstateSet[this.jjnewStateCnt++] = 14;
                     }
                     break;
                  case 16:
                  case 17:
                     if ((576460745995190270L & var14) != 0L) {
                        if (var5 > 51) {
                           var5 = 51;
                        }

                        this.jjCheckNAdd(17);
                     }
                     break;
                  case 19:
                     if ((72057594054705152L & var14) != 0L) {
                        this.jjAddStates(30, 31);
                     }
                     break;
                  case 21:
                     if ((541165879422L & var14) != 0L) {
                        if (var5 > 52) {
                           var5 = 52;
                        }

                        this.jjCheckNAddTwoStates(21, 22);
                     }
                     break;
                  case 22:
                     if ((281612415729696L & var14) != 0L) {
                        this.jjAddStates(32, 33);
                     }
                     break;
                  case 25:
                     if ((541165879422L & var14) != 0L) {
                        if (var5 > 52) {
                           var5 = 52;
                        }

                        this.jjCheckNAddStates(10, 13);
                     }
                     break;
                  case 26:
                     if ((541165879422L & var14) != 0L) {
                        this.jjCheckNAddTwoStates(26, 27);
                     }
                     break;
                  case 28:
                     if ((541165879422L & var14) != 0L) {
                        if (var5 > 52) {
                           var5 = 52;
                        }

                        this.jjCheckNAddTwoStates(28, 22);
                     }
                     break;
                  case 29:
                     if ((541165879422L & var14) != 0L) {
                        if (var5 > 52) {
                           var5 = 52;
                        }

                        this.jjCheckNAddTwoStates(29, 22);
                     }
                     break;
                  case 32:
                     if ((137438953504L & var14) != 0L) {
                        this.jjAddStates(34, 35);
                     }
                     break;
                  case 36:
                     if ((-268435457L & var14) != 0L) {
                        this.jjCheckNAddStates(7, 9);
                     }
                     break;
                  case 38:
                     if (this.curChar == '\\') {
                        this.jjAddStates(36, 38);
                     }
                     break;
                  case 39:
                     this.jjCheckNAddStates(7, 9);
                     break;
                  case 40:
                     if (this.curChar == 'u') {
                        this.jjstateSet[this.jjnewStateCnt++] = 41;
                     }
                     break;
                  case 41:
                     if ((541165879422L & var14) != 0L) {
                        this.jjstateSet[this.jjnewStateCnt++] = 42;
                     }
                     break;
                  case 42:
                     if ((541165879422L & var14) != 0L) {
                        this.jjstateSet[this.jjnewStateCnt++] = 43;
                     }
                     break;
                  case 43:
                     if ((541165879422L & var14) != 0L) {
                        this.jjstateSet[this.jjnewStateCnt++] = 44;
                     }
                     break;
                  case 44:
                     if ((541165879422L & var14) != 0L) {
                        this.jjCheckNAddStates(7, 9);
                     }
                     break;
                  case 49:
                     if ((-268435457L & var14) != 0L) {
                        this.jjCheckNAddStates(4, 6);
                     }
                     break;
                  case 51:
                     if (this.curChar == '\\') {
                        this.jjAddStates(39, 41);
                     }
                     break;
                  case 52:
                     this.jjCheckNAddStates(4, 6);
                     break;
                  case 53:
                     if (this.curChar == 'u') {
                        this.jjstateSet[this.jjnewStateCnt++] = 54;
                     }
                     break;
                  case 54:
                     if ((541165879422L & var14) != 0L) {
                        this.jjstateSet[this.jjnewStateCnt++] = 55;
                     }
                     break;
                  case 55:
                     if ((541165879422L & var14) != 0L) {
                        this.jjstateSet[this.jjnewStateCnt++] = 56;
                     }
                     break;
                  case 56:
                     if ((541165879422L & var14) != 0L) {
                        this.jjstateSet[this.jjnewStateCnt++] = 57;
                     }
                     break;
                  case 57:
                     if ((541165879422L & var14) != 0L) {
                        this.jjCheckNAddStates(4, 6);
                     }
               }
            } while (var4 != var3);
         } else {
            int var6 = this.curChar >> '\b';
            int var7 = var6 >> 6;
            long var8 = 1L << (var6 & 63);
            int var10 = (this.curChar & 255) >> 6;
            long var11 = 1L << (this.curChar & '?');

            do {
               var4--;
               switch (this.jjstateSet[var4]) {
                  case 36:
                  case 39:
                     if (jjCanMove_0(var6, var7, var10, var8, var11)) {
                        this.jjCheckNAddStates(7, 9);
                     }
                     break;
                  case 49:
                  case 52:
                     if (jjCanMove_0(var6, var7, var10, var8, var11)) {
                        this.jjCheckNAddStates(4, 6);
                     }
               }
            } while (var4 != var3);
         }

         if (var5 != Integer.MAX_VALUE) {
            this.jjmatchedKind = var5;
            this.jjmatchedPos = var2;
            var5 = Integer.MAX_VALUE;
         }

         var2++;
         if ((var4 = this.jjnewStateCnt) == (var3 = 66 - (this.jjnewStateCnt = var3))) {
            return var2;
         }

         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var13) {
            return var2;
         }
      }
   }

   private int jjMoveStringLiteralDfa0_1() {
      return this.jjMoveNfa_1(4, 0);
   }

   private int jjMoveNfa_1(int var1, int var2) {
      int var3 = 0;
      this.jjnewStateCnt = 4;
      int var4 = 1;
      this.jjstateSet[0] = var1;
      int var5 = Integer.MAX_VALUE;

      while (true) {
         if (++this.jjround == Integer.MAX_VALUE) {
            this.ReInitRounds();
         }

         if (this.curChar < '@') {
            long var15 = 1L << this.curChar;

            do {
               var4--;
               switch (this.jjstateSet[var4]) {
                  case 0:
                     if ((-9217L & var15) != 0L) {
                        var5 = 17;
                        this.jjCheckNAddStates(42, 44);
                     }
                     break;
                  case 1:
                     if ((9216L & var15) != 0L && var5 > 17) {
                        var5 = 17;
                     }
                     break;
                  case 2:
                     if (this.curChar == '\n' && var5 > 17) {
                        var5 = 17;
                     }
                     break;
                  case 3:
                     if (this.curChar == '\r') {
                        this.jjstateSet[this.jjnewStateCnt++] = 2;
                     }
                     break;
                  case 4:
                     if ((-9217L & var15) != 0L) {
                        if (var5 > 17) {
                           var5 = 17;
                        }

                        this.jjCheckNAddStates(42, 44);
                     } else if ((9216L & var15) != 0L && var5 > 17) {
                        var5 = 17;
                     }

                     if (this.curChar == '\r') {
                        this.jjstateSet[this.jjnewStateCnt++] = 2;
                     }
               }
            } while (var4 != var3);
         } else if (this.curChar < 128) {
            long var14 = 1L << (this.curChar & '?');

            do {
               var4--;
               switch (this.jjstateSet[var4]) {
                  case 0:
                  case 4:
                     var5 = 17;
                     this.jjCheckNAddStates(42, 44);
               }
            } while (var4 != var3);
         } else {
            int var6 = this.curChar >> '\b';
            int var7 = var6 >> 6;
            long var8 = 1L << (var6 & 63);
            int var10 = (this.curChar & 255) >> 6;
            long var11 = 1L << (this.curChar & '?');

            do {
               var4--;
               switch (this.jjstateSet[var4]) {
                  case 0:
                  case 4:
                     if (jjCanMove_0(var6, var7, var10, var8, var11)) {
                        if (var5 > 17) {
                           var5 = 17;
                        }

                        this.jjCheckNAddStates(42, 44);
                     }
               }
            } while (var4 != var3);
         }

         if (var5 != Integer.MAX_VALUE) {
            this.jjmatchedKind = var5;
            this.jjmatchedPos = var2;
            var5 = Integer.MAX_VALUE;
         }

         var2++;
         if ((var4 = this.jjnewStateCnt) == (var3 = 4 - (this.jjnewStateCnt = var3))) {
            return var2;
         }

         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var13) {
            return var2;
         }
      }
   }

   private int jjMoveStringLiteralDfa0_6() {
      return this.jjMoveNfa_6(6, 0);
   }

   private int jjMoveNfa_6(int var1, int var2) {
      int var3 = 0;
      this.jjnewStateCnt = 7;
      int var4 = 1;
      this.jjstateSet[0] = var1;
      int var5 = Integer.MAX_VALUE;

      while (true) {
         if (++this.jjround == Integer.MAX_VALUE) {
            this.ReInitRounds();
         }

         if (this.curChar < '@') {
            long var15 = 1L << this.curChar;

            do {
               var4--;
               switch (this.jjstateSet[var4]) {
                  case 0:
                  case 1:
                     if (this.curChar == '=') {
                        this.jjCheckNAddTwoStates(1, 2);
                     }
                  case 2:
                  default:
                     break;
                  case 3:
                     if (this.curChar == '=') {
                        this.jjstateSet[this.jjnewStateCnt++] = 0;
                     }
                     break;
                  case 4:
                     if (this.curChar == '=') {
                        this.jjstateSet[this.jjnewStateCnt++] = 3;
                     }
                     break;
                  case 5:
                     if (this.curChar == '=') {
                        this.jjstateSet[this.jjnewStateCnt++] = 4;
                     }
               }
            } while (var4 != var3);
         } else if (this.curChar < 128) {
            long var14 = 1L << (this.curChar & '?');

            do {
               var4--;
               switch (this.jjstateSet[var4]) {
                  case 2:
                     if (this.curChar == ']' && var5 > 22) {
                        var5 = 22;
                     }
                     break;
                  case 6:
                     if (this.curChar == ']') {
                        this.jjstateSet[this.jjnewStateCnt++] = 5;
                     }
               }
            } while (var4 != var3);
         } else {
            int var6 = this.curChar >> '\b';
            int var7 = var6 >> 6;
            long var8 = 1L << (var6 & 63);
            int var10 = (this.curChar & 255) >> 6;
            long var11 = 1L << (this.curChar & '?');

            do {
               var4--;
               switch (this.jjstateSet[var4]) {
               }
            } while (var4 != var3);
         }

         if (var5 != Integer.MAX_VALUE) {
            this.jjmatchedKind = var5;
            this.jjmatchedPos = var2;
            var5 = Integer.MAX_VALUE;
         }

         var2++;
         if ((var4 = this.jjnewStateCnt) == (var3 = 7 - (this.jjnewStateCnt = var3))) {
            return var2;
         }

         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var13) {
            return var2;
         }
      }
   }

   private int jjMoveStringLiteralDfa0_5() {
      switch (this.curChar) {
         case ']':
            return this.jjMoveStringLiteralDfa1_5(2097152L);
         default:
            return 1;
      }
   }

   private int jjMoveStringLiteralDfa1_5(long var1) {
      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var4) {
         return 1;
      }

      switch (this.curChar) {
         case '=':
            return this.jjMoveStringLiteralDfa2_5(var1, 2097152L);
         default:
            return 2;
      }
   }

   private int jjMoveStringLiteralDfa2_5(long var1, long var3) {
      if ((var3 = var3 & var1) == 0L) {
         return 2;
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            return 2;
         }

         switch (this.curChar) {
            case '=':
               return this.jjMoveStringLiteralDfa3_5(var3, 2097152L);
            default:
               return 3;
         }
      }
   }

   private int jjMoveStringLiteralDfa3_5(long var1, long var3) {
      if ((var3 = var3 & var1) == 0L) {
         return 3;
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            return 3;
         }

         switch (this.curChar) {
            case '=':
               return this.jjMoveStringLiteralDfa4_5(var3, 2097152L);
            default:
               return 4;
         }
      }
   }

   private int jjMoveStringLiteralDfa4_5(long var1, long var3) {
      if ((var3 = var3 & var1) == 0L) {
         return 4;
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            return 4;
         }

         switch (this.curChar) {
            case ']':
               if ((var3 & 2097152L) != 0L) {
                  return this.jjStopAtPos(4, 21);
               }

               return 5;
            default:
               return 5;
         }
      }
   }

   private int jjMoveStringLiteralDfa0_4() {
      switch (this.curChar) {
         case ']':
            return this.jjMoveStringLiteralDfa1_4(1048576L);
         default:
            return 1;
      }
   }

   private int jjMoveStringLiteralDfa1_4(long var1) {
      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var4) {
         return 1;
      }

      switch (this.curChar) {
         case '=':
            return this.jjMoveStringLiteralDfa2_4(var1, 1048576L);
         default:
            return 2;
      }
   }

   private int jjMoveStringLiteralDfa2_4(long var1, long var3) {
      if ((var3 = var3 & var1) == 0L) {
         return 2;
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            return 2;
         }

         switch (this.curChar) {
            case '=':
               return this.jjMoveStringLiteralDfa3_4(var3, 1048576L);
            default:
               return 3;
         }
      }
   }

   private int jjMoveStringLiteralDfa3_4(long var1, long var3) {
      if ((var3 = var3 & var1) == 0L) {
         return 3;
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            return 3;
         }

         switch (this.curChar) {
            case ']':
               if ((var3 & 1048576L) != 0L) {
                  return this.jjStopAtPos(3, 20);
               }

               return 4;
            default:
               return 4;
         }
      }
   }

   private int jjMoveStringLiteralDfa0_3() {
      switch (this.curChar) {
         case ']':
            return this.jjMoveStringLiteralDfa1_3(524288L);
         default:
            return 1;
      }
   }

   private int jjMoveStringLiteralDfa1_3(long var1) {
      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var4) {
         return 1;
      }

      switch (this.curChar) {
         case '=':
            return this.jjMoveStringLiteralDfa2_3(var1, 524288L);
         default:
            return 2;
      }
   }

   private int jjMoveStringLiteralDfa2_3(long var1, long var3) {
      if ((var3 = var3 & var1) == 0L) {
         return 2;
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            return 2;
         }

         switch (this.curChar) {
            case ']':
               if ((var3 & 524288L) != 0L) {
                  return this.jjStopAtPos(2, 19);
               }

               return 3;
            default:
               return 3;
         }
      }
   }

   private static final boolean jjCanMove_0(int var0, int var1, int var2, long var3, long var5) {
      switch (var0) {
         case 0:
            return (jjbitVec2[var2] & var5) != 0L;
         default:
            return (jjbitVec0[var1] & var3) != 0L;
      }
   }

   public LuaParserTokenManager(SimpleCharStream var1) {
      this.input_stream = var1;
   }

   public LuaParserTokenManager(SimpleCharStream var1, int var2) {
      this(var1);
      this.SwitchTo(var2);
   }

   public void ReInit(SimpleCharStream var1) {
      this.jjmatchedPos = this.jjnewStateCnt = 0;
      this.curLexState = this.defaultLexState;
      this.input_stream = var1;
      this.ReInitRounds();
   }

   private void ReInitRounds() {
      this.jjround = -2147483647;
      int var1 = 66;

      while (var1-- > 0) {
         this.jjrounds[var1] = Integer.MIN_VALUE;
      }
   }

   public void ReInit(SimpleCharStream var1, int var2) {
      this.ReInit(var1);
      this.SwitchTo(var2);
   }

   public void SwitchTo(int var1) {
      if (var1 < 12 && var1 >= 0) {
         this.curLexState = var1;
      } else {
         throw new TokenMgrError("Error: Ignoring invalid lexical state : " + var1 + ". State unchanged.", 2);
      }
   }

   protected Token jjFillToken() {
      String var2;
      int var3;
      int var4;
      int var5;
      int var6;
      if (this.jjmatchedPos < 0) {
         if (this.image == null) {
            var2 = "";
         } else {
            var2 = this.image.toString();
         }

         var3 = var4 = this.input_stream.getBeginLine();
         var5 = var6 = this.input_stream.getBeginColumn();
      } else {
         String var7 = jjstrLiteralImages[this.jjmatchedKind];
         var2 = var7 == null ? this.input_stream.GetImage() : var7;
         var3 = this.input_stream.getBeginLine();
         var5 = this.input_stream.getBeginColumn();
         var4 = this.input_stream.getEndLine();
         var6 = this.input_stream.getEndColumn();
      }

      Token var1 = Token.newToken(this.jjmatchedKind, var2);
      var1.beginLine = var3;
      var1.endLine = var4;
      var1.beginColumn = var5;
      var1.endColumn = var6;
      return var1;
   }

   public Token getNextToken() {
      Token var1 = null;
      int var3 = 0;

      label163:
      while (true) {
         try {
            this.curChar = this.input_stream.BeginToken();
         } catch (IOException var9) {
            this.jjmatchedKind = 0;
            Token var2 = this.jjFillToken();
            var2.specialToken = var1;
            return var2;
         }

         this.image = this.jjimage;
         this.image.setLength(0);
         this.jjimageLen = 0;

         while (true) {
            switch (this.curLexState) {
               case 0:
                  try {
                     this.input_stream.backup(0);

                     while (this.curChar <= ' ' && (4294981120L & 1L << this.curChar) != 0L) {
                        this.curChar = this.input_stream.BeginToken();
                     }
                  } catch (IOException var12) {
                     continue label163;
                  }

                  this.jjmatchedKind = Integer.MAX_VALUE;
                  this.jjmatchedPos = 0;
                  var3 = this.jjMoveStringLiteralDfa0_0();
                  break;
               case 1:
                  this.jjmatchedKind = 17;
                  this.jjmatchedPos = -1;
                  boolean var15 = false;
                  var3 = this.jjMoveStringLiteralDfa0_1();
                  break;
               case 2:
                  this.jjmatchedKind = Integer.MAX_VALUE;
                  this.jjmatchedPos = 0;
                  var3 = this.jjMoveStringLiteralDfa0_2();
                  if (this.jjmatchedPos == 0 && this.jjmatchedKind > 28) {
                     this.jjmatchedKind = 28;
                  }
                  break;
               case 3:
                  this.jjmatchedKind = Integer.MAX_VALUE;
                  this.jjmatchedPos = 0;
                  var3 = this.jjMoveStringLiteralDfa0_3();
                  if (this.jjmatchedPos == 0 && this.jjmatchedKind > 28) {
                     this.jjmatchedKind = 28;
                  }
                  break;
               case 4:
                  this.jjmatchedKind = Integer.MAX_VALUE;
                  this.jjmatchedPos = 0;
                  var3 = this.jjMoveStringLiteralDfa0_4();
                  if (this.jjmatchedPos == 0 && this.jjmatchedKind > 28) {
                     this.jjmatchedKind = 28;
                  }
                  break;
               case 5:
                  this.jjmatchedKind = Integer.MAX_VALUE;
                  this.jjmatchedPos = 0;
                  var3 = this.jjMoveStringLiteralDfa0_5();
                  if (this.jjmatchedPos == 0 && this.jjmatchedKind > 28) {
                     this.jjmatchedKind = 28;
                  }
                  break;
               case 6:
                  this.jjmatchedKind = Integer.MAX_VALUE;
                  this.jjmatchedPos = 0;
                  var3 = this.jjMoveStringLiteralDfa0_6();
                  if (this.jjmatchedPos == 0 && this.jjmatchedKind > 28) {
                     this.jjmatchedKind = 28;
                  }
                  break;
               case 7:
                  this.jjmatchedKind = Integer.MAX_VALUE;
                  this.jjmatchedPos = 0;
                  var3 = this.jjMoveStringLiteralDfa0_7();
                  if (this.jjmatchedPos == 0 && this.jjmatchedKind > 28) {
                     this.jjmatchedKind = 28;
                  }
                  break;
               case 8:
                  this.jjmatchedKind = Integer.MAX_VALUE;
                  this.jjmatchedPos = 0;
                  var3 = this.jjMoveStringLiteralDfa0_8();
                  if (this.jjmatchedPos == 0 && this.jjmatchedKind > 28) {
                     this.jjmatchedKind = 28;
                  }
                  break;
               case 9:
                  this.jjmatchedKind = Integer.MAX_VALUE;
                  this.jjmatchedPos = 0;
                  var3 = this.jjMoveStringLiteralDfa0_9();
                  if (this.jjmatchedPos == 0 && this.jjmatchedKind > 28) {
                     this.jjmatchedKind = 28;
                  }
                  break;
               case 10:
                  this.jjmatchedKind = Integer.MAX_VALUE;
                  this.jjmatchedPos = 0;
                  var3 = this.jjMoveStringLiteralDfa0_10();
                  if (this.jjmatchedPos == 0 && this.jjmatchedKind > 28) {
                     this.jjmatchedKind = 28;
                  }
                  break;
               case 11:
                  this.jjmatchedKind = Integer.MAX_VALUE;
                  this.jjmatchedPos = 0;
                  var3 = this.jjMoveStringLiteralDfa0_11();
                  if (this.jjmatchedPos == 0 && this.jjmatchedKind > 28) {
                     this.jjmatchedKind = 28;
                  }
            }

            if (this.jjmatchedKind == Integer.MAX_VALUE) {
               break label163;
            }

            if (this.jjmatchedPos + 1 < var3) {
               this.input_stream.backup(var3 - this.jjmatchedPos - 1);
            }

            if ((jjtoToken[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 63)) != 0L) {
               Token var14 = this.jjFillToken();
               var14.specialToken = var1;
               if (jjnewLexState[this.jjmatchedKind] != -1) {
                  this.curLexState = jjnewLexState[this.jjmatchedKind];
               }

               return var14;
            }

            if ((jjtoSkip[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 63)) != 0L) {
               if ((jjtoSpecial[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 63)) != 0L) {
                  Token var13 = this.jjFillToken();
                  if (var1 == null) {
                     var1 = var13;
                  } else {
                     var13.specialToken = var1;
                     var1 = var1.next = var13;
                  }

                  this.SkipLexicalActions(var13);
               } else {
                  this.SkipLexicalActions(null);
               }

               if (jjnewLexState[this.jjmatchedKind] != -1) {
                  this.curLexState = jjnewLexState[this.jjmatchedKind];
               }
               break;
            }

            this.jjimageLen = this.jjimageLen + this.jjmatchedPos + 1;
            if (jjnewLexState[this.jjmatchedKind] != -1) {
               this.curLexState = jjnewLexState[this.jjmatchedKind];
            }

            var3 = 0;
            this.jjmatchedKind = Integer.MAX_VALUE;

            try {
               this.curChar = this.input_stream.readChar();
            } catch (IOException var11) {
               break label163;
            }
         }
      }

      int var4 = this.input_stream.getEndLine();
      int var5 = this.input_stream.getEndColumn();
      String var6 = null;
      boolean var7 = false;

      try {
         this.input_stream.readChar();
         this.input_stream.backup(1);
      } catch (IOException var10) {
         var7 = true;
         var6 = var3 <= 1 ? "" : this.input_stream.GetImage();
         if (this.curChar != '\n' && this.curChar != '\r') {
            var5++;
         } else {
            var4++;
            var5 = 0;
         }
      }

      if (!var7) {
         this.input_stream.backup(1);
         var6 = var3 <= 1 ? "" : this.input_stream.GetImage();
      }

      throw new TokenMgrError(var7, this.curLexState, var4, var5, var6, this.curChar, 0);
   }

   void SkipLexicalActions(Token var1) {
      switch (this.jjmatchedKind) {
      }
   }

   private void jjCheckNAdd(int var1) {
      if (this.jjrounds[var1] != this.jjround) {
         this.jjstateSet[this.jjnewStateCnt++] = var1;
         this.jjrounds[var1] = this.jjround;
      }
   }

   private void jjAddStates(int var1, int var2) {
      do {
         this.jjstateSet[this.jjnewStateCnt++] = jjnextStates[var1];
      } while (var1++ != var2);
   }

   private void jjCheckNAddTwoStates(int var1, int var2) {
      this.jjCheckNAdd(var1);
      this.jjCheckNAdd(var2);
   }

   private void jjCheckNAddStates(int var1, int var2) {
      do {
         this.jjCheckNAdd(jjnextStates[var1]);
      } while (var1++ != var2);
   }
}
