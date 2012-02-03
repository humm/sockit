
clean:
	rm -f *.class */*.class */*/*.class */*/*/*.class */*/*/*/*.class *.pyc */*.pyc */*/*.pyc */*/*/*.pyc;
	rm -Rf build/*
	
library: src/sockit/Server.java src/sockit/InputMessage.java src/sockit/OutputMessage.java src/sockit/Client.java src/sockit/utils/Utils.java
	rm -Rf build/sockit/
	javac src/sockit/*.java src/sockit/*/*.java -d build/

sockit.jar: library
	jar -cf build/sockit.jar build/sockit/

Example: library
	javac src/Example.java -cp build/ -d build/
	cd build; java Example
	
Test: library
	javac src/Test.java -cp build/ -d build/
	cd build; java Test
		
FibServer:
	javac pyclient/test/FibServer.java -cp build/ -d build/
	cd build; java FibServer
	