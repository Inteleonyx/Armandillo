package dev.inteleonyx.armandillo.api.luaj;

public class LuaUserdata extends LuaValue {
   public Object m_instance;
   public LuaValue m_metatable;

   public LuaUserdata(Object var1) {
      this.m_instance = var1;
   }

   public LuaUserdata(Object var1, LuaValue var2) {
      this.m_instance = var1;
      this.m_metatable = var2;
   }

   public String tojstring() {
      return String.valueOf(this.m_instance);
   }

   public int type() {
      return 7;
   }

   public String typename() {
      return "userdata";
   }

   public int hashCode() {
      return this.m_instance.hashCode();
   }

   public Object userdata() {
      return this.m_instance;
   }

   public boolean isuserdata() {
      return true;
   }

   public boolean isuserdata(Class var1) {
      return var1.isAssignableFrom(this.m_instance.getClass());
   }

   public Object touserdata() {
      return this.m_instance;
   }

   public Object touserdata(Class var1) {
      return var1.isAssignableFrom(this.m_instance.getClass()) ? this.m_instance : null;
   }

   public Object optuserdata(Object var1) {
      return this.m_instance;
   }

   public Object optuserdata(Class var1, Object var2) {
      if (!var1.isAssignableFrom(this.m_instance.getClass())) {
         this.typerror(var1.getName());
      }

      return this.m_instance;
   }

   public LuaValue getmetatable() {
      return this.m_metatable;
   }

   public LuaValue setmetatable(LuaValue var1) {
      this.m_metatable = var1;
      return this;
   }

   public Object checkuserdata() {
      return this.m_instance;
   }

   public Object checkuserdata(Class var1) {
      return var1.isAssignableFrom(this.m_instance.getClass()) ? this.m_instance : this.typerror(var1.getName());
   }

   public LuaValue get(LuaValue var1) {
      return this.m_metatable != null ? gettable(this, var1) : NIL;
   }

   public void set(LuaValue var1, LuaValue var2) {
      if (this.m_metatable == null || !settable(this, var1, var2)) {
         error("cannot set " + var1 + " for userdata");
      }
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof LuaUserdata)) {
         return false;
      } else {
         LuaUserdata var2 = (LuaUserdata)var1;
         return this.m_instance.equals(var2.m_instance);
      }
   }

   public LuaValue eq(LuaValue var1) {
      return this.eq_b(var1) ? TRUE : FALSE;
   }

   public boolean eq_b(LuaValue var1) {
      if (var1.raweq(this)) {
         return true;
      } else if (this.m_metatable != null && var1.isuserdata()) {
         LuaValue var2 = var1.getmetatable();
         return var2 != null && LuaValue.eqmtcall(this, this.m_metatable, var1, var2);
      } else {
         return false;
      }
   }

   public boolean raweq(LuaValue var1) {
      return var1.raweq(this);
   }

   public boolean raweq(LuaUserdata var1) {
      return this == var1 || this.m_metatable == var1.m_metatable && this.m_instance.equals(var1.m_instance);
   }

   public boolean eqmt(LuaValue var1) {
      return this.m_metatable != null && var1.isuserdata() ? LuaValue.eqmtcall(this, this.m_metatable, var1, var1.getmetatable()) : false;
   }
}
