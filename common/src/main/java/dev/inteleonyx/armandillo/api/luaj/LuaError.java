package dev.inteleonyx.armandillo.api.luaj;

public class LuaError extends RuntimeException {
   private static final long serialVersionUID = 1L;
   protected int level;
   protected String fileline;
   protected String traceback;
   protected Throwable cause;
   private LuaValue object;

   public String getMessage() {
      if (this.traceback != null) {
         return this.traceback;
      } else {
         String var1 = super.getMessage();
         if (var1 == null) {
            return null;
         } else {
            return this.fileline != null ? this.fileline + " " + var1 : var1;
         }
      }
   }

   public LuaValue getMessageObject() {
      if (this.object != null) {
         return this.object;
      } else {
         String var1 = this.getMessage();
         return var1 != null ? LuaValue.valueOf(var1) : null;
      }
   }

   public LuaError(Throwable var1) {
      super("vm error: " + var1);
      this.cause = var1;
      this.level = 1;
   }

   public LuaError(String var1) {
      super(var1);
      this.level = 1;
   }

   public LuaError(String var1, int var2) {
      super(var1);
      this.level = var2;
   }

   public LuaError(LuaValue var1) {
      super(var1.tojstring());
      this.object = var1;
      this.level = 1;
   }

   public Throwable getCause() {
      return this.cause;
   }
}
