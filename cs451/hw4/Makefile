
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
	echo "computing serial..."
	./serial
	echo "computing with omp..."
	./omp 6000 1 1
	./omp 6000 2 1
	./omp 6000 4 1
	./omp 6000 8 1
	./omp 6000 12 1
	./omp 6000 16 1
	./omp 6000 `nproc` 1
	echo "computing with cuda..."
	./cuda 6000 32 16 1
