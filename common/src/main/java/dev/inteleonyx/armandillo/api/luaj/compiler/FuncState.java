package dev.inteleonyx.armandillo.api.luaj.compiler;

import dev.inteleonyx.armandillo.api.luaj.*;

import java.util.Hashtable;

public class FuncState extends Constants {
   Prototype f;
   Hashtable h;
   FuncState prev;
   LexState ls;
   LuaC.CompileState L;
   BlockCnt bl;
   int pc;
   int lasttarget;
   IntPtr jpc;
   int nk;
   int np;
   int firstlocal;
   short nlocvars;
   short nactvar;
   short nups;
   short freereg;

   FuncState() {
   }

   InstructionPtr getcodePtr(LexState.expdesc var1) {
      return new InstructionPtr(this.f.code, var1.u.info);
   }

   int getcode(LexState.expdesc var1) {
      return this.f.code[var1.u.info];
   }

   int codeAsBx(int var1, int var2, int var3) {
      return this.codeABx(var1, var2, var3 + 131071);
   }

   void setmultret(LexState.expdesc var1) {
      this.setreturns(var1, -1);
   }

   void checkrepeated(LexState.Labeldesc[] var1, int var2, LuaString var3) {
      for (int var4 = this.bl.firstlabel; var4 < var2; var4++) {
         if (var3.eq_b(var1[var4].name)) {
            String var5 = this.ls.L.pushfstring("label '" + var3 + " already defined on line " + var1[var4].line);
            this.ls.semerror(var5);
         }
      }
   }

   void checklimit(int var1, int var2, String var3) {
      if (var1 > var2) {
         this.errorlimit(var2, var3);
      }
   }

   void errorlimit(int var1, String var2) {
      String var3 = this.f.linedefined == 0
         ? this.L.pushfstring("main function has more than " + var1 + " " + var2)
         : this.L.pushfstring("function at line " + this.f.linedefined + " has more than " + var1 + " " + var2);
      this.ls.lexerror(var3, 0);
   }

   LocVars getlocvar(int var1) {
      short var2 = this.ls.dyd.actvar[this.firstlocal + var1].idx;
      _assert(var2 < this.nlocvars);
      return this.f.locvars[var2];
   }

   void removevars(int var1) {
      this.ls.dyd.n_actvar = this.ls.dyd.n_actvar - (this.nactvar - var1);

      while (this.nactvar > var1) {
         this.getlocvar(--this.nactvar).endpc = this.pc;
      }
   }

   int searchupvalue(LuaString var1) {
      Upvaldesc[] var3 = this.f.upvalues;

      for (int var2 = 0; var2 < this.nups; var2++) {
         if (var3[var2].name.eq_b(var1)) {
            return var2;
         }
      }

      return -1;
   }

   int newupvalue(LuaString var1, LexState.expdesc var2) {
      this.checklimit(this.nups + 1, 255, "upvalues");
      if (this.f.upvalues == null || this.nups + 1 > this.f.upvalues.length) {
         this.f.upvalues = realloc(this.f.upvalues, this.nups > 0 ? this.nups * 2 : 1);
      }

      this.f.upvalues[this.nups] = new Upvaldesc(var1, var2.k == 7, var2.u.info);
      return this.nups++;
   }

   int searchvar(LuaString var1) {
      for (int var2 = this.nactvar - 1; var2 >= 0; var2--) {
         if (var1.eq_b(this.getlocvar(var2).varname)) {
            return var2;
         }
      }

      return -1;
   }

   void markupval(int var1) {
      BlockCnt var2 = this.bl;

      while (var2.nactvar > var1) {
         var2 = var2.previous;
      }

      var2.upval = true;
   }

   static int singlevaraux(FuncState var0, LuaString var1, LexState.expdesc var2, int var3) {
      if (var0 == null) {
         return 0;
      } else {
         int var4 = var0.searchvar(var1);
         if (var4 >= 0) {
            var2.init(7, var4);
            if (var3 == 0) {
               var0.markupval(var4);
            }

            return 7;
         } else {
            int var5 = var0.searchupvalue(var1);
            if (var5 < 0) {
               if (singlevaraux(var0.prev, var1, var2, 0) == 0) {
                  return 0;
               }

               var5 = var0.newupvalue(var1, var2);
            }

            var2.init(8, var5);
            return 8;
         }
      }
   }

