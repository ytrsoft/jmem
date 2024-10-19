package com.ytrsoft.core;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;

public interface Libmem extends StdCallLibrary {

    Libmem INSTANCE = Native.load("libmem", Libmem.class);

    int LM_PATH_MAX = 4096;
    int LM_INST_MAX = 16;
    int LM_MNEMONIC_MAX = 32;
    int LM_OP_MAX = 160;

    @Structure.FieldOrder({"pid", "ppid", "arch", "bits", "start_time", "path", "name"})
    public static class LmProcess extends Structure {
        public int pid;
        public int ppid;
        public int arch;
        public long bits;
        public long start_time;
        public byte[] path = new byte[LM_PATH_MAX];
        public byte[] name = new byte[LM_PATH_MAX];

        public static class ByReference extends LmProcess implements Structure.ByReference {}
    }

    @Structure.FieldOrder({"tid", "owner_pid"})
    public static class LmThread extends Structure {
        public int tid;
        public int owner_pid;

        public static class ByReference extends LmThread implements Structure.ByReference {}
    }

    @Structure.FieldOrder({"base", "end", "size", "path", "name"})
    public static class LmModule extends Structure {
        public long base;
        public long end;
        public long size;
        public byte[] path = new byte[LM_PATH_MAX];
        public byte[] name = new byte[LM_PATH_MAX];

        public static class ByReference extends LmModule implements Structure.ByReference {}
    }

    @Structure.FieldOrder({"base", "end", "size", "prot"})
    public static class LmSegment extends Structure {
        public long base;
        public long end;
        public long size;
        public int prot;

        public static class ByReference extends LmSegment implements Structure.ByReference {}
    }

    @Structure.FieldOrder({"address", "size", "bytes", "mnemonic", "opStr"})
    public static class LmInst extends Structure {
        public long address;
        public int size;
        public byte[] bytes = new byte[LM_INST_MAX];
        public byte[] mnemonic = new byte[LM_MNEMONIC_MAX];
        public byte[] opStr = new byte[LM_OP_MAX];

        public static class ByReference extends LmInst implements Structure.ByReference {}
    }

    @Structure.FieldOrder({"name", "address"})
    public static class LmSymbol extends Structure {
        public String name;
        public long address;

        public static class ByReference extends LmSymbol implements Structure.ByReference {}
    }

    @Structure.FieldOrder({"orig_func", "index", "next"})
    public static class LmVmtEntry extends Structure {
        public Pointer orig_func;
        public long index;
        public LmVmtEntry.ByReference next;
        public static class ByReference extends LmVmtEntry implements Structure.ByReference {}
    }

    @Structure.FieldOrder({"vtable", "hkentries"})
    public static class LmVmt extends Structure {
        public Pointer vtable;
        public LmVmtEntry.ByReference hkentries;
        public static class ByReference extends LmVmt implements Structure.ByReference {}
    }

    public interface EnumProcessesCallback extends StdCallLibrary.StdCallCallback {
        boolean callback(LmProcess process, Pointer arg);
    }

    public interface EnumThreadsCallback extends StdCallLibrary.StdCallCallback {
        boolean callback(LmThread thread, Pointer arg);
    }

    public interface EnumModulesCallback extends StdCallLibrary.StdCallCallback {
        boolean callback(LmModule module, Pointer arg);
    }

    public interface EnumSegmentsCallback extends StdCallLibrary.StdCallCallback {
        boolean callback(LmSegment segment, Pointer arg);
    }

    public interface EnumSymbolsCallback extends StdCallLibrary.StdCallCallback {
        boolean callback(LmSymbol symbol, Pointer arg);
    }

    boolean LM_EnumProcesses(EnumProcessesCallback callback, Object o);

    boolean LM_GetProcess(LmProcess.ByReference processOut);

    boolean LM_GetProcessEx(int pid, LmProcess.ByReference processOut);

    boolean LM_FindProcess(String processName, LmProcess.ByReference processOut);

    boolean LM_IsProcessAlive(LmProcess process);

    int LM_GetBits();

    int LM_GetSystemBits();

    boolean LM_EnumThreads(EnumThreadsCallback callback, Pointer arg);

    boolean LM_EnumThreadsEx(LmProcess process, EnumThreadsCallback callback, Pointer arg);

    boolean LM_GetThread(LmThread.ByReference threadOut);

    boolean LM_GetThreadEx(LmProcess process, LmThread.ByReference threadOut);

