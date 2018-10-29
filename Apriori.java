/* Nhan Le
 * CSC 240
 * Project 1
 */

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

public class Apriori
{
    // Based on pseudocode in Han et al.’s Data Mining: Concepts and Techniques, 3rd Edition.
    // Return 2D array of frequent itemsets.
    public static ArrayList<ArrayList<FrequentItemset>> main(String[][] df, int supportThreshold)
    {
        String[][] db = prepDB(df);

        ArrayList<ArrayList<FrequentItemset>> L = new ArrayList<ArrayList<FrequentItemset>>();

        // Generate frequent 1-itemsets and add to L.
        ArrayList<FrequentItemset> L1 = firstScan(db, supportThreshold);
        L.add(L1);

        // Generate frequent k-itemsets.
        // k here is 1 less than in k in pseudocode. Ex: index 1 = position 2.
        for (int k = 1; L.get(k - 1).size() != 0; k++)
        {
            // Generate frequent k-candidates.
            ArrayList<FrequentItemset> Ck = scan(L.get(k - 1));

            // For each transaction ...
            for (int a = 0; a < db.length; a++)
            {
                String transactionLine = "";

                for (int b = 0; b < db[a].length; b++)
                    transactionLine += db[a][b] + ", ";

                // Find support count of k-candidates.
                ArrayList<FrequentItemset> Ct = findSubset(Ck, transactionLine);

                for (FrequentItemset candidate : Ct)
                    candidate.supportCount++;
            }

            ArrayList<FrequentItemset> Lk = new ArrayList<FrequentItemset>();

            // Add to frequent k-itemsets if k-candidate meets support threshold.
            for (FrequentItemset candidate : Ck)
                if (candidate.supportCount >= supportThreshold)
                    Lk.add(new FrequentItemset(candidate));

            // Add frequent k-itemsets to L.
            L.add(Lk);
        }

        return L;
    }

    // Scan database for first time.
    protected static ArrayList<FrequentItemset> firstScan(String[][] db, int supportThreshold)
    {
        Hashtable<String, Integer> uniqueItem = new Hashtable<String, Integer>();
        ArrayList<FrequentItemset> L1 = new ArrayList<FrequentItemset>();

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

        Iterator<Entry<String, Integer>> it = uniqueItem.entrySet().iterator();

        // Add to L1 if support threshold is met.
        while (it.hasNext())
        {
            Entry<String, Integer> entry = it.next();

            if (entry.getValue() >= supportThreshold)
            {
                ArrayList<String> item = new ArrayList<String>();
                item.add(entry.getKey());

                L1.add(new FrequentItemset(item, entry.getValue()));
            }
        }

        return L1;
    }

    // Scan database.
    // Based on pseudocode in Han et al.’s Data Mining: Concepts and Techniques, 3rd Edition.
    private static ArrayList<FrequentItemset> scan(ArrayList<FrequentItemset> Lk_1)
    {
        ArrayList<FrequentItemset> Ck = new ArrayList<FrequentItemset>();

        for (FrequentItemset itemset1 : Lk_1)
        {
            for (FrequentItemset itemset2 : Lk_1)
            {
                // Generate candidates for Ck. Join itemset1 with itemset2.
                ArrayList<String> candidate = join(itemset1, itemset2);

                // If joined itemsets are frequent ...
                if (candidate != null && hasInfrequentSubset(candidate, Lk_1) == false)
                {
                    boolean breakVal = false;

                    for (FrequentItemset c : Ck)
                        if (c.itemset.containsAll(candidate) == true)
                        {
                            breakVal = true;
                            break;
                        }

                    // Add candidate to Ck.
                    if (breakVal == false)
                        Ck.add(new FrequentItemset(candidate));
                }

                // Everything else is pruned.
            }
        }

        return Ck;
    }

    // Find subset.
    protected static ArrayList<FrequentItemset> findSubset(ArrayList<FrequentItemset> Ck, String transactionLine)
    {
        Hashtable<String, Integer> uniqueItem = new Hashtable<String, Integer>();
        ArrayList<FrequentItemset> Ct = new ArrayList<FrequentItemset>();

        String[] transactionArr = transactionLine.split(", ");

        // For every item ...
        for (String item: transactionArr)
            // Add to hash table if unique.
            if (uniqueItem.containsKey(item) == false)
                uniqueItem.put(item, 1);

        // From each candidate ...
        for (FrequentItemset candidate : Ck)
        {
            boolean breakVal = false;

            for (String item : candidate.itemset)
                if (uniqueItem.containsKey(item) == false)
                    breakVal = true;

            // Add to Ct if found in hash table.
            if (breakVal == false)
                Ct.add(candidate);
        }

        return Ct;
    }

    // Join items.
    protected static ArrayList<String> join(FrequentItemset itemset1, FrequentItemset itemset2)
    {
        ArrayList<String> join = new ArrayList<String>();
        String L1minusL2 = "";

        // Iterate through Lk_1.
        for (String item : itemset1.itemset)
        {
            // Add item to intersection if item is found in Lk_1.
            if (itemset2.itemset.contains(item))
                join.add(item);

                // Add item to L1minusL2 if item is not found in L2.
            else
                L1minusL2 += item;
        }

        // Return null if join is not possible.
        if (join.size() != itemset1.itemset.size() - 1)
            return null;

        // Add L1minusL2 to give original L1.
        join.add(L1minusL2);

        // Add unique L2 item to original L1 to give joined itemset.
        for (String item : itemset2.itemset)
            if (join.contains(item) == false)
                join.add(item);

        return join;
    }

    // Determine if candidate has infrequent subset.
    // Based on pseudocode in Han et al.’s Data Mining: Concepts and Techniques, 3rd Edition.
    protected static boolean hasInfrequentSubset(ArrayList<String> candidate, ArrayList<FrequentItemset> Lk_1)
    {

        for (String s1 : candidate)
        {
            FrequentItemset subset = new FrequentItemset();

            // For each subset of c ...
            for (String s2 : candidate)
                if (s1.equals(s2) == false)
                    subset.itemset.add(s2);

            boolean infrequentStatus = true;

            // Check k_1 subset against every Lk_1 itemset.
            for (FrequentItemset itemset : Lk_1)
                if (itemset.isFrequent(subset) == false)
                {
                    infrequentStatus = false;
                    break;
                }

            if (infrequentStatus == true)
                return true;
        }

        return false;
    }

    // Convert 2D array to String.
    public static String toString(ArrayList<ArrayList<FrequentItemset>> L)
    {
        ArrayList<FrequentItemset> itemset = new ArrayList<FrequentItemset>();

        for (ArrayList<FrequentItemset> is : L)
            itemset.addAll(is);

        String output = "";

        for (FrequentItemset item : itemset)
            if (item.itemset.size() > 1)
                output += item.itemset + ": " + item.supportCount + System.lineSeparator();

        return output;
    }

    public static String[][] prepDB(String[][] df)
    {
        String[][] db = new String[df.length - 1][df[0].length];

        // Organize df.
        // Remove row of names and concatenate names with values.
        for (int a = 1; a < df.length; a++)
            for (int b = 0; b < df[a].length; b++)
                db[a -  1][b] = df[0][b] + " = " + df[a][b];

        return db;
    }
}