.data
	promptn1: .asciiz "Please input non-negative number 1: "
	promptn2: .asciiz "Please input non-negative number 2: "
	negative: .asciiz "Negative. Try again.\n"
	result: .asciiz "The result is "
	break: .asciiz "\nNumber of Calls to Ackerman:"
	count: .word 0
.text
	main:                  
		# prompy n1
		li $v0, 4             
		la $a0, promptn1
		syscall

		# reads the value of N1 into $s0
		li $v0, 5      
		syscall               
		add $s0, $v0, $zero            

		# prompt n2
		li $v0, 4
		la $a0, promptn2 
		syscall
		
		# read n2 into $s1 
		li $v0, 5
		syscall
		add $s1, $v0, $zero

		# check for negatives
		bltz $s0, fail
		bltz $s1, fail
		j cont

	fail:
		li $v0, 4             # system call code for Print String
		la $a0, negative       # load address of negative into $a0
		syscall
		j main

	cont:
	
		# $s1 = A(n1, n2) = A($s0, $s1)
		add $a0, $s0, $zero
		add $a1, $s1, $zero
		jal Ackermann
		add $s1, $v0, $zero
	
		# print result
		li $v0, 4
		la $a0, result
		syscall
		
		li $v0, 1
		add $a0, $s1, $zero
		syscall
		
		# print number of Ackerman calls
		li $v0, 4
		la $a0, break
		syscall
		li $v0, 1
		lw $a0, count
		syscall

		j end

	Ackermann:
		# increment count
		lw $t0, count
		addi $t0, $t0, 1
		sw, $t0, count

		subu $sp, $sp, 4
		sw $ra, 4($sp) # save the return address on stack
		beqz $a0, terminate # test for termination if x = 0

		# the first and relatively simple case
		beqz $a1, case1 # if y = 0

		# the final and complicated case
		subu $sp, $sp, 4
		sw $a0, 4($sp) # push x on stack
		addi $a1, $a1, -1 # y - 1

		jal Ackermann # A(x, y-1)

		add $a1, $v0, $zero # move the result into the second argument
		lw $a0, 4($sp) # pop x from the stack
		addu $sp, $sp, 4
		addi $a0, $a0, -1 # x - 1

		jal Ackermann # A(x-1, A(x, y-1))

		# after termination condidion is reached
		lw $ra, 4($sp) # prepare to return
		addu $sp, $sp, 4
		jr $ra


	case1:
		addi $a0, $a0, -1 # x - 1
		li $a1, 1 # y = 1
		jal Ackermann # the value is already in $v0

		# after termination condidion is reached
		lw $ra, 4($sp) # prepare to return
		addu $sp, $sp, 4
		jr $ra

	terminate:
		addi $v0, $a1, 1 # y + 1 is the return value
		lw $ra, 4($sp) # get the return address
		addu $sp, $sp, 4 # adjust the stack pointer
		jr $ra # return

	end:
		li $v0, 10     # terminate program run and
		syscall
