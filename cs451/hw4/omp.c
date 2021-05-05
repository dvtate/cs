/* Matrix normalization.
 * Compile with "gcc matrixNorm.c"
 */

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <sys/time.h>
#include <math.h>
#include <omp.h>

// Matrix size
int N;

/* Initialize A and B*/
void initialize_inputs(int argc, char **argv, float** A, float** B) {
    // User requested specific number of threads
    if (argc <= 1) {
        printf("USAGE: %s <N> <#threads> <seed>\n", argv[0]);
        exit(1);
    }

    // Set dimension
    if (argc > 1)
        N = atoi(argv[1]);

    // Set num threads
    if (argc > 2) {
        int nt = atoi(argv[2]);
        if (nt > N)
            nt = N;
        if (nt)
            omp_set_num_threads(nt);
    }

    // Set seed
    if (argc > 3)
        srand(atoi(argv[3]));
    else
        srand((unsigned)time(NULL));

    // Initialize matricies
    *A = (float*) malloc(N * N * sizeof(float));
    *B = (float*) malloc(N * N * sizeof(float));

    // Populate matrix with given values
    for (int row = 0; row < N; row++)
        for (int col = 0; col < N; col++) {
            (*A)[row * N + col] = (float)rand() / 32768.0;
            (*B)[row * N + col] = 0.0;
        }
}

/// Print a matrix's content for debugging
void print_matrix(float* m) {
    printf("[");
    for (int row = 0; row < N; row++) {
        printf("\n\t");
        for (int col = 0; col < N; col++)
            printf("%10.5f\t", m[row * N + col]);
    }
    printf("]\n");
}


/* Kernel function */
void matrixNorm(float* A, float* B) {
    int row, col;

    float mu, sigma; // Mean and Standard Deviation

    for (col=0; col < N; col++) {
        // Calculate mean for the column
        mu = 0.0;
        #pragma omp parallel for
        for (row=0; row < N; row++)
            mu += A[row * N + col];
        mu /= (float) N;

        // Calculate standard deviation for the column
        sigma = 0.0;
        #pragma omp parallel for
        for (row=0; row < N; row++)
            sigma += powf(A[row * N + col] - mu, 2.0);
        sigma /= (float) N;
        sigma = sqrt(sigma);

        //  Normalize column
        #pragma omp parallel for
        for (row=0; row < N; row++) {
            if (sigma == 0.0)
                B[row * N + col] = 0.0;
            else
                B[row * N + col] = (A[row * N + col] - mu) / sigma;
        }
    }
}



int main(int argc, char **argv) {
    /* Timing variables */
    struct timeval start, stop;  /* Elapsed times using gettimeofday() */
    struct timezone tzdummy;
    unsigned long long runtime;

    /* Initialize A and B */
    float* A, * B;
    initialize_inputs(argc, argv, &A, &B);


    /* Start Clock */
    printf("\n---------------------------------------------\n");
    printf("Matrix size N = %d", N);
    printf("\nStarting clock.\n\n");
    gettimeofday(&start, &tzdummy);


    /* Matrix Normalization */
    matrixNorm(A, B);


    /* Stop Clock */
    gettimeofday(&stop, &tzdummy);
    runtime = (unsigned long long)(stop.tv_sec - start.tv_sec) * 1000000 + (stop.tv_usec - start.tv_usec);


    /* Display timing results */
    printf("Runtime = %g ms.\n", (float)runtime/(float)1000);
    printf("\nStopped clock.");
    printf("\n---------------------------------------------\n");

    // For proof of correction we print matricies for small N
    if (N <= 20) {
        print_matrix(A);
        print_matrix(B);
    }

    // Cleanup and exit
    free(A);
    free(B);
    return 0;
}
