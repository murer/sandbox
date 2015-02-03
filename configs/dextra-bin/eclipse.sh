#!/bin/bash -e

($HOME/opt/eclipse/eclipse -vmargs -Xms512m -Xmx512m -XX:PermSize=128m -XX:MaxPermSize=128m 1> $HOME/eclipse.log 2>&1 &)
 
