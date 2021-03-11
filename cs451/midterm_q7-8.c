/* Q7: contained a dependency, restructured to make parallel
float a[500], b[500], c[500],d[500];

int i;

d[1] = a[0] + b[1];
#pragma omp parallel for
for (i=2; i<500; i++)
	d[i] = (a[i-1] = b[i-1]+c[i-1]) + b[i];
*/

// Q8: ... 
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>

float a[500], b[500], c[500],d[500];

int numThreads = 3;

void* loop_thread(void* id) {
	for (int i = (int)(intptr_t)id; i < 500; i += numThreads)
		d[i] = (a[i - 1] = b[i - 1] + c[i - 1]) + b[i];

	return NULL;
}

int main() {

	// Like in q7, a[0] is uninitialized to match orignal
	d[1] = a[0] + b[1];
	
	// Spawn threads
	pthread_t threads[numThreads];
	for (int i = 0; i < numThreads; i++)
		if (pthread_create(threads + i, NULL, loop_thread, (void*)(intptr_t) i) != 0) {
			fprintf(stderr, "pthread_create() error");
			return -1;
		}

	// Join threads
	void* tmp;
	for (int i = 0; i < numThreads; i++)
		if (pthread_join(threads[i], &tmp) != 0) {
			fprintf(stderr, "pthread_create() error");
			return -1;
		}
	
	return 0;
}
