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

## Optimizations
- For the Open MP version I tried manually setting the maximum number of threads like for the pthread version but found that performance was better on my machine with the default
- For the pthread version I was fairly comfortable with so didn't see any easy ways to improve performance
- Compiling with -O3 improves performance nearly 3x

## Room for improvement
The pthreads version currently assigns equal work to all the processors instead of using a work queue or something of that nature. It's likely that using a queue - replacing local `norm` with a statically defined `atomic_int` that each thread increments each step would be ideal.

- Use SIMD vector instructions
  - The body of the main for loop could be broken up into 3 blocks that are either O(1) or replacable by simd operations
- Blocking to improve cache efficiency (untested possibly non-issue)
-