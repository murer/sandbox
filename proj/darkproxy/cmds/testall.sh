#!/bin/bash -xe

node \
  -r ./test/SimpleTest.js \
  -r ./test/HttpServerTest.js \
  test/TestStart.js

  # -r ./test/EchoDeamonTest.js \
  #
