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
import opencard.core.terminal.*;
import opencard.core.service.*;
import opencard.core.util.*;

import opencard.opt.iso.fs.*;

/**
 * This class defines a CardFileInfo object suitable for SIM cards.
 * 
 * @author Nguyen Thanh Lam, Tomasz Molag, Christophe.Muller@research.gemplus.com
 * @version $Id: SIMCardFileInfo.java,v 1.1 2004/09/13 16:26:26 cmuller13 Exp $
 * 
 */
public class SIMCardFileInfo implements CardFileInfo {
    
    protected byte[] fileInfo = new byte[34];
    
    /* MF, DF & EF Constants */
    protected static final byte fileIDHigh = 5;
    protected static final byte fileIDLow = 6;
    protected static final byte fileType = 7; /* cf file type masks */
    protected static final byte gsmLength = 13;
    
    /* MF & DF constante */
    protected static final byte freeSpaceHigh = 3;
    protected static final byte freeSpaceLow = 4;
    protected static final byte fileCar = 14;	/* cf file
						   characteristic masks */
    protected static final byte DFChild = 15;
    protected static final byte EFChild = 16;
    protected static final byte CHVs = 17;
    protected static final byte CHV1Status = 18;
    protected static final byte UnbCHV1Status = 19; /* cf status byte masks */
    protected static final byte CHV2Status = 20;
    protected static final byte UnbCHV2Status = 21;  /* cf status byte masks */
    
    /* EF Constante */
    protected static final byte fileSizeHigh = 3;
    protected static final byte fileSizeLow = 4;
    protected static final byte cyclicIncrease = 8; /* bit 7 = 1 */
    protected static final byte cyclicIncreaseMask = 0x40;
    protected static final byte accessConditionHigh = 9;
    protected static final byte accessConditionMiddle = 10;
    protected static final byte accessConditionLow = 11;
    protected static final byte fileStatus = 12; /* cf file status masks */
    protected static final byte EFStructure = 14; /* cf file structure constants */
    protected static final byte recordLength = 15;
    
    /* File characteristic masks */
    protected static final byte clockStopAllowed = (byte)0x1;
    protected static final byte clockStopHighLevel = (byte)0x4;
    protected static final byte clockStopLowLevel = (byte)0x8;
    protected static final byte authentication = (byte)0x2;
    protected static final byte envelopeCommand = (byte)0x2;
    protected static final byte CHV1isDisable = (byte)0x80;
    
    /* Status byte masks of CHV1/CHV2 */
    protected static final byte remainingTry = (byte)0xF;
    protected static final byte initialisedCode = (byte)0x80;
    
    /* File status masks */
    protected static final byte unvalidated = (byte)0x1;
    protected static final byte unvalReadUpdate = (byte)0x4;
    
    /* File structure type constants */
    protected static final byte transparent = (byte)0x00;
    protected static final byte linearFixed = (byte)0x01;
    protected static final byte cyclic = (byte)0x03;
    
    /* File type constants */
    protected static final byte MFFile = (byte)0x01;
    protected static final byte DFFile = (byte)0x02;
    protected static final byte EFFile = (byte)0x04;
    
    
    /********************************************************************
     * CardFileInfo constructors					*
     ********************************************************************/
    public SIMCardFileInfo(byte[] data) {
	fileInfo = data;
    }
	
    /********************************************************************
     * CardFileInfo interface implementation				*
     ********************************************************************/

    /**
     * Returns the identifier of the file.
     * It is most likely that this method is of no particular use, but since
     * the file identifier is the most basic information about a file at all,
     * it is included here anyway.
     *
     * @return the identifier of the file
     */
    public short getFileID() {
  	return (short)(((short)fileInfo[fileIDHigh])*256+fileInfo[fileIDLow]);
    }
    
    /**
     * Tests whether the file is a DF.
     *
     * @return <tt>true</tt> if the file is a DF, <tt>false</tt> otherwise
     */
    public boolean isDirectory() {
  	return (fileInfo[fileType]==EFFile);
    }
    
    /**
     * Tests whether the file is a transparent file.
     * The value returned is valid only if the file is not a DF,
     * that is if <tt>isDirectory</tt> returns <tt>false</tt>.
     *
     * @return <tt>true</tt> if the file is a transparent file
     *
     * @see #isDirectory
     */
    public boolean isTransparent() {
  	return fileInfo[EFStructure]==transparent;
    }
    
    /**
     * Tests whether the file is a cyclic file.
     * The value returned is valid only if the file is not a DF and not a
     * transparent file, that is if <tt>isDirectory</tt> as well as
     * <tt>isTransparent</tt> return <tt>false</tt>.
     *
     * @return <tt>true</tt> if the file is a cyclic file
     *
     * @see #isDirectory
     * @see #isTransparent
     */
    public boolean isCyclic() {
	return fileInfo[EFStructure]==cyclic;
    }
    
    /**
     * Tests whether the file is a variable record file.
     * The value returned is valid only if the file is not a DF and not a
     * transparent file, that is if <tt>isDirectory</tt> as well as
     * <tt>isTransparent</tt> return <tt>false</tt>.
     *
     * @return    <tt>true</tt> if the file is a structured file with
     *            variable record size
     *
     * @see #isDirectory
     * @see #isTransparent
     */
    public boolean isVariable() {
  	return false;
    }
    
    /**
     * Returns the length of the file.
     * If the file is a transparent file, that is if <tt>isDirectory</tt>
     * returns <tt>false</tt> and <tt>isTransparent</tt> returns <tt>true</tt>,
     * this method returns the number of bytes in the file.
     * <br>
     * If the file is a structured file with fixed record length, either
     * cyclic or non-cyclic, this method returns also returns the number
     * of bytes in the file, that is the size of a record multiplied by
     * the number of bytes in a record.
     * <br>
     * If the file is a DF, a structured file with variable record length,
     * or some card specific file type, the value returned by this method
     * is implementation dependent.
     *
     * @return the number of bytes in the file
     *
     * @see #isDirectory
     * @see #isTransparent
     */
    public int getLength() {
  	return (((int)fileInfo[fileSizeHigh])<<8)|fileInfo[fileSizeLow];
    }
    
    /**
     * Returns the record size of the file.
     * The value returned is valid only if the file is a structured file
     * with fixed record size, that is if <tt>isDirectory</tt>,
     * <tt>isTransparent</tt> and <tt>isVariable</tt> return <tt>false</tt>.
     *
     * @return the size of a record of the file
     *
     * @see #isDirectory
     * @see #isTransparent
     * @see #isVariable
     */
    public int getRecordSize() {
  	return fileInfo[recordLength];
    }
    
    /**
     * Returns the file header.
     * This method actually returns the smartcard's response (or expected
     * response) to a selection of the file. For an EF, this is the file
     * header which may have been expanded by some application data, if
     * the card OS allows to do so. For a DF, this is the file header,
     * also optionally expanded, or some other data that has been defined
     * as the select response for the DF. The EMV standard requires the
     * select response for a DF to be defineable by applications.
     * <br>
     * This method is not invoked by the OpenCard Framework, and it is
     * not yet clear whether it will be useful for applications. However,
     * removing a method from this interface will be easier than adding
     * it later, so it is currently required. File services that do not
     * want to support this method may simply return <tt>null</tt>.
     *
     * @return  the smartcard's response to a selection of the file,
     *          or <tt>null</tt> if not supported
     */
    public byte[] getHeader() {
  	return fileInfo;
    }

}
