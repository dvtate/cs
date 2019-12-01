.data 
contains_msg:	.asciiz "contains("
delete_msg:		.asciiz "delete("
result_msg: 	.asciiz ") = "
debug_fount_msg: .asciiz "foundit"
newline:    .asciiz "\n"
space:	    .asciiz " "

.text 
.globl main


main:
	li $a0, 72		# struct bTree* btree = newTree(72);
	jal newTree		#
	move $t0, $v0		# copy the address of bTree to t0
	move $s0, $v0		# copy address of tree to s0

	move $a0, $t0		# add(root, 12);
	li $a1, 12		#
	subu $sp, $sp, 4	# adjust the stack pointer 
	sw $t0, 0($sp) 		# save the parameter 
	jal addNodeToTree	#
	lw $t0, 0($sp) 		# get the return address
	addu $sp, $sp, 4 	# adjust the stack pointer 
	
	move $a0, $t0		# add(root, 52);
	li $a1, 52		#
	subu $sp, $sp, 4	# adjust the stack pointer 
	sw $t0, 0($sp) 		# save the parameter 
	jal addNodeToTree	#
	lw $t0, 0($sp) 		# get the return address
	addu $sp, $sp, 4 	# adjust the stack pointer 	
	
	move $a0, $t0		# add(root, 87);
	li $a1, 87		#
	subu $sp, $sp, 4	# adjust the stack pointer 
	sw $t0, 0($sp) 		# save the parameter 
	jal addNodeToTree	#
	lw $t0, 0($sp) 		# get the return address
	addu $sp, $sp, 4 	# adjust the stack pointer 
	
	move $a0, $t0		# add(root, 18);
	li $a1, 18		#
	subu $sp, $sp, 4	# adjust the stack pointer 
	sw $t0, 0($sp) 		# save the parameter 
	jal addNodeToTree	#
	lw $t0, 0($sp) 		# get the return address
	addu $sp, $sp, 4 	# adjust the stack pointer 
	
	move $a0, $t0		# add(root, 49);
	li $a1, 49		#
	subu $sp, $sp, 4	# adjust the stack pointer 
	sw $t0, 0($sp) 		# save the parameter 
	jal addNodeToTree	#
	lw $t0, 0($sp) 		# get the return address
	addu $sp, $sp, 4 	# adjust the stack pointer 
			
	move $a0, $t0		# printTree(bTree);
	subu $sp, $sp, 4	# adjust the stack pointer 
	sw $t0, 0($sp) 		# save the parameter 
	jal printTree		#
	lw $t0, 0($sp) 		# get the return address
	addu $sp, $sp, 4 	# adjust the stack pointer 

	# printf("contains(%d) = %d", 12, contains(12))
	la $a0, contains_msg
	li $v0, 4
	syscall
	li $a0, 12
	li $v0, 1
	syscall
	la $a0, result_msg
	li $v0, 4
	syscall
	move $a0, $s0
	li $a1, 12
	jal contain
	
	move $a0, $v0
	li $v0, 1
	syscall



# you can add more test cases here
	
	li $v0, 10		# exit the program
	syscall

# construct a tree
# 
# input: $a0 the value of the root node
#
# output: $v0 the address of the tree

newTree:
	move $t0, $a0		# copy the value of the root node to t0
	
	subu $sp, $sp, 4 	# adjust the stack pointer 
	sw $ra, 0($sp) 		# save the return address on stack 
	subu $sp, $sp, 4	# adjust the stack pointer 
	sw $a0, 0($sp) 		# save the parameter 
	
	li $a0, 3		# (struct Node*)malloc(sizeof(struct Node));
	jal malloc		# create a 3 words length space for root node
	move $t1, $v0		# copy the address of root node to t1
	
	sw $t0, 0($t1)		#root->value = value;
	sw $zero, 8($t1)		#root->rightChild = NULL;
	sw $zero, 4($t1)		#root->leftChild = NULL;
	
	li $a0, 1		# (struct bTree*)malloc(sizeof(struct bTree));
	jal malloc		# create a 1 word length space for bTree
	move $t2, $v0		# copy the address of root node to t2
	
	sw $t1, 0($t2)		# bTree->root = root;
	
	move $v0, $t2		# set the return value to v0
	lw $a0, 0($sp) 		# get the parameter 
	addu $sp, $sp, 4		# adjust the stack pointer 
	lw $ra, 0($sp) 		# get the return address
	addu $sp, $sp, 4 	# adjust the stack pointer 
	jr $ra
	
# add a node to tree
# 
# input: $a0 the address of the tree
#	 $a1 the value you want to add to the tree

