/*
 * Copyright © 2000 Gemplus
 * Av. du Pic de Bertagne - Parc d'Activités de Gémenos
 * BP 100 - 13881 Gémenos CEDEX
 * 
 * "Code derived from the original OpenCard Framework".
 * 
 * Everyone is allowed to redistribute and use this source  (source
 * code)  and binary (object code),  with or  without modification,
 * under some conditions:
 * 
 * - Everyone  must  retain  and/or  reproduce the above copyright
 * notice,  and the below  disclaimer of warranty and limitation
 * of liability  for redistribution and use of these source code
 * and object code.
 * 
 * - Everyone  must  ask a  specific prior written permission from
 * Gemplus to use the name of Gemplus.
 * 
 * DISCLAIMER OF WARRANTY
 * 
 * THIS CODE IS PROVIDED "AS IS",  WITHOUT ANY WARRANTY OF ANY KIND
 * (INCLUDING,  BUT  NOT  LIMITED  TO,  THE IMPLIED  WARRANTIES  OF
 * MERCHANTABILITY  AND FITNESS FOR  A  PARTICULAR PURPOSE)  EITHER
 * EXPRESS OR IMPLIED.  GEMPLUS DOES NOT WARRANT THAT THE FUNCTIONS
 * CONTAINED  IN THIS SOFTWARE WILL MEET THE USER'S REQUIREMENTS OR
 * THAT THE OPERATION OF IT WILL BE UNINTERRUPTED OR ERROR-FREE. NO
 * USE  OF  ANY  CODE  IS  AUTHORIZED  HEREUNDER EXCEPT UNDER  THIS
 * DISCLAIMER.
 * 
 * LIMITATION OF LIABILITY
 * 
 * GEMPLUS SHALL NOT BE LIABLE FOR INFRINGEMENTS OF  THIRD  PARTIES
 * RIGHTS. IN NO EVENTS, UNLESS REQUIRED BY APPLICABLE  LAW,  SHALL
 * GEMPLUS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES OF ANY CHARACTER  INCLUDING,
 * WITHOUT LIMITATION, DAMAGES FOR LOSS OF GOODWILL, WORK STOPPAGE,
 * COMPUTER FAILURE OR MALFUNCTION, OR ANY AND ALL OTHER DAMAGES OR
 * LOSSES, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. ALSO,
 * GEMPLUS IS  UNDER NO  OBLIGATION TO MAINTAIN,  CORRECT,  UPDATE,
 * CHANGE, MODIFY, OR OTHERWISE SUPPORT THIS SOFTWARE.
 */

//-----------------------------------------------------------------------------
// PACKAGE DEFINITION
//-----------------------------------------------------------------------------
package com.gemplus.opencard.service.sim;

//-----------------------------------------------------------------------------
// IMPORTS
//-----------------------------------------------------------------------------
import java.util.*;

import opencard.core.terminal.*;
import opencard.core.service.*;
import opencard.core.util.*;

import opencard.opt.service.CardServiceInvalidCommandException;
import opencard.opt.iso.fs.*;
import opencard.opt.security.*;

/**
 * This class is the implementation of an OCF FileSystemCardService
 * for SIM / SIM Toolkit cards at the exception that the two methods
 * 'create' and 'delete' are not available on SIM cards!
 * 
 * It inherits from (and uses as a low level code base) the BasicSIM
 * class. It is recommended to use the present methods instead of the
 * low-level APDU API.
 *
 * @author Nguyen Thanh Lam, Tomasz Molag, Christophe.Muller@research.gemplus.com
 * @version $Id: SIMFileSystem.java,v 1.2 2004/09/17 07:35:17 antoinethomas Exp $
 * 
 */
