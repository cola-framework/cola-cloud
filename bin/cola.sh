#!/bin/sh

TR_BIN="/usr/bin/tr"
if [ ! -x "$TR_BIN" ]
then
    TR_BIN="/bin/tr"
    if [ ! -x "$TR_BIN" ]
    then
        eval echo `gettext 'Unable to locate "tr".'`
        eval echo `gettext 'Please report this message along with the location of the command on your system.'`
        exit 1
    fi
fi

# Resolve the os
DIST_OS=`uname -s | $TR_BIN "[A-Z]" "[a-z]" | $TR_BIN -d ' '`
case "$DIST_OS" in
    'sunos')
        DIST_OS="solaris"
        ;;
    'hp-ux' | 'hp-ux64')
        # HP-UX needs the XPG4 version of ps (for -o args)
        DIST_OS="hpux"
        UNIX95=""
        export UNIX95
        ;;
    'darwin')
        DIST_OS="macosx"
        ;;
    'unix_sv')
        DIST_OS="unixware"
        ;;
    'os/390')
        DIST_OS="zos"
        ;;
esac

# OSX always places Java in the same location so we can reliably set JAVA_HOME
if [ "$DIST_OS" = "macosx" ]
then
    if [ -z "$JAVA_HOME" ]; then
        if [ -x /usr/libexec/java_home ]; then
            JAVA_HOME=`/usr/libexec/java_home`; export JAVA_HOME
        else
            JAVA_HOME="/Library/Java/Home"; export JAVA_HOME
        fi
    fi
fi