addNodeToTree:
    # prep fxn call
	subu $sp, $sp, 4 	# adjust the stack pointer 
	sw $ra, 0($sp) 		# save the return address on stack 
	subu $sp, $sp, 4	# adjust the stack pointer 
	sw $a0, 0($sp) 		# save the parameter 
	
    # call fxn
	lw $a0, 0($a0)		# addNode(bTree->root);
	jal addNode
	
    # cleanup fxn call
	lw $a0, 0($sp) 		# get the parameter 
	addu $sp, $sp, 4		# adjust the stack pointer 
	lw $ra, 0($sp) 		# get the return address
	addu $sp, $sp, 4 	# adjust the stack pointer 
	
	jr $ra


# please implement this method
#
# add a node to tree
# 
# input: $a0 the address of the root node
#	 $a1 the value you want to add to the tree
#
# hint: you need to write a recursive call here
addNode:
    # procedural is just better

    # li $t1, 0           # prev=NULL
    move $t2, $a0       # n = root node

    # this is a do-while loop
    # it's acceptable here because we're gauranteed the root node has a value
	addNode_while:                          # do {
        move $t1, $t2                       # prev = n
		lw $t0, 0($t2)      				# get value from node

        # 3 case if statement
        blt $t0, $a1, addNode_while_if_lt
        bgt $t0, $a1, addNode_while_if_gt

        # else: n->value == value
        jr $ra  # already in tree, return

        addNode_while_if_gt:                # if (n->value > value)
            lw $t2, 4($t2)                  # 	n = n->leftChild
            j addNode_while_cond

        addNode_while_if_lt:                # if (n->value < value)
            lw $t2, 8($t2)                  # 	n = n->rightChild
            j addNode_while_cond

        addNode_while_cond:                 # } while (n != NULL)
            bnez $t2, addNode_while

    # allocate memory for new node
    subu $sp, $sp, 4 	# adjust the stack pointer 
	sw $ra, 0($sp) 		# save the return address on stack 

    li $a0, 3		# (struct Node*)malloc(sizeof(struct Node));
	jal malloc		# create a 3 words length space for root node
    # address is in $v0
	
    sw $a1, 0($v0)		    #newNode->value = value;
	sw $zero, 8($v0)		#newNode->rightChild = NULL;
	sw $zero, 4($v0)		#newNode->leftChild = NULL;

	lw $ra, 0($sp) 		# get the return address
	addu $sp, $sp, 4 	# adjust the stack pointer 

    # link it with tree
    blt $t0, $a1, addNode_ins_if_lt
    addNode_ins_if_gt:
        sw $v0, 4($t1)
        jr $ra
    addNode_ins_if_lt:
        sw $v0, 8($t1)
        jr $ra
	

# please implement this method
#
# delete a node from tree, you still need to use free() to 
# release the memory when a node is removed
# 
# input: $a0 the address of the tree
#	 $a1 the value you want to delete from the tree
# output: $a0 reference update
# hint: like addNodeToTree or printTree method, you may need 
#	to create another recursive method to implement delete
delete:

	bnez $a0, delete_cond			# if (root==null) return NULL
	jr $ra
	delete_cond:
		lw $t1, 0($a0)
		blt $a1, $t1, delete_cond_l	# if value < root->value
		bgt $a1, $t1, delete_cond_r	# if value > root->value
		j delete_cond_found			# else, we found it!

	delete_cond_l:
		subu $sp, $sp, 4 	# adjust the stack pointer 
		sw $ra, 0($sp) 		# save the return address on stack 
		subu $sp, $sp, 4	# adjust the stack pointer 
		sw $a0, 0($sp) 		# save $a0
		
		jal delete

		lw $a0, 0($sp) 		# get the parameter 
		addu $sp, $sp, 4	# adjust the stack pointer 
		lw $ra, 0($sp) 		# get the return address
		addu $sp, $sp, 4 	# adjust the stack pointer 
		
		sw $a3, 8($a0)
		
		jr $ra
		
	delete_cond_r:
		subu $sp, $sp, 4 	# adjust the stack pointer 
		sw $ra, 0($sp) 		# save the return address on stack 
		subu $sp, $sp, 4	# adjust the stack pointer 
		sw $a0, 0($sp) 		# save $a0
		
		jal delete
		move $a3, $a0

		lw $a0, 0($sp) 		# get the parameter 
		addu $sp, $sp, 4	# adjust the stack pointer 
		lw $ra, 0($sp) 		# get the return address
		addu $sp, $sp, 4 	# adjust the stack pointer 
	
		sw $a3, 4($a0)
		
		jr $ra

	delete_cond_found:
		lw $t0, 4($a0)
		lw $t1, 8($a0)
		and $t3, $t0, $t1					# L && R
		bnez $t3, delete_cond_found_2c
		or $t2, $t0, $t1					# L || R
		bnez $t2, delete_cond_found_lc

		delete_cond_found_nc: # no children
			move $a0, $t0	# free(root)
			li $a1, 3
			jal free

			li $a0, 0		# root = NULL
			jr $ra 			# return root
		delete_cond_found_lc: # left child
			bnez $t1, delete_cond_found_rc	# !!R

			jr $ra
		delete_cond_found_rc: # right child

			jr $ra
		delete_cond_found_2c: # left and right children
			jr $ra




