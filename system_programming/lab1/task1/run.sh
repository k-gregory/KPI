rm build/main;
cc -std=c99 -Wall -Wextra -Wpedantic "main.c" -g -ggdb -o build/main;
./build/main build/sample build/output_file