   void movegotosout(BlockCnt var1) {
      int var2 = var1.firstgoto;
      LexState.Labeldesc[] var3 = this.ls.dyd.gt;

      while (var2 < this.ls.dyd.n_gt) {
         LexState.Labeldesc var4 = var3[var2];
         if (var4.nactvar > var1.nactvar) {
            if (var1.upval) {
               this.patchclose(var4.pc, var1.nactvar);
            }

            var4.nactvar = var1.nactvar;
         }

         if (!this.ls.findlabel(var2)) {
            var2++;
         }
      }
   }

   void enterblock(BlockCnt var1, boolean var2) {
      var1.isloop = var2;
      var1.nactvar = this.nactvar;
      var1.firstlabel = (short)this.ls.dyd.n_label;
      var1.firstgoto = (short)this.ls.dyd.n_gt;
      var1.upval = false;
      var1.previous = this.bl;
      this.bl = var1;
      _assert(this.freereg == this.nactvar);
   }

   void leaveblock() {
      BlockCnt var1 = this.bl;
      if (var1.previous != null && var1.upval) {
         int var2 = this.jump();
         this.patchclose(var2, var1.nactvar);
         this.patchtohere(var2);
      }

      if (var1.isloop) {
         this.ls.breaklabel();
      }

      this.bl = var1.previous;
      this.removevars(var1.nactvar);
      _assert(var1.nactvar == this.nactvar);
      this.freereg = this.nactvar;
      this.ls.dyd.n_label = var1.firstlabel;
      if (var1.previous != null) {
         this.movegotosout(var1);
      } else if (var1.firstgoto < this.ls.dyd.n_gt) {
         this.ls.undefgoto(this.ls.dyd.gt[var1.firstgoto]);
      }
   }

   void closelistfield(LexState.ConsControl var1) {
      if (var1.v.k != 0) {
         this.exp2nextreg(var1.v);
         var1.v.k = 0;
         if (var1.tostore == 50) {
            this.setlist(var1.t.u.info, var1.na, var1.tostore);
            var1.tostore = 0;
         }
      }
   }

   boolean hasmultret(int var1) {
      return var1 == 12 || var1 == 13;
   }

   void lastlistfield(LexState.ConsControl var1) {
      if (var1.tostore != 0) {
         if (this.hasmultret(var1.v.k)) {
            this.setmultret(var1.v);
            this.setlist(var1.t.u.info, var1.na, -1);
            var1.na--;
         } else {
            if (var1.v.k != 0) {
               this.exp2nextreg(var1.v);
            }

            this.setlist(var1.t.u.info, var1.na, var1.tostore);
         }
      }
   }

   void nil(int var1, int var2) {
      int var3 = var1 + var2 - 1;
      if (this.pc > this.lasttarget && this.pc > 0) {
         int var4 = this.f.code[this.pc - 1];
         if (GET_OPCODE(var4) == 4) {
            int var5 = GETARG_A(var4);
            int var6 = var5 + GETARG_B(var4);
            if (var5 <= var1 && var1 <= var6 + 1 || var1 <= var5 && var5 <= var3 + 1) {
               if (var5 < var1) {
                  var1 = var5;
               }

               if (var6 > var3) {
                  var3 = var6;
               }

               InstructionPtr var7 = new InstructionPtr(this.f.code, this.pc - 1);
               SETARG_A(var7, var1);
               SETARG_B(var7, var3 - var1);
               return;
            }
         }
      }

      this.codeABC(4, var1, var2 - 1, 0);
   }

   int jump() {
      int var1 = this.jpc.i;
      this.jpc.i = -1;
      IntPtr var2 = new IntPtr(this.codeAsBx(23, 0, -1));
      this.concat(var2, var1);
      return var2.i;
   }

   void ret(int var1, int var2) {
      this.codeABC(31, var1, var2 + 1, 0);
   }

   int condjump(int var1, int var2, int var3, int var4) {
      this.codeABC(var1, var2, var3, var4);
      return this.jump();
   }

   void fixjump(int var1, int var2) {
      InstructionPtr var3 = new InstructionPtr(this.f.code, var1);
      int var4 = var2 - (var1 + 1);
      _assert(var2 != -1);
      if (Math.abs(var4) > 131071) {
         this.ls.syntaxerror("control structure too long");
      }

      SETARG_sBx(var3, var4);
   }

