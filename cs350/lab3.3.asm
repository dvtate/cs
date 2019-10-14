.data     
	prompt: .asciiz "Enter a Decimal Integer:\n"
	prompt2: .asciiz "Binary:\n"
	number: .word 0
	counter: .word 31
	mask: .word 1

.text                   
	main:                    
		li $v0,4             
		la $a0, prompt       
		syscall              
		li $v0, 5            
		syscall             
		sw $v0, number	    
		li $v0, 1 	    
		lw $a0, number 	    
		syscall 	     
		li $v0,4             
		la $a0, prompt2       
		syscall              

	loop:
		lw $t0, counter
		beq $t0, -1, end
		lw $t1, number
		srlv $t2, $t1, $t0
		and $t3, $t2, 1
		bne $t3, 1, f
		li $v0, 1 	    
		addi $a0, $0, 1
		syscall
		j e
	f:
		li $v0, 1 	     
		move $a0, $0
		syscall
	e:
		addi $t0, $t0, -1
		sw $t0, counter
		j loop

	end:
