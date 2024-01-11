# The XY Programming Language

## What Is It?

The XY programming language is a hobby programming language I am developing to better understand the inner workings of compilers and also just for fun :) . 

It is a compiled programming language that generates x86_64 assembly code, which is then made into an executable using *nasm* and *ld*.

Currently this compiler is only developed for Linux operating systems, so will not work on Windows. I hope to add Windows support later however.

## Getting Started With The XY Language

To use the XY compiler yourself, make sure you have *nasm* and *ld* installed onto your desktop.
You will also require the *gradle* build kit and the JDK as well.

Regarding compatability, these are the versions I'm using to develop this compiler.

```shell
~/personal/xy_java [main *]
-> % gradle --version

------------------------------------------------------------
Gradle 7.2
------------------------------------------------------------

Build time:   2021-08-17 09:59:03 UTC
Revision:     a773786b58bb28710e3dc96c4d1a7063628952ad

Kotlin:       1.5.21
Groovy:       3.0.8
Ant:          Apache Ant(TM) version 1.10.9 compiled on September 27 2020
JVM:          11.0.21 (Ubuntu 11.0.21+9-post-Ubuntu-0ubuntu122.04)
OS:           Linux 5.15.133.1-microsoft-standard-WSL2 amd64


~/personal/xy_java [main *]
-> % java --version
openjdk 11.0.21 2023-10-17
OpenJDK Runtime Environment (build 11.0.21+9-post-Ubuntu-0ubuntu122.04)
OpenJDK 64-Bit Server VM (build 11.0.21+9-post-Ubuntu-0ubuntu122.04, mixed mode, sharing)
```

Once you've cloned this repo and installed all the necessary dependencies. You can run *gradle build* to generate a .jar file to execute programs.
The executable will be in *build/libs/xy_java-1.0-SNAPSHOT.jar*. You can execute this using the following command

```shell
java --jar build/libs/xy_java-1.0.-SNAPSHOT.jar <CL args>
```

All the command line arguments are summarised below under the "Command Line Arguments" header.

A good initial program to test out would be your hello world program, which is shown below!

```
out "hello, world!";
return 0;
```

## Command Line Arguments

| Shorthand      | Longhand | Description |
| ----------- | ----------- | ----------- |  
| -h      | --help       | Provides summary of CL arguments and use of program        |
| -r   | --run        | Will run the program after compilation        |
| -o <filename> | --out <filename> | Specify the name of the executable (defaults to *a.out*) | 
| -t | --tokens | logs to stdout a summary of all the tokens |
| -p | --parser | logs to stdout a summary of the parse tree |
| -a | --assembly | generates a .asm file instead of an executable |
| -q | --quiet | silence any non-crucial warnings |
| -l <filename> | --load | load in compiler settings (read compiler settings header for more info)| |

## Compiler Settings

*TO-DO*

## What's Left?

- handle quiet and load in config CL args
- handle functions
- develop standard libraries
- fix bugs with division
- add modulo operator
- a bunch more lol

## Credits

A large part of my initial compiler development was taken from [this youtube series](https://www.youtube.com/watch?v=vcSijrRsrY0&t=12s). You can find the source code for his project [here](https://github.com/orosmatthew/hydrogen-cpp)