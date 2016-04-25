JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
        $(JC) $(JFLAGS) $*.java

CLASSES = \
        helper/Pair.java \
        lexer/AbstractDFA.java \
        lexer/CommentDFA.java \
        lexer/DFA.java \
        lexer/LexerGenerator.java \
        src/Main.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
        $(RM) *.class