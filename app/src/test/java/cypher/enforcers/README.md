# Remarks about running tests...

When I first made these tests I had originally thought that Junit would run them in sequential order (each function one by one, top to bottom) however I later realised it runs them in parallel.

This is a problem because when I was making these tests, I made them depend on one another. So for example one test would create a user, then a second test would check if the user from the first test is in the database. Running these tests in parallel will definitely produce undefined behavior. I'm too lazy to re-write these tests, so if you'd like to run them, do so in the following order to avoid errors.

Note that the tests create a temporary database file, didn't know how to make
my database class use a in-memory database. Anyway, if you run the tests
described in the following order, the temporary database file should be gone 
after the tests are complete.


First run the tests in `SaveingDataTests.java` top to bottom, so run the first function you see seperately, then the second function and so on.

Second run the tests in `LoadingDataTests.java` top to bottom, just like the first file.

Third run the tests in `SaveAndLoadDataTests.java` top to bottom, just like the first file.

They should all pass.