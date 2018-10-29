import java.util.ArrayList;

public class ImprovedApriori extends Apriori
{
    private static String[][] transactionLineArr;
    private static boolean toDelete = false;

    public static ArrayList<ArrayList<FrequentItemset>> main(String[][] df, int supportThreshold)
    {
        String[][] db = prepDB(df);

        transactionLineArr = toTransactionLineArr(db);

        ArrayList<ArrayList<FrequentItemset>> L = new ArrayList<ArrayList<FrequentItemset>>();

        // Generate frequent 1-itemsets.
        ArrayList<FrequentItemset> L1 = firstScan(db, supportThreshold);
        L.add(L1); // Add to L.

        // Generate frequent k-itemsets.
        // k here is 1 less than in k in pseudocode. Ex: index 1 = position 2.
        for (int k = 1; L.get(k - 1).size() != 0; k++)
        {
            toDelete = false;

            // Generate frequent k-candidates.
            ArrayList<FrequentItemset> Ck = scan(L.get(k - 1));

            // For each transaction ...
            for (int i = 0; i < db.length; i++)
            {
                // If transaction line is frequent.
                if (transactionLineArr[i][0] == "do")
                {
                    // Find subsets of Lk_1.
                    ArrayList<FrequentItemset> Ct = findSubset(Ck, transactionLineArr[i][1]);

                    toDelete = Ct.isEmpty();

                    if (toDelete == true)
                        transactionLineArr[i][0] = "skip";

                    for (FrequentItemset candidate : Ct)
                        candidate.supportCount++;
                }
            }

            ArrayList<FrequentItemset> Lk = new ArrayList<FrequentItemset>();

            // Add to frequent k-itemsets if k-candidates meet support threshold.
            for (FrequentItemset candidate : Ck)
                if (candidate.supportCount >= supportThreshold)
                    Lk.add(new FrequentItemset(candidate));

            // Add frequent k-itemsets to L.
            L.add(Lk);
        }

        return L;
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


    public static String[][] toTransactionLineArr(String[][] df)
    {
        String[][] newArr = new String[df.length][2];

        // Copy array from first row to row before deleted row.
        for (int a = 0; a < df.length; a++)
        {
            newArr[a][0] = "do";

            String transactionLine = "";

            for (int b = 0; b < df[a].length;  b++)
                transactionLine += df[a][b] + ", ";

            newArr[a][1] = transactionLine;
        }

        return newArr;
    }
}