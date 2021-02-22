package steganography;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;
import javax.imageio.ImageIO;
class Embedder 
{
    File srcImg;
    File fileToEmbed;
    File trgtImg;
    Cryptography crypt;
    
    Embedder(String srcImg, String fileToEmbed, String trgtImg, String password) throws Exception
    {
        this.srcImg = new File(srcImg);
        this.fileToEmbed = new File(fileToEmbed);
        this.trgtImg = new File(trgtImg);
        
        if(! this.srcImg.exists())
            throw new Exception(srcImg + " doesnt exist");
        if(! this.fileToEmbed.exists())
            throw new Exception(fileToEmbed + " doesnt exist");
        
        crypt = new Cryptography(password);
    }
    
    void embed() throws Exception
    {
        //load the srcImg in memory
        BufferedImage vessel = ImageIO.read(srcImg);
        
        //calculate the embedding capacity
        int w, h;
        long capacity;
        long need;

        w = vessel.getWidth();
        h = vessel.getHeight();
        capacity = (long)w * h;
        need = fileToEmbed.length() + HeaderManager.HEADER_SIZE;
        if(need > capacity)
            throw new Exception("File to embed is too large to fit in the image");

        //getting ready to embed
        //1) generate the header
        String header = HeaderManager.generateHeader(fileToEmbed.getName(), fileToEmbed.length());
        //2) retrieve the pixel matrix (raster)
        WritableRaster raster = vessel.getRaster();
        //3) open the file to embed for reading
        FileInputStream fin = new FileInputStream(fileToEmbed);
        
        //lets embed
        int i, j;
        int r, g, b;
        long x = 0;
        int data;
        int arr[];
        
        for(j =0 ; j < h && x < need ; j++)
        {
            for(i =0 ; i < w && x < need ; i++)
            {
                //per pixel
                r = raster.getSample(i, j, 0); //get red band of pixel i,j 
                g = raster.getSample(i, j, 1); //get green band of pixel i,j 
                b = raster.getSample(i, j, 2); //get blue band of pixel i,j 

                //byte to embed
                if(x < HeaderManager.HEADER_SIZE)
                    data = header.charAt((int)x);
                else
                    data = fin.read();
                
                //encrypt
                data = crypt.encrypt(data);
                
                //embed
                arr = ByteManager.splitByte(data);
                r = ByteManager.embed(r, arr[0], 1);
                g = ByteManager.embed(g, arr[1], 1);
                b = ByteManager.embed(b, arr[2], 0);
                
                //update the raster
                raster.setSample(i, j, 0, r);//red band
                raster.setSample(i, j, 1, g);//green band
                raster.setSample(i, j, 2, b);//blue band
                
                //counter update
                x++;
            }//for(i
        }//for(j
        
        //update raster into the vessel
        vessel.setData(raster);
        
        //save back the vessel
        ImageIO.write(vessel, "PNG", trgtImg);
        
        //close the file
        fin.close();
    }
    
}
