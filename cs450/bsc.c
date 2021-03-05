// NOTE This question was flawed idk why i'm adding it lol

// this runs atomically (in hardware)
void bsc(int bit_num, int * addr, unsigned * bits, int val) {
     if (*bits  & (1 << bit_num)) {
          *addr = val;
     }
}

// For now pretending that bsc is actually a wrapper around inline assembly

// In a correct solution, I think this would be used as a bitfield instead of a boolean
typedef int mutex_t;

// Initialize lock
void lock_init(mutex_t * m) {
	*m = 0;
}

// flawed
void lock_acquire(mutex_t * m) { 
	// spin until we get lock
	while (*m) ;

	// lock
	bsc(0, &m, &m, 1);
}

// flawed
void lock_release(mutex_t * m) {
	// unlock
	bsc(0, &m, &m, 0);
}
