# Test if the Python client is compatible with the Java server

import sys, os
sys.path += [os.path.join(os.path.dirname(__file__), '../')]

import client

# Socket adress
PORT = 1984
IP = "127.0.0.1"

# Protocol
HELLO_TYPE = 0
BYE_TYPE   = 1
FIBO_TYPE  = 2


def clientTest():
    
    c = Client()
    b = c.connect(PORT, IP)
    
    if b:
        print("CLIENT -> started")
        
        hello = Message()
        hello.type = HELLO_TYPE
        hello.appendString("Hello server !")
        c.send(hello)
        
        for i in range(41):
            f = Message()
            f.type = FIBO_TYPE
            f.appendInt(i)
			print("CLIENT -> asking for fibo(" + i + ")"
            a = c.sendAndReceive(f)
            if not a is None:
                if a.type == FIBO_TYPE:
                    print("CLIENT -> fibo(" + i + ") = " + a.readInt())
                else:
                    print("CLIENT -> error in protocol")
                    c.disconnect()
                    break
            else:
				print("CLIENT -> error in exchange")
				c.disconnect()
				break
        bye = Message()
        bye.type = BYE_TYPE
        bye.appendString("Bye Server !")
        c.send(bye)
        c.disconnect()
        print("CLIENT -> stopped")
    else:
        print("CLIENT -> error")
    return

if __name__ == "__main__":
    clientTest()