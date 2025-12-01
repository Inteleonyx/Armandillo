package dev.inteleonyx.armandillo.api.luaj;

import java.lang.ref.WeakReference;

public class WeakTable implements Metatable {
   private boolean weakkeys;
   private boolean weakvalues;
   private LuaValue backing;

   public static LuaTable make(boolean var0, boolean var1) {
      LuaString var2;
      if (var0 && var1) {
         var2 = LuaString.valueOf("kv");
      } else if (var0) {
         var2 = LuaString.valueOf("k");
      } else {
         if (!var1) {
            return LuaTable.tableOf();
         }

         var2 = LuaString.valueOf("v");
      }

      LuaTable var3 = LuaTable.tableOf();
      LuaTable var4 = LuaTable.tableOf(new LuaValue[]{LuaValue.MODE, var2});
      var3.setmetatable(var4);
      return var3;
   }

   public WeakTable(boolean var1, boolean var2, LuaValue var3) {
      this.weakkeys = var1;
      this.weakvalues = var2;
      this.backing = var3;
   }

   public boolean useWeakKeys() {
      return this.weakkeys;
   }

   public boolean useWeakValues() {
      return this.weakvalues;
   }

   public LuaValue toLuaValue() {
      return this.backing;
   }

   public LuaTable.Slot entry(LuaValue var1, LuaValue var2) {
      var2 = var2.strongvalue();
      if (var2 == null) {
         return null;
      } else if (this.weakkeys && !var1.isnumber() && !var1.isstring() && !var1.isboolean()) {
         return (LuaTable.Slot)(this.weakvalues && !var2.isnumber() && !var2.isstring() && !var2.isboolean()
            ? new WeakKeyAndValueSlot(var1, var2, null)
            : new WeakKeySlot(var1, var2, null));
      } else {
         return (LuaTable.Slot)(this.weakvalues && !var2.isnumber() && !var2.isstring() && !var2.isboolean()
            ? new WeakValueSlot(var1, var2, null)
            : LuaTable.defaultEntry(var1, var2));
      }
   }

   protected static LuaValue weaken(LuaValue var0) {
      switch (var0.type()) {
         case 5:
         case 6:
         case 8:
            return new WeakValue(var0);
         case 7:
            return new WeakUserdata(var0);
         default:
            return var0;
      }
   }

   protected static LuaValue strengthen(Object var0) {
      if (var0 instanceof WeakReference) {
         var0 = ((WeakReference)var0).get();
      }

      return var0 instanceof WeakValue ? ((WeakValue)var0).strongvalue() : (LuaValue)var0;
   }

   public LuaValue wrap(LuaValue var1) {
      return this.weakvalues ? weaken(var1) : var1;
   }

   public LuaValue arrayget(LuaValue[] var1, int var2) {
      LuaValue var3 = var1[var2];
      if (var3 != null) {
         var3 = strengthen(var3);
         if (var3 == null) {
            var1[var2] = null;
         }
      }

      return var3;
   }

   static class WeakKeyAndValueSlot extends WeakSlot {
      private final int keyhash;

      protected WeakKeyAndValueSlot(LuaValue var1, LuaValue var2, LuaTable.Slot var3) {
         super(WeakTable.weaken(var1), WeakTable.weaken(var2), var3);
         this.keyhash = var1.hashCode();
      }

      protected WeakKeyAndValueSlot(WeakKeyAndValueSlot var1, LuaTable.Slot var2) {
         super(var1.key, var1.value, var2);
         this.keyhash = var1.keyhash;
      }

      public int keyindex(int var1) {
         return LuaTable.hashmod(this.keyhash, var1);
      }

      public LuaTable.Slot set(LuaValue var1) {
         this.value = WeakTable.weaken(var1);
         return this;
      }

      public LuaValue strongkey() {
         return WeakTable.strengthen(this.key);
      }

      public LuaValue strongvalue() {
         return WeakTable.strengthen(this.value);
      }

      protected WeakSlot copy(LuaTable.Slot var1) {
         return new WeakKeyAndValueSlot(this, var1);
      }
   }

   static class WeakKeySlot extends WeakSlot {
      private final int keyhash;

      protected WeakKeySlot(LuaValue var1, LuaValue var2, LuaTable.Slot var3) {
         super(WeakTable.weaken(var1), var2, var3);
         this.keyhash = var1.hashCode();
      }

      protected WeakKeySlot(WeakKeySlot var1, LuaTable.Slot var2) {
         super(var1.key, var1.value, var2);
         this.keyhash = var1.keyhash;
      }

      public int keyindex(int var1) {
         return LuaTable.hashmod(this.keyhash, var1);
      }

      public LuaTable.Slot set(LuaValue var1) {
         this.value = var1;
         return this;
      }

      public LuaValue strongkey() {
         return WeakTable.strengthen(this.key);
      }

      protected WeakSlot copy(LuaTable.Slot var1) {
         return new WeakKeySlot(this, var1);
      }
   }

