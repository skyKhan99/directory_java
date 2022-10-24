package denemeler;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class directory {
	static String data = "";
	static FileWriter writer = null;
	static int lastIdNo = 0;
	
	public static void main(String[] args) throws IOException {
		Scanner reader;
		String[] persons = null;
		try {
			File dbPerson = new File("db_person.txt");
			
			if(dbPerson.createNewFile()) {
				System.out.println("created: " + dbPerson.getName());
			};
			
			reader = new Scanner(dbPerson);
			while (reader.hasNextLine()) {
		        data = reader.nextLine();
		      }
			if (data != null || data != "") {
				persons = data.split("}");

			}
		} catch (Exception e) {
			System.out.println("Database Error!");
		}
		
		JFrame frame = new JFrame("📚Directory");
		frame.setSize(500,300);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		
		JTable jt = new JTable(new DefaultTableModel(new Object[]{"ID","Name", "Surname", "Age"}, 0));
		JScrollPane sp = new JScrollPane(jt);    
		frame.add(sp, BorderLayout.CENTER);  	
		frame.setVisible(true);
		DefaultTableModel model = (DefaultTableModel) jt.getModel();
		model.isCellEditable(0, 0);
		try {
			for(int i=0; i < persons.length; i++) {
				String[] personDatas = persons[i].split(";");
				System.out.println(personDatas[0]+ personDatas[1] + personDatas[2]);
				model.addRow(new Object[] {personDatas[0], personDatas[1], personDatas[2], personDatas[3]});
			}
			lastIdNo = Integer.parseInt(model.getValueAt(model.getRowCount()-1, 0).toString());
			System.out.println(lastIdNo);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		JButton btnNewPerson = new JButton("➕New Person");
		frame.add(btnNewPerson, BorderLayout.NORTH);
		btnNewPerson.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame newPersonFrame = new JFrame("New Person");
				newPersonFrame.setSize(300,200);
				newPersonFrame.setVisible(true);
				
				JLabel nameLabel = new JLabel("Name:");
				JLabel surnameLabel = new JLabel("Surname:");
				JLabel ageLabel = new JLabel("Age:");
				
				JTextField nameTf = new JTextField();
				JTextField surnameTf = new JTextField();
				JTextField ageTf = new JTextField();
				
				JButton saveButton = new JButton("Save");
		
				newPersonFrame.setLayout(new GridLayout(4,2));
				newPersonFrame.add(nameLabel);
				newPersonFrame.add(nameTf);
				newPersonFrame.add(surnameLabel);
				newPersonFrame.add(surnameTf);
				newPersonFrame.add(ageLabel);
				newPersonFrame.add(ageTf);
				newPersonFrame.add(saveButton);
				saveButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Person p = new Person();
						p.setName(nameTf.getText());
						p.setSurname(surnameTf.getText());
						p.setAge(Integer.parseInt(ageTf.getText()));
						int newId = lastIdNo + 1;
						
						try {
							writer = new FileWriter("db_person.txt");
							writer.write(data + newId + ";"	+ p.name + ";" + p.surname + ";" + String.valueOf(p.age) +"}");
							writer.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						model.addRow(new Object[]{newId ,p.name, p.surname, p.age});
						newPersonFrame.dispose();
					}
				});
								
			}
			
		});
		
		JButton btnDeletePerson = new JButton("🗑Delete Person");
		frame.add(btnDeletePerson, BorderLayout.SOUTH);
		btnDeletePerson.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog (null, "'" + model.getValueAt(jt.getSelectedRow(), 1).toString() + " " +
						model.getValueAt(jt.getSelectedRow(), 2).toString() + "'" +
						" will be deleted. Are you sure?","Warning",dialogButton);
				if(dialogResult == JOptionPane.YES_OPTION){
					try {
						model.removeRow(jt.getSelectedRow());
						FileWriter wrt = new FileWriter("db_person.txt");
						for (int i = 0; i < model.getRowCount(); i++) {
							wrt.write(model.getValueAt(i, 0).toString() + ";" +
									model.getValueAt(i, 1).toString() +  ";" +
									model.getValueAt(i, 2).toString() + ";" +
									model.getValueAt(i, 3).toString() + "}");
						}
						wrt.close();
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "Delete Error!", "Can't Deleted", JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					}
					}
			}
		});
		jt.setEnabled(true);
	}
}

class Person {
	String name, surname;
	int age;
	public void setName(String setName) {
		name = setName;
	}
	public void setSurname(String setSurname) {
		surname = setSurname;
	}
	public void setAge(int setAge) {
		age = setAge;
	}
}