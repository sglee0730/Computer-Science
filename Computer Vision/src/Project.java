import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.opencv.core.*;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.Imgproc;

class ProjectFunc {
	JFrame frame = new JFrame("Computer Vision"); // 프레임 생성
	Container container = frame.getContentPane();
	
	BufferedImage ci = null;
	BufferedImage ni = null;
	BufferedImage co = null;
	BufferedImage no = null;
	
	public ProjectFunc() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		Dimension monitor = Toolkit.getDefaultToolkit().getScreenSize(); // 모니터 크기 가져오기
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(monitor.width, monitor.height);
		frame.setVisible(true);		
		
		JPanel menuBar = new JPanel();
		menuBar.setLayout(new FlowLayout());
		
		JTextField filterSize = new JTextField(15);
		filterSize.setText("필터 크기를 입력하세요.");
		menuBar.add(filterSize);
		
		JButton mean = new JButton("Mean Filter");
		mean.setBackground(new Color(170, 250, 190));
		menuBar.add(mean);
		
		JButton median = new JButton("Median Filter");
		median.setBackground(new Color(250, 210, 170));
		menuBar.add(median);
		
		JButton gaussian = new JButton("Gaussian Filter");
		gaussian.setBackground(new Color(170, 250, 250));
		menuBar.add(gaussian);
		
		JButton highPass1 = new JButton("High Pass 3 x 3");
		highPass1.setBackground(new Color(200, 170, 250));
		JButton highPass2 = new JButton("High Pass 5 x 5");
		highPass2.setBackground(new Color(200, 170, 250));
		JButton highPass3 = new JButton("High Pass 9 x 9");
		highPass3.setBackground(new Color(200, 170, 250));
		menuBar.add(highPass1);
		menuBar.add(highPass2);
		menuBar.add(highPass3);
		
		JButton highEmphasis1 = new JButton("High Emphasis 3 x 3");
		highEmphasis1.setBackground(new Color(250, 170, 190));
		JButton highEmphasis2 = new JButton("High Emphasis 5 x 5");
		highEmphasis2.setBackground(new Color(250, 170, 190));
		menuBar.add(highEmphasis1);
		menuBar.add(highEmphasis2);
		
		frame.add(menuBar, BorderLayout.NORTH);
		
		JPanel imgBar = new JPanel();
		imgBar.setLayout(new GridLayout(2, 2, 5, 5));
		JLabel cleanInput = new JLabel();
		JLabel cleanOutput = new JLabel();
		JLabel noiseInput = new JLabel();
		JLabel noiseOutput = new JLabel();
		imgBar.add(cleanInput);
		imgBar.add(cleanOutput);
		imgBar.add(noiseInput);
		imgBar.add(noiseOutput);
		frame.add(imgBar, BorderLayout.CENTER);
		
		Mat clean_image = Imgcodecs.imread("clean_image.png", 0);
		ci = toBufferedImage(clean_image);
		int clean_height = clean_image.rows();
		int clean_width = clean_image.cols();
		
		Mat noise_image = Imgcodecs.imread("noise_image.png", 0);
		ni = toBufferedImage(noise_image);
		int noise_height = noise_image.rows();
		int noise_width = noise_image.cols();
		
		Mat clean_output = new Mat(clean_height, clean_width, CvType.CV_8UC1);
		Mat noise_output = new Mat(noise_height, clean_width, CvType.CV_8UC1);
		
		cleanInput.setIcon(new ImageIcon(ci));
		noiseInput.setIcon(new ImageIcon(ni));
		
		mean.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String str = filterSize.getText();
				int size = Integer.parseInt(str);
				
				Imgproc.blur(clean_image, clean_output, new Size(size, size));
				Imgproc.blur(noise_image, noise_output, new Size(size, size));
				
				BufferedImage co = toBufferedImage(clean_output);
				BufferedImage no = toBufferedImage(noise_output);
				
