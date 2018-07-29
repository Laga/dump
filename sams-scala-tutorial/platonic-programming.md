# Introduction

Platonic Programming (PP) is the future.

Practically PP is quite simple to apply, but the motivating theory assumes some acedemic background.

Another name for PP could be Extreme Functional Programming, meaning Functional Programming practices taken to the extreme (like XP takes general practices to the extreme).

Scala programmers can feel free to skip to the final section where we give some simple to follow rules that are derived from the theoretical acedemic sections.

## Hell Is Other Peoples Code

PP is in stark contrast to SOLID, OOP and Design Patterns, which we will collectively refer to as Anthropocentric Principles or Anthropocentric Programming (or just AP). AP currently makes up the vast majority of code, and the vast majority of programmers follow these principles by faith and fashion.  We argue AP is fundementally flawed and is the root of all of the following problems:

1. High bug density
2. Code that is difficult understand, refactor and test
3. Code that is difficult to add new features to
4. Verbose code
5. Unhappy programmers, late delivery and the **end of the world**.  No seriously, due to compounding super-exponential technology advancments human programming incomptentency could be the main reason we don't yet live in a utopia.

The justification is as follows:

 - SOLID & Design Patterns are not formally defined, nor is it even possible in principle to formally define them (with the exception of Liskov Substitution).  They are inherently subjective.
 - SOLID & Design Patterns are at best Social Sciences, yet try to apply to what is really Mathematics.  No other branch of mathematics has ever had some set of anthropocentric and social principles imposed upon it, to do so would be absurd.
 - Even if we consider AP as a Social Science, for a theory of Social Science to be a success it will require vast amounts of experiments and statistical work.  AP has no such evidence base; very few studies exist.

## Quick History of Languages

The irony is that some languages make AP difficult, to nearly impossible.  For example Lisp, Erlang, Haskell and OCaml are founded on Functional Programming, which manages to eliminate the vast majority of the problems AP faces.  Ironically these languages are not that new, in fact Lisp is one of the oldest languages there is.

Some modern attempts have been made to mitigate the hell we find ourselves in.  For example Rust and Go have made attempts to standardise approaches to programming that removes the subjectivity, verbosity and complexity.

The worst languages ever invented and the main source of hell, unfortunately are probably the most popular, namely Java and C#.  The single worst decisions these two languages both made is:

 - Every function has to be arbitrarily tied to a containing scope
 - Class Oriented Programming, by design, forces a complex coupling of many functions
 
This is perhaps why Ruby, and especially Python, is becoming increasingly popular since they allow writing functions without containing scopes.

Observe how these catestrophically stupid language design decisions only occur in basically 2 languages out of dozens of others, yet these two languages are the most popular. Anyway, let's move on.

# Theory, Definitions and Theorems

## Central Tenet of PP

There does not exist an objectively perfect (non-trivial) program, but for any pair of programs we can compare them objectively.

Therefore an objective of subjects is to refactor programs so that we make objective improvements.

## Definition - Equivalent

We say program `P_1` and `P_2` are equivilant if for any input `I`, `P_1(I) = P_2(I)` when the two programs are run in identical universes (i.e. the computer and external states are identical).

## Examples - Equivalent

Below, `f` and `g` are equivalent.

```
def f: Int = 2
def g: Int = 1 + 1
```

Furthermore

```
def f: Int = 2 + readFile("myfile").length
def g: Int = 1 + 1 + readFile("myfile").length
```

are also equivilent even though they depend on an external state so may not always produce identical results in practice.

## Definition - Call Graph

For any program `P` the **call graph** `G_P` is the graph `(V, E)` where for any `v`, `v` is in the vertex set `V` if and only if it is a function in `P`, and for any `e`, `e` is in the edge set `E` if and only if `e = (f, g)` where `f` calls `g` in the program `P`.

So a **leaf function** is a function that calls no other function.  The **height** of a function `f`, written `H(f)`, is defined recursively as follows:

 - For any **leaf functions** `f`, `H(f) = 0`
 - For any function `f` that calls `g_1, ..., g_n`, then `H(f) = max_i(H(g_i)) + 1`

**Note:** We do not include functions native to a language in the call graph.  How we consider functions from an external library will be addressed later.

## Definition - Abstract Syntax Tree and Basic Complexity

The AST of a function `f` is the function written in reverse polish notation (or equivalently Lisp form).  The **Basic Complexity** of a function is the length of the AST, where each symbol has length 1 (equivalently the length of the Lisp form not counting the parens).

E.g. the polish notation form of the expression `l.map(f)` is `(map l f)`, so it's Basic Complexity is 3.

## Definition - Kolmogorov Complexity

The Kolmogorov Complexity (KC) of a function `f` is the length of the shortest program (in some chosen language L) that outputs the AST of the function.  In practice Kolmogorov Complexity must be intuited, not computed, since (a) the choice of language L is arbitrary, (b) KC is not itself a computable function.

E.g. We intuitively can imagine that the shortest program that outputs the expression `1 + 2 + 3 + 4 + 5 + 6` is shorter than the shortest program that outputs the expression `1 + 2 - 3 + 4 - 5 - 6`.

## Definition - Triangulation

For any function with tests `t_1, ..., t_n` and a type signature `(p_1, ...., p_n) -> r` we call the pair `((t_1, ..., t_n), (p_1, ...., p_n) -> r)` the **functions build constraints**.  The collection of all constrains for program is called the **programs build constrains**.

### Finitary Triangulation

For any function `f` with build constrains `B_f`, let `F` be the set of functions that satisfy `B_f`.  If we partition `F` into equivalent classes (i.e. subsets of `F` where for each subset every function is equivalent) we say `f` is **finitarily triangulated** if this parition is finite. Or we say it is **`N`-Triangulated** where `N` is the cardinality of the partition.

