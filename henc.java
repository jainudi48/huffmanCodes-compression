import java.io.FileInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.lang.IllegalStateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.File;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;


class HuffmanMinHeap
{
    private int capacity = 10;
    private int size = 0;
    
    Node[] items = new Node[capacity];

    public int getSize()
    {
        return size;
    }

    private int getLeftChildIndex(int parentIndex){return 2 * parentIndex + 1;}
    private int getRightChildIndex(int parentIndex){ return 2* parentIndex + 2;}
    private int getParentIndex(int childIndex){return (childIndex - 1) / 2;}
    
    private boolean hasLeftChild(int index){return getLeftChildIndex(index) < size;}
    private boolean hasRightChild(int index){return getRightChildIndex(index)<size;}
    private boolean hasParent(int index){return getParentIndex(index)>=0;}

    private Node leftChild(int index){return items[getLeftChildIndex(index)];}
    private Node rightChild(int index){return items[getRightChildIndex(index)];}
    private Node parent(int index){return items[getParentIndex(index)];}

    private void swap(int indexOne, int indexTwo)
    {
        Node temp = items[indexOne];
        items[indexOne] = items[indexTwo];
        items[indexTwo] = temp;
    }

    public void ensureCapacity()
    {
        if(size==capacity)
        {
            items = Arrays.copyOf(items, capacity * 2);
            capacity *= 2;
        }
    }

    public Node peepMin()
    {
        if(size == 0)
        {
            return null;
        }
        return items[0]; 
    }

    public Node extractMin()
    {
        if(size == 0)
        {
            return null;
        }
        Node item = items[0];
        items[0] = items[size - 1];
        size--;
        heapifyDown();
        return item;
    }
    public void add(Node item)
    {
        ensureCapacity();
        items[size] = item;
        size++;
        heapifyUp(); 
    } 
    public void heapifyUp()
    {
          int index = size - 1;
          while(hasParent(index) && parent(index).getFrequency() > items[index].getFrequency())
          {
              swap(getParentIndex(index), index);
              index = getParentIndex(index);
          }
    }
    public void heapifyDown()
    {
        int index = 0;
        while(hasLeftChild(index))
        {
            int smallerChildIndex = getLeftChildIndex(index);
            if(hasRightChild(index) && rightChild(index).getFrequency() < leftChild(index).getFrequency())
            {
                smallerChildIndex = getRightChildIndex(index);
            }
            if(items[index].getFrequency() < items[smallerChildIndex].getFrequency())
            {
                break;
            }
            else{
                swap(index, smallerChildIndex);
                index = smallerChildIndex;
            }
        }
    }
    public void print()
    {    
        for(int i=0; i<size; i++)
        {
            System.out.println("character : " + items[i].getChar() + "\t" + "frequency : " + items[i].getFrequency());
        }
    }
}

class henc
{
    static HuffmanMinHeap heap = null;
    static ArrayList<DataSet> dataList = new ArrayList<DataSet>();
    static ArrayList<DataSet> builtHuffmanDataList = new ArrayList<DataSet>();
    static String filePath; 
    static Read reader = null;
    static Write writer = null;
    public static void main(String[] args) {
        filePath = args[0];
        implementHuffman();

    }
    private static void implementHuffman()
    {
        heap = new HuffmanMinHeap();
        reader = new Read(filePath);
        writer = new Write(filePath + ".huf");
        readFile();
        addNodes();
        
        Node root = encode();
        String arr = "";
        // System.out.println("Value\tFrequency\tHuffman Code");
        writeTreeInFile(root);
        printHuffmanCode(root, arr);
        writeEncodedFile();

        try
        {
            reader.close();
            writer.close();
        }
        catch(IOException ioex)
        {
            System.out.println(ioex);
        }
    }
    
    private static void addNodes()
    {
        copyDataSetToExecute();
    }
    private static void printHeap()
    {
        heap.print();
    }

