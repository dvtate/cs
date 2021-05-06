/* Matrix normalization.
 * Compile with "gcc matrixNorm.c"
 */

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <sys/time.h>
#include <math.h>

/* Program Parameters */
#define N 6000  /* Matrix size */

/* Matrices */
volatile float A[N][N], B[N][N];

/* Initialize A and B*/
void initialize_inputs() {
    int row, col;
    srand((unsigned)time(NULL));
    for (row = 0; row < N; row++) {
        for (col = 0; col < N; col++) {
            A[row][col] = (float)rand() / 32768.0;
            B[row][col] = 0.0;
        }
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

void matrixNorm() {
    int row, col;
    float mu, sigma; // Mean and Standard Deviation

    printf("Computing Serially.\n");

    for (col=0; col < N; col++) {
        // Calculate mean for the column
        mu = 0.0;
        for (row=0; row < N; row++)
            mu += A[row][col];
        mu /= (float) N;

        // Calculate standard deviation for the column
        sigma = 0.0;
        for (row=0; row < N; row++)
            sigma += powf(A[row][col] - mu, 2.0);
        sigma /= (float) N;
        sigma = sqrt(sigma);

        //  Normalize column
        for (row=0; row < N; row++) {
            if (sigma == 0.0)
                B[row][col] = 0.0;
            else
                B[row][col] = (A[row][col] - mu) / sigma;
        }
    }
}



int main(int argc, char **argv) {
    (void)argc;
    (void)argv;

    /* Timing variables */
    struct timeval start, stop;  /* Elapsed times using gettimeofday() */
    struct timezone tzdummy;
    unsigned long long runtime;

    /* Initialize A and B */
    initialize_inputs();


    /* Start Clock */
    printf("\n---------------------------------------------\n");
    printf("Matrix size N = %d", N);
    printf("\nStarting clock.\n\n");
    gettimeofday(&start, &tzdummy);


    /* Matrix Normalization */
    matrixNorm();


    /* Stop Clock */
    gettimeofday(&stop, &tzdummy);
    runtime = (unsigned long long)(stop.tv_sec - start.tv_sec) * 1000000 + (stop.tv_usec - start.tv_usec);


    /* Display timing results */
    printf("Runtime = %g ms.\n", (float)runtime/(float)1000);
    printf("\nStopped clock.");
    printf("\n---------------------------------------------\n");

    // For proof of correction we print matricies for small N
    if (N <= 20) {
        print_matrix((float*) A);
        print_matrix((float*) B);
    }

    exit(0);
}
