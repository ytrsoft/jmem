package com.ytrsoft;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;

import java.util.ArrayList;
import java.util.List;

/**
 *  内存逆向工具类
 */
public final class LibmemUtils {

    private LibmemUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取当前进程的位数
     * @return 位数 (32 或 64)
     */
    public static int getBits() {
        return Libmem.INSTANCE.LM_GetBits();
    }

    /**
     * 获取系统架构的位数
     *
     * @return 系统位数 (32 或 64)
     */
    public static int getSystemBits() {
        return Libmem.INSTANCE.LM_GetSystemBits();
    }

    /**
     * 获取系统的架构标识
     *
     * @return 架构标识
     */
    public static Arch getArchitecture() {
        int arch = Libmem.INSTANCE.LM_GetArchitecture();
        return Arch.valueOf(arch);
    }

    /**
     * 获取当前系统所有进程信息
     *
     * @return 所有进程信息
     */
    public static List<LmProcess> getProcesses() {
        List<LmProcess> processList = new ArrayList<>();
        Libmem.INSTANCE.LM_EnumProcesses((process, arg) -> {
            LmProcess lmProcess = new LmProcess(process);
            processList.add(lmProcess);
            return true;
        }, null);
        return processList;
    }

    /**
     * 获取当前进程的信息
     *
     * @return 当前进程信息
     */
    public static LmProcess getCurrentProcess() {
        Libmem.LmProcess.ByReference nativeProcess = new Libmem.LmProcess.ByReference();
        boolean success = Libmem.INSTANCE.LM_GetProcess(nativeProcess);
        return success ? new LmProcess(nativeProcess) : new LmProcess();
    }

    /**
     * 根据进程 ID 获取进程信息
     *
     * @param processId 进程 ID
     * @return 进程信息
     */
    public static LmProcess getProcessById(int processId) {
        Libmem.LmProcess.ByReference nativeProcess = new Libmem.LmProcess.ByReference();
        boolean success = Libmem.INSTANCE.LM_GetProcessEx(processId, nativeProcess);
        return success ? new LmProcess(nativeProcess) : new LmProcess();
    }

    /**
     * 根据进程名称获取进程信息
     *
     * @param processName 进程名称
     * @return 进程信息
     */
    public static LmProcess getProcessByName(String processName) {
        Libmem.LmProcess.ByReference nativeProcess = new Libmem.LmProcess.ByReference();
        boolean success = Libmem.INSTANCE.LM_FindProcess(processName, nativeProcess);
        return success ? new LmProcess(nativeProcess) : new LmProcess();
    }

    /**
     * 检查指定进程是否存活
     *
     * @param process 进程对象
     * @return 如果进程存活返回 true 否则返回 false
     */
    public static boolean isProcessAlive(LmProcess process) {
        return Libmem.INSTANCE.LM_IsProcessAlive(process.toRef());
    }

    /**
     * 获取系统所有线程信息
     *
     * @return 所有线程信息
     */
    public static List<LmThread> getThreads() {
        List<LmThread> threadList = new ArrayList<>();
        Libmem.INSTANCE.LM_EnumThreads((thread, arg) -> {
            LmThread lmThread = new LmThread(thread);
            threadList.add(lmThread);
            return true;
        }, null);
        return threadList;
    }

    /**
     * 获取指定进程的所有线程信息
     *
     * @param process 进程对象
     * @return 所有包含的线程信息
     */
    public static List<LmThread> getThreads(LmProcess process) {
        List<LmThread> threadList = new ArrayList<>();
        Libmem.INSTANCE.LM_EnumThreadsEx(process.toRef(), (thread, arg) -> {
            LmThread lmThread = new LmThread(thread);
            threadList.add(lmThread);
            return true;
        }, null);
        return threadList;
    }

    /**
     * 获取指定线程所属的进程
     *
     * @param thread 线程对象
     * @return 进程信息
     */
    public static LmProcess getThreadProcess(LmThread thread) {
        Libmem.LmProcess.ByReference nativeProcess = new Libmem.LmProcess.ByReference();
        boolean success = Libmem.INSTANCE.LM_GetThreadProcess(thread.toRef(), nativeProcess);
        return success ? new LmProcess(nativeProcess) : new LmProcess();
    }

