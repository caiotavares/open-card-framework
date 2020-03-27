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
 * This interface defines the features for a simple BER TLV
 * object class for using with a SIM card.
 * 
 * @author Nguyen Thanh Lam, Tomasz Molag, Christophe.Muller@research.gemplus.com
 * @version $Id: BERTLV.java,v 1.1 2004/09/13 16:26:27 cmuller13 Exp $
 * 
 */
public interface BERTLV {
    
    /**
     * The <code>Constants</code> inner interface allows to use
     * standardized constants.<BR>
     * It provides constants used to deal with BER TLV Tags
     * in SIM smartcards.<BR>
     */
    public interface Constants {
	
	// BER TLV Tag Constants
	
	//  - direction ME to SIM
	
        /**
	 *       Status Word (Cp): SMS-PP download tag
	 */
        public static final short SMS_DOWNLOAD_TAG = (byte) 0xD1;
	
        /**
	 *       Status Word (Cp): Cell Broadcast download tag
	 */
        public static final short BROADCAST_DOWNLOAD_TAG = (byte) 0xD2;
	
        /**
	 *       Status Word (Cp): Menu Selection tag
	 */
        public static final short MENU_SELECTION_TAG = (byte) 0xD3;
	
        /**
	 *       Status Word (Cp): Call Controll tag
	 */
        public static final short CALL_CONTROL_TAG = (byte) 0xD4;
	
        /**
	 *       Status Word (Cp): MO short message control tag
	 */
        public static final short SMS_CONTROL_TAG = (byte) 0xD5;
	
        /**
	 *       Status Word (Cp): Event Download tag
	 */
        public static final short EVENT_TAG = (byte) 0xD6;
	
        //  - direction SIM to ME

        /**
	 *       Status Word (Cp): Proactive SIM command tag
	 */
        public static final short PROACTIVE_COMMAND_TAG = (byte) 0xD0;

    }

    public int getNbOfObjects();

    public Object readObject(int index)
	throws BERTLVException;

    public void constructBERTLV(byte[] data);
    
}
