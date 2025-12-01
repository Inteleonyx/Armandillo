package dev.inteleonyx.armandillo.api.luaj.luajc;

import dev.inteleonyx.armandillo.api.luaj.*;
import dev.inteleonyx.armandillo.api.luaj.lib.*;
import org.apache.bcel.generic.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JavaBuilder {
   private static final String STR_VARARGS = Varargs.class.getName();
   private static final String STR_LUAVALUE = LuaValue.class.getName();
   private static final String STR_LUASTRING = LuaString.class.getName();
   private static final String STR_LUAINTEGER = LuaInteger.class.getName();
   private static final String STR_LUANUMBER = LuaNumber.class.getName();
   private static final String STR_LUABOOLEAN = LuaBoolean.class.getName();
   private static final String STR_LUATABLE = LuaTable.class.getName();
   private static final String STR_BUFFER = Buffer.class.getName();
   private static final String STR_STRING = String.class.getName();

   private static final String STR_JSEPLATFORM = "dev.inteleonyx.luaj.vm2.lib.jse.JsePlatform";

   private static final ObjectType TYPE_VARARGS = new ObjectType(STR_VARARGS);
   private static final ObjectType TYPE_LUAVALUE = new ObjectType(STR_LUAVALUE);
   private static final ObjectType TYPE_LUASTRING = new ObjectType(STR_LUASTRING);
   private static final ObjectType TYPE_LUAINTEGER = new ObjectType(STR_LUAINTEGER);
   private static final ObjectType TYPE_LUANUMBER = new ObjectType(STR_LUANUMBER);
   private static final ObjectType TYPE_LUABOOLEAN = new ObjectType(STR_LUABOOLEAN);
   private static final ObjectType TYPE_LUATABLE = new ObjectType(STR_LUATABLE);
   private static final ObjectType TYPE_BUFFER = new ObjectType(STR_BUFFER);
   private static final ObjectType TYPE_STRING = new ObjectType(STR_STRING);

   private static final ArrayType TYPE_LOCALUPVALUE = new ArrayType(TYPE_LUAVALUE, 1);
   private static final ArrayType TYPE_CHARARRAY = new ArrayType(Type.CHAR, 1);
   private static final ArrayType TYPE_STRINGARRAY = new ArrayType(TYPE_STRING, 1);

   private static final String STR_FUNCV = VarArgFunction.class.getName();
   private static final String STR_FUNC0 = ZeroArgFunction.class.getName();
   private static final String STR_FUNC1 = OneArgFunction.class.getName();
   private static final String STR_FUNC2 = TwoArgFunction.class.getName();
   private static final String STR_FUNC3 = ThreeArgFunction.class.getName();

   private static final Type[] ARG_TYPES_NONE = new Type[0];
   private static final Type[] ARG_TYPES_INT = new Type[]{Type.INT};
   private static final Type[] ARG_TYPES_DOUBLE = new Type[]{Type.DOUBLE};
   private static final Type[] ARG_TYPES_STRING = new Type[]{Type.STRING};
   private static final Type[] ARG_TYPES_CHARARRAY = new Type[]{TYPE_CHARARRAY};
   private static final Type[] ARG_TYPES_INT_LUAVALUE = new Type[]{Type.INT, TYPE_LUAVALUE};
   private static final Type[] ARG_TYPES_INT_VARARGS = new Type[]{Type.INT, TYPE_VARARGS};
   private static final Type[] ARG_TYPES_LUAVALUE_VARARGS = new Type[]{TYPE_LUAVALUE, TYPE_VARARGS};
   private static final Type[] ARG_TYPES_LUAVALUE_LUAVALUE_VARARGS = new Type[]{TYPE_LUAVALUE, TYPE_LUAVALUE, TYPE_VARARGS};
   private static final Type[] ARG_TYPES_LUAVALUEARRAY = new Type[]{new ArrayType(TYPE_LUAVALUE, 1)};
   private static final Type[] ARG_TYPES_LUAVALUEARRAY_VARARGS = new Type[]{new ArrayType(TYPE_LUAVALUE, 1), TYPE_VARARGS};
   private static final Type[] ARG_TYPES_LUAVALUE_LUAVALUE_LUAVALUE = new Type[]{TYPE_LUAVALUE, TYPE_LUAVALUE, TYPE_LUAVALUE};
   private static final Type[] ARG_TYPES_VARARGS = new Type[]{TYPE_VARARGS};
   private static final Type[] ARG_TYPES_LUAVALUE_LUAVALUE = new Type[]{TYPE_LUAVALUE, TYPE_LUAVALUE};
   private static final Type[] ARG_TYPES_INT_INT = new Type[]{Type.INT, Type.INT};
   private static final Type[] ARG_TYPES_LUAVALUE = new Type[]{TYPE_LUAVALUE};
   private static final Type[] ARG_TYPES_BUFFER = new Type[]{TYPE_BUFFER};
   private static final Type[] ARG_TYPES_STRINGARRAY = new Type[]{TYPE_STRINGARRAY};
   private static final Type[] ARG_TYPES_LUAVALUE_STRINGARRAY = new Type[]{TYPE_LUAVALUE, TYPE_STRINGARRAY};
   private static final String[] SUPER_NAME_N = new String[]{STR_FUNC0, STR_FUNC1, STR_FUNC2, STR_FUNC3, STR_FUNCV};
   private static final ObjectType[] RETURN_TYPE_N = new ObjectType[]{TYPE_LUAVALUE, TYPE_LUAVALUE, TYPE_LUAVALUE, TYPE_LUAVALUE, TYPE_VARARGS};
   private static final Type[][] ARG_TYPES_N = new Type[][]{
      ARG_TYPES_NONE, ARG_TYPES_LUAVALUE, ARG_TYPES_LUAVALUE_LUAVALUE, ARG_TYPES_LUAVALUE_LUAVALUE_LUAVALUE, ARG_TYPES_VARARGS
   };
   private static final String[][] ARG_NAMES_N = new String[][]{new String[0], {"arg"}, {"arg1", "arg2"}, {"arg1", "arg2", "arg3"}, {"args"}};
   private static final String[] METH_NAME_N = new String[]{"call", "call", "call", "call", "onInvoke"};
   private static final String PREFIX_CONSTANT = "k";
   private static final String PREFIX_UPVALUE = "u";
   private static final String PREFIX_PLAIN_SLOT = "s";
   private static final String PREFIX_UPVALUE_SLOT = "a";
   private static final String NAME_VARRESULT = "v";
   private final ProtoInfo pi;
   private final Prototype p;
   private final String classname;
   private final ClassGen cg;
   private final ConstantPoolGen cp;
   private final InstructionFactory factory;
   private final InstructionList init;
   private final InstructionList main;
   private final MethodGen mg;
   private int superclassType;
   private static int SUPERTYPE_VARARGS = 4;
   private final int[] targets;
   private final BranchInstruction[] branches;
   private final InstructionHandle[] branchDestHandles;
   private final InstructionHandle[] lastInstrHandles;
   private InstructionHandle beginningOfLuaInstruction;
   private LocalVariableGen varresult = null;
   private int prev_line = -1;
   private Map plainSlotVars = new HashMap();
   private Map upvalueSlotVars = new HashMap();
   private Map localVarGenBySlot = new HashMap();
   private Map constants = new HashMap();
   public static final int BRANCH_GOTO = 1;
   public static final int BRANCH_IFNE = 2;
   public static final int BRANCH_IFEQ = 3;

   public JavaBuilder(ProtoInfo var1, String var2, String var3) {
      this.pi = var1;
      this.p = var1.prototype;
      this.classname = var2;
      this.superclassType = this.p.numparams;
      if (this.p.is_vararg != 0 || this.superclassType >= SUPERTYPE_VARARGS) {
         this.superclassType = SUPERTYPE_VARARGS;
      }

      int var4 = 0;

      for (int var5 = this.p.code.length; var4 < var5; var4++) {
         int var6 = this.p.code[var4];
         int var7 = Lua.GET_OPCODE(var6);
         if (var7 == 30 || var7 == 31 && (Lua.GETARG_B(var6) < 1 || Lua.GETARG_B(var6) > 2)) {
            this.superclassType = SUPERTYPE_VARARGS;
            break;
         }
      }

      this.cg = new ClassGen(var2, SUPER_NAME_N[this.superclassType], var3, 33, null);
      this.cp = this.cg.getConstantPool();
      this.factory = new InstructionFactory(this.cg);
      this.init = new InstructionList();
      this.main = new InstructionList();

      for (int var8 = 0; var8 < this.p.upvalues.length; var8++) {
         boolean var10 = var1.isReadWriteUpvalue(var1.upvals[var8]);
         Object var11 = var10 ? TYPE_LOCALUPVALUE : TYPE_LUAVALUE;
         FieldGen var12 = new FieldGen(0, (Type)var11, upvalueName(var8), this.cp);
         this.cg.addField(var12.getField());
      }

      this.mg = new MethodGen(
         17,
         RETURN_TYPE_N[this.superclassType],
         ARG_TYPES_N[this.superclassType],
         ARG_NAMES_N[this.superclassType],
         METH_NAME_N[this.superclassType],
         STR_LUAVALUE,
         this.main,
         this.cp
      );
      this.initializeSlots();
      var4 = this.p.code.length;
      this.targets = new int[var4];
      this.branches = new BranchInstruction[var4];
      this.branchDestHandles = new InstructionHandle[var4];
      this.lastInstrHandles = new InstructionHandle[var4];
   }

   public void initializeSlots() {
      int var1 = 0;
      this.createUpvalues(-1, 0, this.p.maxstacksize);
      if (this.superclassType == SUPERTYPE_VARARGS) {
         for (var1 = 0; var1 < this.p.numparams; var1++) {
            if (this.pi.isInitialValueUsed(var1)) {
               this.append(new ALOAD(1));
               this.append(new PUSH(this.cp, var1 + 1));
               this.append(this.factory.createInvoke(STR_VARARGS, "arg", TYPE_LUAVALUE, ARG_TYPES_INT, (short)182));
               this.storeLocal(-1, var1);
            }
         }

         this.append(new ALOAD(1));
         this.append(new PUSH(this.cp, 1 + this.p.numparams));
         this.append(this.factory.createInvoke(STR_VARARGS, "subargs", TYPE_VARARGS, ARG_TYPES_INT, (short)182));
         this.append(new ASTORE(1));
      } else {
         for (var1 = 0; var1 < this.p.numparams; var1++) {
            this.plainSlotVars.put(var1, 1 + var1);
            if (this.pi.isUpvalueCreate(-1, var1)) {
               this.append(new ALOAD(1 + var1));
               this.storeLocal(-1, var1);
            }
         }
      }

      for (; var1 < this.p.maxstacksize; var1++) {
         if (this.pi.isInitialValueUsed(var1)) {
            this.loadNil();
            this.storeLocal(-1, var1);
         }
      }
   }

   public byte[] completeClass(boolean var1) {
      if (!this.init.isEmpty()) {
         MethodGen var2 = new MethodGen(8, Type.VOID, ARG_TYPES_NONE, new String[0], "<clinit>", this.cg.getClassName(), this.init, this.cg.getConstantPool());
         this.init.append(InstructionConstants.RETURN);
         var2.setMaxStack();
         this.cg.addMethod(var2.getMethod());
         this.init.dispose();
      }

      this.cg.addEmptyConstructor(1);
      this.resolveBranches();
      this.mg.setMaxStack();
      this.cg.addMethod(this.mg.getMethod());
      this.main.dispose();
      if (this.p.upvalues.length == 1 && this.superclassType == SUPERTYPE_VARARGS) {
         MethodGen var5 = new MethodGen(17, Type.VOID, ARG_TYPES_LUAVALUE, new String[]{"env"}, "initupvalue1", STR_LUAVALUE, this.main, this.cp);
         boolean var3 = this.pi.isReadWriteUpvalue(this.pi.upvals[0]);
         this.append(InstructionConstants.THIS);
         this.append(new ALOAD(1));
         if (var3) {
            this.append(this.factory.createInvoke(this.classname, "newupl", TYPE_LOCALUPVALUE, ARG_TYPES_LUAVALUE, (short)184));
            this.append(this.factory.createFieldAccess(this.classname, upvalueName(0), TYPE_LOCALUPVALUE, (short)181));
         } else {
            this.append(this.factory.createFieldAccess(this.classname, upvalueName(0), TYPE_LUAVALUE, (short)181));
         }

         this.append(InstructionConstants.RETURN);
         var5.setMaxStack();
         this.cg.addMethod(var5.getMethod());
         this.main.dispose();
      }

      if (var1) {
         MethodGen var6 = new MethodGen(9, Type.VOID, ARG_TYPES_STRINGARRAY, new String[]{"arg"}, "main", this.classname, this.main, this.cp);
         this.append(this.factory.createNew(this.classname));
         this.append(InstructionConstants.DUP);
         this.append(this.factory.createInvoke(this.classname, "<init>", Type.VOID, ARG_TYPES_NONE, (short)183));
         this.append(new ALOAD(0));
         this.append(this.factory.createInvoke("dev.inteleonyx.luaj.vm2.lib.jse.JsePlatform", "luaMain", Type.VOID, ARG_TYPES_LUAVALUE_STRINGARRAY, (short)184));
         this.append(InstructionConstants.RETURN);
         var6.setMaxStack();
         this.cg.addMethod(var6.getMethod());
         this.main.dispose();
      }

      try {
         ByteArrayOutputStream var7 = new ByteArrayOutputStream();
         this.cg.getJavaClass().dump(var7);
         return var7.toByteArray();
      } catch (IOException var4) {
         throw new RuntimeException("JavaClass.dump() threw " + var4);
      }
   }

   public void dup() {
      this.append(InstructionConstants.DUP);
   }

   public void pop() {
      this.append(InstructionConstants.POP);
   }

   public void loadNil() {
      this.append(this.factory.createFieldAccess(STR_LUAVALUE, "NIL", TYPE_LUAVALUE, (short)178));
   }

   public void loadNone() {
      this.append(this.factory.createFieldAccess(STR_LUAVALUE, "NONE", TYPE_LUAVALUE, (short)178));
   }

   public void loadBoolean(boolean var1) {
      String var2 = var1 ? "TRUE" : "FALSE";
      this.append(this.factory.createFieldAccess(STR_LUAVALUE, var2, TYPE_LUABOOLEAN, (short)178));
   }

   private int findSlot(int var1, Map var2, String var3, Type var4) {
      Integer var5 = var1;
      if (var2.containsKey(var5)) {
         return (Integer)var2.get(var5);
      } else {
         String var6 = var3 + var1;
         LocalVariableGen var7 = this.mg.addLocalVariable(var6, var4, null, null);
         int var8 = var7.getIndex();
         var2.put(var5, var8);
         this.localVarGenBySlot.put(var5, var7);
         return var8;
      }
   }

   private int findSlotIndex(int var1, boolean var2) {
      return var2 ? this.findSlot(var1, this.upvalueSlotVars, "a", TYPE_LOCALUPVALUE) : this.findSlot(var1, this.plainSlotVars, "s", TYPE_LUAVALUE);
   }

   public void loadLocal(int var1, int var2) {
      boolean var3 = this.pi.isUpvalueRefer(var1, var2);
      int var4 = this.findSlotIndex(var2, var3);
      this.append(new ALOAD(var4));
      if (var3) {
         this.append(new PUSH(this.cp, 0));
         this.append(InstructionConstants.AALOAD);
      }
   }

   public void storeLocal(int var1, int var2) {
      boolean var3 = this.pi.isUpvalueAssign(var1, var2);
      int var4 = this.findSlotIndex(var2, var3);
      if (var3) {
         boolean var5 = this.pi.isUpvalueCreate(var1, var2);
         if (var5) {
            this.append(this.factory.createInvoke(this.classname, "newupe", TYPE_LOCALUPVALUE, ARG_TYPES_NONE, (short)184));
            this.append(InstructionConstants.DUP);
            this.append(new ASTORE(var4));
         } else {
            this.append(new ALOAD(var4));
         }

         this.append(InstructionConstants.SWAP);
         this.append(new PUSH(this.cp, 0));
         this.append(InstructionConstants.SWAP);
         this.append(InstructionConstants.AASTORE);
      } else {
         this.append(new ASTORE(var4));
      }
   }

   public void createUpvalues(int var1, int var2, int var3) {
      for (int var4 = 0; var4 < var3; var4++) {
         int var5 = var2 + var4;
         boolean var6 = this.pi.isUpvalueCreate(var1, var5);
         if (var6) {
            int var7 = this.findSlotIndex(var5, true);
            this.append(this.factory.createInvoke(this.classname, "newupn", TYPE_LOCALUPVALUE, ARG_TYPES_NONE, (short)184));
            this.append(new ASTORE(var7));
         }
      }
   }

   public void convertToUpvalue(int var1, int var2) {
      boolean var3 = this.pi.isUpvalueAssign(var1, var2);
      if (var3) {
         int var4 = this.findSlotIndex(var2, false);
         this.append(new ALOAD(var4));
         this.append(this.factory.createInvoke(this.classname, "newupl", TYPE_LOCALUPVALUE, ARG_TYPES_LUAVALUE, (short)184));
         int var5 = this.findSlotIndex(var2, true);
         this.append(new ASTORE(var5));
      }
   }

   private static String upvalueName(int var0) {
      return "u" + var0;
   }

   public void loadUpvalue(int var1) {
      boolean var2 = this.pi.isReadWriteUpvalue(this.pi.upvals[var1]);
      this.append(InstructionConstants.THIS);
      if (var2) {
         this.append(this.factory.createFieldAccess(this.classname, upvalueName(var1), TYPE_LOCALUPVALUE, (short)180));
         this.append(new PUSH(this.cp, 0));
         this.append(InstructionConstants.AALOAD);
      } else {
         this.append(this.factory.createFieldAccess(this.classname, upvalueName(var1), TYPE_LUAVALUE, (short)180));
      }
   }

   public void storeUpvalue(int var1, int var2, int var3) {
      boolean var4 = this.pi.isReadWriteUpvalue(this.pi.upvals[var2]);
      this.append(InstructionConstants.THIS);
      if (var4) {
         this.append(this.factory.createFieldAccess(this.classname, upvalueName(var2), TYPE_LOCALUPVALUE, (short)180));
         this.append(new PUSH(this.cp, 0));
         this.loadLocal(var1, var3);
         this.append(InstructionConstants.AASTORE);
      } else {
         this.loadLocal(var1, var3);
         this.append(this.factory.createFieldAccess(this.classname, upvalueName(var2), TYPE_LUAVALUE, (short)181));
      }
   }

   public void newTable(int var1, int var2) {
      this.append(new PUSH(this.cp, var1));
      this.append(new PUSH(this.cp, var2));
      this.append(this.factory.createInvoke(STR_LUAVALUE, "tableOf", TYPE_LUATABLE, ARG_TYPES_INT_INT, (short)184));
   }

   public void loadVarargs() {
      this.append(new ALOAD(1));
   }

   public void loadVarargs(int var1) {
      this.loadVarargs();
      this.arg(var1);
   }

   public void arg(int var1) {
      if (var1 == 1) {
         this.append(this.factory.createInvoke(STR_VARARGS, "arg1", TYPE_LUAVALUE, ARG_TYPES_NONE, (short)182));
      } else {
         this.append(new PUSH(this.cp, var1));
         this.append(this.factory.createInvoke(STR_VARARGS, "arg", TYPE_LUAVALUE, ARG_TYPES_INT, (short)182));
      }
   }

   private int getVarresultIndex() {
      if (this.varresult == null) {
         this.varresult = this.mg.addLocalVariable("v", TYPE_VARARGS, null, null);
      }

      return this.varresult.getIndex();
   }

   public void loadVarresult() {
      this.append(new ALOAD(this.getVarresultIndex()));
   }

   public void storeVarresult() {
      this.append(new ASTORE(this.getVarresultIndex()));
   }

   public void subargs(int var1) {
      this.append(new PUSH(this.cp, var1));
      this.append(this.factory.createInvoke(STR_VARARGS, "subargs", TYPE_VARARGS, ARG_TYPES_INT, (short)182));
   }

   public void getTable() {
      this.append(this.factory.createInvoke(STR_LUAVALUE, "get", TYPE_LUAVALUE, ARG_TYPES_LUAVALUE, (short)182));
   }

   public void setTable() {
      this.append(this.factory.createInvoke(STR_LUAVALUE, "set", Type.VOID, ARG_TYPES_LUAVALUE_LUAVALUE, (short)182));
   }

   public void unaryop(int var1) {
      String var2;
      switch (var1) {
         case 19:
         default:
            var2 = "neg";
            break;
         case 20:
            var2 = "not";
            break;
         case 21:
            var2 = "len";
      }

      this.append(this.factory.createInvoke(STR_LUAVALUE, var2, TYPE_LUAVALUE, Type.NO_ARGS, (short)182));
   }

   public void binaryop(int var1) {
      String var2;
      switch (var1) {
         case 13:
         default:
            var2 = "add";
            break;
         case 14:
            var2 = "sub";
            break;
         case 15:
            var2 = "mul";
            break;
         case 16:
            var2 = "div";
            break;
         case 17:
            var2 = "mod";
            break;
         case 18:
            var2 = "pow";
      }

      this.append(this.factory.createInvoke(STR_LUAVALUE, var2, TYPE_LUAVALUE, ARG_TYPES_LUAVALUE, (short)182));
   }

   public void compareop(int var1) {
      String var2;
      switch (var1) {
         case 24:
         default:
            var2 = "eq_b";
            break;
         case 25:
            var2 = "lt_b";
            break;
         case 26:
            var2 = "lteq_b";
      }

      this.append(this.factory.createInvoke(STR_LUAVALUE, var2, Type.BOOLEAN, ARG_TYPES_LUAVALUE, (short)182));
   }

   public void areturn() {
      this.append(InstructionConstants.ARETURN);
   }

   public void toBoolean() {
      this.append(this.factory.createInvoke(STR_LUAVALUE, "toboolean", Type.BOOLEAN, Type.NO_ARGS, (short)182));
   }

   public void tostring() {
      this.append(this.factory.createInvoke(STR_BUFFER, "tostring", TYPE_LUASTRING, Type.NO_ARGS, (short)182));
   }

   public void isNil() {
      this.append(this.factory.createInvoke(STR_LUAVALUE, "isnil", Type.BOOLEAN, Type.NO_ARGS, (short)182));
   }

   public void testForLoop() {
      this.append(this.factory.createInvoke(STR_LUAVALUE, "testfor_b", Type.BOOLEAN, ARG_TYPES_LUAVALUE_LUAVALUE, (short)182));
   }

   public void loadArrayArgs(int var1, int var2, int var3) {
      this.append(new PUSH(this.cp, var3));
      this.append(new ANEWARRAY(this.cp.addClass(STR_LUAVALUE)));

      for (int var4 = 0; var4 < var3; var4++) {
         this.append(InstructionConstants.DUP);
         this.append(new PUSH(this.cp, var4));
         this.loadLocal(var1, var2++);
         this.append(new AASTORE());
      }
   }

   public void newVarargs(int var1, int var2, int var3) {
      switch (var3) {
         case 0:
            this.loadNone();
            break;
         case 1:
            this.loadLocal(var1, var2);
            break;
         case 2:
            this.loadLocal(var1, var2);
            this.loadLocal(var1, var2 + 1);
            this.append(this.factory.createInvoke(STR_LUAVALUE, "varargsOf", TYPE_VARARGS, ARG_TYPES_LUAVALUE_VARARGS, (short)184));
            break;
         case 3:
            this.loadLocal(var1, var2);
            this.loadLocal(var1, var2 + 1);
            this.loadLocal(var1, var2 + 2);
            this.append(this.factory.createInvoke(STR_LUAVALUE, "varargsOf", TYPE_VARARGS, ARG_TYPES_LUAVALUE_LUAVALUE_VARARGS, (short)184));
            break;
         default:
            this.loadArrayArgs(var1, var2, var3);
            this.append(this.factory.createInvoke(STR_LUAVALUE, "varargsOf", TYPE_VARARGS, ARG_TYPES_LUAVALUEARRAY, (short)184));
      }
   }

   public void newVarargsVarresult(int var1, int var2, int var3) {
      this.loadArrayArgs(var1, var2, var3);
      this.loadVarresult();
      this.append(this.factory.createInvoke(STR_LUAVALUE, "varargsOf", TYPE_VARARGS, ARG_TYPES_LUAVALUEARRAY_VARARGS, (short)184));
   }

   public void call(int var1) {
      switch (var1) {
         case 0:
            this.append(this.factory.createInvoke(STR_LUAVALUE, "call", TYPE_LUAVALUE, ARG_TYPES_NONE, (short)182));
            break;
         case 1:
            this.append(this.factory.createInvoke(STR_LUAVALUE, "call", TYPE_LUAVALUE, ARG_TYPES_LUAVALUE, (short)182));
            break;
         case 2:
            this.append(this.factory.createInvoke(STR_LUAVALUE, "call", TYPE_LUAVALUE, ARG_TYPES_LUAVALUE_LUAVALUE, (short)182));
            break;
         case 3:
            this.append(this.factory.createInvoke(STR_LUAVALUE, "call", TYPE_LUAVALUE, ARG_TYPES_LUAVALUE_LUAVALUE_LUAVALUE, (short)182));
            break;
         default:
            throw new IllegalArgumentException("can't call with " + var1 + " args");
      }
   }

   public void newTailcallVarargs() {
      this.append(this.factory.createInvoke(STR_LUAVALUE, "tailcallOf", TYPE_VARARGS, ARG_TYPES_LUAVALUE_VARARGS, (short)184));
   }

   public void invoke(int var1) {
      switch (var1) {
         case -1:
            this.append(this.factory.createInvoke(STR_LUAVALUE, "invoke", TYPE_VARARGS, ARG_TYPES_VARARGS, (short)182));
            break;
         case 0:
            this.append(this.factory.createInvoke(STR_LUAVALUE, "invoke", TYPE_VARARGS, ARG_TYPES_NONE, (short)182));
            break;
         case 1:
            this.append(this.factory.createInvoke(STR_LUAVALUE, "invoke", TYPE_VARARGS, ARG_TYPES_VARARGS, (short)182));
            break;
         case 2:
            this.append(this.factory.createInvoke(STR_LUAVALUE, "invoke", TYPE_VARARGS, ARG_TYPES_LUAVALUE_VARARGS, (short)182));
            break;
         case 3:
            this.append(this.factory.createInvoke(STR_LUAVALUE, "invoke", TYPE_VARARGS, ARG_TYPES_LUAVALUE_LUAVALUE_VARARGS, (short)182));
            break;
         default:
            throw new IllegalArgumentException("can't invoke with " + var1 + " args");
      }
   }

   public void closureCreate(String var1) {
      this.append(this.factory.createNew(new ObjectType(var1)));
      this.append(InstructionConstants.DUP);
      this.append(this.factory.createInvoke(var1, "<init>", Type.VOID, Type.NO_ARGS, (short)183));
   }

   public void closureInitUpvalueFromUpvalue(String var1, int var2, int var3) {
      boolean var4 = this.pi.isReadWriteUpvalue(this.pi.upvals[var3]);
      Object var5 = var4 ? TYPE_LOCALUPVALUE : TYPE_LUAVALUE;
      String var6 = upvalueName(var3);
      String var7 = upvalueName(var2);
      this.append(InstructionConstants.THIS);
      this.append(this.factory.createFieldAccess(this.classname, var6, (Type)var5, (short)180));
      this.append(this.factory.createFieldAccess(var1, var7, (Type)var5, (short)181));
   }

   public void closureInitUpvalueFromLocal(String var1, int var2, int var3, int var4) {
      boolean var5 = this.pi.isReadWriteUpvalue(this.pi.vars[var4][var3].upvalue);
      Object var6 = var5 ? TYPE_LOCALUPVALUE : TYPE_LUAVALUE;
      String var7 = upvalueName(var2);
      int var8 = this.findSlotIndex(var4, var5);
      this.append(new ALOAD(var8));
      this.append(this.factory.createFieldAccess(var1, var7, (Type)var6, (short)181));
   }

   public void loadConstant(LuaValue var1) {
      switch (var1.type()) {
         case 0:
            this.loadNil();
            break;
         case 1:
            this.loadBoolean(var1.toboolean());
            break;
         case 2:
         default:
            throw new IllegalArgumentException("bad constant type: " + var1.type());
         case 3:
         case 4:
            String var2 = (String)this.constants.get(var1);
            if (var2 == null) {
               var2 = var1.type() == 3
                  ? (var1.isinttype() ? this.createLuaIntegerField(var1.checkint()) : this.createLuaDoubleField(var1.checkdouble()))
                  : this.createLuaStringField(var1.checkstring());
               this.constants.put(var1, var2);
            }

            this.append(this.factory.createGetStatic(this.classname, var2, TYPE_LUAVALUE));
      }
   }

   private String createLuaIntegerField(int var1) {
      String var2 = "k" + this.constants.size();
      FieldGen var3 = new FieldGen(24, TYPE_LUAVALUE, var2, this.cp);
      this.cg.addField(var3.getField());
      this.init.append(new PUSH(this.cp, var1));
      this.init.append(this.factory.createInvoke(STR_LUAVALUE, "valueOf", TYPE_LUAINTEGER, ARG_TYPES_INT, (short)184));
      this.init.append(this.factory.createPutStatic(this.classname, var2, TYPE_LUAVALUE));
      return var2;
   }

   private String createLuaDoubleField(double var1) {
      String var3 = "k" + this.constants.size();
      FieldGen var4 = new FieldGen(24, TYPE_LUAVALUE, var3, this.cp);
      this.cg.addField(var4.getField());
      this.init.append(new PUSH(this.cp, var1));
      this.init.append(this.factory.createInvoke(STR_LUAVALUE, "valueOf", TYPE_LUANUMBER, ARG_TYPES_DOUBLE, (short)184));
      this.init.append(this.factory.createPutStatic(this.classname, var3, TYPE_LUAVALUE));
      return var3;
   }

   private String createLuaStringField(LuaString var1) {
      String var2 = "k" + this.constants.size();
      FieldGen var3 = new FieldGen(24, TYPE_LUAVALUE, var2, this.cp);
      this.cg.addField(var3.getField());
      LuaString var4 = var1.checkstring();
      if (var4.isValidUtf8()) {
         this.init.append(new PUSH(this.cp, var1.tojstring()));
         this.init.append(this.factory.createInvoke(STR_LUASTRING, "valueOf", TYPE_LUASTRING, ARG_TYPES_STRING, (short)184));
      } else {
         char[] var5 = new char[var4.m_length];

         for (int var6 = 0; var6 < var4.m_length; var6++) {
            var5[var6] = (char)(255 & var4.m_bytes[var4.m_offset + var6]);
         }

         this.init.append(new PUSH(this.cp, new String(var5)));
         this.init.append(this.factory.createInvoke(STR_STRING, "toCharArray", TYPE_CHARARRAY, Type.NO_ARGS, (short)182));
         this.init.append(this.factory.createInvoke(STR_LUASTRING, "valueOf", TYPE_LUASTRING, ARG_TYPES_CHARARRAY, (short)184));
      }

      this.init.append(this.factory.createPutStatic(this.classname, var2, TYPE_LUAVALUE));
      return var2;
   }

   public void addBranch(int var1, int var2, int var3) {
      switch (var2) {
         case 1:
         default:
            this.branches[var1] = new GOTO(null);
            break;
         case 2:
            this.branches[var1] = new IFNE(null);
            break;
         case 3:
            this.branches[var1] = new IFEQ(null);
      }

      this.targets[var1] = var3;
      this.append(this.branches[var1]);
   }

   private void append(Instruction var1) {
      this.conditionalSetBeginningOfLua(this.main.append(var1));
   }

   private void append(CompoundInstruction var1) {
      this.conditionalSetBeginningOfLua(this.main.append(var1));
   }

   private void append(BranchInstruction var1) {
      this.conditionalSetBeginningOfLua(this.main.append(var1));
   }

   private void conditionalSetBeginningOfLua(InstructionHandle var1) {
      if (this.beginningOfLuaInstruction == null) {
         this.beginningOfLuaInstruction = var1;
      }
   }

   public void onEndOfLuaInstruction(int var1, int var2) {
      this.branchDestHandles[var1] = this.beginningOfLuaInstruction;
      this.lastInstrHandles[var1] = this.main.getEnd();
      if (var2 != this.prev_line) {
         this.mg.addLineNumber(this.beginningOfLuaInstruction, this.prev_line = var2);
      }

      this.beginningOfLuaInstruction = null;
   }

   public void setVarStartEnd(int var1, int var2, int var3, String var4) {
      Integer var5 = var1;
      if (this.localVarGenBySlot.containsKey(var5)) {
         var4 = var4.replaceAll("[^a-zA-Z0-9]", "_");
         LocalVariableGen var6 = (LocalVariableGen)this.localVarGenBySlot.get(var5);
         var6.setEnd(this.lastInstrHandles[var3 - 1]);
         if (var2 > 1) {
            var6.setStart(this.lastInstrHandles[var2 - 2]);
         }

         var6.setName(var4);
      }
   }

   private void resolveBranches() {
      int var1 = this.p.code.length;

      for (int var2 = 0; var2 < var1; var2++) {
         if (this.branches[var2] != null) {
            int var3 = this.targets[var2];

            while (var3 < this.branchDestHandles.length && this.branchDestHandles[var3] == null) {
               var3++;
            }

            if (var3 >= this.branchDestHandles.length) {
               throw new IllegalArgumentException("no target at or after " + this.targets[var2] + " op=" + Lua.GET_OPCODE(this.p.code[this.targets[var2]]));
            }

            this.branches[var2].setTarget(this.branchDestHandles[var3]);
         }
      }
   }

   public void setlistStack(int var1, int var2, int var3, int var4) {
      for (int var5 = 0; var5 < var4; var5++) {
         this.dup();
         this.append(new PUSH(this.cp, var3 + var5));
         this.loadLocal(var1, var2 + var5);
         this.append(this.factory.createInvoke(STR_LUAVALUE, "rawset", Type.VOID, ARG_TYPES_INT_LUAVALUE, (short)182));
      }
   }

   public void setlistVarargs(int var1, int var2) {
      this.append(new PUSH(this.cp, var1));
      this.loadVarresult();
      this.append(this.factory.createInvoke(STR_LUAVALUE, "rawsetlist", Type.VOID, ARG_TYPES_INT_VARARGS, (short)182));
   }

   public void concatvalue() {
      this.append(this.factory.createInvoke(STR_LUAVALUE, "concat", TYPE_LUAVALUE, ARG_TYPES_LUAVALUE, (short)182));
   }

   public void concatbuffer() {
      this.append(this.factory.createInvoke(STR_LUAVALUE, "concat", TYPE_BUFFER, ARG_TYPES_BUFFER, (short)182));
   }

   public void tobuffer() {
      this.append(this.factory.createInvoke(STR_LUAVALUE, "buffer", TYPE_BUFFER, Type.NO_ARGS, (short)182));
   }

   public void tovalue() {
      this.append(this.factory.createInvoke(STR_BUFFER, "value", TYPE_LUAVALUE, Type.NO_ARGS, (short)182));
   }

   public void closeUpvalue(int var1, int var2) {
   }
}
