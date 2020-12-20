# Frequent Itemset Mining

Accuracies and runtimes for classical apriori, FP-growth, and my improved apriori are compared. 

My improved apriori seeks to address classical apriori's redundant candidate generation. With my improvement, transactions are parsed only once into an array as opposed to before every iteration in classical apriori. Since information about every transaction is stored inside the array, we can mark which transaction lines lack infrequent itemsets (and will undergo candidate generation) and which transaction lines contain infrequent itemsets (and will skip candidate generation).

## To Run

In Main.java, change the dataset attributes on line 19 as needed

Open command prompt

Change directory to project folder

To compile, type: javac \*.java

To run, type: java Main

## Restrictions

Data file must NOT include an attribute row.

## Future Improvements

Allow user to change dataset attributes via command line inputs.
