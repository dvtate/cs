.data
	var1: .word 2
	var2: .word 0
	var3: .word -2019

.text
	addi $t0, $0, 2
	lw $t0, var1
	addi $t0, $0, 0
	lw $t0, var2
	addi $t0, $0, -2019
	lw $t0, var3

	lw $t1, var1
	lw $t2, var2
	beq $t1, $t2, L
	lw $t3, var1
	lw $t4, var2
	sw $t4, var1
	sw $t3, var2
	j end

	L:
		lw $t1, var1
		lw $t2, var2
		sw $t1, var3
		sw $t2, var3
	end:
