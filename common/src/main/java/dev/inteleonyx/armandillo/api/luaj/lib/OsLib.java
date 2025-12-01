package dev.inteleonyx.armandillo.api.luaj.lib;

import dev.inteleonyx.armandillo.api.luaj.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class OsLib extends TwoArgFunction {
   public static final String TMP_PREFIX = ".luaj";
   public static final String TMP_SUFFIX = "tmp";
   private static final int CLOCK = 0;
   private static final int DATE = 1;
   private static final int DIFFTIME = 2;
   private static final int EXECUTE = 3;
   private static final int EXIT = 4;
   private static final int GETENV = 5;
   private static final int REMOVE = 6;
   private static final int RENAME = 7;
   private static final int SETLOCALE = 8;
   private static final int TIME = 9;
   private static final int TMPNAME = 10;
   private static final String[] NAMES = new String[]{
      "clock", "date", "difftime", "execute", "exit", "getenv", "remove", "rename", "setlocale", "time", "tmpname"
   };
   private static final long t0 = System.currentTimeMillis();
   private static long tmpnames = t0;
   protected Globals globals;
   private static final String[] WeekdayNameAbbrev = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
   private static final String[] WeekdayName = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
   private static final String[] MonthNameAbbrev = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
   private static final String[] MonthName = new String[]{
      "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
   };

   public LuaValue call(LuaValue var1, LuaValue var2) {
      this.globals = var2.checkglobals();
      LuaTable var3 = new LuaTable();

      for (int var4 = 0; var4 < NAMES.length; var4++) {
         var3.set(NAMES[var4], new OsLibFunc(var4, NAMES[var4]));
      }

      var2.set("os", var3);
      if (!var2.get("package").isnil()) {
         var2.get("package").get("loaded").set("os", var3);
      }

      return var3;
   }

   protected double clock() {
      return (System.currentTimeMillis() - t0) / 1000.0;
   }

   protected double difftime(double var1, double var3) {
      return var1 - var3;
   }

   public String date(String var1, double var2) {
      Calendar var4 = Calendar.getInstance();
      var4.setTime(new Date((long)(var2 * 1000.0)));
      if (var1.startsWith("!")) {
         var2 -= this.timeZoneOffset(var4);
         var4.setTime(new Date((long)(var2 * 1000.0)));
         var1 = var1.substring(1);
      }

      byte[] var5 = var1.getBytes();
      int var6 = var5.length;
      Buffer var7 = new Buffer(var6);
      int var9 = 0;

      while (var9 < var6) {
         byte var8;
         switch (var8 = var5[var9++]) {
            case 10:
               var7.append("\n");
               break;
            case 37:
               if (var9 < var6) {
                  switch (var8 = var5[var9++]) {
                     case 37:
                        var7.append((byte)37);
                        break;
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
                     case 59:
                     case 60:
                     case 61:
                     case 62:
                     case 63:
                     case 64:
                     case 67:
                     case 68:
                     case 69:
                     case 70:
                     case 71:
                     case 74:
                     case 75:
                     case 76:
                     case 78:
                     case 79:
                     case 80:
                     case 81:
                     case 82:
                     case 84:
                     case 86:
                     case 90:
                     case 91:
                     case 92:
                     case 93:
                     case 94:
                     case 95:
                     case 96:
                     case 101:
                     case 102:
                     case 103:
                     case 104:
                     case 105:
                     case 107:
                     case 108:
                     case 110:
                     case 111:
                     case 113:
                     case 114:
                     case 115:
                     case 116:
                     case 117:
                     case 118:
                     default:
                        LuaValue.argerror(1, "invalid conversion specifier '%" + var8 + "'");
                        break;
                     case 65:
                        var7.append(WeekdayName[var4.get(7) - 1]);
                        break;
                     case 66:
                        var7.append(MonthName[var4.get(2)]);
                        break;
                     case 72:
                        var7.append(String.valueOf(100 + var4.get(11)).substring(1));
                        break;
                     case 73:
                        var7.append(String.valueOf(100 + var4.get(11) % 12).substring(1));
                        break;
                     case 77:
                        var7.append(String.valueOf(100 + var4.get(12)).substring(1));
                        break;
                     case 83:
                        var7.append(String.valueOf(100 + var4.get(13)).substring(1));
                        break;
                     case 85:
                        var7.append(String.valueOf(this.weekNumber(var4, 0)));
                        break;
                     case 87:
                        var7.append(String.valueOf(this.weekNumber(var4, 1)));
                        break;
                     case 88:
                        var7.append(this.date("%H:%M:%S", var2));
                        break;
                     case 89:
                        var7.append(String.valueOf(var4.get(1)));
                        break;
                     case 97:
                        var7.append(WeekdayNameAbbrev[var4.get(7) - 1]);
                        break;
                     case 98:
                        var7.append(MonthNameAbbrev[var4.get(2)]);
                        break;
                     case 99:
                        var7.append(this.date("%a %b %d %H:%M:%S %Y", var2));
                        break;
                     case 100:
                        var7.append(String.valueOf(100 + var4.get(5)).substring(1));
                        break;
                     case 106:
                        Calendar var15 = this.beginningOfYear(var4);
                        int var16 = (int)((var4.getTime().getTime() - var15.getTime().getTime()) / 86400000L);
                        var7.append(String.valueOf(1001 + var16).substring(1));
                        break;
                     case 109:
                        var7.append(String.valueOf(101 + var4.get(2)).substring(1));
                        break;
                     case 112:
                        var7.append(var4.get(11) < 12 ? "AM" : "PM");
                        break;
                     case 119:
                        var7.append(String.valueOf((var4.get(7) + 6) % 7));
                        break;
                     case 120:
                        var7.append(this.date("%m/%d/%y", var2));
                        break;
                     case 121:
                        var7.append(String.valueOf(var4.get(1)).substring(2));
                        break;
                     case 122:
                        int var10 = this.timeZoneOffset(var4) / 60;
                        int var11 = Math.abs(var10);
                        String var12 = String.valueOf(100 + var11 / 60).substring(1);
                        String var13 = String.valueOf(100 + var11 % 60).substring(1);
                        var7.append((var10 >= 0 ? "+" : "-") + var12 + var13);
                  }
               }
               break;
            default:
               var7.append(var8);
         }
      }

      return var7.tojstring();
   }

   private Calendar beginningOfYear(Calendar var1) {
      Calendar var2 = Calendar.getInstance();
      var2.setTime(var1.getTime());
      var2.set(2, 0);
      var2.set(5, 1);
      var2.set(11, 0);
      var2.set(12, 0);
      var2.set(13, 0);
      var2.set(14, 0);
      return var2;
   }

   private int weekNumber(Calendar var1, int var2) {
      Calendar var3 = this.beginningOfYear(var1);
      var3.set(5, 1 + (var2 + 8 - var3.get(7)) % 7);
      if (var3.after(var1)) {
         var3.set(1, var3.get(1) - 1);
         var3.set(5, 1 + (var2 + 8 - var3.get(7)) % 7);
      }

      long var4 = var1.getTime().getTime() - var3.getTime().getTime();
      return 1 + (int)(var4 / 604800000L);
   }

   private int timeZoneOffset(Calendar var1) {
      int var2 = (var1.get(11) * 3600 + var1.get(12) * 60 + var1.get(13)) * 1000;
      return var1.getTimeZone().getOffset(1, var1.get(1), var1.get(2), var1.get(5), var1.get(7), var2) / 1000;
   }

   private boolean isDaylightSavingsTime(Calendar var1) {
      return this.timeZoneOffset(var1) != var1.getTimeZone().getRawOffset() / 1000;
   }

   protected Varargs execute(String var1) {
      return varargsOf(NIL, valueOf("exit"), ONE);
   }

   protected void exit(int var1) {
      System.exit(var1);
   }

   protected String getenv(String var1) {
      return System.getProperty(var1);
   }

   protected void remove(String var1) throws IOException {
      throw new IOException("not implemented");
   }

   protected void rename(String var1, String var2) throws IOException {
      throw new IOException("not implemented");
   }

   protected String setlocale(String var1, String var2) {
      return "C";
   }

   protected double time(LuaTable var1) {
      Date var2;
      if (var1 == null) {
         var2 = new Date();
      } else {
         Calendar var3 = Calendar.getInstance();
         var3.set(1, var1.get("year").checkint());
         var3.set(2, var1.get("month").checkint() - 1);
         var3.set(5, var1.get("day").checkint());
         var3.set(11, var1.get("hour").optint(12));
         var3.set(12, var1.get("min").optint(0));
         var3.set(13, var1.get("sec").optint(0));
         var3.set(14, 0);
         var2 = var3.getTime();
      }

      return var2.getTime() / 1000.0;
   }

   protected String tmpname() {
      synchronized (OsLib.class) {
         return ".luaj" + tmpnames++ + "tmp";
      }
   }

   class OsLibFunc extends VarArgFunction {
      public OsLibFunc(int var2, String var3) {
         this.opcode = var2;
         this.name = var3;
      }

      public Varargs invoke(Varargs var1) {
         try {
            switch (this.opcode) {
               case 0:
                  return valueOf(OsLib.this.clock());
               case 1:
                  String var9 = var1.optjstring(1, "%c");
                  double var3 = var1.isnumber(2) ? var1.todouble(2) : OsLib.this.time(null);
                  if (var9.equals("*t")) {
                     Calendar var5 = Calendar.getInstance();
                     var5.setTime(new Date((long)(var3 * 1000.0)));
                     LuaTable var6 = LuaValue.tableOf();
                     var6.set("year", LuaValue.valueOf(var5.get(1)));
                     var6.set("month", LuaValue.valueOf(var5.get(2) + 1));
                     var6.set("day", LuaValue.valueOf(var5.get(5)));
                     var6.set("hour", LuaValue.valueOf(var5.get(11)));
                     var6.set("min", LuaValue.valueOf(var5.get(12)));
                     var6.set("sec", LuaValue.valueOf(var5.get(13)));
                     var6.set("wday", LuaValue.valueOf(var5.get(7)));
                     var6.set("yday", LuaValue.valueOf(var5.get(6)));
                     var6.set("isdst", LuaValue.valueOf(OsLib.this.isDaylightSavingsTime(var5)));
                     return var6;
                  }

                  return valueOf(OsLib.this.date(var9, var3 == -1.0 ? OsLib.this.time(null) : var3));
               case 2:
                  return valueOf(OsLib.this.difftime(var1.checkdouble(1), var1.checkdouble(2)));
               case 3:
                  return OsLib.this.execute(var1.optjstring(1, null));
               case 4:
                  OsLib.this.exit(var1.optint(1, 0));
                  return NONE;
               case 5:
                  String var8 = OsLib.this.getenv(var1.checkjstring(1));
                  return (Varargs)(var8 != null ? valueOf(var8) : NIL);
               case 6:
                  OsLib.this.remove(var1.checkjstring(1));
                  return LuaValue.TRUE;
               case 7:
                  OsLib.this.rename(var1.checkjstring(1), var1.checkjstring(2));
                  return LuaValue.TRUE;
               case 8:
                  String var2 = OsLib.this.setlocale(var1.optjstring(1, null), var1.optjstring(2, "all"));
                  return (Varargs)(var2 != null ? valueOf(var2) : NIL);
               case 9:
                  return valueOf(OsLib.this.time(var1.opttable(1, null)));
               case 10:
                  return valueOf(OsLib.this.tmpname());
               default:
                  return NONE;
            }
         } catch (IOException var7) {
            return varargsOf(NIL, valueOf(var7.getMessage()));
         }
      }
   }
}
