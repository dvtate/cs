.data
	prompt:		.asciiz		"\nPlease input a String: "
	inputString:    .space 64
	stack: 		.space 64
	
.globl	main
	
.text
	main:
		li $v0, 4		# print string system call
		la $a0, prompt		# prompt: "Please input a String: "
		syscall			# issue system call	
		li $v0, 8		# read user input from keyboard
    		la $a0, inputString
    		li $a1, 64
    		syscall	
    		
		li $v0, 4		# print string system call

		jal reverse 		# call â€˜reverseâ€™ and
		li $v0, 10		# the input string is stored
		syscall			# in inputString
		
	#you need to reverse the order of the input string(size <= 64)
	#the implementation requires using the stack
	#the input string is stored in inputString
	#you can assume the length of input string are always 64
	#it's OK, your program prints empty characters
	#e.g. input "Hello World", your program need to print "dlroW olleH"

		
	##### Stack subs
	# $a1 : stack size
	# $a0 : stack head
	# $v0 : value
	#####
	
	reverse:


		li $t0, 64	# num chars
		la $t1, stack	# stack head
		li $t2, 0	# stack depth
		stack_loop:
		
			# pop
			subi $a1, $a1, 1	# 
			add $a2, $a0, $a1	#
			lb $v0, 0($a2)		# 
			
			
			# push
			addi $t2, $t2, 1
			add $a2, $t1, $t2
			sb $v0, 0($a2)
		
		
			# update loop counter
			subi $t0, $t0, 1
			bnez $t0, stack_loop

		li $v0, 4
		la $a0, stack
		syscall
		
		## Print it out char by char
		li $t0, 0
		print_loop:
		
			li $v0, 11
			la $a0, stack
			add $a0, $a0, $t0 
			lb $a0, 0($a0)
			syscall
			
			addi $t0, $t0, 1
			ble $t0, 64, print_loop
		
		
		li $v0, 4		# print string system call
		la $a0, prompt		# prompt: "Please input a String: "
		syscall			# issue system call	
		
		jr $ra
	
		
