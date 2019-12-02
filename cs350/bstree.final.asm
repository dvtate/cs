.data 

	added_msg: 		.asciiz "Added Nodes: "
	contains_msg:	.asciiz "\ncontains("
	delete_msg:		.asciiz "\nafter delete "
	findMin_msg:	.asciiz "\nfindMin: "
	sorted_msg: 	.asciiz "\nsorted: "
	result_msg: 	.asciiz ") = "
	newline:    	.asciiz "\n"
	space:	    	.asciiz " "

	# left: 			.asciiz "left\n"
	# right: 			.asciiz "right\n"
	# no_children:	.asciiz "no children\n"

.text 
.globl main


main:
	li $a0, 72		# struct bTree* btree = newTree(72);
	jal newTree		#
	move $t0, $v0		# copy the address of bTree to t0

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
	

	# 45, 82, 34, 73, 21
	move $a0, $t0		# add(root, 45);
	li $a1, 45		#
	subu $sp, $sp, 4	# adjust the stack pointer 
	sw $t0, 0($sp) 		# save the parameter 
	jal addNodeToTree	#
	lw $t0, 0($sp) 		# get the return address
	addu $sp, $sp, 4 	# adjust the stack pointer 


	move $a0, $t0		# add(root, 82);
	li $a1, 82		#
	subu $sp, $sp, 4	# adjust the stack pointer 
	sw $t0, 0($sp) 		# save the parameter 
	jal addNodeToTree	#
	lw $t0, 0($sp) 		# get the return address
	addu $sp, $sp, 4 	# adjust the stack pointer 	


	move $a0, $t0		# add(root, 34);
	li $a1, 34		#
	subu $sp, $sp, 4	# adjust the stack pointer 
	sw $t0, 0($sp) 		# save the parameter 
	jal addNodeToTree	#
	lw $t0, 0($sp) 		# get the return address
	addu $sp, $sp, 4 	# adjust the stack pointer 	

	move $a0, $t0		# add(root, 73);
	li $a1, 73		#
	subu $sp, $sp, 4	# adjust the stack pointer 
	sw $t0, 0($sp) 		# save the parameter 
	jal addNodeToTree	#
	lw $t0, 0($sp) 		# get the return address
	addu $sp, $sp, 4 	# adjust the stack pointer 	

	move $a0, $t0		# add(root, 21);
	li $a1, 21		#
	subu $sp, $sp, 4	# adjust the stack pointer 
	sw $t0, 0($sp) 		# save the parameter 
	jal addNodeToTree	#
	lw $t0, 0($sp) 		# get the return address
	addu $sp, $sp, 4 	# adjust the stack pointer 	


	la $a0, added_msg
	li $v0, 4
	syscall


	move $a0, $t0		# printTree(bTree);
	subu $sp, $sp, 4	# adjust the stack pointer 
	sw $t0, 0($sp) 		# save the parameter 
	jal printTree		#
	lw $t0, 0($sp) 		# get the return address
	addu $sp, $sp, 4 	# adjust the stack pointer 

	subu $sp, $sp, 4	# adjust the stack pointer 
	sw $t0, 0($sp) 		# save the parameter 

	# test cases for contain subroutine

	# printf("\ncontains(%d) = %d", 12, contains(12))
	la $a0, contains_msg # print 'contains('
	li $v0, 4
	syscall
	li $a0, 12			 # print 12
	li $v0, 1
	syscall
	la $a0, result_msg	 # print ') = '
	li $v0, 4
	syscall
	move $a0, $t0
	li $a1, 12
	jal contain			 # r = contain(12)
	
	move $a0, $v0
	li $v0, 1
	syscall				 # print r

	lw $t0, 0($sp) 	
	addu $sp, $sp, 4 	# adjust the stack pointer 

	# printf("\ncontains(%d) = %d", 33, contains(33))
	la $a0, contains_msg
	li $v0, 4
	syscall
	li $a0, 33
	li $v0, 1
	syscall
	la $a0, result_msg
	li $v0, 4
	syscall
	move $a0, $t0


	subu $sp, $sp, 4	# adjust the stack pointer 
	sw $t0, 0($sp) 		# save the parameter 

	li $a1, 33
	jal contain
	
	move $a0, $v0
	li $v0, 1
	syscall

	lw $t0, 0($sp)
	addu $sp, $sp, 4 	# adjust the stack pointer 

	# test cases for delete subroutine


	la $a0, findMin_msg
	li $v0, 4
	syscall

	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $a0, 0($t0)
	jal findMin
	lw $t0, 0($sp)
	addu $sp, $sp, 4

	move $a0, $t1
	li $v0, 1
	syscall

	# printf("\ndelete %d\n"
	la $a0, delete_msg
	li $v0, 4
	syscall
	li $a0, 49
	li $v0, 1
	syscall
	la $a0, newline		# printf("\n");
	li $v0, 4
	syscall
	
	subu $sp, $sp, 4	# adjust the stack pointer 
	sw $t0, 0($sp) 		# save the parameter 

	move $a0, $t0
	li $a1, 49
	jal delete

	lw $t0, 0($sp)		# retrive parameter
	addu $sp, $sp, 4 	# adjust the stack pointer 
	
	move $a0, $t0		# printTree(bTree);
	subu $sp, $sp, 4	# adjust the stack pointer 
	sw $t0, 0($sp) 		# save the parameter 
	jal printTree		#
	lw $t0, 0($sp) 		# get the return address
	addu $sp, $sp, 4 	# adjust the stack pointer 


	# printf("\ndelete %d\n"
	la $a0, delete_msg
	li $v0, 4
	syscall
	li $a0, 18
	li $v0, 1
	syscall
	la $a0, newline		# printf("\n");
	li $v0, 4
	syscall
	
	subu $sp, $sp, 4	# adjust the stack pointer 
	sw $t0, 0($sp) 		# save the parameter 

	move $a0, $t0
	li $a1, 18
	jal delete

	lw $t0, 0($sp)		# retrive parameter
	addu $sp, $sp, 4 	# adjust the stack pointer 
	
	move $a0, $t0		# printTree(bTree);
	subu $sp, $sp, 4	# adjust the stack pointer 
	sw $t0, 0($sp) 		# save the parameter 
	jal printTree		#
	lw $t0, 0($sp) 		# get the return address
	addu $sp, $sp, 4 	# adjust the stack pointer 




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
#	 $a1 the value you want to delete from the tree   (const)

