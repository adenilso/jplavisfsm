package file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class BasicFileWriter {




		private File fFile;

		private FileOutputStream fOut;

		private OutputStreamWriter writer;

		private BufferedWriter out;

		public BasicFileWriter(String name) {
			fFile = new File(name);
			fOut = null;
			writer = null;
			out = null;

		}

		public boolean open() {
			try {
				fOut = new FileOutputStream(fFile);
				writer = new OutputStreamWriter(fOut);
				out = new BufferedWriter(writer);
			} catch (Exception e) {
				return false;
			}
			return true;
		}

		public boolean writeLine(String line) {
			try {
				out.write(line);
				return true;
			} catch (Exception e) {
				return false;
			}
		}

		public boolean newLine() {
			try {
				out.newLine();
				return true;
			} catch (Exception e) {
				return false;
			}
		}

		public boolean close() {
			try {
				out.close();
				writer.close();
				fOut.close();
				return true;
			} catch (Exception e) {
				return false;
			}
		}
	}

