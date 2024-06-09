#!/bin/bash

cd scala-server/odaat-server
sbt -Djline.terminal=jline.UnsupportedTerminal run &

cd ../..
cd webapp
npm run dev