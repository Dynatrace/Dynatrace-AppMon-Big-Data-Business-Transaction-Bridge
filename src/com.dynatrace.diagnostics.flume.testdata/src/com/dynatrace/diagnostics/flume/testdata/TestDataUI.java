package com.dynatrace.diagnostics.flume.testdata;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class TestDataUI extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -412287275980584646L;


	public TestDataUI() {
		super("Flume Test Data");
		
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);
		
		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());
		
		final JTextField urlField = new JTextField();
		urlField.setText("http://localhost:4321");
		top.add(urlField, BorderLayout.CENTER);
		
		JButton sendButton = new JButton("send test-data");
		top.add(sendButton, BorderLayout.EAST);
		
		contentPane.add(top, BorderLayout.NORTH);
		
		final JTextArea output = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(output);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		sendButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				final String url = urlField.getText(); 
				
				new SwingWorker<String, String>() {

					@Override
					protected String doInBackground() throws Exception {
						TestDataSender.exportPurePath(url);
						TestDataSender.exportPageAction(url);
						TestDataSender.exportVisit(url);
						return "test-data has been exported";
					}
					
					@Override
					protected void process(List<String> chunks) {
						for (String s : chunks) {
							output.append("\n" + s);
						}
					}
					
					protected void done() {
						try {
							output.append("\n" + get());
						} catch (InterruptedException ie) {
							Thread.currentThread().interrupt();
						} catch (ExecutionException ee) {
							Throwable e = ee.getCause();
							StringBuilder sb = new StringBuilder();
							sb.append(e.getMessage());
							for (StackTraceElement ste : e.getStackTrace()) {
								sb.append('\n').append(ste);
							}
							output.append("\n" + sb.toString());
						}
					}
				}.execute();
			}
		});
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(600, 1000);
		setVisible(true);
		
	}
	
	
	public static void main(String[] args) {
		new TestDataUI();
	}

}
