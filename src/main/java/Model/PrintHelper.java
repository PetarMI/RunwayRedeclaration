package Model;

import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.net.URLDecoder;
import java.util.Formatter;

public final class PrintHelper
{
    private String airportName;
    private Runway runway;
    private Obstacle obstacle;
    private StringBuilder sb = new StringBuilder();
    private Formatter formatter = new Formatter(sb);
    private BufferedWriter bw;
    private int duplicateFile;
    private final String dirPath;

    public PrintHelper() throws UnsupportedEncodingException
    {
        this.dirPath = this.getDirectoryPath();
    }

    public void print(Runway r, String airport, String filename) throws IOException, FileNotFoundException
    {
        runway = r;
        obstacle = runway.getObstacle();
        airportName = airport;
        duplicateFile = 0;
        Values strVals = runway.getStrip1().getOrigVal();
        //file
        this.makeDirectory();
        String assignedFilename = getFileName(filename);
        File file = new File(this.dirPath, assignedFilename);
        //start writing
        bw = new BufferedWriter(new FileWriter(file));
        //Airport
        formatter.format("%-10s %-10s", "Airport", airportName);
        bw.write(sb.toString());
        sb.setLength(0);
        bw.newLine();
        bw.newLine();
        //Runway details
        formatter.format("%-10s %-10s", "Runway", runway.getRunwayId());
        bw.write(sb.toString());
        sb.setLength(0);
        bw.newLine();
        bw.newLine();
        //Obstacle headings
        bw.write("Obstacle: ");
        if (obstacle != null)
        {
            bw.newLine();
            formatter.format("%-10s %-10s %-10s %-10s %-15s %-15s", "Name", "Height", "Length", "Width",
                    "Distance Left", "Distance Right");
            bw.write(sb.toString());
            sb.setLength(0);
            bw.newLine();
            //Obstacle values
            formatter.format("%-10s %-10d %-10d %-10d %-15d %-15d", obstacle.getName(), obstacle.getHeight(),
                    obstacle.getLength(), obstacle.getWidth(), runway.getPositionFromLeftDT(), runway.getPositionFromRightDT());
            bw.write(sb.toString());
            sb.setLength(0);
            bw.newLine();
            formatter.format("%-10s %-10s", "Description:", obstacle.getDescription());
            bw.write(sb.toString());
            sb.setLength(0);
            bw.newLine();
            bw.newLine();
        }
        else
        {
            bw.write("No Obstacle");
        }
        bw.newLine();
        bw.newLine();
        //Strip 1 headings
        formatter.format("%-10s %-10s %-10s %-10s %-10s", runway.getStrip1().getStripId(),
                "TORA", "TODA", "ASDA", "LDA");
        bw.write(sb.toString());
        sb.setLength(0);
        bw.newLine();
        //Strip 1 Original values
        formatter.format("%-10s %-10d %-10d %-10d %-10d", "Original", strVals.getTora(),
                strVals.getToda(), strVals.getAsda(), strVals.getLda());
        bw.write(sb.toString());
        sb.setLength(0);
        bw.newLine();
        //Strip 1 Instructions
        formatter.format("%-10s %-10d %-10d %-10d %-10d", "Recalc", strVals.getTora(),
                strVals.getToda(), strVals.getAsda(), strVals.getLda());
        bw.write(sb.toString());
        sb.setLength(0);
        bw.newLine();
        //Strip 1 Recalculated Values
        strVals = runway.getStrip1().getRecVal();
        formatter.format("%-10s %-20s", "Instruct", strVals.getLanding() + "/" + strVals.getTakeoff());
        bw.write(sb.toString());
        sb.setLength(0);
        bw.newLine();
        bw.newLine();
        //Strip 2 headings
        formatter.format("%-10s %-10s %-10s %-10s %-10s", runway.getStrip2().getStripId(),
                "TORA", "TODA", "ASDA", "LDA");
        bw.write(sb.toString());
        sb.setLength(0);
        bw.newLine();
        //Strip 2 Original values
        strVals = runway.getStrip2().getOrigVal();
        formatter.format("%-10s %-10d %-10d %-10d %-10d", "Original", strVals.getTora(),
                strVals.getToda(), strVals.getAsda(), strVals.getLda());
        bw.write(sb.toString());
        sb.setLength(0);
        bw.newLine();
        //Strip 2 Recalculated Values
        strVals = runway.getStrip2().getRecVal();
        formatter.format("%-10s %-10d %-10d %-10d %-10d", "Recalc", strVals.getTora(),
                strVals.getToda(), strVals.getAsda(), strVals.getLda());
        bw.write(sb.toString());
        sb.setLength(0);
        bw.newLine();
        //Strip 2 Instructions
        formatter.format("%-10s %-20s", "Instruct", strVals.getLanding() + "/" + strVals.getTakeoff());
        bw.write(sb.toString());
        sb.setLength(0);
        bw.newLine();
        bw.newLine();
        bw.close();
    }

    public String getFileName(String file)
    {
        StringBuilder filename;
        if (file.equals(""))
        {
            filename = new StringBuilder();
            filename.append(airportName);
            filename.append("_");
            filename.append(runway.getStrip1().getStripId());
            filename.append("_");
            filename.append(runway.getStrip2().getStripId());
            filename.append("_");
            if(obstacle != null)
            {
                filename.append(obstacle.getName());
            }
            else
            {
                filename.append("empty");
            }
        }
        else
        {
            filename = new StringBuilder(file);
        }

        File fileDir = new File(dirPath);
        File[] files = fileDir.listFiles();
        while (checkFilenameExists(files, filename.toString()))
        {
            if (duplicateFile == 0)
            {
                duplicateFile++;
                filename.append(duplicateFile);
            }
            else
            {
                duplicateFile++;
                filename.deleteCharAt(filename.length() - 1);
                filename.append(duplicateFile);
            }
        }
        duplicateFile = 0;
        return filename.append(".txt").toString();
    }

    private static boolean checkFilenameExists(File[] files, String filename)
    {
        for (File f : files)
        {
            if(FilenameUtils.getBaseName(f.getAbsolutePath()).equals(filename))
            {
                return true;
            }
        }
        return false;
    }

    private String getDirectoryPath() throws UnsupportedEncodingException
    {
        String path = PrintHelper.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String decodedPath = URLDecoder.decode(path, "UTF-8");
        String folderPath = decodedPath + File.separator + "files" + File.separator;

        return folderPath;
    }

    private boolean makeDirectory()
    {
        File dir = new File(this.dirPath);
        return dir.mkdir();
    }

    //TODO handle invalid character expression PERSONALLY
    /*private static boolean isNameValid(String filename)
    {

    }*/
}
