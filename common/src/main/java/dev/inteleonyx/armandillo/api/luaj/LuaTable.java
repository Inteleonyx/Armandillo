package dev.inteleonyx.armandillo.api.luaj;

import java.lang.ref.WeakReference;
import java.util.Vector;

public class LuaTable extends LuaValue implements Metatable {
   private static final int MIN_HASH_CAPACITY = 2;
   private static final LuaString N = valueOf("n");
   protected LuaValue[] array;
   protected Slot[] hash;
   protected int hashEntries;
   protected Metatable m_metatable;
   private static final Slot[] NOBUCKETS = new Slot[0];

   public LuaTable() {
      this.array = NOVALS;
      this.hash = NOBUCKETS;
   }

   public LuaTable(int var1, int var2) {
      this.presize(var1, var2);
   }

   public LuaTable(LuaValue[] var1, LuaValue[] var2, Varargs var3) {
      int var4 = var1 != null ? var1.length : 0;
      int var5 = var2 != null ? var2.length : 0;
      int var6 = var3 != null ? var3.narg() : 0;
      this.presize(var5 + var6, var4 >> 1);

      for (int var7 = 0; var7 < var5; var7++) {
         this.rawset(var7 + 1, var2[var7]);
      }

      if (var3 != null) {
         int var9 = 1;

         for (int var8 = var3.narg(); var9 <= var8; var9++) {
            this.rawset(var5 + var9, var3.arg(var9));
         }
      }

      for (byte var10 = 0; var10 < var4; var10 += 2) {
         if (!var1[var10 + 1].isnil()) {
            this.rawset(var1[var10], var1[var10 + 1]);
         }
      }
   }

   public LuaTable(Varargs var1) {
      this(var1, 1);
   }

   public LuaTable(Varargs var1, int var2) {
      int var3 = var2 - 1;
      int var4 = Math.max(var1.narg() - var3, 0);
      this.presize(var4, 1);
      this.set(N, valueOf(var4));

      for (int var5 = 1; var5 <= var4; var5++) {
         this.set(var5, var1.arg(var5 + var3));
      }
   }

   public int type() {
      return 5;
   }

   public String typename() {
      return "table";
   }

   public boolean istable() {
      return true;
   }

   public LuaTable checktable() {
      return this;
   }

   public LuaTable opttable(LuaTable var1) {
      return this;
   }

   public void presize(int var1) {
      if (var1 > this.array.length) {
         this.array = resize(this.array, 1 << log2(var1));
      }
   }

   public void presize(int var1, int var2) {
      if (var2 > 0 && var2 < 2) {
         var2 = 2;
      }

      this.array = var1 > 0 ? new LuaValue[1 << log2(var1)] : NOVALS;
      this.hash = var2 > 0 ? new Slot[1 << log2(var2)] : NOBUCKETS;
      this.hashEntries = 0;
   }

   private static LuaValue[] resize(LuaValue[] var0, int var1) {
      LuaValue[] var2 = new LuaValue[var1];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      return var2;
   }

   protected int getArrayLength() {
      return this.array.length;
   }

   protected int getHashLength() {
      return this.hash.length;
   }

   public LuaValue getmetatable() {
      return this.m_metatable != null ? this.m_metatable.toLuaValue() : null;
   }

   public LuaValue setmetatable(LuaValue var1) {
      boolean var2 = this.m_metatable != null && this.m_metatable.useWeakKeys();
      boolean var3 = this.m_metatable != null && this.m_metatable.useWeakValues();
      this.m_metatable = metatableOf(var1);
      if (var2 != (this.m_metatable != null && this.m_metatable.useWeakKeys()) || var3 != (this.m_metatable != null && this.m_metatable.useWeakValues())) {
         this.rehash(0);
      }

      return this;
   }

   public LuaValue get(int var1) {
      LuaValue var2 = this.rawget(var1);
      return var2.isnil() && this.m_metatable != null ? gettable(this, valueOf(var1)) : var2;
   }

