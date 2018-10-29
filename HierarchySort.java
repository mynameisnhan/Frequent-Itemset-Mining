/* Nhan Le
 * CSC 240
 * Project 1
 */

import java.util.StringTokenizer;

public class HierarchySort
{
    public static String[][] numericCHByCat(String[][] df, String cat)
    {
        String[][] newDF = df;

        // For each attribute ...
        for (int b = 0; b < df[0].length; b++)
        {
            // Group together all transactions.
            String everythingInColumn[] = new String[df.length - 1];

            for (int a = 1; a < df.length; a++)
                everythingInColumn[a - 1] = df[a][b];

            boolean numStatus = true;
            boolean binStatus = true;

            // Determine if attribute is numeric and nonbinary.
            for (int a = 0; a < everythingInColumn.length; a++)
            {
                if (isDouble(everythingInColumn[a]) == false)
                {
                    numStatus = false;
                    binStatus = false;
                    break;
                }

                if (numStatus == true)
                    if (everythingInColumn[a] != "0" && everythingInColumn[a] != "1")
                        binStatus = false;
            }

            // If attribute is numeric and nonbinary.
            if (numStatus == true && binStatus == false)
            {
                double toBeTiered[] = new double[everythingInColumn.length];

                for (int a = 0; a < everythingInColumn.length; a++)
                    toBeTiered[a] = Double.valueOf(everythingInColumn[a]);

                // Find min, max, overall range, and bin width based on bin count.
                double max = findMax(toBeTiered);
                double min = findMin(toBeTiered);
                double range = max - min;
                StringTokenizer token = new StringTokenizer(cat, ", ");
                int binCount = token.countTokens();
                double binWidth = range/binCount;

                String tokenArr[] = new String[binCount];
                String binRange[][] = new String[binCount][3];

                int i = 0;

                while (token.hasMoreTokens())
                {
                    tokenArr[i] = token.nextToken();
                    i++;
                }

                double tempMin = min;

                // Assign bins to categories.
                for (int a = 0; a < binCount; a++)
                {
                    double tempMax = tempMin + binWidth;

                    binRange[a][0] = tokenArr[a] + " " + df[0][b];
                    binRange[a][1] = Double.toString(tempMin);
                    binRange[a][2] = Double.toString(tempMax);

                    tempMin += binWidth;
                }

                // Replace numeric values in df with categories.
                for (int a = 0; a < toBeTiered.length; a++)
                {
                    for (int c = 0; c < binRange.length; c++)
                    {
                        if (toBeTiered[a] >= Double.valueOf(binRange[c][1]) && (toBeTiered[a] < Double.valueOf(binRange[c][2])))
                        {
                            newDF[a + 1][b] = binRange[c][0];
                        }

                        else if (toBeTiered[a] >= Double.valueOf(binRange[c][1]) && (toBeTiered[a] == max))
                        {
                            newDF[a + 1][b] = binRange[c][0];
                        }
                    }
                }

            }
        }

        return newDF;
    }

    // Find max.
    private static double findMax(double[] arr)
    {
        double max = arr[0];

        for (int i = 0; i < arr.length; i++)
            if (arr[i] > max)
                max = arr[i];

        return max;
    }

    // Find min.
    private static double findMin(double[] arr)
    {
        double min = arr[0];

        for (int i = 0; i < arr.length; i++)
            if (arr[i] < min)
                min = arr[i];

        return min;
    }

    // Check if value is numeric.
    private static boolean isDouble(String string)
    {
        try
        {
            Double.parseDouble(string);
            return true;
        }

        catch (Exception exception)
        {
            return false;
        }
    }
}
