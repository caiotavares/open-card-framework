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

/**
 * This interface defines the features that are necessary
 * for an application service to be able to access smartcards that
 * provide SIM Toolkit functionalities as defined by GSM 11.14.
 * 
 * @author Nguyen Thanh Lam, Tomasz Molag, Christophe.Muller@research.gemplus.com
 * @version $Id: SIMCardService.java,v 1.1 2004/09/13 16:26:26 cmuller13 Exp $
 * 
 */
public interface SIMCardService {
    
    /**
     * The <code>Constants</code> inner interface allows to use
     * standardized constants.<BR>
     * It provides constants used to deal with SIM smartcards.<BR>
     */
    public interface Constants {
	
	// SIM CommandAPDU Constants
	
	public static final byte CLASS = (byte)0xA0;
	
        public static final byte SELECT = (byte)0xA4;
	public static final byte STATUS = (byte)0xF2;
	
	public static final byte READ_BINARY = (byte)0xB0;
	public static final byte UPDATE_BINARY = (byte)0xD6;
	public static final byte READ_RECORD = (byte)0xB2;
	public static final byte UPDATE_RECORD = (byte)0xDC;
	public static final byte SEEK = (byte)0xA2;
	public static final byte INCREASE = (byte)0x32;
	
	public static final byte VERIFY_CHV = (byte)0x20;
	public static final byte CHANGE_CHV = (byte)0x24;
	public static final byte DISABLE_CHV = (byte)0x26;
	public static final byte ENABLE_CHV = (byte)0x28;
	public static final byte UNBLOCK_CHV = (byte)0x2C;
	
	public static final byte INVALIDATE = (byte)0x04;
	public static final byte REHABILITATE = (byte)0x44;
	
	public static final byte RUN_GSM_ALGORITHM = (byte)0x88;
	
	public static final byte SLEEP = (byte)0xFA;
	public static final byte GET_RESPONSE = (byte)0xC0;
	
	// Constants use by readRecord & updateRecord
	
	public static final byte NEXT_MODe = (byte)0x02;
	public static final byte PREVIOUS_MODE = (byte)0x03;
	public static final byte ABSOLUTE_MODE = (byte)0x04;
	
	// Constants use for CHVNumber

	public static final byte CHV1 = (byte)0x01;
	public static final byte UnblockCHV1 = (byte)0x00;
	public static final byte CHV2 = (byte)0x02;
	
	// SIM ResponseAPDU Constants
	
	//  - Correct processing category

	/**
	 * Status Word (Cp): No error <br> <code>SW1 = 0x90 - SW2 = 0x00</code>
	 */
	public static final byte SW1_NO_ERROR = (byte) 0x90;

	/**
	 * Status Word (Cp): Response bytes available <br> <code>SW1 = 0x9F - SW2 = 0xXX</code>
	 */
	public static final byte SW1_RESPONSE_LENGTH = (byte) 0x9F;

	/**
	 * Status Word (Cp): Response bytes available <br> <code>SW1 = 0x91 - SW2 = 0xXX</code>
	 */
	public static final byte SW1_RESPONSE_PROACTIVE = (byte) 0x91;

	/**
	 * Status Word (Cp): Response bytes available <br> <code>SW1 = 0x9E - SW2 = 0xXX</code>
	 */
	public static final byte SW1_RESPONSE_ENVELOPE = (byte) 0x9F;
	
	//  - Responses to commands which are postponed
	
	/**
	 * Status Word (Cpost): SIM Application Toolkit is busy <br> <code>SW1 = 0x93 - SW2 = 0x00</code>
	 */
	public static final byte SW1_PROACTIVE = (byte) 0x93;
	public static final byte SW2_BUSY = (byte) 0x00;
	
	//  - Application independent errors category
	
	/**
	 * Status Word (Aie): Wrong length <br> <code>SW1 = 0x67 - SW2 = 0x00</code>
	 */
	public static final byte SW1_P3 = (byte) 0x67;

	/**
	 * Status Word (Aie): Wrong P1 and/or P2 <br> <code>SW1 = 0x6B - SW2 = 0x00</code>
	 */
	public static final byte SW1_P1P2 = (byte) 0x6B;

	/**
	 * Status Word (Aie): INS value not supported <br> <code>SW1 = 0x6D - SW2 = 0x00</code>
	 */
	public static final byte SW1_COMMAND_CODE = (byte) 0x6D;

	/**
	 * Status Word (Aie): CLA value not supported <br> <code>SW1 = 0x6E - SW2 = 0x00</code>
	 */
	public static final byte SW1_INSTRUCTION_CLASS = (byte) 0x6E;

	/**
	 * Status Word (Aie): None precise diagnosis <br> <code>SW1 = 0x6F - SW2 = 0x00</code>
	 */
	public static final byte SW1_TECHNICAL = (byte) 0x6F;
	
	//  - Security management category
	
	public static final byte SW1_SECURITY = (byte) 0x98;
	
	/**
	 * Status Word (Sm): The security item does not exist <br> <code>SW1 = 0x98 - SW2 = 0x02</code>
	 */
	public static final byte SW2_NO_CHV = (byte) 0x02;

	/**
	 * Status Word (Sm): Security status not satisfied <br> <code>SW1 = 0x98 - SW2 = 0x04</code>
	 */
	public static final byte SW2_ACCESS_CONDITION = (byte) 0x04;

	/**
	 * Status Word (Sm): In contradiction with the PIN status <br> <code>SW1 = 0x98 - SW2 = 0x08</code>
	 */
	public static final byte SW2_CHV_STATUS = (byte) 0x08;

