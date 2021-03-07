/* Gaussian elimination without pivoting.
 * Compile with "gcc gauss_serial.c"
 */

/* ****** ADD YOUR CODE AT THE END OF THIS FILE. ******
 * You need not submit the provided code.
 */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <math.h>
#include <sys/types.h>
#include <sys/times.h>
#include <sys/time.h>
#include <time.h>
#include <pthread.h>

/* Program Parameters */
#define MAXN 2000  /* Max value of N */
int N;  /* Matrix size */
int numThreads = 1; /* Number of threads */

/* Matrices and vectors */
volatile float A[MAXN][MAXN], B[MAXN], X[MAXN];
/* A * X = B, solve for X */

/* junk */
// #define randm() 4|2[uid]&3

/* Prototype */
void gauss();  /* The function you will provide.
		* It is this routine that is timed.
		* It is called only on the parent.
		*/

/* returns a seed for srand based on the time */
unsigned int time_seed() {
  struct timeval t;
  struct timezone tzdummy;

  gettimeofday(&t, &tzdummy);
  return (unsigned int)(t.tv_usec);
}

/* Set the program parameters from the command-line arguments */
void parameters(int argc, char **argv) {
	// char uid[32]; /*User name */

	/* Read command-line arguments */
	if (argc < 2) {
		printf("Usage: %s <matrix_dimension> [random seed]\n",
				argv[0]);
		exit(0);
	}

	if (argc >= 2) {
		N = atoi(argv[1]);
		if (N < 1 || N > MAXN) {
			printf("N = %i is out of range.\n", N);
			exit(0);
		}
	}

  // Seed random
  if (argc >= 3) {
    const int seed = atoi(argv[2]);
		srand(seed);
		printf("Random seed = %i\n", seed);
  } else {
  	srand(time_seed());
  }

	if (argc >= 4) {
		numThreads = atoi(argv[3]);
    // Only one thread per dimension
    if (numThreads > N)
      numThreads = N;
	}

	/* Print parameters */
	printf("\nMatrix dimension N = %i.\n", N);
}

/* Initialize A and B (and X to 0.0s) */
void initialize_inputs() {
  int row, col;

  printf("\nInitializing...\n");
  for (col = 0; col < N; col++) {
    for (row = 0; row < N; row++) {
      A[row][col] = (float)rand() / 32768.0;
    }
    B[col] = (float)rand() / 32768.0;
    X[col] = 0.0;
  }

}

/* Print input matrices */
void print_inputs() {
  int row, col;

  if (N < 10) {
    printf("\nA =\n\t");
    for (row = 0; row < N; row++) {
      for (col = 0; col < N; col++) {
	printf("%5.2f%s", A[row][col], (col < N-1) ? ", " : ";\n\t");
      }
    }
    printf("\nB = [");
    for (col = 0; col < N; col++) {
      printf("%5.2f%s", B[col], (col < N-1) ? "; " : "]\n");
    }
  }
}

void print_X() {
  int row;

  if (N < 100) {
    printf("\nX = [");
    for (row = 0; row < N; row++) {
      printf("%5.2f%s", X[row], (row < N-1) ? "; " : "]\n");
    }
  }
}

int main(int argc, char **argv) {
  /* Timing variables */
  struct timeval etstart, etstop;  /* Elapsed times using gettimeofday() */
  struct timezone tzdummy;
  // clock_t etstart2, etstop2;  /* Elapsed times using times() */
  unsigned long long usecstart, usecstop;
  struct tms cputstart, cputstop;  /* CPU times for my processes */

  /* Process program parameters */
  parameters(argc, argv);

  /* Initialize A and B */
  initialize_inputs();

  /* Print input matrices */
  print_inputs();

  /* Start Clock */
  printf("\nStarting clock.\n");
  gettimeofday(&etstart, &tzdummy);
  times(&cputstart);

  /* Gaussian Elimination */
  gauss();

  /* Stop Clock */
  gettimeofday(&etstop, &tzdummy);
  times(&cputstop);
  printf("Stopped clock.\n");
  usecstart = (unsigned long long)etstart.tv_sec * 1000000 + etstart.tv_usec;
  usecstop = (unsigned long long)etstop.tv_sec * 1000000 + etstop.tv_usec;

  /* Display output */
  print_X();

  /* Display timing results */
  printf("\nElapsed time = %g ms.\n",
	 (float)(usecstop - usecstart)/(float)1000);

  printf("(CPU times are accurate to the nearest %g ms)\n",
	 1.0/(float)CLOCKS_PER_SEC * 1000.0);
  printf("My total CPU time for parent = %g ms.\n",
	 (float)( (cputstop.tms_utime + cputstop.tms_stime) -
		  (cputstart.tms_utime + cputstart.tms_stime) ) /
	 (float)CLOCKS_PER_SEC * 1000);
  printf("My system CPU time for parent = %g ms.\n",
	 (float)(cputstop.tms_stime - cputstart.tms_stime) /
	 (float)CLOCKS_PER_SEC * 1000);
  printf("My total CPU time for child processes = %g ms.\n",
	 (float)( (cputstop.tms_cutime + cputstop.tms_cstime) -
		  (cputstart.tms_cutime + cputstart.tms_cstime) ) /
	 (float)CLOCKS_PER_SEC * 1000);
      /* Contrary to the man pages, this appears not to include the parent */
  printf("--------------------------------------------\n");

  exit(0);
}

/* ------------------ Above Was Provided --------------------- */

/****** You will replace this routine with your own parallel version *******/
/* Provided global variables are MAXN, N, A[][], B[], and X[],
 * defined in the beginning of this code.  X[] is initialized to zeros.
 */

/*
 * Pthread callback that Performs Gausian Elimination
 *  - Operates on matricies stored in static memory
 *  - Work divided based on normal modulo with number of threads
 * \param id - intptr_t for which thread we're using
 * \returns null
 */
void* elim_thread(void* id) {
  int norm, row, col;  /* Normalization row, and zeroing
			* element row and col */
  float multiplier;

  /* Gaussian elimination */
  // Split each normal across all the threads
  for (norm = 0; norm < N - 1; norm++) {
    for (row = norm + 1 + (int)(intptr_t) id; row < N; row += numThreads) {
      multiplier = A[row][norm] / A[norm][norm];
      for (col = norm; col < N; col++)
	      A[row][col] -= A[norm][col] * multiplier;
      B[row] -= B[norm] * multiplier;
    }
  }

  /* NOTE Diagonal elements are not normalized to 1.  This is treated in back
   * substitution.
   */
   return NULL;
}

void gauss() {

  // Spawn threads to perform gausian elimination
  pthread_t threads[numThreads];
  for (int i = 0; i < numThreads; i++)
    if (pthread_create(threads + i, NULL, elim_thread, (void*)(intptr_t) i) != 0) {
      perror("pthread_create() error");
      exit(1);
    }

  // Synchronyze the threads
  for (int i = 0; i < numThreads; i++) {
    // Variable to capture unused return value
    void* tmp;
  	if (pthread_join(threads[i], &tmp) != 0) {
      perror("pthread_join() error");
      exit(1);
    }
  }

  /* Back substitution: this wasn't required to be parallelized... */
  for (int row = N - 1; row >= 0; row--) {
    X[row] = B[row];
    for (int col = N-1; col > row; col--) {
      X[row] -= A[row][col] * X[col];
    }
    X[row] /= A[row][row];
  }
}