# input: $a0: root ptr
# output: $a0: subnode ptr with lowest value
# uses: $t0: value holder
findMin:

	lw $t0, 4($a0)				# v = n.left
	j findMin_while_cond		# while
	findMin_while:				# {
		move $a0, $t0				# n = v
		lw $t0, 4($a0)				# v = n.left
	findMin_while_cond:
		bnez $t0, findMin_while # } while (v != NULL);
	jr $ra						# return n



# please implement this method
#
# check whether the tree contains the input value
# 
# input: $a0 the address of the tree
#	 $a1 the value you want to searce from the tree
#
# output: $v0 | 1:if the tree contains this value; 
#	      | 0: if the tree doesn't contain this value
#
# hint: like addNodeToTree or printTree method, you may need 
#	to create another recursive method to implement contain

contain:
    move $t2, $a0       # n = root node

    # this is a do-while loop
    # it's acceptable here because we're gauranteed the root node has a value
	contain_while:                          # do {
        lw $t0, 0($t2)      				# get value from node

		move $a0, $t0
		li $v0, 1
		syscall

        # 3 case if statement
        blt $t0, $a1, contain_while_if_lt
        bgt $t0, $a1, contain_while_if_gt

		li $v0, 1
        # else: n->value == value
        jr $ra

        contain_while_if_gt:                # if (n->value > value)
            lw $t2, 4($t2)                  # 	n = n->leftChild
            j contain_while_cond

        contain_while_if_lt:                # if (n->value < value)
            lw $t2, 8($t2)                  # 	n = n->rightChild
            # j contain_while_cond

        contain_while_cond:                 # } while (n != NULL)
            bnez $t2, contain_while
	
	# not in tree
	li $v0, 0
	jr $ra

# print the tree
# 
# input: $a0 the address of the tree

printTree:
	subu $sp, $sp, 4 	# adjust the stack pointer 
	sw $ra, 0($sp) 		# save the return address on stack 
	subu $sp, $sp, 4	# adjust the stack pointer 
	sw $a0, 0($sp) 		# save the parameter 
	
	lw $a0, 0($a0)		# printInorder(bTree->root);
	jal printInorder
	
	lw $a0, 0($sp) 		# get the parameter 
	addu $sp, $sp, 4	# adjust the stack pointer 
	lw $ra, 0($sp) 		# get the return address
	addu $sp, $sp, 4 	# adjust the stack pointer 
	
	la $a0, newline		# printf("\n");
	li $v0, 4
	syscall
	
	jr $ra

# print the tree in order
# 
# input: $a0 the address of the root node
printInorder:
	move $t0, $a0
	bne $t0, 0, else      	# if $node == null  return
	jr $ra              	# to return
    
else:
	subu $sp, $sp, 4 	# adjust the stack pointer 
	sw $ra, 0($sp) 		# save the return address on stack 
	subu $sp, $sp, 4	# adjust the stack pointer 
	sw $t0, 0($sp)		# save the parameter on stack 
	
	lw $a0, 4($t0)		# printInorder(node->leftChild); 
	jal printInorder
	
	lw $t0, 0($sp) 		# get the parameter 
	addu $sp, $sp, 4	# adjust the stack pointer 
	lw $ra, 0($sp) 		# get the return address
	addu $sp, $sp, 4 	# adjust the stack pointer 
	
	lw $a0, 0($t0)		# print node->value
	li $v0, 1
	syscall   
	la $a0, space
	li $v0, 4
	syscall
	
	subu $sp, $sp, 4 	# adjust the stack pointer 
	sw $ra, 0($sp) 		# save the return address on stack 
	subu $sp, $sp, 4	# adjust the stack pointer 
	sw $t0, 0($sp)		# save the parameter on stack 
	
	lw $a0, 8($t0)		# printInorder(node->rightChild); 
	jal printInorder
	
	lw $t0, 0($sp) 		# get the parameter 
	addu $sp, $sp, 4	# adjust the stack pointer 
	lw $ra, 0($sp) 		# get the return address
	addu $sp, $sp, 4 	# adjust the stack pointer 
	
	jr $ra

# dynamically allocated memory block
#
# input: $a0 the size of memory block in words
# 
# output: $v0 the address to the allocated memory

malloc:           
	mul $a0, $a0, 4 # the number of bytes you need.
	li $v0,9      # allocate memory
	syscall          
	
	jr $ra

# please implement this method
#	
# free the memory block, here we can simply set
# all bits of the memory blocks to be zero
#
# input: $a0 the address of memory block
#	 $a1 the size of memory block in words
free:
	# while ($a1--) *$a0++ = 0;
	j free_loop_cond
	free_loop:
		sw $zero, 0($a0)		# *$a0 = 0
		addi $a0, $a0, 4		# $a0 += sizeof(word)
		subi $a1, $a1, 1		# $a1--
	free_loop_cond:
		bnez $a1, free_loop
	jr $ra
