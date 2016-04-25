JFLAGS = -g
JC = javac
BUILDPATH = bin

default: bin main

main:
	$(JC) $(JFLAGS) -d $(BUILDPATH) -sourcepath src src/Main.java

bin:
	mkdir $(BUILDPATH)

clean:
	$(RM) -r $(BUILDPATH)

