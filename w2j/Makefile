JC = javac
JAVA = java
JFLAGS = -g -Xlint:unchecked
BUILDPATH = bin

all: $(BUILDPATH)/Main.class

SRCS=$(wildcard src/*.java) $(wildcard src/*/*.java)

$(BUILDPATH)/Main.class: $(SRCS)
	mkdir -p $(BUILDPATH)
	$(JC) $(JFLAGS) -d $(BUILDPATH) -sourcepath src src/Main.java


run-tests: $(BUILDPATH)/Main.class
	for t in tests/*parser/*.txt; do \
		echo; echo ">>>>>> $$t <<<<<<"; \
		$(JAVA) -ea -cp $(BUILDPATH) Main $$t; \
	done

clean:
	rm -rf $(BUILDPATH)

.PHONY: all clean