   int getlabel() {
      this.lasttarget = this.pc;
      return this.pc;
   }

   int getjump(int var1) {
      int var2 = GETARG_sBx(this.f.code[var1]);
      return var2 == -1 ? -1 : var1 + 1 + var2;
   }

   InstructionPtr getjumpcontrol(int var1) {
      InstructionPtr var2 = new InstructionPtr(this.f.code, var1);
      return var1 >= 1 && testTMode(GET_OPCODE(var2.code[var2.idx - 1])) ? new InstructionPtr(var2.code, var2.idx - 1) : var2;
   }

   boolean need_value(int var1) {
      while (var1 != -1) {
         int var2 = this.getjumpcontrol(var1).get();
         if (GET_OPCODE(var2) != 28) {
            return true;
         }

         var1 = this.getjump(var1);
      }

      return false;
   }

   boolean patchtestreg(int var1, int var2) {
      InstructionPtr var3 = this.getjumpcontrol(var1);
      if (GET_OPCODE(var3.get()) != 28) {
         return false;
      } else {
         if (var2 != 255 && var2 != GETARG_B(var3.get())) {
            SETARG_A(var3, var2);
         } else {
            var3.set(CREATE_ABC(27, GETARG_B(var3.get()), 0, Lua.GETARG_C(var3.get())));
         }

         return true;
      }
   }

   void removevalues(int var1) {
      while (var1 != -1) {
         this.patchtestreg(var1, 255);
         var1 = this.getjump(var1);
      }
   }

   void patchlistaux(int var1, int var2, int var3, int var4) {
      while (var1 != -1) {
         int var5 = this.getjump(var1);
         if (this.patchtestreg(var1, var3)) {
            this.fixjump(var1, var2);
         } else {
            this.fixjump(var1, var4);
         }

         var1 = var5;
      }
   }

   void dischargejpc() {
      this.patchlistaux(this.jpc.i, this.pc, 255, this.pc);
      this.jpc.i = -1;
   }

   void patchlist(int var1, int var2) {
      if (var2 == this.pc) {
         this.patchtohere(var1);
      } else {
         _assert(var2 < this.pc);
         this.patchlistaux(var1, var2, 255, var2);
      }
   }

   void patchclose(int var1, int var2) {
      var2++;

      while (var1 != -1) {
         int var3 = this.getjump(var1);
         _assert(GET_OPCODE(this.f.code[var1]) == 23 && (GETARG_A(this.f.code[var1]) == 0 || GETARG_A(this.f.code[var1]) >= var2));
         SETARG_A(this.f.code, var1, var2);
         var1 = var3;
      }
   }

   void patchtohere(int var1) {
      this.getlabel();
      this.concat(this.jpc, var1);
   }

   void concat(IntPtr var1, int var2) {
      if (var2 != -1) {
         if (var1.i == -1) {
            var1.i = var2;
         } else {
            int var3 = var1.i;

            int var4;
            while ((var4 = this.getjump(var3)) != -1) {
               var3 = var4;
            }

            this.fixjump(var3, var2);
         }
      }
   }

   void checkstack(int var1) {
      int var2 = this.freereg + var1;
      if (var2 > this.f.maxstacksize) {
         if (var2 >= 250) {
            this.ls.syntaxerror("function or expression too complex");
         }

         this.f.maxstacksize = var2;
      }
   }

   void reserveregs(int var1) {
      this.checkstack(var1);
      this.freereg = (short)(this.freereg + var1);
   }

   void freereg(int var1) {
      if (!ISK(var1) && var1 >= this.nactvar) {
         this.freereg--;
         _assert(var1 == this.freereg);
      }
   }

   void freeexp(LexState.expdesc var1) {
      if (var1.k == 6) {
         this.freereg(var1.u.info);
      }
   }

   int addk(LuaValue var1) {
      if (this.h == null) {
         this.h = new Hashtable();
      } else if (this.h.containsKey(var1)) {
         return (Integer)this.h.get(var1);
      }

      int var2 = this.nk;
      this.h.put(var1, Integer.valueOf(var2));
      Prototype var3 = this.f;
      if (var3.k == null || this.nk + 1 >= var3.k.length) {
         var3.k = realloc(var3.k, this.nk * 2 + 1);
      }

      var3.k[this.nk++] = var1;
      return var2;
   }

