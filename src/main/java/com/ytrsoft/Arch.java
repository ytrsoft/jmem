package com.ytrsoft;

public enum Arch {
    NONE(-1),

    ARMV7(0),
    ARMV8(1),
    THUMBV7(2),
    THUMBV8(3),
    ARMV7EB(4),
    THUMBV7EB(5),
    ARMV8EB(6),
    THUMBV8EB(7),
    AARCH64(8),

    MIPS(9),
    MIPS64(10),
    MIPSEL(11),
    MIPSEL64(12),

    X86_16(13),
    X86(14),
    X64(15),

    PPC32(16),
    PPC64(17),
    PPC64LE(18),

    SPARC(19),
    SPARC64(20),
    SPARCEL(21),

    SYSZ(22);

    private final int value;

    Arch(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Arch valueOf(int value) {
        Arch[] values = Arch.values();
        for(Arch v: values) {
            if (v.getValue() == value) {
                return v;
            }
        }
        return Arch.NONE;
    }
}
