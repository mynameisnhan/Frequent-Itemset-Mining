/* Nhan Le
 * CSC 240
 * Project 1
 */

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Scanner;
import java.util.StringTokenizer;

public class DataScanner
{
    // Scan input file to data frame.
    public static String[][] scan(String input, String attribute) throws IOException
    {
        File in = new File(input);
        FileReader fr = new FileReader(in);

        // Count # of transactions.
        LineNumberReader lnr = new LineNumberReader(fr);
        lnr.skip(Integer.MAX_VALUE);
        int transactionCount = lnr.getLineNumber() + 1;
        lnr.close();

		/* Generate data frame as 2D array.

		   {att1, att2, att3, ...}, {att1, att2, att3, ...}, {att1, att2, att3, ...}, ...}

		  			att1	att2	att3	...
		   name
		   item1
		   item2
		   item3
		   ...

		 */

        StringTokenizer attNameToken = new StringTokenizer(attribute, ", ");
        String df[][] = new String[transactionCount + 1][attNameToken.countTokens()];

        int b1 = 0;

        while (attNameToken.hasMoreTokens()) // For each attribute.
        {
            df[0][b1] = attNameToken.nextToken(); // Add attribute name to name row.
            b1++;
        }

        Scanner sc = new Scanner(in);

        int a = 1;

        while (sc.hasNextLine()) // For each transaction line.
        {
            String transactionLine = sc.nextLine();
            StringTokenizer itemToken = new StringTokenizer(transactionLine, ", ");

            int b2 = 0;

            while (itemToken.hasMoreTokens()) // Add attribute value to item row.
            {
                df[a][b2] = itemToken.nextToken();
                b2++;
            }

            a++;
        }

        sc.close();

        return df;
    }
}
