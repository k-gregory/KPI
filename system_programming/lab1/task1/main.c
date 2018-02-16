#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

#include <stdlib.h>
#include <stdio.h>
#include <ctype.h>
#include <errno.h>

const int BUFF_SIZE = 7;

/*
 * Reads data from src_fd, lowers it and writes to dest_fd
 * Returns count of bytes sent
 * On error, returns -1 and sets errno
 * This function doesn't close the FDs
 */
ssize_t lowering_sendfile(int src_fd, int dest_fd){
  char buffer[BUFF_SIZE];
  ssize_t read_bytes, bytes_counter;

  bytes_counter = 0;

  do {
    ssize_t i;
    char *buffer_pos, *buffer_end;

    read_bytes = read(src_fd, buffer, BUFF_SIZE);

    if(read_bytes < 0){
      return -1;
    }

    buffer_pos = buffer;
    buffer_end = buffer + read_bytes;

    for(i = 0; i < read_bytes; i++){
      /* TODO: Doesn't work with Unicode */
      buffer[i] = tolower(buffer[i]);
    }

    while(buffer_pos != buffer_end) {
      ssize_t wrote_bytes;

      wrote_bytes = write(dest_fd, buffer_pos, buffer_end - buffer_pos);
      if(wrote_bytes < 0){
	return -1;
      }

      buffer_pos += wrote_bytes;
    } 

    bytes_counter += read_bytes;
  } while(read_bytes != 0);

  return bytes_counter;
}

int main(int argc, char *argv[]){
  int exit_code;
  int in_fd, out_fd;
  ssize_t bytes_written;
  int stat_code;
  struct stat in_stat_info, out_stat_info;

  exit_code = EXIT_FAILURE;

  if(argc != 3){
    fprintf(stderr, "Usage: %s SRC DEST\n", argv[0]);
    goto EXIT;
  }

  stat_code = stat(argv[1], &in_stat_info);
  if(stat_code < 0) {
    perror("Bad input file");
    goto EXIT;
  }

  stat_code = stat(argv[2], &out_stat_info);
  if(stat_code < 0 && (errno != ENOENT)){
    perror("Bad output file");
    goto EXIT;
  } else if(stat_code == 0){/* Check that specified files are different*/
    if(
	in_stat_info.st_dev == out_stat_info.st_dev &&
	in_stat_info.st_ino == out_stat_info.st_ino
      ) {
      fputs("SRC and DEST must be different files", stderr);
      goto EXIT;
    }
  }


  in_fd = open(argv[1], O_RDONLY);
  if(in_fd < 0){
    perror("Can't open input file");
    goto EXIT;
  }

  out_fd = open(
      argv[2], 
      O_CREAT | O_WRONLY | O_TRUNC,
      S_IRUSR | S_IWUSR | S_IRGRP | S_IROTH
      );
  if(out_fd < 0){
    perror("Can't open output file");
    goto CLEANUP_IN_FD;
  }

  bytes_written = lowering_sendfile(in_fd, out_fd);
  if(bytes_written < 0){
    perror("Can't copy file contents");
    goto CLEANUP_OUT_FD;
  }
  printf("Wrote %ld bytes\n", bytes_written);
 
  exit_code = EXIT_SUCCESS;

CLEANUP_OUT_FD:
  if(close(out_fd) != 0){
    perror("Can't close output file");
  }

CLEANUP_IN_FD:
  if(close(in_fd) != 0){
    perror("Can't close input file");
  }

EXIT:
  exit(exit_code);
}