   public abstract static class WeakSlot implements LuaTable.Slot {
      protected Object key;
      protected Object value;
      protected LuaTable.Slot next;

      protected WeakSlot(Object var1, Object var2, LuaTable.Slot var3) {
         this.key = var1;
         this.value = var2;
         this.next = var3;
      }

      public abstract int keyindex(int var1);

      public abstract LuaTable.Slot set(LuaValue var1);

      public LuaTable.StrongSlot first() {
         LuaValue var1 = this.strongkey();
         LuaValue var2 = this.strongvalue();
         if (var1 != null && var2 != null) {
            return new LuaTable.NormalEntry(var1, var2);
         } else {
            this.key = null;
            this.value = null;
            return null;
         }
      }

      public LuaTable.StrongSlot find(LuaValue var1) {
         LuaTable.StrongSlot var2 = this.first();
         return var2 != null ? var2.find(var1) : null;
      }

      public boolean keyeq(LuaValue var1) {
         LuaTable.StrongSlot var2 = this.first();
         return var2 != null && var2.keyeq(var1);
      }

      public LuaTable.Slot rest() {
         return this.next;
      }

      public int arraykey(int var1) {
         return 0;
      }

      public LuaTable.Slot set(LuaTable.StrongSlot var1, LuaValue var2) {
         LuaValue var3 = this.strongkey();
         if (var3 != null && var1.find(var3) != null) {
            return this.set(var2);
         } else if (var3 != null) {
            this.next = this.next.set(var1, var2);
            return this;
         } else {
            return this.next.set(var1, var2);
         }
      }

      public LuaTable.Slot add(LuaTable.Slot var1) {
         this.next = this.next != null ? this.next.add(var1) : var1;
         return (LuaTable.Slot)(this.strongkey() != null && this.strongvalue() != null ? this : this.next);
      }

      public LuaTable.Slot remove(LuaTable.StrongSlot var1) {
         LuaValue var2 = this.strongkey();
         if (var2 == null) {
            return this.next.remove(var1);
         } else if (var1.keyeq(var2)) {
            this.value = null;
            return this;
         } else {
            this.next = this.next.remove(var1);
            return this;
         }
      }

      public LuaTable.Slot relink(LuaTable.Slot var1) {
         if (this.strongkey() == null || this.strongvalue() == null) {
            return var1;
         } else {
            return var1 == null && this.next == null ? this : this.copy(var1);
         }
      }

      public LuaValue strongkey() {
         return (LuaValue)this.key;
      }

      public LuaValue strongvalue() {
         return (LuaValue)this.value;
      }

      protected abstract WeakSlot copy(LuaTable.Slot var1);
   }

   static final class WeakUserdata extends WeakValue {
      private final WeakReference ob;
      private final LuaValue mt;

      private WeakUserdata(LuaValue var1) {
         super(var1);
         this.ob = new WeakReference<>(var1.touserdata());
         this.mt = var1.getmetatable();
      }

      public LuaValue strongvalue() {
         Object var1 = this.ref.get();
         if (var1 != null) {
            return (LuaValue)var1;
         } else {
            Object var2 = this.ob.get();
            if (var2 != null) {
               LuaUserdata var3 = LuaValue.userdataOf(var2, this.mt);
               this.ref = new WeakReference<>(var3);
               return var3;
            } else {
               return null;
            }
         }
      }
   }

   static class WeakValue extends LuaValue {
      WeakReference ref;

      protected WeakValue(LuaValue var1) {
         this.ref = new WeakReference<>(var1);
      }

      public int type() {
         this.illegal("type", "weak value");
         return 0;
      }

      public String typename() {
         this.illegal("typename", "weak value");
         return null;
      }

      public String toString() {
         return "weak<" + this.ref.get() + ">";
      }

      public LuaValue strongvalue() {
         Object var1 = this.ref.get();
         return (LuaValue)var1;
      }

      public boolean raweq(LuaValue var1) {
         Object var2 = this.ref.get();
         return var2 != null && var1.raweq((LuaValue)var2);
      }
   }

   static class WeakValueSlot extends WeakSlot {
      protected WeakValueSlot(LuaValue var1, LuaValue var2, LuaTable.Slot var3) {
         super(var1, WeakTable.weaken(var2), var3);
      }

      protected WeakValueSlot(WeakValueSlot var1, LuaTable.Slot var2) {
         super(var1.key, var1.value, var2);
      }

      public int keyindex(int var1) {
         return LuaTable.hashSlot(this.strongkey(), var1);
      }

      public LuaTable.Slot set(LuaValue var1) {
         this.value = WeakTable.weaken(var1);
         return this;
      }

      public LuaValue strongvalue() {
         return WeakTable.strengthen(this.value);
      }

      protected WeakSlot copy(LuaTable.Slot var1) {
         return new WeakValueSlot(this, var1);
      }
   }
}
