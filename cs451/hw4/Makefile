
.PHONY: cuda omp serial all clean

cuda:
	nvcc normalize.cu -O3 -o cuda

omp:
	g++ omp.c -o omp -O3 -Wall -Wextra -fopenmp # -g -lefence

serial:
	g++ serial.c -o serial -O3 -Wall -Wextra # -g

all: cuda omp serial

clean:
	rm cuda
	rm omp
	rm serial

test:
	./serial
	./omp 6000 `nproc` 1
	./cuda 6000 13 1 1
