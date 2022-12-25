package net.jpountz.xxhash;

final class StreamingXXHash64JNI extends StreamingXXHash64
{
    private long state;
    
    StreamingXXHash64JNI(final long seed) {
        super(seed);
        this.state = XXHashJNI.XXH64_init(seed);
    }
    
    private void checkState() {
        if (this.state == 0L) {
            throw new AssertionError((Object)"Already finalized");
        }
    }
    
    @Override
    public void reset() {
        this.checkState();
        XXHashJNI.XXH64_free(this.state);
        this.state = XXHashJNI.XXH64_init(this.seed);
    }
    
    @Override
    public long getValue() {
        this.checkState();
        return XXHashJNI.XXH64_digest(this.state);
    }
    
    @Override
    public void update(final byte[] bytes, final int off, final int len) {
        this.checkState();
        XXHashJNI.XXH64_update(this.state, bytes, off, len);
    }
    
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        XXHashJNI.XXH64_free(this.state);
        this.state = 0L;
    }
    
    static class Factory implements StreamingXXHash64.Factory
    {
        public static final StreamingXXHash64.Factory INSTANCE;
        
        @Override
        public StreamingXXHash64 newStreamingHash(final long seed) {
            return new StreamingXXHash64JNI(seed);
        }
        
        static {
            INSTANCE = new Factory();
        }
    }
}
