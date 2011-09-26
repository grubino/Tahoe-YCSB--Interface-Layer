import org.lafs.TahoeLAFSConnection;

import java.io.InputStream;
import java.io.File;

import java.util.Vector;
import java.util.Iterator;

public class TahoeLAFSConnectionTest {

    public static void main(String[] args) throws Exception {
	TahoeLAFSConnection connection = new TahoeLAFSConnection(args[0]
								 , Integer.parseInt(args[1]));

	String location = "/URI:DIR2:lfdwla6opaupuyf2nmwtopxvxa:cvltcnx2undbkydkm5h2vta4z64p2vs3efgzvy4liydafof37p5a/test";
 		

	File file = new File("./test");
	String fileCap = connection.put(location
					, "text/plain"
					, file);
	
	InputStream i = connection.get(location);

	int bytesRead = 0;
	String output = new String();

	try {
	    do {
		byte[] responseContentBuffer = new byte[1024];
		bytesRead = i.read(responseContentBuffer, 0, 1024);
		output = (output + new String(responseContentBuffer));
	    } while(bytesRead >= 0);
	}
	finally {
	    i.close();
	}

	System.out.println(output);

	connection.del(location);

	String dircap = connection.mkdir(new String());

	System.out.println(dircap);
	
	String originalDircap = dircap;
	
	dircap = connection.mkdir(dircap + "/test1");
	System.out.println(dircap);
	dircap = connection.mkdir(dircap + "/test2");
	System.out.println(dircap);
	dircap = connection.mkdir(dircap + "/test3");
	System.out.println(dircap);
	dircap = connection.mkdir(dircap + "/test4");
	System.out.println(dircap);

	Vector<String> fileList = connection.list("/" + originalDircap);

	Iterator file_it = fileList.iterator();
	while(file_it.hasNext()) {
	    System.out.println(file_it.next());
	}

    }
    
}