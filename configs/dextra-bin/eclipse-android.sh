#!/bin/bash -e

($HOME/opt/eclipse-android/eclipse -data $HOME/workspace-android -vmargs -Xms512m -Xmx512m -XX:PermSize=128m -XX:MaxPermSize=128m 1> $HOME/eclipse-android.log 2>&1 &)
 
