#!/bin/bash -xe

./src/build/build.sh bootstrap
./src/build/build.sh customize
./src/build/build.sh cleanup

./src/cdrom/cdrom_build.sh
