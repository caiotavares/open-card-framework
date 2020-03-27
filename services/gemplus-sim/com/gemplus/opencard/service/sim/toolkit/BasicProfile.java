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
import com.gemplus.opencard.service.sim.*;

/**
 * This class is a SIM Toolkit Profile Object. (see GSM 11.14 standard).
 *
 * @author Nguyen Thanh Lam, Tomasz Molag, Christophe.Muller@research.gemplus.com
 * @version $Id: BasicProfile.java,v 1.1 2004/09/13 16:26:27 cmuller13 Exp $
 * 
 */
public class BasicProfile implements Profile, SIMToolkitApplicationService.Constants {
    
    private byte[] contents = new byte[6];
    
    /**
     * constructor
     */
    public BasicProfile() {
	for (int i = 0; i < 6; i++) {
	    contents[i] = (byte) 0;
	}
    }
    
    public BasicProfile(final short feature) {
	for (int i = 0; i < 6; i++) {
	    contents[i] = (byte) 0;
	}
	setFeature(feature);
    }
    
    /**
     * methods
     */

    public void setFeature(final short feature) {
	contents[(feature >> 8)]
	    = (byte)(contents[(feature >> 8)] | (feature & 0x00FF));
    }
    
    public void unSetFeature(final short feature) {
	contents[(feature >> 8)]
	    = (byte)(contents[(feature >> 8)] & ~(feature & 0x00FF));
    }
    
    /**
     * methods from Profile interface
     */
    
    public byte[] getProfile() {
	return contents;
    }
    
    public int getLength() {
	return (byte)contents.length;
    }

}
