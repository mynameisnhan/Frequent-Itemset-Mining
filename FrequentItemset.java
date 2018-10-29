/* Nhan Le
 * CSC 240
 * Project 1
 */

import java.util.ArrayList;

// Simple data structure for frequent itemset.
public class FrequentItemset
{
    int supportCount = 0;
    public ArrayList<String> itemset = new ArrayList<String>();

    public FrequentItemset()
    {
    }

    public FrequentItemset(ArrayList<String> itemset)
    {
        this.itemset = itemset;
        this.supportCount = 0;
    }

    public FrequentItemset(ArrayList<String> itemset, int supportCount)
    {
        this.itemset = itemset;
        this.supportCount = supportCount;
    }

    public FrequentItemset(FrequentItemset frequentItemset)
    {
        this.itemset = frequentItemset.itemset;
        this.supportCount = frequentItemset.supportCount;
    }

    public boolean isFrequent(FrequentItemset comparedItemset)
    {
        boolean returnVal = true;

        for(String comparedItem : comparedItemset.itemset)
        {
            boolean frequentStatus = false;

            for (String item : this.itemset)
                if (item.equals(comparedItem))
                    frequentStatus = true;

            if (frequentStatus == false)
                returnVal = false;
        }

        return returnVal;
    }

    public boolean isIdentical(FrequentItemset comparedItemset)
    {
        boolean returnVal = true;

        for(String comparedItem : comparedItemset.itemset)
        {
            boolean identicalStatus = false;

            for (String item : this.itemset)
                if (item.equals(comparedItem))
                    identicalStatus = true;

            if (identicalStatus == false)
                returnVal = false;
        }

        return returnVal;
    }

    public String toString()
    {
        String output = "";

        for(String s : this.itemset)
            output += s + ", ";

        if (output.equals(""))
            output = "null";

        else output = output.substring(0,output.length() - 2);

        return output;
    }
}