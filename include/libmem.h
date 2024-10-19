/*
 *  ----------------------------------
 * |         libmem - by rdbo         |
 * |         内存劫持库                 |
 *  ----------------------------------
 */

/*
 * 版权所有 (C) 2023 Rdbo
 * 本程序是自由软件：您可以依据 GNU Affero General Public License 第 3 版的条款重新分发或修改该程序，
 * 该条款由自由软件基金会发布。
 *
 * 本程序的分发目的是期望它能有所帮助，
 * 但不提供任何形式的担保，甚至不包括适销性或特定用途适用性的暗示性担保。
 * 有关更多细节，请参阅 GNU Affero General Public License。
 *
 * 您应该已经收到本程序的 GNU Affero General Public License 副本。
 * 如果没有，请查看 <https://www.gnu.org/licenses/>。
 */

#ifndef LIBMEM_H
#define LIBMEM_H

/* 函数导出前缀 */
#ifdef _MSC_VER
	/* MSVC */
#	define LM_API_EXPORT __declspec(dllexport)
#else
	/* GCC/Clang */
#	define LM_API_EXPORT __attribute__((visibility("default")))
#endif

/* 函数导入前缀 */
#ifdef _MSC_VER
#	define LM_API_IMPORT __declspec(dllimport)
#else
#	define LM_API_IMPORT extern
#endif

/* 解析导入/导出 */
#ifdef LM_EXPORT
#	define LM_API LM_API_EXPORT
#else
#	define LM_API LM_API_IMPORT
#endif

/* 调用约定 */
#define LM_CALL

/* 常量 */
#define LM_NULL    (0)
#define LM_NULLPTR ((void *)LM_NULL)

#define LM_PID_BAD ((lm_pid_t)-1) /* PID 0 是有效的，因此不能在此使用。-1 可能有效，但不太可能 */
#define LM_TID_BAD ((lm_tid_t)-1)
#define LM_ADDRESS_BAD ((lm_address_t)-1) /* 0 和 -1 都是不错的选择 */

#define LM_PATH_MAX (4096) /* 最多可容纳 1024 个 4 字节的 UTF-8 字符 */
#define LM_INST_MAX (16) /* 单条指令的最大大小 */

/* 路径分隔符 */
#ifdef _WIN32
#	define LM_PATHSEP '\\'
#else
#	define LM_PATHSEP '/'
#endif

/* 辅助宏 */
#define LM_ARRLEN(arr) (sizeof(arr) / sizeof(arr[0]))
#define LM_CHECK_PROT(prot) ((prot & LM_PROT_XRW) == prot)

