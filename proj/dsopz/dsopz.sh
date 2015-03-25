#!/bin/bash -xe

MAIN="$1"

shift

java -cp target/dsopz-dist-single.jar "com.murerz.dsopz.ds.main.$MAIN" $*

