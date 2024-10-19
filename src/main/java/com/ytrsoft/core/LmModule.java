package com.ytrsoft.core;

import com.ytrsoft.ui.table.Column;
import com.ytrsoft.ui.table.Formatter;
import com.ytrsoft.utils.HexTransform;
import com.ytrsoft.utils.MemSizeTransform;

public class LmModule {
    @Column(value = "基址", center = true)
    @Formatter(HexTransform.class)
    private long base;
    @Formatter(HexTransform.class)
    @Column(value = "地址", center = true)
    private long end;
    @Column(value = "大小", center = true)
    @Formatter(MemSizeTransform.class)
    private long size;
    @Column(value = "路径", width = 200)
    private String path;
    @Column(value = "名称", width = 200)
    private String name;

    public LmModule() {}

    public LmModule(Libmem.LmModule m) {
        this.base = m.base;
        this.end = m.end;
        this.size = m.size;
        this.name = NativeString.load(m.name);
        this.path = NativeString.load(m.path);
    }

    public Libmem.LmModule toRef() {
        Libmem.LmModule m = new Libmem.LmModule();
        m.base = this.base;
        m.end = this.end;
        m.size = this.size;
        m.name = NativeString.toByteArray(this.name, Libmem.LM_PATH_MAX);
        m.path = NativeString.toByteArray(this.path, Libmem.LM_PATH_MAX);
        return m;
    }

    public long getBase() {
        return base;
    }

    public void setBase(long base) {
        this.base = base;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "LmModule{" +
                "base=" + base +
                ", end=" + end +
                ", size=" + size +
                ", path='" + path + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
