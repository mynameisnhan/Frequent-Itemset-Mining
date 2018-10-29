/* Nhan Le
 * CSC 240
 * Project 1
 */

import java.util.Arrays;
import java.util.HashSet;

public class MissingValueHandler
{
    // Count # of missing values.
    public static String[][] count(String[][] df, String missingSymbol)
    {
        String countArr[][] = new String[df[0].length][];

		/* Generate count array.
		 *
		 *			name	# missing	missing at row x	missing at row x	missing at row x	...
		 * att1
		 * att2
		 * att3
		 * ...
		 *
		 */

        int i = 0;

        // For each attribute ...
        for (int b = 0; b < df[0].length; b++)
        {
            // Group together all transactions.
            String everythingInColumn[] = new String[df.length - 1];

            for (int a = 1; a < df.length; a++)
                everythingInColumn[a - 1] = df[a - 1][b];

            int numMissing = 0;

            // Find # of missing values.
            for (int a = 0; a < everythingInColumn.length; a++)
                if (everythingInColumn[a].compareTo(missingSymbol) == 0)
                    numMissing++;

            // Add name and # of missing values to count array row.
            countArr[i] = new String[numMissing + 3];
            countArr[i][0] = df[0][b];
            countArr[i][1] = String.valueOf(numMissing);

            for (int a = 0; a < everythingInColumn.length; a++)
            {
                countArr[i][2] = "nonnumeric";

                if (isDouble(String.valueOf(everythingInColumn[a])))
                    countArr[i][2] = "numeric";
            }

            int j = 3;

            for (int a = 0; a < everythingInColumn.length; a++)
                if (everythingInColumn[a].compareTo(missingSymbol) == 0)
                {
                    countArr[i][j] = String.valueOf(a);
                    j++;
                }

            i++;
        }

        return countArr;
    }

    // Replace missing numeric values with attribute means.
    // TODO for future assignment. Not used for Project 1.
    public static String[][] fillMean()
    {
        throw new IllegalArgumentException("Method not created yet.");
    }

    // Delete transactions with missing nonnumeric values.
    public static String[][] delete(String[][] df, String[][] countArr)
    {
        String newDF[][] = df;

        int i1 = 0;

        for (int a = 0; a < countArr.length; a++)
            if (countArr[a][1].compareTo("0") != 0)
                if (countArr[a][2].compareTo("nonnumeric") == 0)
                    for (int b = 3; b < countArr[a].length; b++)
                        i1++;

        int everyRemovedRow[] = new int[i1];
        int i2 = 0;

        for (int a = 0; a < countArr.length; a++)
            if (countArr[a][1].compareTo("0") != 0)
                if (countArr[a][2].compareTo("nonnumeric") == 0)
                    for (int b = 3; b < countArr[a].length; b++)
                    {
                        everyRemovedRow[i2] = Integer.valueOf(countArr[a][b]);
                        i2++;
                    }

        Arrays.sort(everyRemovedRow);

        HashSet<Integer> hashedRow = new HashSet<Integer>();

        for (int a = 0; a < everyRemovedRow.length; a++)
            hashedRow.add(everyRemovedRow[a]);

        Integer hashedEveryRemovedRow[] = new Integer[hashedRow.size()];
        hashedRow.toArray(hashedEveryRemovedRow);
        Arrays.sort(hashedEveryRemovedRow);

        int numRemoved = 0;

        for (int a = 0; a < hashedEveryRemovedRow.length; a++)
        {
            newDF = MatrixHandler.deleteRow(newDF, Integer.valueOf(hashedEveryRemovedRow[a]) - numRemoved);
            numRemoved++;
        }

        return newDF;
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