   public LuaValue get(LuaValue var1) {
      LuaValue var2 = this.rawget(var1);
      return var2.isnil() && this.m_metatable != null ? gettable(this, var1) : var2;
   }

   public LuaValue rawget(int var1) {
      if (var1 > 0 && var1 <= this.array.length) {
         LuaValue var2 = this.m_metatable == null ? this.array[var1 - 1] : this.m_metatable.arrayget(this.array, var1 - 1);
         return var2 != null ? var2 : NIL;
      } else {
         return this.hashget(LuaInteger.valueOf(var1));
      }
   }

   public LuaValue rawget(LuaValue var1) {
      if (var1.isinttype()) {
         int var2 = var1.toint();
         if (var2 > 0 && var2 <= this.array.length) {
            LuaValue var3 = this.m_metatable == null ? this.array[var2 - 1] : this.m_metatable.arrayget(this.array, var2 - 1);
            return var3 != null ? var3 : NIL;
         }
      }

      return this.hashget(var1);
   }

   protected LuaValue hashget(LuaValue var1) {
      if (this.hashEntries > 0) {
         for (Slot var2 = this.hash[this.hashSlot(var1)]; var2 != null; var2 = var2.rest()) {
            StrongSlot var3;
            if ((var3 = var2.find(var1)) != null) {
               return var3.value();
            }
         }
      }

      return NIL;
   }

   public void set(int var1, LuaValue var2) {
      if (this.m_metatable == null || !this.rawget(var1).isnil() || !settable(this, LuaInteger.valueOf(var1), var2)) {
         this.rawset(var1, var2);
      }
   }

   public void set(LuaValue var1, LuaValue var2) {
      if (var1 != null && (var1.isvalidkey() || this.metatag(NEWINDEX).isfunction())) {
         if (this.m_metatable == null || !this.rawget(var1).isnil() || !settable(this, var1, var2)) {
            this.rawset(var1, var2);
         }
      } else {
         throw new LuaError("value ('" + var1 + "') can not be used as a table index");
      }
   }

   public void rawset(int var1, LuaValue var2) {
      if (!this.arrayset(var1, var2)) {
         this.hashset(LuaInteger.valueOf(var1), var2);
      }
   }

   public void rawset(LuaValue var1, LuaValue var2) {
      if (!var1.isinttype() || !this.arrayset(var1.toint(), var2)) {
         this.hashset(var1, var2);
      }
   }

   private boolean arrayset(int var1, LuaValue var2) {
      if (var1 > 0 && var1 <= this.array.length) {
         this.array[var1 - 1] = var2.isnil() ? null : (this.m_metatable != null ? this.m_metatable.wrap(var2) : var2);
         return true;
      } else {
         return false;
      }
   }

   public LuaValue remove(int var1) {
      int var2 = this.rawlen();
      if (var1 == 0) {
         var1 = var2;
      } else if (var1 > var2) {
         return NONE;
      }

      LuaValue var3 = this.rawget(var1);
      LuaValue var4 = var3;

      while (!var4.isnil()) {
         var4 = this.rawget(var1 + 1);
         this.rawset(var1++, var4);
      }

      return var3.isnil() ? NONE : var3;
   }

   public void insert(int var1, LuaValue var2) {
      if (var1 == 0) {
         var1 = this.rawlen() + 1;
      }

      while (!var2.isnil()) {
         LuaValue var3 = this.rawget(var1);
         this.rawset(var1++, var2);
         var2 = var3;
      }
   }

   public LuaValue concat(LuaString var1, int var2, int var3) {
      Buffer var4 = new Buffer();
      if (var2 <= var3) {
         var4.append(this.get(var2).checkstring());

         while (++var2 <= var3) {
            var4.append(var1);
            var4.append(this.get(var2).checkstring());
         }
      }

      return var4.tostring();
   }

   public int length() {
      if (this.m_metatable != null) {
         LuaValue var1 = this.len();
         if (!var1.isint()) {
            throw new LuaError("table length is not an integer: " + var1);
         } else {
            return var1.toint();
         }
      } else {
         return this.rawlen();
      }
   }

