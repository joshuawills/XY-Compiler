#include <stdio.h>

int main()
{
    char *hello[] = {"a", "b", "c"};
    hello[0] = "hello";
    int i = 0;
    while (i <= 2)
{
    printf("%s\n", hello[i]);
    i++;}
        return 0;
}