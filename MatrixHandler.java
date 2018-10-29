/* Nhan Le
 * CSC 240
 * Project 1
 */

public class MatrixHandler
{
    // Delete row from 2D array.
    public static String[][] deleteRow(String[][] df, int rowNum)
    {
        String[][] newArr = new String[df.length - 1][df[0].length];

        // Copy array from first row to row before deleted row.
        for (int a = 0; a < rowNum; a++)
            for (int b = 0; b < df[a].length;  b++)
                newArr[a][b] = df[a][b];

        // Copy array from row after deleted row to last row.
        for (int a = rowNum + 1; a < df.length; a++)
            for (int b = 0; b < df[a].length;  b++)
                newArr[a-1][b] = df[a][b];

        return newArr;
    }

    // Transpose matrix.
    public static String[][] transposeArray(String[][] arr)
    {
        String [][] tempArr = new String[arr[0].length][arr.length];

        for (int i = 0; i < arr.length; i++)
            for (int j = 0; j < arr[0].length; j++)
                tempArr[j][i] = arr[i][j];

        return tempArr;
    }

    // Print matrix.
    public static void printMatrix(String[][] matrix)
    {
        for (int i = 0; i < matrix.length; i++)
        {
            System.out.print(matrix[i][0]);

            for (int j = 1; j < matrix[i].length; j++)
                System.out.print(", " + matrix[i][j]);

            System.out.println();
        }
    }
}