   public LuaValue len() {
      LuaValue var1 = this.metatag(LEN);
      return (LuaValue)(var1.toboolean() ? var1.call(this) : LuaInteger.valueOf(this.rawlen()));
   }

   public int rawlen() {
      int var1 = this.getArrayLength();
      int var2 = var1 + 1;

      int var3;
      for (var3 = 0; !this.rawget(var2).isnil(); var2 += var1 + this.getHashLength() + 1) {
         var3 = var2;
      }

      while (var2 > var3 + 1) {
         int var4 = (var2 + var3) / 2;
         if (!this.rawget(var4).isnil()) {
            var3 = var4;
         } else {
            var2 = var4;
         }
      }

      return var3;
   }

   public Varargs next(LuaValue var1) {
      int var2;
      var2 = 0;
      label81:
      if (!var1.isnil()) {
         if (var1.isinttype()) {
            var2 = var1.toint();
            if (var2 > 0 && var2 <= this.array.length) {
               break label81;
            }
         }

         if (this.hash.length == 0) {
            error("invalid key to 'next' 1: " + var1);
         }

         var2 = this.hashSlot(var1);
         boolean var3 = false;

         for (Slot var4 = this.hash[var2]; var4 != null; var4 = var4.rest()) {
            if (var3) {
               StrongSlot var5 = var4.first();
               if (var5 != null) {
                  return var5.toVarargs();
               }
            } else if (var4.keyeq(var1)) {
               var3 = true;
            }
         }

         if (!var3) {
            error("invalid key to 'next' 2: " + var1);
         }

         var2 += 1 + this.array.length;
      }

      for (; var2 < this.array.length; var2++) {
         if (this.array[var2] != null) {
            LuaValue var8 = this.m_metatable == null ? this.array[var2] : this.m_metatable.arrayget(this.array, var2);
            if (var8 != null) {
               return varargsOf(LuaInteger.valueOf(var2 + 1), var8);
            }
         }
      }

      for (int var7 = var2 - this.array.length; var7 < this.hash.length; var7++) {
         for (Slot var9 = this.hash[var7]; var9 != null; var9 = var9.rest()) {
            StrongSlot var10 = var9.first();
            if (var10 != null) {
               return var10.toVarargs();
            }
         }
      }

      return NIL;
   }

   public Varargs inext(LuaValue var1) {
      int var2 = var1.checkint() + 1;
      LuaValue var3 = this.rawget(var2);
      return (Varargs)(var3.isnil() ? NONE : varargsOf(LuaInteger.valueOf(var2), var3));
   }

   public void hashset(LuaValue var1, LuaValue var2) {
      if (var2.isnil()) {
         this.hashRemove(var1);
      } else {
         int var3 = 0;
         if (this.hash.length > 0) {
            var3 = this.hashSlot(var1);

            for (Slot var4 = this.hash[var3]; var4 != null; var4 = var4.rest()) {
               StrongSlot var5;
               if ((var5 = var4.find(var1)) != null) {
                  this.hash[var3] = this.hash[var3].set(var5, var2);
                  return;
               }
            }
         }

         if (this.checkLoadFactor()) {
            if (var1.isinttype() && var1.toint() > 0) {
               this.rehash(var1.toint());
               if (this.arrayset(var1.toint(), var2)) {
                  return;
               }
            } else {
               this.rehash(-1);
            }

            var3 = this.hashSlot(var1);
         }

         Object var6 = this.m_metatable != null ? this.m_metatable.entry(var1, var2) : defaultEntry(var1, var2);
         this.hash[var3] = (Slot)(this.hash[var3] != null ? this.hash[var3].add((Slot)var6) : var6);
         this.hashEntries++;
      }
   }

   public static int hashpow2(int var0, int var1) {
      return var0 & var1;
   }

   public static int hashmod(int var0, int var1) {
      return (var0 & 2147483647) % var1;
   }

