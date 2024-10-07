package com.ytrsoft;

public enum Protection {
    READ(0x01),
    WRITE(0x02),
    EXECUTE(0x04),
    READ_WRITE(READ.value | WRITE.value),
    READ_EXECUTE(READ.value | EXECUTE.value),
    WRITE_EXECUTE(WRITE.value | EXECUTE.value),
    READ_WRITE_EXECUTE(READ.value | WRITE.value | EXECUTE.value);

    private final int value;

    Protection(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
