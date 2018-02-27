rm build/main;
cc -std=c99 -Wall -Wextra -Wpedantic "main.c" -g -ggdb -o build/main;
./build/main SOME_ID