    boolean LM_GetThreadProcess(LmThread thread, LmProcess.ByReference processOut);

    boolean LM_EnumModules(EnumModulesCallback callback, Pointer arg);

    boolean LM_EnumModulesEx(LmProcess process, EnumModulesCallback callback, Pointer arg);

    boolean LM_FindModule(String name, LmModule.ByReference moduleOut);

    boolean LM_FindModuleEx(LmProcess process, String name, LmModule.ByReference moduleOut);

    boolean LM_LoadModule(String path, LmModule.ByReference moduleOut);

    boolean LM_LoadModuleEx(LmProcess process, String path, LmModule.ByReference moduleOut);

    boolean LM_UnloadModule(LmModule module);

    boolean LM_UnloadModuleEx(LmProcess process, LmModule module);

    long LM_FindSymbolAddress(LmModule module, String symbolName);

    Pointer LM_DemangleSymbol(String symbolName, Pointer demangledBuf, int maxsize);

    void LM_FreeDemangledSymbol(String symbolName);

    boolean LM_EnumSymbolsDemangled(LmModule module, EnumSymbolsCallback callback, Pointer arg);

    long LM_FindSymbolAddressDemangled(LmModule module, String symbolName);

    boolean LM_EnumSegments(EnumSegmentsCallback callback, Pointer arg);

    boolean LM_EnumSegmentsEx(LmProcess process, EnumSegmentsCallback callback, Pointer arg);

    boolean LM_FindSegment(long address, LmSegment.ByReference segmentOut);

    boolean LM_FindSegmentEx(LmProcess process, long address, LmSegment.ByReference segmentOut);

    long LM_ReadMemoryEx(LmProcess process, long source, Pointer dest, long size);

    long LM_WriteMemoryEx(LmProcess process, long dest, Pointer source, long size);

    long LM_SetMemory(long dest, byte value, long size);

    long LM_SetMemoryEx(LmProcess process, long dest, byte value, long size);

    boolean LM_ProtMemory(long address, long size, int prot, IntByReference oldProtOut);

    boolean LM_ProtMemoryEx(LmProcess process, long address, long size, int prot, IntByReference oldProtOut);

    long LM_AllocMemory(long size, int prot);

    long LM_AllocMemoryEx(LmProcess process, long size, int prot);

    boolean LM_FreeMemory(long alloc, long size);

    boolean LM_FreeMemoryEx(LmProcess process, long alloc, long size);

    long LM_DeepPointer(long base, long[] offsets, int noffsets);

    long LM_DeepPointerEx(LmProcess process, long base, long[] offsets, int noffsets);

    long LM_DataScan(Pointer data, long datasize, long address, long scansize);

    long LM_DataScanEx(LmProcess process, Pointer data, long datasize, long address, long scansize);

    long LM_PatternScan(Pointer pattern, String mask, long address, long scansize);

    long LM_PatternScanEx(LmProcess process, Pointer pattern, String mask, long address, long scansize);

    long LM_SigScan(String signature, long address, long scansize);

    long LM_SigScanEx(LmProcess process, String signature, long address, long scansize);

    int LM_GetArchitecture();

    boolean LM_Assemble(String code, LmInst.ByReference instructionOut);

    long LM_AssembleEx(String code, int arch, long runtimeAddress, PointerByReference payloadOut);

    void LM_FreePayload(Pointer payload);

    boolean LM_Disassemble(long machineCode, LmInst.ByReference instructionOut);

    long LM_DisassembleEx(long machineCode, int arch, long maxSize, long instructionCount, long runtimeAddress, PointerByReference instructionsOut);

    void LM_FreeInstructions(Pointer instructions);

    long LM_CodeLengthEx(LmProcess process, long machineCode, long minLength);

    long LM_HookCodeEx(LmProcess process, long from, long to, LongByReference trampolineOut);

    boolean LM_UnhookCode(long from, long trampoline, long size);

    boolean LM_UnhookCodeEx(LmProcess process, long from, long trampoline, long size);

    boolean LM_VmtNew(Pointer vtable, LmVmt.ByReference vmtOut);

    boolean LM_VmtHook(LmVmt vmt, long fromFnIndex, long to);

    boolean LM_VmtUnhook(LmVmt vmt, long fnIndex);

    long LM_VmtGetOriginal(LmVmt vmt, long fnIndex);

    void LM_VmtReset(LmVmt vmt);

    void LM_VmtFree(LmVmt vmt);
}
