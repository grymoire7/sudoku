# sudoku
Sudoku solvers

## Peter Norvig's Sudoku sovler in Java
If you don't know, Peter Norvig wrote [this nice Sudoku solver](http://www.norvig.com/sudoku.html) in less than 100
lines of python code.  Reading through [this annotated version](https://medium.com/towards-data-science/peter-norvigs-sudoku-solver-25779bb349ce) 
of Peter's article is a great exercise for anyone trying to get a better grip on python.

After reading those articles I wanted to see what this code would look like if
ported to Java 8.  How much more code would be required?  Where and why would
more code be required?  How would the readability compare?  If you're really
interested then you should definitely take a look at the code for yourself--it
won't take long.  If you're not quite that interested, my own summary follows.

The Java version is about 5 times longer then Peter's python version.  Why is that?

I include **more comments**, but this accounts for only about 16% of the difference.

Extra **utility methods** available in python but not in Java that I needed to add (DeepClone.java and StringUtils.java) accounted for about 33% of the difference.

The remaining 51% of the difference is all about the differences between Java and python that I was interested in looking at.
In many ways Java is simply more verbose.  More brackets, longer variable names, namespacing, etc.  One of the more interesting differences is **list comprehensions** in python vs loops and streams in Java.

