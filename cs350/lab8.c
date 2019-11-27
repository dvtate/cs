#include <stdio.h>
#include <stdlib.h>


struct Node {
     int value;
     struct Node* leftChild;
     struct Node* rightChild;
};

struct bTree {
     struct Node * root;
};

//please implement all the four functions
struct bTree* newTree (int value) {
	struct bTree* ret = malloc(sizeof(struct bTree));
	if (ret == NULL)
		return NULL;

	ret->root = malloc(sizeof(struct Node));
	if (ret->root == NULL)
		return NULL;

	ret->root->value = value;
	return ret;
	
}

int add (struct bTree* root, int value) {

	struct Node* prev = NULL;
	struct Node* n = root->root;

	// find where to insert
	while (n != NULL) {
		prev = n;
		if (n->value > value)
			n = n->leftChild;
		else if (n->value < value)
			n = n->rightChild;
		else // already in the tree
			return 1;
	}
	// insert
    const struct Node* ret = malloc(sizeof(struct Node));
    ret->leftChild = NULL;
    ret->rightChild = NULL;
    ret->value = value;

	if (prev->value > value)
		prev->leftChild = ret;
	else
		prev->rightChild = ret;
	
	return 0;   
}


struct Node* findMin(struct Node* root) {
	if (root == NULL)
		return NULL;
   if (root->leftChild != NULL)
      return findMin(root->leftChild); // left tree is smaller

   return root;
}


//you need to use free() to release the memory when a node is removed
struct Node* removeNodeN(struct Node* root, int value) {
	if (root == NULL)
		return NULL;
	
	// b left
	if (value < root->value) {
		root->leftChild = removeNodeN(root->leftChild, value);

	// b right
	} else if (value > root->value) {
		root->rightChild = removeNodeN(root->rightChild, value);
	} else {

		// no children
		if (root->leftChild == NULL && root->rightChild == NULL) {
			free(root);
			root = NULL;

		// one child (right)
		} else if (root->leftChild == NULL) {
			struct Node *temp = root; // save current node as a backup
			root = root->rightChild;
			free(temp);
		
		// one child (left)
		} else if (root->rightChild == NULL) {
			struct Node *temp = root; // save current node as a backup
			root = root->leftChild;
			free(temp);

		// two children
		} else {
			struct Node *temp = findMin(root->rightChild); // find minimal value of right sub tree
			root->value = temp->value; // duplicate the node
			root->rightChild = removeNodeN(root->rightChild, temp->value); // delete the duplicate node
		}
	}
	return root; // parent node can update reference

}



//you need to use free() to release the memory when a node is removed
int removeNode (struct bTree* root, int value) {
	return removeNodeN(root->root, value) != NULL;
}

int contain (struct bTree* root, int value) {
	struct Node* n = root->root;

	// find where to insert
	while (n)
		if (n->value > value)
			n = n->leftChild;
		else if (n->value < value)
			n = n->rightChild;
		else // already in the tree
			return 1;

	return 0;
}



void printInorder(struct Node* node) 
{ 
    if (node == NULL) 
        return; 
  
    printInorder(node->leftChild); 
  

    printf("%d ", (node->value));
  

    printInorder(node->rightChild); 
}

int printTree (struct bTree* root) {
	printInorder(root->root);
	printf("\n");
	return 0;
}

int main () {
	struct bTree* root = newTree(72);
	add(root, 12);
	add(root, 52);
	add(root, 87);
	add(root, 18);
	add(root, 49);
	add(root, 43);
	add(root, 82);
	add(root, 34);
	add(root, 73);
	add(root, 21);

	printTree(root);

	if(contain(root, 73) == 1){
		printf("This tree contains 73\n");
	}else{
		printf("This tree doesn't contains 73\n");
	}
	
	if(contain(root, 22) == 1){
		printf("This tree contains 22\n");
	}else{
		printf("This tree doesn't contains 22\n");
	}

	removeNode(root, 18);
	printf("After delete 18\n");
	printTree(root);

	removeNode(root, 49);
	printf("After delete 49\n");
	printTree(root);
	return 0;
}
