package dev.inteleonyx.armandillo.api.luaj;

public abstract class LuaValue extends Varargs {
   public static final int TINT = -2;
   public static final int TNONE = -1;
   public static final int TNIL = 0;
   public static final int TBOOLEAN = 1;
   public static final int TLIGHTUSERDATA = 2;
   public static final int TNUMBER = 3;
   public static final int TSTRING = 4;
   public static final int TTABLE = 5;
   public static final int TFUNCTION = 6;
   public static final int TUSERDATA = 7;
   public static final int TTHREAD = 8;
   public static final int TVALUE = 9;
   public static final String[] TYPE_NAMES = new String[]{
      "nil", "boolean", "lightuserdata", "number", "string", "table", "function", "userdata", "thread", "value"
   };
   public static final LuaValue NIL = LuaNil._NIL;
   public static final LuaBoolean TRUE = LuaBoolean._TRUE;
   public static final LuaBoolean FALSE = LuaBoolean._FALSE;
   public static final LuaValue NONE = None._NONE;
   public static final LuaNumber ZERO = LuaInteger.valueOf(0);
   public static final LuaNumber ONE = LuaInteger.valueOf(1);
   public static final LuaNumber MINUSONE = LuaInteger.valueOf(-1);
   public static final LuaValue[] NOVALS = new LuaValue[0];
   public static LuaString ENV = valueOf("_ENV");
   public static final LuaString INDEX = valueOf("__index");
   public static final LuaString NEWINDEX = valueOf("__newindex");
   public static final LuaString CALL = valueOf("__call");
   public static final LuaString MODE = valueOf("__mode");
   public static final LuaString METATABLE = valueOf("__metatable");
   public static final LuaString ADD = valueOf("__add");
   public static final LuaString SUB = valueOf("__sub");
   public static final LuaString DIV = valueOf("__div");
   public static final LuaString MUL = valueOf("__mul");
   public static final LuaString POW = valueOf("__pow");
   public static final LuaString MOD = valueOf("__mod");
   public static final LuaString UNM = valueOf("__unm");
   public static final LuaString LEN = valueOf("__len");
   public static final LuaString EQ = valueOf("__eq");
   public static final LuaString LT = valueOf("__lt");
   public static final LuaString LE = valueOf("__le");
   public static final LuaString TOSTRING = valueOf("__tostring");
   public static final LuaString CONCAT = valueOf("__concat");
   public static final LuaString EMPTYSTRING = valueOf("");
   private static int MAXSTACK = 250;
   public static final LuaValue[] NILS = new LuaValue[MAXSTACK];
   private static final int MAXTAGLOOP = 100;

   public abstract int type();

   public abstract String typename();

   public boolean isboolean() {
      return false;
   }

   public boolean isclosure() {
      return false;
   }

   public boolean isfunction() {
      return false;
   }

   public boolean isint() {
      return false;
   }

   public boolean isinttype() {
      return false;
   }

   public boolean islong() {
      return false;
   }

   public boolean isnil() {
      return false;
   }

   public boolean isnumber() {
      return false;
   }

   public boolean isstring() {
      return false;
   }

   public boolean isthread() {
      return false;
   }

   public boolean istable() {
      return false;
   }

   public boolean isuserdata() {
      return false;
   }

   public boolean isuserdata(Class var1) {
      return false;
   }

   public boolean toboolean() {
      return true;
   }

   public byte tobyte() {
      return 0;
   }

   public char tochar() {
      return '\u0000';
   }

   public double todouble() {
      return 0.0;
   }

   public float tofloat() {
      return 0.0F;
   }

   public int toint() {
      return 0;
   }

   public long tolong() {
      return 0L;
   }

   public short toshort() {
      return 0;
   }

   public String tojstring() {
      return this.typename() + ": " + Integer.toHexString(this.hashCode());
   }

   public Object touserdata() {
      return null;
   }

   public Object touserdata(Class var1) {
      return null;
   }

   public String toString() {
      return this.tojstring();
   }

   public LuaValue tonumber() {
      return NIL;
   }

   public LuaValue tostring() {
      return NIL;
   }

   public boolean optboolean(boolean var1) {
      this.argerror("boolean");
      return false;
   }

   public LuaClosure optclosure(LuaClosure var1) {
      this.argerror("closure");
      return null;
   }

   public double optdouble(double var1) {
      this.argerror("number");
      return 0.0;
   }

   public LuaFunction optfunction(LuaFunction var1) {
      this.argerror("function");
      return null;
   }

   public int optint(int var1) {
      this.argerror("int");
      return 0;
   }

   public LuaInteger optinteger(LuaInteger var1) {
      this.argerror("integer");
      return null;
   }

   public long optlong(long var1) {
      this.argerror("long");
      return 0L;
   }

   public LuaNumber optnumber(LuaNumber var1) {
      this.argerror("number");
      return null;
   }

   public String optjstring(String var1) {
      this.argerror("String");
      return null;
   }

   public LuaString optstring(LuaString var1) {
      this.argerror("string");
      return null;
   }

   public LuaTable opttable(LuaTable var1) {
      this.argerror("table");
      return null;
   }

   public LuaThread optthread(LuaThread var1) {
      this.argerror("thread");
      return null;
   }

   public Object optuserdata(Object var1) {
      this.argerror("object");
      return null;
   }

   public Object optuserdata(Class var1, Object var2) {
      this.argerror(var1.getName());
      return null;
   }

   public LuaValue optvalue(LuaValue var1) {
      return this;
   }

   public boolean checkboolean() {
      this.argerror("boolean");
      return false;
   }

   public LuaClosure checkclosure() {
      this.argerror("closure");
      return null;
   }

   public double checkdouble() {
      this.argerror("number");
      return 0.0;
   }

   public LuaFunction checkfunction() {
      this.argerror("function");
      return null;
   }

   public Globals checkglobals() {
      this.argerror("globals");
      return null;
   }

   public int checkint() {
      this.argerror("int");
      return 0;
   }

   public LuaInteger checkinteger() {
      this.argerror("integer");
      return null;
   }

   public long checklong() {
      this.argerror("long");
      return 0L;
   }

   public LuaNumber checknumber() {
      this.argerror("number");
      return null;
   }

   public LuaNumber checknumber(String var1) {
      throw new LuaError(var1);
   }

   public String checkjstring() {
      this.argerror("string");
      return null;
   }

   public LuaString checkstring() {
      this.argerror("string");
      return null;
   }

   public LuaTable checktable() {
      this.argerror("table");
      return null;
   }

   public LuaThread checkthread() {
      this.argerror("thread");
      return null;
   }

   public Object checkuserdata() {
      this.argerror("userdata");
      return null;
   }

   public Object checkuserdata(Class var1) {
      this.argerror("userdata");
      return null;
   }

   public LuaValue checknotnil() {
      return this;
   }

   public boolean isvalidkey() {
      return true;
   }

   public static LuaValue error(String var0) {
      throw new LuaError(var0);
   }

   public static void assert_(boolean var0, String var1) {
      if (!var0) {
         throw new LuaError(var1);
      }
   }

   protected LuaValue argerror(String var1) {
      throw new LuaError("bad argument: " + var1 + " expected, got " + this.typename());
   }

   public static LuaValue argerror(int var0, String var1) {
      throw new LuaError("bad argument #" + var0 + ": " + var1);
   }

   protected LuaValue typerror(String var1) {
      throw new LuaError(var1 + " expected, got " + this.typename());
   }

   protected LuaValue unimplemented(String var1) {
      throw new LuaError("'" + var1 + "' not implemented for " + this.typename());
   }

