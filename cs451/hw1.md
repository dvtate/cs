1. 
	- Moore's Law: The observation that the number of transistors in dense IC's doubles roughly every 2 years. This represents a trend that holds with historic data and can be used to make projections despite not being a natural law
	- Dennard Scaling: States that as transistors get smaller, the power density remains constant. Thus as Moore's Law causes IC's to become more dense, Dennard Scaling ensures that the power usage remains rougly constant as a proportion of the IC area. And thus the performance-to-watt metric increases over time

2.
	- SISD: Single Instruction Stream, Single Data Stream
		+ Single data processor operating on individual data sets
		+ Standard Serial Von Neumann Architecture
	- SIMD: Single Instruction Stream, Multiple Data Streams
		+ Multiple processors performing the same operations on different data
		+ Examples: Vector processors, GPU, etc.
	- MIMD: Multiple Instruction Streams, Multiple Data Streams
		+ Multiple simultaneous instruction streams operating on multiple data streams
		+ Each processor is operates independently of others using separate data
	- MISD: Mutiple Instruction Streams, Single Data Stream
		+ Rare and unrealistic
		+ Could argue that some pipelined computations can fall into this category

3.
	- Shared Address Space:
		+ Threads have private locals on their individual stacks, and shared variables on can be static, on the heap, or in common blocks
		+ Syncronization over shared variables
		+ Communication between threads via shared variables
		+ Pros:
			* Shared Cache - more hits
		+ Cons:
			* Shared Cache - more thrashing
			* Data Bus is performance bottleneck => limited scalablility, rarely > 32 procs
	- Distributed Address Space:
		+ Each processor has it's own, private memory and cache
		+ Communication over a network interface used for all communication and syncronization
		+ Pros:
			* Private Cache - less thrashing
			* Locality - Memory is closer to CPU and doesn't need shared bus
			* Scaleability - Data bus no longer bottleneck
		+ Cons:
			* Overhead and complexity associated with message passing over network not suitable for some applications

4. 
	- Anti-dependence = Write After Read
	- Example Problem: There is an anti-dependence between instructions 2 and 3
		1. a = f()
		2. b = a + 1
		3. a = 6
	- Example Solution: Here the anti-dependence (2-3) was removed by adding a true dependence
		0. c = f()
		1. a = c
		2. b = c + 1
		3. a = 6

5.
	- Note that answers are for this specific example and not general case
	a.
		- If the algorithm branches right first, it would take 6 units of time
		- If the algorithm branches left first, it would take 11 units of time
	b. 
		- If the algorithm branches right first, it would take 6 units of time (not improved)
		- If the algorithm branches left first, it would take 4 units of time (improved)
		- Assuming the algorithm uses the left-first algorithm it would be a super-linear speedup anomaly as the speedup of 11/4 = 2.75 is more than the increase in number of processors (2)
		- It's important to remmeber that this anomaly is due to the specific example and does not represent the general
		- The average execution time of the single processor example is (3+4+6+7+10+11+13+14)/8 = 8.5
		- The average execution time of the dual processor example is (4+4+6+7)/4 = 5 
		- Thus the speedup for the average case is 8.5/5 = 1.7 which is not superlinear

6. 
	- hitrates: p = [.5, .7, .9, 1]
	- latencies(ns): t = [2, 5, 20, 100]
	- average access time: p[0]*t[0] + (1-p[0])*p[1]*(t[0]+t[1]) + (1-p[0])*(1-p[1])*p[2]*(t[0]+t[1]+t[2]) + (1-p[0])*(1-p[1])*(1-p[2])*(t[0]+t[1]+t[2]+t[3])
	- this was derived as the sum of the probailities of each cache missing multiplied by the latency of the corresponding access in order to give the expected value
	- 9 ns (via calculator)
	
7.
	- a. O(M * N) - linear
	- b. O(M * N / P) - linear
	- c. S = O(M * N) / O(M * N / P) = P * O(1)
	- d. 
		+ The algorithm effectively reduces the time complexity assuming M or N is large.
		+ If the dimensions of the matrix aren't large the thread overhead would be more than performance gain
