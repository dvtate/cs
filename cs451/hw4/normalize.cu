/* Matrix normalization.
 * Compile with "gcc matrixNorm.c"
 */

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <sys/time.h>
#include <math.h>

// Matrix dimension
int N;

// Cuda variables
int n_blocks = 16;
int n_threads_per_block = 32;

/* Initialize A and B*/
void initialize_inputs(int argc, char** argv, float*& A, float*& B) {
    // User requested specific number of threads
    if (argc <= 1) {
        printf("USAGE: %s <N> <blocks> <threads per block> <seed>\n", argv[0]);
        exit(1);
    }

    // Set dimension
    if (argc > 1)
        N = atoi(argv[1]);

    // Set number of blocks
    if (argc > 2) {
        n_blocks = atoi(argv[2]);
    }

    // Set threads per block
    if (argc > 3) {
        n_threads_per_block = atoi(argv[3]);
    }

    // Set seed
    if (argc > 4)
        srand(atoi(argv[4]));
    else
        srand((unsigned)time(NULL));

    // Allocate space for the matricies
    A = (float*) malloc(N * N * sizeof(float));
    B = (float*) malloc(N * N * sizeof(float));

    // Initialize the matrix with random values
    for (int row = 0; row < N; row++)
        for (int col = 0; col < N; col++) {
            A[row * N + col] = (float)rand() / 32768.0;
            B[row * N + col] = 0.0;
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
__global__ void matrixNorm(float* A, float* B, int N) {
    // Calculate column number
    int col = blockIdx.x * blockDim.x + threadIdx.x;

    // Maybe some wasted threads
    if (col < N) {
        int row;         // Row index for loops
        float mu, sigma; // Mean and Standard Deviation

        // Calculate mean for column
        mu = 0.0;
        for (row = 0; row < N; ++row)
            mu += A[row * N + col];
            mu /= N;
        __syncthreads();

        // Calculate standard deviation for the column
        sigma = 0.0;
        for (row = 0; row < N; ++row)
            sigma += powf(A[col * N + row] - mu, 2.0);
        sigma /= N;
        sigma = sqrt(sigma);
        __syncthreads();

        // Normalize column
        for (row = 0; row < N; ++row)
            if (sigma == 0.0)
                B[row * N + col] = 0.0;
            else
                B[row * N + col] = (A[col * N + row] - mu) / sigma;
    }
}

int main(int argc, char** argv) {
    /* Timing variables */
    struct timeval start, stop;  /* Elapsed times using gettimeofday() */
    struct timeval norm_start, norm_stop; // time values for the normalization algorithm
    struct timezone tzdummy;
    unsigned long long runtime;

    /* Initialize A and B */
    float* A, * B;
    initialize_inputs(argc, argv, A, B);

    /* Start Clock */
    gettimeofday(&start, &tzdummy);
    // Create buffers on device
    float* gpu_A, * gpu_B;
    cudaMalloc((void**) &gpu_A, N * N * sizeof(float));
    cudaMalloc((void**) &gpu_B, N * N * sizeof(float));

    // Send problem to device
    cudaMemcpy((void*) gpu_A, A, N * N * sizeof(float), cudaMemcpyHostToDevice);
    cudaMemcpy((void*) gpu_B, B, N * N * sizeof(float), cudaMemcpyHostToDevice);

    /* Matrix Normalization */
    gettimeofday(&norm_start, &tzdummy);
    matrixNorm<<<n_blocks, n_threads_per_block>>>(gpu_A, gpu_B, N);
    gettimeofday(&norm_stop, &tzdummy);

    // Pull result from the device
    cudaMemcpy((void*) B, gpu_B, N * N * sizeof(float), cudaMemcpyDeviceToHost);

    /* Calculate runtimes */
    gettimeofday(&stop, &tzdummy);
    runtime = (unsigned long long)(stop.tv_sec - start.tv_sec) * 1000000 + (stop.tv_usec - start.tv_usec);
    unsigned long long norm_time = (unsigned long long)
	    (norm_stop.tv_sec - norm_start.tv_sec) * 1000000 + (norm_stop.tv_usec - norm_start.tv_usec);

    /* Display timing results */
    printf("Runtime = %g ms.\n", (float)runtime/(float)1000);
    printf("Normalization time = %g ms.\n", (float)norm_time/(float)1000);

    // Debug for small N
    if (N <= 20) {
        print_matrix(A);
        print_matrix(B);
    }

    // Cleanup and exit
    free(A);
    free(B);
    cudaFree(gpu_A);
    cudaFree(gpu_B);
    return 0;
}
