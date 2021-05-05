cuda: 
	nvcc normalize.cu

omp:
	g++ omp.c -o omp -O3 -Wall -Wextra -fopenmp

serial:
	g++ serial.c -o serial -O3 -Wall -Wextra -g

all: cuda omp serial

clean:
	rm a.out
	rm omp
	rm serial
	
