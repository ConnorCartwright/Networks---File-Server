url = http://weknowyourdreams.com/images/potato/potato-04.jpg

.SUFFIXES: .java .class
.java.class:
	javac -g $*.java

client = client/SimpleServerClient.java

server = \
	server/CheckContentType.java\
	server/SimpleServerThread.java\
	server/Main.java\
	

classes: \
	$(client:.java=.class) \
	$(server:.java=.class)

clean:
	$(RM) client/*.class
	$(RM) server/*.class

all: classes

run: classes 
	java server.Main

test: classes
	java client.SimpleServerClient $(url)

default: classes

