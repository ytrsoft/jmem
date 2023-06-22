package com.ytrsoft.core;

import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public interface Libmem extends StdCallLibrary {

    Libmem INSTANCE = Native.load("libmem", Libmem.class, W32APIOptions.DEFAULT_OPTIONS);

    boolean LM_EnumProcesses(LMProcessCallback callback);

    boolean LM_GetProcess(LMProcess process);

    boolean LM_GetProcessEx(int pid, LMProcess process);

    boolean LM_FindProcess(byte[] name, LMProcess process);

    boolean LM_IsProcessAlive(LMProcess process);

    void LM_GetSystemBits(LongByReference bits);

    boolean LM_EnumThreads(LMThreadCallback callback);

    boolean LM_EnumThreadsEx(LMProcess process, LMThreadCallback callback);

    boolean LM_GetThread(LMThread thread);

    boolean LM_GetThreadEx(LMProcess process, LMThread thread);

    boolean LM_GetThreadProcess(LMThread thread, LMProcess process);

    boolean LM_EnumModules(LMModuleCallback callback);

    boolean LM_EnumModulesEx(LMProcess process, LMModuleCallback callback);

    boolean LM_FindModule(byte[] name, LMModule module);

    boolean LM_FindModuleEx(LMProcess process, byte[] name, LMModule module);

    boolean LM_LoadModule(byte[] path);

    boolean LM_LoadModuleEx(LMProcess process, byte[] path, LMModule module);

    boolean LM_UnloadModule(LMModule module);

    boolean LM_UnloadModuleEx(LMProcess process, LMModule module);

    boolean LM_EnumSymbols(LMModule module, LMSymbolCallback callback);

    boolean LM_FindSymbolAddress(LMSymbol symbol);

    boolean LM_EnumPages(LMPageCallback callback);

    boolean LM_EnumPagesEx(LMProcess process, LMPageCallback callback);

    boolean LM_GetPage(int address, LMPage page);

    boolean LM_GetPageEx(LMProcess process, int address, LMPage page);

    boolean LM_ReadMemory(int src, IntByReference dst, int size);

    boolean LM_ReadMemoryEx(LMProcess process, int dst, IntByReference ref, int size);

    boolean LM_WriteMemory(int dst, int src, int size);

    boolean LM_WriteMemoryEx(LMProcess process, int dst, int src, int size);

    boolean LM_SetMemory(int dst, int bt, int size);

    boolean LM_SetMemoryEx(LMProcess process, int dst, int bt, int size);

    boolean LM_ProtMemory(int addr, int size, int prot, IntByReference oldprot);

    boolean LM_ProtMemoryEx(LMProcess process, int addr, int size, int prot, IntByReference oldprot);

    boolean LM_AllocMemory(int size, int prot);

    boolean LM_AllocMemoryEx(LMProcess process, int size, int prot);

    boolean LM_FreeMemory(int alloc, int size);

    boolean LM_FreeMemoryEx(LMProcess process, int alloc, int size);

    boolean LM_DataScan(int data, int size, int addr, int scanSize);

    boolean LM_DataScanEx(LMProcess process, int data, int size, int addr, int scanSize);

    boolean LM_PatternScan(int pattern, byte[] mask, int address, int scanSize);

    boolean LM_PatternScan(LMProcess process, int pattern, byte[] mask, int address, int scanSize);

    boolean LM_PatternScan(byte[] sig, int address, int scanSize);

    boolean LM_PatternScanEx(LMProcess process, byte[] sig, int address, int scanSize);

    boolean LM_HookCode(int from, int to, IntByReference ptrampoline);

    boolean LM_HookCode(LMProcess process, int from, int to, IntByReference ptrampoline);

    boolean LM_UnhookCode(int from, int trampoline, int size);

    boolean LM_UnhookCodeEx(LMProcess process, int from, int trampoline, int size);

    boolean LM_Assemble(String code, LMInst inst);

    boolean LM_AssembleEx(String code, int bits, int runtime_addr, LMInst inst, char[] pcodebuf);

    boolean LM_FreeCodeBuffer(char[] pcodebuf);

    boolean LM_Disassemble(int code, LMInst inst);

    boolean LM_DisassembleEx(int code, int size, int count, int runtime_addr, PointerByReference inst);

    boolean LM_FreeInstructions(LMInst inst);

    boolean LM_CodeLength(int code, int minlength);

    boolean LM_CodeLengthEx(LMProcess process, int code, int minlength);

    boolean LM_VmtNew(IntByReference vtable, LMVmt vmt);

    boolean LM_VmtHook(LMVmt vmt, int index, int dst);

    boolean LM_VmtUnhook(LMVmt vmt, int index);

    boolean LM_VmtGetOriginal(LMVmt vmt, int index);

    boolean LM_VmtReset(LMVmt vmt);

    boolean LM_VmtFree(LMVmt vmt);

}
