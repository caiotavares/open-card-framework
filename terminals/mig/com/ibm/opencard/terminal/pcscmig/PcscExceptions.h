/*
 * @License IBM
 */

/*
 * Author:  Stephan Breideneich (sbreiden@de.ibm.com)
 * Version: $Id: PcscExceptions.h,v 1.1.1.1 2004/09/09 13:55:13 dirkhusemann Exp $
 */

#include <jni.h>

#define PCSC_EXCEPTION_CLASS "com/ibm/opencard/terminal/pcscmig/PcscException"


/*
 * throwPcscException
 *
 * throws an PcscException with the return code informations of the PC/SC
 *
 * return: 0 for success, other values for exception occured
 */
int throwPcscException(JNIEnv *env, jobject obj, const char *method, const char *msg, long returnCode);

// $Log: PcscExceptions.h,v $
// Revision 1.1.1.1  2004/09/09 13:55:13  dirkhusemann
// importing OCF 1.2
//
// Revision 1.2  1998/04/20 13:11:04  breid
// PackagePath corrected
//
