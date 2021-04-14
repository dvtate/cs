FLAGS := -g -O3 -Wall -Wextra

.PHONY: all clean

all:
	mpicc hw3_mpi.c -o gauss_mpi $(FLAGS)

clean:
	rm gauss_mpi

test_single: all
	mpirun -n 1 ./gauss_mpi 3000 1

test_multi: 
	mpirun -n 1 ./gauss_mpi 3000 1
	mpirun -n 2 ./gauss_mpi 3000 1
	mpirun -n 4 ./gauss_mpi 3000 1
	mpirun -n 8 ./gauss_mpi 3000 1
	mpirun -n 12 ./gauss_mpi 3000 1
	mpirun -n 16 ./gauss_mpi 3000 1