   protected LuaValue illegal(String var1, String var2) {
      throw new LuaError("illegal operation '" + var1 + "' for " + var2);
   }

   protected LuaValue lenerror() {
      throw new LuaError("attempt to get length of " + this.typename());
   }

   protected LuaValue aritherror() {
      throw new LuaError("attempt to perform arithmetic on " + this.typename());
   }

   protected LuaValue aritherror(String var1) {
      throw new LuaError("attempt to perform arithmetic '" + var1 + "' on " + this.typename());
   }

   protected LuaValue compareerror(String var1) {
      throw new LuaError("attempt to compare " + this.typename() + " with " + var1);
   }

   protected LuaValue compareerror(LuaValue var1) {
      throw new LuaError("attempt to compare " + this.typename() + " with " + var1.typename());
   }

   public LuaValue get(LuaValue var1) {
      return gettable(this, var1);
   }

   public LuaValue get(int var1) {
      return this.get(LuaInteger.valueOf(var1));
   }

   public LuaValue get(String var1) {
      return this.get(valueOf(var1));
   }

   public void set(LuaValue var1, LuaValue var2) {
      settable(this, var1, var2);
   }

   public void set(int var1, LuaValue var2) {
      this.set(LuaInteger.valueOf(var1), var2);
   }

   public void set(int var1, String var2) {
      this.set(var1, valueOf(var2));
   }

   public void set(String var1, LuaValue var2) {
      this.set(valueOf(var1), var2);
   }

   public void set(String var1, double var2) {
      this.set(valueOf(var1), valueOf(var2));
   }

   public void set(String var1, int var2) {
      this.set(valueOf(var1), valueOf(var2));
   }

   public void set(String var1, String var2) {
      this.set(valueOf(var1), valueOf(var2));
   }

   public LuaValue rawget(LuaValue var1) {
      return this.unimplemented("rawget");
   }

   public LuaValue rawget(int var1) {
      return this.rawget(valueOf(var1));
   }

   public LuaValue rawget(String var1) {
      return this.rawget(valueOf(var1));
   }

   public void rawset(LuaValue var1, LuaValue var2) {
      this.unimplemented("rawset");
   }

   public void rawset(int var1, LuaValue var2) {
      this.rawset(valueOf(var1), var2);
   }

   public void rawset(int var1, String var2) {
      this.rawset(var1, valueOf(var2));
   }

   public void rawset(String var1, LuaValue var2) {
      this.rawset(valueOf(var1), var2);
   }

   public void rawset(String var1, double var2) {
      this.rawset(valueOf(var1), valueOf(var2));
   }

   public void rawset(String var1, int var2) {
      this.rawset(valueOf(var1), valueOf(var2));
   }

   public void rawset(String var1, String var2) {
      this.rawset(valueOf(var1), valueOf(var2));
   }

   public void rawsetlist(int var1, Varargs var2) {
      int var3 = 0;

      for (int var4 = var2.narg(); var3 < var4; var3++) {
         this.rawset(var1 + var3, var2.arg(var3 + 1));
      }
   }

   public void presize(int var1) {
      this.typerror("table");
   }

   public Varargs next(LuaValue var1) {
      return this.typerror("table");
   }

   public Varargs inext(LuaValue var1) {
      return this.typerror("table");
   }

   public LuaValue load(LuaValue var1) {
      return var1.call(EMPTYSTRING, this);
   }

   public LuaValue arg(int var1) {
      return var1 == 1 ? this : NIL;
   }

   public int narg() {
      return 1;
   }

   public LuaValue arg1() {
      return this;
   }

   public LuaValue getmetatable() {
      return null;
   }

   public LuaValue setmetatable(LuaValue var1) {
      return this.argerror("table");
   }

   public LuaValue call() {
      return this.callmt().call(this);
   }

   public LuaValue call(LuaValue var1) {
      return this.callmt().call(this, var1);
   }

   public LuaValue call(String var1) {
      return this.call(valueOf(var1));
   }

