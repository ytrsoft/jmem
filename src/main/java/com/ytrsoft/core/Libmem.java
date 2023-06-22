package com.ytrsoft.core;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public interface Libmem extends StdCallLibrary {

    Libmem INSTANCE = Native.load("libmem", Libmem.class, W32APIOptions.DEFAULT_OPTIONS);

    // LM_EnumProcesses
    // LM_GetProcess
    // LM_GetProcessEx
    // LM_FindProcess
    // LM_IsProcessAlive
    // LM_GetSystemBits

    // LM_EnumThreads
    // LM_EnumThreadsEx
    // LM_GetThread
    // LM_GetThreadEx
    // LM_GetThreadProcess

    // LM_EnumModules
    // LM_EnumModulesEx
    // LM_FindModule
    // LM_FindModuleEx
    // LM_LoadModule
    // LM_LoadModuleEx
    // LM_UnloadModule
    // LM_UnloadModuleEx

    // LM_EnumSymbols
    // LM_FindSymbolAddress

    // LM_EnumPages
    // LM_EnumPagesEx
    // LM_GetPage
    // LM_GetPageEx

    // LM_ReadMemory
    // LM_ReadMemoryEx
    // LM_WriteMemory
    // LM_WriteMemoryEx
    // LM_SetMemory
    // LM_SetMemoryEx
    // LM_ProtMemory
    // LM_ProtMemoryEx
    // LM_AllocMemory
    // LM_AllocMemoryEx
    // LM_FreeMemory
    // LM_FreeMemoryEx

    // LM_DataScan
    // LM_DataScanEx
    // LM_PatternScan
    // LM_PatternScanEx
    // LM_SigScan
    // LM_SigScanEx

    // LM_HookCode
    // LM_HookCodeEx
    // LM_UnhookCode
    // LM_UnhookCodeEx

    // LM_Assemble
    // LM_AssembleEx
    // LM_FreeCodeBuffer
    // LM_Disassemble
    // LM_DisassembleEx
    // LM_FreeInstructions
    // LM_CodeLength
    // LM_CodeLengthEx

    // LM_VmtNew
    // LM_VmtHook
    // LM_VmtUnhook
    // LM_VmtGetOriginal
    // LM_VmtReset
    // LM_VmtFree

}
