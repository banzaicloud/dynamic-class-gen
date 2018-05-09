# Dynamic Class Generator


This is a very simple Java app for **_testing_** purposes.

This application generates dynamically classes and instantiates them. The generated classes have a method for allocating some memory on the heap.

It expects the number of classes to be generates passed into the `DYN_CLASS_COUNT` environment variable. application instantiates each generated class and invokes the `consumeSomeMemory` method in them to allocate memory in the heap. Each call will allocate the amount of memory specified in the `MEM_USAGE_PER_OBJECT_MB`
