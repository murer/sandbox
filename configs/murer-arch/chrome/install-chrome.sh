#!/bin/bash -xe

basedir="$(dirname "$0")"
cd "$basedir"

rm -rf target || true
mkdir target

wget 'https://aur.archlinux.org/cgit/aur.git/snapshot/google-chrome.tar.gz' -O 'target/google-chrome.tar.gz'
