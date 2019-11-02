	
.globl main
.text
  main:
    
    
	li $v0, 5 #syscall to read an integer
	syscall

	add $a0, $zero, $v0
	jal Factorial
	
	
	j end
    
  Factorial: #Factorial Recursive function
	subu $sp, $sp, 4
	sw $ra, 4($sp) # save the return address on stack
	beqz $a0, terminate # test for termination
	subu $sp, $sp, 4 # do not terminate yet
	sw $a0, 4($sp) # save the parameter
	sub $a0, $a0, 1 # will call with a smaller argument
	jal Factorial
			
	# after the termination condition is reached these lines
	# will be executed
	lw $t0, 4($sp) # the argument I have saved on stack
	mul $v0, $v0, $t0 # do the multiplication
	lw $ra, 8($sp) # prepare to return
	addu $sp, $sp, 8 # Iâ€™ve popped 2 words (an address and
	jr $ra # .. an argument)
		
  terminate:
	li $v0, 1 # 0! = 1 is the return value
	lw $ra, 4($sp) # get the return address
	addu $sp, $sp, 4 # adjust the stack pointer
	jr $ra # return
	
  end:

