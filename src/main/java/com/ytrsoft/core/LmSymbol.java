package com.ytrsoft.core;

public class LmSymbol {
    public String name;
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