   public LuaValue call(LuaValue var1, LuaValue var2) {
      return this.callmt().call(this, var1, var2);
   }

   public LuaValue call(LuaValue var1, LuaValue var2, LuaValue var3) {
      return this.callmt().invoke(new LuaValue[]{this, var1, var2, var3}).arg1();
   }

   public LuaValue method(String var1) {
      return this.get(var1).call(this);
   }

   public LuaValue method(LuaValue var1) {
      return this.get(var1).call(this);
   }

   public LuaValue method(String var1, LuaValue var2) {
      return this.get(var1).call(this, var2);
   }

   public LuaValue method(LuaValue var1, LuaValue var2) {
      return this.get(var1).call(this, var2);
   }

   public LuaValue method(String var1, LuaValue var2, LuaValue var3) {
      return this.get(var1).call(this, var2, var3);
   }

   public LuaValue method(LuaValue var1, LuaValue var2, LuaValue var3) {
      return this.get(var1).call(this, var2, var3);
   }

   public Varargs invoke() {
      return this.invoke(NONE);
   }

   public Varargs invoke(Varargs var1) {
      return this.callmt().invoke(this, var1);
   }

   public Varargs invoke(LuaValue var1, Varargs var2) {
      return this.invoke(varargsOf(var1, var2));
   }

   public Varargs invoke(LuaValue var1, LuaValue var2, Varargs var3) {
      return this.invoke(varargsOf(var1, var2, var3));
   }

   public Varargs invoke(LuaValue[] var1) {
      return this.invoke(varargsOf(var1));
   }

   public Varargs invoke(LuaValue[] var1, Varargs var2) {
      return this.invoke(varargsOf(var1, var2));
   }

   public Varargs invokemethod(String var1) {
      return this.get(var1).invoke(this);
   }

   public Varargs invokemethod(LuaValue var1) {
      return this.get(var1).invoke(this);
   }

   public Varargs invokemethod(String var1, Varargs var2) {
      return this.get(var1).invoke(varargsOf(this, var2));
   }

   public Varargs invokemethod(LuaValue var1, Varargs var2) {
      return this.get(var1).invoke(varargsOf(this, var2));
   }

   public Varargs invokemethod(String var1, LuaValue[] var2) {
      return this.get(var1).invoke(varargsOf(this, varargsOf(var2)));
   }

   public Varargs invokemethod(LuaValue var1, LuaValue[] var2) {
      return this.get(var1).invoke(varargsOf(this, varargsOf(var2)));
   }

   protected LuaValue callmt() {
      return this.checkmetatag(CALL, "attempt to call ");
   }

   public LuaValue not() {
      return FALSE;
   }

   public LuaValue neg() {
      return this.checkmetatag(UNM, "attempt to perform arithmetic on ").call(this);
   }

   public LuaValue len() {
      return this.checkmetatag(LEN, "attempt to get length of ").call(this);
   }

   public int length() {
      return this.len().toint();
   }

   public int rawlen() {
      this.typerror("table or string");
      return 0;
   }

   public boolean equals(Object var1) {
      return this == var1;
   }

   public LuaValue eq(LuaValue var1) {
      return this.eq_b(var1) ? TRUE : FALSE;
   }

   public boolean eq_b(LuaValue var1) {
      return this == var1;
   }

   public LuaValue neq(LuaValue var1) {
      return this.eq_b(var1) ? FALSE : TRUE;
   }

   public boolean neq_b(LuaValue var1) {
      return !this.eq_b(var1);
   }

   public boolean raweq(LuaValue var1) {
      return this == var1;
   }

   public boolean raweq(LuaUserdata var1) {
      return false;
   }

   public boolean raweq(LuaString var1) {
      return false;
   }

   public boolean raweq(double var1) {
      return false;
   }

   public boolean raweq(int var1) {
      return false;
   }

