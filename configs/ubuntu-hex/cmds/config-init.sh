#!/bin/bash -xe

[[ "x$UID" != "x0" ]]

rm -rf target || true
mkdir target
cp -R config target