   public static int hashSlot(LuaValue var0, int var1) {
      switch (var0.type()) {
         case 2:
         case 3:
         case 5:
         case 7:
         case 8:
            return hashmod(var0.hashCode(), var1);
         case 4:
         case 6:
         default:
            return hashpow2(var0.hashCode(), var1);
      }
   }

   private int hashSlot(LuaValue var1) {
      return hashSlot(var1, this.hash.length - 1);
   }

   private void hashRemove(LuaValue var1) {
      if (this.hash.length > 0) {
         int var2 = this.hashSlot(var1);

         for (Slot var3 = this.hash[var2]; var3 != null; var3 = var3.rest()) {
            StrongSlot var4;
            if ((var4 = var3.find(var1)) != null) {
               this.hash[var2] = this.hash[var2].remove(var4);
               this.hashEntries--;
               return;
            }
         }
      }
   }

   private boolean checkLoadFactor() {
      return this.hashEntries >= this.hash.length;
   }

   private int countHashKeys() {
      int var1 = 0;

      for (int var2 = 0; var2 < this.hash.length; var2++) {
         for (Slot var3 = this.hash[var2]; var3 != null; var3 = var3.rest()) {
            if (var3.first() != null) {
               var1++;
            }
         }
      }

      return var1;
   }

   private void dropWeakArrayValues() {
      for (int var1 = 0; var1 < this.array.length; var1++) {
         this.m_metatable.arrayget(this.array, var1);
      }
   }

   private int countIntKeys(int[] var1) {
      int var2 = 0;
      int var3 = 1;

      for (int var4 = 0; var4 < 31 && var3 <= this.array.length; var4++) {
         int var5 = Math.min(this.array.length, 1 << var4);
         int var6 = 0;

         while (var3 <= var5) {
            if (this.array[var3++ - 1] != null) {
               var6++;
            }
         }

         var1[var4] = var6;
         var2 += var6;
      }

      for (int var7 = 0; var7 < this.hash.length; var7++) {
         for (Slot var8 = this.hash[var7]; var8 != null; var8 = var8.rest()) {
            int var9;
            if ((var9 = var8.arraykey(Integer.MAX_VALUE)) > 0) {
               var1[log2(var9)]++;
               var2++;
            }
         }
      }

      return var2;
   }

   static int log2(int var0) {
      int var1 = 0;
      if (--var0 < 0) {
         return Integer.MIN_VALUE;
      } else {
         if ((var0 & -65536) != 0) {
            var1 = 16;
            var0 >>>= 16;
         }

         if ((var0 & 0xFF00) != 0) {
            var1 += 8;
            var0 >>>= 8;
         }

         if ((var0 & 240) != 0) {
            var1 += 4;
            var0 >>>= 4;
         }

         switch (var0) {
            case 0:
               return 0;
            case 1:
               var1++;
               break;
            case 2:
               var1 += 2;
               break;
            case 3:
               var1 += 2;
               break;
            case 4:
               var1 += 3;
               break;
            case 5:
               var1 += 3;
               break;
            case 6:
               var1 += 3;
               break;
            case 7:
               var1 += 3;
               break;
            case 8:
               var1 += 4;
               break;
            case 9:
               var1 += 4;
               break;
            case 10:
               var1 += 4;
               break;
            case 11:
               var1 += 4;
               break;
            case 12:
               var1 += 4;
               break;
            case 13:
               var1 += 4;
               break;
            case 14:
               var1 += 4;
               break;
            case 15:
               var1 += 4;
         }

         return var1;
      }
   }