   public static final boolean eqmtcall(LuaValue var0, LuaValue var1, LuaValue var2, LuaValue var3) {
      LuaValue var4 = var1.rawget(EQ);
      return !var4.isnil() && var4 == var3.rawget(EQ) ? var4.call(var0, var2).toboolean() : false;
   }

   public LuaValue add(LuaValue var1) {
      return this.arithmt(ADD, var1);
   }

   public LuaValue add(double var1) {
      return this.arithmtwith(ADD, var1);
   }

   public LuaValue add(int var1) {
      return this.add((double)var1);
   }

   public LuaValue sub(LuaValue var1) {
      return this.arithmt(SUB, var1);
   }

   public LuaValue sub(double var1) {
      return this.aritherror("sub");
   }

   public LuaValue sub(int var1) {
      return this.aritherror("sub");
   }

   public LuaValue subFrom(double var1) {
      return this.arithmtwith(SUB, var1);
   }

   public LuaValue subFrom(int var1) {
      return this.subFrom((double)var1);
   }

   public LuaValue mul(LuaValue var1) {
      return this.arithmt(MUL, var1);
   }

   public LuaValue mul(double var1) {
      return this.arithmtwith(MUL, var1);
   }

   public LuaValue mul(int var1) {
      return this.mul((double)var1);
   }

   public LuaValue pow(LuaValue var1) {
      return this.arithmt(POW, var1);
   }

   public LuaValue pow(double var1) {
      return this.aritherror("pow");
   }

   public LuaValue pow(int var1) {
      return this.aritherror("pow");
   }

   public LuaValue powWith(double var1) {
      return this.arithmtwith(POW, var1);
   }

   public LuaValue powWith(int var1) {
      return this.powWith((double)var1);
   }

   public LuaValue div(LuaValue var1) {
      return this.arithmt(DIV, var1);
   }

   public LuaValue div(double var1) {
      return this.aritherror("div");
   }

   public LuaValue div(int var1) {
      return this.aritherror("div");
   }

   public LuaValue divInto(double var1) {
      return this.arithmtwith(DIV, var1);
   }

   public LuaValue mod(LuaValue var1) {
      return this.arithmt(MOD, var1);
   }

   public LuaValue mod(double var1) {
      return this.aritherror("mod");
   }

   public LuaValue mod(int var1) {
      return this.aritherror("mod");
   }

   public LuaValue modFrom(double var1) {
      return this.arithmtwith(MOD, var1);
   }

   protected LuaValue arithmt(LuaValue var1, LuaValue var2) {
      LuaValue var3 = this.metatag(var1);
      if (var3.isnil()) {
         var3 = var2.metatag(var1);
         if (var3.isnil()) {
            error("attempt to perform arithmetic " + var1 + " on " + this.typename() + " and " + var2.typename());
         }
      }

      return var3.call(this, var2);
   }

   protected LuaValue arithmtwith(LuaValue var1, double var2) {
      LuaValue var4 = this.metatag(var1);
      if (var4.isnil()) {
         error("attempt to perform arithmetic " + var1 + " on number and " + this.typename());
      }

      return var4.call(valueOf(var2), this);
   }

   public LuaValue lt(LuaValue var1) {
      return this.comparemt(LT, var1);
   }

   public LuaValue lt(double var1) {
      return this.compareerror("number");
   }

   public LuaValue lt(int var1) {
      return this.compareerror("number");
   }

   public boolean lt_b(LuaValue var1) {
      return this.comparemt(LT, var1).toboolean();
   }

   public boolean lt_b(int var1) {
      this.compareerror("number");
      return false;
   }

   public boolean lt_b(double var1) {
      this.compareerror("number");
      return false;
   }

   public LuaValue lteq(LuaValue var1) {
      return this.comparemt(LE, var1);
   }

   public LuaValue lteq(double var1) {
      return this.compareerror("number");
   }

