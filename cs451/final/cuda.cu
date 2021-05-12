


// Kernel function where each thread does a single element
__global__ void kernel_fxn(float* A, float* B, int N) {
    // Get element to do
    int e = blockIdx.x * blockDim.x + threadIdx.x;
    if (e >= N * N)
        return;

    //
    B[e] = 4 * A[e];
}

// Kernel function where each thread does a single row
__global__ void kernel_fxn2(float* A, float* B, int N) {
    // Get row to do
    int row = blockIdx.x * blockDim.x + threadIdx.x;
    if (row >= N)
        return;

    for (int i = 0; i < N; i++)
        B[row * N + i] = 4 * A[row * N + i];
}


// Host function
int main(int argc, char** argv) {
    // A and B are the matricies
    float* A, B;
    // let N be the dimension of the matricies
    int N;
    get_params(A, B, &N);

    // Reserve space on GPU
    float* gpu_A, * gpu_B;
    cudaMalloc((void**) &gpu_A, N * N * sizeof(float));
    cudaMalloc((void**) &gpu_B, N * N * sizeof(float));

    // Send problem to device
    cudaMemcpy((void*) gpu_A, A, N * N * sizeof(float), cudaMemcpyHostToDevice);

    // Make device do the problem
    kernel_fxn<<n_blocks, n_threads_per_block>>>(gpu_A, gpu_B, N);

    // Pull solution from the device
    cudaMemcpy((void*) B, gpu_B, N * N * sizeof(float), cudaMemcpyDeviceToHost);

    // Solution now stored in B
}