### Ideal Triangulation

This is when a function `f` is `1`-Triangulated.  In other words, every function that satisfies the build constraints is equivalent to `f`.

Note that for any function that is `N`-Triangulated, it is possible to add only `N - 1` tests to make it ideally triangulated.

Usually, in the real world, the tests plus the type signature will not be sufficient to even achieve finatary triangulation. So we define the following

### Complexity Augmented Triangulation

A function `f` is **AST Traingulated** or **A-Triangulated** if and only if, for any function `g` if `g` is not equivalent to `f` but does satisfy the build constraints then `g` has strictly greater Basic Complexity than `f`.

A function `f` is **Kolmogorov Traingulated** or **K-Triangulated** if and only if, for any function `g` if `g` is not equivalent to `f` but does satisfy the build constraints then `g` has strictly greater Kolmogorov Complexity than `f`.

In other words, a function is A or K Triangulated if all non-equivalent functions that build are more complicated.

In practice one will need to intuit whether or not a function is A or K Triangulated.  Usually a function written with perfect TDD will be.  This does not mean the concept is subjective, it just means it is not computable.  In the vast majority of cases one ought to be able to provide a short argument that proves or disproves the triangulation of a function.

### Non Deterministic Triangulation

Some testing frameworks will randomly generate test cases in order to pseudo-rigorously test generic properties on a function (that may be difficult to constrain with the type system alone).

In PP these kinds of tests are forbidden unless the seed of the random generator is fixed.  This essentially collapses a single non-deterministic test into a large collection of ordinary tests.  Strictly speaking then, these kinds of "Property Based Tests" add little, but in practice such tests can be useful to retrospectively add to a codebase with few tests.

## Theorem - Triangulation Strength

1-trangulation is stronger than N-trangulation, which is stronger than K-Triangulation, which is stronger than A-Triangulation, which is stronger than No triangulation

### Proof

Exercise.

## Theorem - Triangulation Transitivity

If a function `f` is X-Triangulated, and `f` calls `g`, then `g` is also X-Triangulated.  Note that the converse need not be true.

### Proof

Exercise (observe how lower level functions tend to have better test coverage than high level functions).

## Definition - Depth Triangulation Table

A programs **depth triangulation table** is the maximum level of triangulation (i.e. `(1, N, A, K, No)`) for functions of each Height.

For example suppose we have a simple program with only 2 functions `f` and `g`, and `f` calls `g`.  Suppose `g` is 1-Triangulated and `f` is A-Triangulated, then we could write the table as follows: `((0, 1), (1, A))`.

Observe that only certain kinds of tables are possible thanks to the Triangulation Transitivity Theorem.  

Furthermore we have an implicit ordering of the strength on tables of the same length, e.g. `((0, 1), (1, A))` is stronger than `((0, N), (1, A))`, and `((0, N), (1, A))` is stronger than `((0, N), (1, No))`.

## Definition - Referential Transparency

todo

## Theorem - State Monism

Given an infinitely fast processor and an infinite amount of memory, every program can be refactored to have at most 1 variable (i.e. `var` in Scala) while remaining functionally equivilant, and this `var` need only occur in the entry point of the application.  This means only one function, the entry point, mutates anything, while all other functions are pure.

**Note:** Most modern functional languages provide many native functions that hide away `var`s and most modern computers are very powerful, therefore most modern programs should respect State Monism.

Theorem - 

Given two scopes S_1 and S_2 where S_2 is a subscope of S_1, moving a `var` from S_1 into only S_2 cannot increase the number of non-referntially transparent expressions.




2. We favour programs with fewer non-referentially transparent expressions
3. 

## Call Complexity

If we where to write down every (potentially infinite) call path a program can take by only looking at it's call graph, the **call complexity** is the Kolmogorov Complexity of this sequence.

For example a program with 2 functions `f` and `g` where `f` calls `g` has only 1 path, so the sequence is `((f, g))`, so this will have very lower call complexity (the simple program `println("((f, g))")` would output this sequence.  A program with 1 recursive function `f` has the following paths: `(f), (f, f), (f, f, f), ...`, this will have higher call complexity (exercise, write a program to output this sequence).

## Scope Tree

A Scope Set is a set of collection of parameters and variables in the same scope.  The Scope Tree is the tree of Scope Sets, where an edge `(s_1, s_2)` means `s_2` is a subscope of `s_1`.

### Example

This function

```
def foo(x: Int, y: Int): Int = {
  val xy = x * y
  val bar = {
    val one = 1
    x + one
  }
  xy ^ bar
}

val ten = 10
```

has this scope tree:

```
(((ten), (x, y)), ((x, y), (xy, bar)), ((xy, bar), (one)))
```

while this function

```
def foo(x: Int, y: Int): Int = x * y ^ (x + 1)

val ten = 10
```

has this scope tree:

```
(((ten), (x, y)))
```

## Scope Complexity

Is the Kolmogorov Complexity of the scope tree.

## Inlining Expressions Theorem

When we use a variable to name an expression that is only used once, if we inline this expression we reduce the Basic, Kolmogorov and Scope complexity of the program.

# Constructing an Objective Set of Programming Principles



# Principles of PP

## Comparative Principles of PP

Given functionally equivilent programs

### 1. Triangulation Principle



2. We favour programs with shorter ASTs
3. We favour programs with fewer non-referentially transparent expressions
4. We favour programs with 

### Social principle

### Inherited Complexity Principle


Not all are compatible, so those with a higher number are favoured over those with a lower number.

## Absalute Principles of PP

The call graph must be fully connected (i.e. every function is transitively called by the entry point).


### Other Links

https://stackoverflow.com/a/22148186/1586965
