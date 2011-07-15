/*
 * Created on Sep 19, 2004
 * Author: Chris Taylor
 */
package com.pk.script;

/**
 * @author Chris Taylor
 *
 */
public class ScriptCommand
{
    public static String STATUS_NOT_RUN = "NOTRUN";
    public static String STATUS_SUCCESS = "SUCCESS";
    public static String STATUS_FAILED = "FAILED";
    private String command;
    private String displayCommand;
    private String status;
    private String errorMessage;
    private boolean run;
    
    /**
     * 
     */
    public ScriptCommand()
    {
        super();
        status = STATUS_NOT_RUN;
        run = true;
    }
    /**
     * @return Returns the command.
     */
    public String getCommand()
    {
        return command;
    }
    /**
     * @param command The command to set.
     */
    public void setCommand(String command)
    {
        this.command = command;
        if(command != null)        
        {
            displayCommand = command.replaceAll("\n", " ");
        }
    }
    /**
     * @return Returns the errorMessage.
     */
    public String getErrorMessage()
    {
        return errorMessage;
    }
    /**
     * @param errorMessage The errorMessage to set.
     */
    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }
    /**
     * @return Returns the status.
     */
    public String getStatus()
    {
        return status;
    }
    /**
     * @param status The status to set.
     */
    public void setStatus(String status)
    {
        this.status = status;
    }
    /**
     * @return Returns the run.
     */
    public boolean isRun()
    {
        return run;
    }
    /**
     * @param run The run to set.
     */
    public void setRun(boolean run)
    {
        this.run = run;
    }
    public String getDisplayCommand()
    {
        return displayCommand;
    }
}
