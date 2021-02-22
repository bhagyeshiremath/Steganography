package steganography;

public class Main 
{
    public static void main(String[] args) 
    {
        try
        {
            String password = "This is a strong password";
            
            Embedder emb = new Embedder("g:/Koala.jpg", "c:/java/Misc.zip", "d:/new_Koala.png", password);
            emb.embed();
            System.out.println("Embedding Success");
            
            Extractor extr = new Extractor("d:/new_Koala.png", "d:/", password);
            extr.extract();
            System.out.println("Extraction Success");
        }
        catch(Exception ex)
        {
            System.out.println("Err: "  + ex.getMessage());
            //ex.printStackTrace();
        }
    }
  
}
