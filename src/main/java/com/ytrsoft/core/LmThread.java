package com.ytrsoft.core;

import com.ytrsoft.ui.table.Column;

public class LmThread {

    @Column(value = "ID", center = true)
    private int id;
    @Column(value = "PID", center = true)
    private int pid;

    public LmThread() {}

    public LmThread(Libmem.LmThread t) {
        this.id = t.tid;
        this.pid = t.owner_pid;
    }

    public Libmem.LmThread toRef() {
        Libmem.LmThread t = new Libmem.LmThread();
        t.tid = this.id;
        t.owner_pid = this.pid;
        return t;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "LmThread{" +
                "id=" + id +
                ", pid=" + pid +
                '}';
    }
}