   private void rehash(int var1) {
      if (this.m_metatable != null && (this.m_metatable.useWeakKeys() || this.m_metatable.useWeakValues())) {
         this.hashEntries = this.countHashKeys();
         if (this.m_metatable.useWeakValues()) {
            this.dropWeakArrayValues();
         }
      }

      int[] var2 = new int[32];
      int var3 = this.countIntKeys(var2);
      if (var1 > 0) {
         var3++;
         var2[log2(var1)]++;
      }

      int var4 = var2[0];
      int var5 = 0;

      for (int var6 = 1; var6 < 32; var6++) {
         var4 += var2[var6];
         if (var3 * 2 < 1 << var6) {
            break;
         }

         if (var4 >= 1 << var6 - 1) {
            var5 = 1 << var6;
         }
      }

      LuaValue[] var19 = this.array;
      Slot[] var7 = this.hash;
      int var10 = 0;
      if (var1 > 0 && var1 <= var5) {
         var10--;
      }

      LuaValue[] var8;
      if (var5 != var19.length) {
         var8 = new LuaValue[var5];
         if (var5 > var19.length) {
            int var20 = log2(var19.length + 1);

            for (int var22 = log2(var5) + 1; var20 < var22; var20++) {
               var10 += var2[var20];
            }
         } else if (var19.length > var5) {
            int var11 = log2(var5 + 1);

            for (int var12 = log2(var19.length) + 1; var11 < var12; var11++) {
               var10 -= var2[var11];
            }
         }

         System.arraycopy(var19, 0, var8, 0, Math.min(var19.length, var5));
      } else {
         var8 = this.array;
      }

      int var21 = this.hashEntries - var10 + (var1 >= 0 && var1 <= var5 ? 0 : 1);
      int var23 = var7.length;
      Slot[] var9;
      int var14;
      if (var21 > 0) {
         int var13 = var21 < 2 ? 2 : 1 << log2(var21);
         var14 = var13 - 1;
         var9 = new Slot[var13];
      } else {
         boolean var24 = false;
         var14 = 0;
         var9 = NOBUCKETS;
      }

      for (int var15 = 0; var15 < var23; var15++) {
         for (Slot var16 = var7[var15]; var16 != null; var16 = var16.rest()) {
            int var17;
            if ((var17 = var16.arraykey(var5)) > 0) {
               StrongSlot var18 = var16.first();
               if (var18 != null) {
                  var8[var17 - 1] = var18.value();
               }
            } else {
               int var28 = var16.keyindex(var14);
               var9[var28] = var16.relink(var9[var28]);
            }
         }
      }

      int var25 = var5;

      while (var25 < var19.length) {
         LuaValue var26;
         if ((var26 = var19[var25++]) != null) {
            int var27 = hashmod(LuaInteger.hashCode(var25), var14);
            Object var29;
            if (this.m_metatable != null) {
               var29 = this.m_metatable.entry(valueOf(var25), var26);
               if (var29 == null) {
                  continue;
               }
            } else {
               var29 = defaultEntry(valueOf(var25), var26);
            }

            var9[var27] = (Slot)(var9[var27] != null ? var9[var27].add((Slot)var29) : var29);
         }
      }

      this.hash = var9;
      this.array = var8;
      this.hashEntries -= var10;
   }

   public Slot entry(LuaValue var1, LuaValue var2) {
      return defaultEntry(var1, var2);
   }

   protected static boolean isLargeKey(LuaValue var0) {
      switch (var0.type()) {
         case 1:
         case 3:
            return false;
         case 2:
         default:
            return true;
         case 4:
            return var0.rawlen() > 32;
      }
   }

   protected static Entry defaultEntry(LuaValue var0, LuaValue var1) {
      if (var0.isinttype()) {
         return new IntKeyEntry(var0.toint(), var1);
      } else {
         return (Entry)(var1.type() == 3 ? new NumberValueEntry(var0, var1.todouble()) : new NormalEntry(var0, var1));
      }
   }

   public void sort(LuaValue var1) {
      if (this.len().tolong() >= 2147483647L) {
         throw new LuaError("array too big: " + this.len().tolong());
      } else {
         if (this.m_metatable != null && this.m_metatable.useWeakValues()) {
            this.dropWeakArrayValues();
         }

         int var2 = this.array.length;

         while (var2 > 0 && this.array[var2 - 1] == null) {
            var2--;
         }

         if (var2 > 1) {
            this.heapSort(var2, var1);
         }
      }
   }

   private void heapSort(int var1, LuaValue var2) {
      this.heapify(var1, var2);
      int var3 = var1 - 1;

      while (var3 > 0) {
         this.swap(var3, 0);
         this.siftDown(0, --var3, var2);
      }
   }