   int stringK(LuaString var1) {
      return this.addk(var1);
   }

   int numberK(LuaValue var1) {
      if (var1 instanceof LuaDouble) {
         double var2 = ((LuaValue)var1).todouble();
         int var4 = (int)var2;
         if (var2 == var4) {
            var1 = LuaInteger.valueOf(var4);
         }
      }

      return this.addk((LuaValue)var1);
   }

   int boolK(boolean var1) {
      return this.addk(var1 ? LuaValue.TRUE : LuaValue.FALSE);
   }

   int nilK() {
      return this.addk(LuaValue.NIL);
   }

   void setreturns(LexState.expdesc var1, int var2) {
      if (var1.k == 12) {
         SETARG_C(this.getcodePtr(var1), var2 + 1);
      } else if (var1.k == 13) {
         SETARG_B(this.getcodePtr(var1), var2 + 1);
         SETARG_A(this.getcodePtr(var1), this.freereg);
         this.reserveregs(1);
      }
   }

   void setoneret(LexState.expdesc var1) {
      if (var1.k == 12) {
         var1.k = 6;
         var1.u.info = GETARG_A(this.getcode(var1));
      } else if (var1.k == 13) {
         SETARG_B(this.getcodePtr(var1), 2);
         var1.k = 11;
      }
   }

   void dischargevars(LexState.expdesc var1) {
      switch (var1.k) {
         case 7:
            var1.k = 6;
            break;
         case 8:
            var1.u.info = this.codeABC(5, 0, var1.u.info, 0);
            var1.k = 11;
            break;
         case 9:
            byte var2 = 6;
            this.freereg(var1.u.ind_idx);
            if (var1.u.ind_vt == 7) {
               this.freereg(var1.u.ind_t);
               var2 = 7;
            }

            var1.u.info = this.codeABC(var2, 0, var1.u.ind_t, var1.u.ind_idx);
            var1.k = 11;
         case 10:
         case 11:
         default:
            break;
         case 12:
         case 13:
            this.setoneret(var1);
      }
   }

   int code_label(int var1, int var2, int var3) {
      this.getlabel();
      return this.codeABC(3, var1, var2, var3);
   }

   void discharge2reg(LexState.expdesc var1, int var2) {
      this.dischargevars(var1);
      switch (var1.k) {
         case 1:
            this.nil(var2, 1);
            break;
         case 2:
         case 3:
            this.codeABC(3, var2, var1.k == 2 ? 1 : 0, 0);
            break;
         case 4:
            this.codeABx(1, var2, var1.u.info);
            break;
         case 5:
            this.codeABx(1, var2, this.numberK(var1.u.nval()));
            break;
         case 6:
            if (var2 != var1.u.info) {
               this.codeABC(0, var2, var1.u.info, 0);
            }
            break;
         case 7:
         case 8:
         case 9:
         case 10:
         default:
            _assert(var1.k == 0 || var1.k == 10);
            return;
         case 11:
            InstructionPtr var3 = this.getcodePtr(var1);
            SETARG_A(var3, var2);
      }

      var1.u.info = var2;
      var1.k = 6;
   }

   void discharge2anyreg(LexState.expdesc var1) {
      if (var1.k != 6) {
         this.reserveregs(1);
         this.discharge2reg(var1, this.freereg - 1);
      }
   }

   void exp2reg(LexState.expdesc var1, int var2) {
      this.discharge2reg(var1, var2);
      if (var1.k == 10) {
         this.concat(var1.t, var1.u.info);
      }

      if (var1.hasjumps()) {
         int var4 = -1;
         int var5 = -1;
         if (this.need_value(var1.t.i) || this.need_value(var1.f.i)) {
            int var6 = var1.k == 10 ? -1 : this.jump();
            var4 = this.code_label(var2, 0, 1);
            var5 = this.code_label(var2, 1, 0);
            this.patchtohere(var6);
         }

         int var3 = this.getlabel();
         this.patchlistaux(var1.f.i, var3, var2, var4);
         this.patchlistaux(var1.t.i, var3, var2, var5);
      }

      var1.f.i = var1.t.i = -1;
      var1.u.info = var2;
      var1.k = 6;
   }

   void exp2nextreg(LexState.expdesc var1) {
      this.dischargevars(var1);
      this.freeexp(var1);
      this.reserveregs(1);
      this.exp2reg(var1, this.freereg - 1);
   }