    /**
     * 获取给定进程中指定线程的信息
     *
     * @param process 进程对象
     * @return 进程信息
     */
    public static LmThread getThread(LmProcess process) {
        Libmem.LmThread.ByReference nativeThread = new Libmem.LmThread.ByReference();
        boolean success = Libmem.INSTANCE.LM_GetThreadEx(process.toRef(), nativeThread);
        return success ? new LmThread(nativeThread) : new LmThread();
    }

    /**
     * 获取当前线程的信息
     *
     * @return 进程信息
     */
    public static LmThread getCurrentThread() {
        Libmem.LmThread.ByReference nativeThread = new Libmem.LmThread.ByReference();
        boolean success = Libmem.INSTANCE.LM_GetThread(nativeThread);
        return success ? new LmThread(nativeThread) : new LmThread();
    }

    /**
     * 获取当前进程中加载的所有模块
     *
     * @return 所有模块信息
     */
    public static List<LmModule> getModules() {
        List<LmModule> moduleList = new ArrayList<>();
        Libmem.INSTANCE.LM_EnumModules((module, arg) -> {
            LmModule lmModule = new LmModule(module);
            moduleList.add(lmModule);
            return true;
        }, null);
        return moduleList;
    }

    /**
     * 获取指定进程中加载的所有模块
     *
     * @param process 进程对象
     * @return 所有模块信息
     */
    public static List<LmModule> getModules(LmProcess process) {
        List<LmModule> moduleList = new ArrayList<>();
        Libmem.INSTANCE.LM_EnumModulesEx(process.toRef(), (module, arg) -> {
            LmModule lmModule = new LmModule(module);
            moduleList.add(lmModule);
            return true;
        }, null);
        return moduleList;
    }

    /**
     * 根据模块名称获取模块信息
     *
     * @param moduleName 模块名称
     * @return 模块信息
     */
    public static LmModule getModuleByName(String moduleName) {
        Libmem.LmModule.ByReference nativeModule = new Libmem.LmModule.ByReference();
        boolean success = Libmem.INSTANCE.LM_FindModule(moduleName, nativeModule);
        return success ? new LmModule(nativeModule) : new LmModule();
    }

    /**
     * 根据模块名称获取指定进程中的模块信息
     *
     * @param process 进程对象
     * @param moduleName 模块名称
     * @return 模块信息
     */
    public static LmModule getModuleByName(LmProcess process, String moduleName) {
        Libmem.LmModule.ByReference nativeModule = new Libmem.LmModule.ByReference();
        boolean success = Libmem.INSTANCE.LM_FindModuleEx(process.toRef(), moduleName, nativeModule);
        return success ? new LmModule(nativeModule) : new LmModule();
    }

    /**
     * 根据模块路径加载模块
     *
     * @param path 模块路径
     * @return 模块信息
     */
    public static LmModule getModuleByPath(String path) {
        Libmem.LmModule.ByReference nativeModule = new Libmem.LmModule.ByReference();
        boolean success = Libmem.INSTANCE.LM_LoadModule(path, nativeModule);
        return success ? new LmModule(nativeModule) : new LmModule();
    }

    /**
     * 根据模块路径加载指定进程中的模块
     *
     * @param process 进程对象
     * @param path 模块路径
     * @return 模块信息
     */
    public static LmModule getModuleByPath(LmProcess process, String path) {
        Libmem.LmModule.ByReference nativeModule = new Libmem.LmModule.ByReference();
        boolean success = Libmem.INSTANCE.LM_LoadModuleEx(process.toRef(), path, nativeModule);
        return success ? new LmModule(nativeModule) : new LmModule();
    }

    /**
     * 卸载模块
     *
     * @param module 模块对象
     * @return 如果卸载成功返回 true 否则返回 false
     */
    public static boolean unloadModule(LmModule module) {
        return Libmem.INSTANCE.LM_UnloadModule(module.toRef());
    }

