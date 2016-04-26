JC = javac
JAVA = java
JFLAGS = -g -Xlint:unchecked
BUILDPATH = bin

all: main

main: bin
	$(JC) $(JFLAGS) -d $(BUILDPATH) -sourcepath src src/Main.java

bin:
	mkdir $(BUILDPATH)


run-tests: main
	for t in tests/*.txt; do \
		echo; echo $$t; \
		$(JAVA) -ea -cp $(BUILDPATH) Main $$t; \
	done

clean:
	$(RM) -r $(BUILDPATH)

