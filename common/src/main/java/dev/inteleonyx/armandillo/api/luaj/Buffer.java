package dev.inteleonyx.armandillo.api.luaj;

public final class Buffer {
   private static final int DEFAULT_CAPACITY = 64;
   private static final byte[] NOBYTES = new byte[0];
   private byte[] bytes;
   private int length;
   private int offset;
   private LuaValue value;

   public Buffer() {
      this(64);
   }

   public Buffer(int var1) {
      this.bytes = new byte[var1];
      this.length = 0;
      this.offset = 0;
      this.value = null;
   }

   public Buffer(LuaValue var1) {
      this.bytes = NOBYTES;
      this.length = this.offset = 0;
      this.value = var1;
   }

   public LuaValue value() {
      return (LuaValue)(this.value != null ? this.value : this.tostring());
   }

   public Buffer setvalue(LuaValue var1) {
      this.bytes = NOBYTES;
      this.offset = this.length = 0;
      this.value = var1;
      return this;
   }

   public final LuaString tostring() {
      this.realloc(this.length, 0);
      return LuaString.valueOf(this.bytes, this.offset, this.length);
   }

   public String tojstring() {
      return this.value().tojstring();
   }

   public String toString() {
      return this.tojstring();
   }

   public final Buffer append(byte var1) {
      this.makeroom(0, 1);
      this.bytes[this.offset + this.length++] = var1;
      return this;
   }

   public final Buffer append(LuaValue var1) {
      this.append(var1.strvalue());
      return this;
   }

   public final Buffer append(LuaString var1) {
      int var2 = var1.m_length;
      this.makeroom(0, var2);
      var1.copyInto(0, this.bytes, this.offset + this.length, var2);
      this.length += var2;
      return this;
   }

   public final Buffer append(String var1) {
      char[] var2 = var1.toCharArray();
      int var3 = LuaString.lengthAsUtf8(var2);
      this.makeroom(0, var3);
      LuaString.encodeToUtf8(var2, var2.length, this.bytes, this.offset + this.length);
      this.length += var3;
      return this;
   }

   public Buffer concatTo(LuaValue var1) {
      return this.setvalue(var1.concat(this.value()));
   }

   public Buffer concatTo(LuaString var1) {
      return this.value != null && !this.value.isstring() ? this.setvalue(var1.concat(this.value)) : this.prepend(var1);
   }

   public Buffer concatTo(LuaNumber var1) {
      return this.value != null && !this.value.isstring() ? this.setvalue(var1.concat(this.value)) : this.prepend(var1.strvalue());
   }

   public Buffer prepend(LuaString var1) {
      int var2 = var1.m_length;
      this.makeroom(var2, 0);
      System.arraycopy(var1.m_bytes, var1.m_offset, this.bytes, this.offset - var2, var2);
      this.offset -= var2;
      this.length += var2;
      this.value = null;
      return this;
   }

   public final void makeroom(int var1, int var2) {
      if (this.value != null) {
         LuaString var3 = this.value.strvalue();
         this.value = null;
         this.length = var3.m_length;
         this.offset = var1;
         this.bytes = new byte[var1 + this.length + var2];
         System.arraycopy(var3.m_bytes, var3.m_offset, this.bytes, this.offset, this.length);
      } else if (this.offset + this.length + var2 > this.bytes.length || this.offset < var1) {
         int var5 = var1 + this.length + var2;
         int var4 = var5 < 32 ? 32 : (var5 < this.length * 2 ? this.length * 2 : var5);
         this.realloc(var4, var1 == 0 ? 0 : var4 - this.length - var2);
      }
   }

   private final void realloc(int var1, int var2) {
      if (var1 != this.bytes.length) {
         byte[] var3 = new byte[var1];
         System.arraycopy(this.bytes, this.offset, var3, var2, this.length);
         this.bytes = var3;
         this.offset = var2;
      }
   }
}
