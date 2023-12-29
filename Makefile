# Edward Lee
# December 21, 2023

# Define variables
SRCDIR = src
BINDIR = bin
SLF4J = dependencies/slf4j-api-1.7.36.jar
SQLITE = dependencies/sqlite-jdbc-3.44.1.0.jar

# Default target
all:
	javac -d $(BINDIR) -cp $(SRCDIR) $(SRCDIR)/*.java $(SRCDIR)/*/*.java


run:
	java -cp $(BINDIR):${SQLITE}:${SLF4J} $(SRCDIR).Main

# Clean compiled files
clean:
	rm -rf $(BINDIR)/*


.PHONY: sim