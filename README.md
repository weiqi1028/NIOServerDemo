# NIOServerDemo
A simple Java NIO server demo

This is a simple java NIO server demo. A thread is used to accept manage multiple clients' IO requests. This is known as a reactor pattern. Each client is registered to the Java Selector, and when there is an IO available, the Selector will find the corresponding key from its key set and call the event handler to handle the IO event (a server handler and many client handlers in this example).