    /**
     * 从指定进程中卸载模块
     *
     * @param process 进程对象
     * @param module 模块对象
     * @return 如果卸载成功返回 true 否则返回 false
     */
    public static boolean unloadModule(LmProcess process, LmModule module) {
        return Libmem.INSTANCE.LM_UnloadModuleEx(process.toRef(), module.toRef());
    }

    /**
     * 读取指定地址的内存
     *
     * @param address 内存地址
     * @param size 要读取的字节数
     * @return 读取的内存
     */
    public static Memory readMemory(long address, long size) {
        Memory buffer = new Memory(size);
        Libmem.INSTANCE.LM_ReadMemory(address, buffer, size);
        return buffer;
    }

    /**
     * 从指定进程的内存中读取数据
     *
     * @param process 进程对象
     * @param address 内存地址
     * @param size 要读取的字节数
     * @return 读取的内存
     */
    public static Memory readMemory(LmProcess process, long address, long size) {
        Memory buffer = new Memory(size);
        Libmem.INSTANCE.LM_ReadMemoryEx(process.toRef(), address, buffer, size);
        return buffer;
    }

    /**
     * 向指定内存地址写入数据
     *
     * @param address 内存地址
     * @param memory 要写入的内存
     * @return 成功写入的字节数
     */
    public static long writeMemory(long address, Memory memory) {
        return Libmem.INSTANCE.LM_WriteMemory(address, memory, memory.size());
    }

    /**
     * 向指定进程的内存地址写入数据
     *
     * @param process 进程对象
     * @param address 内存地址
     * @return 成功写入的字节数
     */
    public static long writeMemory(LmProcess process, long address, Memory memory) {
        return Libmem.INSTANCE.LM_WriteMemoryEx(process.toRef(), address, memory, memory.size());
    }

    /**
     * 修改指定内存地址的保护属性
     *
     * @param address 内存地址
     * @param size 要修改保护属性的内存大小
     * @param protection 新的保护属性
     * @return 如果修改成功返回 true 否则返回 false
     */
    public static boolean protectMemory(long address, long size, Protection protection) {
        IntByReference oldProtection = new IntByReference();
        return Libmem.INSTANCE.LM_ProtMemory(address, size, protection.getValue(), oldProtection);
    }

    /**
     * 修改指定进程的内存保护属性
     *
     * @param process 进程对象
     * @param address 内存地址
     * @param size 要修改保护属性的内存大小
     * @param protection 新的保护属性
     * @return 如果修改成功返回 true 否则返回 false
     */
    public static boolean protectMemory(LmProcess process, long address, long size, Protection protection) {
        IntByReference oldProtection = new IntByReference();
        return Libmem.INSTANCE.LM_ProtMemoryEx(process.toRef(), address, size, protection.getValue(), oldProtection);
    }

    /**
     * 分配指定大小的内存 并应用指定的保护属性
     *
     * @param size 分配的内存大小
     * @param protection 内存保护属性
     * @return 分配的内存地址
     */
    public static long allocateMemory(long size, Protection protection) {
        return Libmem.INSTANCE.LM_AllocMemory(size, protection.getValue());
    }

    /**
     * 为指定进程分配指定大小的内存 并应用指定的保护属性
     *
     * @param process 进程对象
     * @param size 分配的内存大小
     * @param protection 内存保护属性
     * @return 分配的内存地址
     */
    public static long allocateMemory(LmProcess process, long size, Protection protection) {
        return Libmem.INSTANCE.LM_AllocMemoryEx(process.toRef(), size, protection.getValue());
    }

    /**
     * 释放指定地址的内存
     *
     * @param address 内存地址
     * @param size 释放的内存大小
     * @return 如果释放成功返回 true 否则返回 false
     */
    public static boolean freeMemory(long address, long size) {
        return Libmem.INSTANCE.LM_FreeMemory(address, size);
    }

