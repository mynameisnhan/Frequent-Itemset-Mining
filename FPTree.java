/* Nhan Le
 * CSC 240
 * Project 1
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

// Inspired by https://github.com/relekang/fpgrowth/tree/master/src/main/java/no/rolflekang/fpgrowth.
// Based on pseudocode in Han et al.’s Data Mining: Concepts and Techniques, 3rd Edition.
class path
{
    public ArrayList<FPTreeNode> list;
    public ArrayList<String> itemset;
    public int weight;

    public path(FPTree tree, FPTreeNode node)
    {
        FPTreeNode currentNode = node;
        list = new ArrayList<FPTreeNode>();
        itemset = new ArrayList<String>();
        weight = node.count;
        currentNode = currentNode.parent;

        while (currentNode != tree.root)
        {
            list.add(currentNode);
            itemset.add(currentNode.data);
            currentNode = currentNode.parent;
        }
    }
}

class itemsetSort implements Comparator<String>
{
    public Hashtable<String, Integer> itemHash;

    public itemsetSort(Hashtable<String, Integer> itemHash)
    {
        this.itemHash = itemHash;
    }

    @Override
    public int compare(String s1, String s2)
    {
        if (itemHash.get(s1) > itemHash.get(s2))
            return -1;

        else if (itemHash.get(s1) < itemHash.get(s2))
            return 1;

        return 0;
    }
}

public class FPTree
{
    public ArrayList<String> suffixPattern = new ArrayList<String>();
    public FPTreeNode root;
    public Hashtable<String, FPTreeNode> headerTable = new Hashtable<String, FPTreeNode>();

    public FPTree()
    {
    }

    // Tree.
    public FPTree(String[][] db, int threshold, Hashtable<String,Integer> itemHash)
    {
        root = new FPTreeNode();
        FPTreeNode currentNode;

        // For each item ...
        for (int a = 0; a < db.length; a++)
        {
            ArrayList<String> itemset = new ArrayList<String>();

            // Add to itemset if unique.
            for (int b = 0; b < db[a].length; b++)
            {
                String item = db[a][b];

                if (itemHash.containsKey(item))
                    itemset.add(item);
            }

            // Sort itemset.
            Collections.sort(itemset, new itemsetSort(itemHash));

            currentNode = root;

            // Add to tree.
            add(currentNode, itemset);

        }
    }

    private void add(FPTreeNode currentNode, ArrayList<String> itemset)
    {
        Iterator<String> it = itemset.iterator();

        currentNode = root;

        while (it.hasNext())
        {
            String item = it.next();

            if (currentNode.hasChild(item))
            {
                FPTreeNode next = currentNode.getChild(item);
                next.count++;
                currentNode = next;
            }

            else
            {
                FPTreeNode newNode = new FPTreeNode();
                newNode.data = item;
                newNode.count = 1;
                newNode.parent = currentNode;
                currentNode.children.add(newNode);
                currentNode = newNode;

                if (headerTable.containsKey(item))
                {
                    FPTreeNode previous_head = headerTable.get(item);
                    newNode.sibling = previous_head;
                    headerTable.put(item, newNode);
                }

                else
                    headerTable.put(item, newNode);
            }
        }

    }

    // Conditional tree.
    public FPTree conditionalTree(String inputItem, int supportThreshold)
    {
        FPTree conditionalTree = new FPTree();
        conditionalTree.root = new FPTreeNode();
        conditionalTree.suffixPattern.addAll(this.suffixPattern);
        conditionalTree.suffixPattern.add(inputItem);

        FPTreeNode currentNode = headerTable.get(inputItem);
        Hashtable<String,Integer> itemHash = new Hashtable<String,Integer>();
        ArrayList<path> pathList = new ArrayList<path>();

        while (currentNode != null)
        {
            pathList.add(new path(this, currentNode));
            currentNode = currentNode.sibling;
        }

        for (path path : pathList)
        {
            for (FPTreeNode n : path.list)
            {
                if (itemHash.containsKey(n.data))
                    itemHash.put(n.data, itemHash.get(n.data) + path.weight);

                else itemHash.put(n.data, path.weight);
            }
        }

        ArrayList<String> infrequentList = new ArrayList<String>();
        Iterator<Entry<String, Integer>> it = itemHash.entrySet().iterator();

        while (it.hasNext())
        {
            Entry<String, Integer> e = it.next();

            if (e.getValue() < supportThreshold)
            {
                infrequentList.add(e.getKey());
                it.remove();
            }
        }

        for (path path : pathList)
        {
            for (int i = 0; i < path.list.size(); i++)
                if (infrequentList.contains(path.list.get(i).data) == true)
                {
                    path.list.remove(path.list.get(i));
                    i--;
                }

            for (int i = 0; i < path.itemset.size(); i++)
            {
                String s = path.itemset.get(i);

                if (infrequentList.contains(s) == true)
                {
                    path.itemset.remove(s);
                    i--;
                }
            }

            Collections.sort(path.itemset, new itemsetSort(itemHash));

            FPTreeNode currentConditionalNode = conditionalTree.root;

            Iterator<String> itItemset = path.itemset.iterator();

            while (itItemset.hasNext())
            {
                String item = itItemset.next();

                if (currentConditionalNode.hasChild(item))
                {
                    FPTreeNode next = currentConditionalNode.getChild(item);
                    next.count += path.weight;
                    currentConditionalNode = next;
                }

                else
                {
                    FPTreeNode newNode = new FPTreeNode();
                    newNode.data = item;
                    newNode.count = path.weight;

                    if (conditionalTree.headerTable.containsKey(item))
                    {
                        FPTreeNode previous_head = conditionalTree.headerTable.get(item);
                        newNode.sibling = previous_head;
                        conditionalTree.headerTable.put(item, newNode);
                    }

                    else
                        conditionalTree.headerTable.put(item, newNode);

                    newNode.parent = currentConditionalNode;
                    currentConditionalNode.children.add(newNode);
                    currentConditionalNode = newNode;
                }
            }
        }

        return conditionalTree;
    }
}
