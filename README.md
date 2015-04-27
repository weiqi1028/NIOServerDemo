# NIOServerDemo
A simple Java NIO server demo

This is a simple Java NIO server demo. A thread is used to manage multiple clients' IO requests. This is known as a reactor pattern. Each client is registered to the Java Selector, and when there is an IO available, the Selector will find the corresponding key from its key set and call the event handler to handle the IO event (a server handler and many client handlers in this example).
