# Homework assignment 3
## Compile
Use `make all` to compile the program to a MPI binary

## Binary Usage
```
binary [N] [random seed] [# threads]
- N: Dimension of the matricies
- random seed: seed for populating the matricies
- # threads: number of threads to use (ignored for serial version)
```

## Run Tests
To get a rough idea of performance differences you can use the built-in tests via `make test`. Otherwise you can use the binaries as described above.

## Optimizations
- [I tried normalizing the diagonals to 1 before performing the elimination](https://github.com/dvtate/cs/commit/c98c1e08d12bc5c6ecc6de71563f2b6a1a39cad2#diff-bf0707f6954d7ecc4a871f754fb38180b5f98922ba76adbf391833b610029a04R198), such as to reduce the total amount of divisions (an expensive operation), but the performance gain was not significant, and would have been a hardware dependent optimization anyway
- The provided serial algorithm performs the elimination operation on the entire row despite elements not in the upper triangular matrix being known to result in zeros or being used in the back-substitution phase. Instead I simply left those values to what they were initialized so that we don't have to waste resources on them.
- Some other minor changes to the program (some aren't C89 complaint but I'm sure everyone's using C11 by now)

## Algorithm description
- The algorithm is similar to the provided serial algorithm
- for each diagonal element - e
  - use threads to apply elementary row operations on the rows below it that would eliminate the values in the column below e
  - for each row r, below e's row in it's own thread
    - apply subtract a multiple of e's row from r such that it would eliminate the element in the column below e in r
    - don't change the elements in or left of e's column
  - synchronize all threads
