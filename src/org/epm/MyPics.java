package org.epm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class MyPics {

	static private FrameConfiguration frameConfig = new FrameConfiguration();

	public static void main(String[] args) {

		for( int x=0; x<args.length; x++){
			System.out.printf("arg %d: %s\n", x, args[x]);
			}

		MyPics myPictures = new MyPics();
		JFrame myFrame = myPictures.createFrame();
		
		frameConfig.listFilesForFolder(args[0]);
		frameConfig.toConsole();


        JLabel myLabel = new JLabel();
        myLabel.setHorizontalAlignment(JLabel.CENTER);
        myLabel.setBackground(Color.black);
        myLabel.setOpaque(true);
        myFrame.add(myLabel);
        myFrame.setBackground( Color.black );
		
      	while (true) {
            try {
			    for (File f : frameConfig.getPictureFiles() ) {
			    	
			    	BufferedImage img=ImageIO.read(new File(f.getPath()));
		            try {
		            	Image dimg = myPictures.setImageDimensions(myFrame.getWidth(), myFrame.getHeight(), img);
				    	ImageIcon icon=new ImageIcon(dimg);
				        myLabel.setIcon(icon);
			            } catch (Exception e) {
			            	e.printStackTrace();
			    	}
			        
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
			    }
		    
            } catch (IOException ioe) {
            	ioe.printStackTrace();
    	}
			
		}
		
	}
	
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
	
    public void displayImage(JFrame myFrame, String picFile ) throws IOException
    {
        BufferedImage img=ImageIO.read(new File(picFile));
        ImageIcon icon=new ImageIcon(img);

        myFrame.setLayout(new FlowLayout());
        JLabel lbl=new JLabel();
        lbl.setIcon(icon);
        
        myFrame.add(lbl);
        myFrame.setVisible(true);
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
    	
    	System.out.printf("Window: %d, %d Image: %d, %d becomes: %d, %d\n", windowWidth, windowHeight, img.getWidth(), img.getHeight(), proposedImageW, proposedImageH );
    	
    	dimg = img.getScaledInstance(proposedImageW, proposedImageH, Image.SCALE_SMOOTH);
    	
    	return dimg;
    }

}
