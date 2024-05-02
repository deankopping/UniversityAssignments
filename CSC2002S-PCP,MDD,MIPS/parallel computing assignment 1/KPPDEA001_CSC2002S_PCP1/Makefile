#Make File
#Assignment 1
#KPPDEA001

JAVAC= /usr/bin/javac
.SUFFIXES: .java .class
SRCDIR=src
BINDIR=bin

$(BINDIR)/%.class:$(SRCDIR)/%.java
	 $(JAVAC) -d $(BINDIR)/  -cp $(BINDIR) $<

CLASSES= MeanFilterParallel.class MeanFilterSerial.class MedianFilterSerial.class MedianFilterParallel.class
CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)

default: $(CLASS_FILES)

clean:
	rm $(BINDIR)/*.class


runParallelMean: $(CLASS_FILES)
	java -cp  $(BINDIR)  MeanFilterParallel "megos.jpg" "newImage.jpg" "7"

runSequentialMean: $(CLASS_FILES)
	java -cp  $(BINDIR) MeanFilterSerial "megos.jpg" "newImage.jpg" "7"

runParallelMedian: $(CLASS_FILES)
	java -cp  $(BINDIR)  MedianFilterParallel "megos.jpg" "newImage.jpg" "7"

runSequentialMedian: $(CLASS_FILES)
	java -cp  $(BINDIR) MedianFilterSerial "megos.jpg" "newImage.jpg" "7"