#ifdef __cplusplus
extern "C" {
#endif

#include <stdlib.h>
#include <stdint.h>

/* 基本类型 */
typedef void     lm_void_t;
typedef enum {
	LM_FALSE = 0,
	LM_TRUE = 1
}  lm_bool_t;
typedef uint8_t   lm_byte_t;
typedef uintptr_t lm_address_t;
typedef size_t    lm_size_t;

/* 字符串类型 */
typedef char             lm_char_t; /* UTF-8 编码的字符 */
typedef const lm_char_t *lm_string_t;
typedef const lm_byte_t *lm_bytearray_t;

/* 操作系统基本类型 */
typedef uint32_t lm_pid_t;
typedef uint32_t lm_tid_t;
typedef uint64_t lm_time_t;

/*
 * 内存保护标志
 *
 * lm_prot_t 位掩码：
 *
 * 31 30 29 ... 2 1 0
 * 0  0  0      0 0 0
 *              W R X
 */
enum {
	LM_PROT_NONE = 0,

	LM_PROT_R = (1 << 0),
	LM_PROT_W = (1 << 1),
	LM_PROT_X = (1 << 2),

	LM_PROT_XR = LM_PROT_X | LM_PROT_R,
	LM_PROT_XW = LM_PROT_X | LM_PROT_W,
	LM_PROT_RW = LM_PROT_R | LM_PROT_W,
	LM_PROT_XRW = LM_PROT_X | LM_PROT_R | LM_PROT_W
};
typedef uint32_t lm_prot_t;

/* 支持的汇编/反汇编架构 */
/*
 *  注意：此处列出的架构为汇编器（keystone）和反汇编器（capstone）都支持的架构，
 *       但不一定完全支持 libmem。
 */
enum {
	/* ARM */
	LM_ARCH_ARMV7 = 0, /* ARMv7 */
	LM_ARCH_ARMV8,     /* ARMv8 */
	LM_ARCH_THUMBV7,   /* ARMv7, thumb 模式 */
	LM_ARCH_THUMBV8,   /* ARMv8, thumb 模式 */

	LM_ARCH_ARMV7EB,   /* ARMv7, 大端模式 */
	LM_ARCH_THUMBV7EB, /* ARMv7, 大端模式, thumb 模式 */
	LM_ARCH_ARMV8EB,   /* ARMv8, 大端模式 */
	LM_ARCH_THUMBV8EB, /* ARMv8, 大端模式, thumb 模式 */

	LM_ARCH_AARCH64,   /* ARM64/AArch64 */

	/* MIPS */
	LM_ARCH_MIPS,     /* Mips32 */
	LM_ARCH_MIPS64,   /* Mips64 */
	LM_ARCH_MIPSEL,   /* Mips32, 小端模式 */
	LM_ARCH_MIPSEL64, /* Mips64, 小端模式 */

	/* X86 */
	LM_ARCH_X86_16, /* x86_16 */
	LM_ARCH_X86,    /* x86_32 */
	LM_ARCH_X64,    /* x86_64 */

	/* PowerPC */
	LM_ARCH_PPC32,   /* PowerPC 32 */
	LM_ARCH_PPC64,   /* PowerPC 64 */
	LM_ARCH_PPC64LE, /* PowerPC 64, 小端模式 */

	/* SPARC */
	LM_ARCH_SPARC,   /* Sparc */
	LM_ARCH_SPARC64, /* Sparc64 */
	LM_ARCH_SPARCEL, /* Sparc, 小端模式 */

	/* SystemZ */
	LM_ARCH_SYSZ, /* S390X */

	LM_ARCH_MAX,
};
typedef uint32_t lm_arch_t;

typedef struct lm_process_t {
	lm_pid_t  pid;
	lm_pid_t  ppid;
	lm_arch_t arch;
	lm_size_t bits;
	lm_time_t start_time; /* 进程启动时间戳，自上次启动以来的毫秒数 */
	lm_char_t path[LM_PATH_MAX];
	lm_char_t name[LM_PATH_MAX];
} lm_process_t;

typedef struct lm_thread_t {
	lm_tid_t tid;
	lm_pid_t owner_pid;
} lm_thread_t;

typedef struct lm_module_t {
	lm_address_t base;
	lm_address_t end;
	lm_size_t    size;
	lm_char_t    path[LM_PATH_MAX];
	lm_char_t    name[LM_PATH_MAX];
} lm_module_t;

/* 一个已分配的内存段 */
typedef struct lm_segment_t {
	lm_address_t base;
	lm_address_t end;
	lm_size_t    size;
	lm_prot_t    prot;
} lm_segment_t;

typedef struct lm_symbol_t {
	lm_string_t  name;
	lm_address_t address;
} lm_symbol_t;

typedef struct lm_inst_t {
	lm_address_t address;
	lm_size_t    size;
	lm_byte_t    bytes[LM_INST_MAX];
	lm_char_t    mnemonic[32];
	lm_char_t    op_str[160];
} lm_inst_t;

/* 虚方法表（VMT） */
typedef struct lm_vmt_entry_t {
	lm_address_t           orig_func;
	lm_size_t              index;
	struct lm_vmt_entry_t *next;
} lm_vmt_entry_t;

typedef struct lm_vmt_t {
	lm_address_t   *vtable;
	lm_vmt_entry_t *hkentries;
} lm_vmt_t;

/*
 * API 使用指南
 *
 * 1. 用户界面函数在接收到错误参数时应返回。
 *
 * 2. 内部函数应使用 'assert' 来检查错误参数。
 *
 * 3. 不可变的结构指针应为 'const'。
 *
 * 4. 不可变的字符串应为 'lm_string_t'。
 *
 * 5. 函数中的输出变量指针应位于末尾，除非放在那不合适
 *    （例如，当有一个尺寸参数位于可变缓冲区之后时）。
 *
 * 6. 所有用户界面函数应有 'LM_API' 前缀以实现自动导入和导出。
 *
 * 7. 所有用户界面可调用项应有 'LM_CALL' 中缀以确保调用约定一致。
 *    包括回调。
 *
 * 8. 运行回调的函数应有 'lm_void_t *arg' 参数，以便调用者可以传递值
 *    到回调中，而无需使用全局变量或变通方法。
 *
 * 9. 所有用户界面函数的名称前应有 'LM_' 前缀，之后使用 PascalCase。
 *
 * 10. 所有用户界面类型的名称应有 'lm_' 前缀，之后使用蛇形命名法，
 *     并以 '_t' 结尾。
 *
 * 11. 函数中的专有输出参数应有 '_out' 后缀。
 */

/* 进程 API */

/**
 * 枚举系统中的进程，并为每个找到的进程调用回调函数。
 *
 * @param callback 回调函数，该函数会接收枚举中当前的进程和额外参数。
 * 返回 `LM_TRUE` 继续枚举，返回 `LM_FALSE` 停止。
 * @param arg 用户自定义的数据结构，将与 `lm_process_t` 结构一起传递给回调函数。
 *
 * @return 成功返回 `LM_TRUE`，失败返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_EnumProcesses(lm_bool_t (LM_CALL *callback)(lm_process_t *process,
					       lm_void_t    *arg),
		 lm_void_t          *arg);

/**
 * 获取当前进程的信息，包括其 PID、父 PID、路径、名称、启动时间和架构位。
 *
 * @param process_out 指向 `lm_process_t` 结构的指针，将填充有关当前进程的信息。
 *
 * @return 成功检索到进程信息返回 `LM_TRUE`，否则返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_GetProcess(lm_process_t *process_out);

/**
 * 根据进程 ID 获取指定进程的信息。
 *
 * @param pid 想要获取信息的进程 ID。
 * @param process_out 指向 `lm_process_t` 结构的指针，将填充指定进程的信息。
 *
 * @return 成功检索到进程信息返回 `LM_TRUE`，否则返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_GetProcessEx(lm_pid_t      pid,
		lm_process_t *process_out);

/**
 * 根据名称搜索进程，并返回是否找到该进程。
 *
 * @param process_name 要查找的进程名称（例如 `game.exe`）。
 * 它也可以是相对路径，如 `/game/hello` 对于位于 `/usr/share/game/hello` 的进程。
 * @param process_out 指向 `lm_process_t` 结构的指针，将填充找到的进程的信息。
 *
 * @return 成功找到指定名称的进程返回 `LM_TRUE`，否则返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_FindProcess(lm_string_t   process_name,
	       lm_process_t *process_out);

/**
 * 根据进程的 PID 和启动时间判断进程是否在运行。
 *
 * @param process 要检查的进程。
 *
 * @return 进程还在运行返回 `LM_TRUE`，否则返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_IsProcessAlive(const lm_process_t *process);

/**
 * 返回指针的位大小（32 位或 64 位），对应于当前进程的位。
 *
 * @return 返回指针的位大小。
 */
LM_API lm_size_t LM_CALL
LM_GetBits();

/**
 * 返回系统架构的位大小（32 位或 64 位）。
 *
 * @return 返回系统的位大小。
 */
LM_API lm_size_t LM_CALL
LM_GetSystemBits();

/* 线程 API */

/**
 * 枚举当前进程中的线程，并为每个找到的线程调用回调函数。
 *
 * @param callback 回调函数，该函数会接收枚举中当前的线程和额外参数。
 * 返回 `LM_TRUE` 继续枚举，返回 `LM_FALSE` 停止。
 * @param arg 用户自定义的数据结构，将在线程枚举期间传递给回调函数 `callback`。
 *
 * @return 成功返回 `LM_TRUE`，失败返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_EnumThreads(lm_bool_t (LM_CALL *callback)(lm_thread_t *thread,
					     lm_void_t   *arg),
	       lm_void_t          *arg);

/**
 * 枚举给定进程的线程，并为每个找到的线程调用回调函数。
 *
 * @param process 要枚举其线程的进程。
 * @param callback 回调函数，该函数会接收枚举中当前的线程和额外参数。
 * 返回 `LM_TRUE` 继续枚举，返回 `LM_FALSE` 停止。
 * @param arg 用户自定义的数据，可以传递给回调函数，便于提供附加信息或上下文。
 *
 * @return 成功返回 `LM_TRUE`，失败返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_EnumThreadsEx(const lm_process_t *process,
		 lm_bool_t (LM_CALL *callback)(lm_thread_t *thread,
					       lm_void_t   *arg),
		 lm_void_t          *arg);

/**
 * 获取当前运行的线程信息。
 *
 * @param thread_out 指向 `lm_thread_t` 结构的指针，将填充当前线程的信息。
 *
 * @return 成功获取线程信息返回 `LM_TRUE`，否则返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_GetThread(lm_thread_t *thread_out);

/**
 * 获取给定进程中指定线程的信息。
 *
 * @param process 要获取其线程信息的进程。
 * @param thread_out 指向 `lm_thread_t` 变量的指针，将填充从进程中检索到的线程信息。
 *
 * @return 成功检索线程返回 `LM_TRUE`，否则返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_GetThreadEx(const lm_process_t *process,
	       lm_thread_t        *thread_out);

/**
 * 检索给定线程所属的进程。
 *
 * @param thread 要获取其进程信息的线程。
 * @param process_out 指向 `lm_process_t` 结构的指针，将存储与给定线程相关的进程信息。
 *
 * @return 成功返回 `LM_TRUE`，失败返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_GetThreadProcess(const lm_thread_t *thread,
		    lm_process_t      *process_out);

/* 模块 API */

/**
 * 枚举当前进程中的模块，并为每个找到的模块调用回调函数。
 *
 * @param callback 回调函数，将接收当前枚举的模块和额外参数。
 * 返回 `LM_TRUE` 继续枚举，返回 `LM_FALSE` 停止。
 * @param arg 传递给回调函数的额外参数，允许提供附加信息或上下文。
 *
 * @return 成功返回 `LM_TRUE`，失败返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_EnumModules(lm_bool_t (LM_CALL *callback)(lm_module_t *module,
					     lm_void_t   *arg),
	       lm_void_t          *arg);

/**
 * 枚举指定进程中的模块，并为每个找到的模块调用回调函数。
 *
 * @param process 要枚举其模块的进程。
 * @param callback 回调函数，将接收当前枚举的模块和额外参数。
 * 返回 `LM_TRUE` 继续枚举，返回 `LM_FALSE` 停止。
 * @param arg 传递给回调函数的额外参数，允许提供附加信息或上下文。
 *
 * @return 成功返回 `LM_TRUE`，失败返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_EnumModulesEx(const lm_process_t *process,
		 lm_bool_t (LM_CALL *callback)(lm_module_t *module,
					       lm_void_t   *arg),
		 lm_void_t          *arg);

/**
 * 根据名称查找模块，并将找到的模块信息填充到 `module_out` 参数中。
 *
 * @param name 要查找的模块名称（例如 `game.dll`），也可以是相对路径。
 * @param module_out 指向 `lm_module_t` 结构的指针，该函数将填充有关找到的模块的信息。
 *
 * @return 成功找到模块返回 `LM_TRUE`，否则返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_FindModule(lm_string_t  name,
	      lm_module_t *module_out);

/**
 * 在指定进程中根据名称查找模块，并将找到的模块信息填充到 `module_out` 参数中。
 *
 * @param process 要查找其模块的进程。
 * @param name 要查找的模块名称（例如 `game.dll`），也可以是相对路径。
 * @param module_out 指向 `lm_module_t` 结构的指针，该函数将填充有关找到的模块的信息。
 *
 * @return 成功找到模块返回 `LM_TRUE`，否则返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_FindModuleEx(const lm_process_t *process,
		lm_string_t         name,
		lm_module_t        *module_out);

/**
 * 从指定路径加载模块到当前进程。
 *
 * @param path 要加载的模块的路径。
 * @param module_out 指向 `lm_module_t` 类型的指针，用于存储已加载模块的信息（可选）。
 *
 * @return 成功加载模块返回 `LM_TRUE`，否则返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_LoadModule(lm_string_t  path,
	      lm_module_t *module_out);

/**
 * 从指定路径加载模块到指定进程。
 *
 * @param process 要加载模块的进程。
 * @param path 要加载的模块的路径。
 * @param module_out 指向 `lm_module_t` 类型的指针，用于存储已加载模块的信息（可选）。
 *
 * @return 成功加载模块返回 `LM_TRUE`，否则返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_LoadModuleEx(const lm_process_t *process,
		lm_string_t         path,
		lm_module_t        *module_out);

/**
 * 从当前进程中卸载模块。
 *
 * @param module 要卸载的模块。
 *
 * @return 成功卸载模块返回 `LM_TRUE`，否则返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_UnloadModule(const lm_module_t *module);

/**
 * 从指定进程中卸载模块。
 *
 * @param process 要卸载其模块的进程。
 * @param module 要卸载的模块。
 *
 * @return 成功卸载模块返回 `LM_TRUE`，否则返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_UnloadModuleEx(const lm_process_t *process,
		  const lm_module_t  *module);

/* 符号 API */

/**
 * 枚举模块中的符号，并为每个找到的符号调用回调函数。
 *
 * @param module 要枚举符号的模块。
 * @param callback 将接收每个符号和额外参数的函数指针。
 * 回调函数返回 `LM_TRUE` 继续枚举，返回 `LM_FALSE` 停止。
 * @param arg 用户定义的数据指针，可以传递给回调函数，便于提供附加信息或上下文。
 *
 * @return 成功返回 `LM_TRUE`，失败返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_EnumSymbols(const lm_module_t  *module,
	       lm_bool_t (LM_CALL *callback)(lm_symbol_t *symbol,
					     lm_void_t   *arg),
	       lm_void_t          *arg);

/**
 * 查找模块中的符号地址。
 *
 * @param module 要查找符号的模块。
 * @param symbol_name 要查找的符号名称。
 *
 * @return 成功返回符号地址，失败返回 `LM_ADDRESS_BAD`。
 */
LM_API lm_address_t LM_CALL
LM_FindSymbolAddress(const lm_module_t *module,
		     lm_string_t        symbol_name);

/**
 * 解码符号名称。
 *
 * @param symbol_name 要解码的符号名称。
 * @param demangled_buf 解码符号名称将存储在此缓冲区中。如果该值为 `NULL`，符号将被动态分配，忽略 `maxsize`。
 * @param maxsize 解码符号名称的缓冲区最大大小。
 *
 * @return 成功返回解码后的符号字符串指针，失败返回 `NULL`。
 */
LM_API lm_char_t * LM_CALL
LM_DemangleSymbol(lm_string_t symbol_name,
		  lm_char_t  *demangled_buf,
		  lm_size_t   maxsize);

/**
 * 释放为解码符号名称分配的内存。
 *
 * @param symbol_name 要释放的解码符号名称。
 */
LM_API lm_void_t LM_CALL
LM_FreeDemangledSymbol(lm_char_t *symbol_name);

/**
 * 枚举模块中带有解码名称的符号，并为每个找到的符号调用回调函数。
 *
 * @param module 要枚举符号的模块。
 * @param callback 将接收每个解码符号和额外参数的函数指针。回调函数返回 `LM_TRUE` 继续枚举，返回 `LM_FALSE` 停止。
 * @param arg 用户定义的数据指针，可以传递给回调函数，以提供附加信息或上下文。
 *
 * @return 成功返回 `LM_TRUE`，失败返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_EnumSymbolsDemangled(const lm_module_t  *module,
			lm_bool_t (LM_CALL *callback)(lm_symbol_t *symbol,
						      lm_void_t   *arg),
			lm_void_t          *arg);

/**
 * 查找模块中解码符号的地址。
 *
 * @param module 要查找符号的模块。
 * @param symbol_name 要查找的符号名称。
 *
 * @return 成功返回符号地址，失败返回 `LM_ADDRESS_BAD`。
 */
LM_API lm_address_t LM_CALL
LM_FindSymbolAddressDemangled(const lm_module_t *module,
			      lm_string_t        symbol_name);

/* 内存段 API */

/**
 * 枚举当前进程的内存段，并为每个找到的段调用回调函数。
 *
 * @param callback 接收每个段和额外参数的函数指针。回调函数返回 `LM_TRUE` 继续枚举，返回 `LM_FALSE` 停止。
 * @param arg 用户定义的数据指针，可传递给回调函数，用于提供附加信息或上下文。
 *
 * @return 成功返回 `LM_TRUE`，失败返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_EnumSegments(lm_bool_t (LM_CALL *callback)(lm_segment_t *segment,
                			      lm_void_t    *arg),
		lm_void_t          *arg);

/**
 * 枚举给定进程的内存段，并为每个找到的段调用回调函数。
 *
 * @param process 包含有关要枚举其内存段的进程信息的结构。
 * @param callback 接收每个段和额外参数的函数指针。回调函数返回 `LM_TRUE` 继续枚举，返回 `LM_FALSE` 停止。
 * @param arg 用户定义的数据指针，可传递给回调函数，用于提供附加信息或上下文。
 *
 * @return 成功返回 `LM_TRUE`，失败返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_EnumSegmentsEx(const lm_process_t *process,
		  lm_bool_t (LM_CALL *callback)(lm_segment_t *segment,
						lm_void_t    *arg),
		  lm_void_t          *arg);

/**
 * 查找包含指定地址的内存段，并将结果填充到 `segment_out` 参数中。
 *
 * @param address 要查找的地址。
 * @param segment_out 指向 `lm_segment_t` 结构的指针，将填充包含指定地址的段的信息。
 *
 * @return 成功找到指定地址返回 `LM_TRUE`，否则返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_FindSegment(lm_address_t  address,
	       lm_segment_t *segment_out);

/**
 * 查找指定进程中包含指定地址的内存段，并将结果填充到 `segment_out` 参数中。
 *
 * @param process 包含有关要查找的进程的结构。
 * @param address 要查找的地址。
 * @param segment_out 指向 `lm_segment_t` 结构的指针，将填充包含指定地址的段的信息。
 *
 * @return 成功找到指定地址返回 `LM_TRUE`，否则返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_FindSegmentEx(const lm_process_t *process,
		 lm_address_t        address,
		 lm_segment_t       *segment_out);

/* 内存 API */

/*
 * 注意：内存分配/保护/释放函数是页面对齐的
 *
 * 注意：在 LM_ProtMemory(Ex) 中，`oldprot_out` 参数包含整个区域的第一页的旧保护，
 *       对于大多数情况，这已经足够。对于多段内存保护，应使用 LM_FindSegments(Ex)
 *       获取旧保护。
 */

/**
 * 从源地址读取内存并复制到目标地址。
 *
 * @param source 要读取数据的内存地址。
 * @param dest 要存储从源地址读取的数据的内存位置的指针。
 * @param size 从源地址开始读取并写入到 dest 缓冲区的字节数。
 *
 * @return 从内存中读取的字节数。
 */
LM_API lm_size_t LM_CALL
LM_ReadMemory(lm_address_t source,
	      lm_byte_t   *dest,
	      lm_size_t    size);

/**
 * 从指定进程中读取内存并返回读取的字节数。
 *
 * @param process 要从中读取内存的进程。
 * @param source 要读取数据的内存起始地址。
 * @param dest 要存储从源地址读取的数据的目标缓冲区的指针。
 * @param size 要从指定源地址读取的字节数。
 *
 * @return 成功返回读取的字节数，失败返回 0。
 */
LM_API lm_size_t LM_CALL
LM_ReadMemoryEx(const lm_process_t *process,
		lm_address_t        source,
		lm_byte_t          *dest,
		lm_size_t           size);

/**
 * 从源地址写入数据到目标地址。
 *
 * @param dest 数据将被写入到目标地址。
 * @param source 要写入数据的源数组的指针。
 * @param size 要从源数组写入到目标地址的字节数。
 *
 * @return 成功写入的字节数。
 */
LM_API lm_size_t LM_CALL
LM_WriteMemory(lm_address_t   dest,
	       lm_bytearray_t source,
	       lm_size_t      size);

/**
 * 从源地址写入数据到指定进程中的目标地址。
 *
 * @param process 要写入内存的进程。
 * @param dest 数据将被写入到目标地址。
 * @param source 要写入数据的源数组的指针。
 * @param size 要从源数组写入到目标地址的字节数。
 *
 * @return 成功写入的字节数，失败返回 0。
 */
LM_API lm_size_t LM_CALL
LM_WriteMemoryEx(const lm_process_t *process,
		 lm_address_t        dest,
		 lm_bytearray_t      source,
		 lm_size_t           size);

/**
 * 将指定内存区域设置为指定字节值。
 *
 * @param dest 要写入字节值的内存地址。
 * @param byte 要写入的字节值。
 * @param size 要设置的字节数。
 *
 * @return 成功设置的字节数。
 */
LM_API lm_size_t LM_CALL
LM_SetMemory(lm_address_t dest,
	     lm_byte_t    byte,
	     lm_size_t    size);

/**
 * 将指定内存区域设置为指定字节值（在目标进程中）。
 *
 * @param process 要设置内存的进程。
 * @param dest 要写入字节值的目标地址。
 * @param byte 要写入的字节值。
 * @param size 要设置的字节数。
 *
 * @return 成功设置的字节数，失败返回 0。
 */
LM_API lm_size_t LM_CALL
LM_SetMemoryEx(const lm_process_t *process,
	       lm_address_t        dest,
	       lm_byte_t           byte,
	       lm_size_t           size);


/**
 * 设置指定内存地址范围的内存保护标志。
 *
 * @param address 要保护的内存地址。
 * @param size 要保护的内存大小。如果大小为 0，函数将默认使用系统页面大小。
 * @param prot 要应用于从指定地址开始的内存区域的新保护标志。它是 `LM_PROT_X`（执行）、`LM_PROT_R`（读取）、`LM_PROT_W`（写入）的位掩码。
 * @param oldprot_out 用于存储更新前的内存段保护标志的指针。
 *
 * @return 成功返回 `LM_TRUE`，失败返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_ProtMemory(lm_address_t address,
	      lm_size_t    size,
	      lm_prot_t    prot,
	      lm_prot_t   *oldprot_out);

/**
 * 修改指定进程中的地址范围的内存保护标志。
 *
 * @param process 要修改内存标志的进程。
 * @param address 要保护的内存地址。
 * @param size 要保护的内存大小。如果大小为 0，函数将默认使用系统页面大小。
 * @param prot 要应用于从指定地址开始的内存区域的新保护标志。它是 `LM_PROT_X`（执行）、`LM_PROT_R`（读取）、`LM_PROT_W`（写入）的位掩码。
 * @param oldprot_out 用于存储更新前的内存段保护标志的指针。
 *
 * @return 成功返回 `LM_TRUE`，失败返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_ProtMemoryEx(const lm_process_t *process,
		lm_address_t        address,
		lm_size_t           size,
		lm_prot_t           prot,
		lm_prot_t          *oldprot_out);

/**
 * 分配指定大小和保护标志的内存，并返回分配的内存地址。
 *
 * @param size 要分配的内存大小。如果大小为 0，函数将分配一个完整的页面。
 * 如果指定了具体大小，则分配指定大小的内存并对齐到下一个页面大小。
 * @param prot 分配的内存区域的内存保护标志。它是 `LM_PROT_X`（执行）、`LM_PROT_R`（读取）、`LM_PROT_W`（写入）的位掩码。
 *
 * @return 成功返回分配的内存地址，失败返回 `LM_ADDRESS_BAD`。
 */
LM_API lm_address_t LM_CALL
LM_AllocMemory(lm_size_t size,
	       lm_prot_t prot);

/**
 * 在指定进程中分配内存，大小和内存保护标志由用户指定。
 *
 * @param process 要分配内存的进程。
 * @param size 要分配的内存大小。如果大小为 0，函数将分配一个完整的页面。
 * 如果指定了具体大小，则分配指定大小的内存并对齐到下一个页面大小。
 * @param prot 分配的内存区域的内存保护标志。它是 `LM_PROT_X`（执行）、`LM_PROT_R`（读取）、`LM_PROT_W`（写入）的位掩码。
 *
 * @return 成功返回分配的内存地址，失败返回 `LM_ADDRESS_BAD`。
 */
LM_API lm_address_t LM_CALL
LM_AllocMemoryEx(const lm_process_t *process,
		 lm_size_t           size,
		 lm_prot_t           prot);

/**
 * 释放之前使用 `LM_AllocMemory` 分配的内存。
 *
 * @param alloc 之前分配的内存块的地址。
 * @param size 之前分配的内存块大小。如果大小为 0，函数将使用系统页面大小。
 *
 * @return 成功返回 `LM_TRUE`，失败返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_FreeMemory(lm_address_t alloc,
	      lm_size_t    size);

/**
 * 释放指定进程中之前使用 `LM_AllocMemoryEx` 分配的内存。
 *
 * @param process 要释放内存的进程。
 * @param alloc 之前分配的内存块地址。
 * @param size 之前分配的内存块大小。如果大小为 0，函数将使用系统页面大小。
 *
 * @return 成功返回 `LM_TRUE`，失败返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_FreeMemoryEx(const lm_process_t *process,
		lm_address_t        alloc,
		lm_size_t           size);

/**
 * 通过应用一系列偏移量计算深层指针地址。
 *
 * @param base 计算深层指针的起始地址。
 * @param offsets 用于导航内存地址的偏移量数组。
 * @param noffsets `offsets` 数组中的偏移量数量。
 *
 * @return 成功返回计算的深层指针地址，失败返回 `LM_ADDRESS_BAD`。
 */
LM_API lm_address_t LM_CALL
LM_DeepPointer(lm_address_t        base,
	       const lm_address_t *offsets,
	       size_t              noffsets);

/**
 * 通过应用一系列偏移量计算深层指针地址（指定进程内存空间）。
 *
 * @param process 要从中计算深层指针的进程。
 * @param base 计算深层指针的起始地址。
 * @param offsets 用于导航内存地址的偏移量数组。
 * @param noffsets `offsets` 数组中的偏移量数量。
 *
 * @return 成功返回计算的深层指针地址，失败返回 `LM_ADDRESS_BAD`。
 */
LM_API lm_address_t LM_CALL
LM_DeepPointerEx(const lm_process_t *process,
		 lm_address_t        base,
		 const lm_address_t *offsets,
		 lm_size_t           noffsets);

/* 扫描 API */

/**
 * 扫描指定内存地址范围内的特定数据模式，并返回匹配数据的地址。
 *
 * @param data 要在内存中扫描的数据。
 * @param datasize 数据数组的大小。表示需要连续匹配的字节数。
 * @param address 扫描操作的起始内存地址。
 * @param scansize 扫描区域的大小，从指定 `address` 开始。
 *
 * @return 成功返回匹配的数据地址，失败返回 `LM_ADDRESS_BAD`。
 */
LM_API lm_address_t LM_CALL
LM_DataScan(lm_bytearray_t data,
	    lm_size_t      datasize,
	    lm_address_t   address,
	    lm_size_t      scansize);

/**
 * 在指定进程的内存中扫描特定数据模式，并返回匹配数据的地址。
 *
 * @param process 要扫描其内存的进程。
 * @param data 要在内存中扫描的数据。
 * @param datasize 数据数组的大小。表示需要连续匹配的字节数。
 * @param address 扫描操作的起始内存地址。
 * @param scansize 扫描区域的大小，从指定 `address` 开始。
 *
 * @return 成功返回匹配的数据地址，失败返回 `LM_ADDRESS_BAD`。
 */
LM_API lm_address_t LM_CALL
LM_DataScanEx(const lm_process_t *process,
	      lm_bytearray_t      data,
	      lm_size_t           datasize,
	      lm_address_t        address,
	      lm_size_t           scansize);

/**
 * 根据指定的掩码在内存中查找特定模式。
 *
 * @param pattern 要在内存中查找的模式。
 * @param mask 用于扫描内存的模式掩码。用于指定模式中的哪些字节应与内存内容匹配。掩码可包含字符 `?` 作为通配符，允许匹配任何字节，也可使用 `x` 进行精确匹配。
 * @param address 扫描操作的起始内存地址。
 * @param scansize 扫描区域的大小，从指定 `address` 开始。
 *
 * @return 成功返回匹配的内存地址，失败返回 `LM_ADDRESS_BAD`。
 */
LM_API lm_address_t LM_CALL
LM_PatternScan(lm_bytearray_t pattern,
	       lm_string_t    mask,
	       lm_address_t   address,
	       lm_size_t      scansize);

/**
 * 在指定进程的内存中，根据掩码查找特定模式。
 *
 * @param process 要扫描其内存的进程。
 * @param pattern 要在内存中查找的模式。
 * @param mask 用于扫描内存的模式掩码。用于指定模式中的哪些字节应与内存内容匹配。掩码可包含字符 `?` 作为通配符，允许匹配任何字节，也可使用 `x` 进行精确匹配。
 * @param address 扫描操作的起始内存地址。
 * @param scansize 扫描区域的大小，从指定 `address` 开始。
 *
 * @return 成功返回匹配的内存地址，失败返回 `LM_ADDRESS_BAD`。
 */
LM_API lm_address_t LM_CALL
LM_PatternScanEx(const lm_process_t *process,
		 lm_bytearray_t      pattern,
		 lm_string_t         mask,
		 lm_address_t        address,
		 lm_size_t           scansize);

/**
 * 从指定地址开始，扫描内存中的特定签名模式。
 *
 * @param signature 要在内存中扫描的签名。用于标识内存中的特定字节模式。可以使用 `??` 匹配任意字节，或使用字节的十六进制值。例如："DE AD BE EF ?? ?? 13 37"。
 * @param address 扫描操作的起始内存地址。
 * @param scansize 扫描区域的大小，从指定 `address` 开始。
 *
 * @return 成功返回匹配的内存地址，失败返回 `LM_ADDRESS_BAD`。
 */
LM_API lm_address_t LM_CALL
LM_SigScan(lm_string_t  signature,
	   lm_address_t address,
	   lm_size_t    scansize);

/**
 * 在指定进程的内存中，从指定地址开始扫描特定签名模式。
 *
 * @param process 要扫描其内存的进程。
 * @param signature 要在内存中扫描的签名。用于标识内存中的特定字节模式。可以使用 `??` 匹配任意字节，或使用字节的十六进制值。例如："DE AD BE EF ?? ?? 13 37"。
 * @param address 扫描操作的起始内存地址。
 * @param scansize 扫描区域的大小，从指定 `address` 开始。
 *
 * @return 成功返回匹配的内存地址，失败返回 `LM_ADDRESS_BAD`。
 */
LM_API lm_address_t LM_CALL
LM_SigScanEx(const lm_process_t *process,
	     lm_string_t         signature,
	     lm_address_t        address,
	     lm_size_t           scansize);

/* 汇编/反汇编 API */

/**
 * 返回当前系统架构。
 *
 * @return 成功返回系统架构。可能的值包括：
 * - `LM_ARCH_X86` 表示 32 位 x86。
 * - `LM_ARCH_AMD64` 表示 64 位 x86。
 * - 其他值（参考 `lm_arch_t` 枚举）。
 */
LM_API lm_arch_t LM_CALL
LM_GetArchitecture();

/**
 * 将单条指令汇编为机器码。
 *
 * @param code 要汇编的指令。例如："mov eax, ebx"。
 * @param instruction_out 汇编后的指令填充到此参数中。
 *
 * @return 成功返回 `LM_TRUE`，填充 `instruction_out` 参数；失败返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_Assemble(lm_string_t code,
	     lm_inst_t  *instruction_out);

/**
 * 将指令汇编为机器码。
 *
 * @param code 要汇编的指令。例如："mov eax, ebx ; jmp eax"。
 * @param arch 要汇编的架构。
 * @param runtime_address 用于解决寻址的运行时地址（例如，相对跳转将使用此地址解决）。
 * @param payload_out 用于接收汇编后的指令的缓冲区指针。使用 `LM_FreePayload` 释放。
 *
 * @return 成功返回汇编指令的字节数；失败返回 0。
 */
LM_API lm_size_t LM_CALL
LM_AssembleEx(lm_string_t  code,
              lm_arch_t    arch,
	      lm_address_t runtime_address,
	      lm_byte_t  **payload_out);

/**
 * 释放由 `LM_AssembleEx` 分配的内存。
 *
 * @param payload 由 `LM_AssembleEx` 分配的内存。
 */
LM_API lm_void_t LM_CALL
LM_FreePayload(lm_byte_t *payload);

/**
 * 将一条指令反汇编为 `lm_inst_t` 结构。
 *
 * @param machine_code 要反汇编的指令地址。
 * @param instruction_out 反汇编后的指令填充到此参数中。
 *
 * @return 成功返回 `LM_TRUE`，失败返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_Disassemble(lm_address_t machine_code,
	       lm_inst_t   *instruction_out);

/**
 * 将指令反汇编为 `lm_inst_t` 结构数组。
 *
 * @param machine_code 要反汇编的指令地址。
 * @param arch 要反汇编的架构。
 * @param max_size 要反汇编的最大字节数（0 表示尽可能多，受 `instruction_count` 限制）。
 * @param instruction_count 要反汇编的指令数量（0 表示尽可能多，受 `max_size` 限制）。
 * @param runtime_address 用于解决寻址的运行时地址（例如，相对跳转将使用此地址解决）。
 * @param instructions_out 用于接收反汇编指令的缓冲区指针。使用 `LM_FreeInstructions` 释放。
 *
 * @return 成功返回反汇编指令数量；失败返回 0。
 */
LM_API lm_size_t LM_CALL
LM_DisassembleEx(lm_address_t machine_code,
		 lm_arch_t    arch,
		 lm_size_t    max_size,
		 lm_size_t    instruction_count,
		 lm_address_t runtime_address,
		 lm_inst_t  **instructions_out);

/**
 * 释放由 `LM_DisassembleEx` 为反汇编指令分配的内存。
 *
 * @param instructions 由 `LM_DisassembleEx` 分配的反汇编指令。
 */
LM_API lm_void_t LM_CALL
LM_FreeInstructions(lm_inst_t *instructions);

/**
 * 计算与指令长度对齐的大小，基于最小长度。
 *
 * @param machine_code 指令地址。
 * @param min_length 与指令长度对齐的最小大小。
 *
 * @return 成功返回与下一个指令长度对齐的大小，失败返回 0。
 */
LM_API lm_size_t LM_CALL
LM_CodeLength(lm_address_t machine_code,
	      lm_size_t    min_length);

/**
 * 计算与指令长度对齐的大小，基于最小长度（在远程进程中）。
 *
 * @param process 要获取对齐长度的远程进程。
 * @param machine_code 远程进程中的指令地址。
 * @param min_length 与指令长度对齐的最小大小。
 *
 * @return 成功返回与下一个指令长度对齐的大小，失败返回 0。
 */
LM_API lm_size_t LM_CALL
LM_CodeLengthEx(const lm_process_t *process,
		lm_address_t        machine_code,
		lm_size_t           min_length);

/* 挂钩 API */

/**
 * 在地址 `from` 上放置一个钩子/绕行，将其重定向到地址 `to`。
 * 可选地，在 `trampoline_out` 中生成跳板以调用原始函数。
 *
 * @param from 要放置钩子的地址。
 * @param to 钩子跳转到的地址。
 * @param trampoline_out 可选指针，用于接收一个跳板/网关以调用原始函数。
 *
 * @return 成功返回钩子占用的字节数（对齐到最近的指令）。
 */
LM_API lm_size_t LM_CALL
LM_HookCode(lm_address_t  from,
	    lm_address_t  to,
	    lm_address_t *trampoline_out);

/**
 * 在远程进程中地址 `from` 上放置一个钩子/绕行，将其重定向到地址 `to`。
 * 可选地，在 `trampoline_out` 中生成跳板以调用远程进程中的原始函数。
 *
 * @param process 要放置钩子的远程进程。
 * @param from 远程进程中放置钩子的地址。
 * @param to 远程进程中钩子跳转到的地址。
 * @param trampoline_out 可选指针，用于接收远程进程中原始函数的跳板/网关。
 *
 * @return 成功返回钩子占用的字节数（对齐到最近的指令）。
 */
LM_API lm_size_t LM_CALL
LM_HookCodeEx(const lm_process_t *process,
	      lm_address_t        from,
	      lm_address_t        to,
	      lm_address_t       *trampoline_out);

/**
 * 移除地址 `from` 上的钩子/绕行，并恢复到其原始状态。
 * 同时释放由 `LM_HookCode` 分配的跳板。
 *
 * @param from 放置钩子的地址。
 * @param trampoline 由 `LM_HookCode` 生成的跳板地址。
 * @param size 钩子占用的字节数（对齐到最近的指令）。
 *
 * @return 成功返回 `LM_TRUE`，失败返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_UnhookCode(lm_address_t from,
	      lm_address_t trampoline,
	      lm_size_t    size);

/**
 * 移除远程进程中地址 `from` 上的钩子/绕行，并恢复到其原始状态。
 * 同时释放由 `LM_HookCodeEx` 分配的跳板。
 *
 * @param process 放置钩子的远程进程。
 * @param from 远程进程中放置钩子的地址。
 * @param trampoline 由 `LM_HookCodeEx` 生成的跳板地址。
 * @param size 远程进程中钩子占用的字节数（对齐到最近的指令）。
 *
 * @return 成功返回 `LM_TRUE`，失败返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_UnhookCodeEx(const lm_process_t *process,
		lm_address_t        from,
		lm_address_t        trampoline,
		lm_size_t           size);

/* 虚方法表 API */

/**
 * 从地址 `vtable` 创建一个新的 VMT 管理器。
 *
 * @param vtable 要管理的虚方法表。
 * @param vmt_out 指向将由此函数填充的 VMT 管理器的指针。
 *
 * @return 成功返回 `LM_TRUE`，失败返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_VmtNew(lm_address_t *vtable,
	  lm_vmt_t     *vmt_out);

/**
 * 在由 `vmt` 管理的 VMT 中，将索引 `from_fn_index` 的函数钩子修改为 `to`。
 *
 * @param vmt VMT 管理器。
 * @param from_fn_index 要钩子的 VMT 函数的索引。
 * @param to 将替换原始 VMT 函数的函数。
 *
 * @return 成功返回 `LM_TRUE`，失败返回 `LM_FALSE`。
 */
LM_API lm_bool_t LM_CALL
LM_VmtHook(lm_vmt_t    *vmt,
	   lm_size_t    from_fn_index,
	   lm_address_t to);

/**
 * 在由 `vmt` 管理的 VMT 中，解除索引 `fn_index` 的函数钩子，恢复原始函数。
 *
 * @param vmt VMT 管理器。
 * @param fn_index 要解除钩子的 VMT 函数的索引。
 */
LM_API lm_bool_t LM_CALL
LM_VmtUnhook(lm_vmt_t *vmt,
	     lm_size_t fn_index);

/**
 * 返回由 `vmt` 管理的 VMT 中，索引 `fn_index` 处的原始 VMT 函数。
 * 如果函数之前未被钩子，则返回 VMT 数组中该索引处的函数指针。
 *
 * @param vmt VMT 管理器。
 * @param fn_index 要查询的 VMT 函数的索引。
 *
 * @return 成功返回索引 `fn_index` 处的原始 VMT 函数。
 */
LM_API lm_address_t LM_CALL
LM_VmtGetOriginal(const lm_vmt_t *vmt,
		  lm_size_t       fn_index);

/**
 * 重置所有 VMT 函数到其原始地址。
 *
 * @param vmt VMT 管理器。
 */
LM_API lm_void_t LM_CALL
LM_VmtReset(lm_vmt_t *vmt);

/**
 * 释放 VMT 管理器，恢复所有内容。
 *
 * @param vmt VMT 管理器。
 */
LM_API lm_void_t LM_CALL
LM_VmtFree(lm_vmt_t *vmt);

#ifdef __cplusplus
}
#endif

#endif