	/**
	 * Status Word (Sm): In contradiction with the invalidation status <br> <code>SW1 = 0x98 - SW2 = 0x10</code>
	 */
	public static final byte SW2_INVALIDATION = (byte) 0x10;

	/**
	 * Status Word (Sm): Crypto process not incomplet <br> <code>SW1 = 0x98 - SW2 = 0x35</code>
	 */
	// public static final byte SW_CRYPTO_PROCESS_INCOMPLET= (byte) 0x9835;
	
	/**
	 * Status Word (Sm): PIN is blocked <br> <code>SW1 = 0x98 - SW2 = 0x40</code>
	 */
	public static final byte SW2_CHV_ERROR = (byte) 0x40;

	/**
	 * Status Word (Sm): Counter blocked <br> <code>SW1 = 0x98 - SW2 = 0x50</code>
	 */
	public static final byte SW2_INCREASE= (byte) 0x50;
	
	//  - Memory management category
	
	public static final byte SW1_MEMORY = (byte) 0x92;
	
	/**
	 * Status Word (Mm): Insufficient memory space <br> <code>SW1 = 0x92 - SW2 = 0x10</code>
	 */
	public static final byte SW2_MEMORY = (byte) 0x40;
	
	//  - Referencing management category
	
	public static final byte SW1_FILE = (byte) 0x94;
	/**
	 * Status Word (Mm): EF not selected <br> <code>SW1 = 0x94 - SW2 = 0x00</code>
	 */
	public static final byte SW2_NO_FILE_SELECTED = (byte) 0x00;
	
	/**
	 * Status Word (Mm): Out of range <br> <code>SW1 = 0x94 - SW2 = 0x02</code>
	 */
	public static final byte SW2_OUT_OF_RANGE= (byte) 0x02;
	
	/**
	 * Status Word (Mm): File not find <br> <code>SW1 = 0x94 - SW2 = 0x04</code>
	 */
	public static final byte SW2_FILE_NOT_FOUND = (byte) 0x04;
	
	/**
	 * Status Word (Mm): File incosistent with the APDU <br> <code>SW1 = 0x94 - SW2 = 0x08</code>
	 */
	public static final byte SW2_WRONG_FILE_TYPE = (byte) 0x08;

	// SIM ProactiveAPDU Constants

	public static byte TERMINAL_PROFILE = (byte)0x10;

	public static byte ENVELOPE = (byte)0xC2;

	public static byte FETCH = (byte)0x12;

	public static byte TERMINAL_RESPONSE = (byte)0x14;

    }

    /**********************************************
     * SIM Low Level Commands			  *
     **********************************************/
    
    public boolean proactiveCommandWaiting();
    
    public int getProactiveCommandLength();
    
    public ResponseAPDU select(short fileID)
	throws InvalidCardChannelException, CardTerminalException;
    
    public ResponseAPDU status(byte length) 
	throws InvalidCardChannelException, CardTerminalException;
    
    public ResponseAPDU readBinary(short offset, byte length) 
	throws InvalidCardChannelException, CardTerminalException;
    
    public ResponseAPDU updateBinary(short offset, byte length, byte[] data) 
	throws InvalidCardChannelException, CardTerminalException;
    
    public ResponseAPDU readRecord(byte recordNumber, byte mode, byte length)
	throws InvalidCardChannelException, CardTerminalException;
    
    public ResponseAPDU updateRecord(byte recordNumber, byte mode, byte length, byte[] data) 
	throws InvalidCardChannelException, CardTerminalException;
    
    public ResponseAPDU seek(byte type_mode, byte length, byte[] pattern) 
	throws InvalidCardChannelException, CardTerminalException;
    
    public ResponseAPDU increase(byte[] value) 
	throws InvalidCardChannelException, CardTerminalException;
    
    public ResponseAPDU verifyCHV(byte CHVNumber) 
	throws InvalidCardChannelException,
	       CardServiceInvalidCredentialException,
	       CardTerminalException;
    
    public ResponseAPDU changeCHV(byte CHVNumber, byte[] oldCHV, byte[]newCHV) 
	throws InvalidCardChannelException, CardTerminalException;
    
    public ResponseAPDU disableCHV(byte[] CHV1Value) 
	throws InvalidCardChannelException, CardTerminalException;
    
    public ResponseAPDU enableCHV(byte[] CHV1Value) 
	throws InvalidCardChannelException, CardTerminalException;
    
    public ResponseAPDU unblockCHV(byte CHVNumber, byte[] unblockCHVValue, byte[] newCHV) 
	throws InvalidCardChannelException, CardTerminalException;
    
    public ResponseAPDU invalidate() 
	throws InvalidCardChannelException, CardTerminalException;
    
    public ResponseAPDU rehabilitate() 
	throws InvalidCardChannelException, CardTerminalException;
    
    public ResponseAPDU runGsmAlgorithm(byte[] rand) 
	throws InvalidCardChannelException, CardTerminalException;
    
    public ResponseAPDU sleep() 
	throws InvalidCardChannelException, CardTerminalException;
    
    public ResponseAPDU getResponse(byte length) 
	throws InvalidCardChannelException, CardTerminalException;
    
    public ResponseAPDU terminalProfile(byte length, byte[] data) 
	throws InvalidCardChannelException, CardTerminalException;
    
    public ResponseAPDU envelope(byte length, byte[] data) 
	throws InvalidCardChannelException, CardTerminalException;
    
    public ResponseAPDU fetch(byte length) 
	throws InvalidCardChannelException, CardTerminalException;
    
    public ResponseAPDU terminalResponse(byte length, byte[] data) 
	throws InvalidCardChannelException, CardTerminalException;

}
