JFLAGS = -g
JC = javac
JCR = java

.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Main.java \
	SemaphoresUtil.java \
	Valve.java

MAIN = Main

default: classes run

classes: $(CLASSES:.java=.class)

run: $(MAIN).class
	 @$(JCR) $(MAIN)

clean:
	$(RM) *.class *~
