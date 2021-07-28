package file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class BasicFileReader {

	private File arquivo;

	private FileInputStream fIn;

	private InputStreamReader reader;

	private BufferedReader entrada;

	public BasicFileReader(String filename) {
		arquivo = new File(filename);
		fIn = null;
		reader = null;
		entrada = null;

	}

	public boolean open() {
		if (arquivo.exists()) {
			try {
				fIn = new FileInputStream(arquivo);
				reader = new InputStreamReader(fIn);
				entrada = new BufferedReader(reader);
			} catch (Exception e) {
				return false;
			}
			return true;
		} else
			return false;
	}

	public boolean endOfFile() {
		try {
			return !entrada.ready();
		} catch (Exception e) {
			return true;
		}
	}

	public String readLine() {
		try {
			if (entrada.ready())
				return entrada.readLine();
			else
				return "";
		} catch (Exception e) {
			return null;
		}
	}

	public boolean close() {
		try {
			entrada.close();
			reader.close();
			fIn.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