   public LuaValue lteq(int var1) {
      return this.compareerror("number");
   }

   public boolean lteq_b(LuaValue var1) {
      return this.comparemt(LE, var1).toboolean();
   }

   public boolean lteq_b(int var1) {
      this.compareerror("number");
      return false;
   }

   public boolean lteq_b(double var1) {
      this.compareerror("number");
      return false;
   }

   public LuaValue gt(LuaValue var1) {
      return var1.comparemt(LE, this);
   }

   public LuaValue gt(double var1) {
      return this.compareerror("number");
   }

   public LuaValue gt(int var1) {
      return this.compareerror("number");
   }

   public boolean gt_b(LuaValue var1) {
      return var1.comparemt(LE, this).toboolean();
   }

   public boolean gt_b(int var1) {
      this.compareerror("number");
      return false;
   }

   public boolean gt_b(double var1) {
      this.compareerror("number");
      return false;
   }

   public LuaValue gteq(LuaValue var1) {
      return var1.comparemt(LT, this);
   }

   public LuaValue gteq(double var1) {
      return this.compareerror("number");
   }

   public LuaValue gteq(int var1) {
      return valueOf(this.todouble() >= var1);
   }

   public boolean gteq_b(LuaValue var1) {
      return var1.comparemt(LT, this).toboolean();
   }

   public boolean gteq_b(int var1) {
      this.compareerror("number");
      return false;
   }

   public boolean gteq_b(double var1) {
      this.compareerror("number");
      return false;
   }

   public LuaValue comparemt(LuaValue var1, LuaValue var2) {
      LuaValue var3;
      if ((var3 = this.metatag(var1)).isnil() && (var3 = var2.metatag(var1)).isnil()) {
         return !LE.raweq(var1) || (var3 = this.metatag(LT)).isnil() && (var3 = var2.metatag(LT)).isnil()
            ? error("attempt to compare " + var1 + " on " + this.typename() + " and " + var2.typename())
            : var3.call(var2, this).not();
      } else {
         return var3.call(this, var2);
      }
   }

   public int strcmp(LuaValue var1) {
      error("attempt to compare " + this.typename());
      return 0;
   }

   public int strcmp(LuaString var1) {
      error("attempt to compare " + this.typename());
      return 0;
   }

   public LuaValue concat(LuaValue var1) {
      return this.concatmt(var1);
   }

   public LuaValue concatTo(LuaValue var1) {
      return var1.concatmt(this);
   }

   public LuaValue concatTo(LuaNumber var1) {
      return var1.concatmt(this);
   }

   public LuaValue concatTo(LuaString var1) {
      return var1.concatmt(this);
   }

   public Buffer buffer() {
      return new Buffer(this);
   }

   public Buffer concat(Buffer var1) {
      return var1.concatTo(this);
   }

   public LuaValue concatmt(LuaValue var1) {
      LuaValue var2 = this.metatag(CONCAT);
      if (var2.isnil() && (var2 = var1.metatag(CONCAT)).isnil()) {
         error("attempt to concatenate " + this.typename() + " and " + var1.typename());
      }

      return var2.call(this, var1);
   }

   public LuaValue and(LuaValue var1) {
      return this.toboolean() ? var1 : this;
   }

   public LuaValue or(LuaValue var1) {
      return this.toboolean() ? this : var1;
   }

   public boolean testfor_b(LuaValue var1, LuaValue var2) {
      return var2.gt_b(0) ? this.lteq_b(var1) : this.gteq_b(var1);
   }

   public LuaString strvalue() {
      this.typerror("string or number");
      return null;
   }

   public LuaValue strongvalue() {
      return this;
   }

   public static LuaBoolean valueOf(boolean var0) {
      return var0 ? TRUE : FALSE;
   }

   public static LuaInteger valueOf(int var0) {
      return LuaInteger.valueOf(var0);
   }

