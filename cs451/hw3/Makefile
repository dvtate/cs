FLAGS := -g -O3 -Wall -Wextra

.PHONY: all clean

all:
	mpicc hw3_mpi.c -o gauss_mpi $(FLAGS)

clean:
	rm gauss_mpi

test_single: all
	mpirun -n 1 ./gauss_mpi 3000 1

test_assigned:
	mpirun -n 1 ./gauss_mpi 3000 1
	mpirun -n 2 ./gauss_mpi 3000 1
	mpirun -n 4 ./gauss_mpi 3000 1
	mpirun -n 8 ./gauss_mpi 3000 1
	mpirun -n 12 ./gauss_mpi 3000 1
	mpirun -n 16 ./gauss_mpi 3000 1

test_1_22: 
	mpirun -n 1 ./gauss_mpi 3000 1
	mpirun -n 2 ./gauss_mpi 3000 1
	mpirun -n 3 ./gauss_mpi 3000 1
	mpirun -n 4 ./gauss_mpi 3000 1
	mpirun -n 5 ./gauss_mpi 3000 1
	mpirun -n 6 ./gauss_mpi 3000 1
	mpirun -n 7 ./gauss_mpi 3000 1
	mpirun -n 8 ./gauss_mpi 3000 1
	mpirun -n 9 ./gauss_mpi 3000 1
	mpirun -n 10 ./gauss_mpi 3000 1
	mpirun -n 11 ./gauss_mpi 3000 1
	mpirun -n 12 ./gauss_mpi 3000 1
	mpirun -n 13 ./gauss_mpi 3000 1
	mpirun -n 14 ./gauss_mpi 3000 1
	mpirun -n 15 ./gauss_mpi 3000 1
	mpirun -n 16 ./gauss_mpi 3000 1
	mpirun -n 17 ./gauss_mpi 3000 1
	mpirun -n 18 ./gauss_mpi 3000 1
	mpirun -n 19 ./gauss_mpi 3000 1
	mpirun -n 20 ./gauss_mpi 3000 1
	mpirun -n 21 ./gauss_mpi 3000 1
	mpirun -n 22 ./gauss_mpi 3000 1