   private void heapify(int var1, LuaValue var2) {
      for (int var3 = var1 / 2 - 1; var3 >= 0; var3--) {
         this.siftDown(var3, var1 - 1, var2);
      }
   }

   private void siftDown(int var1, int var2, LuaValue var3) {
      int var4 = var1;

      while (var4 * 2 + 1 <= var2) {
         int var5 = var4 * 2 + 1;
         if (var5 < var2 && this.compare(var5, var5 + 1, var3)) {
            var5++;
         }

         if (!this.compare(var4, var5, var3)) {
            return;
         }

         this.swap(var4, var5);
         var4 = var5;
      }
   }

   private boolean compare(int var1, int var2, LuaValue var3) {
      LuaValue var4;
      LuaValue var5;
      if (this.m_metatable == null) {
         var4 = this.array[var1];
         var5 = this.array[var2];
      } else {
         var4 = this.m_metatable.arrayget(this.array, var1);
         var5 = this.m_metatable.arrayget(this.array, var2);
      }

      if (var4 == null || var5 == null) {
         return false;
      } else {
         return !var3.isnil() ? var3.call(var4, var5).toboolean() : var4.lt_b(var5);
      }
   }

   private void swap(int var1, int var2) {
      LuaValue var3 = this.array[var1];
      this.array[var1] = this.array[var2];
      this.array[var2] = var3;
   }

   public int keyCount() {
      LuaValue var1 = LuaValue.NIL;
      int var2 = 0;

      while (true) {
         Varargs var3 = this.next(var1);
         if ((var1 = var3.arg1()).isnil()) {
            return var2;
         }

         var2++;
      }
   }

   public LuaValue[] keys() {
      Vector var1 = new Vector();
      LuaValue var2 = LuaValue.NIL;

      while (true) {
         Varargs var3 = this.next(var2);
         if ((var2 = var3.arg1()).isnil()) {
            LuaValue[] var4 = new LuaValue[var1.size()];
            var1.copyInto(var4);
            return var4;
         }

         var1.addElement(var2);
      }
   }

   public LuaValue eq(LuaValue var1) {
      return this.eq_b(var1) ? TRUE : FALSE;
   }

   public boolean eq_b(LuaValue var1) {
      if (this == var1) {
         return true;
      } else if (this.m_metatable != null && var1.istable()) {
         LuaValue var2 = var1.getmetatable();
         return var2 != null && LuaValue.eqmtcall(this, this.m_metatable.toLuaValue(), var1, var2);
      } else {
         return false;
      }
   }

   public Varargs unpack() {
      return this.unpack(1, this.rawlen());
   }

   public Varargs unpack(int var1) {
      return this.unpack(var1, this.rawlen());
   }

   public Varargs unpack(int var1, int var2) {
      if (var2 < var1) {
         return NONE;
      } else {
         int var3 = var2 - var1;
         if (var3 < 0) {
            throw new LuaError("too many results to unpack: greater 2147483647");
         } else {
            int var4 = 16777215;
            if (var3 >= var4) {
               throw new LuaError("too many results to unpack: " + var3 + " (max is " + var4 + ')');
            } else {
               int var5 = var2 + 1 - var1;
               switch (var5) {
                  case 0:
                     return NONE;
                  case 1:
                     return this.get(var1);
                  case 2:
                     return varargsOf(this.get(var1), this.get(var1 + 1));
                  default:
                     if (var5 < 0) {
                        return NONE;
                     } else {
                        try {
                           LuaValue[] var6 = new LuaValue[var5];

                           while (--var5 >= 0) {
                              var6[var5] = this.get(var1 + var5);
                           }

                           return varargsOf(var6);
                        } catch (OutOfMemoryError var7) {
                           throw new LuaError("too many results to unpack [out of memory]: " + var5);
                        }
                     }
               }
            }
         }
      }
   }

   public boolean useWeakKeys() {
      return false;
   }

   public boolean useWeakValues() {
      return false;
   }

