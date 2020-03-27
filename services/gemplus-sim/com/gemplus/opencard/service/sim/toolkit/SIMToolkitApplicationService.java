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
package com.gemplus.opencard.service.sim.toolkit;

//-----------------------------------------------------------------------------
// IMPORTS
//-----------------------------------------------------------------------------
import java.util.*;
import opencard.core.terminal.CardTerminalException;
import opencard.core.service.CardServiceException;

/**
 * This interface defines the features that are necessary
 * for an application service to be able to access smartcards that
 * provide SIM Toolkit functionalities as defined by GSM 11.14.
 * 
 * @author Nguyen Thanh Lam, Tomasz Molag, Christophe.Muller@research.gemplus.com
 * @version $Id: SIMToolkitApplicationService.java,v 1.1 2004/09/13 16:26:27 cmuller13 Exp $
 * 
 */
public interface SIMToolkitApplicationService {

    /**
     * The <code>Constants</code> inner interface allows to use
     * standardized constants.<BR>
     * It provides constants used to deal with SIM smartcards.<BR>
     */
    public interface Constants {

	// Device Constants
	
        /**
	 *       Status Word (Cp): Keypad
	 */
        public static final byte KEYPAD = (byte) 0x01;
	
        /**
	 *       Status Word (Cp): Display
	 */
        public static final short DISPLAY = (byte) 0x02;
	
        /**
	 *       Status Word (Cp): Earpice
	 */
        public static final short EARPICE = (byte) 0x03;
	
        /**
	 *       Status Word (Cp): SIM
	 */
        public static final short SIM = (byte) 0x81;
	
        /**
	 *       Status Word (Cp): ME
	 */
        public static final short ME = (byte) 0x82;
	
        /**
	 *       Status Word (Cp): Network
	 */
        public static final short NETWORK = (byte) 0x83;
	
	// Profile Constants
	
	//  - download
	
        /**
	 *       Status Word (Cp): profile Download
	 */
        public static final short PROFILEDOWNLOAD = (short) 0x0001;
	
        /**
	 *       Status Word (Cp): SMS-PP data download
	 */
        public static final short SMSDOWNLOAD = (short) 0x0002;
	
        /**
	 *       Status Word (Cp): cell broadcast data download
	 */
        public static final short CELLBDOWNLOAD = (short) 0x0004;
	
        /**
	 *       Status Word (Cp): Nenu selection
	 */
        public static final short MENUSELECTION = (short) 0x0008;
	
        /**
	 *       Status Word (Cp): new status word for SIM data download error
	 */
        public static final short NEWSTATUS = (short) 0x0010;
	
	//  - other 
	
        /**
	 *       Status Word (Cp): command result
	 */
        public static final short COMMANDRESULT = (short) 0x0101;
	
        /**
	 *       Status Word (Cp): call control by SIM
	 */
        public static final short CALLCONTROL = (short) 0x0102;
	
        /**
	 *       Status Word (Cp): call identity included in call control
	 */
        public static final short CALLIDENTITY = (short) 0x0104;
	
        /**
	 *       Status Word (Cp): MO short message control
	 */
        public static final short MOCONTROL = (short) 0x0108;
	
        /**
	 *       Status Word (Cp): handling of the alpha identifier
	 */
        public static final short ALPHAHANDLING = (short) 0x0110;
	
        /**
	 *       Status Word (Cp): UCS2 entry supported
	 */
        public static final short UCS2ENTRY = (short) 0x0120;
	
        /**
	 *       Status Word (Cp): UCS2 display supported
	 */
        public static final short UCS2DISPLAY = (short) 0x0140;
	
	
	//  - proactive SIM

        /**
	 *       Status Word (Cp):display text
	 */
        public static final short DISPLAYTEXT = (short) 0x0201;
	
        /**
	 *       Status Word (Cp): get inkey
	 */
        public static final short GETINKEY = (short) 0x0202;
	
        /**
	 *       Status Word (Cp): get input
	 */
        public static final short GETINPUT = (short) 0x0204;
	
        /**
	 *       Status Word (Cp): more time
	 */
        public static final short MORETIME = (short) 0x0208;
	
        /**
	 *       Status Word (Cp): play tone
	 */
        public static final short PLAYTONE = (short) 0x0210;
	
        /**
	 *       Status Word (Cp): poll interval
	 */
        public static final short POLLINTERVAL = (short) 0x0220;
	
        /**
	 *       Status Word (Cp): polling off
	 */
        public static final short POLLINGOFF = (short) 0x0240;
	
        /**
	 *       Status Word (Cp): refresh
	 */
        public static final short REFRESH = (short) 0x0280;
	
