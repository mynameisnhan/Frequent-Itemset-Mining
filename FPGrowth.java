/* Nhan Le
 * CSC 240
 * Project 1
 */

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

public class FPGrowth
{
    // Based on pseudocode in Han et al.’s Data Mining: Concepts and Techniques, 3rd Edition.
    public static ArrayList<FrequentItemset> main(String[][] df, int supportThreshold)
    {
        String[][] db = new String[df.length - 1][df[0].length];

        // Remove row of names and concatenate names with values.
        for (int a = 1; a < df.length; a++)
            for (int b = 0; b < df[a].length; b++)
                db[a -  1][b] = df[0][b] + " = " + df[a][b];

        // Scan db for first time. Create hash table of items
        Hashtable<String,Integer> item = firstScan(db);

        Iterator<Entry<String, Integer>> it = item.entrySet().iterator();

        // Remove item from hash table if item is infrequent.
        while (it.hasNext())
        {
            if (it.next().getValue() < supportThreshold)
                it.remove();
        }

        // Count item frequency. Sort itemset with FPTree.itemsorter. Add most frequent item to tree.
        FPTree tree = new FPTree(db, supportThreshold, item);

        // Mine tree.
        ArrayList<FrequentItemset> L = mineTree(tree, new ArrayList<String>(), supportThreshold);

        return L;
    }

    // Scan database for first time. Identical to first part of firstScan method of Apriori.
    public static Hashtable<String,Integer> firstScan(String[][] db)
    {
        Hashtable<String, Integer> uniqueItem = new Hashtable<String, Integer>();

        for (int a = 0; a < db.length; a++)
            for (int b = 0; b < db[a].length; b++)
            {
                String item = db[a][b];

                // Put item at index incremented by 1 if item is already in hash table.
                if (uniqueItem.containsKey(item))
                    uniqueItem.put(item, uniqueItem.get(item) + 1);

                    // Put item at index 1 is item is not already in hash table.
                else
                    uniqueItem.put(item, 1);
            }

        return uniqueItem;
    }

    // Mine tree.
    // Based on pseudocode in Han et al.’s Data Mining: Concepts and Techniques, 3rd Edition.
    public static ArrayList<FrequentItemset> mineTree(FPTree tree, ArrayList<String> suffixPattern, int supportThreshold)
    {
        Hashtable<String, FPTreeNode> headerTable = tree.headerTable;
        Iterator<Entry<String, FPTreeNode>> it = headerTable.entrySet().iterator();
        ArrayList<FrequentItemset> L = new ArrayList<FrequentItemset>();

        // Iterate through each item in header table.
        while (it.hasNext())
        {
            Entry<String, FPTreeNode> entry = it.next();
            int supportCount = 0;
            FPTreeNode treeRoot = entry.getValue();

            supportCount += treeRoot.count;
            FPTreeNode current = treeRoot;

            while (current.sibling != null)
            {
                current = current.sibling;
                supportCount += current.count;
            }

            // If itemset is frequent and suffix pattern is found.
            if (supportCount >= supportThreshold && suffixPattern.contains(entry.getKey()) == false)
            {
                // Add frequent itemset to L.
                FrequentItemset freq = new FrequentItemset();
                freq.itemset.add(entry.getKey());
                freq.itemset.addAll(suffixPattern);
                freq.supportCount = supportCount;
                L.add(freq);

                // Recursively mine tree.
                ArrayList<FrequentItemset> mineTree = mineTree(tree.conditionalTree(entry.getKey(), supportThreshold), freq.itemset, supportThreshold);
                L.addAll(mineTree);
            }
        }

        return L;
    }

    // Convert array to String.
    public static String toString(ArrayList<FrequentItemset> I)
    {
        String output = "";

        for (FrequentItemset item : I)
            if (item.itemset.size() > 1)
                output += item.itemset + ": " + item.supportCount + System.lineSeparator();

        return output;
    }
}