   public LuaValue toLuaValue() {
      return this;
   }

   public LuaValue wrap(LuaValue var1) {
      return var1;
   }

   public LuaValue arrayget(LuaValue[] var1, int var2) {
      return var1[var2];
   }

   private static class DeadSlot implements Slot {
      private final Object key;
      private Slot next;

      private DeadSlot(LuaValue var1, Slot var2) {
         this.key = LuaTable.isLargeKey(var1) ? new WeakReference<>(var1) : var1;
         this.next = var2;
      }

      private LuaValue key() {
         return (LuaValue)(this.key instanceof WeakReference ? ((WeakReference)this.key).get() : this.key);
      }

      public int keyindex(int var1) {
         return 0;
      }

      public StrongSlot first() {
         return null;
      }

      public StrongSlot find(LuaValue var1) {
         return null;
      }

      public boolean keyeq(LuaValue var1) {
         LuaValue var2 = this.key();
         return var2 != null && var1.raweq(var2);
      }

      public Slot rest() {
         return this.next;
      }

      public int arraykey(int var1) {
         return -1;
      }

      public Slot set(StrongSlot var1, LuaValue var2) {
         Slot var3 = this.next != null ? this.next.set(var1, var2) : null;
         if (this.key() != null) {
            this.next = var3;
            return this;
         } else {
            return var3;
         }
      }

      public Slot add(Slot var1) {
         return this.next != null ? this.next.add(var1) : var1;
      }

      public Slot remove(StrongSlot var1) {
         if (this.key() != null) {
            this.next = this.next.remove(var1);
            return this;
         } else {
            return this.next;
         }
      }

      public Slot relink(Slot var1) {
         return var1;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append("<dead");
         LuaValue var2 = this.key();
         if (var2 != null) {
            var1.append(": ");
            var1.append(var2.toString());
         }

         var1.append('>');
         if (this.next != null) {
            var1.append("; ");
            var1.append(this.next.toString());
         }

         return var1.toString();
      }
   }

   abstract static class Entry extends Varargs implements StrongSlot {
      public abstract LuaValue key();

      public abstract LuaValue value();

      abstract Entry set(LuaValue var1);

      public abstract boolean keyeq(LuaValue var1);

      public abstract int keyindex(int var1);

      public int arraykey(int var1) {
         return 0;
      }

      public LuaValue arg(int var1) {
         switch (var1) {
            case 1:
               return this.key();
            case 2:
               return this.value();
            default:
               return LuaValue.NIL;
         }
      }

      public int narg() {
         return 2;
      }

      public Varargs toVarargs() {
         return LuaValue.varargsOf(this.key(), this.value());
      }

      public LuaValue arg1() {
         return this.key();
      }

      public Varargs subargs(int var1) {
         switch (var1) {
            case 1:
               return this;
            case 2:
               return this.value();
            default:
               return LuaValue.NONE;
         }
      }

      public StrongSlot first() {
         return this;
      }

      public Slot rest() {
         return null;
      }

      public StrongSlot find(LuaValue var1) {
         return this.keyeq(var1) ? this : null;
      }

      public Slot set(StrongSlot var1, LuaValue var2) {
         return this.set(var2);
      }

      public Slot add(Slot var1) {
         return new LinkSlot(this, var1);
      }

      public Slot remove(StrongSlot var1) {
         return new DeadSlot(this.key(), null);
      }

      public Slot relink(Slot var1) {
         return (Slot)(var1 != null ? new LinkSlot(this, var1) : this);
      }
   }

   private static class IntKeyEntry extends Entry {
      private final int key;
      private LuaValue value;

      IntKeyEntry(int var1, LuaValue var2) {
         this.key = var1;
         this.value = var2;
      }

      public LuaValue key() {
         return LuaValue.valueOf(this.key);
      }

      public int arraykey(int var1) {
         return this.key >= 1 && this.key <= var1 ? this.key : 0;
      }

      public LuaValue value() {
         return this.value;
      }

      public Entry set(LuaValue var1) {
         this.value = var1;
         return this;
      }