        /**
	 *       Status Word (Cp): select item
	 */
        public static final short SELECTITEM = (short) 0x0301;
	
        /**
	 *       Status Word (Cp): send short message
	 */
        public static final short SENDSMS = (short) 0x0302;
	
        /**
	 *       Status Word (Cp) :send SS
	 */
        public static final short SENDSS = (short) 0x0304;
	
        /**
	 *       Status Word (Cp): send USSD
	 */
        public static final short SENDUSSD = (short) 0x0308;
	
        /**
	 *       Status Word (Cp): setup call
	 */
        public static final short SETUPCALL = (short) 0x0310;
	
        /**
	 *       Status Word (Cp): set up menu
	 */
        public static final short SETUPMENU = (short) 0x0320;
	
        /**
	 *       Status Word (Cp): provide local information
	 */
        public static final short PROVIDEINFO = (short) 0x0340;
	
        /**
	 *       Status Word (Cp): provide local information if NMR supported
	 */
        public static final short PROVIDEINFONMR = (short) 0x0380;

	//  - event
	
        /**
	 *       Status Word (Cp): set up event list
	 */
        public static final short SETUP = (short) 0x0401;
	
        /**
	 *       Status Word (Cp): MT call
	 */
        public static final short MTCALL = (short) 0x0402;
	
        /**
	 *       Status Word (Cp): call connected
	 */
        public static final short CALLCONNECTED = (short) 0x0404;
	
        /**
	 *       Status Word (Cp): call disconnected
	 */
        public static final short CALLDISCONNECTED = (short) 0x0408;
	
        /**
	 *       Status Word (Cp): location status
	 */
        public static final short LOCATIONSTATUS = (short) 0x0410;
	
        /**
	 *       Status Word (Cp): user activity
	 */
        public static final short USER = (short) 0x0420;
	
        /**
	 *       Status Word (Cp): Idle screen available
	 */
        public static final short IDLESCREEN = (short) 0x0440;

	// Interval Constants

        /**
	 *       Status Word (Cp): minutes
	 */
        public static final short MINUTES = (byte) 0x00;
	
        /**
	 *       Status Word (Cp): Seconds
	 */
        public static final short SECONDS = (byte) 0x01;
	
        //  - the real tags
	
        /**
	 *       Status Word (Cp): Tenths of seconds
	 */
        public static final short INTERVAL_TAG = (byte) 0x02;

	// Tone Constants

	//  - standard supervisory tones

        /**
	 *       Status Word (Cp): Dial tone
	 */
        public static final byte DIAL = (byte) 0x01;
	
        /**
	 *       Status Word (Cp): Called subscriber busy
	 */
        public static final byte BUSY = (byte) 0x02;
	
        /**
	 *       Status Word (Cp): Congestion
	 */
        public static final byte CONGESTION = (byte) 0x03;
	
        /**
	 *       Status Word (Cp): Radio path acknowledge
	 */
        public static final byte RADIO_PATH_ACK = (byte) 0x04;
	
        /**
	 *       Status Word (Cp): Radio path not available / Call droped
	 */
        public static final byte NO_RADIO_PATH = (byte) 0x05;
	
        /**
	 *       Status Word (Cp): Error / Special information
	 */
        public static final byte SPECIAL = (byte) 0x06;
	
        /**
	 *       Status Word (Cp): Call waiting tone
	 */
        public static final byte CALL_WAITING = (byte) 0x07;
	
        /**
	 *       Status Word (Cp): Ringing tone
	 */
        public static final byte RINGING = (byte) 0x08;
	
	
        //  - ME propprietary tones
	
        /**
	 *       Status Word (Cp): General beep
	 */
        public static final byte GENERAL = (byte) 0x10;
	
        /**
	 *       Status Word (Cp): Positive acknowledgement
	 */
        public static final byte POSITIVE_ACK = (byte) 0x11;
	
        /**
	 *       Status Word (Cp): Negative acknoeledgement OR error
	 */
        public static final byte NEGATIVE_ACK = (byte) 0x12;
	
    }

    /**
     * This method allows to send a fetch command.
     * 
     * @return  an enumeration of the BER TLV objects, if any available
     */
    public Enumeration fetch(int length, BERTLV ber)
	throws CardServiceException, CardTerminalException;

    /**
     * This method allows to send a terminal profile command.
     */
    public void profileDownload(Profile profile)
	throws CardServiceException, CardTerminalException;

    /**
     * This method allows to send an envelope.
     * 
     * @return  byte[]
     */
    public byte[] envelope(Envelope envelope)
	throws CardServiceException, CardTerminalException;

    /**
     * This method allows to send a terminal response
     */
    public void terminalResponse(TerminalResponse terminalresponse)
	throws CardServiceException, CardTerminalException;

}