   int exp2anyreg(LexState.expdesc var1) {
      this.dischargevars(var1);
      if (var1.k == 6) {
         if (!var1.hasjumps()) {
            return var1.u.info;
         }

         if (var1.u.info >= this.nactvar) {
            this.exp2reg(var1, var1.u.info);
            return var1.u.info;
         }
      }

      this.exp2nextreg(var1);
      return var1.u.info;
   }

   void exp2anyregup(LexState.expdesc var1) {
      if (var1.k != 8 || var1.hasjumps()) {
         this.exp2anyreg(var1);
      }
   }

   void exp2val(LexState.expdesc var1) {
      if (var1.hasjumps()) {
         this.exp2anyreg(var1);
      } else {
         this.dischargevars(var1);
      }
   }

   int exp2RK(LexState.expdesc var1) {
      this.exp2val(var1);
      switch (var1.k) {
         case 1:
         case 2:
         case 3:
            if (this.nk <= 255) {
               var1.u.info = var1.k == 1 ? this.nilK() : this.boolK(var1.k == 2);
               var1.k = 4;
               return RKASK(var1.u.info);
            }
            break;
         case 5:
            var1.u.info = this.numberK(var1.u.nval());
            var1.k = 4;
         case 4:
            if (var1.u.info <= 255) {
               return RKASK(var1.u.info);
            }
      }

      return this.exp2anyreg(var1);
   }

   void storevar(LexState.expdesc var1, LexState.expdesc var2) {
      switch (var1.k) {
         case 7:
            this.freeexp(var2);
            this.exp2reg(var2, var1.u.info);
            return;
         case 8:
            int var5 = this.exp2anyreg(var2);
            this.codeABC(9, var5, var1.u.info, 0);
            break;
         case 9:
            int var3 = var1.u.ind_vt == 7 ? 10 : 8;
            int var4 = this.exp2RK(var2);
            this.codeABC(var3, var1.u.ind_t, var1.u.ind_idx, var4);
            break;
         default:
            _assert(false);
      }

      this.freeexp(var2);
   }

   void self(LexState.expdesc var1, LexState.expdesc var2) {
      this.exp2anyreg(var1);
      this.freeexp(var1);
      short var3 = this.freereg;
      this.reserveregs(2);
      this.codeABC(12, var3, var1.u.info, this.exp2RK(var2));
      this.freeexp(var2);
      var1.u.info = var3;
      var1.k = 6;
   }

   void invertjump(LexState.expdesc var1) {
      InstructionPtr var2 = this.getjumpcontrol(var1.u.info);
      _assert(testTMode(GET_OPCODE(var2.get())) && GET_OPCODE(var2.get()) != 28 && Lua.GET_OPCODE(var2.get()) != 27);
      int var3 = GETARG_A(var2.get());
      int var4 = var3 != 0 ? 0 : 1;
      SETARG_A(var2, var4);
   }

   int jumponcond(LexState.expdesc var1, int var2) {
      if (var1.k == 11) {
         int var3 = this.getcode(var1);
         if (GET_OPCODE(var3) == 20) {
            this.pc--;
            return this.condjump(27, GETARG_B(var3), 0, var2 != 0 ? 0 : 1);
         }
      }

      this.discharge2anyreg(var1);
      this.freeexp(var1);
      return this.condjump(28, 255, var1.u.info, var2);
   }

   void goiftrue(LexState.expdesc var1) {
      this.dischargevars(var1);
      int var2;
      switch (var1.k) {
         case 2:
         case 4:
         case 5:
            var2 = -1;
            break;
         case 3:
         case 6:
         case 7:
         case 8:
         case 9:
         default:
            var2 = this.jumponcond(var1, 0);
            break;
         case 10:
            this.invertjump(var1);
            var2 = var1.u.info;
      }

      this.concat(var1.f, var2);
      this.patchtohere(var1.t.i);
      var1.t.i = -1;
   }

   void goiffalse(LexState.expdesc var1) {
      this.dischargevars(var1);
      int var2;
      switch (var1.k) {
         case 1:
         case 3:
            var2 = -1;
            break;
         case 10:
            var2 = var1.u.info;
            break;
         default:
            var2 = this.jumponcond(var1, 1);
      }

      this.concat(var1.t, var2);
      this.patchtohere(var1.f.i);
      var1.f.i = -1;
   }

