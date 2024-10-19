    package com.ytrsoft.core;

    import com.ytrsoft.ui.table.Column;
    import com.ytrsoft.ui.table.Formatter;
    import com.ytrsoft.utils.HexTransform;
    import com.ytrsoft.utils.MemSizeTransform;

    public class LmSegment {
        @Formatter(HexTransform.class)
        @Column(value = "基址", center = true)
        private long base;
        @Formatter(HexTransform.class)
        @Column(value = "地址", center = true)
        private long end;
        @Column(value = "大小", center = true)
        @Formatter(MemSizeTransform.class)
        private long size;
        @Column(value = "权限", center = true)
        private Protection prot;
    
        public LmSegment() {}
    
        public LmSegment(Libmem.LmSegment s) {
            this.base = s.base;
            this.end = s.end;
            this.size = s.size;
            this.prot = Protection.valueOf(s.prot);
        }
    
        public Libmem.LmSegment toRef() {
            Libmem.LmSegment s = new Libmem.LmSegment();
            s.base = this.base;
            s.end = this.end;
            s.size = this.size;
            s.prot = this.prot.getValue();
            return s;
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
    
        public Protection getProt() {
            return prot;
        }
    
        public void setProt(Protection prot) {
            this.prot = prot;
        }
    
        @Override
        public String toString() {
            return "LmSegment{" +
                    "base=" + base +
                    ", end=" + end +
                    ", size=" + size +
                    ", prot=" + prot +
                    '}';
        }
    }
