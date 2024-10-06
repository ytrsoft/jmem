package com.ytrsoft;

import java.util.List;

public class MainTest {
    public static void main(String[] args) {
        List<LmProcess> processes = LibmemUtils.getProcesses();
        processes.forEach(System.out::println);
    }
}