    /**
     * 释放指定进程的内存
     *
     * @param process 进程对象
     * @param address 内存地址
     * @param size 释放的内存大小
     * @return 如果释放成功返回 true 否则返回 false
     */
    public static boolean freeMemory(LmProcess process, long address, long size) {
        return Libmem.INSTANCE.LM_FreeMemoryEx(process.toRef(), address, size);
    }

    /**
     * 执行深指针操作 通过基础地址和偏移量计算内存地址
     *
     * @param process 进程对象
     * @param base 基础地址
     * @param offsets 偏移量数组
     * @return 计算后的内存地址
     */
        public static long deepPointer(LmProcess process, long base, long[] offsets) {
            return Libmem.INSTANCE.LM_DeepPointerEx(process.toRef(), base, offsets, offsets.length);
        }

        /**
         * 执行深指针操作 通过基础地址和偏移量计算内存地址
         *
         * @param base 基础地址
         * @param offsets 偏移量数组
         * @return 计算后的内存地址
         */
        public static long deepPointer(long base, long[] offsets) {
            return Libmem.INSTANCE.LM_DeepPointer(base, offsets, offsets.length);
        }

    /**
     * 获取当前进程的索所有内存段信息
     *
     * @return 所有内存段信息
     */
    public static List<LmSegment> getMemorySegments() {
        List<LmSegment> segmentList = new ArrayList<>();
        Libmem.INSTANCE.LM_EnumSegments((segment, arg) -> {
            LmSegment lmSegment = new LmSegment(segment);
            segmentList.add(lmSegment);
            return true;
        }, null);
        return segmentList;
    }

    /**
     * 获取指定进程的内存段信息
     *
     * @param process 进程对象
     * @return 所有内存段信息
     */
    public static List<LmSegment> getMemorySegments(LmProcess process) {
        List<LmSegment> segmentList = new ArrayList<>();
        Libmem.INSTANCE.LM_EnumSegmentsEx(process.toRef(), (segment, arg) -> {
            LmSegment lmSegment = new LmSegment(segment);
            segmentList.add(lmSegment);
            return true;
        }, null);
        return segmentList;
    }

    /**
     * 获取指定地址的内存段信息
     *
     * @param address 内存地址
     * @return 内存段信息
     */
    public static LmSegment getSegment(long address) {
        Libmem.LmSegment.ByReference nativeSegment = new Libmem.LmSegment.ByReference();
        boolean success = Libmem.INSTANCE.LM_FindSegment(address, nativeSegment);
        return success ? new LmSegment(nativeSegment) : new LmSegment();
    }

    /**
     * 获取指定进程中的内存段
     *
     * @param process 进程对象
     * @param address 内存地址
     * @return 内存段信息
     */
    public static LmSegment getSegment(LmProcess process, long address) {
        Libmem.LmSegment.ByReference nativeSegment = new Libmem.LmSegment.ByReference();
        boolean success = Libmem.INSTANCE.LM_FindSegmentEx(process.toRef(), address, nativeSegment);
        return success ? new LmSegment(nativeSegment) : new LmSegment();
    }

    /**
     * 获取模块中的带有解码名称的符号
     *
     * @param module 模块对象
     * @return 所有带有解码名称的符号
     */
    public static List<LmSymbol> getSymbolsDemangled(LmModule module) {
        List<LmSymbol> symbolList = new ArrayList<>();
        Libmem.INSTANCE.LM_EnumSymbolsDemangled(module.toRef(), (symbol, arg) -> {
            LmSymbol lmSymbol = new LmSymbol(symbol);
            symbolList.add(lmSymbol);
            return true;
        }, null);
        return symbolList;
    }

    /**
     * 计算指定进程中的机器代码长度
     *
     * @param process 进程对象
     * @param machineCode 机器代码地址
     * @param minLength 最小代码长度
     * @return 计算出的代码长度
     */
    public static long getCodeLength(LmProcess process, long machineCode, long minLength) {
        return Libmem.INSTANCE.LM_CodeLengthEx(process.toRef(), machineCode, minLength);
    }

