package steganography;

public class Cryptography 
{
    private String password;
    private int index;
    private int pLength;
    
    Cryptography(String password)
    {
        this.password = password;
        index = -1;
        pLength = this.password.length();
    }
    
    int encrypt(int data)
    {
        index= (index +1) %pLength;
        return data ^ password.charAt(index);
    }
    
    
    int decrypt(int data)
    {
        index= (index +1) %pLength;
        return data ^ password.charAt(index);
    }
}
