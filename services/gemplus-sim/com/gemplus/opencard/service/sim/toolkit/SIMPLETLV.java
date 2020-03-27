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

/**
 * This interface defines constants for a SIMPLE TLV Object.
 * 
 * @author Nguyen Thanh Lam, Tomasz Molag, Christophe.Muller@research.gemplus.com
 * @version $Id: SIMPLETLV.java,v 1.1 2004/09/13 16:26:27 cmuller13 Exp $
 * 
 */
public interface SIMPLETLV {
    
    /**
     * The <code>Constants</code> inner interface allows to use
     * standardized constants.<BR>
     * It provides constants used to deal with SIMPLE TLV Tags
     * in SIM smartcards.<BR>
     */
    public interface Constants {

        // SIMPLE TLV Tag Constants
	
	//  - extended indicator of extended length
	
	/**
	 *       Status Word (Cp): extanded Data indication
	 */
	public static final int extendedData = (int) 0x81;
	
	//  - comprehension flag
	
	/**
	 *       Status Word (Cp): comprehension flag set
	 */
	public static final byte COMPREHENSION = (byte) 0x80;
	
	/**
	 *       Status Word (Cp): comprehension flag NOT set
	 */
	public static final byte NO_COMPREHENSION = (byte) 0x00;
	
	//  - the real tags
	
	/**
	 *       Status Word (Cp): Command details tag
	 */
	public static final byte COMMAND_TAG = (byte) 0x01;
	
	/**
	 *       Status Word (Cp): Device identity tag
	 */
	public static final byte DEVICE_TAG = (byte) 0x02;
	
	/**
	 *       Status Word (Cp): Result tag
	 */
	public static final byte RESULT_TAG = (byte) 0x03;
	
	/**
	 *       Status Word (Cp): Duration tag
	 */
	public static final byte DURATION_TAG = (byte) 0x04;
	
	/**
	 *       Status Word (Cp): Alpha identifier tag
	 */
	public static final byte ALPHA_TAG = (byte) 0x05;
	
	/**
	 *       Status Word (Cp): Address tag
	 */
	public static final byte ADDRESS_TAG = (byte) 0x06;
	
	/**
	 *       Status Word (Cp): Capability configuration parameters tag
	 */
	public static final byte CAPABILITY_TAG = (byte) 0x07;
	
	/**
	 *       Status Word (Cp): Called party subaddress tag
	 */
	public static final byte CALLED_TAG = (byte) 0x08;
	
	/**
	 *       Status Word (Cp): SS string tag
	 */
	public static final byte SS_TAG = (byte) 0x09;
	
	/**
	 *       Status Word (Cp): reserved for USSD string tag
	 */
	public static final byte USSD_TAG = (byte) 0x0A;
	
	/**
	 *       Status Word (Cp): SMS TPDU tag
	 */
	public static final byte SMS_TAG = (byte) 0x0B;
	
	/**
	 *       Status Word (Cp): Cell broadcast page tag
	 */
	public static final byte CELL_TAG = (byte) 0x0C;
	
	/**
	 *       Status Word (Cp): Text string tag
	 */
	public static final byte TEXT_TAG = (byte) 0x0D;
	
	/**
	 *       Status Word (Cp): Tone tag
	 */
	public static final byte TONE_TAG = (byte) 0x0E;
	
	/**
	 *       Status Word (Cp): Item tag
	 */
	public static final byte ITEM_TAG = (byte) 0x0F;
	
	/**
	 *       Status Word (Cp): Item identifier tag
	 */
	public static final byte ITEM_IDENTIFIER_TAG = (byte) 0x10;
	
	/**
	 *       Status Word (Cp): Respone length tag
	 */
	public static final byte RESPONSE_TAG = (byte) 0x11;
	
	/**
	 *       Status Word (Cp): File list tag
	 */
	public static final byte FILE_TAG = (byte) 0x12;
	
	/**
	 *       Status Word (Cp): Location Information tag
	 */
	public static final byte LOCATION_INFO_TAG = (byte) 0x13;
	
	/**
	 *       Status Word (Cp): IMEI tag
	 */
	public static final byte IMEI_TAG = (byte) 0x14;
	
	/**
	 *       Status Word (Cp): Help request tag
	 */
	public static final byte HELP_TAG = (byte) 0x15;
	
	/**
	 *       Status Word (Cp): Network measurements result tag
	 */
	public static final byte MEASUREMENT_TAG = (byte) 0x16;
	
	/**
	 *       Status Word (Cp): Default text tag
	 */
	public static final byte DEFAULT_TEXT_TAG = (byte) 0x17;
	
	/**
	 *       Status Word (Cp): Items next action indication tag
	 */
	public static final byte NEXT_ACTION_TAG = (byte) 0x18;
	
	/**
	 *       Status Word (Cp): Event list tag
	 */
	public static final byte EVENT_TAG = (byte) 0x19;
	
	/**
	 *       Status Word (Cp): Cause tag
	 */
	public static final byte CAUSE_TAG = (byte) 0x1A;
	
	/**
	 *       Status Word (Cp): Location status tag
	 */
	public static final byte LOCATION_STATUS_TAG = (byte) 0x1B;
	
	/**
	 *       Status Word (Cp): Transaction identifier tag
	 */
	public static final byte TRANSACTION_TAG = (byte) 0x1C;
	
	/**
	 *       Status Word (Cp): BOCH channel list tag
	 */
	public static final byte BOCH_TAG = (byte) 0x1D;
	
    }
}
