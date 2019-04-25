# Kotlin Web Stack

## Problem
I don't like Spring (Boot)! Why?

* Very slow startup (Especially in integration tests)
* A lot of magic with reflection and path scanning. Which
leads to problems which are very hard to debug and often
require to look into bad or not documented library code.
* Overriding Beans for tests is not intuitiv and doesn't 
work in all cases

But I love Kotlin! In this repo I want to try
an alternative web stack to develop easy to maintain
and lightweight applications.

## Goals
I have the following goals
* Avoiding reflection, path scanning and annotations if possible.
We want to be as explicit as possible and every part of the application
should be reachable from the main method. During development, this should lead to as few 
surprises as possible and allows developers to concentrate
on the business logic itself.
* Favor composition of small libraries over the usage of big frameworks that
may lack flexibility. The modular structure of the architecture should allow
to change libraries with minimal effort.
* Errors should be catched during compile time. Complex annotations or magic
strings should be avoided at all costs.

## Stack
In this section I want to describe the stack I choose and explain my decisions. 

### Dependency Injection
For dependency injection I chose [Koin](https://github.com/InsertKoinIO/koin).
It shares some of the same goals that this project has. It is very lightweight
and can be used without any Reflection. It allows the creation of modules, which
is perfect for the modular design that I desire.

### Web
As a web technology I chose [Javalin](https://javalin.io/). It is also 
very lightweight and I like the API, which is very similar to [express](https://expressjs.com/).
It also doesn't require any reflection or annotations.

A feature that I miss, is the generation of swagger definitions from the 
source code (similar to [Springfox](https://springfox.github.io/springfox/)).
But I started to implement the logic by myself. As I concentrate on my own
use cases, the required time investment for the implementation is just a few days.

