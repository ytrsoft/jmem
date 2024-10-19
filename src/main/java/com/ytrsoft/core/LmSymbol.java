package com.ytrsoft.core;

import com.ytrsoft.ui.table.Column;
import com.ytrsoft.ui.table.Formatter;
import com.ytrsoft.utils.HexTransform;

public class LmSymbol {
    @Column("名称")
    public String name;
    @Formatter(HexTransform.class)
    @Column(value = "地址", center = true)
    public long address;

    public LmSymbol() {}

    public LmSymbol(Libmem.LmSymbol s) {
        this.name = s.name;
        this.address = s.address;
    }

    public Libmem.LmSymbol toRef() {
        Libmem.LmSymbol s = new Libmem.LmSymbol();
        s.name = this.name;
        s.address = this.address;
        return s;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAddress() {
        return address;
    }

    public void setAddress(long address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "LmSymbol{" +
                "name='" + name + '\'' +
                ", address=" + address +
                '}';
    }
}