   public static LuaNumber valueOf(double var0) {
      return LuaDouble.valueOf(var0);
   }

   public static LuaString valueOf(String var0) {
      return LuaString.valueOf(var0);
   }

   public static LuaString valueOf(byte[] var0) {
      return LuaString.valueOf(var0);
   }

   public static LuaString valueOf(byte[] var0, int var1, int var2) {
      return LuaString.valueOf(var0, var1, var2);
   }

   public static LuaTable tableOf() {
      return new LuaTable();
   }

   public static LuaTable tableOf(Varargs var0, int var1) {
      return new LuaTable(var0, var1);
   }

   public static LuaTable tableOf(int var0, int var1) {
      return new LuaTable(var0, var1);
   }

   public static LuaTable listOf(LuaValue[] var0) {
      return new LuaTable(null, var0, null);
   }

   public static LuaTable listOf(LuaValue[] var0, Varargs var1) {
      return new LuaTable(null, var0, var1);
   }

   public static LuaTable tableOf(LuaValue[] var0) {
      return new LuaTable(var0, null, null);
   }

   public static LuaTable tableOf(LuaValue[] var0, LuaValue[] var1) {
      return new LuaTable(var0, var1, null);
   }

   public static LuaTable tableOf(LuaValue[] var0, LuaValue[] var1, Varargs var2) {
      return new LuaTable(var0, var1, var2);
   }

   public static LuaUserdata userdataOf(Object var0) {
      return new LuaUserdata(var0);
   }

   public static LuaUserdata userdataOf(Object var0, LuaValue var1) {
      return new LuaUserdata(var0, var1);
   }

   protected static LuaValue gettable(LuaValue var0, LuaValue var1) {
      int var3 = 0;

      do {
         LuaValue var2;
         if (var0.istable()) {
            LuaValue var4 = var0.rawget(var1);
            if (!var4.isnil() || (var2 = var0.metatag(INDEX)).isnil()) {
               return var4;
            }
         } else if ((var2 = var0.metatag(INDEX)).isnil()) {
            var0.indexerror(var1.tojstring());
         }

         if (var2.isfunction()) {
            return var2.call(var0, var1);
         }

         var0 = var2;
      } while (++var3 < 100);

      error("loop in gettable");
      return NIL;
   }

   protected static boolean settable(LuaValue var0, LuaValue var1, LuaValue var2) {
      int var4 = 0;

      do {
         LuaValue var3;
         if (var0.istable()) {
            if (!var0.rawget(var1).isnil() || (var3 = var0.metatag(NEWINDEX)).isnil()) {
               var0.rawset(var1, var2);
               return true;
            }
         } else if ((var3 = var0.metatag(NEWINDEX)).isnil()) {
            throw new LuaError("table expected for set index ('" + var1 + "') value, got " + var0.typename());
         }

         if (var3.isfunction()) {
            var3.call(var0, var1, var2);
            return true;
         }

         var0 = var3;
      } while (++var4 < 100);

      error("loop in settable");
      return false;
   }

   public LuaValue metatag(LuaValue var1) {
      LuaValue var2 = this.getmetatable();
      return var2 == null ? NIL : var2.rawget(var1);
   }

   protected LuaValue checkmetatag(LuaValue var1, String var2) {
      LuaValue var3 = this.metatag(var1);
      if (var3.isnil()) {
         throw new LuaError(var2 + this.typename());
      } else {
         return var3;
      }
   }

   protected static Metatable metatableOf(LuaValue var0) {
      if (var0 != null && var0.istable()) {
         LuaValue var1 = var0.rawget(MODE);
         if (var1.isstring()) {
            String var2 = var1.tojstring();
            boolean var3 = var2.indexOf(107) >= 0;
            boolean var4 = var2.indexOf(118) >= 0;
            if (var3 || var4) {
               return new WeakTable(var3, var4, var0);
            }
         }

         return (LuaTable)var0;
      } else {
         return var0 != null ? new NonTableMetatable(var0) : null;
      }
   }

