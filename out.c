#include <stdio.h>

int main()
    {
    int x = 4;
    while (1)
    {
    x = x + 1;
    if ((x == 5))
    {
    continue;
    }
    printf("%d\n", x);
    if ((x == 6))
    {
    break;
    }
    }
    x = x + 1;
    return x;
    }
