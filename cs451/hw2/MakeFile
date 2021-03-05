CC := gcc
FLAGS := -g

.PHONY: all pthread omp clean test

pthread:
	$(CC) hw2_pthread.c -o gauss_pthread -lpthread $(FLAGS)

omp:
	$(CC) hw2_omp.c -o gauss_openmp -fopenmp $(FLAGS)

all: pthread omp
	$(CC) hw2_serial.c -o gauss_serial $(FLAGS)

clean:
	rm gauss_openmp
	rm gauss_pthread
	rm gauss_serial

test: all
	./gauss_openmp 2000 1 4
	./gauss_pthread 2000 1 4
	./gauss_serial 2000 1 4