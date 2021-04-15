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
    N = argc >= 2 ? atoi(argv[1]) : 3000;

    // Read random seed from params
    srand(argc >= 3 ? atoi(argv[2]) : time_seed());
}

// Populate with random floats
void random_fill(float* p, unsigned len) {
    for (unsigned n = 0; n < len; n++)
        p[n] = (float)rand() / (float) (1 << 19);
}

// Pretty-Print augmented matrix
void print_aug_matrix(float matrix[], unsigned rows) {
    printf("matrix(%d):\n\t", rows);
    for (unsigned r = 0; r < rows; r++)
        for (unsigned c = 0; c < N + 1; c++)
            printf("%10.5f%s", matrix[r * (N + 1) + c], (c < N) ? (c == N - 1 ? " | ": ", ") : "\n\t");
    printf("\n");
}
// Pretty print vector
void print_vector(float vector[], unsigned len) {
    printf("vector: [");
    for (unsigned i = 0; i < len; i++)
        printf("%5.2f%s", vector[i], (i < N -1) ? ", " : "]\n");
}


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

    // Distribute the work

    // Rank zero holds complete matrix
    // Notice that we're storing the augmented matrix in order to reduce MPI
    // calls and better mimic the math involved
    const unsigned row_width = N + 1;
    float* matrix;
    if (comm_rank == 0 ) {
        // Notice that the last column is our vector
        matrix = malloc(N * (N + 1) * sizeof(float));
        random_fill(matrix, N * (N + 1));
        //print_aug_matrix(matrix, N);
    }

    // Start tracking time as we now have the matrix
    double start_time;
    if (comm_rank == 0)
        start_time = MPI_Wtime();

    // Calculate work for each rank
    // And any overflow rows in the event that N isn't evenly divisible
    // Rows per rank is lower for the processors which don't have overflow work
    const unsigned overflow_rows = N % comm_size;
    const unsigned rows_per_rank = N / comm_size
       + (((int)overflow_rows > comm_rank) ? 1 : 0);

    // Distribute the work to the processors
    // Note: for N >= 2500 this is too big to put on the stack
    float* work = (float*) malloc(row_width * (rows_per_rank + 1) * sizeof(float));
    if (comm_size == 1)
        work = matrix;
    else
        // Distribute the work to the ranks
        for (unsigned r = 0; r < N; r++) {
            const int loc_row = r / comm_size;
            const int loc_rank = r % comm_size;
            MPI_Status tmp;
            if (comm_rank == 0 && loc_rank == 0)
                memcpy(work + loc_row * row_width, matrix + r * row_width, row_width * sizeof(float));
            else if (comm_rank == 0)
                MPI_Send(matrix + r * row_width,
                    row_width, MPI_FLOAT,
                    loc_rank, r, MPI_COMM_WORLD);
            else if (comm_rank == loc_rank)
                MPI_Recv(work + loc_row * row_width,
                    row_width, MPI_FLOAT,
                    0, r, MPI_COMM_WORLD, &tmp);
        }

    // Do the work

    // Communication buffer
    float row[row_width];

    double gauss_start_time;
    if (comm_rank == 0)
        gauss_start_time = MPI_Wtime();

    // For each diagonal, pivot
    for (unsigned n = 0; n < N; n++) {
        // Row in work[]
        const unsigned loc_row = n / comm_size;

        // Rank responsible for this row's elimination
        const int elim_rank = n % comm_size;

        if (comm_rank == elim_rank) {
            // Divide row to right of the pivot by the pivot
            // Note: Optimizations: mul by reciporocal, assignment for trivial case
            const float rpivot = 1.0f / work[loc_row * row_width + n];
            work[loc_row * row_width + n] = 1.0f;
            for (unsigned i = n + 1; i < row_width; i++)
                work[loc_row * row_width + i] *= rpivot;

            // Send row to other ranks
            memcpy(row, work + loc_row * row_width, row_width * sizeof(float));
            MPI_Bcast(row, row_width, MPI_FLOAT, elim_rank, MPI_COMM_WORLD);

            // Eliminate other rows mapped to this rank
            for (unsigned i = loc_row + 1; i < rows_per_rank; i++) {
                const float scale = work[i * row_width + n];
                for (unsigned j = n + 1; j < row_width; j++)
                    work[i * row_width + j] -= scale * row[j];

                // Mathematically correct but not needed
                //work[i * row_width + n] = 0;
            }
        } else {
            // Recive row from elimination thread
            MPI_Bcast(row, row_width, MPI_FLOAT, elim_rank, MPI_COMM_WORLD);

            for (unsigned i = loc_row; i < rows_per_rank; i++)
                if (elim_rank < comm_rank || i > loc_row)
                {
                    const float scale = work[i * row_width + n];
                    for (unsigned j = n  + 1; j < row_width; j++)
                        work[i * row_width + j] -= scale * row[j];

                    // Mathematically correct but not needed
                    //work[i * row_width + n] = 0;
                }
        }
    }

    MPI_Barrier(MPI_COMM_WORLD);

    // Recombine work from ranks
    // if comm_size == 1 then don't need to recombine
    if (comm_size != 1)
        for (unsigned r = 0; r < N; r++) {
            const int loc_row = r / comm_size;
            const int loc_rank = r % comm_size;
            MPI_Status tmp;
            if (comm_rank == 0 && loc_rank == 0)
                memcpy(matrix + r * row_width, work + loc_row * row_width, row_width * sizeof(float));
            else if (comm_rank == 0)
                MPI_Recv(matrix + r * row_width,
                    row_width, MPI_FLOAT,
                    loc_rank, r, MPI_COMM_WORLD, &tmp);
            else if (comm_rank == loc_rank)
                MPI_Send(work + loc_row * row_width,
                    row_width, MPI_FLOAT,
                    0, r, MPI_COMM_WORLD);
        }

    // Find the solution
    if (comm_rank == 0) {
        // Time before backsubstitution
        double pre_sol_time = MPI_Wtime();

        // Backsubstitution
        float solution[N];
        for (int r = N -1; r >= 0; r--) {
            solution[r] = matrix[r * row_width + row_width - 1];
            for (int c = N - 1; c > r; c--)
                solution[r] -= matrix[r * row_width + c] * solution[c];
            solution[r] /= matrix[r * row_width + r];
        }

        // Finished all calculations, take time
        double time = MPI_Wtime() - start_time;
        if (N <= 10) {
            print_aug_matrix(matrix, N);
            print_vector(solution, N);
        }

        printf("gausian elimination time: %f seconds\n", pre_sol_time - gauss_start_time);
        printf("total time: %f seconds\n\n", time);

        // Clean up
        free(matrix);
    }

    MPI_Finalize();
}
