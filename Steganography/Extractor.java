package steganography;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public class Extractor 
{
    File imgFile, trgtFolder;
    Cryptography crypt;
    
    Extractor(String imgFile, String trgtFolder, String password) throws Exception
    {
        this.imgFile = new File(imgFile);
        this.trgtFolder = new File(trgtFolder);
        
        if(! this.imgFile.exists())
            throw new Exception(imgFile + " doesnt exists");
        
        if(! this.trgtFolder.exists())
            this.trgtFolder.mkdirs();//create it
        else
        {
            if(!this.trgtFolder.isDirectory())
                throw new Exception(trgtFolder + " is not a directory");
        }
        
        crypt = new Cryptography(password);
    }//Extract
    
    void extract() throws Exception
    {
        //load the image in memory
        BufferedImage vessel = ImageIO.read(imgFile);
        //retrieve the pixel matrix (raster)
        Raster raster = vessel.getData();
        //fetch
        int i, j, w, h;
        int r, g, b;
        int data;
        long x = 0, need = HeaderManager.HEADER_SIZE;
        
        
        w = vessel.getWidth();
        h = vessel.getHeight();
        StringBuffer hdr = new StringBuffer();
        String fileName = null;
        long fileSize = 0;
        FileOutputStream fout = null;
        
        for(j =0 ; j < h && x <= need; j++)
        {
            for(i =0 ; i < w && x <= need ; i++)
            {
                //per pixel, extract the bands
                r = raster.getSample(i, j, 0);//red
                g = raster.getSample(i, j, 1);//green
                b = raster.getSample(i, j, 2);//blue
        
                //extract bits and merge to form the byte
                r = ByteManager.extract(r, 1);
                g = ByteManager.extract(g, 1);
                b = ByteManager.extract(b, 0);
                data = ByteManager.mergeBits(r, g, b);
                
                //decrypt 
                data = crypt.decrypt(data);
                
                if(x < HeaderManager.HEADER_SIZE)
                    hdr.append((char)data);
                else if(x == HeaderManager.HEADER_SIZE)
                {//header is ready
                    String arr[] = HeaderManager.extract(hdr.toString());
                    fileName = arr[0];
                    fileSize = Long.parseLong(arr[1]);
                    System.out.println("* " + fileName);
                    System.out.println("* " + fileSize);
                    need = need + fileSize -1;
                    fout = new FileOutputStream(trgtFolder.getAbsolutePath() +  "/" + fileName);
                }
                
                if(fileName != null)
                    fout.write(data);

                x++;
            }//for(i
        }//for(j
    
        fout.flush();//buffered data is transferred into the disk.
        fout.close();
    }//extract
}