# hint: like addNodeToTree or printTree method, you may need 
#	to create another recursive method to implement delete
delete:

	
    # prep fxn call
	subu $sp, $sp, 4 	# adjust the stack pointer 
	sw $ra, 0($sp) 		# save the return address on stack 
	subu $sp, $sp, 4 	# adjust the stack pointer 
	sw $a0, 0($sp) 		# save the return address on stack 
	
    # call fxn
	# imo its kinda pointless having a separate structure for the tree
	lw $a0, 0($a0)		# deleteNode(bTree->root);
	jal deleteNode
	move $a3, $a0

	lw $a0, 0($sp)
	addu $sp, $sp, 4
    # cleanup fxn call
	lw $ra, 0($sp) 		# get the return address
	addu $sp, $sp, 4 	# adjust the stack pointer 

	# update root
	sw $a3, 0($a0)

	jr $ra


# input:
#	$a0: address of node
#	$a1: value to be deleted
# output: $a0 reference update
deleteNode:
	bnez $a0, deleteNode_cond			# if (root==null) return NULL
	jr $ra
	deleteNode_cond:
		lw $t1, 0($a0)


		
		blt $a1, $t1, deleteNode_cond_l	# if value < root->value
		bgt $a1, $t1, deleteNode_cond_r	# if value > root->value
		j deleteNode_cond_found			# else, we found it!

	deleteNode_cond_l:
		subu $sp, $sp, 4 	# adjust the stack pointer 
		sw $ra, 0($sp) 		# save the return address on stack 
		subu $sp, $sp, 4	# adjust the stack pointer 
		sw $a0, 0($sp) 		# save $a0
		
		lw $a0, 4($a0)
		jal deleteNode
		move $a3, $a0

		# la $a0, left
		# li $v0, 4
		# syscall

		lw $a0, 0($sp) 		# get the parameter 
		addu $sp, $sp, 4	# adjust the stack pointer 
		lw $ra, 0($sp) 		# get the return address
		addu $sp, $sp, 4 	# adjust the stack pointer 
		
		sw $a3, 4($a0)
		
		jr $ra
		
	deleteNode_cond_r:
		subu $sp, $sp, 4 	# adjust the stack pointer 
		sw $ra, 0($sp) 		# save the return address on stack 
		subu $sp, $sp, 4	# adjust the stack pointer 
		sw $a0, 0($sp) 		# save $a0
		

		lw $a0, 8($a0)
		jal deleteNode
		move $a3, $a0

		# la $a0, right
		# li $v0, 4
		# syscall


		lw $a0, 0($sp) 		# get the parameter 
		addu $sp, $sp, 4	# adjust the stack pointer 
		lw $ra, 0($sp) 		# get the return address
		addu $sp, $sp, 4 	# adjust the stack pointer 
	
		sw $a3, 8($a0)
		
		jr $ra

	deleteNode_cond_found:
		lw $t0, 4($a0)						# L = *((word*)n + 4)
		lw $t1, 8($a0)						# R = *((word*)n + 8)
		and $t3, $t0, $t1					# L && R
		bnez $t3, deleteNode_cond_found_2c
		bnez $t0, deleteNode_cond_found_lc		# L && !R
		bnez $t1, deleteNode_cond_found_rc		# R && !L
		# else (!L && !R)

		deleteNode_cond_found_nc: # no children
			#move $a0, $t0	# free(root)
			
			subu $sp, $sp, 4 	# adjust the stack pointer 
			sw $ra, 0($sp) 		# save the return address on stack 
			li $a1, 3
			jal free
			lw $ra, 0($sp) 		# get the return address
			addu $sp, $sp, 4 	# adjust the stack pointer 

			
			# la $a0, no_children
			# li $v0, 4
			# syscall


			li $a0, 0		# root = NULL
			jr $ra 			# return root

		deleteNode_cond_found_lc: # left child
			lw $s1, 4($a0)	# ret = root->left

			# free(root)
			subu $sp, $sp, 4 	# adjust the stack pointer 
			sw $ra, 0($sp) 		# save the return address on stack 
			li $a1, 3
			jal free
			lw $ra, 0($sp) 		# get the return address
			addu $sp, $sp, 4 	# adjust the stack pointer 

			# return ret
			move $a0, $s1 
			jr $ra

		deleteNode_cond_found_rc: # right child
			lw $s1, 8($a0)	# ret = root->right

			# free(root)
			subu $sp, $sp, 4 	# adjust the stack pointer 
			sw $ra, 0($sp) 		# save the return address on stack 
			li $a1, 3
			jal free
			lw $ra, 0($sp) 		# get the return address
			addu $sp, $sp, 4 	# adjust the stack pointer 

			# return ret
			move $a0, $s1 
			jr $ra

		deleteNode_cond_found_2c: # left and right children (tree manipulation o god)

			lw $t3, 8($a0)			# $t3 = root->right

									# $t1 = findMin($t3)
			subu $sp, $sp, 4
			sw $ra, 0($sp)
			jal findMin
			lw $ra, 0($sp)
			addu $sp, $sp, 4

			sw $t1, 0($a0)			# root->value = $t1 
			

									# $a3 = deleteNode($t3, $t1)

			
			subu $sp, $sp, 4 	# adjust the stack pointer 
			sw $ra, 0($sp) 		# save the return address on stack 
			subu $sp, $sp, 4	# adjust the stack pointer 
			sw $a0, 0($sp) 		# save $a0
			
			move $a0, $t3
			move $a1, $t1
			jal deleteNode
			move $a3, $a0

			lw $a0, 0($sp) 		# get the parameter 
			addu $sp, $sp, 4	# adjust the stack pointer 
			lw $ra, 0($sp) 		# get the return address
			addu $sp, $sp, 4 	# adjust the stack pointer 
			jr $ra

			sw $a3, 8($a0)			# root->right = $a3



