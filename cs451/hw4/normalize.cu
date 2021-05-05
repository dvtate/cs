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
    for (row = 0; row < N; row++)
        for (col = 0; col < N; col++) {
            A[row][col] = (float)rand() / 32768.0;
            B[row][col] = 0.0;
        }

}

 /* Kernel function */

__global__ void matrixNorm(float* A, float* B) {
     int col = blockIdx.x * blockDim.x + threadIdx.x;
     __shared__ int row;
     __shared__ float mu, sigma; // Mean and Standard Deviation

    // Clamp to number of cols
    if (col < N) {
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

int main(int argc, char **argv) {
    /* Timing variables */
    struct timeval start, stop;  /* Elapsed times using gettimeofday() */
    struct timezone tzdummy;
    unsigned long long runtime;

    /* Initialize A and B */
    initialize_inputs();

    /* Start Clock *//*
    printf("\n---------------------------------------------\n");
    printf("Matrix size N = %d", N);
    printf("\nStarting clock.\n\n");*/
    gettimeofday(&start, &tzdummy);

    // Create buffers on device
    float* gpu_A, * gpu_b;
    cudaMalloc((void**) &gpu_A, sizeof(float) * N * N);
    cudaMalloc((void**) &gpu_B, sizeof(float) * N * N);

    // Send problem to device
    cudaMemcpy(A, gpu_A, sizeof(float) * N * N, cudaMemcpyHostToDevice);

    /* Matrix Normalization */
    matrixNorm<<<numBlocks,numThreadsPerBlock>>>(gpu_A, gpu_B);

    // Pull result from the device
    cudaMemcpy((void*)B, gpu_B, sizeof(float) * N * N, cudaMemcpyDeviceToHost);

    /* Stop Clock */
    gettimeofday(&stop, &tzdummy);
    runtime = (unsigned long long)(stop.tv_sec - start.tv_sec) * 1000000 + (stop.tv_usec - start.tv_usec);

    /* Display timing results */
/*    printf("Runtime = %g ms.\n", (float)runtime/(float)1000);
    printf("\nStopped clock.");
    printf("\n---------------------------------------------\n");*/

    return 0;
}
