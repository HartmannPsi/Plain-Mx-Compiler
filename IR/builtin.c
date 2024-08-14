#define bool _Bool
//#define size_t unsigned long

int printf(const char *pattern, ...);
int scanf(const char *format, ...);
int sprintf(char *buffer, const char *format, ...);
int sscanf(const char *buffer, const char *format, ...);
unsigned long strlen(const char *str);
char *strcat(char *dest, const char *src);
int strcmp(const char *lhs, const char *rhs);
char *strcpy(char *dest, const char *src);
void *malloc(unsigned long size);
void free(void *ptr);
void *memcpy(void *dest, const void *src, unsigned long count);
void *memset(void *dest, int ch, unsigned long count);

void print(const char *str) { printf("%s", str); }

void println(const char *str) { printf("%s\n", str); }

void printInt(int n) { printf("%d", n); }

void printlnInt(int n) { printf("%d\n", n); }

const char *getString() {
  static char str[300];
  scanf("%s", str);
  return str;
}

int getInt() {
  int n;
  scanf("%d", &n);
  return n;
}

const char *toString(int n) {
  static char str[300];
  sprintf(str, "%d", n);
  return str;
}

int string_length(const char *str) { return strlen(str); }

const char *string_substring(const char *str, int left, int right) {
  static char sub[300];
  int len = right - left;
  for (int i = 0; i < len; i++) {
    sub[i] = str[left + i];
  }
  sub[len] = '\0';
  return sub;
}

int string_parseInt(const char *str) {
  int n;
  sscanf(str, "%d", &n);
  return n;
}

int string_ord(const char *str, int pos) { return str[pos]; }

const char *string_add(const char *lhs, const char *rhs) {
  static char str[300];
  strcpy(str, lhs);
  strcat(str, rhs);
  return str;
}

bool string_eq(const char *lhs, const char *rhs) {
  return strcmp(lhs, rhs) == 0;
}

bool string_ne(const char *lhs, const char *rhs) {
  return strcmp(lhs, rhs) != 0;
}

bool string_lt(const char *lhs, const char *rhs) {
  return strcmp(lhs, rhs) < 0;
}

bool string_le(const char *lhs, const char *rhs) {
  return strcmp(lhs, rhs) <= 0;
}

bool string_gt(const char *lhs, const char *rhs) {
  return strcmp(lhs, rhs) > 0;
}

bool string_ge(const char *lhs, const char *rhs) {
  return strcmp(lhs, rhs) >= 0;
}

void *array_malloc(int size) {
  void *ret = malloc(4 + 4 * size);
  ((int *)ret)[0] = size;
  return (void *)(1 + (int *)ret);
}

int array_size(void *arr) { return ((int *)arr)[-1]; }