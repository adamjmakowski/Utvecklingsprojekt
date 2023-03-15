package Model;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class Logger
{
    String filename;

    public Logger( )
    {
        this( "Logger_Output.txt" );
    }
    public Logger( String filename )
    {
        this.filename = filename;
    }

    public void Log( String line )
    {
        String time = LocalDateTime.now().withNano( 0 ).toString();
        String output = time + "\t " + line + "\n";
        new Thread( new LoggerWrite( filename, output ) ).start();
    }

    public void LogMessage( Message message )
    {
        String username = message.getSender().getUsername();
        String[] recievers = new String[message.getReceivers().size()];
        for( int i = 0; i < recievers.length; i++ )
        {
            recievers[i] = message.getReceivers().get(i).getUsername();
        }
        String text = message.getText();
        String line = "Message from " + username + " to " + Arrays.toString( recievers ) + ": " + text;
        Log( line );
    }

    public void LogConnect( String username)
    {
        String line = username + "  connected to the server.";
        Log( line );
    }

    public void LogDisconnect( String username )
    {
        String line = username + " disconnected from the server.";
        Log( line );
    }

    public static <T> T GetValueOrDefault(T value, T valueDefault)
    {
        return value == null ? valueDefault : value;
    }

    public LocalDateTime GetLocalDateTime( String line )
    {
        try
        {
            return LocalDateTime.parse( line.split( "\t" )[0] );
        }
        catch( Exception ignored ) { }

        return null;
    }

    public ArrayList<String> GetFileContent( String lower, String upper )
    {
        ArrayList<String> data = new ArrayList<String>( );
        LocalDateTime lowerDate = GetValueOrDefault( GetLocalDateTime( lower ), LocalDateTime.MIN );
        LocalDateTime upperDate = GetValueOrDefault( GetLocalDateTime( upper ), LocalDateTime.MAX );

        try
        {
            File file = new File(filename);
            BufferedReader br = new BufferedReader( new FileReader( file ) );
            String line;
            while( ( line = br.readLine( ) ) != null )
            {
                LocalDateTime date = GetLocalDateTime( line );
                if( ( date.isAfter( lowerDate ) || date.isEqual( lowerDate ) ) && ( date.isBefore( upperDate ) || date.isEqual( upperDate ) ) )
                {
                    data.add( line );
                }
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace( );
        }

        return data;
    }
    public ArrayList<String> GetFileContent( )
    {
        return GetFileContent( null, null );
    }

    public LocalDateTime GetFileContentDateFirst( )
    {
        try
        {
            ArrayList<String> data = GetFileContent();
            String line = data.get( 0 ).split( "\t" )[0];
            return LocalDateTime.parse( line );
        }
        catch( Exception e ) { }

        return null;
    }

    public LocalDateTime GetFileContentDateLast( )
    {
        try
        {
            ArrayList<String> data = GetFileContent();
            String line = data.get( data.size() - 1 ).split( "\t" )[0];
            return LocalDateTime.parse( line );
        }
        catch( Exception e ) { }

        return null;
    }

    public void CreateLoggerGUI()
    {
        LoggerGUI loggerGUI = new LoggerGUI( this );
        loggerGUI.CreateFrame();
    }

    private class LoggerWrite implements Runnable
    {
        String filename;
        String line;

        public LoggerWrite( String filename, String line )
        {
            this.filename = filename;
            this.line = line;
        }

        @Override
        public void run( )
        {
            try
            {
                File file = new File( filename );
                file.createNewFile();
                FileOutputStream outputStream = new FileOutputStream( filename, true );
                outputStream.write( line.getBytes() );
                outputStream.close();
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Logger logger = new Logger( );
        User sender = new User( "Rabaa", null );
        ArrayList<User> recievers = new ArrayList<User>(  );
        recievers.add( sender );
        recievers.add( sender );
        Message message = new Message( sender, recievers, "Test", null );
        logger.LogMessage( message );
        logger.CreateLoggerGUI();
    }
}
