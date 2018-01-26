import java.io.IOException;
public class hdec
{
    private static Read reader;
    private static Write writer;
    private static String filePath;
    public static void main(String args[])
    {
        if(args.length != 1)
        {
            System.out.println("Invalid parameters received!");
            return;
        }
        else
        {
            filePath = args[0];
        }
        implementDecode();
    }

    private static void implementDecode()
    {
        reader = new Read(filePath);
        writer = new Write(filePath.substring(0,filePath.length()-4));
        Node rootNode = traceTreeFromFile();
        readHuffmanCodesFromFile(rootNode);
        try
        {
            reader.close();
            writer.close();
        }
        catch(IOException ioex)
        {
            // System.out.println("in func implementDecode");
            System.out.println(ioex);
        }
    }
	
    private static void readHuffmanCodesFromFile(Node rootNode)
    {
        Node node = null;
        boolean huffmanBit = false;
        int count = 0;
        try
        {
            int fileLength = reader.readInt();
            count = 0;
            // System.out.println("file length : " + fileLength);
            for(int index=0;index<fileLength-1;index++)
            {
                node = rootNode;
                count++;
                while(node.isInternalNode == true)
                {
                    huffmanBit = reader.readBit();
                    if(huffmanBit == true)
                    {
                        node = node.getRightChild();
                    }
                    else
                    {
                        node = node.getLeftChild();
                    }
                }
                writer.write(node.getChar());
                // System.out.print(node.getChar());
            }
        }
        catch(Exception ex)
        {
            // System.out.println("in func readHuffmanCodes " + count);
            System.out.println(ex);
        }
    }
    
    private static Node traceTreeFromFile()
    {
        Node left = null, right = null;
        boolean isInternalNode;
        try
        {
            isInternalNode = reader.readBit();
            if(isInternalNode == false)
            {
                return new Node(-1, reader.readChar());
            }
            else
            {
                left = traceTreeFromFile();
                right = traceTreeFromFile();
                return new Node(-1, right, left);
            }
        }
        catch(Exception ex)
        {
            // System.out.println("in func traceTreeFromFile");
            System.out.println(ex);
        }
        return null;
    }
}