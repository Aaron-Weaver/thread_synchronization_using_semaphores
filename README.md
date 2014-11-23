=================Program 2 Write-up===================

Name: Aaron Weaver
Student Name: waaronl
Student ID: 0211
Date: 2014-10-23
Instructor: Dr. A. T. Burrell
Class: CS4323 Des. & Impl. of Operating Systems
Assignment: Programming Homework 2

======================================================

Problem Description:

Create a program that spawns an arbitrary, user defined,
amount of threads that will then synchronize between
themselves and open valves based on the following rules:
1. Even numbers will be randomly chosen to open those
valves before any odd valves can be opened.
2. If a magic number greater than 0 is chosen, then the
modulus of the valve and magic number equating to 0 will
give those valves that pass priority over the rest of
the evens. If their are multiple valves that pass, those
that have priority will be opened in a random order before
all other evens.
3. After ALL of the evens have opened, then the odds may
begin opening.
4. The odds must open in a descending order, in concurrent
groups of however many wrenches the user entered. Each
group of wrenches will open valves in a random order,
but the valves will still maintain descending order based
on the groups.
5. After all valves have been opened, the threads must
end, and the process must terminate.

```sh
For a more detailed look at the program
see the source code and its comments.
```
======================================================

Running Instructions:

Running make will do 2 things:

1. Compile all java classes related to the program
    (Main.java, SemaphoresUtil.java, and Valve.java)
2. Run Main.

There are a few different commands that can be
run with the makefile, they are listed below:

make run: runs the program from Main
make classes: compiles all java files
make clean: removes all .class files

If you would like to run without the make file
just compile using javac on all of the java files
in the program directory and run java Main.

Questions?
E-mail me at: waaronl@okstate.edu

======================================================

Collaboration Details:

Some small collaboration was had with a few of my
classmates. Chris Portokalis and I discussed the
program at length many times. I also spoke with 
Victoria and Gabrielle on various other occasions about
this program. I did not use anyone else's code
but my own. I used the book (Chapter 5) quite a bit
to get a better idea about semaphores, and thread
synchronization in general.

None of my code is copied from any source, and is
owned only by me.

======================================================

Problem Areas:

Thread synchronization was, and is a very tricky
subject to tackle, given that It has something I have
never had to think about prior. Many of my problems
came from using semaphores to block properly so that
any of my critical sections would not be accessed while
another thread was using them. I spent quite a few hours
(somewhere between 12 and 14) on just figuring out
these issues. Thinking synchronously between an arbitrary
amount of threads was incredibly difficult, but through
that struggle I feel that I have learned quite a bit
about it.

=======================================================

Notes on My Solution:

I enjoy getting feedback on any program I write,
and schoolwork is no different. If you have any
comments on my code, I would love to hear them!
If there are any questions on the functionality
of the code, or if it isn't working for you,
feel free to e-mail me at: waaronl@okstate.edu.
I was able to get it to compile and run properly
on the CSX machine, and I confirmed the output
with a few test cases that I will provide with my
final solution.

A list of the required Java files are as folows:

-Main.java
-SemaphoresUtil.java
-Valve.java

All of these items are also mentioned in the makefile
and will be compiled when the "make" command is run
in the program's directory.

=======================================================

Thank you,
      Aaron Weaver
