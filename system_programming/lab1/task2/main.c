#include <sys/select.h>
#include <sys/types.h>
#include <unistd.h>

#include <stdio.h>
#include <stdlib.h>

// Max buffer size is 1024 and 1 byte for \0
static const size_t BUFF_SIZE = 1024 + 1;

int main(int argc, char *argv[]) {
  int exit_code;
  char *buffer;
  fd_set rfds;

  exit_code = EXIT_FAILURE;

  if (argc != 2) {
    fprintf(stderr, "Usage: %s ID\n", argv[0]);
    goto EXIT;
  }

  buffer = malloc(BUFF_SIZE);

  while (1) {
    int retval;
    struct timeval tv;

    tv.tv_sec = 5;
    tv.tv_usec = 0;

    FD_ZERO(&rfds);
    FD_SET(STDIN_FILENO, &rfds);

    retval = select(STDIN_FILENO + 1, &rfds, NULL, NULL, &tv);

    if (retval < 0) {
      perror("select()");
      goto CLEANUP_BUFFER;
    }

    if(retval == 0) {
      printf("%s no input for 5 sec\n", argv[1]);      
    }

    if (retval > 0) {
      // STDIN_FILENO is the only descriptor in set, no need to check FD_ISSET
      if (fgets(buffer, BUFF_SIZE, stdin) != NULL) {
        printf("%s got: %s", argv[1], buffer);
      } else { // fgets returns NULL
	puts("Exiting...");
        break;
      }
    }
  }

  exit_code = EXIT_SUCCESS;

CLEANUP_BUFFER:
  free(buffer);

EXIT:
  exit(exit_code);
}