    private static Node encode()
    {
        Node min1 = null,  min2 = null;
        int sumOfFrequencies = 0;
        while(heap.getSize() > 1)
        {
            Node newNode = null;
            sumOfFrequencies = 0;
            min1 = heap.extractMin();
            sumOfFrequencies += min1.getFrequency();
            
            min2 = heap.extractMin();
            sumOfFrequencies += min2.getFrequency();
            
            newNode = new Node(sumOfFrequencies, min1, min2);
            heap.add(newNode);
        }
        return heap.peepMin();
    }
    private static void printHuffmanCode(Node root, String arr)
    {
        if(root.getLeftChild()!=null)
        {
            printHuffmanCode(root.getLeftChild(), arr+"0");
        }
        if(root.getRightChild()!=null)
        {
            printHuffmanCode(root.getRightChild(), arr+"1");
        }
        if(root.getLeftChild() == null && root.getRightChild() == null)
        {
            // System.out.print(root.getChar() + "\t" + root.getFrequency() + "\t\t" );
            // System.out.println(arr);
            // System.out.println("");
            
            DataSet ds = new DataSet();
            ds.setValue((int)(root.getChar()));
            ds.setFrequency(root.getFrequency());
            ds.setHuffmanCode(arr);
            builtHuffmanDataList.add(ds);
        }
    }

    private static void readFile()
    {
        String input = null;
        try{
            input = reader.readString();
            // System.out.print("File content : " +input);
            for (char var : input.toCharArray()) {
                addToList((int)var);
            }
        }
        catch(FileNotFoundException ex)
        {
            System.out.println(ex);
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
    }

    private static void addToList(int value)
    {
        boolean found = false;
        Iterator iterate = dataList.iterator();
        DataSet dataSet = new DataSet();
        DataSet ds = null;
        dataSet.setValue(value);
        dataSet.setFrequency(1);
        while(iterate.hasNext())
        {
            ds = (DataSet)iterate.next();
            if(ds.getValue() == value)
            {
                ds.setFrequency(ds.getFrequency() + 1);
                iterate.remove();
                found = true;
                break;
            }
        }
        if(found == true)
        {
            dataSet = ds;
        }
        dataList.add(dataSet);
    }

    private static void copyDataSetToExecute()
    {
        Iterator iterate = dataList.iterator();
        while(iterate.hasNext())
        {
            DataSet ds = (DataSet)iterate.next();
            heap.add(new Node(ds.getFrequency(), (char)(ds.getValue())));
        }
    }

    private static void writeTreeInFile(Node node)
    {
        if(node.isInternalNode)
        {
            writer.write(true);
            if(node.getRightChild() != null)
            {
                writeTreeInFile(node.getRightChild());
            }
            if(node.getLeftChild() != null)
            {
                writeTreeInFile(node.getLeftChild());
            }
        }
        else
        {
            writer.write(false);
            writer.write(node.getChar());
        }
    }    

    private static void writeEncodedFile()
    {
        String input = null;
        Iterator iterate = null;
        reader = new Read(filePath);
        try{
            input = reader.readString();
            writer.write(input.length());
            // System.out.print("length of input file = " + input.length());
            for (char var : input.toCharArray()) {
                iterate = builtHuffmanDataList.iterator();
                while(iterate.hasNext())
                {
                    DataSet ds = (DataSet)iterate.next();
                    if((char)ds.getValue() == var)
                    {
                        String codes = ds.getHuffmanCode();
                        for (char ch : codes.toCharArray()) {
                            if(ch == '0')
                            {
                                writer.write(false);
                                // System.out.print("0");
                            }
                            else
                            {
                                writer.write(true);
                                // System.out.print("1");
                            }
                        }
                        break;
                    }
                }
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}

class DataSet 
{
    private int value;
    private long frequency;
    private String huffmanCode;

    public String getHuffmanCode()
    {
        return huffmanCode;
    }
    public void setHuffmanCode(String huffmanCode)
    {
        this.huffmanCode = huffmanCode;
    }
    public int getValue()
    {
        return value;
    }
    public long getFrequency()
    {
        return frequency;
    }
    public void setValue(int value)
    {
        this.value = value;
    }
    public void setFrequency(long frequency)
    {
        this.frequency = frequency;
    }
}