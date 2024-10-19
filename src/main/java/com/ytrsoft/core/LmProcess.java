    package com.ytrsoft.core;

    import com.ytrsoft.ui.table.Column;
    import com.ytrsoft.ui.table.Formatter;
    import com.ytrsoft.utils.TimeTransform;

    public class LmProcess {
        @Column(value = "ID", center = true)
        private int id;
        @Column(value = "PID", center = true)
        private int pid;
        @Column(value = "架构", center = true)
        private Arch arch;
        @Column(value = "位数", center = true)
        private long bits;
        @Column(value = "启动时间", width = 120)
        @Formatter(TimeTransform.class)
        private long startTime;
        @Column(value = "路径", width = 200)
        private String path;
        @Column(value = "名称", width = 200)
        private String name;

        public LmProcess() {}

        public LmProcess(Libmem.LmProcess p) {
            this.id = p.pid;
            this.pid = p.ppid;
            this.arch = Arch.valueOf(p.arch);
            this.bits = p.bits;
            this.startTime = p.start_time;
            this.name = NativeString.load(p.name);
            this.path = NativeString.load(p.path);
        }

        public Libmem.LmProcess toRef() {
            Libmem.LmProcess p = new Libmem.LmProcess();
            p.pid = this.id;
            p.ppid = this.pid;
            if (this.arch != null) {
                p.arch = this.arch.getValue();
            }
            p.bits = this.bits;
            p.start_time = this.startTime;
            p.name = NativeString.toByteArray(this.name, Libmem.LM_PATH_MAX);
            p.path = NativeString.toByteArray(this.path, Libmem.LM_PATH_MAX);
            return p;
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

        public Arch getArch() {
            return arch;
        }

        public void setArch(Arch arch) {
            this.arch = arch;
        }

        public long getBits() {
            return bits;
        }

        public void setBits(long bits) {
            this.bits = bits;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
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
            return "LmProcess{" +
                    "id=" + id +
                    ", pid=" + pid +
                    ", arch=" + arch +
                    ", bits=" + bits +
                    ", startTime=" + startTime +
                    ", path='" + path + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
