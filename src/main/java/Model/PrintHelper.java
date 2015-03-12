package Model;

import java.io.*;
import java.util.Formatter;
import org.apache.commons.io.FilenameUtils;

public final class PrintHelper
{
    private static String airportName;
    private static Runway runway;
    private static Obstacle obstacle;
    private static StringBuilder sb = new StringBuilder();
    private static Formatter formatter = new Formatter(sb);
    private static BufferedWriter bw;
    private static int duplicateFile;

    private PrintHelper() {}

    public static void print(Runway r, String airport, String filename) throws IOException, FileNotFoundException
    {
        runway = r;
        obstacle = runway.getObstacle();
        airportName = airport;
        duplicateFile = 0;
        Values strVals = runway.getStrip1().getOrigVal();
        //TODO: The printhelper runs to this line and does not carry on
        //Maybe the folder/file needs to be created first?
        File file = new File("files/", getFileName(filename));
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
        bw.write("Obstacle:");
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
        //TODO: Confirmation message on completion?
    }

    public static String getFileName(String file)
    {
        StringBuilder filename;
        if (file.equals(""))
        {
            filename = new StringBuilder();
            filename.append(runway.getStrip1().getStripId());
            filename.append("_");
            filename.append(runway.getStrip2().getStripId());
            filename.append("_");
            filename.append(obstacle.getName());
        }
        else
        {
            filename = new StringBuilder(file);
        }

        File fileDir = new File("files/");
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
        System.out.println(filename);
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

    //TODO handle invalid character expression PERSONALLY
    /*private static boolean isNameValid(String filename)
    {

    }*/
}
