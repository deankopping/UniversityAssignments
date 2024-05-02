#Make File
#Assignment 2
#KPPDEA001

#JAVAC = $(JAVA_HOME)/bin/javac

.SUFFIXES: .java .class 
SRCDIR=src/typingTutor
BINDIR=bin/typingTutor

$(BINDIR)/%.class:$(SRCDIR)/%.java
	 $(JAVAC) -d $(BINDIR)/  -cp $(BINDIR) $<

CLASSES=  FallingWord.class WordDictionary.class HungryWord.class GamePanel.class WordMover.class HungryWordMover.class ScoreUpdater.class Score.class CatchWord.class CatchHungryWord.class TypingTutorApp.class
CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)

default: $(CLASS_FILES)

clean:
	rm $(BINDIR)/*.class

run: $(CLASS_FILES)
	java -cp  $(BINDIR) TypingTutorApp
