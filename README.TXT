------------------------------------------------------------------------
This is the project README file. Here, you should describe your project.
Tell the reader (someone who does not know anything about this project)
all he/she needs to know. The comments should usually include at least:
------------------------------------------------------------------------

PROJECT TITLE: EX16
PURPOSE OF PROJECT: Write implementation of pseudo online algorithm to find K smallest elements from online input.


VERSION or DATE: 30/7/2017
HOW TO START THIS PROJECT: Use command line and run EX16.jar from its path with next command java –jar EX16.jar 
AUTHORS: Michael Bochkovsky
USER INSTRUCTIONS:
Main function. This function is only test function to test an algorithm of pseudo online finding K smallest elements. Simulates receiving an online data and finding K smallest elements in that data. K smallest element retrieved in sorted order(growing up sequence) by In-Order traversal over Red-Black-Tree. Arrays of input are created by standart random function. This function uses class of RedBlackTree. In order to find K smallest elements first of all need to create empty Red Black Tree and to run function kSmallest. Function kSmallest retrieve as parameters, empty Red-Black Tree, the array of inputs and the number of K elements to found. This function creates a data structure of K smallest elements and print K smallest elements 3 times accordingly to test points which are 1\4, 1\2 ,3\4 and 1\1 of size of input array, followed with input array as reference. And only after it you can use printKMin function in order to print only K smallest elements.
