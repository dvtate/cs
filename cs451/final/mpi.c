#include <stdlib.h>
#include <mpi.h>



int main(int argc, char** argv) {
    // This proc rank
    int comm_rank;
    // Number of ranks
    int comm_size;

    // Initialize mpi and get comm info
    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &comm_rank);
    MPI_Comm_size(MPI_COMM_WORLD, &comm_size);

    // Generate A & B
    float A[1000][1000], B[1000][1000];
    populate(A,B, 1000);

    // Distribute
    MPI_Status tmp;
    for (int i = 0; i < 1000; i++) {
        const int rank = i % comm_size;

        if (comm_rank != rank && comm_rank != 0)
            continue;

        // Don't broadcast for rank 0 to self
        if (comm_rank == 0 && rank == 0) {
            for (int j = 0; j < 1000; j++)
                B[i][j] = A[j][i];
            continue;
        }

        // Type to represent i-th column in A
        MPI_Datatype col_type;
        MPI_Type_vector(1000, i, 1000, MPI_FLOAT, &col_type);
        MPI_Type_commit(&col_type);

        // Distribute the column to the ranks
        if (comm_rank == 0)
            MPI_Send(A, 1, col_type, rank, i, MPI_COMM_WORLD);
        else if (comm_rank == rank)
            MPI_Recv(B[i], 1000, MPI_FLOAT, 0, i, MPI_COMM_WORLD, &tmp);
    }

    // Block all threads
    MPI_Barrier(MPI_COMM_WORLD);

    // rejoin
    MPI_Status tmp;
    for (int i = 0; i < 1000; i++) {
        const int rank = i % comm_size;

        // Rank zero rows are already solved
        // Other ranks might not be relevant
        if ((comm_rank == rank) == (comm_rank == 0))
            continue;

        // Distribute the column to the ranks
        if (comm_rank == 0)
            MPI_Recv(B[i], 1000, MPI_FLOAT, rank, i, MPI_COMM_WORLD, &tmp);
        else if (comm_rank == rank)
            MPI_Send(B[i], 1000, MPI_FLOAT, 0, i, MPI_COMM_WORLD);
    }

    // Solution stored in B in rank 0
}