# Homework assignment 2
## Compile
Use `make all` to compile the 3 source files into binaries:
1. gauss_openmp
2. gauss_pthread
3. gauss_serial

## Binary Usage
```
binary [N] [random seed] [# threads]
- N: Dimension of the matricies
- random seed: seed for populating the matricies
- # threads: number of threads to use (note this is required but ignored for serial version)
```

## Run Tests
To get a rough idea of performance differences you can use the built-in tests via `make test`. Otherwise you can use the binaries as described above.

## Correctness Argument
As you can see by running the tests with many cores, the time used by the application decreases by to roughly 20% of it's original runtime which falls inline with the time for gaussian elimination approaching zero as the number of cores increases and the time spent back-substituting remaining fixed.