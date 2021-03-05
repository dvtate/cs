// Stateless server fs api
int open(char *path);
int pread(int fh, void* buf, size_t size, size_t offset) {
	// open()
	// seek()
	// read() into buff
}
int pwrite(int fh, void* buf, size_t size, size_t offset) {
	// open()
	// seek()
	// write() from buff
}

// Client-side Wrappers
struct FILE {
	int handle;		// Active file handle
	long offset;	// Cursor position
};

int nread (struct FILE* f, void * buf, size_t nbytes)
{
	// This function should invoke an RPC that does the following:
	int bytes_read = pread(f->handle, buf, nbytes, f->offset);
	if (bytes_written == -1)
		return -1;
	f->offset += bytes_read;
	return bytes_read;
}

int nwrite (struct FILE* f, void * buf, size_t nbytes)
{
	// This function should invoke an RPC that does the following:
	int bytes_written = pwrite(f->handle, buf, nbytes, f->offset);
	if (bytes_written == -1)
		return -1;
	f->offset += bytes_written;
	return bytes_written;
}
