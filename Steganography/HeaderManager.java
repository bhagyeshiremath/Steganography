package steganography;

//A header stores metadata.
//A system must be able to generate from given set of metadata.
//Also parse back the header to retrieve the metadata.

class HeaderManager
{
    private static final int FILENAME_SIZE = 15;
    private static final int FILESIZE_SIZE = 10;
    static final int HEADER_SIZE = 25;

    private static String processFileName(String fileName)
    {
        if(fileName.length() == FILENAME_SIZE)
            return fileName;
        else if(fileName.length() > FILENAME_SIZE)
        {
            //trim: MapOfArunachalPradesh.png ---> "MapOfArunac.png"
            int pos = fileName.lastIndexOf(".");
            if(pos == -1)
            {//"." not found 
                return fileName.substring(0,FILENAME_SIZE);
            }
            else
            {//"." found at pos
                String name = fileName.substring(0, pos);//MapOfArunachalPradesh
                String extension = fileName.substring(pos);//.png
                name = name.substring(0, FILENAME_SIZE - extension.length());//MapOfArunac
                return name + extension;
            }
        }
        else //if(fileName.length() < FILENAME_SIZE)
        {
            //pad: "a.png" ---> "a.png##########"
            StringBuffer sbuff = new StringBuffer(fileName);
            while(sbuff.length() < FILENAME_SIZE)
                sbuff.append('#');
            return sbuff.toString();
        }
    } 

    private static String processFileSize(long fileSize) throws Exception
    {
        String fs = String.valueOf(fileSize);//long to String

        if(fs.length() == FILESIZE_SIZE)
            return fs;
        else if(fs.length() > FILESIZE_SIZE)
        {
            //raise an exception
            throw new Exception("FileSize " + fs + " is beyond embedding capacity"); 
        }
        else //if(fs.length() < FILESIZE_SIZE)
        {
            //pad: "34356" ---> "34356#####"
            StringBuffer sbuff = new StringBuffer(fs);
            while(sbuff.length() < FILESIZE_SIZE)
                sbuff.append('#');

            return sbuff.toString();
        }
    }      

    static String generateHeader(String fileName, long fileSize) throws Exception
    {//fixed sized header
        return processFileName(fileName) + processFileSize(fileSize);     
    }

    static String [] extract(String hdr)
    {   //MapOfArunac.png34356#####
        //a.png##########34356#####
        String arr[] = new String[2];
 
        String fileName = hdr.substring(0, FILENAME_SIZE);
        //MapOfArunac.png or a.png##########
        arr[0] = fileName.replaceAll("#", ""); 
   
        String fileSize = hdr.substring(FILENAME_SIZE); 
        //34356#####
        arr[1] = fileSize.replaceAll("#", ""); 
  
        return arr; 
    }
 
}//HeaderManager