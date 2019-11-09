.data
a:	.float	8.4      # f4
b:	.float -12.6     # f5
c:	.float 21.2	 # f6
x:	.float 1.245	 # f7
y:	.float 3.4233    # f8
.text
.globl main
main:       
	l.s $f4, a
	l.s $f5, b
	l.s $f6, c
	l.s $f7, x
	l.s $f8, y
	# a * x*x + b*y*y - c*x*y
	
	# a * x^2
	mul.s $f9, $f7, $f7
	mul.s $f9, $f9, $f4
	
	# b * y^2
	mul.s $f10, $f8, $f8
	mul.s $f10, $f10, $f5
	
	# +
	add.s $f9, $f9, $f10
	
	# c * x * y
	mul.s $f11, $f6, $f7
	mul.s $f11, $f11, $f8
	
	sub.s $f12, $f9, $f11
	
	# 8,4 * 1,245*1,245 + -12,6*3,4233*3,4233 - 21,2*1,245*3,4233 = -224,993554614
	#    looks good 
	
	# print the result
	li $v0, 2          # system call for print_double
        syscall
	
	
