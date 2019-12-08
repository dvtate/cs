#include <stdio.h>
#include <stdlib.h>
#include <string.h>


char aStack[20];
int aTop = -1;

//Please implement all the functions
int isEmpty (char stack[], int* pTop) {
	// This assignment was extremely vague and poorly defined
	// imma do obfusicated C challenge :D
	return !*stack; // pls add `inline` to fxn def
}
int isFull (char stack[], int* pTop) {
	while (stack < pTop)
		if (!*stack++)
			return 0;
	return 1;
}

//You need to throw exceptions when user try to push more char but the stack is full
char push (char stack[], int* pTop, char c) {
	// check for error
	if (isFull(stack, pTop))
		return 0; // full

	while (*stack++);			// find null terminator
	*stack = c;					// replace it w/ c
	return 'k';
}

//You need to throw exceptions when user try to pop out more char but the stack is empty
char pop (char stack[], int* pTop) {
	if (isEmpty(stack, pTop))
		return 0;
	while (*stack++);			// find null terminator
	const char r = *--stack; 	// copy top
	*stack = 0;					// delete top
	return r;					// return old top
}
//You need to throw exceptions when the stack is empty
char top (char stack [], int* pTop) {
	if (isEmpty(stack, pTop))
		return 0;
	while (*stack++);	// find top+1
	return *--stack;	// ret top
}

//you need to reverse the order of the input string(size <= 20)
//the implementation requires using the stack
//e.g. input "Hello World", your program need to print "dlr"
void reverse(char* string, int size){
	// but I thought all functions use the stack?

	char s[size];
	char c;
	while (c = pop(string, string + size))
		if (!push(s, s + size, c)) {
			printf("reverse: Error: stack too small\n");
			return;
		}

	for (c = 0; c < size; c++)
		string[c] = s[c];

}

//you need to check whether the input expression's brackets is balanced
//return 1:true 0:false
//the implementation requires using the stack
//e.g. input "(a+b)*c)" is not balanced and "((a+b)/c)" is balanced
int balancedbrackets(char* string, int size){
	// only checking parens
	char s[size];
	int* e = s + size;

	for (unsigned i = 0; i < size; i++)
		if (string[i] == '(' && !push(s, e, string[i]))
			return 0; // string only has open parens
		else if (string[i] == ')' && pop(s, e) != '(')
			return 1;
	return !isEmpty(s, e);
}

int main () {
	char* string = "Hello World";
	printf("The reverse of %s is ", string);
	reverse(string, strlen(string));

	char* expression = "((a+b)/c)";
	int result = balancedbrackets(expression, sizeof(expression));
	printf("The expression %s is ", expression);
	if(result){
		printf("balanced\n");
	} else {
		printf("not balanced\n");
	}
	return 0;
}