   void codenot(LexState.expdesc var1) {
      this.dischargevars(var1);
      switch (var1.k) {
         case 1:
         case 3:
            var1.k = 2;
            break;
         case 2:
         case 4:
         case 5:
            var1.k = 3;
            break;
         case 6:
         case 11:
            this.discharge2anyreg(var1);
            this.freeexp(var1);
            var1.u.info = this.codeABC(20, 0, var1.u.info, 0);
            var1.k = 11;
            break;
         case 7:
         case 8:
         case 9:
         default:
            _assert(false);
            break;
         case 10:
            this.invertjump(var1);
      }

      int var2 = var1.f.i;
      var1.f.i = var1.t.i;
      var1.t.i = var2;
      this.removevalues(var1.f.i);
      this.removevalues(var1.t.i);
   }

   static boolean vkisinreg(int var0) {
      return var0 == 6 || var0 == 7;
   }

   void indexed(LexState.expdesc var1, LexState.expdesc var2) {
      var1.u.ind_t = (short)var1.u.info;
      var1.u.ind_idx = (short)this.exp2RK(var2);
      LuaC._assert(var1.k == 8 || vkisinreg(var1.k));
      var1.u.ind_vt = (short)(var1.k == 8 ? 8 : 7);
      var1.k = 9;
   }

   boolean constfolding(int var1, LexState.expdesc var2, LexState.expdesc var3) {
      if (var2.isnumeral() && var3.isnumeral()) {
         if ((var1 == 16 || var1 == 17) && var3.u.nval().eq_b(LuaValue.ZERO)) {
            return false;
         } else {
            LuaValue var4 = var2.u.nval();
            LuaValue var5 = var3.u.nval();
            LuaValue var6;
            switch (var1) {
               case 13:
                  var6 = var4.add(var5);
                  break;
               case 14:
                  var6 = var4.sub(var5);
                  break;
               case 15:
                  var6 = var4.mul(var5);
                  break;
               case 16:
                  var6 = var4.div(var5);
                  break;
               case 17:
                  var6 = var4.mod(var5);
                  break;
               case 18:
                  var6 = var4.pow(var5);
                  break;
               case 19:
                  var6 = var4.neg();
                  break;
               case 20:
               default:
                  _assert(false);
                  var6 = null;
                  break;
               case 21:
                  return false;
            }

            if (Double.isNaN(var6.todouble())) {
               return false;
            } else {
               var2.u.setNval(var6);
               return true;
            }
         }
      } else {
         return false;
      }
   }

   void codearith(int var1, LexState.expdesc var2, LexState.expdesc var3, int var4) {
      if (!this.constfolding(var1, var2, var3)) {
         int var5 = var1 != 19 && var1 != 21 ? this.exp2RK(var3) : 0;
         int var6 = this.exp2RK(var2);
         if (var6 > var5) {
            this.freeexp(var2);
            this.freeexp(var3);
         } else {
            this.freeexp(var3);
            this.freeexp(var2);
         }

         var2.u.info = this.codeABC(var1, 0, var6, var5);
         var2.k = 11;
         this.fixline(var4);
      }
   }

   void codecomp(int var1, int var2, LexState.expdesc var3, LexState.expdesc var4) {
      int var5 = this.exp2RK(var3);
      int var6 = this.exp2RK(var4);
      this.freeexp(var4);
      this.freeexp(var3);
      if (var2 == 0 && var1 != 24) {
         int var7 = var5;
         var5 = var6;
         var6 = var7;
         var2 = 1;
      }

      var3.u.info = this.condjump(var1, var2, var5, var6);
      var3.k = 10;
   }

   void prefix(int var1, LexState.expdesc var2, int var3) {
      LexState.expdesc var4 = new LexState.expdesc();
      var4.init(5, 0);
      switch (var1) {
         case 0:
            if (var2.isnumeral()) {
               var2.u.setNval(var2.u.nval().neg());
            } else {
               this.exp2anyreg(var2);
               this.codearith(19, var2, var4, var3);
            }
            break;
         case 1:
            this.codenot(var2);
            break;
         case 2:
            this.exp2anyreg(var2);
            this.codearith(21, var2, var4, var3);
            break;
         default:
            _assert(false);
      }
   }

