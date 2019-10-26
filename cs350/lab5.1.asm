.data
	prompt: .asciiz "enter number: "
	aeq: .asciiz "\na0= "
	veq: .asciiz "\nv0= "
	tzeq: .asciiz "\nt0= "
	

.globl main

.text 

    main:
	# print prompt
	li $v0, 4
	la $a0, prompt
	syscall
	
	li $v0, 5 #syscall to read an integer
	syscall

	add $a0, $zero, $v0
	jal factorial
	move $a0, $v0
	li $v0,1 
	syscall 	# print it
	j end

    factorial:
	# fac($a0) {
	#    return $a0 > 0 ? $a0 * fac($a0 - 1) : 1;
	# }

	# $a0 > 0 ? -> recursion
	bgt $a0, $zero, recursion
	
	li $v0, 1
	jr $ra 

	recursion:
		# push $a0 and $ra to stack
		addiu $sp, $sp, 8
		sw $a0, 4($sp)
		sw $ra, 0($sp)
		
		# recursion
		subi $a0, $a0, 1 	# $a0 -= 1
		jal factorial 		# $v0 = factorial(n-1)
		lw $t0, 4($sp) 		# restore original $a0 from stack

		# return
		mul $v0, $v0, $t0 	# $v0 *= $a0  
		lw $ra, 0($sp) 		# get return address from stack
		addiu $sp, $sp, -8 	# pop stack
		jr $ra # return
	
		                             
end:
