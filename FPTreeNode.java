/* Nhan Le
 * CSC 240
 * Project 1
 */

import java.util.ArrayList;

// Inspired by https://github.com/relekang/fpgrowth/tree/master/src/main/java/no/rolflekang/fpgrowth.
public class FPTreeNode
{
    public ArrayList<FPTreeNode> children;
    public String data;
    public int count;
    public FPTreeNode sibling;
    public FPTreeNode parent;

    public FPTreeNode()
    {
        children = new ArrayList<FPTreeNode>();
        count = 0;
        data = "";
    }

    public boolean hasChild(String child)
    {
        boolean returnVal = false;

        for (FPTreeNode node : children)
            if (node.data.equals(child))
                return returnVal;

        return returnVal;
    }

    public FPTreeNode getChild(String child)
    {
        FPTreeNode returnNode =  null;

        for (FPTreeNode node : children)
            if (node.data.equals(child))
                returnNode = node;

        return returnNode;
    }
}