   private void indexerror(String var1) {
      error("attempt to index ? (a " + this.typename() + " value) with key '" + var1 + "'");
   }

   public static Varargs varargsOf(LuaValue[] var0) {
      switch (var0.length) {
         case 0:
            return NONE;
         case 1:
            return var0[0];
         case 2:
            return new Varargs.PairVarargs(var0[0], var0[1]);
         default:
            return new Varargs.ArrayVarargs(var0, NONE);
      }
   }

   public static Varargs varargsOf(LuaValue[] var0, Varargs var1) {
      switch (var0.length) {
         case 0:
            return var1;
         case 1:
            return (Varargs)(var1.narg() > 0 ? new Varargs.PairVarargs(var0[0], var1) : var0[0]);
         case 2:
            return (Varargs)(var1.narg() > 0 ? new Varargs.ArrayVarargs(var0, var1) : new Varargs.PairVarargs(var0[0], var0[1]));
         default:
            return new Varargs.ArrayVarargs(var0, var1);
      }
   }

   public static Varargs varargsOf(LuaValue[] var0, int var1, int var2) {
      switch (var2) {
         case 0:
            return NONE;
         case 1:
            return var0[var1];
         case 2:
            return new Varargs.PairVarargs(var0[var1 + 0], var0[var1 + 1]);
         default:
            return new Varargs.ArrayPartVarargs(var0, var1, var2, NONE);
      }
   }

   public static Varargs varargsOf(LuaValue[] var0, int var1, int var2, Varargs var3) {
      switch (var2) {
         case 0:
            return var3;
         case 1:
            return (Varargs)(var3.narg() > 0 ? new Varargs.PairVarargs(var0[var1], var3) : var0[var1]);
         case 2:
            return (Varargs)(var3.narg() > 0 ? new Varargs.ArrayPartVarargs(var0, var1, var2, var3) : new Varargs.PairVarargs(var0[var1], var0[var1 + 1]));
         default:
            return new Varargs.ArrayPartVarargs(var0, var1, var2, var3);
      }
   }

   public static Varargs varargsOf(LuaValue var0, Varargs var1) {
      switch (var1.narg()) {
         case 0:
            return var0;
         default:
            return new Varargs.PairVarargs(var0, var1);
      }
   }

   public static Varargs varargsOf(LuaValue var0, LuaValue var1, Varargs var2) {
      switch (var2.narg()) {
         case 0:
            return new Varargs.PairVarargs(var0, var1);
         default:
            return new Varargs.ArrayPartVarargs(new LuaValue[]{var0, var1}, 0, 2, var2);
      }
   }

   public static Varargs tailcallOf(LuaValue var0, Varargs var1) {
      return new TailcallVarargs(var0, var1);
   }

   public Varargs onInvoke(Varargs var1) {
      return this.invoke(var1);
   }

   public void initupvalue1(LuaValue var1) {
   }

   public Varargs subargs(int var1) {
      if (var1 == 1) {
         return this;
      } else {
         return var1 > 1 ? NONE : argerror(1, "start must be > 0");
      }
   }

   static {
      for (int var0 = 0; var0 < MAXSTACK; var0++) {
         NILS[var0] = NIL;
      }
   }

   private static final class None extends LuaNil {
      static None _NONE = new None();

      public LuaValue arg(int var1) {
         return NIL;
      }

      public int narg() {
         return 0;
      }

      public LuaValue arg1() {
         return NIL;
      }

      public String tojstring() {
         return "none";
      }

      public Varargs subargs(int var1) {
         return (Varargs)(var1 > 0 ? this : argerror(1, "start must be > 0"));
      }

      void copyto(LuaValue[] var1, int var2, int var3) {
         while (var3 > 0) {
            var1[var2++] = NIL;
            var3--;
         }
      }
   }
}