    /**
     * 安装代码钩子 在两个地址之间安装钩子
     *
     * @param from 源代码地址
     * @param to 目标代码地址
     * @param trampolineOut 保存跳板地址的引用
     * @return 钩子安装后的跳板地址
     */
    public static long hookCode(long from, long to, LongByReference trampolineOut) {
        return Libmem.INSTANCE.LM_HookCode(from, to, trampolineOut);
    }


    /**
     * 根据符号名称获取模块中的符号地址
     *
     * @param module 模块对象
     * @param symbolName 符号名称
     * @return 符号的内存地址
     */
    public static long getSymbolAddress(LmModule module, String symbolName) {
        return Libmem.INSTANCE.LM_FindSymbolAddress(module.toRef(), symbolName);
    }

    /**
     * 获取解码符号的内存地址
     *
     * @param module 模块对象
     * @param symbolName 符号名称
     * @return 解码符号的内存地址
     */
    public static long getSymbolAddressDemangled(LmModule module, String symbolName) {
        return Libmem.INSTANCE.LM_FindSymbolAddressDemangled(module.toRef(), symbolName);
    }

    /**
     * 解码符号名称
     *
     * @param symbolName 符号名称
     * @return 解码后的符号名称
     */
    public static String demangleSymbol(String symbolName) {
        Pointer demangledBuf = new Memory(1024);
        Pointer result = Libmem.INSTANCE.LM_DemangleSymbol(symbolName, demangledBuf, 1024);
        return result != null ? result.getString(0) : null;
    }

    /**
     * 释放解码后的符号名称
     *
     * @param symbolName 符号名称指针
     */
    public static void freeDemangledSymbol(String symbolName) {
        Libmem.INSTANCE.LM_FreeDemangledSymbol(symbolName);
    }

    /**
     * 执行反汇编操作
     *
     * @param machineCode 机器代码地址
     * @param arch 目标架构
     * @param maxSize 反汇编的最大字节数
     * @param instructionCount 指令数量
     * @param runtimeAddress 运行时地址
     * @return 反汇编后的指令数
     */
    public static long disassemble(long machineCode, Arch arch, long maxSize, long instructionCount, long runtimeAddress) {
        PointerByReference instructionsOut = new PointerByReference();
        return Libmem.INSTANCE.LM_DisassembleEx(machineCode, arch.getValue(), maxSize, instructionCount, runtimeAddress, instructionsOut);
    }

    /**
     * 执行简单的反汇编操作
     *
     * @param machineCode 机器代码地址
     * @param instructionOut 用于存储输出指令的对象
     * @return 如果反汇编成功返回 true 否则返回 false
     */
    public static boolean disassemble(long machineCode, Libmem.LmInst.ByReference instructionOut) {
        return Libmem.INSTANCE.LM_Disassemble(machineCode, instructionOut);
    }

    /**
     * 获取指定机器代码的指令长度
     *
     * @param machineCode 机器代码地址
     * @param minLength 最小指令长度
     * @return 指令长度
     */
    public static long getCodeLength(long machineCode, long minLength) {
        return Libmem.INSTANCE.LM_CodeLength(machineCode, minLength);
    }

    /**
     * 释放已分配的指令内存
     *
     * @param instructions 指令内存指针
     */
    public static void freeInstructions(Memory instructions) {
        Libmem.INSTANCE.LM_FreeInstructions(instructions);
    }

    /**
     * 为指定代码地址安装钩子
     *
     * @param from 源代码地址
     * @param to 目标代码地址
     * @param trampolineOut 保存跳板地址
     * @return 钩子操作的结果地址
     */
    public static long hookCode(LmProcess process, long from, long to) {
        LongByReference trampolineOut = new LongByReference();
        return Libmem.INSTANCE.LM_HookCodeEx(process.toRef(), from, to, trampolineOut);
    }

    /**
     * 移除指定代码地址的钩子
     *
     * @param from 源代码地址
     * @param trampoline 跳板地址
     * @param size 钩子大小
     * @return 如果移除成功返回 true 否则返回 false
     */
    public static boolean unhookCode(long from, long trampoline, long size) {
        return Libmem.INSTANCE.LM_UnhookCode(from, trampoline, size);
    }

    /**
     * 从指定进程中移除代码钩子
     *
     * @param process 进程对象
     * @param from 源代码地址
     * @param trampoline 跳板地址
     * @param size 钩子大小
     * @return 如果移除成功返回 true 否则返回 false
     */
    public static boolean unhookCode(LmProcess process, long from, long trampoline, long size) {
        return Libmem.INSTANCE.LM_UnhookCodeEx(process.toRef(), from, trampoline, size);
    }

    /**
     * 进行模式扫描 根据指定模式在内存中搜索数据
     *
     * @param pattern 要匹配的字节模式
     * @param mask 模式掩码
     * @param address 搜索起始地址
     * @param scanSize 搜索的内存大小
     * @return 匹配模式的内存地址 如果未找到返回 0
     */
    public static long patternScan(byte[] pattern, String mask, long address, long scanSize) {
        Memory patternMemory = new Memory(pattern.length);
        patternMemory.write(0, pattern, 0, pattern.length);
        return Libmem.INSTANCE.LM_PatternScan(patternMemory, mask, address, scanSize);
    }

    /**
     * 在指定进程中进行模式扫描 根据指定模式搜索数据
     *
     * @param process 进程对象
     * @param pattern 要匹配的字节模式
     * @param mask 模式掩码
     * @param address 搜索起始地址
     * @param scanSize 搜索的内存大小
     * @return 匹配模式的内存地址 如果未找到返回 0
     */
    public static long patternScan(LmProcess process, byte[] pattern, String mask, long address, long scanSize) {
        Memory patternMemory = new Memory(pattern.length);
        patternMemory.write(0, pattern, 0, pattern.length);
        return Libmem.INSTANCE.LM_PatternScanEx(process.toRef(), patternMemory, mask, address, scanSize);
    }

    /**
     * 进行签名扫描 根据指定签名搜索内存
     *
     * @param signature 要匹配的签名
     * @param address 搜索起始地址
     * @param scanSize 搜索的内存大小
     * @return 匹配签名的内存地址 如果未找到返回 0
     */
    public static long signatureScan(String signature, long address, long scanSize) {
        return Libmem.INSTANCE.LM_SigScan(signature, address, scanSize);
    }

    /**
     * 在指定进程中进行签名扫描 根据指定签名搜索内存
     *
     * @param process 进程对象
     * @param signature 要匹配的签名
     * @param address 搜索起始地址
     * @param scanSize 搜索的内存大小
     * @return 匹配签名的内存地址 如果未找到返回 0
     */
    public static long signatureScan(LmProcess process, String signature, long address, long scanSize) {
        return Libmem.INSTANCE.LM_SigScanEx(process.toRef(), signature, address, scanSize);
    }

    /**
     * 执行数据扫描 在内存中搜索特定数据
     *
     * @param data 要搜索的数据
     * @param dataSize 数据大小
     * @param address 搜索起始地址
     * @param scanSize 搜索的内存大小
     * @return 匹配数据的内存地址 如果未找到返回 0
     */
    public static long dataScan(byte[] data, long dataSize, long address, long scanSize) {
        Memory dataMemory = new Memory(data.length);
        dataMemory.write(0, data, 0, data.length);
        return Libmem.INSTANCE.LM_DataScan(dataMemory, dataSize, address, scanSize);
    }

    /**
     * 在指定进程中执行数据扫描
     *
     * @param process 进程对象
     * @param data 要搜索的数据
     * @param dataSize 数据大小
     * @param address 搜索起始地址
     * @param scanSize 搜索的内存大小
     * @return 匹配数据的内存地址 如果未找到返回 0
     */
    public static long dataScan(LmProcess process, byte[] data, long dataSize, long address, long scanSize) {
        Memory dataMemory = new Memory(data.length);
        dataMemory.write(0, data, 0, data.length);
        return Libmem.INSTANCE.LM_DataScanEx(process.toRef(), dataMemory, dataSize, address, scanSize);
    }

    /**
     * 设置内存值
     *
     * @param process 进程对象
     * @param address 内存地址
     * @param value 要设置的字节值
     * @param size 设置的内存大小
     * @return 成功操作的字节数
     */
    public static long setMemory(LmProcess process, long address, byte value, long size) {
        return Libmem.INSTANCE.LM_SetMemoryEx(process.toRef(), address, value, size);
    }

    /**
     * 设置内存值
     *
     * @param address 内存地址
     * @param value 要设置的字节值
     * @param size 设置的内存大小
     * @return 成功操作的字节数
     */
    public static long setMemory(long address, byte value, long size) {
        return Libmem.INSTANCE.LM_SetMemory(address, value, size);
    }

    /**
     * 将汇编代码转为机器代码
     *
     * @param code 汇编代码
     * @param instructionOut 用于存储输出指令的对象
     * @return 如果汇编成功返回 true 否则返回 false
     */
    public static boolean assemble(String code, Libmem.LmInst.ByReference instructionOut) {
        return Libmem.INSTANCE.LM_Assemble(code, instructionOut);
    }

    /**
     * 扩展汇编操作
     *
     * @param code 汇编代码
     * @param arch 目标架构
     * @param runtimeAddress 运行时地址
     * @param payloadOut 用于存储输出的代码段
     * @return 汇编后的机器代码地址
     */
    public static long assemble(String code, Arch arch, long runtimeAddress, PointerByReference payloadOut) {
        return Libmem.INSTANCE.LM_AssembleEx(code, arch.getValue(), runtimeAddress, payloadOut);
    }

    /**
     * 释放汇编代码内存
     *
     * @param payload 汇编代码指针
     */
    public static void freePayload(Pointer payload) {
        Libmem.INSTANCE.LM_FreePayload(payload);
    }

    /**
     * 创建新的虚方法表
     *
     * @param vtable 虚表数据
     * @param vmtOut 用于存储输出虚方法表的对象
     * @return 如果创建成功返回 true 否则返回 false
     */
    public static boolean createVmt(byte[] vtable, Libmem.LmVmt.ByReference vmtOut) {
        Memory vtableMemory = new Memory(vtable.length);
        vtableMemory.write(0, vtable, 0, vtable.length);
        return Libmem.INSTANCE.LM_VmtNew(vtableMemory, vmtOut);
    }

    /**
     * 为虚方法表中的方法安装钩子
     *
     * @param vmt 虚方法表对象
     * @param fromFnIndex 源函数索引
     * @param to 替换函数地址
     * @return 如果钩子成功返回 true 否则返回 false
     */
    public static boolean hookVmt(Libmem.LmVmt vmt, long fromFnIndex, long to) {
        return Libmem.INSTANCE.LM_VmtHook(vmt, fromFnIndex, to);
    }

    /**
     * 移除虚方法表中的钩子
     *
     * @param vmt 虚方法表对象
     * @param fnIndex 函数索引
     * @return 如果钩子移除成功返回 true 否则返回 false
     */
    public static boolean unhookVmt(Libmem.LmVmt vmt, long fnIndex) {
        return Libmem.INSTANCE.LM_VmtUnhook(vmt, fnIndex);
    }

    /**
     * 获取虚方法表中原始函数地址
     *
     * @param vmt 虚方法表对象
     * @param fnIndex 函数索引
     * @return 原始函数地址
     */
    public static long getOriginalVmt(Libmem.LmVmt vmt, long fnIndex) {
        return Libmem.INSTANCE.LM_VmtGetOriginal(vmt, fnIndex);
    }

    /**
     * 重置虚方法表
     *
     * @param vmt 虚方法表对象
     */
    public static void resetVmt(Libmem.LmVmt vmt) {
        Libmem.INSTANCE.LM_VmtReset(vmt);
    }

    /**
     * 释放虚方法表
     *
     * @param vmt 虚方法表对象
     */
    public static void freeVmt(Libmem.LmVmt vmt) {
        Libmem.INSTANCE.LM_VmtFree(vmt);
    }
}
