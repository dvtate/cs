from matplotlib import pyplot as plt
import math

# subdivide 
def farthest(n):
	if n > 2:
		return 1 + farthest(math.ceil((n - 1) / 2)) \
				 + farthest(math.floor((n - 1) / 2));
	return 0;

# deal with first 2 people, subdivide rest
def U(n):
	if n > 4:
		return 2 + farthest(n - 2);
	return math.ceil(n / 2);

# plot
n = 50; # int(input());
plt.plot(list(range(n)), list(map(U, range(n))));
plt.show();

