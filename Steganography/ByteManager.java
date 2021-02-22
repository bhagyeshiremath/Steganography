package steganography;

class ByteManager
{
    static int[] splitByte(int n)
    {//104 : 3,2,0
        int arr[] = new int[3];
        arr[0] = n >> 5;
        arr[1] = (n & 0x1C) >> 2;//0x1C : 11100
        arr[2] = (n & 0x3);//0x3 : 11
        return arr;
    }

    static int embed(int data, int toEmbed, int flag)
    {
        if(flag == 1)
            return (data & ~0x7) | toEmbed;    
        return (data & ~0x3) | toEmbed;    
    }

    static int extract(int data, int flag)
    {
        if(flag == 1)
            return data & 0x7;
        return data & 0x3;    
    }

    static int mergeBits(int x, int y, int z)
    {
        return (((x<<3)| y)<<2)|z;   
    }
}