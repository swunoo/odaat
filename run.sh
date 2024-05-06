#!/bin/bash

cd server/odaat-server
sbt -Djline.terminal=jline.UnsupportedTerminal run &

cd ../..
cd webapp
npm run dev