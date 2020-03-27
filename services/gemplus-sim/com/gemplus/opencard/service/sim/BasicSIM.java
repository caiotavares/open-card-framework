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
import java.lang.String;

import opencard.core.terminal.*;
import opencard.core.service.*;
import opencard.core.util.*;

import opencard.opt.iso.fs.*;

import com.gemplus.opencard.service.sim.SIMCardFileInfo;

/**
 * This class is designed as a basic default class which allows
 * programmers to access GSM 11.11-compliant smartcards. 
 * <p>It provides all methods necessary to access SIM smartcards,
 * as describes in the GSM 11.11 norm.
 * <p>It is intended that the CardService developers for specific
 * card use this class as a base class and by inheriting its
 * functionalities.
 * 
 * @author Nguyen Thanh Lam, Tomasz Molag, Christophe.Muller@research.gemplus.com
 * @version $Id: BasicSIM.java,v 1.1 2004/09/13 16:26:26 cmuller13 Exp $
 * 
 */
public class BasicSIM  extends CardService
    implements SIMCardService.Constants, SIMCardService {

    // A tracer for debugging output
    private static Tracer ctracer = new Tracer(BasicSIM.class);

    protected CardChannel channel;
    protected CardServiceScheduler scheduler;
    protected SmartCard smartcard;
    
    protected CommandAPDU command = new CommandAPDU(260);
    protected ResponseAPDU response = new ResponseAPDU(258);
    protected boolean proactiveInWaiting;
    protected int proactiveCommandLength;

    /**********************************************
     * Constructor, Initializer                   *
     **********************************************/
    public void BasicSIM() {
    }
  
    protected void initialize(CardServiceScheduler aScheduler,
			      SmartCard aSmartCard,
			      boolean blocking)
	throws CardServiceException {
	super.initialize(aScheduler, aSmartCard, blocking);
	scheduler = aScheduler;
	smartcard = aSmartCard;
    }
    
    /**********************************************
     * SIMCommands				*
     **********************************************/
    protected void testProactive(byte sw1, byte sw2) {
	if (proactiveInWaiting = (sw1 == (byte)0x91))
	    proactiveCommandLength = sw2;
    }
    
    protected ResponseAPDU sendAPDU(byte INS, byte P1, byte P2, byte P3)
	throws InvalidCardChannelException,
	       CardTerminalException	{		
	command.setLength(0);
	command.append(CLASS);
	command.append(INS);
	command.append(P1);
	command.append(P2);
	command.append(P3);
	
	allocateCardChannel();
	channel = getCardChannel();
	if (!channel.isOpen())
	    channel.open();
	response = channel.sendCommandAPDU(command);
	channel.close();
	releaseCardChannel();
	testProactive(response.sw1(), response.sw2());
	return response;
    }
    
    protected ResponseAPDU sendAPDU(byte INS, byte P1, byte P2, byte P3, byte[] data)
	throws InvalidCardChannelException,
	       CardTerminalException	{	
	command.setLength(0);
	command.append(CLASS);
	command.append(INS);
	command.append(P1);
	command.append(P2);
	command.append(P3);
	command.append(data);
	
	allocateCardChannel();
	channel = getCardChannel();	
	if (!channel.isOpen())
	    channel.open();
	response = channel.sendCommandAPDU(command);
	channel.close();
	releaseCardChannel();
	
	testProactive(response.sw1(), response.sw2());
	return response;
    }
    
    
    public boolean proactiveCommandWaiting() {
	return proactiveInWaiting;
    }
    
    public int getProactiveCommandLength() {
	return proactiveCommandLength;
    }
    
    public ResponseAPDU select(short fileID) 
	throws InvalidCardChannelException,
	       CardTerminalException {
	byte[] transmitData = new byte[2];
	
	transmitData[0]=(byte)(fileID>>8);
	transmitData[1]=(byte)(fileID&0xFF);
	return sendAPDU(SELECT, (byte)0x00, (byte)0x00, (byte)0x02, transmitData);
    }
    
    public ResponseAPDU status(byte length) 
	throws InvalidCardChannelException,
	       CardTerminalException {
	return sendAPDU(STATUS, (byte)0x00, (byte)0x00, length);
    }
    
    public ResponseAPDU readBinary(short offset, byte length) 
	throws InvalidCardChannelException,
	       CardTerminalException {
	return sendAPDU(READ_BINARY, (byte)(offset>>8), (byte)(offset|0xFF), length);
    }
    
    public ResponseAPDU updateBinary(short offset, byte length, byte[] data) 
	throws InvalidCardChannelException,
	       CardTerminalException {
	return sendAPDU(UPDATE_BINARY, (byte)(offset>>8), (byte)(offset|0xFF), length, data);
    }
    
    public ResponseAPDU readRecord(byte recordNumber, byte mode, byte length) 
	throws InvalidCardChannelException,
	       CardTerminalException {
	return sendAPDU(READ_RECORD, recordNumber, mode, length);
    }
    
    public ResponseAPDU updateRecord(byte recordNumber, byte mode, byte length, byte[] data) 
	throws InvalidCardChannelException,
	       CardTerminalException {
	return sendAPDU(UPDATE_RECORD, recordNumber, mode, length, data);
    }
    
    public ResponseAPDU seek(byte type_mode, byte length, byte[] pattern) 
	throws InvalidCardChannelException,
	       CardTerminalException {
	return sendAPDU(SEEK, (byte)0x00, type_mode, length, pattern);
    }
    
    public ResponseAPDU increase(byte[] value) 
	throws InvalidCardChannelException,
	       CardTerminalException {
	return sendAPDU(INCREASE, (byte)0x00, (byte)0x00, (byte)0x03, value);
    }
    
    public ResponseAPDU verifyCHV(byte CHVNumber) 
	throws InvalidCardChannelException,
	       CardServiceInvalidCredentialException,
	       CardTerminalException {
	byte[] password = { (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
			    (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF };
	
	ctracer.debug("verifyCHV", "Enter in verifyCHV Code.");
	
	command.setLength(0);
	command.append(CLASS);
	command.append(VERIFY_CHV);
	command.append((byte)0x00);
	command.append(CHVNumber);  
	command.append((byte)0x08); 
	command.append(password);
	
	ctracer.debug("verifyCHV", "set IOCtrl.");
	CardTerminalIOControl IOCtrl = new CardTerminalIOControl(8, 30, null, null);
	ctracer.debug("verifyCHV", "IOCtrl ok\nCHVCtrl");
	CHVControl CHVCtrl = new CHVControl("Coucou", CHVNumber, CHVEncoder.STRING_ENCODING, 0, IOCtrl);
	ctracer.debug("verifyCHV", "CHVCtrl ok\nallocateCardChannel...");
	allocateCardChannel();
	ctracer.debug("verifyCHV", "allocateCardChannel ok\ngetCardChannel...");
   	channel = getCardChannel();
	ctracer.debug("verifyCHV", "getCardChannel ok\nOpen ...");
	if (!channel.isOpen())
	    channel.open();
  	ctracer.debug("verifyCHV", "Send sendVerifiedAPDU...");
	// CM: removed 'timeout' because it has been deprecated in OCF 1.2!
	response = channel.sendVerifiedAPDU(command, CHVCtrl, getCHVDialog());
	ctracer.debug("verifyCHV", "Send sendVerifiedAPDU ok.");
	channel.close();
	releaseCardChannel();
	
	ctracer.debug("verifyCHV", "Exit in verifyCHV.");
	
	return response;
    }
    
    public ResponseAPDU changeCHV(byte CHVNumber, byte[] oldCHV, byte[]newCHV) 
	throws InvalidCardChannelException,
	       CardTerminalException {
	byte[] transmitData = new byte[16];
	byte i;
	
	for(i = 0; i < 8; i++)
	    transmitData[i] = oldCHV[i];
	for(i = 0; i < 8; i++)
	    transmitData[i+8]  = newCHV[i];
	return sendAPDU(CHANGE_CHV, (byte)0x00, CHVNumber, (byte)0x10, transmitData);
    }
    
    public ResponseAPDU disableCHV(byte[] CHV1Value) 
	throws InvalidCardChannelException,
	       CardTerminalException {
	return sendAPDU(DISABLE_CHV, (byte)0x00, (byte)0x01, (byte)0x08, CHV1Value);
    }
    
    public ResponseAPDU enableCHV(byte[] CHV1Value) 
	throws InvalidCardChannelException,
	       CardTerminalException {
	return sendAPDU(ENABLE_CHV, (byte)0x00, (byte)0x01, (byte)0x08, CHV1Value);
    }
    
    public ResponseAPDU unblockCHV(byte CHVNumber, byte[] unblockCHVValue, byte[] newCHV) 
	throws InvalidCardChannelException,
	       CardTerminalException {
	byte[] transmitData = new byte[16];
	int i;
	int length;
	
	ctracer.debug("unblockCHV", "Enter in unblockCHV Code.");
	length = 8;
	if (unblockCHVValue.length < length)
	    length = unblockCHVValue.length;
	for(i = 0; i < length; i++)
	    transmitData[i] = unblockCHVValue[i];
	if (length < 8)
	    for(i = length; i < 8; i++)
		transmitData[i] = (byte)0xff;
	
	length = 8;
	if (newCHV.length < length)
	    length = newCHV.length;
	for(i = 0; i < length; i++)
	    transmitData[i+8] = newCHV[i];
	if (length < 8)
	    for(i = length; i < 8; i++)
		transmitData[i+8] = (byte)0xff;
	
	response = sendAPDU(UNBLOCK_CHV, (byte)0x00, CHVNumber, (byte)0x10, transmitData);
	
	ctracer.debug("unblockCHV", "Exit in unblockCHV Code.");
	
	return response;
    }
    
    public ResponseAPDU invalidate() 
	throws InvalidCardChannelException,
	       CardTerminalException {
	return sendAPDU(INVALIDATE, (byte)0x00, (byte)0x00, (byte)0x00);
    }
    
    public ResponseAPDU rehabilitate() 
	throws InvalidCardChannelException,
	       CardTerminalException {
	return sendAPDU(REHABILITATE, (byte)0x00, (byte)0x00, (byte)0x00);
    }
    
    public ResponseAPDU runGsmAlgorithm(byte[] rand) 
	throws InvalidCardChannelException,
	       CardTerminalException {
	return sendAPDU(RUN_GSM_ALGORITHM, (byte)0x00, (byte)0x00, (byte)0x10, rand);
    }
    
    public ResponseAPDU sleep() 
	throws InvalidCardChannelException,
	       CardTerminalException {
	return sendAPDU(SLEEP, (byte)0x00, (byte)0x00, (byte)0x00);
    }
    
    public ResponseAPDU getResponse(byte length) 
	throws InvalidCardChannelException,
	       CardTerminalException {
	return sendAPDU(GET_RESPONSE, (byte)0x00, (byte)0x00, length);
    }
    
    public ResponseAPDU terminalProfile(byte length, byte[] data) 
	throws InvalidCardChannelException,
	       CardTerminalException {
	return sendAPDU(TERMINAL_PROFILE, (byte)0x00, (byte)0x00, length, data);
    }
    
    public ResponseAPDU envelope(byte length, byte[] data) 
	throws InvalidCardChannelException,
	       CardTerminalException {
	return sendAPDU(ENVELOPE, (byte)0x00, (byte)0x00, length, data);
    }
    
    public ResponseAPDU fetch(byte length) 
	throws InvalidCardChannelException,
	       CardTerminalException {
	return sendAPDU(FETCH, (byte)0x00, (byte)0x00, length);
    }
    
    public ResponseAPDU terminalResponse(byte length, byte[] data) 
	throws InvalidCardChannelException,
	       CardTerminalException {
	return sendAPDU(TERMINAL_RESPONSE, (byte)0x00, (byte)0x00, length, data);
    }
    
    /**********************************************
     * SIM Card Recognition (used by Factories)   *
     **********************************************/

    /**
     * A static method to analyze a smartcard in order to
     * determine if it is a SIM card, i.e., compliant with the
     * GSM 11.11 standard. It is not possible here to analyze only
     * the card ATR because the standard does not specify anything
     * about it. On the contrary the following method is applied:
     * <ol>
     *    <li>a temporary SlotChannel is allocated (using the scheduler),
     *    <li>a 'STATUS' command w/o password is sent, and
     *    <li>the response is analyzed (it should be 9000 for a SIM card).
     * </ol><br>
     * (Note: a more precise method should be applied, e.g., to analyse
     * some mandatory file contents etc.)
     * This method complies with the following OCF naming pattern:<br>
     * "A CardService 'knows' how to recognize supported cards"
     *
     * @param cid         the ATR of the smartcard
     * @param sched       a CardServiceScheduler for temporary communication
     * @return            true or false
     */
    public static boolean knows(CardID cid, CardServiceScheduler sched) {
        boolean isSIM = false;

        CommandAPDU statusCommand=null;
        ResponseAPDU statusResponse=null;

        statusCommand=new CommandAPDU(22);
	statusCommand.setLength(0);
        statusCommand.append((byte) 0xA0);
	statusCommand.append((byte) 0xF2);
	statusCommand.append((byte) 0x00);
	statusCommand.append((byte) 0x00);
	statusCommand.append((byte) 0x16);

        // Send 'STATUS' Command
        // (Note: it is not possible to use the "status()" method here
        // because a SlotChannel must be used instead of a CardChannel!)
        try {
            ctracer.debug("knows",
                          "STATUS CommandAPDU : "
                          + statusCommand.toString());
            statusResponse = sched.getSlotChannel().sendAPDU(statusCommand);
            ctracer.debug("knows",
                          "STATUS ResponseAPDU : "
                          + statusResponse.toString());
        } catch (Exception cte) {
            java.lang.System.err.println("Communication Problems During StatusUser.");
        }

        // Analyze response and returns result
	//0000:  85 14 00 88 3F 00 01 80 FF FF FF 03 09 09 04 0A
	//0010:  04 00 83 8A 83 8A 90 00

        if (((byte)statusResponse.sw1() == (byte)0x90)
            && ((byte)statusResponse.sw2() == (byte)0x00)) {
            isSIM = true;
            ctracer.info("knows", "SIM_CARDTYPE card found!");
        } else {
            isSIM = false;
            ctracer.info("knows", "Not a SIM card.");
        }
        return isSIM;
    }

}
