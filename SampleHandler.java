package plugin3.handlers;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.MessageDialog;


public class SampleHandler extends AbstractHandler {
	public int totalMethod = 0;
	private final String filename = "results.txt"; 
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			processRootDirectory();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		System.out.println("We are in the method");
		MessageDialog.openInformation(
				window.getShell(),
				"Plugin3",
				"Hello, Eclipse world");
		try {
			MessageDialog.openInformation(window.getShell(), "Try2", processRootDirectory());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JFrame frame = new JFrame("Info");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.getContentPane().add("", BorderLayout.CENTER);
		JLabel label = null;
		try {
			label = new JLabel("<html>" + processRootDirectory() + "<html>");
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		frame.add(label);
		frame.pack();
		frame.setVisible(true);
		
		return null;
	}

	private String processRootDirectory() throws JavaModelException,
	CoreException {
		String string = "";
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		//System.out.println("root" + root.getLocation().toOSString());
		string += "root" + root.getLocation().toOSString() + "\n";
		IProject[] projects = root.getProjects();
		
		// process each project
		for (IProject project : projects) {
		
			//System.out.println("project name: " + project.getName());
			string += "project name: " + project.getName() + "\n";
			
		if (project.isNatureEnabled("org.eclipse.jdt.core.javanature")) {
			IJavaProject javaProject = JavaCore.create(project);
			IPackageFragment[] packages = javaProject.getPackageFragments();
		
			// process each package
			for (IPackageFragment aPackage : packages) {
		
				// We will only look at the package from the source folder
				// K_BINARY would include also included JARS, e.g. rt.jar
				// only process the JAR files
				if (aPackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
		
					for (ICompilationUnit unit : aPackage
							.getCompilationUnits()) {
					
						System.out.println("--class name: "
								+ unit.getElementName());
		
						IType[] allTypes = unit.getAllTypes();
						for (IType type : allTypes) {
		
							IMethod[] methods = type.getMethods();
		
							for (IMethod method : methods) {
								totalMethod++;
								string += "--Method name: "+ method.getElementName() + "\n";
								string += "Signature: "+ method.getSignature() + "\n";
								string += "Return Type: "+ method.getReturnType() + "\n";
								string += "source: "+ method.getSource() + "\n";
								string += "to string: "+ method.toString() + "\n";
								string += "new: "+ method.getPath().toString() + "\n";
								
								System.out.println("--Method name: "+ method.getElementName());
								System.out.println("Signature: "+ method.getSignature());
								System.out.println("Return Type: "+ method.getReturnType());
								System.out.println("source: "+ method.getSource());
								System.out.println("to string: "+ method.toString());
								System.out.println("new: "+ method.getPath().toString());
							}
						}
					}
				}
			}
		
		}
}
		return string;
}
	private void writeFile(String filename) {
		
		String results = null;
		try {
			results = processRootDirectory();
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	
		try ( FileWriter fileWriter = new FileWriter(filename, true);
			  BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			  PrintWriter printWriter = new PrintWriter(bufferedWriter);) 
		{
			  // Each password file contains the user ID, the hashed password, and the salt value
			  printWriter.println(results);
			
		} catch (IOException e) {
			System.out.println("There has been a failure");
			e.printStackTrace();
		}
	}
}
