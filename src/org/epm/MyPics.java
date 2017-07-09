package org.epm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.naming.ConfigurationException;
import javax.security.auth.login.Configuration;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;


public class MyPics {

	static private String GLOBAL_ConfigFileName = "src/picture.properties";
	static public boolean GLOBAL_Debug = false;
	
	static private FrameConfiguration frameConfig = new FrameConfiguration();
	Wini config = null;

	public static void main(String[] args) {

		MyPics myPictures = new MyPics();
		// TODO Picture Folders have bad file names
		String picFolder = myPictures.getConfig("Pictures", "folder");
		
		GLOBAL_Debug = (myPictures.getConfig("Environment", "debug").toUpperCase().trim()) == "YES" ? true : false;
		
		for( int x=0; x<args.length; x++){
			if (GLOBAL_Debug) System.out.printf("arg %d: %s\n", x, args[x]);
			}

		JFrame myFrame = myPictures.createFrame();

      	while (true) {
            try {
        		frameConfig.listFilesForFolder(picFolder);
        		if (GLOBAL_Debug) frameConfig.toConsole();

                JLabel myLabel = new JLabel();
                myLabel.setHorizontalAlignment(JLabel.CENTER);
                myLabel.setBackground(Color.black);
                myLabel.setOpaque(true);
                myFrame.add(myLabel);
                myFrame.setBackground( Color.black );
                
                int growloops = (int)new Integer(myPictures.getConfig("Presentation",  "growloops")).intValue();
                
			    for (File f : frameConfig.getPictureFiles() ) {
			    	
			    	BufferedImage img=ImageIO.read(new File(f.getPath()));
		            try {
		            	for (int n = 0; n<=growloops; n++ ) {
		            		
			            	Image dimg = myPictures.setImageDimensions(myFrame.getWidth()+n, myFrame.getHeight()+n, img);
					    	ImageIcon icon=new ImageIcon(dimg);
					        myLabel.setIcon(icon);
		            	}
			            } catch (Exception e) {
			            	e.printStackTrace();
			    	}
			        
					try {
						long l = new Long(myPictures.getConfig("Presentation", "delay")).longValue();
						Thread.sleep(l);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			    }
		    
            } catch (IOException ioe) {
            	ioe.printStackTrace();
    	}
			
		}
		
	}
	
	// Create the window frame
	public JFrame createFrame() {
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		JFrame myFrame = new JFrame("PictureFrame");
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel myLabel = new JLabel();
		
		myFrame.getContentPane().add(myLabel,  BorderLayout.CENTER);
		myFrame.pack();
		int h = (int)(screenSize.getHeight() * 400 ) / 1000;
		int w = (int)(screenSize.getWidth() * 300 ) / 1000;
		myFrame.setSize( w, h);
		
		myFrame.setVisible(true);
		return myFrame;
	}
	
	
    public Image setImageDimensions( int windowWidth, int windowHeight, BufferedImage img ) throws Exception {
    	
    	Image dimg = null;
    	int proposedImageH, proposedImageW;
    	
    	double ratioHeight = (double)img.getHeight() / (double)windowHeight;
    	double ratioWidth = (double)img.getWidth() / (double)windowWidth;
    	double ratioImage = (double)img.getHeight() / (double)img.getWidth();
    	
    	if ( windowHeight < img.getHeight() || windowWidth < img.getWidth() ) {
    	
	    	if ( ratioHeight < ratioWidth ) {
	    		proposedImageW = windowWidth;
	    		proposedImageH = (int)(windowWidth * ratioImage );
	    		
	    	} else {
	    		proposedImageH = windowHeight;
	    		proposedImageW = (int)(windowHeight / ratioImage );
	    		
	    	} } else {
    		proposedImageW = img.getWidth();
    		proposedImageH = img.getHeight();
    		
    	}
    	
    	if (GLOBAL_Debug ) System.out.printf("Window: %d, %d Image: %d, %d becomes: %d, %d\n", windowWidth, windowHeight, img.getWidth(), img.getHeight(), proposedImageW, proposedImageH );
    	
    	dimg = img.getScaledInstance(proposedImageW, proposedImageH, Image.SCALE_SMOOTH);
    	
    	return dimg;
    }

    public String getConfigFile() {
    	return GLOBAL_ConfigFileName;
    }
    
    public String getConfig( String section, String configItem ) {
    	String rValue = "";
    	
    	try {
    		
    		if (config == null ) {
    			config = new Wini( new File(getConfigFile()));
    		}
			
    		rValue = config.get(section, configItem);
    		
			
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return rValue;
    }
    
}
