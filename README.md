# sudoku
Sudoku solvers

## Peter Norvig's Sudoku sovler in Java
If you don't know, Peter Norvig wrote this nice Sudoku solver in less than 100
lines of python code.  Reading through this annotated version of Peter's
article is a great exercise for anyone trying to get a better grip on python.

After reading those articles I wanted to see what this code would look like if
ported to Java 8.  How much more code would be required?  Where and why would
more code be required?  How would the readability compare?  If you're really
interested then you should definitely take a look at the code for yourself--it
won't take long.  If you're not quite that interested, my own summary follows.

The Java version is about 5 times longer then Peter's python version.  Why is that?

I include **more comments**, but this accounts for only about 16% of the new size.

Support for **list comprehensions** in python accounted for a large part of the difference.

Finally, **utility methods** accounted for the next largest share of the difference.

