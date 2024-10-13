    package com.ytrsoft.core;
    
    public class LmSegment {
        private long base;
        private long end;
        private long size;
        private int prot;
    
        public LmSegment() {}
    
        public LmSegment(Libmem.LmSegment s) {
            this.base = s.base;
            this.end = s.end;
            this.size = s.size;
            this.prot = s.prot;
        }
    
        public Libmem.LmSegment toRef() {
            Libmem.LmSegment s = new Libmem.LmSegment();
            s.base = this.base;
            s.end = this.end;
            s.size = this.size;
            s.prot = this.prot;
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
    
        public int getProt() {
            return prot;
        }
    
        public void setProt(int prot) {
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
