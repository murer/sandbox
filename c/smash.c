#include <stdio.h>
#include <stdlib.h>

int main() {
	while(1) {
		malloc(1024);
		fork();
	}
	return 0;
}