public class SIMFileSystem
    extends BasicSIM
    implements FileSystemCardService,
	       SIMCardService.Constants {	
    
    private final static CardFilePath masterFile = new CardFilePath(":3f00");
    private SecureService secureService = null;
    private final static CardFilePath currentFile = null;
    private final static SIMCardFileInfo currentFileInfo = null;
    
    protected void throwException(ResponseAPDU apdu) throws SIMException {
	byte sw1, sw2;
	
	sw1 = apdu.sw1();
	sw2 = apdu.sw2();
	switch(sw1) {
	case SW1_PROACTIVE:
	    switch(sw2) {
	    case SW2_BUSY :
		throw new SIMBusyException();
	    default:
		throw new SIMUnknownErrorCodeException(sw1, sw2, "Unknown error code.");
	    }
	case SW1_MEMORY:
	    switch(sw2) {
	    case SW2_MEMORY :
		throw new SIMMemoryException();
	    default:
		throw new SIMUnknownErrorCodeException(sw1, sw2, "Unknown error code.");
	    }
	case SW1_FILE:
	    switch(sw2) {
	    case SW2_NO_FILE_SELECTED :
		throw new SIMNoFileSelectedException();
	    case SW2_OUT_OF_RANGE:
		throw new SIMOutOfRangeException();
	    case SW2_FILE_NOT_FOUND:
		throw new SIMFileNotFoundException();
	    case SW2_WRONG_FILE_TYPE:
		throw new SIMWrongFileTypeException();
	    default:
		throw new SIMUnknownErrorCodeException(sw1, sw2, "Unknown error code.");
	    }
	case SW1_SECURITY:
	    switch(sw2) {
	    case SW2_NO_CHV:
		throw new SIMNoCHVException();
	    case SW2_ACCESS_CONDITION:
		throw new SIMAccessConditionException();
	    case SW2_CHV_STATUS:
		throw new SIMCHVStatusException();
	    case SW2_INVALIDATION:
		throw new SIMInvalidationException();
	    case SW2_CHV_ERROR:
		throw new SIMCHVErrorException();
	    case SW2_INCREASE:
		throw new SIMIncreaseException();
	    default:
		throw new SIMUnknownErrorCodeException(sw1, sw2, "Unknown error code.");
	    }
	case SW1_P3:
	    throw new SIMP3Exception(sw2);
	case SW1_P1P2:
	    throw new SIMP1P2Exception(sw2);
	case SW1_COMMAND_CODE:
	    throw new SIMCommandCodeException(sw2);
	case SW1_INSTRUCTION_CLASS:
	    throw new SIMInstructionClassException(sw2);
	case SW1_TECHNICAL:
	    throw new SIMTechnicalException(sw2);
	default:
	    throw new SIMUnknownErrorCodeException(sw1, sw2, "Unknown error code.");
	}
    }
    
    protected void select(CardFilePath file)
	throws CardTerminalException,
	       SIMException {
	Enumeration componentList;
	CardFilePathComponent component;
	short fileID;
	String tmp;
	
	componentList = file.components();
	while (componentList.hasMoreElements()) {
	    component = (CardFilePathComponent)componentList.nextElement();
	    tmp = component.toString();
	    if (tmp.length() != 5)
		throw new SIMFileNotFoundException();
	    try {
		fileID = Short.parseShort(tmp.substring(1, 5));
	    } catch (NumberFormatException e) {
		throw new SIMFileNotFoundException();
	    }
	    response = select(fileID);
	    try {
		throwException(response);
	    } catch (SIMApplicationException e) {
		throw new SIMGeneralException("Error in SIMFileSystem.select(CardFilePath).");
	    }
	}
    }
    
    public void  provideCredentials(SecurityDomain domain, CredentialBag bag)
	throws CardServiceException {
	secureService.provideCredentials(domain, bag);
    }	
    
    public void setSecureService(SecureService service) {
	secureService = service;
    }
    
    public SecureService getSecureService() {
	return secureService;
    }

    /********************************************************************
     * FileAccessCardService interface implementation			*
     ********************************************************************/

    /**
     * Returns the absolute path to the master file (MF) of the smartcard.
     * For ISO compliant cards, the master file has the fixed id 0x3f00, so
     * this method will typically be implemented in the following way:
     * <p>
     * <pre><blockquote>
     * private final static CardFilePath master_file
     *                = new CardFilePath(":3f00");
     *
     * public final CardFilePath getRoot()
     * {
     *   return master_file;
     * }
     * </blockquote></pre>
     * <p>
     * The value returned is <b>not</b> allowed <b>to be modified</b>.
     * When taking a look at the sample implementation above, it should
     * be obvious why.
     * There are no exceptions thrown by this method, since it does
     * not require interaction with the smartcard.
     *
     * @return    the path to the master file
     */
    public CardFilePath getRoot() {
	return masterFile;
    };
    
    /**
     * Checks whether a file exists.
     *
     * @param file   the path to the file to query
     * @return   <tt>true</tt> if the file exists, <tt>false</tt> otherwise
     *
     * @exception opencard.core.service.CardServiceException
     *            if the service encountered an error
     * @exception opencard.core.terminal.CardTerminalException
     *            if the terminal encountered an error
     */
    public boolean exists(CardFilePath file)
	throws CardServiceException, 
	       CardTerminalException {
	CardFilePath previous = currentFile;
	boolean result = true;
	
	try {
	    select(file);
	} catch(SIMFileNotFoundException exception) {
	    result=false;
	} catch (SIMApplicationException e) {
	    throw new SIMGeneralException("Error in SIMFileSystem.exists(CardFilePath).");
	} finally {
	    if (previous!=null) select(previous);
	    return result;
	}
    };
    
    /**
     * Queries information about a file.
     *
     * @param file   the path to the file to query
     * @return   information about the file,
     *           or <tt>null</tt> if it doesn't exist
     *
     * @exception opencard.core.service.CardServiceException
     *            if the service encountered an error
     * @exception opencard.core.terminal.CardTerminalException
     *            if the terminal encountered an error
     */
    public CardFileInfo getFileInfo(CardFilePath file)
        throws CardServiceException, 
	       CardTerminalException {
	CardFilePath previous = null;
   	SIMCardFileInfo result = null;
	
   	if (!file.equals(currentFile)) {
	    previous = currentFile;
	    try {
		select(file);
		result = currentFileInfo;
	    } catch (SIMFileNotFoundException exception) {
		result = null;
	    } catch (SIMApplicationException e) {
		throw new SIMGeneralException("Error in SIMFileSystem.getFileInfo(CardFilePath).");
	    }
	    if (previous!=null) select(previous);
	}
   	return result;
    };
    
    /**
     * Reads a given amount of data from a transparent file.
     * Transparent files are similiar to files in traditional file systems.
     * They provide random access to an array of bytes.
     * <br>
     * Instead of specifying a number of bytes to read, the constant
     * <tt>READ_SEVERAL</tt> can be passed. The service will then read at
     * least one byte, probably more. Only one read command will be sent to
     * the card in this case, that means a maximum of about 255 bytes can
     * be returned. If the specified <tt>offset</tt> points to the end of
     * the file, that is if not even one byte can be read, <tt>null</tt> is
     * returned, but no exception will be thrown.
     *
     * @param file     the path to the file to read from
     * @param offset   the index of the first byte to read (0 for first)
     * @param length   the number of bytes to read, or <tt>READ_SEVERAL</tt>.
     *                 If 0 is passed, the behavior is implementation dependent
     *
     * @return    an array holding the data read from the file,
     *            or <tt>null</tt> if a read with length <tt>READ_SEVERAL</tt>
     *            has been performed at the end of the file
     *
     * @exception opencard.core.service.CardServiceException
     *            if the service encountered an error
     * @exception opencard.core.terminal.CardTerminalException
     *            if the terminal encountered an error
     *
     * @see #READ_SEVERAL
     */
    public byte[] read(CardFilePath file, int offset, int length)
        throws CardServiceException, 
	       CardTerminalException {
	byte[] result=null;
	
	if (!file.equals(currentFile))
	    select(file);
	if (currentFileInfo.isDirectory() || !currentFileInfo.isTransparent())
	    throw new SIMWrongFileTypeException("Not a Transparent file !!");
	if (length==READ_SEVERAL)
	    length = currentFileInfo.getLength();
	response = readBinary((short)offset, (byte)length);
	try {
	    throwException(response);
	    result = response.data();
	} catch (SIMOutOfRangeException e) {
	    result = null;
	} catch (SIMApplicationException e) {
	    throw new SIMGeneralException("Error in SIMFileSystem.read(CardFilePath, int, int).");
	}
	return(result);
    };
    
    /**
     * Reads a record from a structured file.
     * Structured files consist of records. Each record is an array of bytes.
     * Data is addressed only in terms of records, and records are always
     * read completely. The record size will be determined by the card service.
     * ISO 7816-4 specifies the following structured file types:
     * <p>
     * <dl>
     * <dt><b>linear fixed</b></dt>
     *     <dd>An array of records, with absolute addressing. All records
     *         have the same, fixed size.
     *     </dd>
     * <dt><b>linear variable</b></dt>
     *     <dd>An array of records, with absolute addressing. Every record
     *         may have a different size.
     *     </dd>
     * <dt><b>cyclic fixed</b></dt>
     *     <dd>A ring buffer of records, with relative addressing. All records
     *         have the same, fixed size. Cyclic files are typically used for
     *         keeping logs on transactions.
     *     </dd>
     * </dl>
     * <p>
     * Files with a cyclic structure may not be easily accessible with this
     * method, since the absolute addressing may be interpreted in different
     * ways by different cards. For example, the <i>first</i> record may be
     * the record that is physically stored first on the card, or it may be
     * the record that was last written into the ring buffer. The method
     * <tt>readRecords</tt> is the preferred way to read cyclic files.
     *
     * @param file        the path to the file to read from
     * @param record      the index of the record to read (0 for first)
     *
     * @return    an array holding the record read. If the record has
     *            length 0, which may happen with linear variable files,
     *            an array of length 0 is returned.
     *
     * @exception opencard.core.service.CardServiceException
     *            if the service encountered an error
     * @exception opencard.core.terminal.CardTerminalException
     *            if the terminal encountered an error
     *
     * @see #readRecords
     */
    public byte[] readRecord(CardFilePath file, int recordNumber)
        throws CardServiceException, 
	       CardTerminalException {
	byte[] result = null;
	
	if (!file.equals(currentFile))
	    select(file);
	if (currentFileInfo.isDirectory() || currentFileInfo.isTransparent())
	    throw new SIMWrongFileTypeException("Not a structured file !!");
	recordNumber++;
	response = readRecord((byte)recordNumber, ABSOLUTE_MODE, (byte)currentFileInfo.getRecordSize());
	try {
	    throwException(response);
	} catch (SIMApplicationException e) {
	    throw new SIMGeneralException("Error in SIMFileSystem.readRecord(CardFilePath, int).");
	}
	result = response.data();
	
	return result;
    };
    
    /**
     * Reads consecutive records from a structured file.
     * For a discussion of structured file types, see <tt>readRecord</tt>.
     * The first record read will always be the first in the structured file.
     * For linear files with fixed or variable record size, <i>first</i> is
     * interpreted as an absolute record number. For cyclic files, <i>first</i>
     * refers to the record most recently written. Starting with that first
     * record, the specified number of consecutive records will be read. In
     * the case of a cyclic file, the second record will be the second most
     * recently written record, and so on.
     * <br>
     * Typically, smartcards will implement absolute addressing for cyclic
     * files, where the first record is the least recently written, and the
     * following are sorted by decreasing time of writing. In this case,
     * this method can be implemented by repeated invocations of
     * <tt>readRecord</tt>.
     * <br>
     * The magic number <tt>READ_SEVERAL</tt> may be passed as the number
     * of records to read. In this case, all records in the file are read.
     * This is especially useful with linear variable files, where the number
     * of records in the file cannot be determined via file attributes.
     *
     * @param file     the path to the file to read from
     * @param number   the number of records to read, or <tt>READ_SEVERAL</tt>.
     *                 If 0 is passed, the behavior is implementation dependent
     *
     * @return    an array holding the records read,
     *            where the records are arrays themselves
     *
     * @exception opencard.core.service.CardServiceException
     *            if the service encountered an error
     * @exception opencard.core.terminal.CardTerminalException
     *            if the terminal encountered an error
     *
     * @see #readRecord
     * @see #READ_SEVERAL
     */
    public byte[][] readRecords(CardFilePath file, int number)
        throws CardServiceException, 
	       CardTerminalException {
	byte[][] result = null;
	
	if (!file.equals(currentFile))
	    select(file);
	if (currentFileInfo.isDirectory() || currentFileInfo.isTransparent())
	    throw new SIMWrongFileTypeException("Not a structured file !!");
	
	result = new byte[number][currentFileInfo.getRecordSize()];
	if (number == READ_SEVERAL) 
	    number = currentFileInfo.getLength() / currentFileInfo.getRecordSize();
	for(int i = 0; i < number; i++)
	    try {
		result[i] = readRecord(file, i);
	    } catch (SIMGeneralException e) {
		throw new SIMGeneralException("Error in SIMFileSystem.readRecords(CardFilePath, int).");
	    }
	return result;
    };
    
    /**
     * Writes data to a transparent file, using part of an array.
     * This method corresponds to the UPDATE BINARY command defined in
     * ISO 7816-4. The term <tt>write</tt> has been chosen since it is
     * more natural for programmers that are used to traditional file
     * systems. For an explanation of the term <i>transparent file</i>,
     * see <tt>read</tt>.
     * To write an array completely, the convenience method <tt>write</tt>
     * with three arguments can be used.
     *
     * @param file     the path to the file to write to
     * @param foffset  the file index of the first byte to overwrite
     *                 (0 for first byte in file)
     * @param source   an array holding the data to write
     * @param soffset  the array index of the first byte to write
     * @param length   the number of bytes to write
     *
     * @exception opencard.core.service.CardServiceException
     *            if the service encountered an error
     * @exception opencard.core.terminal.CardTerminalException
     *            if the terminal encountered an error
     *
     * @see #read
     * @see #write(opencard.opt.iso.fs.CardFilePath, int, byte[])
     */
    public void write(CardFilePath file, int foffset,
		      byte[] source, int soffset, int length)
        throws CardServiceException, 
	       CardTerminalException {
	int i;
	byte y;
	byte[] data = new byte[255];
	
	if (!file.equals(currentFile))
	    select(file);
	if (currentFileInfo.isDirectory() || !currentFileInfo.isTransparent())
	    throw new SIMWrongFileTypeException("Not a transparent file !!");
	
	foffset++;
	while (length > 0) {
	    if (length > (byte)0xFF)
		y = (byte)0xFF;
	    else
		y = (byte)length;
	    for (i = 0 ; i <= y; i++)
		data[i] = source[ i + soffset ];
	    response = updateBinary((short)foffset, y, data);
	    try {
		throwException(response);
	    } catch (SIMApplicationException e) {
		throw new SIMGeneralException("Error in SIMFileSystem.write(CardFilePath, int, byte[], int, int).");
	    }
	    soffset += 255;
	    foffset += 255;
	    length -= 255;
	}
    };
    
    /**
     * Writes data to a transparent file, using a complete array.
     * This is a convenience method for <tt>write</tt> with five arguments.
     * It does not allow to specify an array index and the number of bytes
     * to write. Instead, it always writes the complete array passed.
     * Typically, this method will be implemented as follows:
     * <p>
     * <pre><blockquote>
     * final public void write(CardFilePath file, int offset, byte[] data)
     *  {
     *    write(file, offset, data, 0, data.length);
     *  }
     * </blockquote></pre>
     *
     * @param file        the path to the file to write to
     * @param offset      the file index of the first byte to overwrite
     *                    (0 for first byte in file)
     * @param data        the data to write to the file
     *
     * @exception opencard.core.service.CardServiceException
     *            if the service encountered an error
     * @exception opencard.core.terminal.CardTerminalException
     *            if the terminal encountered an error
     *
     * @see #write(opencard.opt.iso.fs.CardFilePath, int, byte[], int, int)
     */
    public void write(CardFilePath file, int offset, byte[] data)
        throws CardServiceException, 
	       CardTerminalException {
	write(file, offset, data, 0, data.length);
    };
    
    /**
     * Writes data to a structured file.
     * This method corresponds to the UPDATE RECORD command defined in
     * ISO 7816-4. The term <tt>write</tt> has been chosen since it is
     * more natural for programmers that are used to traditional file
     * systems. For a discussion of structured file types, see
     * <tt>readRecord</tt>.
     * <br>
     * A record is always written completely. For linear fixed files, the
     * size of the input record must be exactly the file's record size.
     * For files with variable record sizes, the size of the input record
     * must not exceed the maximum size for the record that will be
     * overwritten. That maximum size is typically the initial size of the
     * record when the smartcard was initialized. For cyclic files, this
     * method is not necessarily supported. Use <tt>appendRecord</tt> instead.
     *
     * @param file        the path to the file to write to
     * @param record      the index of the record to overwrite (0 for first)
     * @param data        the data to write to the file
     *
     * @exception opencard.core.service.CardServiceException
     *            if the service encountered an error
     * @exception opencard.core.terminal.CardTerminalException
     *            if the terminal encountered an error
     *
     * @see #readRecord
     * @see #appendRecord
     */
    public void writeRecord(CardFilePath file, int recordNumber, byte[] data)
        throws CardServiceException, 
	       CardTerminalException {
   	if (!file.equals(currentFile))
	    select(file);
	if (currentFileInfo.isDirectory() || currentFileInfo.isTransparent())
	    throw new SIMWrongFileTypeException("Not a structured file !!");
	recordNumber++;
	response = updateRecord((byte) recordNumber, ABSOLUTE_MODE, (byte)data.length, data);
	try {
	    throwException(response);
	} catch (SIMApplicationException e) {
	    throw new SIMGeneralException("Error in SIMFileSystem.writeRecord(CardFilePath, int, byte[]).");
	};
    };
    
    /**
     * Appends data to a structured file.
     * For a discussion of structured file types, see <tt>readRecord</tt>.
     * For linear files with variable record size, this method appends a new
     * record at the end of the file. Typically, the space for appending a
     * record must have been allocated at the time the file was created.
     * For cyclic files, this method overwrites the oldest record in the
     * ring buffer, which then becomes the newest. The size of the record to
     * append has to match the file's record size exactly. For linear files
     * with a fixed record size, this method is not necessarily supported.
     * Use <tt>writeRecord</tt> instead.
     *
     * @param file        the path to the file to append to
     * @param data        the data to write to the new record
     *
     * @exception opencard.core.service.CardServiceException
     *            if the service encountered an error
     * @exception opencard.core.terminal.CardTerminalException
     *            if the terminal encountered an error
     *
     * @see #readRecord
     * @see #writeRecord
     */
    public void appendRecord(CardFilePath file, byte[] data)
        throws CardServiceException, CardTerminalException {
   	if (!file.equals(currentFile))
	    select(file);
	if (currentFileInfo.isDirectory() || currentFileInfo.isTransparent() || !currentFileInfo.isCyclic())
	    throw new SIMWrongFileTypeException("Not a structured file !!");
	response = updateRecord((byte)1, PREVIOUS_MODE, (byte)data.length, data);
	try {
	    throwException(response);
	} catch (SIMApplicationException e) {
	    throw new SIMGeneralException("Error in SIMFileSystem.appendRecord(CardFilePath, byte[]).");
	}	
    }
 
    public void invalidate(CardFilePath file)
	throws CardServiceInabilityException,
	       CardServiceException,
	       CardTerminalException {

	if (!file.equals(currentFile))
	    select(file);
	if (currentFileInfo.isDirectory() || !currentFileInfo.isTransparent())
	    throw new SIMWrongFileTypeException("Not a Transparent file !!");
	response = invalidate();
	try {
	    throwException(response);
	} catch (SIMApplicationException e) {
	    throw new SIMGeneralException("Error in SIMFileSystem.invalidate(CardFilePath).");
	}
    };
    
    public void rehabilitate(CardFilePath file)
	throws CardServiceInabilityException,
	       CardServiceException,
	       CardTerminalException {
	
	if (!file.equals(currentFile))
	    select(file);
	if (currentFileInfo.isDirectory() || !currentFileInfo.isTransparent())
	    throw new SIMWrongFileTypeException("Not a Transparent file !!");
	response = rehabilitate();
	try {
	    throwException(response);
	} catch (SIMApplicationException e) {
	    throw new SIMGeneralException("Error in SIMFileSystem.rehabilitate(CardFilePath).");
	}
    };
    
    public void create(CardFilePath parent,
		       byte[] data)
	throws CardServiceException,
	       CardTerminalException {
	
	throw new CardServiceInvalidCommandException("Error in SIMFileSystem: GSM 11.11 Smart Cards are unable to perform file creation.");
	
    }
    
    public void delete(CardFilePath file)
	throws CardServiceException,
	       CardTerminalException {
	
	throw new CardServiceInvalidCommandException("Error in SIMFileSystem: GSM 11.11 Smart Cards are unable to perform file deletion.");
	
    }
    
}
