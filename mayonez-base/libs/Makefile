all: test.jar
	java -jar $<

test.jar: Test.class foo
	jar -cfe $@ Test $^

Test.class: Test.java
	javac $<

clean:
	rm test.jar Test.class

.PHONY: all clean
