.data
	my_array: .space 40
	initial_value: .word 2

.text
	lw $t1, initial_value
	move $t0, $0
	L:
		beq $t0, 10, end
		la $t3, my_array
		sll $t4, $t0, 2
		add $t3, $t3, $t4
		sw $t1, 0($t3)
		addi $t1, $t1, 1
		addi $t0, $t0, 1
		j L
	end:
