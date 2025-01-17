package com.ytrsoft;

import com.sun.jna.Memory;
import com.sun.jna.ptr.LongByReference;
import com.ytrsoft.core.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * junit 测试
 */
public class LibmemTest {

    private String modulePath;
    private LmProcess MarioXP_0121;


    @Before
    public void setUp() {
        modulePath = "C:\\WINDOWS\\System32\\libmem.dll";
        MarioXP_0121 = LibmemUtils.getProcessByName("MarioXP_0121.exe");
    }

    @Test
    public void testGetSystemBits() {
        int systemBits = LibmemUtils.getSystemBits();
        System.out.println(systemBits);
    }

    @Test
    public void testGetBits() {
        int bits = LibmemUtils.getBits();
        System.out.println(bits);
    }

    @Test
    public void testGetArch() {
        Arch arch = LibmemUtils.getArch();
        System.out.println(arch);
    }

    @Test
    public void testGetProcesses() {
        List<LmProcess> processes = LibmemUtils.getProcesses();
        processes.forEach(System.out::println);
    }

    @Test
    public void testGetProcessById() {
        LmProcess process = LibmemUtils.getProcessById(MarioXP_0121.getId());
        System.out.println(process);
    }

    @Test
    public void testGetProcessByName() {
        LmProcess process = LibmemUtils.getProcessByName(MarioXP_0121.getName());
        System.out.println(process);
    }

    @Test
    public void testIsProcessAlive() {
        LmProcess process = LibmemUtils.getCurrentProcess();
        boolean alive = LibmemUtils.isProcessAlive(process);
        System.out.println(alive);
    }

    @Test
    public void testGetCurrentProcess() {
        LmProcess currentProcess = LibmemUtils.getCurrentProcess();
        System.out.println(currentProcess);
    }

    @Test
    public void testGetThreads() {
        List<LmThread> threads = LibmemUtils.getThreads();
        threads.forEach(System.out::println);
    }

    @Test
    public void testGetThreadsByLmProcess() {
        LmProcess process = LibmemUtils.getProcessByName(MarioXP_0121.getName());
        List<LmThread> threads = LibmemUtils.getThreads(process);
        threads.forEach(System.out::println);
    }

    @Test
    public void testGetThreadProcess() {
        LmProcess process = LibmemUtils.getProcessByName(MarioXP_0121.getName());
        List<LmThread> threads = LibmemUtils.getThreads(process);
        LmProcess headProcess = LibmemUtils.getThreadProcess(threads.get(0));
        System.out.println(headProcess);
    }

    @Test
    public void testGetThread() {
        LmProcess process = LibmemUtils.getProcessByName(MarioXP_0121.getName());
        LmThread threads = LibmemUtils.getThread(process);
        System.out.println(threads);
    }

    @Test
    public void testGetCurrentThread() {
        LmThread threads = LibmemUtils.getCurrentThread();
        System.out.println(threads);
    }

    @Test
    public void testGetModules() {
        List<LmModule> modules = LibmemUtils.getModules();
        modules.forEach(System.out::println);
    }

    @Test
    public void testGetModulesByLmProcess() {
        LmProcess process = LibmemUtils.getProcessById(MarioXP_0121.getId());
        List<LmModule> modules = LibmemUtils.getModules(process);
        modules.forEach(System.out::println);
    }

    @Test
    public void testGetModulesByLmName() {
        File file = new File(modulePath);
        LmModule module = LibmemUtils.getModuleByName(file.getName());
        System.out.println(module);
    }

    @Test
    public void testGetModulesByPath() {
        LmModule module = LibmemUtils.getModuleByPath(modulePath);
        System.out.println(module);
    }

    @Test
    public void testGetModulesByNameAndLmProcess() {
        LmModule module = LibmemUtils.getModuleByName(MarioXP_0121, MarioXP_0121.getName());
        System.out.println(module);
    }

    @Test
    public void testGetModulesByPathAndLmProcess() {
        LmModule module = LibmemUtils.getModuleByPath(MarioXP_0121, MarioXP_0121.getPath());
        System.out.println(module);
    }

    @Test
    public void testUnloadModule() {
        LmModule root = LibmemUtils.getModuleByPath(modulePath);
        boolean status = LibmemUtils.unloadModule(root);
        System.out.println(status);
    }

    @Test
    public void testUnloadModuleV2() {
        LmModule root = LibmemUtils.getModuleByPath(modulePath);
        LmModule module = LibmemUtils.getModuleByName(MarioXP_0121, root.getName());
        boolean status = LibmemUtils.unloadModule(MarioXP_0121, module);
        System.out.println(status);
    }

    @Test
    public void testReadMemory() {
        long hp = 0x00428282;
        Memory memory = LibmemUtils.readMemory(MarioXP_0121, hp, 4);
        System.out.println(memory.getInt(0));
    }

    @Test
        public void testWriteMemory() {
        long hp = 0x00428282;
        Memory memory = new Memory(4);
        memory.setInt(0, 10);
        long writeSize = LibmemUtils.writeMemory(MarioXP_0121, hp, memory);
        System.out.println(writeSize);
    }

    @Test
    @Deprecated
    public void testGetMemorySegments() {
        List<LmSegment> segments = LibmemUtils.getMemorySegments();
        segments.forEach(System.out::println);
    }

    @Test
    @Deprecated
    public void testGetMemorySegmentsV2() {
        List<LmSegment> segments = LibmemUtils.getMemorySegments(MarioXP_0121);
        segments.forEach(System.out::println);
    }

    @Test
    @Deprecated
    public void testGetSegment() {
        long address = 2147352576;
        LmSegment segments = LibmemUtils.getSegment(address);
        System.out.println(segments);
    }

    @Test
    @Deprecated
    public void testGetSegmentV2() {
        long address = 2147352576;
        LmProcess process = LibmemUtils.getCurrentProcess();
        LmSegment segments = LibmemUtils.getSegment(process, address);
        System.out.println(segments);
    }

    @Test
    public void testGetSymbolsDemangled() {
        LmModule module = LibmemUtils.getModuleByPath(modulePath);
        List<LmSymbol> lmSymbols = LibmemUtils.getSymbolsDemangled(module);
        lmSymbols.forEach(System.out::println);
    }

    @Test
    @Deprecated
    public void testProtectMemory() {
        long address = 140733513662464L;
        Protection protection = LibmemUtils.protectMemory(address, 0, Protection.READ_WRITE);
        System.out.println(protection);
    }

    @Test
    public void testProtectMemoryWithProcess() {
        long address = 0x0019CA98;
        Protection protection = LibmemUtils.protectMemory(MarioXP_0121, address, 0, Protection.READ_EXECUTE);
        System.out.println(protection);
    }

    @Test
    public void testFreeMemory() {
        long address = LibmemUtils.allocateMemory(4096, Protection.READ_WRITE);
        boolean result = LibmemUtils.freeMemory(address, 4096);
        System.out.println(result);
    }

    @Test
    public void testFreeMemoryWithProcess() {
        long address = LibmemUtils.allocateMemory(MarioXP_0121, 4096, Protection.READ_WRITE);
        boolean result = LibmemUtils.freeMemory(MarioXP_0121, address, 4096);
        System.out.println(result);
    }

    @Test
    public void testDeepPointerWithProcess() {
        long address = 0x000998D0;
        long[] offsets = {0x00};
        long result = LibmemUtils.deepPointer(MarioXP_0121, address, offsets);
        System.out.println(result);
    }

    @Test
    @Deprecated
    public void testDeepPointerWithoutProcess() {
        long address = 140733513662464L;
        long[] offsets = {0x00};
        long result = LibmemUtils.deepPointer(address, offsets);
        System.out.println(result);
    }

    @Test
    public void testGetCodeLengthEx() {
        long machineCode = 0x000998D0;
        long minLength = 1;
        long codeLength = LibmemUtils.getCodeLength(MarioXP_0121, machineCode, minLength);
        System.out.println(codeLength);
    }

    @Test
    public void testGetSymbolAddress() {
        String symbolName = "LM_ReadMemoryEx";
        LmModule module = LibmemUtils.getModuleByPath(modulePath);
        long symbolAddress = LibmemUtils.getSymbolAddress(module, symbolName);
        System.out.println(Long.toHexString(symbolAddress));
    }

    @Test
    public void testGetSymbolAddressDemangled() {
        String symbolName = "LM_ReadMemoryEx";
        LmModule module = LibmemUtils.getModuleByPath(modulePath);
        long symbolAddress = LibmemUtils.getSymbolAddressDemangled(module, symbolName);
        System.out.println(Long.toHexString(symbolAddress));
    }

    @Test
    public void testDemangleSymbol() {
        String symbolName = "LM_ReadMemoryEx";
        String demangled = LibmemUtils.demangleSymbol(symbolName);
        System.out.println(demangled);
    }

    @Test
    public void testFreeDemangledSymbol() {
        String symbolName = "LM_ReadMemoryEx";
        LibmemUtils.freeDemangledSymbol(symbolName);
    }

    @Test
    public void testDisassemble() {
        LmModule module = LibmemUtils.getModuleByPath(modulePath);
        List<LmSymbol> symbols = LibmemUtils.getSymbolsDemangled(module);
        if (!symbols.isEmpty()) {
            long address = symbols.get(0).getAddress();
            long result = LibmemUtils.disassemble(address, Arch.X64, 64, 0, address);
            System.out.println(result);
        }
    }

    @Test
    @Deprecated
    public void testDisassembleV2() {
        LmModule module = LibmemUtils.getModuleByPath(modulePath);
        List<LmSymbol> symbols = LibmemUtils.getSymbolsDemangled(module);
        if (!symbols.isEmpty()) {
            Libmem.LmInst.ByReference inst = new Libmem.LmInst.ByReference();
            long address = symbols.get(0).getAddress();
            boolean result = LibmemUtils.disassemble(address, inst);
            System.out.println(result);
            System.out.println(inst);
        }
    }

    @Test
    public void testFreeInstructions() {
        Memory instructions = new Memory(1024);
        LibmemUtils.freeInstructions(instructions);
    }

    @Test
    public void testHookCodeWithProcess() {
        long fromAddress = 0x00400000;
        long toAddress = 0x00401000;
        long hookResult = LibmemUtils.hookCode(MarioXP_0121, fromAddress, toAddress);
        System.out.println(hookResult);
    }

    @Test
    @Deprecated
    public void testUnhookCode() {
        long trampoline = 0x00402000;
        long size = 64;
        long machineCodeAddress = 0x00400000;
        boolean result = LibmemUtils.unhookCode(machineCodeAddress, trampoline, size);
        System.out.println(result);
    }

    @Test
    public void testUnhookCodeWithProcess() {
        long trampoline = 0x00402000;
        long size = 64;
        long machineCodeAddress = 0x00400000;
        boolean result = LibmemUtils.unhookCode(MarioXP_0121, machineCodeAddress, trampoline, size);
        System.out.println(result);
    }

}