   void infix(int var1, LexState.expdesc var2) {
      switch (var1) {
         case 0:
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
            if (!var2.isnumeral()) {
               this.exp2RK(var2);
            }
            break;
         case 6:
            this.exp2nextreg(var2);
            break;
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         default:
            this.exp2RK(var2);
            break;
         case 13:
            this.goiftrue(var2);
            break;
         case 14:
            this.goiffalse(var2);
      }
   }

   void posfix(int var1, LexState.expdesc var2, LexState.expdesc var3, int var4) {
      switch (var1) {
         case 0:
            this.codearith(13, var2, var3, var4);
            break;
         case 1:
            this.codearith(14, var2, var3, var4);
            break;
         case 2:
            this.codearith(15, var2, var3, var4);
            break;
         case 3:
            this.codearith(16, var2, var3, var4);
            break;
         case 4:
            this.codearith(17, var2, var3, var4);
            break;
         case 5:
            this.codearith(18, var2, var3, var4);
            break;
         case 6:
            this.exp2val(var3);
            if (var3.k == 11 && GET_OPCODE(this.getcode(var3)) == 22) {
               _assert(var2.u.info == GETARG_B(this.getcode(var3)) - 1);
               this.freeexp(var2);
               SETARG_B(this.getcodePtr(var3), var2.u.info);
               var2.k = 11;
               var2.u.info = var3.u.info;
            } else {
               this.exp2nextreg(var3);
               this.codearith(22, var2, var3, var4);
            }
            break;
         case 7:
            this.codecomp(24, 0, var2, var3);
            break;
         case 8:
            this.codecomp(24, 1, var2, var3);
            break;
         case 9:
            this.codecomp(25, 1, var2, var3);
            break;
         case 10:
            this.codecomp(26, 1, var2, var3);
            break;
         case 11:
            this.codecomp(25, 0, var2, var3);
            break;
         case 12:
            this.codecomp(26, 0, var2, var3);
            break;
         case 13:
            _assert(var2.t.i == -1);
            this.dischargevars(var3);
            this.concat(var3.f, var2.f.i);
            var2.setvalue(var3);
            break;
         case 14:
            _assert(var2.f.i == -1);
            this.dischargevars(var3);
            this.concat(var3.t, var2.t.i);
            var2.setvalue(var3);
            break;
         default:
            _assert(false);
      }
   }

   void fixline(int var1) {
      this.f.lineinfo[this.pc - 1] = var1;
   }

   int code(int var1, int var2) {
      Prototype var3 = this.f;
      this.dischargejpc();
      if (var3.code == null || this.pc + 1 > var3.code.length) {
         var3.code = LuaC.realloc(var3.code, this.pc * 2 + 1);
      }

      var3.code[this.pc] = var1;
      if (var3.lineinfo == null || this.pc + 1 > var3.lineinfo.length) {
         var3.lineinfo = LuaC.realloc(var3.lineinfo, this.pc * 2 + 1);
      }

      var3.lineinfo[this.pc] = var2;
      return this.pc++;
   }

   int codeABC(int var1, int var2, int var3, int var4) {
      _assert(getOpMode(var1) == 0);
      _assert(getBMode(var1) != 0 || var3 == 0);
      _assert(getCMode(var1) != 0 || var4 == 0);
      return this.code(CREATE_ABC(var1, var2, var3, var4), this.ls.lastline);
   }

   int codeABx(int var1, int var2, int var3) {
      _assert(getOpMode(var1) == 1 || getOpMode(var1) == 2);
      _assert(getCMode(var1) == 0);
      _assert(var3 >= 0 && var3 <= 262143);
      return this.code(CREATE_ABx(var1, var2, var3), this.ls.lastline);
   }

   void setlist(int var1, int var2, int var3) {
      int var4 = (var2 - 1) / 50 + 1;
      int var5 = var3 == -1 ? 0 : var3;
      _assert(var3 != 0);
      if (var4 <= 511) {
         this.codeABC(36, var1, var5, var4);
      } else {
         this.codeABC(36, var1, var5, 0);
         this.code(var4, this.ls.lastline);
      }

      this.freereg = (short)(var1 + 1);
   }

   static class BlockCnt {
      BlockCnt previous;
      short firstlabel;
      short firstgoto;
      short nactvar;
      boolean upval;
      boolean isloop;
   }
}
