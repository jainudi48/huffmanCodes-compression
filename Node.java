public class Node
{
    private long frequency;
    private char ch;
    public boolean isInternalNode = false;
    private Node leftChild;
    private Node rightChild;


    public void setFrequency(long frequency){this.frequency = frequency;}
    public void setChar(char ch){this.ch = ch;}
    public void setLeftChild(Node leftChild){this.leftChild = leftChild;}
    public void setRightChild(Node rightChild){this.rightChild = rightChild;}

    
    public long getFrequency(){return this.frequency;}
    public char getChar(){return this.ch;}
    public Node getLeftChild(){return this.leftChild;}
    public Node getRightChild(){return this.rightChild;}
    public Node(long frequency, Node leftChild, Node rightChild)
    {
        if(!(leftChild == null && rightChild == null))
        {
            isInternalNode = true;
        }
        setFrequency(frequency);
        setLeftChild(leftChild);
        setRightChild(rightChild);
    }
    public Node(long frequency, char ch, Node leftChild, Node rightChild)
    {   
        setFrequency(frequency);
        setChar(ch);
        setLeftChild(leftChild);
        setRightChild(rightChild);
    }

    public Node(long frequency, char ch)
    {
        setFrequency(frequency);
        setChar(ch);
        setLeftChild(null);
        setRightChild(null);
    }
    public void print()
    {
        System.out.println("Data : " + this.getChar() + "\tFrequency : " + this.getFrequency() + "\tLeftChild : " + this.getLeftChild().getChar() + "\tRightChild : " + this.getRightChild().getChar());
    }
}