      public int keyindex(int var1) {
         return LuaTable.hashmod(LuaInteger.hashCode(this.key), var1);
      }

      public boolean keyeq(LuaValue var1) {
         return var1.raweq(this.key);
      }
   }

   private static class LinkSlot implements StrongSlot {
      private Entry entry;
      private Slot next;

      LinkSlot(Entry var1, Slot var2) {
         this.entry = var1;
         this.next = var2;
      }

      public LuaValue key() {
         return this.entry.key();
      }

      public int keyindex(int var1) {
         return this.entry.keyindex(var1);
      }

      public LuaValue value() {
         return this.entry.value();
      }

      public Varargs toVarargs() {
         return this.entry.toVarargs();
      }

      public StrongSlot first() {
         return this.entry;
      }

      public StrongSlot find(LuaValue var1) {
         return this.entry.keyeq(var1) ? this : null;
      }

      public boolean keyeq(LuaValue var1) {
         return this.entry.keyeq(var1);
      }

      public Slot rest() {
         return this.next;
      }

      public int arraykey(int var1) {
         return this.entry.arraykey(var1);
      }

      public Slot set(StrongSlot var1, LuaValue var2) {
         if (var1 == this) {
            this.entry = this.entry.set(var2);
            return this;
         } else {
            return this.setnext(this.next.set(var1, var2));
         }
      }

      public Slot add(Slot var1) {
         return this.setnext(this.next.add(var1));
      }

      public Slot remove(StrongSlot var1) {
         if (this == var1) {
            return new DeadSlot(this.key(), this.next);
         } else {
            this.next = this.next.remove(var1);
            return this;
         }
      }

      public Slot relink(Slot var1) {
         return (Slot)(var1 != null ? new LinkSlot(this.entry, var1) : this.entry);
      }

      private Slot setnext(Slot var1) {
         if (var1 != null) {
            this.next = var1;
            return this;
         } else {
            return this.entry;
         }
      }

      public String toString() {
         return this.entry + "; " + this.next;
      }
   }

   static class NormalEntry extends Entry {
      private final LuaValue key;
      private LuaValue value;

      NormalEntry(LuaValue var1, LuaValue var2) {
         this.key = var1;
         this.value = var2;
      }

      public LuaValue key() {
         return this.key;
      }

      public LuaValue value() {
         return this.value;
      }

      public Entry set(LuaValue var1) {
         this.value = var1;
         return this;
      }

      public Varargs toVarargs() {
         return this;
      }

      public int keyindex(int var1) {
         return LuaTable.hashSlot(this.key, var1);
      }

      public boolean keyeq(LuaValue var1) {
         return var1.raweq(this.key);
      }
   }

   private static class NumberValueEntry extends Entry {
      private double value;
      private final LuaValue key;

      NumberValueEntry(LuaValue var1, double var2) {
         this.key = var1;
         this.value = var2;
      }

      public LuaValue key() {
         return this.key;
      }

      public LuaValue value() {
         return LuaValue.valueOf(this.value);
      }

      public Entry set(LuaValue var1) {
         if (var1.type() == 3) {
            LuaValue var2 = var1.tonumber();
            if (!var2.isnil()) {
               this.value = var2.todouble();
               return this;
            }
         }

         return new NormalEntry(this.key, var1);
      }

      public int keyindex(int var1) {
         return LuaTable.hashSlot(this.key, var1);
      }

      public boolean keyeq(LuaValue var1) {
         return var1.raweq(this.key);
      }
   }

   interface Slot {
      int keyindex(int var1);

      StrongSlot first();

      StrongSlot find(LuaValue var1);

      boolean keyeq(LuaValue var1);

      Slot rest();

      int arraykey(int var1);

      Slot set(StrongSlot var1, LuaValue var2);

      Slot add(Slot var1);

      Slot remove(StrongSlot var1);

      Slot relink(Slot var1);
   }

   interface StrongSlot extends Slot {
      LuaValue key();

      LuaValue value();

      Varargs toVarargs();
   }
}
