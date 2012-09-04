/**
 * This file was automatically generated by the Mule Development Kit
 */
package com.mulesoft.module.fileutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;

import org.mule.api.annotations.Module;
import org.mule.api.ConnectionException;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * File Utils module
 *
 * @author MuleSoft, Inc.
 */
@Module(name="fileutils", schemaVersion="3.3")
public class FileUtilsModule
{
    protected static final Log logger = LogFactory.getLog(FileUtilsModule.class);

    /**
     * Copy file from one location to another
     *
     * {@sample.xml ../../../doc/FileUtils-connector.xml.sample file-utils:copy-file}
     *
     * @param fileName the source file name
     * @param filePath optional location of the source file (if not specified, current working directory is used)
     * @param destinationPath the new location of the file
     * @param destinationName optional new file name (if not set, the source file name is used)
     * @param overrideIfExists if the file exists in the destination, delete it before copying (true by default)
     * @param preserveDate if set to true, the original file date will be preserved (true by default)
     * @param fileAge minimum file age in milliseconds requirement
     * 
     * @return full path to the new location
     */
    @Processor
    public String copyFile(String fileName, @Optional String filePath, String destinationPath, 
                           @Optional String destinationName,
                           @Optional @Default("true") boolean overrideIfExists,
                           @Optional @Default("true") boolean preserveDate,
                           @Optional long fileAge) throws Exception
    {
        String fullSrcName = "";
        if (!StringUtils.isEmpty(filePath))
            fullSrcName = fullSrcName + filePath + File.separator;
        fullSrcName = fullSrcName + fileName;
        
        logger.debug("Source file name is " + fullSrcName);
            
        File srcFile = new File(fullSrcName);
        //Check the age
        if (fileAge != 0) {
            long currentTime = new Date().getTime();
            if (FileUtils.isFileNewer(srcFile, currentTime - fileAge)) {
                logger.debug("File is newer than " + fileAge + " milliseconds!");
                throw new Exception("File is newer than " + fileAge + " milliseconds!");
            }
            
        }
        
        String srcFileShortName = srcFile.getName();
            
        String fullDestName = "";
        if (!StringUtils.isEmpty(destinationPath))
            fullDestName = fullDestName + destinationPath + File.separator;
        else
            if (!StringUtils.isEmpty(filePath))
                fullDestName = fullDestName + filePath + File.separator;
        
        if (!StringUtils.isEmpty(destinationName))
            fullDestName = fullDestName + destinationName;
        else 
            fullDestName = fullDestName + srcFileShortName;
        
        logger.debug("Copying to " + fullDestName);
        
        File destFile = new File(fullDestName);
        if (destFile.exists() && overrideIfExists) {
            FileUtils.forceDelete(destFile);
        }
        
        if (srcFile.isDirectory())
            FileUtils.copyDirectory(srcFile, destFile, preserveDate);
        else
            FileUtils.copyFile(srcFile, destFile, preserveDate);
        
        return fullDestName;
    }

    /**
     * Move file from one location to another
     *
     * {@sample.xml ../../../doc/FileUtils-connector.xml.sample file-utils:copy-file}
     *
     * @param fileName the source file name
     * @param filePath optional location of the source file (if not specified, current working directory is used)
     * @param destinationPath the new location of the file
     * @param destinationName optional new file name (if not set, the source file name is used)
     * @param overrideIfExists if the file exists in the destination, delete it before copying (true by default)
     * @param fileAge minimum file age in milliseconds requirement
     * 
     * @return full path to the new location
     */
    @Processor
    public String moveFile(String fileName, @Optional String filePath, String destinationPath, 
                           @Optional String destinationName, @Optional @Default("true") boolean overrideIfExists,
                           @Optional long fileAge) throws Exception
    {
        String fullSrcName = "";
        if (!StringUtils.isEmpty(filePath))
            fullSrcName = fullSrcName + filePath + File.separator;
        fullSrcName = fullSrcName + fileName;
        
        logger.debug("Source file name is " + fullSrcName);
            
        File srcFile = new File(fullSrcName);
        
        if (fileAge != 0) {
            long currentTime = new Date().getTime();
            if (FileUtils.isFileNewer(srcFile, currentTime - fileAge)) {
                logger.debug("File is newer than " + fileAge + " milliseconds!");
                throw new Exception("File is newer than " + fileAge + " milliseconds!");
            }
            
        }
        String srcFileShortName = srcFile.getName();
            
        String fullDestName = "";
        if (!StringUtils.isEmpty(destinationPath))
            fullDestName = fullDestName + destinationPath + File.separator;
        else
            if (!StringUtils.isEmpty(filePath))
                fullDestName = fullDestName + filePath + File.separator;
        
        if (!StringUtils.isEmpty(destinationName))
            fullDestName = fullDestName + destinationName;
        else 
            fullDestName = fullDestName + srcFileShortName;
        
        logger.debug("Moving to " + fullDestName);
        
        File destFile = new File(fullDestName);
        if (destFile.exists() && overrideIfExists) {
            FileUtils.forceDelete(destFile);
        }
   
        if (srcFile.isDirectory())
            FileUtils.moveDirectory(srcFile, destFile);
        else
            FileUtils.moveFile(srcFile, destFile);
        
        return fullDestName;
    }
    
    /**
     * Opens a file for read and returns input stream
     *
     * {@sample.xml ../../../doc/FileUtils-connector.xml.sample file-utils:read-file}
     *
     * @param fileName the source file name
     * @param filePath optional location of the source file (if not specified, current working directory is used)
     * 
     * @return Stream to an open file
     */
    @Processor
    public InputStream readFile(String fileName, @Optional String filePath) throws Exception
    {
        String fullSrcName = "";
        if (!StringUtils.isEmpty(filePath))
            fullSrcName = fullSrcName + filePath + File.separator;
        fullSrcName = fullSrcName + fileName;
        
        logger.debug("Source file name is " + fullSrcName);
                    
        File srcFile = new File(fullSrcName);
        
        return new FileInputStream(srcFile);
    }

    /**
     * Deletes a file or directory
     *
     * {@sample.xml ../../../doc/FileUtils-connector.xml.sample file-utils:read-file}
     *
     * @param fileName the file name
     * @param filePath optional location of the file (if not specified, current working directory is used)
     * @param throwIfUnsuccessful throws an exception if file does not exist or cannot be deleted (true by default)
     * 
     * @return Stream to an open file
     */
    @Processor
    public void deleteFile(String fileName, @Optional String filePath, @Optional @Default("true") boolean throwIfUnsuccessful) throws Exception
    {
        String fullSrcName = "";
        if (!StringUtils.isEmpty(filePath))
            fullSrcName = fullSrcName + filePath + File.separator;
        fullSrcName = fullSrcName + fileName;
        
        logger.debug("Source file name is " + fullSrcName);
                    
        File srcFile = new File(fullSrcName);
        
        if (throwIfUnsuccessful)
            FileUtils.forceDelete(srcFile);
        else
            FileUtils.deleteQuietly(srcFile);
    }
}
