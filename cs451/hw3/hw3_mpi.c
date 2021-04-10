#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <math.h>
#include <string.h>
#include <sys/types.h>
#include <sys/times.h>
#include <sys/time.h>
#include <time.h>
#include <mpi.h>

// Max matrix size
#define MAX_N 5000

// Create random seed using time
int time_seed() {
    struct timeval t;
    struct timezone tzdummy;

    gettimeofday(&t, &tzdummy);
    return (int)(t.tv_usec);
}

// The Dimension of our square matrix
unsigned N;

// Parse parameters
void read_params(int argc, char** argv) {
    // Read dimension from params
    N = argc >= 2 ? atoi(argv[1]) : 5000;

    // Read random seed from params
    srand(argc >= 3 ? atoi(argv[2]) : time_seed());
}

// Populate with random floats
void random_fill(float* p, unsigned len) {
    // for (unsigned j = 0; j < c; j++)
    //     for (unsigned i = 0; i < r; i++)
    for (unsigned n = 0; n < len; n++)
            p[n] = (float)rand() / (float) (1 << 19);
}

void print_aug_matrix(float matrix[]) {
    printf("matrix:\n\t");
    for (unsigned r = 0; r < N; r++)
        for (unsigned c = 0; c < N + 1; c++)
            printf("%f.2f%s", matrix[r * N + c], (c < N) ? (c == N - 1 ? " | ": ", ") : "\n\t");
}
void print_vector(float vector[], unsigned len) {
    printf("vector: [");
    for (unsigned i = 0; i < len; i++)
        printf("%5.2f%s", vector[i], (i < N -1) ? ", " : ";\n\t");
}

//
int main(int argc, char** argv) {
    // This proc rank
    int comm_rank;
    // Number of ranks
    int comm_size;

    // Initialize mpi and get comm info
    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &comm_rank);
    MPI_Comm_size(MPI_COMM_WORLD, &comm_size);

    // Read command line args
    read_params(argc, argv);

    ////////
    // Distr work
    ////////

    // Rank zero holds complete solution
    // Notice that we're storing the augmented matrix in order to reduce MPI
    // calls and better mimic the math
    const unsigned row_width = N + 1;
    float* matrix;
    if (comm_rank == 0 ) {
        // Notice that the last column is our vector
        matrix = malloc(N * (N + 1) * sizeof(float));
        random_fill(matrix, N * (N + 1));
    }

    // Calculate work for each processor
    const unsigned rows_per_rank = N / comm_size;
    if (N % rows_per_rank) {
        printf("ERROR: Dimension must be divisible by number of processors, %d", comm_size);
        return -1;
    }

    // Distribute the work to the processors
    float work[row_width * rows_per_rank];
    if (comm_size == 1)
        memcpy(work, matrix, N * N * sizeof(float)); // want to be able to measure overhead
    else
        for (unsigned i = 0; i < rows_per_rank; i++)
            MPI_Scatter(
                matrix + i * row_width * comm_size, row_width, MPI_FLOAT, // send
                work + i * row_width, row_width, MPI_FLOAT,              // recv
                0, MPI_COMM_WORLD);                                     // env


    // Gaussian Elimination
    float row[row_width];

    // Start tracking time
    if (comm_rank == 0) {
        // start_time = MPI_Wtime();

        print_aug_matrix(matrix);
    }

    // Locals

    // For each diagonal, pivot
    for (unsigned n = 0; n < N; n++) {
        // Row in work[]
        const unsigned loc_row = n / comm_size;
        // Rank responsible for this row's elimination
        const int elim_rank = n % comm_size;


        if (comm_rank == elim_rank) {
            // Divide row to right of the pivot by the pivot
            // Note: Optimizations: mul by reciporocal, assignment for trivial case
            printf("pivot=%f\n", work[loc_row * row_width + n]);
            const float rpivot = 1.0f / work[loc_row * row_width + n];
            work[loc_row * row_width + n] = 1.0f;
            printf("pivot: work(%d)[%d][%d] = %f\n", comm_rank, loc_row, n, rpivot);
            for (unsigned i = n + 1; i < row_width; i++)
                work[loc_row * row_width + i] *= rpivot;

            // Send row to other ranks
            memcpy(row, work + loc_row * row_width, row_width * sizeof(float)); // TODO remove this ?
            MPI_Bcast(row, row_width, MPI_FLOAT, elim_rank, MPI_COMM_WORLD);

            // Eliminate other rows mapped to this rank
            for (unsigned i = loc_row + 1; i < rows_per_rank; i++) {
                const float scale = work[i * row_width + n];
                for (unsigned j = i  + 1; j < row_width; j++)
                    work[i * row_width + j] -= scale * row[j];

                // Mathematically correct but overkill
                work[i * N + 1] = 0;
            }

        } else {
            // Recive row from elimination thread
            MPI_Bcast(row, row_width, MPI_FLOAT, elim_rank, MPI_COMM_WORLD);

            for (unsigned i = loc_row; i < rows_per_rank; i++)
                if (elim_rank < comm_rank || i > loc_row) {
                    const float scale = work[i * row_width + n];
                    for (unsigned j = i  + 1; j < row_width; j++)
                        work[i * row_width + j] -= scale * row[j];

                    // Mathematically correct but overkill
                    work[i * N + 1] = 0;
                }
        }
    }

    MPI_Barrier(MPI_COMM_WORLD);

    // Stop timer
    if (comm_rank == 0) {
        // t_end = MPI_Wtime();
        // t_total = t_end - t_start;
    }

    if (comm_size == 1)
        memcpy(matrix, work, N * row_width * sizeof(float));
    else
        for (unsigned i = 0; i < rows_per_rank; i++)
            MPI_Gather(
                work + i * row_width, row_width, MPI_FLOAT,                 // send
                matrix + i * comm_size * row_width, row_width, MPI_FLOAT,   // recv
                0, MPI_COMM_WORLD);                                         // env


    // Find the solution
    if (comm_rank == 0) {
        // Backsubstitution
        float solution[N];
        for (int r = N -1; r >= 0; r--) {
            solution[r] = matrix[r * row_width + row_width - 1];
            for (int c = N - 1; c >= 0; c--)
                solution[r] -= matrix[r * row_width + c] * solution[c];
            solution[r] /= matrix[r * row_width + r];
        }

        print_aug_matrix(matrix);
        print_vector(solution, N);
    }

    MPI_Finalize();

    // Debug args
    // if (comm_rank == 0)
    //     for (int i = 0 ; i < argc; i++)
    //         puts(argv[i]);
}
