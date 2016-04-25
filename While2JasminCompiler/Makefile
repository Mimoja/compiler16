JFLAGS = -g
JC = javac
BUILDPATH = bin

default: main

main:
	$(JC) $(JFLAGS) -d $(BUILDPATH) -sourcepath src src/Main.java

clean:
	$(RM) -r $(BUILDPATH)/*