				cleanOutput.setIcon(new ImageIcon(co));
				noiseOutput.setIcon(new ImageIcon(no));
			}
		});
		
		median.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String str = filterSize.getText();
				int size = Integer.parseInt(str);
				
				Imgproc.medianBlur(clean_image, clean_output, size);
				Imgproc.medianBlur(noise_image, noise_output, size);
				
				BufferedImage co = toBufferedImage(clean_output);
				BufferedImage no = toBufferedImage(noise_output);
				
				cleanOutput.setIcon(new ImageIcon(co));
				noiseOutput.setIcon(new ImageIcon(no));
			}
		});
		
		gaussian.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String str = filterSize.getText();
				int size = Integer.parseInt(str);
				
				Imgproc.GaussianBlur(clean_image, clean_output, new Size(size, size), 1.5);
				Imgproc.GaussianBlur(noise_image, noise_output, new Size(size, size), 1.5);
				
				BufferedImage co = toBufferedImage(clean_output);
				BufferedImage no = toBufferedImage(noise_output);
				
				cleanOutput.setIcon(new ImageIcon(co));
				noiseOutput.setIcon(new ImageIcon(no));
			}
		});
		
		highPass1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int filterSize = 3;
				double[][] filter = {
						{-1, -1, -1},
						{-1, 8, -1},
						{-1, -1, -1}
				};
				BufferedImage tempInput = null, tempOutput = null;
				try {
					tempInput = ImageIO.read(new File("clean_image.png"));
				} catch(Exception exception) {
				}
				tempOutput = new BufferedImage(tempInput.getWidth(), tempInput.getHeight(), BufferedImage.TYPE_INT_RGB);
				
				HighPassFilter(tempInput, tempOutput, tempInput.getWidth(), tempInput.getHeight(), filterSize, filter);
				
				cleanOutput.setIcon(new ImageIcon(tempOutput));
			}
		});
		
		highPass2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int filterSize = 5;
				double[][] filter = {
						{0, 0, -1, 0, 0},
						{0, -1, -2, -1, 0},
						{-1, -2, 16, -2, -1},
						{0, -1, -2, -1, 0},
						{0, 0, -1, 0, 0}
				};
				BufferedImage tempInput = null, tempOutput = null;
				try {
					tempInput = ImageIO.read(new File("clean_image.png"));
				} catch(Exception exception) {
				}
				tempOutput = new BufferedImage(tempInput.getWidth(), tempInput.getHeight(), BufferedImage.TYPE_INT_RGB);
				
				HighPassFilter(tempInput, tempOutput, tempInput.getWidth(), tempInput.getHeight(), filterSize, filter);
				
				cleanOutput.setIcon(new ImageIcon(tempOutput));
			}
		});
		
		highPass3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int filterSize = 9;
				double[][] filter = {
						{0, 0, -3, -2, -2, -2, -3, 0, 0},
						{0, -2, -3, -5, -5, -5, -3, -2, 0},
						{-3, -3, -5, -3, 0, -3, -5, -3, -3},
						{-2, -5, -3, 12, 23, 12, -3, -5, -2},
						{-2, -5, 0, 23, 40, 23, 0, -5, -2},
						{-2, -5, -3, 12, 23, 12, -3, -5, -2},
						{-3, -3, -5, -3, 0, -3, -5, -3, -3},
						{0, -2, -3, -5, -5, -5, -3, -2, 0},
						{0, 0, -3, -2, -2, -2, -3, 0, 0}
				};
				BufferedImage tempInput = null, tempOutput = null;
				try {
					tempInput = ImageIO.read(new File("clean_image.png"));
				} catch(Exception exception) {
				}
				tempOutput = new BufferedImage(tempInput.getWidth(), tempInput.getHeight(), BufferedImage.TYPE_INT_RGB);
				
				HighPassFilter(tempInput, tempOutput, tempInput.getWidth(), tempInput.getHeight(), filterSize, filter);
				
				cleanOutput.setIcon(new ImageIcon(tempOutput));
			}
		});
		
		highEmphasis1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int filterSize = 3;
				double[][] filter = {
						{-1, -1, -1},
						{-1, 9, -1},
						{-1, -1, -1}
				};
				BufferedImage tempInput = null, tempOutput = null;
				try {
					tempInput = ImageIO.read(new File("clean_image.png"));
				} catch(Exception exception) {
				}
				tempOutput = new BufferedImage(tempInput.getWidth(), tempInput.getHeight(), BufferedImage.TYPE_INT_RGB);
				
				HighPassFilter(tempInput, tempOutput, tempInput.getWidth(), tempInput.getHeight(), filterSize, filter);
				
				cleanOutput.setIcon(new ImageIcon(tempOutput));
			}
		});
		
		highEmphasis2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int filterSize = 5;
				double[][] filter = {
						{0, 0, -1, 0, 0},
						{0, -1, -2, -1, 0},
						{-1, -2, 17, -2, -1},
						{0, -1, -2, -1, 0},
						{0, 0, -1, 0, 0}
				};
				BufferedImage tempInput = null, tempOutput = null;
				try {
					tempInput = ImageIO.read(new File("clean_image.png"));
				} catch(Exception exception) {
				}
				tempOutput = new BufferedImage(tempInput.getWidth(), tempInput.getHeight(), BufferedImage.TYPE_INT_RGB);
				
				HighPassFilter(tempInput, tempOutput, tempInput.getWidth(), tempInput.getHeight(), filterSize, filter);
				
				cleanOutput.setIcon(new ImageIcon(tempOutput));
			}
		});
	}
	
	public void HighPassFilter(BufferedImage input, BufferedImage output, int width, int height, int filterSize, double[][] filter) {
		for(int x = (filterSize / 2); x < width - (filterSize / 2); x++) {
			for(int y = (filterSize / 2); y < height - (filterSize / 2); y++) {
				int sum = 0;
				for(int tX = x - (filterSize / 2), mX = 0; mX < filterSize; tX++, mX++) {
					for(int tY = y - (filterSize / 2), mY = 0; mY < filterSize; tY++, mY++) {
						Color color = new Color(input.getRGB(tX, tY));
						sum += color.getRed() * filter[mX][mY];
					}
				}
				
				if(sum > 255) sum = 255;
				if(sum < 0) sum = 0;
				
				output.setRGB(x, y, new Color((int) sum, (int) sum, (int) sum).getRGB());
			}
		}
		
		// 가장자리 작업
		for(int x = (filterSize / 2); x < width - (filterSize / 2); x++) {
			for(int y = filterSize - 1; y >= 0; y--)
				output.setRGB(x, y, output.getRGB(x, y + 1));
			for(int y = height - filterSize; y < height; y++)
				output.setRGB(x, y,  output.getRGB(x, y - 1));
		}
		for(int y = 0; y < height; y++) {
			for(int x = (filterSize / 2) - 1; x >= 0; x--)
				output.setRGB(x, y, output.getRGB(x + 1, y));
			for(int x = width - (filterSize / 2); x < width; x++)
				output.setRGB(x, y, output.getRGB(x - 1, y));
		}
	}
	
	private BufferedImage toBufferedImage(Mat m) {
	    if (!m.empty()) {
	        int type = BufferedImage.TYPE_BYTE_GRAY;
	        if (m.channels() > 1) {
	            type = BufferedImage.TYPE_3BYTE_BGR;
	        }
	        int bufferSize = m.channels() * m.cols() * m.rows();
	        byte[] b = new byte[bufferSize];
	        m.get(0, 0, b); // get all the pixels
	        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
	        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	        System.arraycopy(b, 0, targetPixels, 0, b.length);
	        return image;
	    }
	    return null;
	}
}

public class Project {
	public static void main(String[] args) {
		ProjectFunc func = new ProjectFunc();
	}
}