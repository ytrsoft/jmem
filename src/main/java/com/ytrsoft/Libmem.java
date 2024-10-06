package com.ytrsoft;

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

        @Override
        public String toString() {
            return String.format("LmProcess{pid=%d, ppid=%d, arch=%d, bits=%d, start_time=%d, path=%s, name=%s}",
                    pid, ppid, arch, bits, start_time, Native.toString(path).trim(), Native.toString(name).trim());
        }
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
        boolean callback(LmModule module, Pointer arg);
    }

    // 枚举进程
    boolean LM_EnumProcesses(EnumProcessesCallback callback);

    // 获取当前进程信息
    boolean LM_GetProcess(LmProcess.ByReference processOut);

    // 根据进程ID获取进程信息
    boolean LM_GetProcessEx(int pid, LmProcess.ByReference processOut);

    // 查找进程
    boolean LM_FindProcess(String processName, LmProcess.ByReference processOut);

    // 检查进程是否存活
    boolean LM_IsProcessAlive(LmProcess process);

    // 获取当前进程的位数
    int LM_GetBits();

    // 获取系统架构的位数
    int LM_GetSystemBits();

    // 枚举线程
    boolean LM_EnumThreads(EnumThreadsCallback callback, Pointer arg);

    // 枚举指定进程中的线程
    boolean LM_EnumThreadsEx(LmProcess process, EnumThreadsCallback callback, Pointer arg);

    // 获取当前线程信息
    boolean LM_GetThread(LmThread.ByReference threadOut);

    // 获取指定进程的线程信息
    boolean LM_GetThreadEx(LmProcess process, LmThread.ByReference threadOut);

    // 获取线程所属的进程
    boolean LM_GetThreadProcess(LmThread thread, LmProcess.ByReference processOut);

    // 枚举模块
    boolean LM_EnumModules(EnumModulesCallback callback, Pointer arg);

    // 枚举指定进程的模块
    boolean LM_EnumModulesEx(LmProcess process, EnumModulesCallback callback, Pointer arg);

    // 查找模块
    boolean LM_FindModule(String name, LmModule.ByReference moduleOut);

    // 在指定进程中查找模块
    boolean LM_FindModuleEx(LmProcess process, String name, LmModule.ByReference moduleOut);

    // 加载模块
    boolean LM_LoadModule(String path, LmModule.ByReference moduleOut);

    // 在指定进程中加载模块
    boolean LM_LoadModuleEx(LmProcess process, String path, LmModule.ByReference moduleOut);

    // 卸载模块
    boolean LM_UnloadModule(LmModule module);

    // 在指定进程中卸载模块
    boolean LM_UnloadModuleEx(LmProcess process, LmModule module);

    // 查找符号地址
    long LM_FindSymbolAddress(LmModule module, String symbolName);

    // 解码符号名称
    Pointer LM_DemangleSymbol(String symbolName, Pointer demangledBuf, int maxsize);

    // 释放解码后的符号名称
    void LM_FreeDemangledSymbol(Pointer symbolName);

    // 在模块中枚举符号并解码
    boolean LM_EnumSymbolsDemangled(LmModule module, EnumSymbolsCallback callback, Pointer arg);

    // 查找解码后的符号地址
    long LM_FindSymbolAddressDemangled(LmModule module, String symbolName);

    // 枚举内存段
    boolean LM_EnumSegments(EnumSegmentsCallback callback, Pointer arg);

    // 在指定进程中枚举内存段
    boolean LM_EnumSegmentsEx(LmProcess process, EnumSegmentsCallback callback, Pointer arg);

    // 查找内存段
    boolean LM_FindSegment(long address, LmSegment.ByReference segmentOut);

    // 在指定进程中查找内存段
    boolean LM_FindSegmentEx(LmProcess process, long address, LmSegment.ByReference segmentOut);

    // 读取内存
    long LM_ReadMemory(long source, Pointer dest, long size);

    // 在指定进程中读取内存
    long LM_ReadMemoryEx(LmProcess process, long source, Pointer dest, long size);

    // 写入内存
    long LM_WriteMemory(long dest, Pointer source, long size);

    // 在指定进程中写入内存
    long LM_WriteMemoryEx(LmProcess process, long dest, Pointer source, long size);

    // 设置内存值
    long LM_SetMemory(long dest, byte value, long size);

    // 在指定进程中设置内存值
    long LM_SetMemoryEx(LmProcess process, long dest, byte value, long size);

    // 修改内存保护
    boolean LM_ProtMemory(long address, long size, int prot, IntByReference oldProtOut);

    // 在指定进程中修改内存保护
    boolean LM_ProtMemoryEx(LmProcess process, long address, long size, int prot, IntByReference oldProtOut);

    // 分配内存
    long LM_AllocMemory(long size, int prot);

    // 在指定进程中分配内存
    long LM_AllocMemoryEx(LmProcess process, long size, int prot);

    // 释放内存
    boolean LM_FreeMemory(long alloc, long size);

    // 在指定进程中释放内存
    boolean LM_FreeMemoryEx(LmProcess process, long alloc, long size);

    // 深指针操作
    long LM_DeepPointer(long base, long[] offsets, int noffsets);

    // 在指定进程中进行深指针操作
    long LM_DeepPointerEx(LmProcess process, long base, long[] offsets, int noffsets);

    // 内存扫描
    long LM_DataScan(Pointer data, long datasize, long address, long scansize);

    // 在指定进程中进行内存扫描
    long LM_DataScanEx(LmProcess process, Pointer data, long datasize, long address, long scansize);

    // 模式扫描
    long LM_PatternScan(Pointer pattern, String mask, long address, long scansize);

    // 在指定进程中进行模式扫描
    long LM_PatternScanEx(LmProcess process, Pointer pattern, String mask, long address, long scansize);

    // 签名扫描
    long LM_SigScan(String signature, long address, long scansize);

    // 在指定进程中进行签名扫描
    long LM_SigScanEx(LmProcess process, String signature, long address, long scansize);

    // 获取架构信息
    int LM_GetArchitecture();

    // 汇编指令
    boolean LM_Assemble(String code, LmInst.ByReference instructionOut);

    // 扩展汇编指令
    long LM_AssembleEx(String code, int arch, long runtimeAddress, PointerByReference payloadOut);

    // 释放汇编代码
    void LM_FreePayload(Pointer payload);

    // 反汇编指令
    boolean LM_Disassemble(long machineCode, LmInst.ByReference instructionOut);

    // 扩展反汇编指令
    long LM_DisassembleEx(long machineCode, int arch, long maxSize, long instructionCount, long runtimeAddress, PointerByReference instructionsOut);

    // 释放反汇编指令
    void LM_FreeInstructions(Pointer instructions);

    // 计算代码长度
    long LM_CodeLength(long machineCode, long minLength);

    // 在指定进程中计算代码长度
    long LM_CodeLengthEx(LmProcess process, long machineCode, long minLength);

    // 安装代码钩子
    long LM_HookCode(long from, long to, LongByReference trampolineOut);

    // 在指定进程中安装代码钩子
    long LM_HookCodeEx(LmProcess process, long from, long to, LongByReference trampolineOut);

    // 移除代码钩子
    boolean LM_UnhookCode(long from, long trampoline, long size);

    // 在指定进程中移除代码钩子
    boolean LM_UnhookCodeEx(LmProcess process, long from, long trampoline, long size);

    // 创建新的虚拟方法表
    boolean LM_VmtNew(Pointer vtable, LmVmt.ByReference vmtOut);

    // 安装虚拟方法表钩子
    boolean LM_VmtHook(LmVmt vmt, long fromFnIndex, long to);

    // 移除虚拟方法表钩子
    boolean LM_VmtUnhook(LmVmt vmt, long fnIndex);

    // 获取虚拟方法表原始函数
    long LM_VmtGetOriginal(LmVmt vmt, long fnIndex);

    // 重置虚拟方法表
    void LM_VmtReset(LmVmt vmt);

    // 释放虚拟方法表
    void LM_VmtFree(LmVmt vmt);
}