# input: $a0: root ptr
# output: $t1: lowest value
# uses: $t0: ptr holder
findMin:
	# not a do-while as that would change logic
	move $t0, $a0				# n = root
	lw $t1, 0($t0)				# v = n.value
	findMin_while:				# findMin_while:
		lw $t1, 0($t0)			#	v = n.value
		lw $t0, 4($t0)			#   if ((n = n.left
		bnez $t0, findMin_while # ) != NULL) goto findMin_while
	jr $ra						# return v



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
	# recursion still bad

	lw $t2, 0($a0)		# n = bTree->root;
	# do while is acceptable here as we're gauranteed root node is not NULL
	contain_while:						# do {
		lw $t3, 0($t2) # get value		# 	v = n->value

		blt $t3, $a1, contain_while_lt	# 	if (v < value) ...
		bgt $t3, $a1, contain_while_gt	# 	if (v > value) ...

		# else (v == value)
		# return 1
		li $v0, 1
		jr $ra

		contain_while_gt:				#... if (v < value)
			lw $t2, 4($t2)				# 		n = n->left
			j contain_while_cond
		contain_while_lt:				#... if (v > value)
			lw $t2, 8($t2)				#	 	n = n->right
			# j contain_while_cond
		contain_while_cond:
			bnez $t2, contain_while		# } while (n != NULL)
		
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
